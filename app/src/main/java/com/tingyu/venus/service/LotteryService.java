package com.tingyu.venus.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LotteryService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new LotteryBinder();
    }

    //自定义Binder
    public class LotteryBinder extends Binder{
        //自定义获取Service方法；Activity通过Binder对象获取当前Service对象
        public LotteryService getService(){
            return LotteryService.this;
        }
    }

    //自定义生成随机数集合
    public List<String> getRandomNumber(){
        List<String> result=new ArrayList<>(7);
        String value="";
        for (int i = 0; i <7 ; i++) {
            int num=new Random().nextInt(33)+1;
            //格式化显示2位数字
            if(num<10){
                value="0"+String.valueOf(num);
            }else{
                value=String.valueOf(num);
            }
            result.add(value);
        }
        return result;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
