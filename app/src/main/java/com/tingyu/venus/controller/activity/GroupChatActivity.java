package com.tingyu.venus.controller.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tingyu.venus.R;
import com.tingyu.venus.adpater.GroupChatAdapter;
import com.tingyu.venus.model.Model;
import com.tingyu.venus.model.entity.GroupInfo;
import com.tingyu.venus.utils.Constants;

import java.util.List;

public class GroupChatActivity extends AppCompatActivity {

    private RecyclerView rv_group_list;
    private GroupChatAdapter groupChatAdapter;
    private LocalBroadcastManager mLBM;
    private BroadcastReceiver groupChatBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //接收群组变化广播

            //刷新群组列表
            List<GroupInfo> groupInfoList = Model.getInstance().dbManager().getGroupChatDao().getGroupInfoList();
            groupChatAdapter.refresh(groupInfoList);

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        //跳转聊天页面
        groupChatAdapter.setOnItemClickListener(new GroupChatAdapter.OnItemClickListener() {
            @Override
            public void onClick(GroupInfo groupInfo, int position) {
                //

                Bundle bundle = new Bundle();
                bundle.putString(Constants.GROUP_ID, groupInfo.getGroupId());
                Intent intent = new Intent(GroupChatActivity.this, ChatActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);


            }
        });

        mLBM = LocalBroadcastManager.getInstance(this);
        mLBM.registerReceiver(groupChatBroadcastReceiver, new IntentFilter(Constants.GROUP_CHANGED));

    }

    private void initData() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        rv_group_list.setLayoutManager(linearLayoutManager);

        List<GroupInfo> groupInfoList = Model.getInstance().dbManager().getGroupChatDao().getGroupInfoList();

        groupChatAdapter = new GroupChatAdapter(groupInfoList);

        rv_group_list.setAdapter(groupChatAdapter);


    }

    /**
     * 初始化视图
     */
    private void initView() {
        rv_group_list = findViewById(R.id.rv_group_list);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLBM.unregisterReceiver(groupChatBroadcastReceiver);//关闭广播，避免出现内存泄漏
    }
}
