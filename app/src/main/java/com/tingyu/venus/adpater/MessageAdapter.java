package com.tingyu.venus.adpater;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.tingyu.venus.R;
import com.tingyu.venus.model.Model;
import com.tingyu.venus.model.entity.GroupInfo;
import com.tingyu.venus.model.entity.MessageInfo;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<MessageInfo> messageInfoList;
    private OnItemClickListener onItemClickListener;
    private OnScrollChangeListener onScrollChangeListener;

    public MessageAdapter(List<MessageInfo> messageInfos) {
        this.messageInfoList = messageInfos;
    }

    public List<MessageInfo> getMessageInfoList() {
        return messageInfoList;
    }

    public void setMessageInfoList(List<MessageInfo> messageInfoList) {
        this.messageInfoList = messageInfoList;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OnScrollChangeListener getOnScrollChangeListener() {
        return onScrollChangeListener;
    }

    public void setOnScrollChangeListener(OnScrollChangeListener onScrollChangeListener) {
        this.onScrollChangeListener = onScrollChangeListener;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);

        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {


        if (this.messageInfoList != null && this.messageInfoList.size() >= 0) {
            MessageInfo messageInfo = this.messageInfoList.get(position);
            //判断是联系消息还是群消息

            String groupId = messageInfo.getGroupId();
            if (groupId == null || TextUtils.isEmpty(groupId)) {
                //联系人新消息
                holder.iv_message_avatar.setImageResource(R.drawable.user_default_avatar);//显示联系人头像 TODO
                holder.tv_message_contact.setText(messageInfo.getNickname()); //显示联系人昵称
                holder.tv_message_new.setText(messageInfo.getNewMessage()); //显示最新的一条消息
            } else {
                //群新消息
                GroupInfo groupInfo = Model.getInstance().dbManager().getGroupChatDao().getGroupInfo(groupId);
                if (groupInfo != null) {
                    holder.iv_message_avatar.setImageResource(R.drawable.group_default_avatar); //显示群头像 TODO
                    holder.tv_message_contact.setText(groupInfo.getGroupName()); //显示联系人昵称
                    holder.tv_message_new.setText(messageInfo.getNickname() + ":" + messageInfo.getNewMessage()); //显示最新的一条消息
                }
            }


            //TODO 对时间显示处理
            String time = messageInfo.getTime();
            if (time != null) {


            }
            //未读消息记录数显示
            MessageInfo message = Model.getInstance().dbManager().getMessageDao().getMessageInfoByPhone(messageInfo.getId());
            Log.d("mes ada", message.toString());
            int count = message.getUnReadCount();
            if (count > 0) {
                holder.tv_message_point.setText(String.valueOf(count));
                holder.tv_message_point.setVisibility(View.VISIBLE); //

            }


            //设置消息单击事件
            holder.ll_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //调用单击事件，由外部调用处传
                    if (onItemClickListener != null) {
                        onItemClickListener.onClick(messageInfo, position);
                    }
                }
            });
            //设置滚动变化事件
            holder.ll_message.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    onScrollChangeListener.onScrollChange(messageInfo);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return this.messageInfoList.size();
    }

    /**
     * 刷新页面方法
     */
    public void refresh() {
        //再次调用获取最新消息数据
        messageInfoList = Model.getInstance().dbManager().getMessageDao().getMessageInfoList();
        Log.d("refresh msg", String.valueOf(messageInfoList.size()));
        Log.d("refresh msg", messageInfoList.toString());
        if (messageInfoList != null && messageInfoList.size() >= 0) {
            if (messageInfoList.size() == 0) {
                return;
            }
            notifyDataSetChanged();

        }
    }

    /**
     * 刷新特定列的值
     *
     * @param position
     */
    public void refresh(int position) {
        notifyItemChanged(position);
    }

    /**
     * 消息视图
     */
    class ViewHolder extends RecyclerView.ViewHolder {


        private final ImageView iv_message_avatar;
        private final LinearLayout ll_message;
        private final TextView tv_message_new;
        private final TextView tv_message_contact;
        private final TextView tv_message_time;
        private final TextView tv_message_point;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ll_message = itemView.findViewById(R.id.ll_message);
            iv_message_avatar = itemView.findViewById(R.id.iv_message_avatar);
            tv_message_contact = itemView.findViewById(R.id.tv_message_contact);
            tv_message_new = itemView.findViewById(R.id.tv_message_new);
            tv_message_time = itemView.findViewById(R.id.tv_message_time);
            tv_message_point = itemView.findViewById(R.id.tv_message_point);
            tv_message_point.setVisibility(View.GONE); //默认不显示

        }
    }

    /**
     * 单击事件监听
     */
    public interface OnItemClickListener {
        void onClick(MessageInfo messageInfo, int position);
    }

    /**
     * 滚动事件监听
     */
    public interface OnScrollChangeListener {

        void onScrollChange(MessageInfo messageInfo);
    }
}
