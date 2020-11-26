package com.tingyu.venus.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tingyu.venus.R;
import com.tingyu.venus.model.Model;
import com.tingyu.venus.model.entity.InvitationInfo;
import com.tingyu.venus.model.entity.UserInfo;
import com.tingyu.venus.netty.protobuf.ContactAddNotice;
import com.tingyu.venus.netty.util.ContactMessageUtil;
import com.tingyu.venus.utils.Constants;

/**
 * 添加联系人
 */
public class ContactAddActivity extends AppCompatActivity {

    private TextView tv_contact_find;
    private EditText et_contact_find;
    private TextView tv_contact_result;
    private Button btn_contact_add;
    private UserInfo contact;

    private SearchView searchView;
    private LinearLayout ll_result;
    private EditText et_contact_add_reason;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        getSupportActionBar().setDisplayShowTitleEnabled(false);


        //初始化视图
        initView();
        //初始化监听
        initListener();
    }

    private void initListener() {


        //添加联系人查找单击事件
        tv_contact_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String contactPhone = et_contact_find.getText().toString(); //获取查找的联系人手机号
                if (contactPhone == null || TextUtils.isEmpty(contactPhone)) {
                    Toast.makeText(ContactAddActivity.this, "请输入联系人手机号", Toast.LENGTH_LONG).show();
                } else {
                    //显示查询结果
                    ll_result.setVisibility(View.VISIBLE);
                    //先在本地数据库查询是否为已存在的联系人
                    UserInfo userInfo = Model.getInstance().dbManager().getContactDao().getContactByPhone(contactPhone);

                    if (userInfo != null) {
                        //联系人已存在
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.USER_PHONE, userInfo.getPhone()); //封装参数，跳转联系人详情页面
                        Intent intent = new Intent(ContactAddActivity.this, ContactActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        return;
                    }


                    //异步请求服务器
                    contact = Model.getInstance().contactManager().asyncGetContactFromServer(contactPhone);
                    Log.d("add contact", contact.toString());
                    if (contact != null) {
                        tv_contact_result.setText(contact.getUsername());
                    } else {
                        tv_contact_result.setText("用户不存在");
                    }

                    //
                }

            }
        });

        //添加联系人到通讯录
        btn_contact_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (contact != null) {
                    //封装邀请信息
                    UserInfo currentUser = Model.getInstance().getUserDao().getCurrentUser();
                    Log.d("current user", currentUser.toString());
                    InvitationInfo invitationInfo = new InvitationInfo();
                    invitationInfo.setUser(currentUser);
                    invitationInfo.setStatus(InvitationInfo.InvitationStatus.NEW_INVITE); //新的邀请 可以不用指定，该类型只是为了区分本地数据库显示
                    invitationInfo.setReason(et_contact_add_reason.getText().toString());
                    //发送消息到服务器，由服务器转发给目标联系人
                    ContactMessageUtil.sendMessage(invitationInfo, contact, ContactAddNotice.ContactAddNoticeMessage.InvitationStatus.INVITE_NEW);
                    //跳转联系人页面
                    Intent intent = new Intent(ContactAddActivity.this, MainActivity.class);
                    startActivity(intent);
                    //关闭当前页
                    finish();
                }
            }
        });
        //搜索好友的

    }


    private void initView() {
        tv_contact_find = findViewById(R.id.tv_contact_find);
        et_contact_find = findViewById(R.id.et_contact_find);
        tv_contact_result = findViewById(R.id.tv_contact_result);
        btn_contact_add = findViewById(R.id.btn_contact_add);
        searchView = findViewById(R.id.menu_contact_add);
        ll_result = findViewById(R.id.ll_result);
        et_contact_add_reason = findViewById(R.id.et_contact_add_reason);
        //设置标题
        setTitle("添加联系人");
        ll_result.setVisibility(View.GONE);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contact_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Toast.makeText(ContactAddActivity.this, item.getTitle(), Toast.LENGTH_LONG).show();
        return super.onOptionsItemSelected(item);
    }
}
