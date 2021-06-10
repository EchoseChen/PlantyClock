package com.ecnu.plantyclock.activities;

import android.Manifest;
import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.ecnu.plantyclock.R;
import com.ecnu.plantyclock.service.MusicServer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenLu on 2021/5/21
 * Use for 开启界面
 */
public class MainActivity extends AppCompatActivity{

//    MediaPlayer mp;

    //定位
    public LocationClient mLocationClient;
    String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startAnimation();
        Intent intent = new Intent(MainActivity.this, MusicServer.class);
        startService(intent);
        //背景音
//        mp = MediaPlayer.create(this, R.raw.bg);
//        mp.start();
//        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer arg0) {
//                mp.start();
//                mp.setLooping(true);
//            }
//        });
        ImageView enter_button = (ImageView) findViewById(R.id.enter);

        //定位
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        SDKInitializer.initialize(getApplicationContext());
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(this, permissions, 1);
        } else {
            requestLocation();
        }
        enter_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this, StyleActivity.class);
                intent1.putExtra("city",city);
                startActivity(intent1);
            }
        });
    }


    //放大动画
    private void startAnimation() {
        final View splashIv = findViewById(R.id.start);
        ValueAnimator animator = ValueAnimator.ofObject(new FloatEvaluator(), 1.0f, 1.2f);
        animator.setDuration(3000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                if (value != 1.2f) {
                    splashIv.setScaleX(value);
                    splashIv.setScaleY(value);
                } else {
                }
            }

        });
        animator.start();
    }

    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            city = location.getCity();
            Toast.makeText(getApplicationContext(),"已定位到"+city,Toast.LENGTH_SHORT).show();
//            Log.d("city",city);
        }

    }
    protected void onResume(){
        super.onResume();
        Intent intent = new Intent(MainActivity.this,MusicServer.class);
        startService(intent);
    }
    protected void onStop(){
        super.onStop();
        Intent intent = new Intent(MainActivity.this,MusicServer.class);
        stopService(intent);
    }

}