package com.tingyu.venus;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.tingyu.venus.model.EventListener;
import com.tingyu.venus.model.Model;
import com.tingyu.venus.service.HeartbeatService;

/**
 * 集成第三方SDK,全局类初始化在此类中进行初始化配置，需要在AndroidManifest.xml文件中的《application>标签下面关联该类
 */
public class BootstrapApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化
        Log.d("BootstrapApplication", "应用程序启动完成");
        //在LocationActivity视图加载之前初始化百度地图
        // SDKInitializer.initialize(this);

        //初始化全局数据处理类
        Model.getInstance().init(this);
        //初始化全局事件监听处理器
        EventListener eventListener = new EventListener(this);


    }
}
