package com.tingyu.venus.chat;

import com.tingyu.venus.event.ChatListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 单聊管理器
 */
public class ChatManager {

    private static List<ChatListener> chatListeners = Collections.synchronizedList(new ArrayList<>());


    public List<ChatListener> getChatListeners() {
        return chatListeners;
    }

    /**
     * 设置单聊监听
     *
     * @param listener
     */
    public void setChatListener(ChatListener listener) {

        if (!this.chatListeners.contains(listener)) {
            this.chatListeners.add(listener);
        }
    }

    /**
     * 移除单聊监听
     *
     * @param listener
     */
    public void removeChatListener(ChatListener listener) {
        this.chatListeners.remove(listener);
    }

}
