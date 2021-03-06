package com.ecnu.plantyclock.Clock.activity;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ecnu.plantyclock.Clock.AlarmService;
import com.ecnu.plantyclock.Clock.Model.AlarmModel;
import com.ecnu.plantyclock.R;
import com.ecnu.plantyclock.Clock.data.MyAlarmDataBase;
import com.ecnu.plantyclock.service.MusicServer;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ChenLu on 2021/5/24
 */
public class EditAlarmActivity extends AppCompatActivity implements
        TimePickerDialog.OnTimeSetListener {

    private Toolbar mToolbar;
    private EditText mTitleText;
    private TextView mTimeText, mRepeatText,mWakeText,mRingText;
    private FloatingActionButton mFAB1;
    private FloatingActionButton mFAB2;
    private int  mHour, mMinute;
    private String mTitle;
    private String mTime;
    private String mRepeatType,mRepeatCode;
    private String mActive,mWake,mRing;
    private List<Integer> repeatCode = new ArrayList<>() ;
    private int mAlarmID;
    private MyAlarmDataBase db;
    private AlarmModel mCheckedAlarm;
    private AlarmService.MyBinder binder;
    private ServiceConnection connection = null;

    private static final String KEY_TITLE = "title_key";
    private static final String KEY_TIME = "time_key";
    private static final String KEY_REPEAT = "repeat_key";
    private static final String KEY_WAKE= "wake_key";
    private static final String KEY_RING = "ring_key";
    private static final String KEY_ACTIVE = "active_key";
    public static final String ALARM_ID = "Alarm_ID";
    CharSequence html1;



    private String finalDefine;
    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);
        Intent intent1 = new Intent(this, MusicServer.class);
        stopService(intent1);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitleText = (EditText) findViewById(R.id.alarm_title);
        mTimeText = (TextView) findViewById(R.id.set_time);
        mRepeatText = (TextView) findViewById(R.id.set_repeat);
        mWakeText = (TextView) findViewById(R.id.set_wake);
        mRingText = (TextView) findViewById(R.id.set_ring);

        mFAB1 = (FloatingActionButton) findViewById(R.id.starred1);
        mFAB2 = (FloatingActionButton) findViewById(R.id.starred2);


        //??????ToolBar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.title_activity_edit_alarm);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mTitleText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTitle = s.toString().trim();
                mTitleText.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mAlarmID = Integer.parseInt(getIntent().getStringExtra(ALARM_ID));

        db = new MyAlarmDataBase(this);
        mCheckedAlarm = db.getAlarm(mAlarmID);

        Log.d("ed id", String.valueOf(mAlarmID));

        mTitle = mCheckedAlarm.getTitle();
        mRepeatType = mCheckedAlarm.getRepeatType();
        mTime = mCheckedAlarm.getTime();
        mActive = mCheckedAlarm.getActive();
        mWake = mCheckedAlarm.getWakeType();
        mRing = mCheckedAlarm.getRing();

        mTitleText.setText(mTitle);
        mTimeText.setText(mTime);
        mRepeatText.setText(mRepeatType);
        mRingText.setText(mRing);
        mWakeText.setText(mWake);


        // ????????????????????????
        if (savedInstanceState != null) {
            String savedTitle = savedInstanceState.getString(KEY_TITLE);
            mTitleText.setText(savedTitle);
            mTitle = savedTitle;

            String savedTime = savedInstanceState.getString(KEY_TIME);
            mTimeText.setText(savedTime);
            mTime = savedTime;
            String savedRepeat= savedInstanceState.getString(KEY_REPEAT);
            mRepeatText.setText(savedRepeat);
            mRepeatType = savedRepeat;

            String savedRing = savedInstanceState.getString(KEY_RING);
            mRingText.setText(savedRing);
            mRing = savedRing;

            String savedWake = savedInstanceState.getString(KEY_WAKE);
            mWakeText.setText(savedWake);
            mWake = savedWake;

            mActive = savedInstanceState.getString(KEY_ACTIVE);
        }

        if (mActive.equals("false")) {
            mFAB1.setVisibility(View.VISIBLE);
            mFAB2.setVisibility(View.GONE);
        } else if (mActive.equals("true")) {
            mFAB1.setVisibility(View.GONE);
            mFAB2.setVisibility(View.VISIBLE);
        }


    }

    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putCharSequence(KEY_TITLE, mTitleText.getText());
        outState.putCharSequence(KEY_TIME, mTimeText.getText());
        outState.putCharSequence(KEY_REPEAT, mRepeatText.getText());
        outState.putCharSequence(KEY_RING, mRingText.getText());
        outState.putCharSequence(KEY_WAKE, mWakeText.getText());
        outState.putCharSequence(KEY_ACTIVE, mActive);

    }

    public void selectFab1(View v){
        mFAB1 = (FloatingActionButton) findViewById(R.id.starred1);
        mFAB1.setVisibility(View.GONE);
        mFAB2 = (FloatingActionButton) findViewById(R.id.starred2);
        mFAB2.setVisibility(View.VISIBLE);
        mActive = "true";
    }

    public void selectFab2(View v){
        mFAB2 = (FloatingActionButton) findViewById(R.id.starred2);
        mFAB2.setVisibility(View.GONE);
        mFAB1 = (FloatingActionButton) findViewById(R.id.starred1);
        mFAB1.setVisibility(View.VISIBLE);
        mActive = "false";
    }


    public void selectTime(View v){
        Calendar now = Calendar.getInstance();
        TimePickerDialog timeDialog = TimePickerDialog.newInstance(
                this, now.get(Calendar.HOUR_OF_DAY),now.get(Calendar.MINUTE),false);
        timeDialog.setThemeDark(false);
        timeDialog.show(getFragmentManager(), "????????????");
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        mHour = hourOfDay;
        mMinute = minute;
        if (minute < 10) {
            mTime = hourOfDay + ":" + "0" + minute;
        } else {
            mTime = hourOfDay + ":" + minute;
        }
        mTimeText.setText(mTime);
    }

    public void selectRepeat(View v){

        final String[] items = {"????????????","??????","???????????????","????????????","?????????"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_view_day_grey600_24dp);
        builder.setTitle("????????????");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String repeatType = items[which];
                if (which == 4) {
                    showDefineDialog(dialog);
                } else {
                    mRepeatType = repeatType;
                    mRepeatText.setText(mRepeatType);
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void selectWake(View v){
        final String[] items = {"??????","????????????"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("????????????");
        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mWake = items[which];
                mWakeText.setText( mWake);

                if (which == 1){
                    Intent i = new Intent(com.ecnu.plantyclock.Clock.activity.EditAlarmActivity.this, com.ecnu.plantyclock.Clock.activity.QuestionActivity.class);
                    i.putExtra(ALARM_ID, Integer.toString(mAlarmID));
                    startActivity(i);
                }

                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    private void showDefineDialog(DialogInterface lastDialog) {

        lastDialog.dismiss();

        final String[] myDefine = {"??????","??????","??????","??????","??????","??????","??????"};
        final List<String> choosedDefine = new ArrayList<>();
        finalDefine="";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("?????????");
        builder.setMultiChoiceItems(myDefine, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                if (isChecked) {
                    choosedDefine.add(myDefine[which]);
                    repeatCode.add(which + 2);

                    StringBuilder sb = new StringBuilder();
                    if (repeatCode != null && repeatCode.size() > 0) {
                        for (int i = 0; i < repeatCode.size(); i++) {
                            if (i < repeatCode.size() - 1) {
                                sb.append(repeatCode.get(i) + ",");
                            } else {
                                sb.append(repeatCode.get(i));
                            }
                        }
                    }
                    mRepeatCode = sb.toString();
                }
            }
        });

        builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < choosedDefine.size(); i++) {
                    finalDefine = finalDefine + " " + choosedDefine.get(i);
                    if (choosedDefine.size() == 7) {
                        mRepeatType = "??????";
                        mRepeatText.setText(mRepeatType);
                    } else {
                        mRepeatType = finalDefine;
                        mRepeatText.setText(mRepeatType);
                    }
                }
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void showRingDialog(DialogInterface lastDialog) {

        lastDialog.dismiss();


        String[] ringList = new String[]{"Morning","??????","??????","????????????","??????","????????????"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setSingleChoiceItems(ringList, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor editor = getSharedPreferences("ringCode", MODE_PRIVATE).edit();
                editor.putInt("key_ring", which+1);
                System.out.println("?????????ringCode ??????  "+(which+1));
                editor.apply();
                playRing(which + 1);

            }
        });

        builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (player != null && player.isPlaying()) {
                    player.stop();
                    player.release();
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (player.isPlaying()) {
                    player.stop();
                    player.release();
                }
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void selectRing(View v){
        final String[] options = new String[]{"??????","??????","???????????????"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("????????????");
        builder.setSingleChoiceItems(options, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mRing = options[which];
                mRingText.setText(mRing);
                if (which == 1 || which == 2) {
                    showRingDialog(dialog);
                }
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void playRing(int i){
        switch (i){
            case 1:
                if (player!=null && player.isPlaying()){
                    player.stop();
                    player.release();
                    player = MediaPlayer.create(this, R.raw.ring01);
                }else {
                    player = MediaPlayer.create(this,R.raw.ring01);

                }
                break;
            case 2:
                if (player!=null && player.isPlaying()){
                    player.stop();
                    player.release();
                    player = MediaPlayer.create(this,R.raw.ring02);
                }else {
                    player = MediaPlayer.create(this,R.raw.ring02);

                }
                break;
            case 3:
                if (player!=null && player.isPlaying()){
                    player.stop();
                    player.release();
                    player = MediaPlayer.create(this,R.raw.ring03);
                }else {
                    player = MediaPlayer.create(this,R.raw.ring03);

                }
                break;
            case 4:
                if (player!=null && player.isPlaying()){
                    player.stop();
                    player.release();
                    player = MediaPlayer.create(this,R.raw.ring04);
                }else {
                    player = MediaPlayer.create(this,R.raw.ring04);

                }
                break;
            case 5:
                if (player!=null && player.isPlaying()){
                    player.stop();
                    player.release();
                    player = MediaPlayer.create(this,R.raw.ring05);
                }else {
                    player = MediaPlayer.create(this,R.raw.ring05);

                }
                break;
            case 6:
                if (player!=null && player.isPlaying()){
                    player.stop();
                    player.release();
                    player = MediaPlayer.create(this,R.raw.ring06);
                }else {
                    player = MediaPlayer.create(this,R.raw.ring06);

                }
                break;

        }
        assert player != null;
        player.setLooping(true);
        player.start();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_alarm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.save_Alarm:
                mTitleText.setText(mTitle);
                if (mTitleText.getText().toString().length() == 0){
                    html1 = Html.fromHtml("<font color=#CFF1A8>?????????????????????</font>");
                    mTitleText.setError(html1);
                }
                else {
                    updateAlarm();
                }
                return true;

            case R.id.discard_alarm:
                Toast.makeText(getApplicationContext(), "????????????",
                        Toast.LENGTH_SHORT).show();

                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateAlarm() {

        mCheckedAlarm.setTitle(mTitle);
        mCheckedAlarm.setTime(mTime);
        mCheckedAlarm.setRepeatType(mRepeatType);
        mCheckedAlarm.setRepeatCode(mRepeatCode);
        mCheckedAlarm.setWakeType(mWake);
        mCheckedAlarm.setActive(mActive);
        mCheckedAlarm.setRing(mRing);
        db.updateAlarm(mCheckedAlarm);

        if (mActive.equals("true")){
            connection = new ServiceConnection(){
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    binder = (AlarmService.MyBinder)service;

                    switch (mRepeatType) {
                        case "????????????":
                            binder.setSingleAlarm(getApplicationContext(), mTime, mAlarmID);
                            break;
                        case "??????":
                            binder.setEverydayAlarm(getApplicationContext(), mTime, mAlarmID);
                            break;
                        default:
                            binder.setDiyAlarm(getApplicationContext(), mRepeatType, mTime, mAlarmID,mRepeatCode);
                            break;
                    }
                }
                @Override
                public void onServiceDisconnected(ComponentName name) {
                }
            };
            Intent intent = new Intent(this, AlarmService.class);
            bindService(intent, connection, BIND_AUTO_CREATE);
        }

        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {

        if (connection!=null) {
            unbindService(connection);
        }
        if (player!=null){
            player.release();
        }
        super.onDestroy();
    }

    protected void onResume() {
        super.onResume();
        Intent intent1 = new Intent(this, MusicServer.class);
        stopService(intent1);

    }
}
