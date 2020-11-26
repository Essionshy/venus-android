package com.tingyu.venus.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tingyu.venus.model.db.DBHelper;
import com.tingyu.venus.model.entity.ChatRecord;
import com.tingyu.venus.model.table.ChatRecordTable;

import java.util.ArrayList;
import java.util.List;

/**
 * 单聊数据库访问类
 */
public class ChatRecordDao {


    private DBHelper dbHelper;

    public ChatRecordDao(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    //保存聊天记录
    public void save(ChatRecord record) {
        //校验
        if (record == null) {
            return;
        }
        //获取数据库连接
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(ChatRecordTable.COL_ID, record.getId());
        values.put(ChatRecordTable.COL_GROUP_ID, record.getGroupId());
        values.put(ChatRecordTable.COL_CONTACT_ID, record.getContactId());
        values.put(ChatRecordTable.COL_AVATAR, record.getAvatar());
        values.put(ChatRecordTable.COL_MESSAGE_TYPE, record.getMessageType().ordinal());
        values.put(ChatRecordTable.COL_CONTENT, record.getContent());
        values.put(ChatRecordTable.COL_CONTENT_TYPE, record.getContentType().ordinal());
        db.replace(ChatRecordTable.TAB_NAME, null, values);

    }

    //获取联系人的聊天记录

    public List<ChatRecord> getSingleChatRecordByContactId(String contactId) {
        List<ChatRecord> chatRecordList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "select * from " + ChatRecordTable.TAB_NAME + " where " + ChatRecordTable.COL_CONTACT_ID + "=? and " + ChatRecordTable.COL_GROUP_ID + "=?";

        Cursor cursor = db.rawQuery(sql, new String[]{contactId, "0"});
        while (cursor.moveToNext()) {
            ChatRecord chatRecord = new ChatRecord();

            chatRecord.setId(cursor.getString(cursor.getColumnIndex(ChatRecordTable.COL_ID)));
            chatRecord.setGroupId(cursor.getString(cursor.getColumnIndex(ChatRecordTable.COL_GROUP_ID)));
            chatRecord.setContactId(cursor.getString(cursor.getColumnIndex(ChatRecordTable.COL_CONTACT_ID)));
            chatRecord.setAvatar(cursor.getString(cursor.getColumnIndex(ChatRecordTable.COL_AVATAR)));
            chatRecord.setContent(cursor.getString(cursor.getColumnIndex(ChatRecordTable.COL_CONTENT)));
            chatRecord.setContentType(converToEnumContentType(cursor.getInt(cursor.getColumnIndex(ChatRecordTable.COL_CONTENT_TYPE))));
            chatRecord.setMessageType(convertToEnumMessageType(cursor.getInt(cursor.getColumnIndex(ChatRecordTable.COL_MESSAGE_TYPE))));
            chatRecordList.add(chatRecord);
        }
        //关闭资源
        cursor.close();
        //返回结果
        return chatRecordList;

    }

    public List<ChatRecord> getGroupChatRecordByGroupId(String groupId) {
        List<ChatRecord> chatRecordList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "select * from " + ChatRecordTable.TAB_NAME + " where " + ChatRecordTable.COL_GROUP_ID + "=? ";

        Cursor cursor = db.rawQuery(sql, new String[]{groupId});
        while (cursor.moveToNext()) {
            ChatRecord chatRecord = new ChatRecord();
            chatRecord.setId(cursor.getString(cursor.getColumnIndex(ChatRecordTable.COL_ID)));
            chatRecord.setGroupId(cursor.getString(cursor.getColumnIndex(ChatRecordTable.COL_GROUP_ID)));
            chatRecord.setContactId(cursor.getString(cursor.getColumnIndex(ChatRecordTable.COL_CONTACT_ID)));
            chatRecord.setAvatar(cursor.getString(cursor.getColumnIndex(ChatRecordTable.COL_AVATAR)));
            chatRecord.setContent(cursor.getString(cursor.getColumnIndex(ChatRecordTable.COL_CONTENT)));
            chatRecord.setContentType(converToEnumContentType(cursor.getInt(cursor.getColumnIndex(ChatRecordTable.COL_CONTENT_TYPE))));
            chatRecord.setMessageType(convertToEnumMessageType(cursor.getInt(cursor.getColumnIndex(ChatRecordTable.COL_MESSAGE_TYPE))));
            chatRecordList.add(chatRecord);
        }
        //关闭资源
        cursor.close();
        //返回结果
        return chatRecordList;

    }


    private ChatRecord.MessageType convertToEnumMessageType(int type) {

        if (type == ChatRecord.MessageType.RECEIVE.ordinal()) {
            return ChatRecord.MessageType.RECEIVE;
        }


        if (type == ChatRecord.MessageType.SEND.ordinal()) {
            return ChatRecord.MessageType.SEND;
        }
        return null;

    }

    private ChatRecord.ContentType converToEnumContentType(int type) {

        if (type == ChatRecord.ContentType.TEXT.ordinal()) {
            return ChatRecord.ContentType.TEXT;
        }

        if (type == ChatRecord.ContentType.IMAGE.ordinal()) {
            return ChatRecord.ContentType.IMAGE;
        }

        if (type == ChatRecord.ContentType.VOICE.ordinal()) {
            return ChatRecord.ContentType.VOICE;
        }

        if (type == ChatRecord.ContentType.VIDEO.ordinal()) {
            return ChatRecord.ContentType.VIDEO;
        }


        return null;
    }

    /**
     * 删除单条聊天记录
     *
     * @param id
     */
    public void removeById(String id) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        db.delete(ChatRecordTable.TAB_NAME, ChatRecordTable.COL_ID + "=?", new String[]{id});

    }

    /**
     * 删除所有聊天记录
     */
    public void removeAll() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        db.delete(ChatRecordTable.TAB_NAME, null, null);
    }

    /**
     * 删除某个联系人的所有聊天记录
     *
     * @param contactId
     */
    public void removeByContactId(String contactId) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        db.delete(ChatRecordTable.TAB_NAME, ChatRecordTable.COL_CONTACT_ID + "=? and" + ChatRecordTable.COL_GROUP_ID + "=?", new String[]{contactId, "0"});

    }
}
