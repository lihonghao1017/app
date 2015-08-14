package com.jin91.preciousmetal.ui.price;

import java.util.List;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.AbsListView.LayoutParams;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.api.entity.Price;

/**
 * Created by xiaolin on 2015/1/24.
 */
public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private List<Price> strList;
    private int hidePosition = AdapterView.INVALID_POSITION;

    public GridViewAdapter(Context context, List<Price> strList) {
        this.context = context;
        this.strList = strList;
    }

    @Override
    public int getCount() {
        return strList.size();
    }

    @Override
    public Price getItem(int position) {
        return strList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view;
        if(convertView == null) {
            view = new TextView(context);
            LayoutParams lp=new LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(lp);
            view.setBackgroundResource(R.drawable.zixuan_textview_bj_style);
            view.setGravity(Gravity.CENTER);
            view.setTextSize(14);
            view.setTextColor(0xFFF7F8F8);
            
        }
        else {
            view = (TextView)convertView;
        }

        //hide时隐藏Text
        if(position != hidePosition) {
            view.setText(strList.get(position).name);
        }
        else {
            view.setText("");
        }
        view.setId(position);

        return view;
    }

    public void hideView(int pos) {
        hidePosition = pos;
        notifyDataSetChanged();
    }

    public void showHideView() {
        hidePosition = AdapterView.INVALID_POSITION;
        notifyDataSetChanged();
    }

    public void removeView(int pos) {
        strList.remove(pos);
        notifyDataSetChanged();
    }

    //更新拖动时的gridView
    public void swapView(int draggedPos, int destPos) {
        //从前向后拖动，其他item依次前移
        if(draggedPos < destPos) {
            strList.add(destPos+1, getItem(draggedPos));
            strList.remove(draggedPos);
        }
        //从后向前拖动，其他item依次后移
        else if(draggedPos > destPos) {
            strList.add(destPos, getItem(draggedPos));
            strList.remove(draggedPos+1);
        }
        hidePosition = destPos;
        notifyDataSetChanged();
    }
}
