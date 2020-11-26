package com.tingyu.venus.netty.handler.websocket;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.tingyu.venus.netty.protobuf.TransportMessageOuterClass;
import com.tingyu.venus.netty.util.MessageUtil;

import java.util.ArrayList;
import java.util.List;

import io.netty.util.internal.StringUtil;

/**
 * 抽象责任链模式，根据消息类型找到对应的处理类
 */
public abstract class AbstractWebSocketHandler implements WebSocketHandler {

    protected WebSocketHandler handler;

    private static List<String> supportMessageTypes = new ArrayList<>();

    static {
        supportMessageTypes.add(TransportMessageOuterClass.MessageType.HEARTBEAT_NOTICE.name());
        supportMessageTypes.add(TransportMessageOuterClass.MessageType.USER_ONLINE_NOTICE.name());
        supportMessageTypes.add(TransportMessageOuterClass.MessageType.USER_OFFLINE_NOTICE.name());
        supportMessageTypes.add(TransportMessageOuterClass.MessageType.CHAT_MSG_NOTICE.name());
        supportMessageTypes.add(TransportMessageOuterClass.MessageType.ADD_CONTACT_NOTICE.name());
        supportMessageTypes.add(TransportMessageOuterClass.MessageType.CREATE_GROUP_CHAT_NOTICE.name());
    }

    @Override
    public void setHandler(WebSocketHandler handler) {
        this.handler = handler;
    }


    @Override
    public void handle(String response) {

        if (!supportMessageTypes.contains(getMessageType(response))) {
            Log.d("handler", "不支持的消息类型");
            return;
        }

        handleInternal(response);
    }

    //交由子类处理具体的业务逻辑
    protected abstract void handleInternal(String response);

    /**
     * 返回消息类型
     *
     * @param response
     */
    protected String getMessageType(String response) {
        if (response == null || StringUtil.isNullOrEmpty(response)) {
            return null;
        }
        //解析响应消息，返回消息类型的字符串值
        try {
            JSONObject jsonObject = JSONObject.parseObject(response);
            String messageType = jsonObject.getString(MessageUtil.MESSAGE_TYPE);
            return messageType;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 根据服务器响应获取消息
     *
     * @param response
     * @return
     */
    protected String getMessage(String response) {
        if (response == null || StringUtil.isNullOrEmpty(response)) {
            return null;
        }
        //解析响应消息，返回消息类型的字符串值
        try {
            JSONObject jsonObject = JSONObject.parseObject(response);
            String messageType = jsonObject.getString(MessageUtil.MESSAGE);
            return messageType;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
