package com.tingyu.venus.controller.activity.video;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tingyu.venus.R;

import java.io.File;

public class VideoViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        //设置全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        VideoView video=(VideoView)findViewById(R.id.vv_vedio);
        //获取视频文件
        File file=new File(Environment.getExternalStorageDirectory()+"/video.mp4");

        //判断文件是否存在
        if(file.exists()){

            video.setVideoPath(file.getAbsolutePath());
        }else {
            Toast.makeText(VideoViewActivity.this,"视频文件不存在",Toast.LENGTH_LONG);
        }

        //关联MediaController

        MediaController mediaController=new MediaController(VideoViewActivity.this);
        video.setMediaController(mediaController);
        video.requestFocus();//video获得焦点
        video.start();//开始播放视频
        //监听视频播放完毕事件
        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(VideoViewActivity.this,"视频播放完毕",Toast.LENGTH_LONG);
            }
        });



    }
}
