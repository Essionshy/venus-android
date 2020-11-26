package com.tingyu.venus.netty.handler.socket;

import android.util.Log;

import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import com.tingyu.venus.netty.protobuf.TransportMessageOuterClass;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class ContactChangedHandler extends SimpleChannelInboundHandler<TransportMessageOuterClass.TransportMessage> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Log.d("ContactChanged","联系人变化事件");
        TransportMessageOuterClass.TransportMessage.Builder builder = TransportMessageOuterClass.TransportMessage.newBuilder();
      //  builder.setMessageId(10303);
       // builder.setMessageType(TransportMessageOuterClass.MessageType.UserAuthenticationNotice);

        Any.Builder newBuilder = Any.newBuilder();
        newBuilder.setValue(ByteString.copyFrom("你是个什么东西", CharsetUtil.UTF_8));
        builder.setMessageBody(newBuilder);
        TransportMessageOuterClass.TransportMessage message = builder.build();
        ctx.writeAndFlush(message);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TransportMessageOuterClass.TransportMessage msg) throws Exception {

    }
}
