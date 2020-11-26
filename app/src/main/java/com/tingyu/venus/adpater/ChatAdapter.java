package com.tingyu.venus.adpater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tingyu.venus.R;
import com.tingyu.venus.model.entity.ChatRecord;

import java.util.List;

/**
 * 聊天视图适配器
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<ChatRecord> recordList;

    public ChatAdapter(List<ChatRecord> records) {
        this.recordList = records;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message, parent, false);
        return new ViewHolder(view);
    }

    /**
     * 绑定视图
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //校验
        if (recordList != null && recordList.size() >= 0) {


            ChatRecord record = this.recordList.get(position);
            if (record.getMessageType() == ChatRecord.MessageType.SEND) {
                //如果是发送消息，则显示右侧
                holder.rightLinearLayout.setVisibility(View.VISIBLE);//显示
                holder.leftLinearLayout.setVisibility(View.GONE);//隐藏
                holder.rightAvatar.setImageResource(R.drawable.user_default_avatar);
                holder.rightMessage.setText(record.getContent());
            } else if (record.getMessageType() == ChatRecord.MessageType.RECEIVE) {
                //接收消息，左侧显示
                holder.leftLinearLayout.setVisibility(View.VISIBLE);//显示
                holder.rightLinearLayout.setVisibility(View.GONE);//隐藏
                holder.leftAvatar.setImageResource(R.drawable.user_default_avatar);
                holder.leftMessage.setText(record.getContent());
                //判断如果是群聊，则显示用户名

                if (record.getGroupId() != null && !record.getGroupId().equals("0")) {
                    holder.tv_chat_username.setVisibility(View.VISIBLE);
                    holder.tv_chat_username.setText(record.getContactId());
                } else {
                    holder.tv_chat_username.setVisibility(View.GONE);
                }

            }
        }
    }

    @Override
    public int getItemCount() {
        //校验
        if (this.recordList == null) {
            return 0;
        }
        return this.recordList.size();
    }


    /**
     * 刷新页面
     */
    public void refresh(List<ChatRecord> records) {
        this.recordList = records;
        if (recordList != null && recordList.size() >= 0) {

            if (recordList.size() == 0) {
                return;
            }
            //notifyItemInserted(recordList.size()-1);
            //notifyItemChanged(recordList.size()-1);
            notifyDataSetChanged();

        }

    }

    /**
     * ViewHolder
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout leftLinearLayout;
        LinearLayout rightLinearLayout;
        TextView leftMessage;
        TextView rightMessage;
        ImageView leftAvatar;
        ImageView rightAvatar;
        private final TextView tv_chat_username;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            leftLinearLayout = itemView.findViewById(R.id.left_message);
            rightLinearLayout = itemView.findViewById(R.id.right_message);
            tv_chat_username = itemView.findViewById(R.id.tv_chat_username);

            leftMessage = itemView.findViewById(R.id.tv_contact_msg_left);
            rightMessage = itemView.findViewById(R.id.tv_contact_msg_right);

            leftAvatar = itemView.findViewById(R.id.iv_contact_avatar_left);
            rightAvatar = itemView.findViewById(R.id.iv_contact_avatar_right);
        }
    }
}
