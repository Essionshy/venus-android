package com.tingyu.venus.service;

import android.util.Log;

import com.google.protobuf.Message;
import com.tingyu.venus.netty.handler.socket.AbstractProtobufMessageHandler;
import com.tingyu.venus.netty.protobuf.TransportMessageOuterClass;
import com.tingyu.venus.netty.util.MessageUtil;

import io.netty.channel.ChannelHandlerContext;

/**
 * 创建群聊处理器
 */
public class GroupChatCreateHandler extends AbstractProtobufMessageHandler {

    public GroupChatCreateHandler(Message message) {
        super(message);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        MessageUtil.send(ctx, TransportMessageOuterClass.MessageType.CREATE_GROUP_CHAT_NOTICE,message);
    }

    @Override
    protected void channelRead(ChannelHandlerContext ctx, TransportMessageOuterClass.TransportMessage msg) throws Exception {
        if(msg.getMessageType().equals(TransportMessageOuterClass.MessageType.MESSAGE_RECEIVED_ACK)){
            Log.d("create group","消息发送成功");
            ctx.close(); //关闭通道
        }else{
            //消息发送失败
            Log.d("create group","消息发送失败");
            //TODO 消息重发
            MessageUtil.send(ctx, TransportMessageOuterClass.MessageType.CREATE_GROUP_CHAT_NOTICE,message);
        }
    }
}
