package com.tingyu.venus.netty.handler.websocket;

import com.tingyu.venus.event.ContactListener;
import com.tingyu.venus.model.Model;
import com.tingyu.venus.netty.protobuf.TransportMessageOuterClass;

import java.util.Iterator;
import java.util.List;

/**
 * 用户上线通知接收处理类
 */
public class UserOnlineNoticeWebSocketHandler extends AbstractWebSocketHandler{
    @Override
    protected void handleInternal(String response) {
        if(getMessageType(response).equals(TransportMessageOuterClass.MessageType.USER_ONLINE_NOTICE.name())){

            //处理业务逻辑
            callContactListener(getMessage(response));

        }else{
            super.handler.handle(response);//自己不能处理的，交给下一个handler处理
        }
    }


    /**
     * 调用联系人监听器的上线方法
     * @param message
     */
    private void callContactListener(String message) {
        List<ContactListener> contactListeners = Model.getInstance().contactManager().getContactListeners();
        Iterator<ContactListener> iterator = contactListeners.iterator();
        while (iterator.hasNext()){
            ContactListener next = iterator.next();
            next.onContactOnline(message);
        }

    }

}
