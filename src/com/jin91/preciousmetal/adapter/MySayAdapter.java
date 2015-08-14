package com.jin91.preciousmetal.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.api.entity.MySay;
import com.jin91.preciousmetal.customview.GlideCircleTransform;
import com.jin91.preciousmetal.ui.Constants;
import com.jin91.preciousmetal.util.FaceUtil;
import com.jin91.preciousmetal.util.StringUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by lijinhua on 2015/5/14.
 */
@SuppressLint("NewApi")
public class MySayAdapter extends BaseAdapter {
    private Context mContext;
    private List<MySay> mList;

    public MySayAdapter(Context mContext, List<MySay> mList) {
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
        FullTextOnClickListener listener = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.mysay_list_item, parent, false);
            holder = new ViewHolder(convertView);
            listener = new FullTextOnClickListener(holder.tvMysayContent);
            holder.tvSugtextMore.setOnClickListener(listener);
            convertView.setTag(holder);
            convertView.setTag(holder.tvSugtextMore.getId(), listener);
        } else {
            holder = (ViewHolder) convertView.getTag();
            listener = (FullTextOnClickListener) convertView.getTag(holder.tvSugtextMore.getId());
        }
        listener.setPosition(position);
        MySay mySay = mList.get(position);
        if(!TextUtils.isEmpty(mySay.Type) && "1".equals(mySay.Type))
        {
            // 提问
            holder.llNoPass.setVisibility(View.GONE);
            if(!TextUtils.isEmpty(mySay.AnswerID) && !"0".equals(mySay.AnswerID))
            {
                // 有回答者
                holder.rlReplyContent.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(mySay.AnswerPic).transform(new GlideCircleTransform(mContext)).error(R.mipmap.default_error_avatar).placeholder(R.mipmap.default_normal_avatar).into(holder.ivUserAvtar);
                holder.tvReplyteacherName.setText(mySay.AnswerName);

                setTextCollapsb(holder,mySay,mySay.Answer);
                if(mySay.AuditTime1=="0001-01-01 00:00:00")
                    holder.tvReplyTime.setText(StringUtil.getSectionTime(mySay.AuditTime2));
                else
                    holder.tvReplyTime.setText(StringUtil.getSectionTime(mySay.AuditTime1));
                holder.tvAskNickname.setText("@"+mySay.NickName);
                holder.tvAskTime.setText(StringUtil.getSectionTime(mySay.CreatedTime));
                holder.tvAskContent.setText(FaceUtil.textAddFace(mContext,mySay.Question));
                holder.tvAskContent.setMovementMethod(LinkMovementMethod.getInstance());
            }else
            {
                // 没回答只显示提问
                holder.rlReplyContent.setVisibility(View.GONE);
                Glide.with(mContext).load(mySay.AskerPic).transform(new GlideCircleTransform(mContext)).error(R.mipmap.default_error_avatar).placeholder(R.mipmap.default_normal_avatar).into(holder.ivUserAvtar);
                holder.tvReplyteacherName.setText(mySay.NickName);
                if(mySay.AuditTime1=="0001-01-01 00:00:00")
                    holder.tvReplyTime.setText(StringUtil.getSectionTime(mySay.AuditTime2));
                else
                    holder.tvReplyTime.setText(StringUtil.getSectionTime(mySay.AuditTime1));
                setTextCollapsb(holder,mySay,mySay.Question);
            }

        }else
        {
            // 发言字段
            if("2".equals(mySay.AuditStatus) && !TextUtils.isEmpty(mySay.CloseReason))
            {
                   // 2代表未通过审核
                holder.llNoPass.setVisibility(View.VISIBLE);
                holder.tvCloseReason.setText(FaceUtil.textAddFace(mContext,mySay.CloseReason));
                holder.tvCloseReason.setMovementMethod(LinkMovementMethod.getInstance());
            }else
            {
                holder.llNoPass.setVisibility(View.GONE);
                holder.tvCloseReason.setText("");
                holder.tvCloseReason.setMovementMethod(LinkMovementMethod.getInstance());
            }
            Glide.with(mContext).load(mySay.UserPic).transform(new GlideCircleTransform(mContext)).error(R.mipmap.default_error_avatar).placeholder(R.mipmap.default_normal_avatar).into(holder.ivUserAvtar);
            holder.tvReplyteacherName.setText(mySay.UserName);

            holder.tvReplyTime.setText(StringUtil.getSectionTime(mySay.CreatedTime));
            setTextCollapsb(holder,mySay,mySay.Content);
            if("0".equals(mySay.ReplyTo))
            {
                // 没回复
                holder.rlReplyContent.setVisibility(View.GONE);
            }else
            {
                // 有回复
                holder.rlReplyContent.setVisibility(View.VISIBLE);

                holder.tvAskNickname.setText("@"+mySay.ReplyUserName);
                holder.tvAskTime.setText(StringUtil.getSectionTime(mySay.ReplyAuditTime));
                holder.tvAskContent.setText(FaceUtil.textAddFace(mContext,mySay.ReplyContent));
                holder.tvAskContent.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
        if (mySay.Imgs != null && mySay.Imgs.size() > 0) {
            holder.gvReplyImgs.setVisibility(View.VISIBLE);
            GrideViewImgAdapter adapter = new GrideViewImgAdapter(mContext, mySay.Imgs);
            holder.gvReplyImgs.setOnItemClickListener(adapter);
            holder.gvReplyImgs.setAdapter(adapter);
        } else {
            holder.gvReplyImgs.setVisibility(View.GONE);
        }


        return convertView;
    }
    public void setTextCollapsb(final ViewHolder holder, MySay message,String newMessage) {
        holder.tvMysayContent.setText(FaceUtil.textAddFace(mContext, TextUtils.isEmpty(newMessage) ? newMessage : newMessage.trim()));
        holder.tvMysayContent.setMovementMethod(LinkMovementMethod.getInstance());
        holder.tvMysayContent.post(new Runnable() {
            @Override
            public void run() {
                if (holder.tvMysayContent.getLineCount() > Constants.MAX_LINE) {
                    holder.tvSugtextMore.setVisibility(View.VISIBLE);
                } else {
                    holder.tvSugtextMore.setVisibility(View.GONE);
                }
            }
        });
        if (message.isFullText) {
            holder.tvMysayContent.setMaxLines(Integer.MAX_VALUE);
            holder.tvSugtextMore.setText(mContext.getString(R.string.desc_shrinkup));
        } else {
            holder.tvMysayContent.setMaxLines(Constants.MAX_LINE);
            holder.tvSugtextMore.setText(mContext.getString(R.string.desc_spread));
        }
    }

    class FullTextOnClickListener implements View.OnClickListener {
        int position;
        TextView tv_jinmessage_content;

        public FullTextOnClickListener(TextView tv_jinmessage_content) {
            this.tv_jinmessage_content = tv_jinmessage_content;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_sugtext_more:
                    MySay message = mList.get(position);
                    TextView textView = (TextView) v;
                    if (message.isFullText) {
                        // 显示的全文.应该收取
                        textView.setText(mContext.getString(R.string.desc_spread));
                        tv_jinmessage_content.setMaxLines(Constants.MAX_LINE);
                    } else {
                        // 没有显示全文
                        textView.setText(mContext.getString(R.string.desc_shrinkup));
                        tv_jinmessage_content.setMaxLines(Integer.MAX_VALUE);
                    }
                    message.isFullText = !message.isFullText;
                    break;
            }
        }
    }

    static class ViewHolder {
        @ViewInject(R.id.in_img_avatar)
        ImageView ivUserAvtar;
        @ViewInject(R.id.tv_replyteacher_name)
        TextView tvReplyteacherName;
        @ViewInject(R.id.tv_reply_time)
        TextView tvReplyTime;
        @ViewInject(R.id.tv_mysay_content)
        TextView tvMysayContent;
        @ViewInject(R.id.tv_sugtext_more)
        TextView tvSugtextMore;
        @ViewInject(R.id.tv_close_reason)
        TextView tvCloseReason;
        @ViewInject(R.id.ll_no_pass)
        LinearLayout llNoPass;
        @ViewInject(R.id.tv_ask_time)
        TextView tvAskTime;
        @ViewInject(R.id.tv_ask_nickname)
        TextView tvAskNickname;
        @ViewInject(R.id.tv_ask_content)
        TextView tvAskContent;
        @ViewInject(R.id.rl_reply_content)
        RelativeLayout rlReplyContent;
        @ViewInject(R.id.gv_reply_imgs)
        GridView gvReplyImgs;

        ViewHolder(View view) {
        	ViewUtils.inject(this, view);
        }
    }
}
