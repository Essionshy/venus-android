package com.tingyu.venus.netty.handler.websocket;

import java.util.ArrayList;
import java.util.List;

public class WebSocketHandlerExecutionChain {

    private WebSocketHandler handler;

    //保存所有handler
    private List<WebSocketHandler> webSocketHandlers;


    public WebSocketHandlerExecutionChain() {

        //初始化容器
        initWebSocketHandlers();
        //构建执行handler处理链
        buildHandlerExecutionChain();
    }

    /**
     * 构建消息处理链
     */
    private void buildHandlerExecutionChain() {
        if (this.webSocketHandlers != null && this.webSocketHandlers.size() > 0) {
            for (int i = 0; i < this.webSocketHandlers.size(); i++) {
                if (this.webSocketHandlers.size() == i + 1) {
                    webSocketHandlers.get(i).setHandler(webSocketHandlers.get(0));
                } else {
                    webSocketHandlers.get(i).setHandler(webSocketHandlers.get(i + 1));
                }
            }
            this.handler = webSocketHandlers.get(0);//返回第一个处理器
        }
    }

    /**
     * 初始中化
     */
    private void initWebSocketHandlers() {
        this.webSocketHandlers = null; //调用前清空,确保还没有被GC时又调用该方法，造成重复添加的情况
        //注册各种handler
        this.webSocketHandlers = new ArrayList<>();

        registryWebSocketHandler(new UserOnlineNoticeWebSocketHandler());
        registryWebSocketHandler(new ContactAddWebSocketHandler());
        registryWebSocketHandler(new ChatWebSocketHandler());
        registryWebSocketHandler(new HeartbeatWebSocketHandler());
        registryWebSocketHandler(new GroupChatCreateNoticeHandler());


    }

    /**
     * 注册handler，并保证唯一
     *
     * @param handler
     */
    private void registryWebSocketHandler(WebSocketHandler handler) {
        if (!webSocketHandlers.contains(handler)) {
            webSocketHandlers.add(handler);
        }
    }

    //getter

    public WebSocketHandler getHandler() {
        return handler;
    }
}
