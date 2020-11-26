package com.tingyu.venus.controller.activity.boot;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tingyu.venus.R;
import com.tingyu.venus.controller.activity.MainActivity;
import com.tingyu.venus.controller.activity.login.LoginActivity;
import com.tingyu.venus.model.Model;

public class BootStrapActivity extends AppCompatActivity {

    private Handler handler;

    @Override
    protected void onStart() {
        super.onStart();
       // startService(new Intent(BootStrapActivity.this, MonitorService.class));
       // startService(new Intent(BootStrapActivity.this, HeartbeatService.class));


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bootstrap);

        //线程操作资源类
        setTitle("欢迎页");

        handler = new Handler(BootStrapActivity.this.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                //判断如果当前 Activity已经退出，则不进行处理
                if (isFinishing()) {
                    return;
                }

                //判断是直接跳转首页还是登录面
                toMainOrLogin();

                //关闭当前Activity
                finish();
            }


        };

        //延迟2s后跳转页面
        handler.sendMessageDelayed(handler.obtainMessage(), 2 * 1000L);

    }


    /**
     * 判断跳转目的地
     */
    private void toMainOrLogin() {

        if(Model.getInstance().isLoginedBefore()){
            //调用DBManager
            Model.getInstance().isLoginSuccess(Model.getInstance().getUserDao().getCurrentUser());

            //跳转首页
            startActivity(new Intent(BootStrapActivity.this, MainActivity.class));

        }else{
            startActivity(new Intent(BootStrapActivity.this, LoginActivity.class));

        }

    }

}
