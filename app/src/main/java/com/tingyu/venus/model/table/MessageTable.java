package com.tingyu.venus.model.table;

/**
 * 消息表封装字段
 */
public class MessageTable {
    
    public static final String TAB_NAME="tb_message";


    public static final String COL_ID="id"; //联系人ID 或者群ID
    public static final String COL_GROUP_ID="group_id"; //群ID
    public static final String COL_CONTACT_ID="contact_id"; //联系人ID
    public static final String COL_AVATAR="avatar"; //联系人头像
    public static final String COL_NICKNAME="nickname"; //联系人昵称
    public static final String COL_NEW_MESSAGE="new_message"; //最新消息
    public static final String COL_TIME="time"; //最新消息时间
    public static final String COL_UNREAD_COUNT="unread_count"; //未读消息数量

    //创建消息表
    public static final String CREATE_TAB="create table "
            +TAB_NAME+" ("
            +COL_ID+" text primary key,"
            +COL_GROUP_ID+" text,"
            +COL_CONTACT_ID+" text,"
            +COL_AVATAR+" text,"
            +COL_NICKNAME+" text,"
            +COL_NEW_MESSAGE+" text,"
            +COL_UNREAD_COUNT+" Integer,"
            +COL_TIME+" text);";
    
}
