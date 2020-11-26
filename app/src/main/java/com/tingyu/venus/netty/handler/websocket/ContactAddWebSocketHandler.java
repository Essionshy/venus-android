package com.tingyu.venus.netty.handler.websocket;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.tingyu.venus.event.ContactListener;
import com.tingyu.venus.model.Model;
import com.tingyu.venus.netty.protobuf.TransportMessageOuterClass;

import java.util.Iterator;
import java.util.List;

/**
 * 联系人添加消息响应处理类
 */
public class ContactAddWebSocketHandler extends AbstractWebSocketHandler {
    @Override
    protected void handleInternal(String response) {
        if (getMessageType(response).equals(TransportMessageOuterClass.MessageType.ADD_CONTACT_NOTICE.name())) {
            //处理业务
            Log.d("dispatcher", "CONTACT_ADD_NOTICE");
            callAddContactNoticeListener(getMessage(response));

        } else {
            super.handler.handle(response);
        }

    }

    private void callAddContactNoticeListener(String message) {
        Log.d("dis", "callAddContactNoticeListener");
        List<ContactListener> contactListeners = Model.getInstance().contactManager().getContactListeners();
        Iterator<ContactListener> iterator = contactListeners.iterator();
        while (iterator.hasNext()) {
            ContactListener next = iterator.next();

            doCall(message, next);

        }
    }


    private void doCall(String message, ContactListener next) {
        Log.d("dis", "doCall");
        //解析message ，获取消息的邀请状态，新邀请，接受邀请，拒绝邀请
        JSONObject jsonObject = JSONObject.parseObject(message);
        String status = jsonObject.getString("status");
        Log.d("status", status);
        switch (status) {
            case "INVITE_NEW":
                next.onContactInvited(message);
                break;
            case "INVITE_ACCEPT":
                next.onFriendRequestAccepted(message);
                break;
            case "INVITE_REJECT":
                next.onFriendRequestDeclined(message);
                break;

            default:
                break;

        }
    }
}
