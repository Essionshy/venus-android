package com.tingyu.venus.controller.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tingyu.venus.R;
import com.tingyu.venus.adpater.MessageAdapter;
import com.tingyu.venus.controller.activity.ChatActivity;
import com.tingyu.venus.model.Model;
import com.tingyu.venus.model.entity.MessageInfo;
import com.tingyu.venus.utils.Constants;

import java.util.List;

public class MessageFragment extends Fragment {


    private RecyclerView rv_message;
    private LocalBroadcastManager mLBM;
    //新消息广播接收者
    private BroadcastReceiver messageBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //处理红点

            //刷新页面
            messageAdapter.refresh();
        }
    };
    private MessageAdapter messageAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_message, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("消息");

        initView();
        initData();
        initListener();


    }

    private void initListener() {

        mLBM = LocalBroadcastManager.getInstance(getContext());

        mLBM.registerReceiver(messageBroadcastReceiver, new IntentFilter(Constants.NEW_MSG_NOTICE));
        //注册单击事件跳转聊天页面
        messageAdapter.setOnItemClickListener(new MessageAdapter.OnItemClickListener() {
            @Override
            public void onClick(MessageInfo messageInfo, int position) {
                //清空未读消息数
                Model.getInstance().dbManager().getMessageDao().updateUnreadCount(messageInfo.getId(), 0);
                //修改控件显示,列表项值发生成变化的刷新方法
                messageAdapter.refresh(position);
                //跳转聊天页面

                Intent intent = new Intent(getActivity(), ChatActivity.class);
                Bundle bundle = new Bundle();

                //判断是联系人还是群组，如果是群组，则传递groupId
                if (messageInfo.getGroupId() == null) {

                    bundle.putString(Constants.USER_PHONE, messageInfo.getId());
                } else {
                    bundle.putString(Constants.GROUP_ID, messageInfo.getGroupId());
                }
                intent.putExtras(bundle);
                if (getActivity() == null) {
                    return;
                }

                getActivity().startActivity(intent);
            }
        });
        //注册滚动变化事件
        messageAdapter.setOnScrollChangeListener(new MessageAdapter.OnScrollChangeListener() {
            @Override
            public void onScrollChange(MessageInfo messageInfo) {
                Toast.makeText(getContext(), messageInfo.getNewMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 初始化页面数据
     */
    private void initData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv_message.setLayoutManager(layoutManager);
        List<MessageInfo> messageInfoList = Model.getInstance().dbManager().getMessageDao().getMessageInfoList();
        messageAdapter = new MessageAdapter(messageInfoList);
        rv_message.setAdapter(messageAdapter);
        rv_message.addItemDecoration(new MyItemDecoration()); //设置列表项样式

    }

    private void initView() {
        //加载RecycleView
        rv_message = getActivity().findViewById(R.id.rv_message);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mLBM.unregisterReceiver(messageBroadcastReceiver);
    }

    /**
     * 设置列表项显示自定义样式，可以抽取为公共类，用于显示相同样式的 RecyclerView
     */
    class MyItemDecoration extends RecyclerView.ItemDecoration {

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            //对outRect设置参数
            outRect.set(0, getResources().getDimensionPixelSize(R.dimen.dividerHeight), 0, 0);
        }
    }
}
