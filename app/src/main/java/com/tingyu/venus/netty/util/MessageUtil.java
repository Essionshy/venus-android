package com.tingyu.venus.netty.util;

import com.google.protobuf.Any;
import com.google.protobuf.Message;
import com.tingyu.venus.netty.protobuf.TransportMessageOuterClass;

import io.netty.channel.ChannelHandlerContext;

/**
 * Socket 消息工具类
 */
public class MessageUtil {


    public static final String MESSAGE_TYPE = "messageType";//获取响应消息类型
    public static final String MESSAGE = "message"; //获取响应消息体
    public static final String REQUEST_MESSAGE_BODY ="messageBody" ;

    public static  void send(ChannelHandlerContext ctx, TransportMessageOuterClass.MessageType messageType, Message request){
        TransportMessageOuterClass.TransportMessage.Builder builder = TransportMessageOuterClass.TransportMessage.newBuilder();
        //TODO 设置消息ID
        builder.setMessageId(MessageIdGenerator.getId());
        //设置消息类型
        builder.setMessageType(messageType);
        //封装聊天消息到传输消息对象中
        builder.setMessageBody(Any.pack(request));
        TransportMessageOuterClass.TransportMessage transportMessage = builder.build();
        //socket 发送消息
        ctx.writeAndFlush(transportMessage);


    }


}
