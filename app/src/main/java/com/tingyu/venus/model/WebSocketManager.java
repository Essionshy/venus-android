package com.tingyu.venus.model;

import com.tingyu.venus.netty.NettyClientConnectUtil;
import com.tingyu.venus.netty.handler.websocket.WebSocketHandler;
import com.tingyu.venus.netty.handler.websocket.WebSocketHandlerExecutionChain;

import okhttp3.WebSocket;

public class WebSocketManager {

    private WebSocket webSocket;

    private WebSocketHandler handler;

    public WebSocketManager() {
        reconnect();
        //实例化
        WebSocketHandlerExecutionChain executionChain = new WebSocketHandlerExecutionChain();
        this.handler = executionChain.getHandler();
    }

    /**
     * 失败重联
     */
    public void reconnect(){
        this.webSocket= NettyClientConnectUtil.wsconnect();

    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    public void close(){
        webSocket.cancel();
    }

    public WebSocketHandler getHandler() {
        return handler;
    }
}
