package com.tingyu.venus.netty.handler.socket;

import android.util.Log;

import com.google.protobuf.Message;
import com.tingyu.venus.model.Model;
import com.tingyu.venus.model.entity.GroupInfo;
import com.tingyu.venus.netty.protobuf.GroupChatCreateNotice;
import com.tingyu.venus.netty.protobuf.TransportMessageOuterClass;
import com.tingyu.venus.netty.util.MessageUtil;

import io.netty.channel.ChannelHandlerContext;

/**
 * 创建群聊
 */
public class GroupChatCreateNoticeHandler extends AbstractProtobufMessageHandler {
    //通过有参构建传入指定类型的消息
    public GroupChatCreateNoticeHandler(Message message) {
        super(message);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        MessageUtil.send(ctx, TransportMessageOuterClass.MessageType.CREATE_GROUP_CHAT_NOTICE, message);
    }

    @Override
    protected void channelRead(ChannelHandlerContext ctx, TransportMessageOuterClass.TransportMessage msg) throws Exception {
        if (msg.getMessageType().equals(TransportMessageOuterClass.MessageType.MESSAGE_RECEIVED_ACK)) {
            Log.d("del contact", "消息发送成功");

            GroupChatCreateNotice.GroupChatCreateNoticeMessage message = msg.getMessageBody().unpack(GroupChatCreateNotice.GroupChatCreateNoticeMessage.class);
            GroupInfo groupInfo = new GroupInfo();
            groupInfo.setGroupId(message.getGroupId());
            groupInfo.setInviter(message.getCreateId());
            groupInfo.setGroupName(message.getGroupName());
            groupInfo.setMembers(message.getMemberIds());
            Model.getInstance().dbManager().getGroupChatDao().save(groupInfo);


            ctx.close(); //关闭通道
        } else {
            //消息发送失败
            Log.d("del contact", "消息发送失败");
            //TODO 消息重发

        }
    }
}
