package com.jin91.preciousmetal.adapter;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.ui.service.CalenderBean;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class CalendarAdapter extends RecyclerView.Adapter {
    public List<CalenderBean> mList;
    public Context mContext;

    public CalendarAdapter(List<CalenderBean> list, Context context) {
        this.mList = list;
        this.mContext = context;
    }

   


    @Override
    public long getItemId(int position) {
        return position;
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder;
//        if (convertView == null) {
//            convertView = LayoutInflater.from(mContext).inflate(R.layout.calender_text_item, parent, false);
//            holder = new ViewHolder(convertView);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//        CalenderBean financeCalen = mList.get(position);
//        holder.week.setText(financeCalen.week);
//        holder.date.setText(financeCalen.date);
//        return convertView;
//    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'finance_calen_list_item.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder extends RecyclerView.ViewHolder{
        
        @ViewInject(R.id.week)
        TextView week;
        @ViewInject(R.id.date)
        TextView date;

        ViewHolder(View view) {
        	 super(view);
        	ViewUtils.inject(this, view);
        }
    }

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public void onBindViewHolder(
			android.support.v7.widget.RecyclerView.ViewHolder arg0, int arg1) {
		CalenderBean calenderBean=mList.get(arg1);
		ViewHolder h=(ViewHolder) arg0;
		h.week.setText(calenderBean.week);
		h.date.setText(calenderBean.date);
	}

	@Override
	public android.support.v7.widget.RecyclerView.ViewHolder onCreateViewHolder(
			ViewGroup arg0, int arg1) {
		return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.calender_text_item, arg0, false));
	}
}
