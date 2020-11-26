package com.tingyu.venus.controller.activity.lottery;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tingyu.venus.R;
import com.tingyu.venus.service.LotteryService;

import java.util.List;

public class LotteryActivity extends AppCompatActivity {

    //声明自定义Service对象
    private LotteryService lotteryService;
    int[] items = {R.id.tv_item_01, R.id.tv_item_02, R.id.tv_item_03, R.id.tv_item_04, R.id.tv_item_05, R.id.tv_item_06, R.id.tv_item_07};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery);
        //获取
        Button lotteryButton = (Button) findViewById(R.id.btn_choose);
        lotteryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != lotteryService) {
                    List<String> randomNumber = lotteryService.getRandomNumber();
                    for (int i = 0; i < randomNumber.size(); i++) {
                        TextView tv = (TextView) findViewById(items[i]);
                        tv.setText(randomNumber.get(i).toString());//为文本框设置值
                    }
                } else {
                    Log.e("LotteryService", "抽奖系统错误");
                }

            }
        });
    }

    /**
     * 创建ServiceConnection对象
     */
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("ServiceConnection", "服务绑定成功");
            //通过service获取实际的LotteryService对象
            lotteryService = ((LotteryService.LotteryBinder) service).getService();
        }

        /**
         * Service与Activity断开连接
         * @param name
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("ServiceConnection", "服务断开连接");

        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, LotteryService.class);
        bindService(intent, conn, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //解绑
        unbindService(conn);
    }
}
