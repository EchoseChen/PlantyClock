package com.ecnu.plantyclock.service;

import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

/**
 * Created by ChenLu on 2021/5/25
 * Use...
 */
public class TreeAnimation {

    public static Animation getAnimation() {
        // 创建缩放的动画对象
        ScaleAnimation sa = new ScaleAnimation(1f, 1.0f, 1.0f, 1.1f, ScaleAnimation.RELATIVE_TO_SELF, 0.0f, ScaleAnimation.RELATIVE_TO_SELF, 1.0f);
        // 设置动画播放的时间
        sa.setDuration(1500);
        sa.setInterpolator(new SpringInterpolator(0.3f));
        return sa;
    }

}
