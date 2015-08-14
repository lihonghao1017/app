package com.jin91.preciousmetal.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.api.entity.DirectPlayAsk;
import com.jin91.preciousmetal.util.FaceUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by lijinhua on 2015/5/13.
 * 回复的item
 */
public class ReplyItemAdapter extends BaseAdapter {
    public Context mContext;
    private List<DirectPlayAsk> mList;

    public ReplyItemAdapter(Context mContext, List<DirectPlayAsk> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.play_reply_list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DirectPlayAsk ask = mList.get(position);
        holder.tv_ask_time.setText(ask.CreatedTime);
        holder.tv_ask_nickname.setText("@"+ask.NickName);
        holder.tv_ask_content.setText(FaceUtil.textAddFace(mContext, ask.Question));
        return convertView;
    }


    static class ViewHolder {
        @ViewInject(R.id.tv_ask_time)
        TextView tv_ask_time;
        @ViewInject(R.id.tv_ask_nickname)
        TextView tv_ask_nickname;
        @ViewInject(R.id.tv_ask_content)
        TextView tv_ask_content;

        ViewHolder(View view) {
        	ViewUtils.inject(this, view);
        }
    }
}
