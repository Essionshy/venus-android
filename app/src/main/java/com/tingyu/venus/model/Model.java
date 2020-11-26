package com.tingyu.venus.model;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.tingyu.venus.chat.ChatManager;
import com.tingyu.venus.chat.ContactManager;
import com.tingyu.venus.chat.GroupChatManager;
import com.tingyu.venus.model.dao.UserDao;
import com.tingyu.venus.model.db.DBManager;
import com.tingyu.venus.model.entity.UserInfo;
import com.tingyu.venus.service.HeartbeatService;
import com.tingyu.venus.utils.SpUtils;
import com.tingyu.venus.utils.threadpool.SingleThreadPoolExecutor;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * 全局数据处理器，本应用采用mvc的架构模式，controller需要的数据统一经过Model类来实现数据的解耦
 */
public class Model {

    private DBManager dbManager;
    private Handler handler;
    private Context context;
    private UserDao userDao;

    //采用静态方式生成单实例
    private static Model model = new Model();

    private ExecutorService executor;
    private WebSocketManager webSocketManager;


    //构造方法私有化
    private Model() {
    }

    //对外提供访问实例
    public static Model getInstance() {
        return model;
    }

    //初始化方法，将应用程序上下文对象注入
    public void init(Context context) {
        this.context = context;
        //创建UserDao实例
        userDao = new UserDao(context);

        LocalBroadcastManager instance = LocalBroadcastManager.getInstance(context);
        instance.sendBroadcast(new Intent());

        //初始化全局handler
        initHandler();
        //初始化全局线程池
        this.executor = SingleThreadPoolExecutor.newInstance();
    }

    private void initHandler() {

        this.handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Log.d("global handler", "全局消息处理器");
                switch (msg.what) {
                    //用户上线通知
                    case 1010:
                        Toast.makeText(context, msg.getData().getString("message"), Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }

            }
        };
    }


    public ExecutorService getExecutor() {
        return executor;
    }

    public Context getContext() {
        return context;
    }

    //通过Model获得UserDao实例
    public UserDao getUserDao() {
        return userDao;
    }

    /**
     * 处理登录成功
     */
    public void isLoginSuccess(UserInfo currentUser) {

        //TODO 登录成功后保存用户信息到本地数据库
        if (currentUser == null) {
            return;
        }
        if (dbManager != null) {
            //关闭之前
            dbManager.close();
        }
        dbManager = new DBManager(context, "contact_" + currentUser.getPhone());
        // 加载联系人列表

        List<UserInfo> userInfoList = contactManager().asyncGetContactList(currentUser.getPhone());
        //将联系人保存到数据库
        dbManager.getContactDao().saveContacts(userInfoList, true);

        //登录成功后，与服务器建立WebSocket长连接
        webSocketManager = new WebSocketManager();
        //启动心跳检测服务
        this.context.startService(new Intent(context, HeartbeatService.class));


    }

    /**
     * 判断之前是否登录过
     *
     * @return
     */
    public boolean isLoginedBefore() {
        return SpUtils.getInstance().getBoolean(SpUtils.IS_LOGINED_BEFORE, false);
    }

    /**
     * 获取联系人管理器
     *
     * @return
     */
    public ContactManager contactManager() {
        return new ContactManager();
    }

    public Handler getHandler() {
        return this.handler;
    }

    /**
     * 获取聊天管理器
     *
     * @return
     */
    public ChatManager chatManager() {
        return new ChatManager();
    }

    /**
     * 获取群聊管理器
     *
     * @return
     */
    public GroupChatManager groupChatManager() {
        return new GroupChatManager();
    }

    /**
     * 获取数据管理器
     *
     * @return
     */
    public DBManager dbManager() {
        return dbManager;
    }

    /**
     * 获取WebSocket连接
     *
     * @return
     */

    public WebSocketManager getWebSocketManager() {
        return webSocketManager;
    }
}
