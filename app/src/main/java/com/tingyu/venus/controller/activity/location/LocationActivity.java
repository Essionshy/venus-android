package com.tingyu.venus.controller.activity.location;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.tingyu.venus.R;

public class LocationActivity extends AppCompatActivity {

    private MapView mapView;//声明百度地图视图对象
    private BaiduMap baiduMap;//声明百度地图对象
    private boolean isFirstLocation = true;
    private MyLocationConfiguration.LocationMode locationMode;//设置定位模式

    /***************************MapView生命周期与Activity生命周期绑定************************************/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_location);
        //获取MapView
        mapView = (MapView) findViewById(R.id.mv);
        baiduMap=mapView.getMap();//获取baiduMap对象

        /*获取位置管理器*/
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        LocationProvider locationProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);


        //权限检查
        //从GPS获取最新的定位信息
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        //设置位置监听器  minTimeMs: 表示位置更新间隔时间，单位毫秒； minDistanceM 位置间隔，单位米

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, new LocationListener() {
            /**
             * 位置发生改变时触发
             * @param location
             */
            @Override
            public void onLocationChanged(@NonNull Location location) {
                locationUpdate(location); //可以将该方法写在一个工具类中
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }
        });
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locationUpdate(lastKnownLocation);
    }

    /**
     * 更新位置数据
     *
     * @param location
     */
    public void locationUpdate(Location location) {
        //获取用户当前位置的经纬度信息
        if (null != location) {
            //LatLng对象为com.baidu.map封装经纬度的对象
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            Log.i("Location", "纬度：" + location.getLatitude() + "经度：" + location.getLongitude());
            //判断是否为首次定位

            if (isFirstLocation) {
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);//更新坐标位置
                baiduMap.animateMapStatus(mapStatusUpdate);
                isFirstLocation = false;
            }
            //创建MyLocationData对象保存当前所在位置数据
            MyLocationData myLocationData = new MyLocationData.Builder()
                    .accuracy(location.getAccuracy())
                    .direction(100)//设置方向信息 值为（0，360）
                    .latitude(location.getLatitude()) //设置纬度坐标
                    .longitude(location.getLongitude()) //设置经度坐标
                    .build();
            baiduMap.setMyLocationData(myLocationData);
            //设置自定义定位图标
            BitmapDescriptor bitmapDescriptor= BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_background);//加载自定义图标资源
            locationMode=MyLocationConfiguration.LocationMode.NORMAL;//指定定位模式
            MyLocationConfiguration configuration=new MyLocationConfiguration(locationMode,true,bitmapDescriptor);
            baiduMap.setMyLocationConfiguration(configuration);

        } else {
            Log.i("Location", "没有获取到GPS信息，请检查是否开启GPS访问权限");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        mapView = null;
    }

    /****/

    @Override
    protected void onStart() {
        super.onStart();
        //启动百度地图定位图层
        baiduMap.setMyLocationEnabled(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //关闭百度地图定位图层
        baiduMap.setMyLocationEnabled(false);
    }
}
