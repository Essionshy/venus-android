package com.tingyu.venus.model.entity;

/**
 * 群组
 */
public class GroupInfo {

    private String groupId;//群ID

    private String groupName;//群名称

    private String groupAvatar;//群头像

    private String groupHolder;//群主ID

    private String inviter;// 邀请人

    private String members;//群成员

    public GroupInfo() {
    }


    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getInviter() {
        return inviter;
    }

    public void setInviter(String inviter) {
        this.inviter = inviter;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupHolder() {
        return groupHolder;
    }

    public void setGroupHolder(String groupHolder) {
        this.groupHolder = groupHolder;
    }

    public GroupInfo(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupAvatar() {
        return groupAvatar;
    }

    public void setGroupAvatar(String groupAvatar) {
        this.groupAvatar = groupAvatar;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return "GroupInfo{" +
                "groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                ", inviter=" + inviter +
                '}';
    }
}
