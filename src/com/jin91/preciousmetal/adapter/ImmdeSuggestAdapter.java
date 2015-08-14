package com.jin91.preciousmetal.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.api.entity.ImmedSuggest;
import com.jin91.preciousmetal.customview.GlideCircleTransform;
import com.jin91.preciousmetal.ui.Constants;
import com.jin91.preciousmetal.util.StringUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;


/**
 * Created by lijinhua on 2015/5/11.
 * 即时建议
 */
@SuppressLint("NewApi")
public class ImmdeSuggestAdapter extends BaseAdapter {
    public Context mContext;
    private List<ImmedSuggest> mList;
    private String suggestNote;
    private int sugColor;

    public ImmdeSuggestAdapter(Context mContext, List<ImmedSuggest> mList) {
        this.mContext = mContext;
        this.mList = mList;
        sugColor = mContext.getResources().getColor(R.color.red);
    }

    public void setSuggestNote(String suggestNote) {
        this.suggestNote = suggestNote;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.mine_immde_suggest_item, parent, false);
            holder = new ViewHolder(convertView);
            listener = new FullTextOnClickListener(holder.tv_suggest_content);
            holder.tv_sugtext_more.setOnClickListener(listener);
            convertView.setTag(holder);
            convertView.setTag(holder.tv_sugtext_more.getId(), listener);
        } else {
            holder = (ViewHolder) convertView.getTag();
            listener = (FullTextOnClickListener) convertView.getTag(holder.tv_sugtext_more.getId());
        }
        listener.setPosition(position);
        ImmedSuggest suggest = mList.get(position);
        Glide.with(mContext).load(suggest.TeacherPic).transform(new GlideCircleTransform(mContext)).error(R.mipmap.default_error_avatar).placeholder(R.mipmap.default_normal_avatar).into(holder.iv_user_avtar);
        holder.tv_sugteacher_name.setText(suggest.Teacher);
        holder.tv_suggest_time.setText(StringUtil.getSectionTime(suggest.CreatedTime));
        setTextCollapsb(holder, suggest);
        return convertView;
    }


    /**
     * 拼接回复的内容
     *
     * @param suggest
     * @return
     */
    private String getSuggestContent(ImmedSuggest suggest) {
        if (TextUtils.isEmpty(suggest.Varieties)) {
            return suggest.Content + "";
        }

//        if (!TextUtils.isEmpty(suggest.TL)) {
//            switch (suggest.TL) {
//                case Constants.TYPE_REDUCE: //  减持
//
//                    break;
//            }
//        }
        if (TextUtils.isEmpty(suggest.Content)) {
            StringBuilder builder = new StringBuilder();
            builder.append(suggest.Varieties);
            builder.append(suggest.Pattern);
            builder.append(suggest.Price);
            builder.append(suggest.Direction);
            builder.append(mContext.getString(R.string.idea_stopLoss));
            builder.append(suggest.StopLoss);
            builder.append(", ");
            builder.append(mContext.getString(R.string.idea_targetPrice));
            builder.append(suggest.TargetPrice);
            if (!suggest.Position.equals("0")) {
                builder.append(", ");
                builder.append(mContext.getString(R.string.idea_position));
                builder.append(suggest.Position);
                builder.append("%");
            } else {
                builder.append(" ");
            }
            return builder.toString() + "<br/>";
        } else {
            return suggest.Content + "<br/>";
        }


    }

    public void setTextCollapsb(final ViewHolder holder,final ImmedSuggest message) {

        holder.tv_suggest_content.setText(Html.fromHtml(message.Content + "<br/>" + "<font color=" + sugColor + ">" + suggestNote + "</font>"));
//        holder.tv_suggest_content.post(new Runnable() {
//            @Override
//            public void run() {
//                Logger.i("adapter","holder.tv_suggest_content.getLineCount()----"+holder.tv_suggest_content.getLineCount());
                if (holder.tv_suggest_content.getLineCount() > Constants.MAX_LINE) {
                    holder.tv_sugtext_more.setVisibility(View.VISIBLE);
                    holder.tv_suggest_content.setMaxLines(Constants.MAX_LINE);
                    holder.tv_sugtext_more.setText(mContext.getString(R.string.desc_spread));
                } else {
                    holder.tv_suggest_content.setMaxLines(Integer.MAX_VALUE);
                    holder.tv_sugtext_more.setVisibility(View.GONE);
                }
                if (message.isFullText) {
                    holder.tv_suggest_content.setMaxLines(Integer.MAX_VALUE);
                    holder.tv_sugtext_more.setText(mContext.getString(R.string.desc_shrinkup));
                } else {
                    holder.tv_suggest_content.setMaxLines(Constants.MAX_LINE);
                    holder.tv_sugtext_more.setText(mContext.getString(R.string.desc_spread));
                }
//            }
//        });

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
                    ImmedSuggest message = mList.get(position);
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
        ImageView iv_user_avtar;
        @ViewInject(R.id.tv_sugteacher_name)
        TextView tv_sugteacher_name;
        @ViewInject(R.id.tv_suggest_time)
        TextView tv_suggest_time;
        @ViewInject(R.id.tv_suggest_content)
        TextView tv_suggest_content;
        @ViewInject(R.id.tv_sugtext_more)
        TextView tv_sugtext_more;

        ViewHolder(View view) {
        	ViewUtils.inject(this, view);
        }
    }
}
