package com.jin91.preciousmetal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jin91.preciousmetal.R;

/**
 * Created by lijinhua on 2015/4/23.
 * 底部分享的dialog
 */
public class ShareDialogAdapter extends BaseAdapter{

    private Context context;
    private int[] iconList;
    private int[] namelist;

    public ShareDialogAdapter(Context context, int[] iconList, int[] namelist)
    {
        super();
        this.iconList = iconList;
        this.namelist = namelist;
        this.context = context;
    }
    @Override
    public int getCount() {
        return iconList.length;
    }

    @Override
    public Object getItem(int position) {
        return iconList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.share_dialog_item, parent,false);
            holder.icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            holder.name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(namelist[position]);
        holder.icon.setImageResource(iconList[position]);
        return convertView;
    }
    static class ViewHolder {
        ImageView icon;
        TextView name;
    }
}
