package com.tingyu.venus.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public abstract class OkHttpCallback implements Callback {
    @Override
    public void onFailure(Call call, IOException e) {
        onError(e.getMessage());
    }



    @Override
    public void onResponse(Call call, Response response) throws IOException {

        //解析响应数据

        try {
            String body = response.body().string();
            JSONObject jsonObject = new JSONObject(body);
            String message = jsonObject.getString("message");
            boolean success = jsonObject.getBoolean("success");
            if(success){
                onSuccess();
            }else{
                onError(message);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    protected abstract void onSuccess();

    protected abstract void onError(String message);
}
