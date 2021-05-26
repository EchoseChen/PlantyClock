package com.ecnu.plantyclock.activities;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ecnu.plantyclock.R;
import com.ecnu.plantyclock.db.Plant;
import com.ecnu.plantyclock.service.TreeAnimation;
import com.ecnu.plantyclock.util.leafloading.AnimationUtils;
import com.ecnu.plantyclock.util.leafloading.LeafLoadingView;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by ChenLu on 2021/5/25
 * Use for snow weather
 */
public class CloudActivity extends AppCompatActivity{

    private ImageView image;//image是climate小图
    private RelativeLayout addBill;//fab按钮点击后弹出的布局
    private ImageView image1;
    private ImageView image2;
    private ImageView image3;
    private ImageView back;
    private ImageView Tree;
    private boolean isAdd = false;
    private int[] llId = new int[]{R.id.ll01,R.id.ll02,R.id.ll03};
    private LinearLayout[] ll = new LinearLayout[llId.length];
    private AnimatorSet addBillTranslate1;
    private AnimatorSet addBillTranslate2;
    private AnimatorSet addBillTranslate3;

    private LeafLoadingView mLeafLoadingView;
    private View mFanView;
    //    private Button mClearButton;
//    private int mProgress = 0;
    private int progress = 0;
    private int num;
    private TextView mProgressText;
    private TextView numText;
    //    private View mAddProgress;
    private Plant plant;
    private ImageView img_plant;

