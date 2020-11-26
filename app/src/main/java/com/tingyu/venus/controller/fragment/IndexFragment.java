package com.tingyu.venus.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tingyu.venus.R;
import com.tingyu.venus.controller.activity.audio.MediaPlayerActivity;
import com.tingyu.venus.controller.activity.location.LocationActivity;
import com.tingyu.venus.controller.activity.lottery.LotteryActivity;
import com.tingyu.venus.controller.activity.query.SheBaoQueryActivity;
import com.tingyu.venus.controller.activity.sensor.LightSensorActivity;
import com.tingyu.venus.service.BackgroundMusicService;
import com.tingyu.venus.service.MonitorService;
import com.tingyu.venus.controller.activity.video.VideoViewActivity;

public class IndexFragment extends Fragment {

    private Intent monitorService;
    private Intent backgroundMusicService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_index, container, false);
        return view;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("首页");
        Button buttonShebao = getActivity().findViewById(R.id.btn_shebao);
        Button musicButton = getActivity().findViewById(R.id.btn_music);
        Button vedioButton = getActivity().findViewById(R.id.btn_vedio);
        Button startService = getActivity().findViewById(R.id.btn_start_service);
        Button stopService = getActivity().findViewById(R.id.btn_stop_service);
        Button lotteryButton = getActivity().findViewById(R.id.btn_lottery);
        Button sensorButton = getActivity().findViewById(R.id.btn_sensor);
        Button locationButton = getActivity().findViewById(R.id.btn_map);
        ImageButton backgroundMusic = getActivity().findViewById(R.id.ib_play);
        buttonShebao.setOnClickListener(onClickListener);
        musicButton.setOnClickListener(onClickListener);
        vedioButton.setOnClickListener(onClickListener);
        startService.setOnClickListener(onClickListener);
        stopService.setOnClickListener(onClickListener);
        lotteryButton.setOnClickListener(onClickListener);
        sensorButton.setOnClickListener(onClickListener);
        locationButton.setOnClickListener(onClickListener);
        backgroundMusic.setOnClickListener(onClickListener);

        monitorService = new Intent(getActivity(), MonitorService.class);
        backgroundMusicService = new Intent(getActivity(), BackgroundMusicService.class);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btn_shebao:
                    startActivity(new Intent(getActivity(), SheBaoQueryActivity.class));
                    break;
                case R.id.btn_music:
                    startActivity(new Intent(getActivity(), MediaPlayerActivity.class));
                    break;
                case R.id.btn_vedio:
                    startActivity(new Intent(getActivity(), VideoViewActivity.class));
                    break;
                case R.id.btn_start_service:
                    getActivity().startService(monitorService);
                    break;
                case R.id.btn_lottery:
                    startActivity(new Intent(getActivity(), LotteryActivity.class));
                    break;
                case R.id.btn_stop_service:
                    getActivity().stopService(monitorService);
                    break;
                case R.id.btn_sensor:
                    startActivity(new Intent(getActivity(), LightSensorActivity.class));
                    break;
                case R.id.btn_map:
                    startActivity(new Intent(getActivity(), LocationActivity.class));
                    break;
                case R.id.ib_play:
                    if (BackgroundMusicService.isPlay) {
                        getActivity().stopService(backgroundMusicService);
                        //切换为停止状态
                    } else {
                        getActivity().startService(backgroundMusicService);
                        //切换为播放状态
                    }
                    break;

                default:
                    break;
            }


        }
    };



}
