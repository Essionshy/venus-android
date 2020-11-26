package com.tingyu.venus.event;

/**
 * 联系人事件监听器
 */
public interface ContactListener extends EventListener {


    /**
     * 联系人上线通知
     * @param var1
     */
    void onContactOnline(String var1);

    /**
     * 联系人下线通知
     * @param var1
     */
    void onContactOffline(String var1);

    /**
     * 联系被添加
     * @param var1  用户ID
     */
    void onContactAdded(String var1);



    /**
     *  联系人被删除
     * @param var1
     */
    void onContactDeleted(String var1);

    //联系人被邀请
    void onContactInvited(String var1);

    //添加好友被接受
    void onFriendRequestAccepted(String var1);

    //添加好友被删除
    void onFriendRequestDeclined(String var1);




}
