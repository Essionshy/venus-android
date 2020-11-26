package com.tingyu.venus.model;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.alibaba.fastjson.JSONObject;
import com.tingyu.venus.event.ContactListener;
import com.tingyu.venus.event.ChatListener;
import com.tingyu.venus.event.GroupChatListener;
import com.tingyu.venus.model.entity.GroupInfo;
import com.tingyu.venus.model.entity.InvitationInfo;
import com.tingyu.venus.model.entity.MessageInfo;
import com.tingyu.venus.model.entity.ChatRecord;
import com.tingyu.venus.model.entity.UserInfo;
import com.tingyu.venus.utils.Constants;
import com.tingyu.venus.utils.SpUtils;

import java.util.Date;

/**
 * 全局事件监听器
 */
public class EventListener {


    private Context mContext;
    private final LocalBroadcastManager mLBM;

    public EventListener(Context context) {
        mContext = context;
        //注册联系人事件监听器
        Model.getInstance().contactManager().setContactListener(contactListener);
        //注册聊天事件监听器
        Model.getInstance().chatManager().setChatListener(chatListener);
        //注册群组事件监听器

        Model.getInstance().groupChatManager().setGroupChatListener(groupChatListener);
        mLBM = LocalBroadcastManager.getInstance(context);
        Log.d("EventListener", "全局事件监听器初始化完成...");
    }

    /**
     * 群组事件
     */
    private final GroupChatListener groupChatListener = new GroupChatListener() {
        @Override
        public void onCreate(String message) {
            //解析消息
            JSONObject jsonObject = JSONObject.parseObject(message);
            //封装群组信息
            GroupInfo groupInfo = new GroupInfo();
            groupInfo.setGroupId(jsonObject.getString("groupId"));
            groupInfo.setGroupName(jsonObject.getString("groupName"));
            groupInfo.setInviter(jsonObject.getString("createId"));
            groupInfo.setMembers(jsonObject.getString("memberIds"));
            Model.getInstance().dbManager().getGroupChatDao().save(groupInfo);

            //发送群组变化广播
            mLBM.sendBroadcast(new Intent(Constants.GROUP_CHANGED));

        }

        @Override
        public void onRemove() {

        }
    };


