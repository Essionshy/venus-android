package com.tingyu.venus.task;

import android.util.Log;

import com.tingyu.venus.utils.Constants;
import com.tingyu.venus.utils.OkHttpClientUtils;
import com.tingyu.venus.utils.OkHttpResponseUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetAreaDataTask implements Callable<List<String>> {

    private String areaName;

    private CountDownLatch countDownLatch;

    public GetAreaDataTask(String areaName, CountDownLatch countDownLatch) {
        this.areaName = areaName;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public List<String> call() throws Exception {
        Log.d("GetAreaDataTask", Thread.currentThread().getName() + "\t start successfully...");
        List<String> result = new ArrayList<>();


        OkHttpClientUtils.get(Constants.HTTP_BASE_URL + "/client/address/area/list/name/" + this.areaName, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
              /*  String responseBody = response.body().string();
                Log.d("response data",responseBody);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseBody);
                    String data = jsonObject.getString("data");
                    JSONObject jsonData = new JSONObject(data);
                    JSONArray cityList = jsonData.getJSONArray("areaList");
                    for (int i = 0; i < cityList.length(); i++) {
                        JSONObject obj = (JSONObject) cityList.get(i);
                        // Log.d("cityName",obj.getString("cityName"));
                        result.add(obj.getString("areaName"));
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
*/
                try {
                    List<String> list = OkHttpResponseUtils.resolve(call, response, "areaList", "areaName");
                    Iterator<String> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        String next = iterator.next();
                        result.add(next);
                    }
                } catch (Exception ex) {

                    ex.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }


            }
        });


        return result;
    }
}
