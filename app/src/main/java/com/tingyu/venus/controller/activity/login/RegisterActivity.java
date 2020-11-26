package com.tingyu.venus.controller.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tingyu.venus.R;
import com.tingyu.venus.utils.Constants;
import com.tingyu.venus.utils.OkHttpCallback;
import com.tingyu.venus.utils.OkHttpClientUtils;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {

    private Button btnRegister;
    private EditText etUsername;
    private EditText etPhone;
    private EditText etPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //绑定视图
        setContentView(R.layout.activity_register);
        initView();
        //提交注册
        initListener();


    }

    private void initListener() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取参数值
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String phone = etPhone.getText().toString();
                //封装请求参数
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                params.put("phone", phone);
                OkHttpClientUtils.post(Constants.HTTP_BASE_URL + "/client/register", params, new OkHttpCallback() {
                    @Override
                    protected void onSuccess() {
                        runOnUiThread(() -> {
                            Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                            //跳转登录
                            Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(loginIntent);
                            finish();
                        });
                    }

                    @Override
                    protected void onError(String message) {
                        runOnUiThread(() -> {
                            Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();

                        });
                    }
                });
            }
        });

    }

    private void initView() {
        btnRegister = (Button) findViewById(R.id.btn_login_register);
        etUsername = (EditText) findViewById(R.id.et_username);
        etPhone = (EditText) findViewById(R.id.et_phone);
        etPassword = (EditText) findViewById(R.id.et_password);
    }

}