    private Boolean boolean1 = false;
    private Boolean boolean2 = false;
    private Boolean boolean3 = false;
    private Boolean boolean4 = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cloud_layout);
        initView();
        setDefaultValues();
        //悬浮按钮操作
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAdd = !isAdd;
                addBill.setVisibility(isAdd ? View.VISIBLE : View.GONE);
                if (isAdd) {
                    addBillTranslate1.setTarget(ll[0]);
                    addBillTranslate1.start();
                    addBillTranslate2.setTarget(ll[1]);
                    addBillTranslate2.setStartDelay(100);
                    addBillTranslate2.start();
                    addBillTranslate3.setTarget(ll[2]);
                    addBillTranslate3.setStartDelay(200);
                    addBillTranslate3.start();
                }

                hideFAB();
            }
        });

        addBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideFABMenu();
                showFAB();
            }
        });

        List<Plant> plantList = DataSupport.findAll(Plant.class);
        if(plantList.size()==0){
            Plant newPlant=new Plant();
            newPlant.setId(1);
            newPlant.setNum(0);
            newPlant.setProgress(0);
            newPlant.save();
        }
        plant = DataSupport.findFirst(Plant.class);

        changePlant(plant);

    }

    private void missionComplete(){
        if(boolean1&&boolean2&&boolean3&&(!boolean4)){
            Toast.makeText(getApplicationContext(),"此次任务完成啦，下次再来吧~",Toast.LENGTH_LONG).show();
            boolean4 = true;
        }


    }
    //更换Plant
    private void changePlant(Plant plant) {
        progress=plant.getProgress();
        num=plant.getNum();
        mLeafLoadingView.setProgress(progress);
        mProgressText.setText(String.valueOf(progress)+"%");
        numText.setText(String.valueOf(num));
        if(progress>=0&&progress<=20){
            img_plant.setImageResource(R.drawable.plant_1);
        }
        else if(progress>=21&&progress<=40){
            img_plant.setImageResource(R.drawable.plant_2);
        }
        else if(progress>=41&&progress<=60){
            img_plant.setImageResource(R.drawable.plant_3);
        }
        else if (progress>=61&&progress<=80){
            img_plant.setImageResource(R.drawable.plant_4);
        }
        else if(progress>=81&&progress<100){
            img_plant.setImageResource(R.drawable.plant_5);
        }
    }


    //隐藏fab按钮时的操作
    private void hideFAB(){
        image.setVisibility(View.GONE);
    }

    private void showFAB(){
        image.setVisibility(View.VISIBLE);
    }

    //隐藏fab菜单按钮时的操作
    private void hideFABMenu(){
        addBill.setVisibility(View.GONE);
        isAdd = false;
    }

    private void executeAnimation(View view) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.95f, 1.0f, 0.95f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(100);
        scaleAnimation.setRepeatCount(1);
        scaleAnimation.setRepeatMode(Animation.REVERSE);
        scaleAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        view.startAnimation(scaleAnimation);
    }




    //实例化控件
    private void initView(){
        image = (ImageView)findViewById(R.id.farm_climate);
        image1 = (ImageView)findViewById(R.id.miniImage01);
        image2 = (ImageView)findViewById(R.id.miniImage02);
        image3 = (ImageView)findViewById(R.id.miniImage03);
        back = (ImageView)findViewById(R.id.back);
        addBill = (RelativeLayout)findViewById(R.id.addBill);
        img_plant = (ImageView)findViewById(R.id.plant);

        mProgressText = (TextView) findViewById(R.id.text_progress);
        numText= (TextView)findViewById(R.id.num);
        mLeafLoadingView = (LeafLoadingView) findViewById(R.id.leaf_loading);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CloudActivity.this,StyleActivity.class);
                startActivity(intent);
            }
        });
        img_plant.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                img_plant.startAnimation(TreeAnimation.getAnimation());
            }
        });

        for (int i = 0; i < llId.length;i++){
            ll[i] = (LinearLayout)findViewById(llId[i]);
        }

        image1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                executeAnimation(view);
                img_plant.startAnimation(TreeAnimation.getAnimation());
                if(!boolean4&&!boolean1){
                    boolean1 = true;
                    progress+=10;
                    missionComplete();
                }
                else{
                    progress++;//设置增长
                }
                if(progress>=100){
                    progress=0;
                    num+=1;
                }
                mLeafLoadingView.setProgress(progress);
                mProgressText.setText(String.valueOf(progress)+"%");
                numText.setText(String.valueOf(num));
                plant = DataSupport.findFirst(Plant.class);
                plant.setProgress(progress);
                plant.setNum(num);
                plant.save();
                changePlant(plant);
            }
        });
        image2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                executeAnimation(view);
                img_plant.startAnimation(TreeAnimation.getAnimation());
                if(!boolean4&&!boolean2){
                    boolean2 = true;
                    progress+=10;
                    missionComplete();
                }
                else{
                    progress++;
                }
                if(progress>=100){
                    progress=0;
                    num+=1;
                }
                mLeafLoadingView.setProgress(progress);
                mProgressText.setText(String.valueOf(progress)+"%");
                numText.setText(String.valueOf(num));
                plant = DataSupport.findFirst(Plant.class);
                plant.setProgress(progress);
                plant.save();
                changePlant(plant);
            }
        });
        image3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                executeAnimation(view);
                img_plant.startAnimation(TreeAnimation.getAnimation());
                if(!boolean4&&!boolean3){
                    boolean3 = true;
                    progress+=10;
                    missionComplete();
                }
                else{
                    progress++;
                }
                if(progress>=100){
                    progress=0;
                    num+=1;
                }
                mLeafLoadingView.setProgress(progress);
                mProgressText.setText(String.valueOf(progress)+"%");
                numText.setText(String.valueOf(num));
                plant = DataSupport.findFirst(Plant.class);
                plant.setProgress(progress);
                plant.save();
                changePlant(plant);
            }
        });



        mFanView = findViewById(R.id.fan_pic);
        RotateAnimation rotateAnimation = AnimationUtils.initRotateAnimation(false, 1500, true,
                Animation.INFINITE);
        mFanView.startAnimation(rotateAnimation);





    }

    private void setDefaultValues(){
        addBillTranslate1 = (AnimatorSet) AnimatorInflater.loadAnimator(this,R.animator.add_bill_anim);
        addBillTranslate2 = (AnimatorSet) AnimatorInflater.loadAnimator(this,R.animator.add_bill_anim);
        addBillTranslate3 = (AnimatorSet) AnimatorInflater.loadAnimator(this,R.animator.add_bill_anim);
    }


}

