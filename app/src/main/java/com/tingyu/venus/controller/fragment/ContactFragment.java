package com.tingyu.venus.controller.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.tingyu.venus.R;
import com.tingyu.venus.controller.activity.ContactActivity;
import com.tingyu.venus.controller.activity.ContactAddActivity;
import com.tingyu.venus.controller.activity.GroupChatActivity;
import com.tingyu.venus.controller.activity.InvitationActivity;
import com.tingyu.venus.model.Model;
import com.tingyu.venus.model.entity.UserInfo;
import com.tingyu.venus.netty.NettyClientConnectUtil;
import com.tingyu.venus.netty.handler.socket.ContactDeleteNoticeHandler;
import com.tingyu.venus.netty.protobuf.ContactDeleteNotice;
import com.tingyu.venus.utils.Constants;
import com.tingyu.venus.utils.SpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ContactFragment extends Fragment {

    private LinearLayout ll_contact_invite;
    private LinearLayout ll_group_chat;
    private ListView lv_contact_list;
    private View iv_contact_point;
    private LocalBroadcastManager mLBM;
    List<Map<String, Object>> data = new ArrayList<>();
    private BroadcastReceiver contactInviteChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //更新红点显示
            iv_contact_point.setVisibility(View.VISIBLE);
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, true);

        }
    };
    private String phone;
    /**
     * 联系人变化广播接收
     */
    private BroadcastReceiver contactChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refresh();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        intView();
        initData();
        initListener();
    }

    /**
     * 初始化列表视图数据
     */
    private void initData() {
        SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(), data, R.layout.item_contact, new String[]{"avatar", "phone"}, new int[]{R.id.iv_contact_avatar, R.id.tv_contact_phone});
        lv_contact_list.setAdapter(simpleAdapter);
        refresh();
    }


    private void initListener() {

        //红点变化

        Boolean isNewInvite = SpUtils.getInstance().getBoolean(SpUtils.IS_NEW_INVITE, false);
        iv_contact_point.setVisibility(isNewInvite ? View.VISIBLE : View.GONE);

        //注册广播
        mLBM = LocalBroadcastManager.getInstance(getContext());
        mLBM.registerReceiver(contactInviteChangeReceiver, new IntentFilter(Constants.CONTACT_INVITE_CHANGED));
        //mLBM.registerReceiver(contactChangedReceiver,new IntentFilter(Constants.CONTACT_CHANGED));

        //初始化监听器
        ll_contact_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getContext(), ContactAddActivity.class));
            }
        });

        //联系人选项单击事件

        lv_contact_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取item的选项
                Map<String, Object> item = (Map<String, Object>) parent.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                //bundle.putString("avatar", (String) item.get("avatar"));
                bundle.putString(Constants.USER_PHONE, (String) item.get("phone"));
                //跳转会话页面
                Intent intent = new Intent(getContext(), ContactActivity.class);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);

            }
        });
        /**
         * 设置长按事件
         */
        lv_contact_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //长按事件
                Map<String, Object> map = data.get(position);
                if (map != null) {
                    phone = (String) map.get("phone");
                }

                return false;
            }
        });

        //联系人新的邀请处理
        ll_contact_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //隐藏红点
                iv_contact_point.setVisibility(View.GONE);
                SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, false);
                //跳转到邀请信息列表页面
                Intent intent = new Intent(getContext(), InvitationActivity.class);
                getActivity().startActivity(intent);

            }
        });
        //点击群组进入群组列表
        ll_group_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GroupChatActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }

    private void refresh() {

        List<UserInfo> contacts = Model.getInstance().dbManager().getContactDao().getContactList();
        Iterator<UserInfo> iterator = contacts.iterator();
        while (iterator.hasNext()) {
            UserInfo contact = iterator.next();
            Map<String, Object> map = new HashMap<>();
            map.put("avatar", R.drawable.user_default_avatar);
            map.put("phone", contact.getPhone());
            data.add(map);
        }
        //刷新列表
        lv_contact_list.invalidateViews();
    }

    private void intView() {
        ll_contact_invite = getActivity().findViewById(R.id.lv_friend);
        ll_group_chat = getActivity().findViewById(R.id.ll_group_chat);
        lv_contact_list = getActivity().findViewById(R.id.lv_contact_list);
        iv_contact_point = getActivity().findViewById(R.id.iv_contact_point);
        //联系人列表绑定弹出菜单
        registerForContextMenu(lv_contact_list);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //加载
        getActivity().getMenuInflater().inflate(R.menu.menu_delete, menu);
    }

    /**
     * 弹出菜单
     *
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        //如果是选择删除菜单项
        if (item.getItemId() == R.id.menu_invite_delete) {
            Toast.makeText(getContext(), phone, Toast.LENGTH_LONG).show();

            //删除本地联系人相关的信息
            UserInfo currentUser = Model.getInstance().getUserDao().getCurrentUser();

            if (currentUser != null) {
                removeFromServer(currentUser.getPhone(), phone);
                removeLocalContactInfo(currentUser, phone);
            }

            //刷新联系人列表
            refresh();


        }

        return super.onContextItemSelected(item);

    }

    /**
     * 删除联系人相关
     *
     * @param currentUser
     * @param contactId
     */
    private void removeLocalContactInfo(UserInfo currentUser, String contactId) {
        //删除本地联系人
        Model.getInstance().dbManager().getContactDao().deleteContactByPhone(contactId);
        //删除本地联系人的新消息提示
        Model.getInstance().dbManager().getMessageDao().remove(contactId);
        //删除本地联系人的聊天记录
        Model.getInstance().dbManager().getChatRecordDao().removeByContactId(contactId);


    }

    /**
     * @param phone
     */
    private void removeFromServer(String userPhone, String phone) {

        Model.getInstance().getExecutor().execute(() -> {
            ContactDeleteNotice.ContactDeleteNoticeMessage.Builder builder = ContactDeleteNotice.ContactDeleteNoticeMessage.newBuilder();
            builder.setFrom(userPhone);
            builder.setTo(phone);
            ContactDeleteNotice.ContactDeleteNoticeMessage message = builder.build();
            NettyClientConnectUtil.connect(new ContactDeleteNoticeHandler(message));
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //关闭广播,注册后一定要关闭
        mLBM.unregisterReceiver(contactInviteChangeReceiver);
        // mLBM.unregisterReceiver(contactChangedReceiver);
    }


}
