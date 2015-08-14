package com.jin91.preciousmetal.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.api.entity.DirectPlay;
import com.jin91.preciousmetal.common.api.entity.DirectPlayAsk;
import com.jin91.preciousmetal.customview.GlideCircleTransform;
import com.jin91.preciousmetal.util.FaceUtil;
import com.jin91.preciousmetal.util.StringUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by lijinhua on 2015/5/13.
 * 直播
 */
public class DirectPlayAdapter extends BaseAdapter {

    private Context mContext;
    private List<DirectPlay> mList;

    public DirectPlayAdapter(Context mContext, List<DirectPlay> mList) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.direct_play_list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DirectPlay directPlay = mList.get(position);
        if ("1".equals(directPlay.Type)) {
            holder.ll_reply_content.setVisibility(View.VISIBLE);
            holder.ll_reply_content.removeAllViews();
            // 提问
            Glide.with(mContext).load(directPlay.AnswerPic).transform(new GlideCircleTransform(mContext)).error(R.mipmap.default_error_avatar).placeholder(R.mipmap.default_normal_avatar).into(holder.ivUserAvtar);
            holder.tv_directrepte_name.setText(directPlay.AnswerName);
            holder.tv_directrep_time.setText(StringUtil.getSectionTime(directPlay.AnswerTime));
            holder.tv_directrep_content.setText(FaceUtil.textAddFace(mContext, directPlay.Answer));
            holder.tv_directrep_content.setTextColor(mContext.getResources().getColor(R.color.gray));
            List<DirectPlayAsk> mAskList = new ArrayList<DirectPlayAsk>();
            if (directPlay.AskerList != null && directPlay.AskerList.size() > 0) {
                mAskList = directPlay.AskerList;
            } else {
                mAskList.add(new DirectPlayAsk(directPlay.NickName, directPlay.Question, directPlay.CreatedTime));
            }

            for (DirectPlayAsk ask : mAskList) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.play_reply_list_item, parent, false);
                TextView tv_ask_nickname = (TextView) view.findViewById(R.id.tv_ask_nickname);
                TextView tv_ask_time = (TextView) view.findViewById(R.id.tv_ask_time);
                TextView tv_ask_content = (TextView) view.findViewById(R.id.tv_ask_content);

                tv_ask_time.setText(StringUtil.getSectionTime(ask.CreatedTime));
                tv_ask_nickname.setText("@" + ask.NickName);
                tv_ask_content.setText(FaceUtil.textAddFace(mContext, ask.Question));
                tv_ask_content.setMovementMethod(LinkMovementMethod.getInstance());
                holder.ll_reply_content.addView(view);
            }

        } else if ("2".equals(directPlay.Type)) {
            // 直播
            holder.ll_reply_content.setVisibility(View.GONE);
            Glide.with(mContext).load(directPlay.StaffPic).transform(new GlideCircleTransform(mContext)).error(R.mipmap.default_error_avatar).placeholder(R.mipmap.default_normal_avatar).into(holder.ivUserAvtar);
            holder.tv_directrepte_name.setText(directPlay.Teacher);
            holder.tv_directrep_time.setText(StringUtil.getSectionTime(directPlay.CreatedTime));
            holder.tv_directrep_content.setText(FaceUtil.textAddFace(mContext, directPlay.Content));
            holder.tv_directrep_content.setTextColor(mContext.getResources().getColor(R.color.red));
        }
        if (directPlay.Imgs == null) {
            holder.gv_directrep_imgs.setVisibility(View.GONE);
        } else {
            holder.gv_directrep_imgs.setVisibility(View.VISIBLE);
            GrideViewImgAdapter adapter = new GrideViewImgAdapter(mContext, directPlay.Imgs);
            holder.gv_directrep_imgs.setOnItemClickListener(adapter);
            holder.gv_directrep_imgs.setAdapter(adapter);
        }

        return convertView;
    }


    static class ViewHolder {
        @ViewInject(R.id.in_direct_avatar)
        ImageView ivUserAvtar;
        @ViewInject(R.id.tv_directrepte_name)
        TextView tv_directrepte_name;
        @ViewInject(R.id.tv_directrep_time)
        TextView tv_directrep_time;
        @ViewInject(R.id.tv_directrep_content)
        TextView tv_directrep_content;
        @ViewInject(R.id.gv_directrep_imgs)
        GridView gv_directrep_imgs;
        @ViewInject(R.id.ll_reply_content)
        LinearLayout ll_reply_content;

        ViewHolder(View view) {
        	ViewUtils.inject(this, view);
        }
    }
}
