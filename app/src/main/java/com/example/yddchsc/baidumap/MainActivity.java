package com.example.yddchsc.baidumap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextureMapView mMapView = null;
    Button friends;

    private ToggleButton refresh;
    public static String long_lang;

    LocationClient mLocClient;
    public static BaiduMap mBaiduMap;
    boolean isFirstLoc = true;

    public LocationClient mLocationClient = null;
    public  MyLocationListenner myListener = new MyLocationListenner();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.main);
        mMapView = (TextureMapView) findViewById(R.id.bmapView);

        mBaiduMap = mMapView.getMap();

        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();

        File file = new File(MainActivity.this);
        List<friend> datas = (List<friend>) file.getObject("friends.dat");  //因为一条短信有字数限制，因此要将长短信拆分
        for(friend bean:datas){
            if(bean.getText("long_lang")!=null) {
                String loc[] = bean.getText("long_lang").split("/");
                //定义Maker坐标点
                LatLng point = new LatLng(Double.parseDouble(loc[0]),Double.parseDouble(loc[1]));
                //构建Marker图标
                BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.friend_marker);
                //构建MarkerOption，用于在地图上添加Marker
                OverlayOptions option0 = new MarkerOptions()
                        .position(point)
                        .icon(bitmap);
                //在地图上添加Marker，并显示
                mBaiduMap.addOverlay(option0);
            }
        }

        long_lang = myListener.long_lan;

        friends = (Button) findViewById(R.id.btn_friends);
        friends.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent( MainActivity.this, friendListActivity.class);
                startActivity(intent);
                finish();
            }
        });
        refresh = (ToggleButton) findViewById(R.id.btn_refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SmsManager manager = SmsManager.getDefault();
                File file = new File(MainActivity.this);
                List<friend> datas = (List<friend>) file.getObject("friends.dat");  //因为一条短信有字数限制，因此要将长短信拆分
                for(friend bean:datas){
                    String phone = bean.getText("number");
                    String text = "where are you?";
                    manager.sendTextMessage(phone, null, text, null, null);
                }
                Toast.makeText(getApplicationContext(), "发送完毕", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
    public class MyLocationListenner implements BDLocationListener {

        public String long_lan;

        @Override
        public void onReceiveLocation(BDLocation location) {

            long_lang = Double.toString(location.getLatitude()) + "/" + Double.toString(location.getLongitude());
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

}
