package com.tingyu.venus.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tingyu.venus.R;
import com.tingyu.venus.adpater.InvitationAdapter;
import com.tingyu.venus.model.Model;
import com.tingyu.venus.model.entity.InvitationInfo;
import com.tingyu.venus.model.entity.UserInfo;
import com.tingyu.venus.netty.NettyClientConnectUtil;
import com.tingyu.venus.netty.handler.socket.ContactAddNoticeHandler;
import com.tingyu.venus.netty.protobuf.ContactAddNotice;
import com.tingyu.venus.utils.Constants;

import java.util.List;

/**
 * 邀请列表显示页面
 */
public class InvitationActivity extends AppCompatActivity {

    private RecyclerView rv_invite;
    private InvitationAdapter invitationAdapter;
    private InvitationInfo invitationInfo;
    private LocalBroadcastManager mLBM;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);
        initView();
        initData();
        initListener();
        mLBM = LocalBroadcastManager.getInstance(this);
    }

    private void initListener() {


        //列表项长按事件监听  由于该事件与菜单绑定因此不需要处理
        invitationAdapter.setOnItemClickListener(new InvitationAdapter.OnItemClickListener() {
            @Override
            public void onItemLongClick(View itemView, int position) {
                invitationInfo = invitationAdapter.getInvitationInfos().get(position);

                //  Toast.makeText(InvitationActivity.this, "删除联系人"+itemView.getId()+"成功！", Toast.LENGTH_LONG).show();
            }
        });
        //创建适配器监听咕咕

        invitationAdapter.setInvitationListener(new InvitationAdapter.OnInvitationListener() {
            @Override
            public void onReceived(InvitationInfo invitationInfo) {
                //隐藏按键
                // Toast.makeText(InvitationActivity.this, invitationInfo.getUser().getPhone(),Toast.LENGTH_LONG).show();
                //更新状态

                Model.getInstance().dbManager().getInvitationDao().updateInvitationStatus(InvitationInfo.InvitationStatus.INVITE_ACCEPT, invitationInfo.getUser().getPhone());

                //发送消息通知对方
                ContactAddNotice.ContactAddNoticeMessage.Builder builder = ContactAddNotice.ContactAddNoticeMessage.newBuilder();
                builder.setToPhone(invitationInfo.getUser().getPhone());
                UserInfo currentUser = Model.getInstance().getUserDao().getCurrentUser();
                builder.setFromPhone(currentUser.getPhone());
                builder.setReason("你长得太漂亮了，约吗？");
                builder.setStatus(ContactAddNotice.ContactAddNoticeMessage.InvitationStatus.INVITE_ACCEPT);
                ContactAddNotice.ContactAddNoticeMessage message = builder.build();

                //异步执行消息发送
                NettyClientConnectUtil.connect(new ContactAddNoticeHandler(message));


                //更新联系人，将联系人保存到本地数据库
                UserInfo user = invitationInfo.getUser();
                Model.getInstance().dbManager().getContactDao().saveContact(user, true);
                //发送联系人变化的广播
                mLBM.sendBroadcast(new Intent(Constants.CONTACT_CHANGED));

            }

            @Override
            public void onRejected(InvitationInfo invitationInfo) {
                //拒绝的单击事件
                //发送消息通知对方
                ContactAddNotice.ContactAddNoticeMessage.Builder builder = ContactAddNotice.ContactAddNoticeMessage.newBuilder();
                builder.setToPhone(invitationInfo.getUser().getPhone());
                UserInfo currentUser = Model.getInstance().getUserDao().getCurrentUser();
                builder.setFromPhone(currentUser.getPhone());
                builder.setReason(invitationInfo.getReason());
                builder.setStatus(ContactAddNotice.ContactAddNoticeMessage.InvitationStatus.INVITE_REJECT);
                ContactAddNotice.ContactAddNoticeMessage message = builder.build();

                //异步执行消息发送

                NettyClientConnectUtil.connect(new ContactAddNoticeHandler(message));

                //发送消息通知对方
            }
        });

    }

    private void initData() {

        List<InvitationInfo> invitations = Model.getInstance().dbManager().getInvitationDao().getInvitations();
        invitationAdapter = new InvitationAdapter(invitations);
        //获取布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_invite.setLayoutManager(layoutManager);
        rv_invite.setAdapter(invitationAdapter);
        //调用刷新方法
        invitationAdapter.refresh();
    }

    private void initView() {
        rv_invite = findViewById(R.id.rv_invite);
        //为列表视图绑定弹出菜单
        registerForContextMenu(rv_invite);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //int position=((AdapterView.AdapterContextMenuInfo)menuInfo).position;
        //RecyclerView.ItemDecoration itemDecorationAt = rv_invite.getItemDecorationAt(position);

        getMenuInflater().inflate(R.menu.menu_delete, menu);

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_invite_delete) {
            deleteInvitationInfo();
            Toast.makeText(InvitationActivity.this, item.getItemId(), Toast.LENGTH_LONG).show();

        }

        return super.onContextItemSelected(item);


    }

    /**
     * 删除联系人邀请记录
     */
    private void deleteInvitationInfo() {
        //删除本地数据库
        Model.getInstance().dbManager().getInvitationDao().deleteInvitation(invitationInfo.getUser().getPhone());
        //刷新数据
        invitationAdapter.refresh();
    }
}
