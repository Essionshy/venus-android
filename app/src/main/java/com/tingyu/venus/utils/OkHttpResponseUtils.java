package com.tingyu.venus.utils;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * okhttp请求响应
 */
public final class OkHttpResponseUtils {


    /**
     * 根据请求返回结果集  CommonResult(int code,String message,Map<String,Object> data  )进行解析
     * <p>
     * 说明：返回结果集 key 统一为 items
     *
     * @param call
     * @param response
     * @param items    返回集合的map的key名称
     * @param key      返回列表字段名称
     * @return
     * @throws IOException
     */
    public static List<String> resolve(Call call, Response response, String items, String key) throws Exception {
        List<String> result = new ArrayList<>();
        String body = response.body().string();
        Log.d("response data", body);
        JSONObject jsonObject = new JSONObject(body);
        String data = jsonObject.getString("data");
        JSONObject jsonData = new JSONObject(data);
        JSONArray jsonItems = jsonData.getJSONArray(items);
        for (int i = 0; i < jsonItems.length(); i++) {
            JSONObject obj = (JSONObject) jsonItems.get(i);
            result.add(obj.getString(key));
        }
        return result;
    }
}
