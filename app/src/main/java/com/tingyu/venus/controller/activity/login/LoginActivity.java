package com.tingyu.venus.controller.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.tingyu.venus.R;
import com.tingyu.venus.controller.activity.MainActivity;
import com.tingyu.venus.model.Model;
import com.tingyu.venus.model.entity.UserInfo;
import com.tingyu.venus.utils.Constants;
import com.tingyu.venus.utils.OkHttpClientUtils;
import com.tingyu.venus.utils.SpUtils;
import com.tingyu.venus.utils.threadpool.SingleThreadPoolExecutor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private Button btnRegister;
    private EditText et_phone;
    private EditText et_password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //初始化页面
        initView();
        //初始化监听事件
        initListener();
    }

    /**
     * 校验用户输入
     */
    private void validateUsernameAndPassword(String phone, String password) {
        if (phone == null || TextUtils.isEmpty(phone)) {
            Toast.makeText(LoginActivity.this, Constants.USER_PHONE_IS_EMPTY, Toast.LENGTH_LONG).show();
            return;
        }
        if (password == null || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, Constants.USER_PASSWORD_IS_EMPTY, Toast.LENGTH_LONG).show();
            return;
        }
    }


    //处理登录业务逻辑
    private void login() {
        String inputPassword = et_password.getText().toString();
        String inputPhone = et_phone.getText().toString();
        validateUsernameAndPassword(inputPhone, inputPassword);
        //向服务器发起请求，验证用户合法性
        SingleThreadPoolExecutor.newInstance().execute(() -> {
            Map<String, String> params = new HashMap<>();
            params.put("phone", inputPhone);
            params.put("password", inputPassword);

            OkHttpClientUtils.post(Constants.HTTP_BASE_URL + "/client/login", params, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(() -> {
                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_LONG).show();
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String body = response.body().string();
                    JSONObject jsonObject = JSONObject.parseObject(body);
                    String data = jsonObject.getString("data");
                    JSONObject jsonObjectData = JSONObject.parseObject(data);
                    String user = jsonObjectData.getString("user");
                    JSONObject jsonObjectUser = JSONObject.parseObject(user);
                    UserInfo userInfo = new UserInfo();
                    userInfo.setId(jsonObjectUser.getString("phone"));
                    userInfo.setPhone(jsonObjectUser.getString("phone"));
                    userInfo.setUsername(jsonObjectUser.getString("username"));
                    userInfo.setNickname(jsonObjectUser.getString("username"));
                    Model.getInstance().getUserDao().save(userInfo);
                    //
                    Model.getInstance().isLoginSuccess(userInfo);
                    //修改是否登录过的值为 true
                    SpUtils.getInstance().save(SpUtils.IS_LOGINED_BEFORE, true);
                    SpUtils.getInstance().save(SpUtils.CURRENT_USER_ID, inputPhone);
                    //保存当前用户账号信息到本地数据库


                    //调用UI线程处理消息提示
                    runOnUiThread(() -> {
                        //提示
                        Toast.makeText(LoginActivity.this, Constants.USER_LOGIN_SUCCESS, Toast.LENGTH_LONG).show();
                        //跳转主页
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);


                        //关闭当前Activity
                        finish();
                    });

                }

            });


        });
    }

    //初始化注册事件监听
    private void initListener() {
        //监听登录事件
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }

        });




        /*注册*/
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转注册页面
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
                // finish();
            }
        });
    }

    /**
     * 初始化渲染页面
     */
    private void initView() {
        btnLogin = (Button) findViewById(R.id.btn_login_login);
        btnRegister = (Button) findViewById(R.id.btn_login_register);
        et_phone = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
    }
}
