package com.tingyu.venus.model.entity;

/**
 * 用户信息
 */
public class UserInfo {
    private String id;

    private String account;

    private String username;

    private String nickname;// 昵称

    private String phone;

    private String avatar;

    public UserInfo(){}

    public UserInfo(String phone){
        this.phone=phone;
    }

    public UserInfo(String id, String account, String username, String phone, String avatar) {
        this.id = id;
        this.account = account;
        this.username = username;
        this.phone = phone;
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id='" + id + '\'' +
                ", account='" + account + '\'' +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", phone='" + phone + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
