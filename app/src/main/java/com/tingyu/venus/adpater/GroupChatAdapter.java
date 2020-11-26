package com.tingyu.venus.adpater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tingyu.venus.R;
import com.tingyu.venus.model.entity.GroupInfo;

import java.util.List;

public class GroupChatAdapter extends RecyclerView.Adapter<GroupChatAdapter.ViewHolder> {


    private List<GroupInfo> groupInfoList;
    private OnItemClickListener onItemClickListener;

    public GroupChatAdapter(List<GroupInfo> groupInfoList) {
        this.groupInfoList = groupInfoList;
    }

    /**
     * 刷新页面
     */
    public void refresh(List<GroupInfo> groupInfoList) {
        groupInfoList = groupInfoList;
        if (groupInfoList != null && groupInfoList.size() >= 0) {
            //刷新列表
            notifyDataSetChanged();
        }


    }

    /**
     * 设置列表项单击事件
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_chat, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (groupInfoList != null && groupInfoList.size() >= 0) {
            GroupInfo groupInfo = groupInfoList.get(position);
            if (groupInfo != null) {
                holder.iv_group_chat_avatar.setImageResource(R.drawable.group_default_avatar);
                holder.tv_group_chat_name.setText(groupInfo.getGroupName());


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //列表项单击事件
                        onItemClickListener.onClick(groupInfo, position);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.groupInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_group_chat_name;
        private final ImageView iv_group_chat_avatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_group_chat_avatar = itemView.findViewById(R.id.iv_group_chat_avatar);
            tv_group_chat_name = itemView.findViewById(R.id.tv_group_chat_name);
        }
    }

    /**
     * 列表项单击事件
     */
    public interface OnItemClickListener {
        void onClick(GroupInfo groupInfo, int position);
    }
}
