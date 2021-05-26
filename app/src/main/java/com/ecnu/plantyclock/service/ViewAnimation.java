package com.ecnu.plantyclock.service;

import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * Created by ChenLu on 2021/5/21
 * Use...
 */
public class ViewAnimation {

    //想要让控件向上出现调用enterAnimation("up");
    //想要让控件向下消失调用hideAnimation("down");
    //上下左右出现消失均可，以此类推


    //显示动画
    public static TranslateAnimation enterAnimation(String direction) {
        TranslateAnimation mShowAction;
        switch (direction) {
            case "up":                                                                                      //向上出现，以此类推
                mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                        1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                break;

            case "down":
                mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                        -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                break;
            case "left":
                mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                        0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                break;
            case "right":
                mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                        -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                break;
            default:
                mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                        1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                break;
        }
        mShowAction.setDuration(800);
        return mShowAction;

    }

    //隐藏动画
    public static TranslateAnimation hideAnimation(String direction) {
        TranslateAnimation mShowAction;
        switch (direction) {
            case "up":                                                                                          //向上消失，以此类推
                mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                        0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
                break;

            case "down":
                mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                        0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
                break;
            case "left":
                mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF,
                        0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                break;
            case "right":
                mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
                        0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                break;
            default:
                mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                        0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
                break;
        }
        mShowAction.setDuration(800);
        return mShowAction;

    }


}
