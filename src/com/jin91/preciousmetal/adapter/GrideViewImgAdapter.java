package com.jin91.preciousmetal.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.api.entity.Image;
import com.jin91.preciousmetal.ui.service.ViewPagerPhotoActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by lijinhua on 2015/5/11.
 */
public class GrideViewImgAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

    private Context mContext;
    private List<Image> mUrls;

    public GrideViewImgAdapter(Context mContext, List<Image> mUrls) {
        this.mContext = mContext;
        this.mUrls = mUrls;
    }

    @Override
    public int getCount() {
        return mUrls.size();
    }

    @Override
    public Object getItem(int position) {
        return mUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grideview_img_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(mContext).load(mUrls.get(position).Img).placeholder(R.mipmap.default_icon).error(R.mipmap.default_icon).into(holder.iv_image);
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ViewPagerPhotoActivity.actionLaunch(mContext, mUrls, position);
    }

    static class ViewHolder {
        @ViewInject(R.id.iv_image)
        ImageView iv_image;

        ViewHolder(View view) {
        	ViewUtils.inject(this, view);
        }
    }
}
