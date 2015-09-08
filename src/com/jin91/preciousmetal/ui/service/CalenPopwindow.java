package com.jin91.preciousmetal.ui.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.jin91.preciousmetal.R;

public class CalenPopwindow {
	private static final String[] years = { "2000年", "2001年", "2002年", "2003年",
			"2004年", "2005年", "2006年", "2007年", "2008年", "2009年", "2010年",
			"2011年", "2012年", "2013年", "2014年", "2015年", "2016年", "2017年",
			"2018年", "2019年", "2020年" };
	private static final String[] moths = { "1月", "2月", "3月", "4月", "5月", "6月",
			"7月", "8月", "9月", "10月", "11月", "12月" };
	private static final String[] dates = { "1日", "2日", "3日", "4日", "5日", "6日",
			"7日", "8日", "9日", "10日", "11日", "12日", "13日", "14日", "15日", "16日",
			"17日", "18日", "19日", "20日", "21日", "22日", "23日", "24日", "25日",
			"26日", "27日", "28日", "29日", "30日", "31日" };

	public static PopupWindow getCalenPopwindow(final Context context, View v) {
		View mView = LayoutInflater.from(context).inflate(
				R.layout.calen_popwindow_layout, null);
		final PopupWindow menuWindow = new PopupWindow(mView,
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		menuWindow.setOutsideTouchable(true);
		menuWindow.setFocusable(true);
		menuWindow.setBackgroundDrawable(new ColorDrawable(-00000));
		menuWindow.setTouchable(true);
		menuWindow.setContentView(mView);
		Calendar c = Calendar.getInstance();
		int year01 = c.get(Calendar.YEAR);
		int month01 = c.get(Calendar.MONTH);
		int day01 = c.get(Calendar.DAY_OF_MONTH);
		final NumberPicker year = (NumberPicker) mView
				.findViewById(R.id.numberpicker_year);
		year.setDisplayedValues(years);
		year.setMaxValue(years.length - 1);
		year.setMinValue(0);
		year.setValue(year01-2000);
		final NumberPicker moth = (NumberPicker) mView
				.findViewById(R.id.numberpicker_moth);
		moth.setDisplayedValues(moths);
		moth.setMaxValue(moths.length - 1);
		moth.setMinValue(0);
		moth.setValue(month01);
		final NumberPicker date = (NumberPicker) mView
				.findViewById(R.id.numberpicker_date);
		date.setDisplayedValues(dates);
		date.setMaxValue(dates.length - 1);
		date.setMinValue(0);
		date.setValue(day01-1);
		menuWindow.showAtLocation(v, Gravity.TOP | Gravity.LEFT, 0, 0);

		mView.findViewById(R.id.CalenPopwindow_cancle).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						menuWindow.dismiss();
					}
				});
		mView.findViewById(R.id.CalenPopwindow_cormit).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						String y = years[year.getValue()].replace("年", "");
						String m = moths[moth.getValue()].replace("月", "");
						if (m.length() < 2)
							m = 0 + m;
						String d = dates[date.getValue()].replace("日", "");
						if (d.length() < 2)
							d = 0 + d;
						SimpleDateFormat sdf= new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
							try {
								Date dt2 = sdf.parse(m+"/"+d+"/"+y+" 00:00:00");
								((FinanceCalenActivity) context).setCalenderData(dt2.getTime());
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						((FinanceCalenActivity) context).financeCalenPre
								.getFinCalList(FinanceCalenActivity.TAG, y + m
										+ d);
						menuWindow.dismiss();
					}
				});
		return menuWindow;
	}
}
