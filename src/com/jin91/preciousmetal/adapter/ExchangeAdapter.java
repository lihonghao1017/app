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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.api.entity.Exchange;
import com.jin91.preciousmetal.common.api.entity.User;
import com.jin91.preciousmetal.customview.GlideCircleTransform;
import com.jin91.preciousmetal.ui.Constants;
import com.jin91.preciousmetal.ui.PreciousMetalAplication;
import com.jin91.preciousmetal.util.FaceUtil;
import com.jin91.preciousmetal.util.StringUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by lijinhua on 2015/5/14.
 */
@SuppressLint("NewApi")
public class ExchangeAdapter extends BaseAdapter {
    private Context mContext;
    private List<Exchange> mList;
    private String userId = "";
    private commReplyListener commReplyListener;

    public ExchangeAdapter(Context mContext, List<Exchange> mList) {
        this.mContext = mContext;
        this.mList = mList;
        User user = PreciousMetalAplication.getINSTANCE().user;
        if (user != null) {
            userId = user.ID;
        }

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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.exchange_list_item, parent, false);
            holder = new ViewHolder(convertView);
            listener = new FullTextOnClickListener(holder.tvReplyContent);
            holder.tvSugtextMore.setOnClickListener(listener);
            holder.tvReplyteacherName.setOnClickListener(listener);
            holder.ivExchangeComment.setOnClickListener(listener);
            convertView.setTag(holder);
            convertView.setTag(holder.tvSugtextMore.getId(), listener);
        } else {
            holder = (ViewHolder) convertView.getTag();
            listener = (FullTextOnClickListener) convertView.getTag(holder.tvSugtextMore.getId());
        }
        listener.setPosition(position);
        Exchange exchange = mList.get(position);
        Glide.with(mContext).load(exchange.UserPic).transform(new GlideCircleTransform(mContext)).error(R.mipmap.default_error_avatar).placeholder(R.mipmap.default_normal_avatar).into(holder.ivUserAvtar);
        if (userId.equals(exchange.UserID)) {
            holder.ivExchangeComment.setVisibility(View.GONE);
        } else {
            holder.ivExchangeComment.setVisibility(View.VISIBLE);
        }


        if (!"0".equals(exchange.ReplyTo)) {
            // 有回复
            holder.rlReplyContent.setVisibility(View.VISIBLE);

            holder.tvReplyteacherName.setText(exchange.UserName);
            holder.tvReplyTime.setText(StringUtil.getSectionTime(exchange.ReplyCreatedTime));
            setTextCollapsb(holder, exchange, exchange.Content);


            holder.tvAskNickname.setText("@" + exchange.ReplyUserName);
            holder.tvAskTime.setText(StringUtil.getSectionTime(exchange.ReplyAuditTime));
            holder.tvAskContent.setText(FaceUtil.textAddFace(mContext, exchange.ReplyContent));
            holder.tvAskContent.setMovementMethod(LinkMovementMethod.getInstance());

        } else {
            holder.rlReplyContent.setVisibility(View.GONE);
            // 没有回复
            holder.tvReplyteacherName.setText(exchange.UserName);
            holder.tvReplyTime.setText(StringUtil.getSectionTime(exchange.CreatedTime));
            setTextCollapsb(holder, exchange, exchange.Content);

        }

        if (exchange.Imgs != null && exchange.Imgs.size() > 0) {
            holder.gvReplyImgs.setVisibility(View.VISIBLE);
            GrideViewImgAdapter adapter = new GrideViewImgAdapter(mContext, exchange.Imgs);
            holder.gvReplyImgs.setOnItemClickListener(adapter);
            holder.gvReplyImgs.setAdapter(adapter);
        } else {
            holder.gvReplyImgs.setVisibility(View.GONE);
        }
        return convertView;
    }

    public void setTextCollapsb(final ViewHolder holder, Exchange message, String newMessage) {
        holder.tvReplyContent.setText(FaceUtil.textAddFace(mContext, TextUtils.isEmpty(newMessage) ? newMessage : newMessage.trim()));
        holder.tvReplyContent.setMovementMethod(LinkMovementMethod.getInstance());
        holder.tvReplyContent.post(new Runnable() {
            @Override
            public void run() {
                if (holder.tvReplyContent.getLineCount() > Constants.MAX_LINE) {
                    holder.tvSugtextMore.setVisibility(View.VISIBLE);
                } else {
                    holder.tvSugtextMore.setVisibility(View.GONE);
                }
            }
        });
        if (message.isFullText) {
            holder.tvReplyContent.setMaxLines(Integer.MAX_VALUE);
            holder.tvSugtextMore.setText(mContext.getString(R.string.desc_shrinkup));
        } else {
            holder.tvReplyContent.setMaxLines(Constants.MAX_LINE);
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
                    Exchange message = mList.get(position);
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
                case R.id.iv_exchange_comment:
                case R.id.tv_replyteacher_name:
                    if (commReplyListener != null && !userId.equals(mList.get(position).UserID)) {
                        commReplyListener.comClick(position);
                    }
                    break;
            }
        }
    }


    static class ViewHolder {
        @ViewInject(R.id.in_img_avatar)
        ImageView ivUserAvtar;
        @ViewInject(R.id.tv_replyteacher_name)
        TextView tvReplyteacherName;
        @ViewInject(R.id.iv_exchange_comment)
        ImageView ivExchangeComment;
        @ViewInject(R.id.tv_reply_time)
        TextView tvReplyTime;
        @ViewInject(R.id.tv_reply_content)
        TextView tvReplyContent;
        @ViewInject(R.id.tv_sugtext_more)
        TextView tvSugtextMore;
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

    public void setCommReplyListener(ExchangeAdapter.commReplyListener commReplyListener) {
        this.commReplyListener = commReplyListener;
    }

    public interface commReplyListener {
        public void comClick(int position);
    }
}
