package com.tingyu.venus.netty.handler.websocket;

import android.util.Log;

import com.tingyu.venus.event.GroupChatListener;
import com.tingyu.venus.model.Model;
import com.tingyu.venus.netty.protobuf.TransportMessageOuterClass;

import java.util.Iterator;
import java.util.List;

import io.netty.util.internal.StringUtil;

/**
 * 创建群通知消息处理器
 */
public class GroupChatCreateNoticeHandler extends AbstractWebSocketHandler {


    @Override
    protected void handleInternal(String response) {

        if (getMessageType(response).equals(TransportMessageOuterClass.MessageType.CREATE_GROUP_CHAT_NOTICE.name())) {
            //处理创建群信息
            callGroupChatCreateListener(getMessage(response));


        } else {
            super.handler.handle(response);
        }

    }

    /**
     * 调用群创建事件监听器
     *
     * @param message
     */
    private void callGroupChatCreateListener(String message) {
        Log.d("group chat lis", message);
        if (message == null || StringUtil.isNullOrEmpty(message)) {
            return;
        }

        List<GroupChatListener> groupChatListeners = Model.getInstance().groupChatManager().getGroupChatListeners();
        if (groupChatListeners == null || groupChatListeners.size() == 0) {
            return;
        }

        //遍历所有监听器
        Iterator<GroupChatListener> iterator = groupChatListeners.iterator();
        while (iterator.hasNext()) {
            GroupChatListener next = iterator.next();
            next.onCreate(message);//调用群创建事件监听
        }

    }
}
