package com.jin91.preciousmetal.ui.service;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.customview.pagerindicator.IcsLinearLayout;

public class CalenderView extends HorizontalScrollView {
	/** Title text used when no title is provided by the adapter. */
	private static final CharSequence EMPTY_TITLE = "";

	private Runnable mTabSelector;
	private PositionCallBack pcb;
	private ArrayList<CalenderBean> CalenderBeans;
	private final OnClickListener mTabClickListener = new OnClickListener() {
		public void onClick(View view) {
			int index = (Integer) view.getTag();
			setCurrentItem(index);
			financeCalenActivity.financeCalenPre.getFinCalList("FinanceCalenActivity",CalenderBeans.get(index).url);
		}
	};

	public  com.jin91.preciousmetal.customview.pagerindicator.IcsLinearLayout mTabLayout;

	private ViewPager.OnPageChangeListener mListener;

	private int mMaxTabWidth;
	private int mSelectedTabIndex;
	private FinanceCalenActivity financeCalenActivity;

	public CalenderView(Context context) {
		this(context, null);
		
	}

	public CalenderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		financeCalenActivity=(FinanceCalenActivity) context;
		setHorizontalScrollBarEnabled(false);
		mTabLayout = new IcsLinearLayout(context,
				R.attr.vpiTabPageIndicatorStyle);
		addView(mTabLayout, new ViewGroup.LayoutParams(WRAP_CONTENT,
				MATCH_PARENT));
	}

	public void setData(ArrayList<CalenderBean> CalenderBeans) {
		this.CalenderBeans=CalenderBeans;
		notifyDataSetChanged();
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		final boolean lockedExpanded = widthMode == MeasureSpec.EXACTLY;
		setFillViewport(lockedExpanded);

		final int childCount = mTabLayout.getChildCount();
		if (childCount > 1
				&& (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST)) {
			if (childCount > 2) {
				mMaxTabWidth = (int) (MeasureSpec.getSize(widthMeasureSpec) * 0.4f);
			} else {
				mMaxTabWidth = MeasureSpec.getSize(widthMeasureSpec) / 2;
			}
		} else {
			mMaxTabWidth = -1;
		}

		final int oldWidth = getMeasuredWidth();
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int newWidth = getMeasuredWidth();

		if (lockedExpanded && oldWidth != newWidth) {
			setCurrentItem(mSelectedTabIndex);
		}
	}

	private void animateToTab(final int position) {
		final View tabView = mTabLayout.getChildAt(position);
		if (mTabSelector != null) {
			removeCallbacks(mTabSelector);
		}
		mTabSelector = new Runnable() {
			public void run() {
				final int scrollPos = tabView.getLeft()
						- (getWidth() - tabView.getWidth()) / 2;
				smoothScrollTo(scrollPos, 0);
				mTabSelector = null;
			}
		};
		post(mTabSelector);
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		if (mTabSelector != null) {
			post(mTabSelector);
		}
	}

	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (mTabSelector != null) {
			removeCallbacks(mTabSelector);
		}
	}

	private void addTab(int index, CharSequence date, CharSequence xingqi) {
		View v = LayoutInflater.from(getContext()).inflate(
				R.layout.calender_text_item, null);
		((TextView) v.findViewById(R.id.week)).setText(xingqi);
		((TextView) v.findViewById(R.id.date)).setText(date);
		v.setOnClickListener(mTabClickListener);
		v.setTag(index);
		mTabLayout
				.addView(v, new LinearLayout.LayoutParams(0, MATCH_PARENT, 1));
	}

	public void notifyDataSetChanged() {
		mTabLayout.removeAllViews();
		final int count = CalenderBeans.size();
		for (int i = 0; i < count; i++) {
			CalenderBean title = CalenderBeans.get(i);
			addTab(i, title.date, title.week);
		}
		if (mSelectedTabIndex > count) {
			mSelectedTabIndex = count - 1;
		}
		setCurrentItem(mSelectedTabIndex);
		requestLayout();
	}

	public void setPositionCallBack(PositionCallBack pcb) {
		this.pcb = pcb;
	}

	public interface PositionCallBack {
		public void getCurrentPosition(int postion);
	}

	public void setCurrentItem(int item) {
		mSelectedTabIndex = item;
		final int tabCount = mTabLayout.getChildCount();
		for (int i = 0; i < tabCount; i++) {
			final View child = mTabLayout.getChildAt(i);
			final boolean isSelected = (i == item);
			child.setBackgroundColor(0x00000000);
			if (isSelected) {
				child.setBackgroundColor(0xFFFFFFFF);
				animateToTab(item);
			}
		}
	}
}
