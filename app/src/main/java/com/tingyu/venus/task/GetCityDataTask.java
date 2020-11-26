package com.tingyu.venus.task;

import android.util.Log;

import com.tingyu.venus.utils.Constants;
import com.tingyu.venus.utils.OkHttpClientUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 获取城市地址数据
 */
public class GetCityDataTask implements Callable<List<String>> {

    private CountDownLatch countDownLatch;

    public GetCityDataTask(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public List<String> call() throws Exception {
        Log.d("GetCityDataTask",Thread.currentThread().getName()+"\t start successfully...");

        List<String> result=new ArrayList<>();

        OkHttpClientUtils.get(Constants.HTTP_BASE_URL + "/client/address/city/list/510000", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    String responseBody = response.body().string();
                    Log.d("response data",responseBody);
                    JSONObject jsonObject = new JSONObject(responseBody);
                    String data = jsonObject.getString("data");
                    JSONObject jsonData = new JSONObject(data);
                    JSONArray cityList = jsonData.getJSONArray("cityList");
                    for (int i = 0; i <cityList.length() ; i++) {
                        JSONObject obj = (JSONObject) cityList.get(i);
                        // Log.d("cityName",obj.getString("cityName"));
                        result.add(obj.getString("cityName"));
                    }
                    countDownLatch.countDown();
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });
        return result;
    }
}
