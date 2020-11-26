package com.tingyu.venus.netty.handler.socket;

import android.util.Log;

import com.google.protobuf.Message;
import com.tingyu.venus.netty.protobuf.TransportMessageOuterClass;
import com.tingyu.venus.netty.util.MessageUtil;

import io.netty.channel.ChannelHandlerContext;

/**
 * 用户下线通知
 */
public class UserOfflineNoticeHandler extends AbstractProtobufMessageHandler {


    public UserOfflineNoticeHandler(Message message) {
        super(message);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        MessageUtil.send(ctx, TransportMessageOuterClass.MessageType.USER_OFFLINE_NOTICE,message);
    }

    @Override
    protected void channelRead(ChannelHandlerContext ctx, TransportMessageOuterClass.TransportMessage msg) throws Exception {
        if(msg.getMessageType().equals(TransportMessageOuterClass.MessageType.MESSAGE_RECEIVED_ACK)){
            Log.d("response","消息发送成功");
            //如果没有关闭通道，会造成缓存区被占满，发送一定数据量的消息后，服务器将不再处理请求
            ctx.close();
        }else {
            Log.d("response","消息发送遇到问题");
//            TimeUnit.SECONDS.sleep(2);
            //消息发送失败，2s后重试

        }
    }
}
