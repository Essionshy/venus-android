package com.tingyu.venus.model.table;

/**
 * 单聊记录表
 */
public class ChatRecordTable {

    public static final String TAB_NAME = "tb_single_chat_record";

    public static final String COL_ID = "id";
    public static final String COL_GROUP_ID = "group_id"; //默认0
    public static final String COL_CONTACT_ID = "contact_id";
    public static final String COL_AVATAR = "avatar";
    public static final String COL_CONTENT = "content";
    public static final String COL_CONTENT_TYPE = "content_type";
    public static final String COL_MESSAGE_TYPE = "message_type"; //0 接受， 1发送

    public static final String CREATE_TAB = "create table "
            + TAB_NAME + "("
            + COL_ID + " text primary key,"
            + COL_GROUP_ID + " text,"
            + COL_CONTACT_ID + " text,"
            + COL_AVATAR + " text,"
            + COL_CONTENT + " text,"
            + COL_CONTENT_TYPE + " Integer,"
            + COL_MESSAGE_TYPE + " Integer);";


}
