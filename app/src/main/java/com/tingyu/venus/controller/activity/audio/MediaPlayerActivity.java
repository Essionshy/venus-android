package com.tingyu.venus.controller.activity.audio;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.tingyu.venus.R;

public class MediaPlayerActivity extends AppCompatActivity {
     MediaPlayer mediaPlayer=null;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        mediaPlayer.release();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_media);
        Button btnPlay = (Button) findViewById(R.id.btn_play);
        Button btnPause = (Button) findViewById(R.id.btn_pause);
        Button btnStop = (Button) findViewById(R.id.btn_stop);
        //创建媒体播放器 Nexus 6上无法正常播放，Nexus 4可以正常播放
         mediaPlayer = MediaPlayer.create(this, R.raw.backgroudmusic);

        //监听单击事件
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("message", "播放音乐");


                mediaPlayer.start();

            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("message", "暂停播放");
                mediaPlayer.pause();
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("message", "关闭音乐");
                mediaPlayer.stop();
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });



    }
}
