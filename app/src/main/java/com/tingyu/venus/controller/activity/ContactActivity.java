package com.tingyu.venus.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tingyu.venus.R;
import com.tingyu.venus.model.Model;
import com.tingyu.venus.model.entity.UserInfo;
import com.tingyu.venus.utils.Constants;

/**
 * 联系人信息展示页面
 */
public class ContactActivity extends AppCompatActivity {

    private Button btn_contact_send;
    private ImageView iv_contact_avatar;
    private String contactId;
    private TextView tv_contact_username;
    private TextView tv_contact_phone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        //初始化数据
        initView();
        initListener();
        initData();
    }

    private void initData() {

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        contactId = bundle.getString(Constants.USER_PHONE);

        //根据联系人ID查询本地数据库获取联系信息
        UserInfo userInfo = Model.getInstance().dbManager().getContactDao().getContactByPhone(contactId);
        tv_contact_username.setText(userInfo.getUsername());
        tv_contact_phone.setText(userInfo.getPhone());
        //展示联系信息
        Log.d("contact==",userInfo.toString());
    }

    /**
     * 初始化事件
     */
    private void initListener() {
        btn_contact_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转聊天会话页面
                Intent intent = new Intent(ContactActivity.this, ChatActivity.class);

                Bundle bundle=new Bundle();
                bundle.putString(Constants.USER_PHONE,contactId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    private void initView() {
        iv_contact_avatar = findViewById(R.id.iv_contact_avatar);
        btn_contact_send = findViewById(R.id.btn_contact_send);
        tv_contact_username = findViewById(R.id.tv_contact_nickname);
        tv_contact_phone = findViewById(R.id.tv_contact_phone);
    }
}
