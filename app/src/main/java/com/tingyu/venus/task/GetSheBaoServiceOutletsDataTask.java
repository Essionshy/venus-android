package com.tingyu.venus.task;

import android.util.Log;

import com.tingyu.venus.utils.Constants;
import com.tingyu.venus.utils.OkHttpClientUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetSheBaoServiceOutletsDataTask implements Callable<List<Map<String,String>>> {

    private String areaName;

    private CountDownLatch countDownLatch;


    public GetSheBaoServiceOutletsDataTask(String areaName, CountDownLatch countDownLatch) {
        this.areaName = areaName;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public List<Map<String,String>> call() throws Exception {
        Log.d("GetSheBaoDataTask",Thread.currentThread().getName()+"\t start successfully...");
        List<Map<String,String>> result = new ArrayList<>();
        //发送get请求社保网点信息列表
        OkHttpClientUtils.get(Constants.HTTP_BASE_URL + "/client/shebao/outlets/" + this.areaName, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String responseBody = response.body().string();
                    Log.d("response data", responseBody);
                    JSONObject jsonObject = new JSONObject(responseBody);

                    String data = jsonObject.getString("data");
                    JSONObject jsonData = new JSONObject(data);
                    JSONArray cityList = jsonData.getJSONArray("items");
                    for (int i = 0; i < cityList.length(); i++) {
                        JSONObject obj = (JSONObject) cityList.get(i);
                        // Log.d("cityName",obj.getString("cityName"));
                        Map<String,String> map=new HashMap<>();
                        map.put("outlets_name",obj.getString("outletsName"));
                        map.put("outlets_address",obj.getString("outletsAddress"));
                        result.add(map);
                    }
                    countDownLatch.countDown();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        return result;
    }
}
