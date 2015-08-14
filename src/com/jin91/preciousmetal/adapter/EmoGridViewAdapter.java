package com.jin91.preciousmetal.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jin91.preciousmetal.customview.emo.EmoCommonUtil;


public class EmoGridViewAdapter extends BaseAdapter {
    private Context context = null;
    private int[] emoResIds = null;

    public EmoGridViewAdapter(Context cxt, int[] ids) {
        this.context = cxt;
        this.emoResIds = ids;
    }

    @Override
    public int getCount() {
        return emoResIds.length;
    }

    @Override
    public Object getItem(int position) {
        return emoResIds[position];
    }

    @Override
    public long getItemId(int position) {
        return emoResIds[position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            GridViewHolder gridViewHolder = null;
            if (null == convertView && null != context) {
                gridViewHolder = new GridViewHolder();
                convertView = gridViewHolder.layoutView;
                if (convertView != null) {
                    convertView.setTag(gridViewHolder);
                }
            } else {
                gridViewHolder = (GridViewHolder) convertView.getTag();
            }
            if (null == gridViewHolder || null == convertView) {
                return null;
            }
            gridViewHolder.faceIv.setImageBitmap(getBitmap(position));

            if (position == emoResIds.length - 1) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                params.topMargin = EmoCommonUtil.getElementSzie(context) / 3;
                gridViewHolder.faceIv.setLayoutParams(params);
            }
            return convertView;
        } catch (Exception e) {
            return null;
        }
    }

    private Bitmap getBitmap(int position) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeResource(context.getResources(), emoResIds[position]);
        } catch (Exception e) {
        }
        return bitmap;
    }

    public class GridViewHolder {
        public LinearLayout layoutView;
        public ImageView faceIv;

        public GridViewHolder() {
            try {
                LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                layoutView = new LinearLayout(context);
                faceIv = new ImageView(context);
                layoutView.setLayoutParams(layoutParams);
                layoutView.setOrientation(LinearLayout.VERTICAL);
                layoutView.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(EmoCommonUtil.getElementSzie(context), EmoCommonUtil.getElementSzie(context));
                params.gravity = Gravity.CENTER;
                layoutView.addView(faceIv, params);
            } catch (Exception e) {
            }
        }
    }
}