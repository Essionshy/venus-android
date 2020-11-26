package com.tingyu.venus.netty.handler.socket;

import android.util.Log;

import com.tingyu.venus.netty.protobuf.ChatNotice;
import com.tingyu.venus.netty.protobuf.TransportMessageOuterClass;
import com.tingyu.venus.netty.util.MessageUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 单聊消息处理器
 */
public class ChatMessageHandler extends SimpleChannelInboundHandler<TransportMessageOuterClass.TransportMessage> {

    private ChatNotice.ChatNoticeMessage message;


    public ChatMessageHandler(ChatNotice.ChatNoticeMessage message) {
        this.message = message;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {


        doSend(ctx);



    }

    /**
     *
     * @param ctx
     */
    private void doSend(ChannelHandlerContext ctx) {
        MessageUtil.send(ctx,TransportMessageOuterClass.MessageType.CHAT_MSG_NOTICE,message);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TransportMessageOuterClass.TransportMessage msg) throws Exception {
        //服务器返回消息是否签收
        if(msg.getMessageType().equals(TransportMessageOuterClass.MessageType.MESSAGE_RECEIVED_ACK)){
            Log.d("response","消息发送成功");
            //如果没有关闭通道，会造成缓存区被占满，发送一定数据量的消息后，服务器将不再处理请求
            ctx.close();
        }else {
            Log.d("response","消息发送遇到问题");
//            TimeUnit.SECONDS.sleep(2);
            //消息发送失败，2s后重试
            doSend(ctx);
        }
    }
}
