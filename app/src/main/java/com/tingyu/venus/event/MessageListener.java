package com.tingyu.venus.event;

/**
 * 消息监听器，服务器接收到消息后，需要回复消息是否已经收到，以此来保证消息的可靠性，如果服务器一定时间内都没有返回接收成功的消息
 * 客户端将启动消息重发机制
 */
public interface MessageListener extends EventListener{

    void onSend();

    //消息被签收
    void onReceived();

    //消息发送出错时调用
    void onError();
}
