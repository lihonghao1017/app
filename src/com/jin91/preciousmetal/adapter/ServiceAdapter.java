package com.jin91.preciousmetal.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.api.entity.Room;
import com.jin91.preciousmetal.util.ScreenUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by lijinhua on 2015/5/12.
 */
public class ServiceAdapter extends BaseAdapter {

    private Context mContext;
    private List<Room> mList;
    private int selectItem;

    public ServiceAdapter(Context mContext, List<Room> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        if (mList.size() > 0) {
            return Integer.MAX_VALUE;
        }
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.service_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Room room = mList.get(position % mList.size());
        holder.tv_play_name.setText(room.Name);
        Glide.with(mContext).load(ChangeImgUrlToMoblie(room.ImageUrlApp)).into(holder.iv_play_img);
        if (selectItem == position) {
            convertView.setLayoutParams(new Gallery.LayoutParams(ScreenUtil.dip2px(mContext, 120), ScreenUtil.dip2px(mContext, 120)));
            holder.tv_play_name.setTextColor(mContext.getResources().getColor(R.color.red));
            holder.iv_play_img.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtil.dip2px(mContext, 70), ScreenUtil.dip2px(mContext, 70)));
            if("1".equals(room.CustomerId))
            {
                holder.tv_bind_status.setText("已绑定");
                holder.tv_bind_status.setVisibility(View.VISIBLE);
            }else
            {
                holder.tv_bind_status.setVisibility(View.INVISIBLE);
            }

        } else {
            holder.tv_bind_status.setVisibility(View.INVISIBLE);
            holder.tv_play_name.setTextColor(mContext.getResources().getColor(R.color.gray));
            holder.iv_play_img.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtil.dip2px(mContext, 48), ScreenUtil.dip2px(mContext, 48)));
            convertView.setLayoutParams(new Gallery.LayoutParams(ScreenUtil.dip2px(mContext, 80), ScreenUtil.dip2px(mContext, 80)));
        }
//        if ("1".equals(room.CustomerId)) {
//            // 已绑定
//            holder.tv_bind_status.setText("已绑定");
//            holder.tv_bind_status.setVisibility(View.VISIBLE);
////            holder.tv_bind_status.setTextColor(mContext.getResources().getColor(R.color.red));
//        } else {
//            // 未绑定
////            holder.tv_bind_status.setText("未绑定");
////            holder.tv_bind_status.setTextColor(mContext.getResources().getColor(R.color.deep_gray));
//            holder.tv_bind_status.setVisibility(View.GONE);
//
//        }

        return convertView;
    }


    static class ViewHolder {
        @ViewInject(R.id.iv_play_img)
        ImageView iv_play_img;
        @ViewInject(R.id.tv_bind_status)
        TextView tv_bind_status;
        @ViewInject(R.id.tv_play_name)
        TextView tv_play_name;

        ViewHolder(View view) {
        	ViewUtils.inject(this, view);
        }
    }

    public void setSelectItem(int selectItem) {

        if (this.selectItem != selectItem) {
            this.selectItem = selectItem;
            notifyDataSetChanged();
        }
    }

    public String ChangeImgUrlToMoblie(String link)// 改变图片地址为mobile专用
    {
        String result_name = null;
        int idx = link.lastIndexOf("/", link.length());
        String firsturl = link.substring(0, idx + 1);
        String name = link.substring(idx + 1, link.length());
        if (!name.startsWith("M_")) {
            result_name = "M_" + name;

        }
        return firsturl + result_name;
    }

}
