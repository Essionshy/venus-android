package com.tingyu.venus.adpater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tingyu.venus.R;
import com.tingyu.venus.model.Model;
import com.tingyu.venus.model.entity.InvitationInfo;
import com.tingyu.venus.model.entity.UserInfo;

import java.util.List;

public class InvitationAdapter extends RecyclerView.Adapter<InvitationAdapter.InvitationViewHolder> {

    private List<InvitationInfo> invitationInfos; //接收邀请信息列表
    private OnInvitationListener invitationListener; //邀请监听
    private OnItemClickListener onItemClickListener; //列表视图项长按事件监听

    public InvitationAdapter(List<InvitationInfo> invitationInfos) {
        this.invitationInfos = invitationInfos;
    }

    public void setInvitationListener(OnInvitationListener invitationListener) {
        this.invitationListener = invitationListener;
    }

    public List<InvitationInfo> getInvitationInfos() {
        return invitationInfos;
    }

    public void refresh() {

        invitationInfos = Model.getInstance().dbManager().getInvitationDao().getInvitations();
        if (invitationInfos != null && invitationInfos.size() >= 0) {
            if(invitationInfos.size()==0){
                return;
            }
            notifyDataSetChanged();
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public InvitationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invite, parent, false);

        return new InvitationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InvitationViewHolder holder, int position) {
        InvitationInfo invitationInfo = this.invitationInfos.get(position);
        holder.tv_invite_reason.setText(invitationInfo.getReason());

        holder.btn_invite_receive.setVisibility(View.GONE);
        holder.btn_invite_reject.setVisibility(View.GONE);
        //展示邀请信息
        UserInfo user = invitationInfo.getUser();
        if (user != null) {
            //名称的展示
            holder.tv_invite_name.setText(user.getUsername());
            //原因
            //根据邀请信息的状态显示 按钮
            InvitationInfo.InvitationStatus status = invitationInfo.getStatus();

            if (InvitationInfo.InvitationStatus.NEW_INVITE.equals(status)) {
                if (invitationInfo.getReason() == null) {
                    holder.tv_invite_reason.setText("添加好友");
                } else {
                    holder.tv_invite_reason.setText(invitationInfo.getReason());
                }

                holder.btn_invite_receive.setVisibility(View.VISIBLE);
                holder.btn_invite_reject.setVisibility(View.VISIBLE);
            } else if (InvitationInfo.InvitationStatus.INVITE_ACCEPT.equals(status)) {
                if (invitationInfo.getReason() == null) {
                    holder.tv_invite_reason.setText("接受邀请");
                } else {
                    holder.tv_invite_reason.setText(invitationInfo.getReason());
                }
            } else if (InvitationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER.equals(status)) {
                if (invitationInfo.getReason() == null) {
                    holder.tv_invite_reason.setText("邀请被接受");
                } else {
                    holder.tv_invite_reason.setText(invitationInfo.getReason());
                }
            }
        } else {
            //群组 TODO 显示群组的时候再完善


        }

        //接受按钮的单击事件
        holder.btn_invite_receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invitationListener.onReceived(invitationInfo);
                holder.btn_invite_receive.setVisibility(View.GONE);
                holder.btn_invite_reject.setVisibility(View.GONE);
            }
        });
        //拒绝按键的单击事件
        holder.btn_invite_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invitationListener.onRejected(invitationInfo);
                holder.btn_invite_receive.setVisibility(View.GONE);
                holder.btn_invite_reject.setVisibility(View.GONE);
            }
        });
        //列表项被选中的长按事件
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemClickListener.onItemLongClick(holder.itemView, position);
                return false;
            }
        });

    }


    @Override
    public int getItemCount() {
        return this.invitationInfos.size();
    }


    static class InvitationViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_invite_name;
        private final TextView tv_invite_reason;
        private final Button btn_invite_receive;
        private final Button btn_invite_reject;

        public InvitationViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_invite_name = itemView.findViewById(R.id.tv_invite_name);
            tv_invite_reason = itemView.findViewById(R.id.tv_invite_reason);
            btn_invite_receive = itemView.findViewById(R.id.btn_invite_receive);
            btn_invite_reject = itemView.findViewById(R.id.btn_invite_reject);

        }
    }

    public interface OnInvitationListener {
        void onReceived(InvitationInfo invitationInfo);

        void onRejected(InvitationInfo invitationInfo);
    }

    public interface OnItemClickListener {
        void onItemLongClick(View itemView, int position);
    }
}
