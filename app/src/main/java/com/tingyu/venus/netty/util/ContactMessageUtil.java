package com.tingyu.venus.netty.util;

import com.tingyu.venus.model.entity.InvitationInfo;
import com.tingyu.venus.model.entity.UserInfo;
import com.tingyu.venus.netty.NettyClientConnectUtil;
import com.tingyu.venus.netty.handler.socket.ContactAddNoticeHandler;
import com.tingyu.venus.netty.protobuf.ContactAddNotice;

/**
 * 联系人消息工具类
 */
public class ContactMessageUtil {


    public static void sendMessage(InvitationInfo invitationInfo, UserInfo contact, ContactAddNotice.ContactAddNoticeMessage.InvitationStatus status){
        ContactAddNotice.ContactAddNoticeMessage.Builder builder = ContactAddNotice.ContactAddNoticeMessage.newBuilder();
        builder.setFromPhone(invitationInfo.getUser().getPhone());
        builder.setToPhone(contact.getPhone()); //通知查询的联系人获取联系电话
        builder.setReason(invitationInfo.getReason());
        builder.setStatus(status);
        ContactAddNotice.ContactAddNoticeMessage message = builder.build();
        //发送添加联系人邀请
        NettyClientConnectUtil.connect(new ContactAddNoticeHandler(message));

    }

}
