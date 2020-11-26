package com.tingyu.venus.adpater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tingyu.venus.R;
import com.tingyu.venus.model.Model;
import com.tingyu.venus.model.entity.UserInfo;

import java.util.List;

/**
 * 创建群聊
 */
public class CreateGroupChatAdapter extends RecyclerView.Adapter<CreateGroupChatAdapter.ViewHolder> {

    private List<UserInfo> userInfoList;
    private OnItemCheckedChangeListener onItemCheckedChangeListener;

    public CreateGroupChatAdapter(List<UserInfo> userInfoList) {
        this.userInfoList = userInfoList;
    }


    /**
     * 刷新列表方法
     */
    public void refresh() {
        this.userInfoList = Model.getInstance().dbManager().getContactDao().getContactList();

        if (userInfoList != null && userInfoList.size() >= 0) {
            notifyDataSetChanged();
        }


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_create, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (this.userInfoList != null && this.userInfoList.size() >= 0) {
            UserInfo userInfo = this.userInfoList.get(position);

            if (userInfo != null) {
                holder.iv_group_avatar.setImageResource(R.drawable.user_default_avatar);
                holder.tv_group_username.setText(userInfo.getUsername());

                //为选择框添加事件监听
                holder.cb_group_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        onItemCheckedChangeListener.onChecked(userInfo, isChecked);

                    }
                });
            }
        }


    }

    @Override
    public int getItemCount() {
        return userInfoList.size();
    }

    public OnItemCheckedChangeListener getOnItemCheckedChangeListener() {
        return onItemCheckedChangeListener;
    }

    public void setOnItemCheckedChangeListener(OnItemCheckedChangeListener onItemCheckedChangeListener) {
        this.onItemCheckedChangeListener = onItemCheckedChangeListener;
    }

    /**
     *
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        private final CheckBox cb_group_check;
        private final ImageView iv_group_avatar;
        private final TextView tv_group_username;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cb_group_check = itemView.findViewById(R.id.cb_group_check);
            iv_group_avatar = itemView.findViewById(R.id.iv_group_avatar);
            tv_group_username = itemView.findViewById(R.id.tv_group_username);

        }
    }

    public interface OnItemCheckedChangeListener {

        void onChecked(UserInfo userInfo, boolean isChecked);
    }
}