    /**
     * 单聊事件监听器
     */
    private final ChatListener chatListener = new ChatListener() {
        @Override
        public void onReceived(String message) {
            Log.d("chat listener", message);
            //TODO 消息保存数据库
            JSONObject jsonObject = JSONObject.parseObject(message);
            String fromId = jsonObject.getString("fromId");
            String content = jsonObject.getString("content");
            Log.d("chat listener", content);
            //判断是单聊还是群聊
            String groupId = jsonObject.getString("groupId");


            //保存消息提示信息
            MessageInfo messageInfo = new MessageInfo();
            if (groupId != null) {
                messageInfo.setId(groupId);
                messageInfo.setGroupId(groupId);
            } else {
                messageInfo.setId(fromId);
                messageInfo.setContactId(fromId);
            }

            messageInfo.setNewMessage(content);
            messageInfo.setTime(new Date().toString());
            messageInfo.setNickname(fromId);

            MessageInfo exists = Model.getInstance().dbManager().getMessageDao().getMessageInfoByPhone(fromId);
            if (exists != null) {
                messageInfo.setUnReadCount(exists.getUnReadCount() + 1);
            } else {
                messageInfo.setUnReadCount(1);
            }
            Model.getInstance().dbManager().getMessageDao().save(messageInfo);
            //更新数据库该联系人未读消息数
     /*       MessageInfo exists = Model.getInstance().messageManager().getMessageDao().getMessageInfoByPhone(fromId);
            Model.getInstance().messageManager().getMessageDao().updateUnreadCount(messageInfo.getId(),exists.getUnReadCount()+1);*/


            //保存聊天信息 TODO 根据消息类型进行不同消息类型的封装
            ChatRecord record = new ChatRecord();
            record.setContent(content);
            record.setContactId(fromId);

            if (groupId == null) {
                //单聊
                record.setGroupId("0");
            } else {
                //群聊
                record.setGroupId(groupId);
            }

            record.setContentType(ChatRecord.ContentType.TEXT);
            record.setMessageType(ChatRecord.MessageType.RECEIVE);
            Model.getInstance().dbManager().getChatRecordDao().save(record);


            //TODO 未读消息提示，红点处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_MSG, true);
            //本地广播
            mLBM.sendBroadcast(new Intent(Constants.NEW_MSG_NOTICE));//发送新消息通知广播
            mLBM.sendBroadcast(new Intent(Constants.CHAT_CHANGED_NOTICE));//发送聊天通知广播

        }

        @Override
        public void onError(String message) {

        }
    };
    /**
     * 定义事件
     */
    private final ContactListener contactListener = new ContactListener() {


        @Override
        public void onContactOnline(String var1) {
            JSONObject jsonObject = JSONObject.parseObject(var1);
            String userPhone = jsonObject.getString("userPhone");
            Log.d("ContactListener", "联系人" + userPhone + "上线了");
            mLBM.sendBroadcast(new Intent(Constants.CONTACT_ON_LINE));//发送联系人上线广播
        }

        @Override
        public void onContactOffline(String var1) {
            Log.d("ContactListener", "onContactOffline..." + var1);
            mLBM.sendBroadcast(new Intent(Constants.CONTACT_OFF_LINE));//发送联系人上线广播


        }

        @Override
        public void onContactAdded(String var1) {
            Log.d("ContactListener", "onContactAdded..." + var1);
            //更新数据库

            //发送联系人变化的广播
            mLBM.sendBroadcast(new Intent(Constants.CONTACT_CHANGED));

        }

        @Override
        public void onContactDeleted(String var1) {

        }

        @Override
        public void onContactInvited(String var1) {
            //解析响应数据 保存数据到数据库
            Log.d("ContactListener", "onContactInvited..." + var1);
            JSONObject jsonObject = JSONObject.parseObject(var1);
            String fromPhone = jsonObject.getString("fromPhone"); //邀请人
            String fromId = jsonObject.getString("fromId"); //邀请人
            String fromUsername = jsonObject.getString("fromUsername"); //邀请人
            String reason = jsonObject.getString("reason"); //邀请原因

            UserInfo userInfo = new UserInfo();
            userInfo.setId(fromPhone);
            userInfo.setPhone(fromPhone);
            userInfo.setUsername(fromUsername);


            InvitationInfo invitationInfo = new InvitationInfo();
            invitationInfo.setReason(reason);
            invitationInfo.setUser(userInfo);
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.NEW_INVITE);
            Model.getInstance().dbManager().getInvitationDao().addInvitation(invitationInfo);

            //处理消息提示 红点
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, true);

            //广播添加联系人事件
            mLBM.sendBroadcast(new Intent(Constants.CONTACT_INVITE_CHANGED));


        }

        @Override
        public void onFriendRequestAccepted(String var1) {
            //添加好友被接收
            Log.d("ContactListener", "onContactInvited..." + var1);
            JSONObject jsonObject = JSONObject.parseObject(var1);
            String fromPhone = jsonObject.getString("fromPhone"); //邀请人
            String fromId = jsonObject.getString("fromId"); //邀请人
            String fromUsername = jsonObject.getString("fromUsername"); //邀请人
            String reason = jsonObject.getString("reason"); //邀请原因
            //保存联系人
            UserInfo userInfo = new UserInfo();
            userInfo.setId(fromPhone);
            userInfo.setPhone(fromPhone);
            userInfo.setUsername(fromUsername);
            Model.getInstance().dbManager().getContactDao().saveContact(userInfo, true);
            //保存邀请信息表
            InvitationInfo invitationInfo = new InvitationInfo();
            invitationInfo.setReason(reason);
            invitationInfo.setUser(userInfo);
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER);
            Model.getInstance().dbManager().getInvitationDao().addInvitation(invitationInfo);
            //红点提示
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, true);
            //广播添加联系人事件
            mLBM.sendBroadcast(new Intent(Constants.CONTACT_INVITE_CHANGED));//发送联系人邀请变化广播
            mLBM.sendBroadcast(new Intent(Constants.CONTACT_CHANGED));//发送联系人变化广播
        }

        @Override
        public void onFriendRequestDeclined(String var1) {
            //红点提示
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, true);
            //广播添加联系人事件
            mLBM.sendBroadcast(new Intent(Constants.CONTACT_INVITE_CHANGED));
        }
    };


}
