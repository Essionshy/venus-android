package com.tingyu.venus.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.tingyu.venus.listener.DispatcherWebSocketListener;
import com.tingyu.venus.utils.Constants;
import com.tingyu.venus.utils.OkHttpClientUtils;
import com.tingyu.venus.utils.threadpool.SingleThreadPoolExecutor;

import java.util.List;

public class MessageWebSocketService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("MessageService", "StartCommand MessageService...");
        //websocket连接服务器
        SingleThreadPoolExecutor.newInstance().execute(()->{

            Log.d("MessageService",Thread.currentThread().getName()+"与服务器建立 websocket 连接");


            OkHttpClientUtils.ws(Constants.WS_BASE_URL+"/msg",new DispatcherWebSocketListener());



          //  NettyClientConnectUtil.connect(new UserAuthenticationHandler());
            //NettyClientConnectUtil.connect(new ContactChangedHandler());

        });


        return super.onStartCommand(intent, flags, startId);
    }

    //判断Service是否正常运行
    public boolean isRunning() {
        //获取ActivityManger
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        //获取所有正在运行的Service
        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(50);
        for (int i = 0; i < runningServices.size(); i++) {
            if (runningServices.get(i).service.getClassName().toString().equals("com.tingyu.venus.service.MessageService"))
                return true;
        }
        return false;
    }
}
