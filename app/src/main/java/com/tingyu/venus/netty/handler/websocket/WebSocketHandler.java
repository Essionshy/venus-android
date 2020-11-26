package com.tingyu.venus.netty.handler.websocket;

/**
 * WebSocket响应处理类
 */
public interface WebSocketHandler {

    void setHandler(WebSocketHandler handler);
    void handle(String response); //处理服务器返回的Json字符串消息

}
