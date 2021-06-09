package com.ecnu.plantyclock.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import com.ecnu.plantyclock.Clock.activity.ClockFragment;
import com.ecnu.plantyclock.R;
import com.ecnu.plantyclock.Weather.WeatherFragment;
import com.ecnu.plantyclock.fragments.BlankFragment;
import com.ecnu.plantyclock.util.leafloading.LeafLoadingActivity;

/**
 * Created by ChenLu on 2021/5/21
 * Use for 加载下框导航
 */
public class StyleActivity extends AppCompatActivity implements WeatherFragment.CallBackInterface{

    public static final String TAG="StyleActivity";


    private ViewPager mViewPager;
    private RadioGroup mTabRadioGroup;

    private List<Fragment> mFragments;
    private FragmentPagerAdapter mAdapter;

    private String weather;
    private String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style2);
        initView();
        Intent intent = getIntent();
        city = intent.getStringExtra("city");
    }

    private void initView() {
        // find view
        mViewPager = findViewById(R.id.fragment_vp);
        mTabRadioGroup = findViewById(R.id.tabs_rg);
        // init fragment
        mFragments = new ArrayList<>(2);
        WeatherFragment weatherFragment=new WeatherFragment();
        ClockFragment clockFragment=new ClockFragment();
        mFragments.add(weatherFragment);
        mFragments.add(clockFragment);
//        mFragments.add(BlankFragment.newInstance("Alarm"));
//        mFragments.add(BlankFragment.newInstance("设置"));
        // init view pager
        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(mAdapter);
        // register listener
        mViewPager.addOnPageChangeListener(mPageChangeListener);
        mTabRadioGroup.setOnCheckedChangeListener(mOnCheckedChangeListener);
        findViewById(R.id.farmer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,weather);
                if(weather.equals("多云")||weather.equals("阴")){
                    startActivity(new Intent(StyleActivity.this, CloudActivity.class));
                }
                else if(weather.equals("晴")){
                    startActivity(new Intent(StyleActivity.this, SunActivity.class));
                }
                else if(weather.equals("雨夹雪")||weather.equals("小雪")){
                    startActivity(new Intent(StyleActivity.this, SnowActivity.class));
                }
                else if(weather.equals("小雨")||weather.equals("中雨")||weather.equals("大雨")||weather.equals("雷阵雨")){
                    startActivity(new Intent(StyleActivity.this, RainActivity.class));
                }
                else if(weather.equals("雾")){
                    startActivity(new Intent(StyleActivity.this, FogActivity.class));
                }
                else{
                    startActivity(new Intent(StyleActivity.this, SunActivity.class));
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewPager.removeOnPageChangeListener(mPageChangeListener);
    }

    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            RadioButton radioButton = (RadioButton) mTabRadioGroup.getChildAt(position);
            radioButton.setChecked(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            for (int i = 0; i < group.getChildCount(); i++) {
                if (group.getChildAt(i).getId() == checkedId) {
                    mViewPager.setCurrentItem(i);
                    return;
                }
            }
        }
    };

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> mList;

        public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.mList = list;
        }

        @Override
        public Fragment getItem(int position) {
            return this.mList == null ? null : this.mList.get(position);
        }

        @Override
        public int getCount() {
            return this.mList == null ? 0 : this.mList.size();
        }
    }

    @Override
    public void gettValues(String str){
        weather = str;
    }



}

