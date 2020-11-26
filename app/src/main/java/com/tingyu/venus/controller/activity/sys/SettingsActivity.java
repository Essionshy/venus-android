package com.tingyu.venus.controller.activity.sys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tingyu.venus.R;
import com.tingyu.venus.controller.activity.login.LoginActivity;
import com.tingyu.venus.model.Model;
import com.tingyu.venus.netty.NettyClientConnectUtil;
import com.tingyu.venus.netty.handler.socket.UserOfflineNoticeHandler;
import com.tingyu.venus.netty.protobuf.UserOfflineNotice;
import com.tingyu.venus.utils.SpUtils;

public class SettingsActivity extends AppCompatActivity {


    private TextView tv_setting_logout;

    private TextView tv_settings_chat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        initView();

        initListener();


    }

    /**
     * 初始化事件监听器
     */
    private void initListener() {

        /**
         * 清空聊天记录
         */
        tv_settings_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清空所有聊天记录，包括个人和群组
                Model.getInstance().dbManager().getChatRecordDao().removeAll();
                Toast.makeText(SettingsActivity.this, "删除聊天记录完成", Toast.LENGTH_LONG).show();


            }
        });

        /**
         * 退出当前账号事件
         */
        tv_setting_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清除当前账号的登录数据
//                SpUtils.getInstance().
//                SharedPreferences sp=getSharedPreferences("loginUserInfo",MODE_PRIVATE);
//                SharedPreferences.Editor edit = sp.edit();
//                edit.remove("phone");
//                edit.remove("password");
//                edit.commit();
                //关闭数据库资源

                releaseDB();


                UserOfflineNotice.UserOfflineNoticeMessage.Builder builder = UserOfflineNotice.UserOfflineNoticeMessage.newBuilder();
                builder.setReason(UserOfflineNotice.UserOfflineNoticeMessage.Reason.LOGIN_OUT);
                builder.setUserPhone(SpUtils.getInstance().getString(SpUtils.CURRENT_USER_ID, "100000"));
                UserOfflineNotice.UserOfflineNoticeMessage message = builder.build();

                //通知下线
                NettyClientConnectUtil.connect(new UserOfflineNoticeHandler(message));


                //跳转到登录页面
                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void initView() {
        tv_setting_logout = findViewById(R.id.tv_setting_logout);
        tv_settings_chat = findViewById(R.id.tv_settings_chat);
    }

    /**
     * 释放数据库资源
     */
    private void releaseDB() {
        Model.getInstance().dbManager().close();
    }


}
