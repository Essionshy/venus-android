package com.tingyu.venus.controller.activity.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tingyu.venus.R;

/**
 * 光线传感器
 */
public class LightSensorActivity extends AppCompatActivity implements SensorEventListener {

    TextView textView;

    private SensorManager sensorManager;//声明传感器


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);
        //获取传感器管理器对象 SensorManager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        textView =(TextView) findViewById(R.id.tv_light);

    }

    /**
     * 监听传感器值的变化
     *
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        //1.获取传感器的值
        float[] values = event.values;
        //2.获取传感器类型
        int type = event.sensor.getType();

        //3.创建StringBuilder对象构建数据
        StringBuilder builder=null;
        //4.判断当前传感器是否为光线传感器
        if(type==Sensor.TYPE_LIGHT){
            builder=new StringBuilder();
            builder.append("光线强度值：");
            builder.append(values[0]);//values中第一个值为光强度值
            //将构建好的信息显示到文本域中
            textView.setText(builder.toString());
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * 在此方法中注册传感器监听器
     */
    @Override
    protected void onResume() {
        super.onResume();
        //SensorManager.SENSOR_DELAY_GAME 指定传感器的频率
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //取消监听器
        sensorManager.unregisterListener(this);
    }
}
