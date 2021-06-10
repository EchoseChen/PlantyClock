package com.ecnu.plantyclock.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.ecnu.plantyclock.R;

/**
 * Created by ChenLu on 2021/6/10
 * Use for 播放游戏背景音
 */
public class MusicServer extends Service {
    private MediaPlayer mediaPlayer;
    public MusicServer() {
    }

    @Override
    public IBinder onBind(Intent intent) {
//        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.bg);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();

        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mediaPlayer.stop();
    }
}