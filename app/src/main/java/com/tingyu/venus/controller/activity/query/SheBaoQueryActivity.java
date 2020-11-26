package com.tingyu.venus.controller.activity.query;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.tingyu.venus.R;
import com.tingyu.venus.task.GetAreaDataTask;
import com.tingyu.venus.task.GetCityDataTask;
import com.tingyu.venus.task.GetSheBaoServiceOutletsDataTask;
import com.tingyu.venus.utils.threadpool.SingleThreadPoolExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class SheBaoQueryActivity extends AppCompatActivity {

    private Button btnQuery;
    private Spinner spinnerCity;
    private Spinner spinnerArea;
    private TextView tvResult;
    private SingleThreadPoolExecutor threadPoolExecutor = SingleThreadPoolExecutor.newInstance();


    @Override
    protected void onStart() {
        super.onStart();
        //加载地级市数据


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shebao);
        btnQuery = findViewById(R.id.btn_query);
        spinnerCity = (Spinner) findViewById(R.id.spinner_city);
        spinnerArea = (Spinner) findViewById(R.id.spinner_area);
        tvResult = (TextView) findViewById(R.id.tv_result);

        //创建层级导航
        if(NavUtils.getParentActivityName(SheBaoQueryActivity.this)!=null){
            //设置显示向左的导航图标
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        CountDownLatch countDownLatch = new CountDownLatch(1);
        FutureTask<List<String>> futureTask = new FutureTask<List<String>>(new GetCityDataTask(countDownLatch));


        threadPoolExecutor.execute(futureTask);


        try {
            countDownLatch.await();
            //  TimeUnit.SECONDS.sleep(10);
            List<String> cityList = new ArrayList<>();
            cityList = futureTask.get();
            Log.d("cityList", cityList.toString());
            Log.d("cityList", String.valueOf(cityList.size()));
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cityList);
            //设置适配器的下拉框样式
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

            spinnerCity.setAdapter(adapter);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        /**
         * 渲染地级市下拉列表框
         */

        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //清空
                spinnerArea.setAdapter(null);

                String cityName = parent.getItemAtPosition(position).toString();
                Toast.makeText(SheBaoQueryActivity.this, cityName, Toast.LENGTH_SHORT).show();
                CountDownLatch areaCountDownLatch = new CountDownLatch(1);
                //根据选择的城市名称加载下辖区县列表
                FutureTask<List<String>> areaFutureTask = new FutureTask<>(new GetAreaDataTask(cityName, areaCountDownLatch));

                threadPoolExecutor.execute(areaFutureTask);

                try {

                    areaCountDownLatch.await();
                    Log.d("test end", String.valueOf(areaFutureTask.isDone()));
                    List<String> areaList = areaFutureTask.get();
                    Log.d("areaList", areaList.toString());
                    //创建适配器
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(SheBaoQueryActivity.this, android.R.layout.simple_spinner_item, areaList);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerArea.setAdapter(arrayAdapter);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        /**
         * 查询按钮监听器
         */
        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //提交查询
                String areaName = spinnerArea.getSelectedItem().toString();

                Toast.makeText(SheBaoQueryActivity.this, areaName, Toast.LENGTH_SHORT).show();
                CountDownLatch sheBaoCountDownLatch = new CountDownLatch(1);

                FutureTask<List<Map<String, String>>> sheBaoFutureTask = new FutureTask<>(new GetSheBaoServiceOutletsDataTask(areaName, sheBaoCountDownLatch));

                threadPoolExecutor.execute(sheBaoFutureTask);
                //如果sheBaoTuturetask没有执行完成，main线程自旋

                try {
                    sheBaoCountDownLatch.await();
                    List<Map<String, String>> addressList = sheBaoFutureTask.get();
                    Log.d("addressList", addressList.toString());

                    StringBuilder sb = new StringBuilder();
                    for (Map<String, String> map : addressList) {
                        sb.append("网点名称：").append(map.get("outlets_name")).append("\n")
                                .append("网点地址：").append(map.get("outlets_address")).append("\n");

                    }

                    Log.d("sb ", sb.toString());
                    tvResult.setText(sb.toString());
                    Log.d("this", tvResult.getText().toString());


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("SheBaoQueryActivity", "调用stop()");
//        if (!threadPoolExecutor.isShutdown()) {
//            threadPoolExecutor.shutdown();
//        }
    }
}
