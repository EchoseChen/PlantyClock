package com.ecnu.plantyclock.Clock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ecnu.plantyclock.Clock.activity.PlayAlarmActivity;
/**
 * Created by ChenLu on 2021/5/24
 */

public class SnoozeReceiver extends BroadcastReceiver {
    public static final String ID_SNOOZE_FLAG = "id_snooze";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("SnoozeReceiver","接收到贪睡广播");
        Intent i = new Intent(context,PlayAlarmActivity.class);
        i.putExtra(PlayAlarmActivity.ALARM_ID, intent.getStringExtra(ID_SNOOZE_FLAG));
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
