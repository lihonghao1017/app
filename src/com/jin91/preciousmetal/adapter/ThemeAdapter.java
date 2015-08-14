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
import android.widget.TextView;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.api.entity.Theme;
import com.jin91.preciousmetal.ui.Constants;
import com.jin91.preciousmetal.util.FaceUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by lijinhua on 2015/5/13.
 * 主题的adapter
 */
@SuppressLint("NewApi")
public class ThemeAdapter extends BaseAdapter {

    private Context mContext;
    private List<Theme> mList;

    public ThemeAdapter(Context mContext, List<Theme> mList) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.theme_list_item, parent, false);
            holder = new ViewHolder(convertView);
            listener = new FullTextOnClickListener(holder.tv_theme_content);
            holder.tv_theme_contentmore.setOnClickListener(listener);
            convertView.setTag(holder.tv_theme_contentmore.getId(), listener);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            listener = (FullTextOnClickListener) convertView.getTag(holder.tv_theme_contentmore.getId());
        }

        Theme theme = mList.get(position);
        holder.tv_theme_name.setText(theme.Title);
        setTextCollapsb(holder, theme);
        if (theme.Imgs != null && theme.Imgs.size() > 0) {
            holder.gv_them_imgs.setVisibility(View.VISIBLE);
            GrideViewImgAdapter adapter = new GrideViewImgAdapter(mContext, theme.Imgs);
            holder.gv_them_imgs.setOnItemClickListener(adapter);
            holder.gv_them_imgs.setAdapter(adapter);
        } else {
            holder.gv_them_imgs.setVisibility(View.GONE);
        }
        listener.setPosition(position);
        return convertView;
    }

    public void setTextCollapsb(final ViewHolder holder, Theme message) {
        holder.tv_theme_content.setText(FaceUtil.textAddFace(mContext, TextUtils.isEmpty(message.Contents) ? message.Contents : message.Contents.trim()));
        holder.tv_theme_content.setMovementMethod(LinkMovementMethod.getInstance());
        holder.tv_theme_content.post(new Runnable() {
            @Override
            public void run() {
                if (holder.tv_theme_content.getLineCount() > Constants.MAX_LINE) {
                    holder.tv_theme_contentmore.setVisibility(View.VISIBLE);
                } else {
                    holder.tv_theme_contentmore.setVisibility(View.GONE);
                }
            }
        });
        if (message.isFullText) {
            holder.tv_theme_content.setMaxLines(Integer.MAX_VALUE);
            holder.tv_theme_contentmore.setText(mContext.getString(R.string.desc_shrinkup));
        } else {
            holder.tv_theme_content.setMaxLines(Constants.MAX_LINE);
            holder.tv_theme_contentmore.setText(mContext.getString(R.string.desc_spread));
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
                case R.id.tv_theme_contentmore:
                    Theme message = mList.get(position);
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

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'theme_list_item.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @ViewInject(R.id.tv_theme_name)
        TextView tv_theme_name;
        @ViewInject(R.id.tv_theme_content)
        TextView tv_theme_content;
        @ViewInject(R.id.tv_theme_contentmore)
        TextView tv_theme_contentmore;
        @ViewInject(R.id.gv_them_imgs)
        GridView gv_them_imgs;

        ViewHolder(View view) {
        	ViewUtils.inject(this, view);
        }
    }
}
