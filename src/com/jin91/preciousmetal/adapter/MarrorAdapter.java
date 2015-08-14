package com.jin91.preciousmetal.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.jin91.preciousmetal.common.api.entity.Marrow;
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
public class MarrorAdapter extends BaseAdapter {
    private Context mContext;
    private List<Marrow> mList;

    public MarrorAdapter(Context mContext, List<Marrow> mList) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.marrow_list_item, parent, false);
            holder = new ViewHolder(convertView);
            listener = new FullTextOnClickListener(holder.tvMarrorContent);
            holder.tvSugtextMore.setOnClickListener(listener);
            convertView.setTag(holder.tvMarrorTittle.getId(),listener);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            listener = (FullTextOnClickListener) convertView.getTag(holder.tvMarrorTittle.getId());
        }
        listener.setPosition(position);
        Marrow marrow = mList.get(position);
        holder.tvMarrorTittle.setText("【"+marrow.MarrowTitle+"】");
        if("1".equals(marrow.Type))
        {
            // 提问
            holder.rlReplyContent.setVisibility(View.VISIBLE);
            holder.tvMarrorContent.setTextColor(mContext.getResources().getColor(R.color.gray));
            holder.tvReplyteacherName.setText(marrow.AnswerName);
            holder.tvReplyTime.setText(StringUtil.getSectionTime(marrow.AnswerTime));
            setTextCollapsb(holder,marrow,marrow.Answer);
            Glide.with(mContext).load(marrow.AnswerPic).transform(new GlideCircleTransform(mContext)).error(R.mipmap.default_error_avatar).placeholder(R.mipmap.default_normal_avatar).into(holder.ivUserAvtar);
            holder.tvAskNickname.setText("@"+marrow.NickName);
            holder.tvAskTime.setText(StringUtil.getSectionTime(marrow.CreatedTime));
            holder.tvAskContent.setText(FaceUtil.textAddFace(mContext,marrow.Question));
            holder.tvAskContent.setMovementMethod(LinkMovementMethod.getInstance());
        }else if("2".equals(marrow.Type))
        {
            // 直播
            holder.rlReplyContent.setVisibility(View.GONE);
            holder.tvMarrorContent.setTextColor(mContext.getResources().getColor(R.color.red));
            holder.tvReplyteacherName.setText(marrow.Teacher);
            Glide.with(mContext).load(marrow.StaffPic).transform(new GlideCircleTransform(mContext)).error(R.mipmap.default_error_avatar).placeholder(R.mipmap.default_normal_avatar).into(holder.ivUserAvtar);
            holder.tvReplyTime.setText(StringUtil.getSectionTime(marrow.CreatedTime));
            setTextCollapsb(holder,marrow,marrow.Content);
        }
        if(marrow.Imgs!=null && marrow.Imgs.size()>0)
        {
            holder.gvReplyImgs.setVisibility(View.VISIBLE);
            GrideViewImgAdapter adapter = new GrideViewImgAdapter(mContext, marrow.Imgs);
            holder.gvReplyImgs.setOnItemClickListener(adapter);
            holder.gvReplyImgs.setAdapter(adapter);
        }else
        {
            holder.gvReplyImgs.setVisibility(View.GONE);
        }

        return convertView;
    }
    public void setTextCollapsb(final ViewHolder holder, Marrow message,String newMessage) {

        holder.tvMarrorContent.setText(FaceUtil.textAddFace(mContext,newMessage));
        holder.tvMarrorContent.setMovementMethod(LinkMovementMethod.getInstance());
        holder.tvMarrorContent.post(new Runnable() {
            @Override
            public void run() {
                if (holder.tvMarrorContent.getLineCount() > Constants.MAX_LINE) {
                    holder.tvSugtextMore.setVisibility(View.VISIBLE);
                } else {
                    holder.tvSugtextMore.setVisibility(View.GONE);
                }
            }
        });
        if (message.isFullText) {
            holder.tvMarrorContent.setMaxLines(Integer.MAX_VALUE);
            holder.tvSugtextMore.setText(mContext.getString(R.string.desc_shrinkup));
        } else {
            holder.tvMarrorContent.setMaxLines(Constants.MAX_LINE);
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
                    Marrow message = mList.get(position);
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
        @ViewInject(R.id.tv_marror_tittle)
        TextView tvMarrorTittle;
        @ViewInject(R.id.tv_marror_content)
        TextView tvMarrorContent;
        @ViewInject(R.id.tv_sugtext_more)
        TextView tvSugtextMore;
        @ViewInject(R.id.tv_ask_time)
        TextView tvAskTime;
        @ViewInject(R.id.tv_ask_nickname)
        TextView tvAskNickname;
        @ViewInject(R.id.tv_ask_content)
        TextView tvAskContent;
        @ViewInject(R.id.gv_reply_imgs)
        GridView gvReplyImgs;
        @ViewInject(R.id.rl_reply_content)
        RelativeLayout rlReplyContent;
        ViewHolder(View view) {
        	ViewUtils.inject(this, view);
        }
    }
}
