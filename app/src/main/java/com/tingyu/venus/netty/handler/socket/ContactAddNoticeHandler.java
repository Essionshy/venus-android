package com.tingyu.venus.netty.handler.socket;

import android.util.Log;

import com.google.protobuf.Message;
import com.tingyu.venus.netty.protobuf.TransportMessageOuterClass;
import com.tingyu.venus.netty.util.MessageUtil;

import io.netty.channel.ChannelHandlerContext;

/**
 * 添加联系人通知处理器
 */
public class ContactAddNoticeHandler extends AbstractProtobufMessageHandler{
    public ContactAddNoticeHandler(Message message) {
        super(message);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        MessageUtil.send(ctx, TransportMessageOuterClass.MessageType.ADD_CONTACT_NOTICE,message);
    }

    @Override
    protected void channelRead(ChannelHandlerContext ctx, TransportMessageOuterClass.TransportMessage msg) throws Exception {

        if(msg.getMessageType().equals(TransportMessageOuterClass.MessageType.MESSAGE_RECEIVED_ACK)){
            Log.d("add contact","消息发送成功");
            ctx.close(); //关闭通道
        }else{
            //消息发送失败
            Log.d("add contact","消息发送失败");
            //TODO 消息重发

        }
    }
}
