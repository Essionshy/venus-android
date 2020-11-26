package com.tingyu.venus.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.tingyu.venus.model.Model;
import com.tingyu.venus.utils.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.WebSocket;

public class HeartbeatService extends Service {

    private static final long HEARTBEAT_RATE = 30L;//每隔30

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Model.getInstance().getExecutor().execute(() -> {
            while (true) {
                WebSocket webSocket = Model.getInstance().getWebSocketManager().getWebSocket(); //获取当前WebSocket对象
                try {
                    TimeUnit.SECONDS.sleep(HEARTBEAT_RATE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //异步执行心跳检测

                boolean isSuccess = webSocket.send(Constants.HEARTBEAT_NOTICE);
                if (!isSuccess) {
                    Log.d("heartbeat", "客户端与服务器 WebSocket 断开连接...");
                    //重
                    webSocket.cancel(); //关闭之前的WebSocket连接
                    //重新发送
                    Model.getInstance().getWebSocketManager().reconnect();

                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Model.getInstance().getWebSocketManager().getWebSocket().close(1000, "应用关闭");
    }
}
