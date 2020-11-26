package com.tingyu.venus.controller.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tingyu.venus.R;
import com.tingyu.venus.adpater.ChatAdapter;
import com.tingyu.venus.model.Model;
import com.tingyu.venus.model.entity.ChatRecord;
import com.tingyu.venus.model.entity.GroupInfo;
import com.tingyu.venus.model.entity.UserInfo;
import com.tingyu.venus.netty.NettyClientConnectUtil;
import com.tingyu.venus.netty.handler.socket.ChatMessageHandler;
import com.tingyu.venus.netty.protobuf.ChatNotice;
import com.tingyu.venus.netty.util.MessageIdGenerator;
import com.tingyu.venus.utils.Constants;

import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private EditText et_content;
    private Button btn_send;
    private RecyclerView lv_show;
    private ChatAdapter chatAdapter;
    private String contactId;
    private String groupId;
    private String fromId;
    private List<ChatRecord> chatRecords;
    private boolean isSingleChat = false;
    private boolean isGroupChat = false;

    private LocalBroadcastManager mLBM = LocalBroadcastManager.getInstance(this);

    //单聊广播接收器
    private BroadcastReceiver singleChatBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            //处理消息提示的变化

            if (isSingleChat) {
                //刷新列表
                refreshSingleChat();
            } else if (isGroupChat) {
                //群聊
                refreshGroupChat();
            }


        }
    };


    /**
     * 刷新单聊列表
     */
    private void refreshSingleChat() {
        List<ChatRecord> records = Model.getInstance().dbManager().getChatRecordDao().getSingleChatRecordByContactId(contactId);
        chatAdapter.refresh(records);
        lv_show.scrollToPosition(records.size() - 1);//显示最新的一条记录

    }

    /**
     * 初始化监听
     */
    private void initListener() {
        //消息发送单击事件监听
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取输入的消息内容
                String content = et_content.getText().toString();

                //回显自己发送的消息
                show(content);

                //发送消息
                send(content);
                //清空
                clearInput();
            }
        });

        //注册广播
        mLBM = LocalBroadcastManager.getInstance(this);
        mLBM.registerReceiver(singleChatBroadcast, new IntentFilter(Constants.CHAT_CHANGED_NOTICE));
    }

    /**
     * 发送消息
     */
    private void send(String content) {
        if (isSingleChat) {
            sendSingleChat(content);
            return;
        }

        if (isGroupChat) {
            sendGroupChat(content);
            return;
        }

    }

    /**
     * 发送单聊消息
     *
     * @param content
     */
    private void sendSingleChat(String content) {
        //封装消息
        ChatNotice.ChatNoticeMessage.Builder builder = ChatNotice.ChatNoticeMessage.newBuilder();


        //TODO 根据一定规则生成ID
        builder.setId(MessageIdGenerator.getId());
        builder.setContentType(ChatNotice.ChatNoticeMessage.ContentType.TEXT);
        builder.setContent(content);
        //设置消息发送者
        builder.setFromId(fromId); //获取当前用户ID 17318656979
        // 设置消息接收者 TODO
        builder.setToId(contactId);//联系手机号
        ChatNotice.ChatNoticeMessage chatMessage = builder.build();


        //发送消息
        NettyClientConnectUtil.connect(new ChatMessageHandler(chatMessage));
    }

    /**
     * 发送群聊消息
     *
     * @param content
     */
    private void sendGroupChat(String content) {
        ChatNotice.ChatNoticeMessage.Builder builder = ChatNotice.ChatNoticeMessage.newBuilder();
        builder.setGroupId(groupId);
        builder.setFromId(fromId);
        builder.setContent(content);
        ChatNotice.ChatNoticeMessage message = builder.build();
        NettyClientConnectUtil.connect(new ChatMessageHandler(message));

    }

    //清空输入框
    private void clearInput() {
        et_content.setText("");
    }

    /**
     * 回显自己发送的消息
     */
    private void show(Object content) {
        if (content == null || TextUtils.isEmpty((String) content)) {
            return;
        }
        Log.d("show content", "回显自己发送的消息");
        ChatRecord record = new ChatRecord();
        if (isSingleChat) {
            record.setGroupId("0");
            record.setContactId(contactId); //指定是与哪位联系人的聊天记录
            record.setContent((String) content);
            record.setMessageType(ChatRecord.MessageType.SEND);
            record.setContentType(ChatRecord.ContentType.TEXT);
            Model.getInstance().dbManager().getChatRecordDao().save(record);
            //刷新页面
            refreshSingleChat();
            return;
        }

        if (isGroupChat) {
            //群聊
            record.setGroupId(groupId);
            record.setContent((String) content);
            record.setMessageType(ChatRecord.MessageType.SEND);
            record.setContentType(ChatRecord.ContentType.TEXT);
            Model.getInstance().dbManager().getChatRecordDao().save(record);
            refreshGroupChat();
            return;
        }


    }

    private void refreshGroupChat() {

        List<ChatRecord> records = Model.getInstance().dbManager().getChatRecordDao().getGroupChatRecordByGroupId(groupId);
        chatAdapter.refresh(records);
        lv_show.scrollToPosition(records.size() - 1);//显示最新的一条记录
    }


    /**
     * 初始化控件
     */
    private void initView() {
        et_content = findViewById(R.id.et_content);
        btn_send = findViewById(R.id.btn_send);
        lv_show = findViewById(R.id.rv_show);
        //查询该联系人
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        lv_show.setLayoutManager(layoutManager);
        chatAdapter = new ChatAdapter(chatRecords);
        lv_show.setAdapter(chatAdapter);
        if (chatRecords != null && chatRecords.size() > 0) {

            lv_show.scrollToPosition(chatRecords.size() - 1);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_chat);

        initData();
        initView();
        initListener();
    }

    /**
     * 初始化聊天数据
     */
    private void initData() {
        //获取当前用户手机号
        fromId = Model.getInstance().getUserDao().getCurrentUser().getPhone();
        //从跳转页面中获取参数
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        contactId = bundle.getString(Constants.USER_PHONE);

        //判断是单聊还是群聊
        if (contactId != null) {
            isSingleChat = true;
            loadSingleChatData(contactId);
            return;
        }
        //判断是否为群聊
        groupId = bundle.getString(Constants.GROUP_ID);
        if (groupId != null) {
            isGroupChat = true;
            loadGroupChatData(groupId);
            return;
        }


    }

    private void loadGroupChatData(String groupId) {

        //加载群信息
        GroupInfo groupInfo = Model.getInstance().dbManager().getGroupChatDao().getGroupInfo(groupId);
        if (groupInfo == null) {
            return;
        }
        String members = groupInfo.getMembers();
        if (members != null) {
            String[] memberList = members.split(",");

            setTitle(groupInfo.getGroupName() + "(" + memberList.length + ")");
        } else {
            setTitle(groupInfo.getGroupName());
        }
        //加载群聊记录
        chatRecords = Model.getInstance().dbManager().getChatRecordDao().getGroupChatRecordByGroupId(groupId);


    }

    /**
     * 加载单聊记录
     *
     * @param contactId
     */
    private void loadSingleChatData(String contactId) {
        UserInfo contact = Model.getInstance().dbManager().getContactDao().getContactByPhone(contactId);
        if (contact != null) {
            setTitle(contact.getUsername());
        }

        chatRecords = Model.getInstance().dbManager().getChatRecordDao().getSingleChatRecordByContactId(contactId);

    }

    @Override
    protected void onStop() {
        super.onStop();
        Model.getInstance().dbManager().getMessageDao().updateUnreadCount(contactId, 0); //将通知消息设置为0，会话中也有新消息进入，会在消息提示界面显示，然后这并不是我们想要的

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //关闭广播监听
        mLBM.unregisterReceiver(singleChatBroadcast);
    }
}