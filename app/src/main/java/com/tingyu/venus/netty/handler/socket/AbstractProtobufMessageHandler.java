package com.tingyu.venus.netty.handler.socket;

import com.google.protobuf.Message;
import com.tingyu.venus.netty.protobuf.TransportMessageOuterClass;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public abstract class AbstractProtobufMessageHandler  extends SimpleChannelInboundHandler<TransportMessageOuterClass.TransportMessage> {

   protected Message message;

    public AbstractProtobufMessageHandler(Message message) {
        this.message = message;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TransportMessageOuterClass.TransportMessage msg) throws Exception {
        channelRead(ctx,msg);
    }

    protected abstract void channelRead(ChannelHandlerContext ctx, TransportMessageOuterClass.TransportMessage msg) throws Exception;
}
