package com.tingyu.venus.model.db;

import android.content.Context;

import com.tingyu.venus.model.dao.ContactDao;
import com.tingyu.venus.model.dao.GroupChatDao;
import com.tingyu.venus.model.dao.InvitationDao;
import com.tingyu.venus.model.dao.MessageDao;
import com.tingyu.venus.model.dao.ChatRecordDao;

/**
 * 数据库管理器， 统一管理联系人表和邀请信息表的操作类
 */
public class DBManager {

    private final DBHelper dbHelper;
    private final ContactDao contactDao;
    private final InvitationDao invitationDao;
    private final ChatRecordDao chatRecordDao;
    private final MessageDao messageDao;
    private final GroupChatDao groupChatDao;

    public DBManager(Context context, String name) {
        dbHelper = new DBHelper(context, name);
        //创建表操作类的实例
        contactDao = new ContactDao(dbHelper);
        invitationDao = new InvitationDao(dbHelper);
        //实例化聊天记录操作类
        chatRecordDao = new ChatRecordDao(dbHelper);
        //实例化联系人消息记录操作类
        messageDao = new MessageDao(dbHelper);
        //实例化群组
        groupChatDao = new GroupChatDao(dbHelper);

    }

    public DBHelper getDbHelper() {
        return dbHelper;
    }

    public ContactDao getContactDao() {
        return contactDao;
    }

    public InvitationDao getInvitationDao() {
        return invitationDao;
    }

    public ChatRecordDao getChatRecordDao() {
        return chatRecordDao;
    }

    public MessageDao getMessageDao() {
        return messageDao;
    }

    public GroupChatDao getGroupChatDao() {
        return groupChatDao;
    }

    //关闭数据库的方法
    public void close() {
        dbHelper.close();
    }


}
