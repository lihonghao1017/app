package com.jin91.preciousmetal.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.api.entity.CounselorReply;
import com.jin91.preciousmetal.customview.GlideCircleTransform;
import com.jin91.preciousmetal.util.FaceUtil;
import com.jin91.preciousmetal.util.StringUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by lijinhua on 2015/5/12.
 * 顾问回复
 */
public class CounSelorReplyAdapter extends BaseAdapter {

    private Context mContext;
    private List<CounselorReply> mList;

    public CounSelorReplyAdapter(Context mContext, List<CounselorReply> mList) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.mine_counselor_reply_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CounselorReply reply = mList.get(position);
        Glide.with(mContext).load(reply.AskerPic).transform(new GlideCircleTransform(mContext)).error(R.mipmap.default_error_avatar).placeholder(R.mipmap.default_normal_avatar).into(holder.iv_user_avtar);
        holder.tv_replyteacher_name.setText(reply.AnswerName);
        holder.tv_reply_time.setText(StringUtil.getSectionTime(reply.AnswerTime));
        holder.tv_reply_content.setText(FaceUtil.textAddFace(mContext, reply.Answer));
        holder.tv_ask_nickname.setText("@" + reply.NickName);
        holder.tv_ask_time.setText(StringUtil.getSectionTime(reply.CreatedTime));
        holder.tv_ask_content.setText(FaceUtil.textAddFace(mContext, reply.Question));
        if (reply.Imgs != null && reply.Imgs.size() > 0) {
            holder.gv_reply_imgs.setVisibility(View.VISIBLE);
            GrideViewImgAdapter adapter = new GrideViewImgAdapter(mContext, reply.Imgs);
            holder.gv_reply_imgs.setOnItemClickListener(adapter);
            holder.gv_reply_imgs.setAdapter(adapter);
        } else {
            holder.gv_reply_imgs.setVisibility(View.GONE);
        }
        return convertView;
    }


    static class ViewHolder {
        @ViewInject(R.id.in_img_avatar)
        ImageView iv_user_avtar;
        @ViewInject(R.id.tv_replyteacher_name)
        TextView tv_replyteacher_name;
        @ViewInject(R.id.tv_reply_time)
        TextView tv_reply_time;
        @ViewInject(R.id.tv_reply_content)
        TextView tv_reply_content;
        @ViewInject(R.id.tv_ask_time)
        TextView tv_ask_time;
        @ViewInject(R.id.tv_ask_nickname)
        TextView tv_ask_nickname;
        @ViewInject(R.id.tv_ask_content)
        TextView tv_ask_content;
        @ViewInject(R.id.gv_reply_imgs)
        GridView gv_reply_imgs;

        ViewHolder(View view) {
        	ViewUtils.inject(this, view);
        }
    }
}
