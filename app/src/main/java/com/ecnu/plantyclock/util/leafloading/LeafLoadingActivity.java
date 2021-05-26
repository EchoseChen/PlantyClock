
package com.ecnu.plantyclock.util.leafloading;


/**
 * Created by ChenLu on 2021/5/21
 * Use for loading
 */
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.ecnu.plantyclock.R;



public class LeafLoadingActivity extends Activity implements OnClickListener {
    private LeafLoadingView mLeafLoadingView;
    private View mFanView;
    private Button mClearButton;
    private int mProgress = 0;

    private TextView mProgressText;
    private View mAddProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaf_loading_layout);
        initViews();
    }

    private void initViews() {
        mFanView = findViewById(R.id.fan_pic);
        RotateAnimation rotateAnimation = AnimationUtils.initRotateAnimation(false, 1500, true,
                Animation.INFINITE);
        mFanView.startAnimation(rotateAnimation);
        mClearButton = (Button) findViewById(R.id.clear_progress);
        mClearButton.setOnClickListener(this);

        mLeafLoadingView = (LeafLoadingView) findViewById(R.id.leaf_loading);

        mAddProgress = findViewById(R.id.add_progress);
        mAddProgress.setOnClickListener(this);
        mProgressText = (TextView) findViewById(R.id.text_progress);
    }


    @Override
    public void onClick(View v) {
        if (v == mClearButton) {
            mLeafLoadingView.setProgress(0);
            mProgress = 0;
            mProgressText.setText(String.valueOf(mProgress));
        } else if (v == mAddProgress) {
            mProgress++;
            mLeafLoadingView.setProgress(mProgress);
            mProgressText.setText(String.valueOf(mProgress)+"%");
        }
    }
}
