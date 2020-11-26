package com.tingyu.venus.netty.handler.websocket;

import android.util.Log;

import com.tingyu.venus.event.ChatListener;
import com.tingyu.venus.model.Model;
import com.tingyu.venus.netty.protobuf.TransportMessageOuterClass;

import java.util.Iterator;
import java.util.List;

/**
 * 单聊消息处理类
 */
public class ChatWebSocketHandler extends AbstractWebSocketHandler {
    @Override
    protected void handleInternal(String response) {

        boolean flag = getMessageType(response).equals(TransportMessageOuterClass.MessageType.CHAT_MSG_NOTICE.name());

        if (flag) {
            //处理业务逻辑
            Log.d("dispatcher", "SINGLE_CHAT_NOTICE");
            callSingleChatListener(getMessage(response));

        } else {
            super.handler.handle(response);
        }

    }

    /**
     * 调用聊天监听器
     *
     * @param message
     */
    private void callSingleChatListener(String message) {
        List<ChatListener> chatListeners = Model.getInstance().chatManager().getChatListeners();
        Iterator<ChatListener> iterator = chatListeners.iterator();
        while (iterator.hasNext()) {
            ChatListener next = iterator.next();
            next.onReceived(message);
        }

    }

}
