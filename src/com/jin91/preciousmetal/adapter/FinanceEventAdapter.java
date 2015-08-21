package com.jin91.preciousmetal.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.api.entity.FinanceEvent;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class FinanceEventAdapter extends BaseAdapter {
    public List<FinanceEvent> mList;
    public Context mContext;

    public FinanceEventAdapter(List<FinanceEvent> list, Context context) {
        this.mList = list;
        this.mContext = context;
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.finance_calen_list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        FinanceEvent financeCalen = mList.get(position);
        holder.tv_finance_time.setText(financeCalen.Time);
        if ("1".equals(financeCalen.Importance)) {
            holder.tv_finance_leve.setTextColor(mContext.getResources().getColor(R.color.deep_gray));
            holder.tv_finance_leve.setText("低");
        } else if ("2".equals(financeCalen.Importance)) {
            holder.tv_finance_leve.setTextColor(mContext.getResources().getColor(R.color.orange_gray));
            holder.tv_finance_leve.setText("中");
        } else {
            holder.tv_finance_leve.setTextColor(mContext.getResources().getColor(R.color.orange));
            holder.tv_finance_leve.setText("高");
        }
        holder.tv_finance_content.setText(financeCalen.Event);
        holder.tv_finance_forward.setText("国家: " + financeCalen.Country);
        holder.tv_finance_project.setText("地区: " + financeCalen.Area);
        holder.tv_finance_result.setVisibility(View.GONE);
        return convertView;
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'finance_calen_list_item.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @ViewInject(R.id.tv_finance_time)
        TextView tv_finance_time;
        @ViewInject(R.id.tv_finance_leve)
        TextView tv_finance_leve;
        @ViewInject(R.id.tv_finance_content)
        TextView tv_finance_content;
        @ViewInject(R.id.tv_finance_forward)
        TextView tv_finance_forward;
        @ViewInject(R.id.tv_finance_project)
        TextView tv_finance_project;
        @ViewInject(R.id.tv_finance_result)
        TextView tv_finance_result;

        ViewHolder(View view) {
        	ViewUtils.inject(this, view);
        }
    }
}

