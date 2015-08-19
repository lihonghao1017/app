package com.jin91.preciousmetal.adapter;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.ui.service.CalenderBean;
import com.jin91.preciousmetal.ui.service.FinanceCalenActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class CalendarAdapter extends RecyclerView.Adapter {
    public List<CalenderBean> mList;
    public FinanceCalenActivity mContext;

    public CalendarAdapter(List<CalenderBean> list, Context context) {
        this.mList = list;
        this.mContext = (FinanceCalenActivity) context;
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
     class ViewHolder extends RecyclerView.ViewHolder{
        
        @ViewInject(R.id.week)
        TextView week;
        @ViewInject(R.id.date)
        TextView date;
        @ViewInject(R.id.allView)
        LinearLayout allView;

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
		final CalenderBean calenderBean=mList.get(arg1);
		ViewHolder h=(ViewHolder) arg0;
		h.week.setText(calenderBean.week);
		h.date.setText(calenderBean.date);
		h.allView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mContext.financeCalenPre.getFinCalList("FinanceCalenActivity",calenderBean.url);
			}
		});
	}

	@Override
	public android.support.v7.widget.RecyclerView.ViewHolder onCreateViewHolder(
			ViewGroup arg0, int arg1) {
		return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.calender_text_item, arg0, false));
	}
}
