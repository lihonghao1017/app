package com.jin91.preciousmetal.adapter;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.api.entity.Alarm;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by lijinhua on 2015/5/21.
 */
public class PriceAlarmAdapter extends BaseAdapter {

    public Context mContext;
    public List<Alarm> mlist;
    SharedPreferences preferences;

    public PriceAlarmAdapter(Context mContext, List<Alarm> mlist) {
        this.mContext = mContext;
        this.mlist = mlist;
        preferences = mContext.getSharedPreferences("price", Context.MODE_PRIVATE);
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.price_alarm_list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Alarm alarm = mlist.get(position);
        if (TextUtils.isEmpty(alarm.D)) {
            holder.tvAlarmPrice.setText("");
        } else {
            holder.tvAlarmPrice.setText(("0".equals(alarm.D) ? "价格<=" : ">=") + alarm.P);
        }
        holder.tvAlarmName.setText(preferences.getString(alarm.C, ""));
        holder.tvAlarmAddtime.setText(alarm.B + "   创建");
        holder.tvAlarmFailime.setText(alarm.E + "   失效");
        return convertView;
    }


    static class ViewHolder {
        @ViewInject(R.id.tv_alarm_name)
        TextView tvAlarmName;
        @ViewInject(R.id.tv_alarm_price)
        TextView tvAlarmPrice;
        @ViewInject(R.id.tv_alarm_addtime)
        TextView tvAlarmAddtime;
        @ViewInject(R.id.tv_alarm_failime)
        TextView tvAlarmFailime;

        ViewHolder(View view) {
        	ViewUtils.inject(this, view);
        }
    }
}
