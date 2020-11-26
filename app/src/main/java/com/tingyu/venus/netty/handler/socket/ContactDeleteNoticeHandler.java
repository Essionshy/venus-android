package com.tingyu.venus.netty.handler.socket;

import android.util.Log;

import com.google.protobuf.Message;
import com.tingyu.venus.netty.protobuf.TransportMessageOuterClass;
import com.tingyu.venus.netty.util.MessageUtil;

import io.netty.channel.ChannelHandlerContext;

/**
 * 删除联系人通知
 */
public class ContactDeleteNoticeHandler extends AbstractProtobufMessageHandler{

    public ContactDeleteNoticeHandler(Message message) {
        super(message);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //发送联系人删除通知
        MessageUtil.send(ctx, TransportMessageOuterClass.MessageType.DELETE_CONTACT_NOTICE,message);
    }

    @Override
    protected void channelRead(ChannelHandlerContext ctx, TransportMessageOuterClass.TransportMessage msg) throws Exception {


        if(msg.getMessageType().equals(TransportMessageOuterClass.MessageType.MESSAGE_RECEIVED_ACK)){
            Log.d("del contact","消息发送成功");
            ctx.close(); //关闭通道
        }else{
            //消息发送失败
            Log.d("del contact","消息发送失败");
            //TODO 消息重发

        }
    }
}
