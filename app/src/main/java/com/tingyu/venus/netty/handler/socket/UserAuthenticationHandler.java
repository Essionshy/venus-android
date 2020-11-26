package com.tingyu.venus.netty.handler.socket;

import android.util.Log;

import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import com.tingyu.venus.event.ContactListener;
import com.tingyu.venus.model.Model;
import com.tingyu.venus.netty.protobuf.TransportMessageOuterClass;

import java.util.Iterator;
import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class UserAuthenticationHandler extends SimpleChannelInboundHandler<TransportMessageOuterClass.TransportMessage> {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

      /*  TransportMessageOuterClass.TransportMessage.Builder builder = TransportMessageOuterClass.TransportMessage.newBuilder();
        builder.setMessageType(TransportMessageOuterClass.MessageType.UserAuthenticationNotice);
        TransportMessageOuterClass.TransportMessage message = builder.build();
        ctx.writeAndFlush(message);*/


        Log.d("ContactChanged", "联系人变化事件");
        TransportMessageOuterClass.TransportMessage.Builder builder = TransportMessageOuterClass.TransportMessage.newBuilder();
        /*builder.setMessageId(10303);
        builder.setMessageType(TransportMessageOuterClass.MessageType.UserAuthenticationNotice);
*/
        Any.Builder newBuilder = Any.newBuilder();
        newBuilder.setValue(ByteString.copyFrom("你是个什么东西", CharsetUtil.UTF_8));
        builder.setMessageBody(newBuilder);
        TransportMessageOuterClass.TransportMessage message = builder.build();
        ctx.writeAndFlush(message);

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TransportMessageOuterClass.TransportMessage msg) throws Exception {

        Log.d("userAuth","接收到服务器回送的消息");
        //解析服务器回传数据

        TransportMessageOuterClass.MessageType messageType = msg.getMessageType();
        if (messageType != null && messageType.equals(TransportMessageOuterClass.MessageType.UserAuthenticationNotice)) {
            //
            List<ContactListener> contactListeners = Model.getInstance().contactManager().getContactListeners();
            Iterator<ContactListener> iterator = contactListeners.iterator();
            while (iterator.hasNext()){
                ContactListener contactListener = iterator.next();
                contactListener.onContactAdded("你是个什么东西...");
            }
        }
        //告诉服务器消息已经收到


    }
}
