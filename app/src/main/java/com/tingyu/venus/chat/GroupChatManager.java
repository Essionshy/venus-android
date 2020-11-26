package com.tingyu.venus.chat;

import com.tingyu.venus.event.GroupChatListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 群聊管理器
 */
public class GroupChatManager {

    private static List<GroupChatListener> groupChatListeners= Collections.synchronizedList(new ArrayList<>());

    public List<GroupChatListener> getGroupChatListeners() {
        return groupChatListeners;
    }

    /**
     * 设置群聊监听
     * @param listener
     */
    public void setGroupChatListener(GroupChatListener listener){

        if(!this.groupChatListeners.contains(listener)){
            this.groupChatListeners.add(listener);
        }
    }

    /**
     * 移除群聊监听
     * @param listener
     */
    public void removeGroupChatListener(GroupChatListener listener){
        this.groupChatListeners.remove(listener);
    }




}
