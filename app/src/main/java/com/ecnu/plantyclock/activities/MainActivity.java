package com.ecnu.plantyclock.activities;

import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.ecnu.plantyclock.R;

/**
 * Created by ChenLu on 2021/5/21
 * Use for 开启界面
 */
public class MainActivity extends AppCompatActivity{

    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startAnimation();
        //背景音
        mp = MediaPlayer.create(this, R.raw.bg);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer arg0) {
                mp.start();
                mp.setLooping(true);
            }
        });
        ImageView enter_button = (ImageView) findViewById(R.id.enter);
        enter_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this, StyleActivity.class);
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
}