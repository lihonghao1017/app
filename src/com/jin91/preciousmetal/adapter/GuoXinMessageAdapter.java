package com.jin91.preciousmetal.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.api.entity.GuoXinMessage;
import com.jin91.preciousmetal.ui.Constants;
import com.jin91.preciousmetal.util.FaceUtil;
import com.jin91.preciousmetal.util.StringUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by lijinhua on 2015/5/11.
 * 国鑫消息
 */
@SuppressLint("NewApi")
public class GuoXinMessageAdapter extends BaseAdapter {
    private Context mContext;
    private List<GuoXinMessage> mList;

    public GuoXinMessageAdapter(Context mContext, List<GuoXinMessage> mList) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.guo_xin_message_list_item, parent, false);
            holder = new ViewHolder(convertView);
            listener = new FullTextOnClickListener(holder.tv_jinmessage_content);
            holder.tv_text_more.setOnClickListener(listener);
            convertView.setTag(holder);
            convertView.setTag(holder.tv_text_more.getId(), listener);
        } else {
            holder = (ViewHolder) convertView.getTag();
            listener = (FullTextOnClickListener) convertView.getTag(holder.tv_text_more.getId());
        }
        GuoXinMessage message = mList.get(position);
        holder.tv_jinmessage_date.setText(StringUtil.getFormatTime(message.CreatedTime, StringUtil.FORMAT_MONTH_DAY_HOUR_MINUTE));
        setTextCollapsb(holder, message);
        if (message.Imgs != null && message.Imgs.size() > 0) {
            holder.gv_jinmessage_imgs.setVisibility(View.VISIBLE);
            GrideViewImgAdapter adapter = new GrideViewImgAdapter(mContext, message.Imgs);
            holder.gv_jinmessage_imgs.setOnItemClickListener(adapter);
            holder.gv_jinmessage_imgs.setAdapter(adapter);
        } else {
            holder.gv_jinmessage_imgs.setVisibility(View.GONE);
        }
        listener.setPosition(position);
        return convertView;
    }


    public void setTextCollapsb(final ViewHolder holder, GuoXinMessage message) {
        holder.tv_jinmessage_content.setText(FaceUtil.textAddFace(mContext,TextUtils.isEmpty(message.Content) ? message.Content : message.Content.trim()));
        holder.tv_jinmessage_content.post(new Runnable() {
            @Override
            public void run() {
                if (holder.tv_jinmessage_content.getLineCount() > Constants.MAX_LINE) {
                    holder.tv_text_more.setVisibility(View.VISIBLE);
                } else {
                    holder.tv_text_more.setVisibility(View.GONE);
                }
            }
        });
        if (message.isFullText) {
            holder.tv_jinmessage_content.setMaxLines(Integer.MAX_VALUE);
            holder.tv_text_more.setText(mContext.getString(R.string.desc_shrinkup));
        } else {
            holder.tv_jinmessage_content.setMaxLines(Constants.MAX_LINE);
            holder.tv_text_more.setText(mContext.getString(R.string.desc_spread));
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
                case R.id.tv_text_more:
                    GuoXinMessage message = mList.get(position);
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
        @ViewInject(R.id.tv_jinmessage_content)
        TextView tv_jinmessage_content;
        @ViewInject(R.id.tv_text_more)
        TextView tv_text_more;
        @ViewInject(R.id.tv_jinmessage_date)
        TextView tv_jinmessage_date;
        @ViewInject(R.id.gv_jinmessage_imgs)
        GridView gv_jinmessage_imgs;

        ViewHolder(View view) {
        	ViewUtils.inject(this, view);
        }
    }
}
