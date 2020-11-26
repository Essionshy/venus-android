package com.tingyu.venus.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tingyu.venus.model.db.DBHelper;
import com.tingyu.venus.model.entity.MessageInfo;
import com.tingyu.venus.model.table.MessageTable;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息数据库访问类
 */
public class MessageDao {

    private DBHelper dbHelper;

    public MessageDao(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    /**
     * 获取所有会话联系人列表
     *
     * @return
     */
    public List<MessageInfo> getMessageInfoList() {
        List<MessageInfo> messageInfoList = new ArrayList<>();
        //获取数据库连接
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //执行查询sql
        String sql = "select * from " + MessageTable.TAB_NAME;

        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            //封装返回数据
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setId(cursor.getString(cursor.getColumnIndex(MessageTable.COL_ID)));//保存联系人手机号
            messageInfo.setGroupId(cursor.getString(cursor.getColumnIndex(MessageTable.COL_GROUP_ID)));//保存联系人手机号
            messageInfo.setContactId(cursor.getString(cursor.getColumnIndex(MessageTable.COL_CONTACT_ID)));//保存联系人手机号
            messageInfo.setAvatar(cursor.getString(cursor.getColumnIndex(MessageTable.COL_AVATAR)));
            messageInfo.setNickname(cursor.getString(cursor.getColumnIndex(MessageTable.COL_NICKNAME)));
            messageInfo.setNewMessage(cursor.getString(cursor.getColumnIndex(MessageTable.COL_NEW_MESSAGE)));
            messageInfo.setUnReadCount(cursor.getInt(cursor.getColumnIndex(MessageTable.COL_UNREAD_COUNT)));
            messageInfo.setTime(cursor.getString(cursor.getColumnIndex(MessageTable.COL_TIME)));
            messageInfoList.add(messageInfo);
        }
        //关闭资源
        cursor.close();
        //返回结果集
        return messageInfoList;
    }

    /**
     * 保存最新消息
     *
     * @param messageInfo
     */
    public void save(MessageInfo messageInfo) {
        if (messageInfo == null) {
            return;
        }
        //获取数据库连接
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //执行sql
        ContentValues values = new ContentValues();
        values.put(MessageTable.COL_ID, messageInfo.getId());
        values.put(MessageTable.COL_GROUP_ID, messageInfo.getGroupId());
        values.put(MessageTable.COL_CONTACT_ID, messageInfo.getContactId());
        values.put(MessageTable.COL_AVATAR, messageInfo.getAvatar());
        values.put(MessageTable.COL_NICKNAME, messageInfo.getNickname());
        values.put(MessageTable.COL_NEW_MESSAGE, messageInfo.getNewMessage());
        values.put(MessageTable.COL_UNREAD_COUNT, messageInfo.getUnReadCount());
        values.put(MessageTable.COL_TIME, messageInfo.getTime());
        db.replace(MessageTable.TAB_NAME, null, values);
    }


    /**
     * 根据联系人手机号删除对应会话
     *
     * @param phone
     */
    public void remove(String phone) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.delete(MessageTable.TAB_NAME, MessageTable.COL_ID + "=?", new String[]{phone});
    }

    /**
     * 释放资源
     */
    public void close() {
        dbHelper.close();
    }


    public MessageInfo getMessageInfoByPhone(String contactId) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "select * from " + MessageTable.TAB_NAME + " where id=?;  ";
        Cursor cursor = db.rawQuery(sql, new String[]{contactId});
        MessageInfo messageInfo = null;
        if (cursor.moveToNext()) {
            messageInfo = new MessageInfo();
            messageInfo.setId(cursor.getString(cursor.getColumnIndex(MessageTable.COL_ID)));//保存联系人手机号
            messageInfo.setAvatar(cursor.getString(cursor.getColumnIndex(MessageTable.COL_AVATAR)));
            messageInfo.setGroupId(cursor.getString(cursor.getColumnIndex(MessageTable.COL_GROUP_ID)));
            messageInfo.setContactId(cursor.getString(cursor.getColumnIndex(MessageTable.COL_CONTACT_ID)));
            messageInfo.setNickname(cursor.getString(cursor.getColumnIndex(MessageTable.COL_NICKNAME)));
            messageInfo.setNewMessage(cursor.getString(cursor.getColumnIndex(MessageTable.COL_NEW_MESSAGE)));
            messageInfo.setUnReadCount(cursor.getInt(cursor.getColumnIndex(MessageTable.COL_UNREAD_COUNT)));
            messageInfo.setTime(cursor.getString(cursor.getColumnIndex(MessageTable.COL_TIME)));

        }
        //关闭资源
        cursor.close();
        return messageInfo;
    }

    /**
     * 更新未读消息数量
     *
     * @param contactId
     * @param count
     */
    public void updateUnreadCount(String contactId, int count) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(MessageTable.COL_UNREAD_COUNT, count);
        db.update(MessageTable.TAB_NAME, values, MessageTable.COL_ID + "=?", new String[]{contactId});
    }
}
