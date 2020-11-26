package com.tingyu.venus.event;

/**
 * 聊天消息监听器
 */
public interface ChatListener extends  EventListener{

   void onReceived(String message);

   void onError(String message);

}
