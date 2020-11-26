package com.tingyu.venus.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.tingyu.venus.R;

public class BackgroundMusicService extends Service {

    public static boolean isPlay; //标识背景音乐播放状态
    private MediaPlayer mediaPlayer; //定义媒体播放器

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * 创建Service
     */
    @Override
    public void onCreate() {
        mediaPlayer = MediaPlayer.create(this, R.raw.backgroudmusic);


        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //判断当前背景音乐播放状态,如果没有播放则播放

        if (!isPlay) {
            mediaPlayer.start();
            isPlay = mediaPlayer.isPlaying();
        }


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        isPlay = mediaPlayer.isPlaying(); //isPlaying()表示当前播放状态
        mediaPlayer.release();//释放资源
        super.onDestroy();
    }
}
