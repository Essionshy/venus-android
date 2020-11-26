package com.tingyu.venus.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tingyu.venus.model.db.DBHelper;
import com.tingyu.venus.model.entity.GroupInfo;
import com.tingyu.venus.model.entity.InvitationInfo;
import com.tingyu.venus.model.entity.InvitationInfo.InvitationStatus;
import com.tingyu.venus.model.entity.UserInfo;
import com.tingyu.venus.model.table.InvitationTable;

import java.util.ArrayList;
import java.util.List;

/**
 * 邀请表操作类
 */
public class InvitationDao {

    private DBHelper dbHelper;

    public InvitationDao(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void addInvitation(InvitationInfo invitationInfo) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();

        values.put(InvitationTable.COL_REASON, invitationInfo.getReason());
        values.put(InvitationTable.COL_STATUS, invitationInfo.getStatus().ordinal());// ordinal方法，获取枚举值的序号，需要该枚举类型为public 修饰符

        //判断联系人邀请还是群组邀请
        UserInfo user = invitationInfo.getUser();
        if (user != null) {
            //联系人
            values.put(InvitationTable.COL_USER_ID, invitationInfo.getUser().getId());
            values.put(InvitationTable.COL_USERNAME, invitationInfo.getUser().getUsername());
            values.put(InvitationTable.COL_USER_PHONE, invitationInfo.getUser().getPhone());
        } else {
            //群组
            values.put(InvitationTable.COL_GROUP_ID, invitationInfo.getGroup().getGroupId());
            values.put(InvitationTable.COL_GROUP_NAME, invitationInfo.getGroup().getGroupName());
            values.put(InvitationTable.COL_USER_ID, invitationInfo.getGroup().getInviter());//设置邀请人
        }
        db.replace(InvitationTable.TAB_NAME, null, values);

    }

    //获取所有邀请信息
    public List<InvitationInfo> getInvitations() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "select * from " + InvitationTable.TAB_NAME;
        Cursor cursor = db.rawQuery(sql, null);
        List<InvitationInfo> invitationInfoList = new ArrayList<>();
        while (cursor.moveToNext()) {
            InvitationInfo invitationInfo = new InvitationInfo();
            invitationInfo.setReason(cursor.getString(cursor.getColumnIndex(InvitationTable.COL_REASON)));
            invitationInfo.setStatus(convert(cursor.getInt(cursor.getColumnIndex(InvitationTable.COL_STATUS))));

            //判断是群组还是联系人
            String groupId = cursor.getString(cursor.getColumnIndex(InvitationTable.COL_GROUP_ID));
            if(groupId==null){
                //联系人

                UserInfo userInfo = new UserInfo();
                userInfo.setId(cursor.getString(cursor.getColumnIndex(InvitationTable.COL_USER_ID)));
                userInfo.setUsername(cursor.getString(cursor.getColumnIndex(InvitationTable.COL_USERNAME)));
                userInfo.setPhone(cursor.getString(cursor.getColumnIndex(InvitationTable.COL_USER_PHONE)));
                invitationInfo.setUser(userInfo);

            }else{
                //群组

                GroupInfo groupInfo = new GroupInfo();
                groupInfo.setGroupId(cursor.getString(cursor.getColumnIndex(InvitationTable.COL_GROUP_ID)));
                groupInfo.setGroupName(cursor.getString(cursor.getColumnIndex(InvitationTable.COL_GROUP_NAME)));
                groupInfo.setInviter(cursor.getString(cursor.getColumnIndex(InvitationTable.COL_USER_ID)));

                invitationInfo.setGroup(groupInfo);
            }

            invitationInfoList.add(invitationInfo);
        }

        //关闭资源
        cursor.close();
        //返回结果
        return invitationInfoList;

    }

    private InvitationStatus convert(int status) {
        if (status == InvitationStatus.NEW_INVITE.ordinal()) {
            return InvitationStatus.NEW_INVITE;
        }
        if (status == InvitationStatus.INVITE_ACCEPT.ordinal()) {
            return InvitationStatus.INVITE_ACCEPT;
        }
        if (status == InvitationStatus.INVITE_ACCEPT_BY_PEER.ordinal()) {
            return InvitationStatus.INVITE_ACCEPT_BY_PEER;
        }
        if (status == InvitationStatus.NEW_GROUP_INVITE.ordinal()) {
            return InvitationStatus.NEW_GROUP_INVITE;
        }

        if (status == InvitationStatus.NEW_GROUP_APPLICATION.ordinal()) {
            return InvitationStatus.NEW_GROUP_APPLICATION;
        }
        return null;
    }

    //删除邀请

    public void deleteInvitation(String phone){
        if(phone==null){
            return;
        }
        //获取数据库连接
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        db.delete(InvitationTable.TAB_NAME,InvitationTable.COL_USER_PHONE+"=?",new String[]{phone});

    }

    /**
     * 根据联系人手机号，更新状态
     * @param invitationStatus
     * @param phone
     */
    public void updateInvitationStatus(InvitationStatus invitationStatus,String phone){
        if(phone==null){
            return;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();


        ContentValues values=new ContentValues();
        values.put(InvitationTable.COL_STATUS,invitationStatus.ordinal());
        db.update(InvitationTable.TAB_NAME,values,InvitationTable.COL_USER_PHONE+"=?",new String[]{phone});

    }

}
