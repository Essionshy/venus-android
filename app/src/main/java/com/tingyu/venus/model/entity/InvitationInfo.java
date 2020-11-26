package com.tingyu.venus.model.entity;

/**
 * 联系人邀请信息
 */
public class InvitationInfo {

    private Integer id;

    private UserInfo user; //联系人邀请

    private GroupInfo group; //群组邀请

    private String reason; //邀请原因

    private InvitationStatus status;//邀请状态

    public enum InvitationStatus {
        NEW_INVITE, //新的邀请

        INVITE_ACCEPT, //接受邀请

        INVITE_ACCEPT_BY_PEER, //邀请被接受

        // 群组邀请的状态
        NEW_GROUP_INVITE,

        NEW_GROUP_APPLICATION;


    }

    public InvitationInfo() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public GroupInfo getGroup() {
        return group;
    }

    public void setGroup(GroupInfo group) {
        this.group = group;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public InvitationStatus getStatus() {
        return status;
    }

    public void setStatus(InvitationStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "InvitationInfo{" +
                "id=" + id +
                ", user=" + user +
                ", group=" + group +
                ", reason='" + reason + '\'' +
                ", status=" + status +
                '}';
    }
}
