package com.tingyu.venus.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tingyu.venus.model.db.DBHelper;
import com.tingyu.venus.model.entity.GroupInfo;
import com.tingyu.venus.model.table.GroupChatTable;

import java.util.ArrayList;
import java.util.List;

/**
 * 群组数据库操作类
 */
public class GroupChatDao {

    private DBHelper dbHelper;

    public GroupChatDao(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    //保存群组
    public void save(GroupInfo groupInfo) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(GroupChatTable.COL_GROUP_ID, groupInfo.getGroupId());
        values.put(GroupChatTable.COL_GROUP_NAME, groupInfo.getGroupName());
        values.put(GroupChatTable.COL_GROUP_AVATAR, groupInfo.getGroupAvatar());
        values.put(GroupChatTable.COL_GROUP_CREATE_ID, groupInfo.getInviter());
        db.replace(GroupChatTable.TAB_NAME, null, values);
    }

    /**
     * 查询群
     *
     * @return
     */
    public List<GroupInfo> getGroupInfoList() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "select * from " + GroupChatTable.TAB_NAME;
        Cursor cursor = db.rawQuery(sql, null);
        List<GroupInfo> groupInfoList = new ArrayList<>();
        while (cursor.moveToNext()) {
            GroupInfo groupInfo = new GroupInfo();
            groupInfo.setGroupId(cursor.getString(cursor.getColumnIndex(GroupChatTable.COL_GROUP_ID)));
            groupInfo.setGroupName(cursor.getString(cursor.getColumnIndex(GroupChatTable.COL_GROUP_NAME)));
            groupInfo.setGroupAvatar(cursor.getString(cursor.getColumnIndex(GroupChatTable.COL_GROUP_AVATAR)));
            groupInfo.setInviter(cursor.getString(cursor.getColumnIndex(GroupChatTable.COL_GROUP_CREATE_ID)));
            groupInfo.setMembers(cursor.getString(cursor.getColumnIndex(GroupChatTable.COL_GROUP_MEMBER)));

            groupInfoList.add(groupInfo);
        }
        cursor.close();
        return groupInfoList;
    }

    public void remove(String groupId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        db.delete(GroupChatTable.TAB_NAME, GroupChatTable.COL_GROUP_ID + "=?", new String[]{groupId});
    }


    /**
     * 根据群ID查询
     * @param groupId
     * @return
     */
    public GroupInfo getGroupInfo(String groupId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "select * from " + GroupChatTable.TAB_NAME + " where " + GroupChatTable.COL_GROUP_ID + "=?";

        Cursor cursor = db.rawQuery(sql, new String[]{groupId});
        GroupInfo groupInfo = null;
        if (cursor.moveToNext()) {
            groupInfo = new GroupInfo();
            groupInfo.setGroupId(cursor.getString(cursor.getColumnIndex(GroupChatTable.COL_GROUP_ID)));
            groupInfo.setGroupName(cursor.getString(cursor.getColumnIndex(GroupChatTable.COL_GROUP_NAME)));
            groupInfo.setGroupAvatar(cursor.getString(cursor.getColumnIndex(GroupChatTable.COL_GROUP_AVATAR)));
            groupInfo.setInviter(cursor.getString(cursor.getColumnIndex(GroupChatTable.COL_GROUP_CREATE_ID)));
            groupInfo.setMembers(cursor.getString(cursor.getColumnIndex(GroupChatTable.COL_GROUP_MEMBER)));

        }
        cursor.close();
        return groupInfo;

    }
}
