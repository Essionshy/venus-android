package com.tingyu.venus.event;

/**
 * 群聊事件监听
 */
public interface GroupChatListener extends EventListener {

    void onCreate(String message);

    void onRemove();



}
