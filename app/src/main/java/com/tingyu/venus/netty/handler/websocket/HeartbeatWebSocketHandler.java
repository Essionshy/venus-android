package com.tingyu.venus.netty.handler.websocket;

import android.util.Log;

import com.tingyu.venus.netty.protobuf.TransportMessageOuterClass;

/**
 * 心跳检测响应消息处理器
 */
public class HeartbeatWebSocketHandler extends AbstractWebSocketHandler {
    @Override
    protected void handleInternal(String response) {
        if (getMessageType(response).equals(TransportMessageOuterClass.MessageType.HEARTBEAT_NOTICE.name())) {
            //处理心跳响应
            Log.d("heartbeat", "客户端与服务器WebSocket连接正常");
        } else {
            super.handler.handle(response);
        }
    }
}
