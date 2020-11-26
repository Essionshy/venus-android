package com.tingyu.venus.utils;

public final class Constants {

//    public static final String HTTP_BASE_URL = "http://192.168.0.105:9001/venus/api";
//    public static final String WS_BASE_URL = "http://192.168.0.105:11099/ws";
     public static final String WS_BASE_URL = "http://192.168.0.103:11099/ws";
     public static final String HTTP_BASE_URL = "http://192.168.0.103:9001/venus/api";


    public static final String USER_PHONE_IS_EMPTY = "用户手机号不能为空！！！";
    public static final String USER_PASSWORD_IS_EMPTY = "用户密码不能为空！！！";
    public static final String USER_LOGIN_FAILD = "用户登录失败";
    public static final String USER_LOGIN_SUCCESS = "登录成功";
    public static final String CONTACT_CHANGED = "contact_changed";//联系人变化的广播
    public static final String CONTACT_ON_LINE = "contact_online";//联系人上线的广播
    public static final String CONTACT_OFF_LINE = "contact_offline";//联系人下线的广播
    public static final String CHAT_CHANGED_NOTICE = "single_chat";//单聊广播
    public static final String CONTACT_INVITE_CHANGED = "contact_invite_changed";//联系人邀请变化的广播
    public static final String GROUP_CHAT_CHANGED = "group_chat_changed";//发送群组变化广播
    public static final String NEW_MSG_NOTICE = "new_msg_notice"; //新消息通知广播

    /*常量*/
    public static final String HEARTBEAT_NOTICE = "are you ok?"; //发送心跳检测消息
    public static final String DEFAULT_GROUP_NAME = "群聊";

    public static final String GROUP_ID = "group_chat_id";
    public static final String USER_PHONE = "user_phone";

    public static final String GROUP_CHANGED = "group_changed";
}
