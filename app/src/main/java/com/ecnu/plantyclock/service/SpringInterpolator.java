package com.ecnu.plantyclock.service;

import android.view.animation.Interpolator;

/**
 * Created by ChenLu on 2021/5/25
 * Use...
 */
public class SpringInterpolator implements Interpolator {

    //控制弹簧系数
    private float factor;

    public SpringInterpolator(float factor) {
        this.factor = factor;
    }

    @Override
    public float getInterpolation(float input) {
        //factor = 0.4
//        pow(2, -10 * x) * sin((x - factor / 4) * (2 * PI) / factor) + 1

        return (float) -(Math.pow(2, -10 * input) * Math.sin((input - factor / 4) * (2 * Math.PI) / factor) );
    }
}
