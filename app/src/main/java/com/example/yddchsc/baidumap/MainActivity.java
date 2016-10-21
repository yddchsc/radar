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
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextureMapView mMapView = null;
    Button friends;

    private ToggleButton refresh;
    public static String long_lang = "0.000/0.0";

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
        mLocClient = new LocationClient(getApplicationContext());
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();

        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);

        mLocClient.setLocOption(option);
        mLocClient.start();

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
    private void addmaker(){
        File file = new File(MainActivity.this);
        List<friend> datas = (List<friend>) file.getObject("friends.dat");  //因为一条短信有字数限制，因此要将长短信拆分
        for(friend bean:datas){
            if(bean.getText("long_lang")!=null) {
                String loc[] = bean.getText("long_lang").split("/");
                //定义Maker坐标点
                LatLng p1 = new LatLng(Double.parseDouble(loc[0]),Double.parseDouble(loc[1]));
                //构建Marker图标
                BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.friend_marker);
                //构建MarkerOption，用于在地图上添加Marker
                OverlayOptions option0 = new MarkerOptions().position(p1).icon(bitmap);
                //在地图上添加Marker，并显示
                mBaiduMap.addOverlay(option0);

                String loc1[] = long_lang.split("/");

                Double lan = Double.parseDouble(loc1[0]);
                Double lon = Double.parseDouble(loc1[1]);

                LatLng p2 = new LatLng(lan,lon);
                List<LatLng> points = new ArrayList<LatLng>();
                points.add(p1);
                points.add(p2);
                OverlayOptions ooPolyline = new PolylineOptions().width(5).color(0xAAFF0000).points(points);
                mBaiduMap.addOverlay(ooPolyline);

                //double cc= Distance(Double.parseDouble(loc[0]),Double.parseDouble(loc[1]),Double.parseDouble(loc1[0]),Double.parseDouble(loc1[1]));
            }
        }
    }

    public Double Distance(double lat1, double lng1,double lat2, double lng2) {

        Double R = 6370996.81;  //地球的半径
        /*
         * 获取两点间x,y轴之间的距离
         */
        Double x = (lng2 - lng1)*Math.PI*R*Math.cos(((lat1+lat2)/2)*Math.PI/180)/180;
        Double y = (lat2 - lat1)*Math.PI*R/180;

        Double distance = Math.hypot(x, y);   //得到两点之间的直线距离

        return   distance;
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

        @Override
        public void onReceiveLocation(BDLocation location) {

            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            if (long_lang != Double.toString(location.getLatitude()) + "/" + Double.toString(location.getLongitude()))
            {
                long_lang = Double.toString(location.getLatitude()) + "/" + Double.toString(location.getLongitude());
                mBaiduMap.clear();
                addmaker();
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
