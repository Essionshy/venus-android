package com.tingyu.venus.listener;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.tingyu.venus.model.Model;
import com.tingyu.venus.model.entity.UserInfo;
import com.tingyu.venus.netty.protobuf.TransportMessageOuterClass;
import com.tingyu.venus.netty.util.MessageUtil;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * 用户上线通知WebSocket监听器
 */
public class DispatcherWebSocketListener extends WebSocketListener {


    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        //发送用户上线通知
        sendUserOnlineNotice(webSocket);

    }

    private void sendUserOnlineNotice(WebSocket webSocket) {
        //获取用户信息
        UserInfo currentUser = Model.getInstance().getUserDao().getCurrentUser();
        //用户上线，通知服务
        JSONObject param = new JSONObject();
        param.put(MessageUtil.MESSAGE_TYPE, TransportMessageOuterClass.MessageType.USER_ONLINE_NOTICE);
        JSONObject messageBody = new JSONObject();
        messageBody.put("userPhone", currentUser.getPhone());
        param.put(MessageUtil.REQUEST_MESSAGE_BODY, messageBody);
        String jsonString = param.toJSONString();
        Log.d("WebSocket send message", jsonString);
        //发送
        webSocket.send(jsonString);
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        Model.getInstance().getWebSocketManager().getHandler().handle(text);
    }


    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        super.onClosing(webSocket, code, reason);
        Log.d("WebSocket", "与服务务器断开websocket连接");
    }



    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        Log.d("online notice", "通道已关闭");

    }
}
