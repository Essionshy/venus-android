package com.tingyu.venus.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tingyu.venus.R;
import com.tingyu.venus.controller.activity.sys.SettingsActivity;
import com.tingyu.venus.model.Model;
import com.tingyu.venus.model.entity.UserInfo;

public class MeFragment extends Fragment {

    private TextView btn_me_settings;

    private ImageView iv_me_avatar;

    private TextView tv_me_username;

    private LinearLayout me_settings;
    private TextView tv_me_phone;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_me, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("我的");
        //初始化加载页面
        initView();
        //初始化绑定监听
        initListener();
        //初始化数据
        initData();
    }

    /**
     * 初始化页面数据
     */
    private void initData() {
        UserInfo currentUser = Model.getInstance().getUserDao().getCurrentUser();
        tv_me_username.setText(currentUser.getUsername());
        tv_me_phone.setText(currentUser.getPhone());
    }

    /**
     * 初始化监听
     */

    private void initListener() {

        //处理点击设置事件业务逻辑
        me_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), SettingsActivity.class));
            }
        });
    }

    /**
     * 初始化页面
     */
    private void initView() {
        if (getActivity() != null) {
            iv_me_avatar = getActivity().findViewById(R.id.iv_me_avatar);
            tv_me_username = getActivity().findViewById(R.id.tv_me_username);
            tv_me_phone = getActivity().findViewById(R.id.tv_me_phone);
            me_settings = getActivity().findViewById(R.id.me_settings);
        }
    }
}
