package com.tingyu.venus.model.entity;

/**
 * 消息
 */
public class MessageInfo {

    private String id; //消息ID
    private String groupId;// 群ID,如果非群消息，则设置为0

    private String contactId; //联系人ID

    private String avatar; //联系人头像

    private String nickname; //联系人昵称

    private String newMessage;//最新消息

    private String time;// 显示消息发送时间

    private int unReadCount;// 未读消息数

    public MessageInfo() {
    }

    public MessageInfo(String id) {
        this.id = id;
    }


    public MessageInfo(String avatar, String nickname, String newMessage, String time) {
        this.avatar = avatar;
        this.nickname = nickname;
        this.newMessage = newMessage;
        this.time = time;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNewMessage() {
        return newMessage;
    }

    public void setNewMessage(String newMessage) {
        this.newMessage = newMessage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    @Override
    public String toString() {
        return "MessageInfo{" +
                "id='" + id + '\'' +
                ", groupId='" + groupId + '\'' +
                ", contactId='" + contactId + '\'' +
                ", avatar='" + avatar + '\'' +
                ", nickname='" + nickname + '\'' +
                ", newMessage='" + newMessage + '\'' +
                ", time='" + time + '\'' +
                ", unReadCount=" + unReadCount +
                '}';
    }
}
