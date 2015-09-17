package com.jin91.preciousmetal.ui.service;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.google.gson.reflect.TypeToken;
import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.adapter.DirectPlayAdapter;
import com.jin91.preciousmetal.common.api.entity.DirectPlay;
import com.jin91.preciousmetal.common.api.entity.LiveRoom;
import com.jin91.preciousmetal.common.util.Logger;
import com.jin91.preciousmetal.customview.ExpandListView;
import com.jin91.preciousmetal.ui.Constants;
import com.jin91.preciousmetal.ui.service.play.BasePlayFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class AllRomeFragment extends BasePlayFragment<DirectPlay> implements
		OnClickListener {
	@ViewInject(R.id.id_stickynavlayout_innerscrollview)
	ExpandListView mListView;
	@ViewInject(R.id.swipeRereshLayout)
	SwipeRefreshLayout swipeRereshLayout;
	@ViewInject(R.id.fl_price_content)
	FrameLayout fl_price_content;
	private DirectPlayAdapter adapter;
	private ArrayList<DirectPlay> mList;
	private static final String action = "getlivedata";
	private int index;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.activity = (ServiceDetailActivity) activity;
	}

	private ServiceDetailActivity activity;

	public AllRomeFragment(int index) {
		this.index = index;
		
	}
	public ArrayList<DirectPlay> getData(int index){
		switch (index) {
		case 0:
			return activity.mAllList;
		case 1:

			return activity.mBaiyinList;
		case 2:

			return activity.mHuangjinList;
		case 3:

			return activity.mTongLvList;
		case 4:

			return activity.mShiyouList;
		}
		return null;
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.rome_frgament, null);
		ViewUtils.inject(this, v);
		isUserShu=true;
		initialize();
		emptyLayout.setEmptyButtonClickListener(this);
		emptyLayout.setErrorButtonClickListener(this);

		adapter = new DirectPlayAdapter(mContext, getData(index));
		mListView.setAdapter(adapter);
		mListView.setOnRefreshListener(new ExpandListView.OnRefreshListener() {

			@Override
			public void onScrollListener(int firstitem, int visibleitem,
					int totalitem) {
			}

			@Override
			public void onItemClickListener(AdapterView<?> parent, View view,
					int position, long id) {
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onFootRefresh() {
				if (!threadStart) {
					threadStart = true;
					presenter.getDirectPlayMoreNewList(null,
							getData(index).get(getData(index).size() - 1).ID, action, index + "");
				} else {
					setRefreshComplete();
				}
				isAutoRefresh = false;

			}
		}, true);
		swipeRereshLayout
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {
						if (!threadStart) {
							threadStart = true;
							isAutoRefresh = false;
							presenter.getDirectPlayFirstNewList(null, false,
									index + "", startId);
						} else {
							 threadStart = false;
							 mListView.setRefreshFootComplet();
							 swipeRereshLayout.setRefreshing(false);
//							setRefreshComplete();
						}

					}
				});
		mListView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (detailActivity != null) {
						// detailActivity.setDirectPlayComInfo(replyType,
						// replyToId);
					}
					break;
				}
				return false;
			}
		});
		if (getData(index).size() == 0) {
			presenter.getDirectPlayFirstNewList(
					new TypeToken<LiveRoom<DirectPlay>>() {
					}, true, index + "", startId);
		}
		return v;
	}

	@Override
	public void setFistDataList(List<DirectPlay> list) {
		if (fl_price_content != null
				&& fl_price_content.getVisibility() == View.VISIBLE) {
			fl_price_content.setVisibility(View.GONE);
		}

		startId = list.get(0).ID;
		presenter.setLiveid(startId);
		getData(index).clear();
		getData(index).addAll(list);
		adapter.notifyDataSetChanged();
	}

	@OnClick({ R.id.ll_view_error, R.id.btn_empty_data })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_view_error: // 网络错误
			presenter
					.getDirectPlayFirstNewList(null, true, index + "", startId);
			break;
		case R.id.btn_empty_data: // 数据为null
			presenter
					.getDirectPlayFirstNewList(null, true, index + "", startId);
			break;
		}
	}

	@Override
	public void setMoreDataList(List<DirectPlay> list) {
		getData(index).addAll(list);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void startThreadGetList() {
//		refreshHandler.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				Logger.i(ServiceDetailActivity.TAG, "isVisable===+" + isVisable);
//				if (!threadStart && isVisable) {
//					Logger.i(ServiceDetailActivity.TAG, "线程开始获取数据===+");
//					threadStart = true;
//					isAutoRefresh = true;
//					presenter.getDirectPlayFirstNewList(null, false,
//							index + "", startId);
//				}
//			}
//		}, Constants.ROOM_INTERVAL);
	}

}
