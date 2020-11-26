package com.tingyu.venus.model.entity;

/**
 * 单聊记录
 */
public class ChatRecord {

    private String id; //记录ID

    private String groupId;//群ID 如果联系人不在任何群，设置groupId=0;

    private String contactId;//联系人ID

    private String avatar;//

    private String content; //内容

    private ContentType contentType; //内容类型

    private MessageType messageType;//消息类型，接受还是发送


    public ChatRecord() {
    }

    public ChatRecord(String contactId) {
        this.contactId = contactId;
    }

    public ChatRecord(String id, String contactId, String avatar, String content, ContentType contentType) {
        this.id = id;
        this.contactId = contactId;
        this.avatar = avatar;
        this.content = content;
        this.contentType = contentType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public enum ContentType {
        TEXT,
        IMAGE,
        VOICE,
        VIDEO;
    }

    public enum MessageType {
        RECEIVE, //接收的的消息
        SEND;//发送消息
    }
}
