package com.tingyu.venus.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.tingyu.venus.utils.threadpool.SingleThreadPoolExecutor;

import java.util.List;

public class MonitorService extends Service {

    private Handler handler;

    public MonitorService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("MonitorService", "创建MonitorService...");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        SingleThreadPoolExecutor.newInstance().execute(() -> {
            //发送客户端认证


        });

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i("MonitorService", "Destroy MonitorService...");
        super.onDestroy();
    }

    //判断Service是否正常运行
    public boolean isRunning() {
        //获取ActivityManger
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        //获取所有正在运行的Service
        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(50);
        for (int i = 0; i < runningServices.size(); i++) {
            if (runningServices.get(i).service.getClassName().toString().equals("com.tingyu.venus.service.MonitorService"))
                return true;
        }
        return false;
    }


}