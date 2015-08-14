package com.jin91.preciousmetal.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.api.entity.Study;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by lijinhua on 2015/4/28.
 */
public class NewsAdapter extends BaseAdapter {

    private List<Study> mList;
    private Context mContext;

    public NewsAdapter(Context mContext, List<Study> list) {
        this.mList = list;
        this.mContext = mContext;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.news_list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Study study = mList.get(position);
        holder.tv_new_time.setText(study.adddate);
        holder.tv_new_title.setText(study.title);
        return convertView;
    }

    static class ViewHolder {
        @ViewInject(R.id.tv_new_title)
        TextView tv_new_title;
        @ViewInject(R.id.tv_new_time)
        TextView tv_new_time;

        public ViewHolder(View view) {
        	ViewUtils.inject(this, view);
        }
    }
}
