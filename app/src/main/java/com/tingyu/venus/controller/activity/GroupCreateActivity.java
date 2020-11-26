package com.tingyu.venus.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tingyu.venus.R;
import com.tingyu.venus.adpater.CreateGroupChatAdapter;
import com.tingyu.venus.model.Model;
import com.tingyu.venus.model.entity.GroupInfo;
import com.tingyu.venus.model.entity.UserInfo;
import com.tingyu.venus.netty.NettyClientConnectUtil;
import com.tingyu.venus.netty.protobuf.GroupChatCreateNotice;
import com.tingyu.venus.netty.util.MessageIdGenerator;
import com.tingyu.venus.service.GroupChatCreateHandler;
import com.tingyu.venus.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 创建群
 */
public class GroupCreateActivity extends AppCompatActivity {

    private RecyclerView rv_group;
    private Button btn_group_submit;

    private CreateGroupChatAdapter groupChatAdapter;

    private List<UserInfo> checkedContacts = new ArrayList<>();
    private LocalBroadcastManager mLBM;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    private void initListener() {

        groupChatAdapter.setOnItemCheckedChangeListener(new CreateGroupChatAdapter.OnItemCheckedChangeListener() {
            @Override
            public void onChecked(UserInfo userInfo, boolean isChecked) {
                if (isChecked) {
                    checkedContacts.add(userInfo);
                } else {
                    checkedContacts.remove(userInfo);
                }
            }
        });

        //提交
        btn_group_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfo currentUser = Model.getInstance().getUserDao().getCurrentUser();
                if (currentUser == null) {
                    return;
                }
                //将群主加入到群成员中
                checkedContacts.add(currentUser);

                //保存群信息到本地，并返回该对象
                GroupInfo groupInfo = saveLocalGroupInfo(currentUser.getPhone());
                //发送创建群消息到服务器
                sendRequest(groupInfo);
                //发送群组变化通知

                mLBM.sendBroadcast(new Intent(Constants.GROUP_CHANGED));

                //跳转到会话页面
                Intent intent = new Intent(GroupCreateActivity.this, GroupChatActivity.class);
                startActivity(intent);
                //清除当前Activity
                finish();

            }


        });

    }

    /**
     * 保存群信息到本地数据库，如果服务器端没有创建成功，则删除该条记录
     *
     * @param phone
     */
    private GroupInfo saveLocalGroupInfo(String phone) {
        if (phone == null || TextUtils.isEmpty(phone)) {
            return null;
        }
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setGroupId(UUID.randomUUID().toString().substring(0, 5)); //随机生成群组ID
        groupInfo.setGroupName(getMemberNames(checkedContacts)); //自动生成群名称【取前几个联系人的名称】
        groupInfo.setMembers(getMemberIds(checkedContacts));
        groupInfo.setInviter(phone);
        Model.getInstance().dbManager().getGroupChatDao().save(groupInfo);
        return groupInfo;
    }

    /**
     * 发送创建群消息
     *
     * @param groupInfo
     */
    private void sendRequest(GroupInfo groupInfo) {

        GroupChatCreateNotice.GroupChatCreateNoticeMessage.Builder builder = GroupChatCreateNotice.GroupChatCreateNoticeMessage.newBuilder();
        builder.setId(MessageIdGenerator.getId());
        builder.setGroupId(groupInfo.getGroupId());
        builder.setGroupName(groupInfo.getGroupName());
        builder.setCreateId(groupInfo.getInviter());
        builder.setMemberIds(groupInfo.getMembers());
        GroupChatCreateNotice.GroupChatCreateNoticeMessage message = builder.build();
        NettyClientConnectUtil.connect(new GroupChatCreateHandler(message));

    }

    private String getMemberIds(List<UserInfo> checkedContacts) {
        if (checkedContacts == null || checkedContacts.size() == 0) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (UserInfo userInfo : checkedContacts) {
            sb.append(userInfo.getId()).append(",");
        }

        String memberIds = sb.toString();

        return sb.toString().substring(0, memberIds.length() - 1);
    }

    private String getMemberNames(List<UserInfo> checkedContacts) {
        if (checkedContacts == null || checkedContacts.size() == 0) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (UserInfo userInfo : checkedContacts) {
            sb.append(userInfo.getUsername()).append("、");
        }

        String memberNames = sb.toString();

        return sb.toString().substring(0, memberNames.length() - 1);
    }

    private void initData() {
        mLBM = LocalBroadcastManager.getInstance(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv_group.setLayoutManager(linearLayoutManager);
        List<UserInfo> contactList = Model.getInstance().dbManager().getContactDao().getContactList();
        groupChatAdapter = new CreateGroupChatAdapter(contactList);
        rv_group.setAdapter(groupChatAdapter);


    }

    /**
     * 初始化视图
     */
    private void initView() {
        setContentView(R.layout.activity_group_create);

        rv_group = findViewById(R.id.rv_group);
        btn_group_submit = findViewById(R.id.btn_group_submit);

    }
}
