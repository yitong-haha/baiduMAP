package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
//
//import com.baidu.location.BDLocation;
//import com.baidu.location.BDLocationListener;
//import com.baidu.location.LocationClient;
//import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public LocationClient mLocationClient;
    private TextView positionText;
    private MapView bdmapView;
    private BaiduMap baiduMap;
    private boolean isFirstLocate=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient= new LocationClient(getApplicationContext());
        //mLocationClient.registerLocationListener(new MyLocationListener());
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        bdmapView=(MapView)findViewById(R.id.bdmapView);
        baiduMap=bdmapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        positionText=(TextView) findViewById(R.id.position_text_view);//定位文字信息（已隐藏）
        List<String> peimissionList=new ArrayList<>();
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
            peimissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED)
            peimissionList.add(Manifest.permission.READ_PHONE_STATE);
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
            peimissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(!peimissionList.isEmpty())
        {
            String[] permissions=peimissionList.toArray(new String[peimissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this,permissions,1);
        }
        else
            requestLocation();
    }
//    private void initLocation(){
//        LocationClientOption option=new LocationClientOption();
//        option.setScanSpan(1000);
//        //每秒钟更新一下当前位置
//       /* option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//        //强制指定使用高精确度模式定位*/
//        option.setIsNeedAddress(true);
//        mLocationClient.setLocOption(option);
//    }
    private void requestLocation()
    {
        LocationManager locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if(!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            // 未打开位置开关，可能导致定位失败或定位不准，提示用户或做相应处理
            Toast.makeText(MainActivity.this,"建议您打开位置开关哦~",Toast.LENGTH_LONG).show();
        }
        initLocation();
        mLocationClient.start();
    }

    private void initLocation() {
    }


    @Override
    protected void onResume() {
        super.onResume();
        bdmapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bdmapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        bdmapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0)
                {for(int result : grantResults)
                    if(result!=PackageManager.PERMISSION_GRANTED)
                    {Toast.makeText(this,"必须同意所有权限才能使用本程序",Toast.LENGTH_SHORT).show();
                        finish();
                        return;}
                    requestLocation();}
                else
                {Toast.makeText(this,"发生未知错误",Toast.LENGTH_SHORT).show();
                    finish();}
                break;
            default:
        }
    }

    private class LocationClient {
        public LocationClient(Context applicationContext) {
        }

        public void start() {
        }

        public void stop() {
        }
    }

//    private void navigateTo(BDLocation location){
//        if(isFirstLocate)
//        {
//            LatLng ll=new LatLng(location.getLatitude(),location.getLongitude());
//            MapStatusUpdate mUpdate= MapStatusUpdateFactory.newLatLng(ll);
//            baiduMap.animateMapStatus(mUpdate);
//            mUpdate=MapStatusUpdateFactory.zoomTo(16f);
//            baiduMap.animateMapStatus(mUpdate);
//            isFirstLocate=false;
//        }
//        MyLocationData.Builder locationBuilder=new MyLocationData.Builder();
//        locationBuilder.latitude(location.getLatitude());
//        locationBuilder.longitude(location.getLongitude());
//        MyLocationData myLocationData=locationBuilder.build();
//        baiduMap.setMyLocationData(myLocationData);
//    }
//    public class MyLocationListener implements BDLocationListener{
//        @Override
//        public void onReceiveLocation(BDLocation bdLocation) {
//            StringBuilder currentPosition=new StringBuilder();
//            currentPosition.append("纬度：").append(bdLocation.getLatitude()).append("\n");
//            currentPosition.append("经度：").append(bdLocation.getLongitude()).append("\n");
//            currentPosition.append("国家：").append(bdLocation.getCountry()).append("\n");
//            currentPosition.append("省：").append(bdLocation.getProvince()).append("\n");
//            currentPosition.append("市：").append(bdLocation.getCity()).append("\n");
//            currentPosition.append("区：").append(bdLocation.getDistrict()).append("\n");
//            currentPosition.append("街道：").append(bdLocation.getStreet()).append("\n");
//            currentPosition.append("定位方式：");
//            if(bdLocation.getLocType()==BDLocation.TypeGpsLocation)
//                currentPosition.append("GPS");
//            else if(bdLocation.getLocType()==BDLocation.TypeNetWorkLocation)
//                currentPosition.append("网络定位");
//            positionText.setText(currentPosition);
//            //定位文字信息（已隐藏）
//            if(bdLocation.getLocType()==BDLocation.TypeGpsLocation||bdLocation.getLocType()==BDLocation.TypeNetWorkLocation)
//                navigateTo(bdLocation);
//        }
//    }
}

