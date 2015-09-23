package com.jin91.preciousmetal.ui.service.play;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.adapter.DirectPlayAdapter;
import com.jin91.preciousmetal.common.api.PlayRoomApi;
import com.jin91.preciousmetal.common.api.base.ResultCallback;
import com.jin91.preciousmetal.common.api.entity.AllData;
import com.jin91.preciousmetal.common.api.entity.DirectPlay;
import com.jin91.preciousmetal.common.api.entity.LiveRoom;
import com.jin91.preciousmetal.common.api.entity.User;
import com.jin91.preciousmetal.customview.EmptyLayout;
import com.jin91.preciousmetal.customview.ExpandListView;
import com.jin91.preciousmetal.ui.PreciousMetalAplication;
import com.jin91.preciousmetal.ui.service.ServiceDetailActivity;
import com.jin91.preciousmetal.util.JsonUtil;
import com.jin91.preciousmetal.util.MessageToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class AllRomeFragment extends Fragment implements OnClickListener {
	@ViewInject(R.id.id_stickynavlayout_innerscrollview)
	ExpandListView mListView;
	@ViewInject(R.id.swipeRereshLayout)
	SwipeRefreshLayout swipeRereshLayout;
	@ViewInject(R.id.fl_price_content)
	FrameLayout fl_price_content;
	private DirectPlayAdapter adapter;
	private static final String action = "getlivedata";
	private int index;
	private String startId = "o";
	// private PlayPresenter presenter;
	SharedPreferences preferences;
	private String userId;
	public String roomId;
	EmptyLayout emptyLayout;
	private String replyType="1"; // 回复的类型 // 2--回复或者交流 1 提问
	private String replyToId=""; // 回复的id 如果是交流传0,回复传回复的id,提问的时候不传
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (ServiceDetailActivity) activity;
		User user = PreciousMetalAplication.INSTANCE.user;
		if (user == null) {
			userId = "";
		} else {
			userId = user.ID;
		}
		Intent intent = activity.getIntent();
		if (intent != null) {
			roomId = intent.getStringExtra("roomId");
		}
	}

	private ServiceDetailActivity activity;

	public AllRomeFragment(int index) {
		this.index = index;

	}

	public ArrayList<DirectPlay> getData(int index) {
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
		preferences = PreciousMetalAplication.getContext()
				.getSharedPreferences("0", Context.MODE_PRIVATE);
		emptyLayout = new EmptyLayout(getActivity(), fl_price_content);
		emptyLayout.setEmptyButtonClickListener(this);
		emptyLayout.setErrorButtonClickListener(this);

		adapter = new DirectPlayAdapter(getActivity(), getData(index));
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
//				 if (!threadStart) {
//				 threadStart = true;
				 getDirectPlayMoreNewList(null,
				 getData(index).get(getData(index).size() - 1).ID, action,
				 index + "");
//				 } else {
//				 setRefreshComplete();
//				 }
//				 isAutoRefresh = false;

			}
		}, true);
		swipeRereshLayout
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {
//						if (!threadStart) {
//							threadStart = true;
//							isAutoRefresh = false;
							getDirectPlayFirstNewList(null, false, index + "",
									startId);
//						} else {
//							threadStart = false;
//							mListView.setRefreshFootComplet();
//							swipeRereshLayout.setRefreshing(false);
//							// setRefreshComplete();
//						}

					}
				});
		mListView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					 if (activity != null) {
						 activity.setDirectPlayComInfo(replyType,
					  replyToId);
					 }
					break;
				}
				return false;
			}
		});
		if (getData(index).size() == 0) {
			getDirectPlayFirstNewList(new TypeToken<LiveRoom<DirectPlay>>() {
			}, true, index + "", startId);
		}
		return v;
	}

	public void setFistDataList(List<DirectPlay> list) {
		if (fl_price_content != null
				&& fl_price_content.getVisibility() == View.VISIBLE) {
			fl_price_content.setVisibility(View.GONE);
		}

		startId = list.get(0).ID;
		// presenter.setLiveid(startId);
		getData(index).clear();
		getData(index).addAll(list);
		adapter.notifyDataSetChanged();
	}

	@OnClick({ R.id.ll_view_error, R.id.btn_empty_data })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_view_error: // 网络错误
			getDirectPlayFirstNewList(null, true, index + "", startId);
			break;
		case R.id.btn_empty_data: // 数据为null
			getDirectPlayFirstNewList(null, true, index + "", startId);
			break;
		}
	}

	private HashMap<String, String> getFirstMapParams(String type) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("liveid", preferences.getString("liveid", "0"));
		map.put("speakid", preferences.getString("speakid", "0"));
		map.put("mydataid", preferences.getString("mydataid", "0"));
		map.put("topicid", preferences.getString("topicid", "0"));
		map.put("marrowid", preferences.getString("marrowid", "0"));
		map.put("callid", preferences.getString("callid", "0"));
		//
		map.put("roomid", roomId);
		map.put("uid", userId);
		map.put("type", type);
		return map;
	}

	public void getDirectPlayFirstNewList(Object typeToken,
			final boolean isShowLoading, final String type, String startId) {
		if (isShowLoading) {
			emptyLayout.showLoading();
		}
		HashMap<String, String> map = getFirstMapParams("0");
		map.put("LeftIndex", type);
		PlayRoomApi.getDirectPlayMoreList(ServiceDetailActivity.TAG, map,
				startId, new ResultCallback() {
					@SuppressWarnings("unchecked")
					@Override
					public void onEntitySuccess(String json) {
						TypeToken obj = new TypeToken<LiveRoom<DirectPlay>>() {
						};
						try {
							LiveRoom<DirectPlay> liveRoom = (LiveRoom<DirectPlay>) JsonUtil
									.parse(json, obj);
							if (liveRoom == null || liveRoom.Alldata == null) {
								if (isShowLoading) {
									emptyLayout.showError();
								}
								return;
							}
							// 设置新的消息
							activity.setNewMsgCount(liveRoom.Alldata);
							if (liveRoom.Alldata.Table == null
									|| liveRoom.Alldata.Table.size() == 0) {
								if (isShowLoading&&getData(index).size()==0) {
									emptyLayout.showEmpty();
								}
								return;
							}
							setFistDataList(liveRoom.Alldata.Table);
							if (isShowLoading) {
								emptyLayout.hideLoadinAnim();
								fl_price_content.setVisibility(View.GONE);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							mListView.setRefreshFootComplet();
							swipeRereshLayout.setRefreshing(false);
						}
					}

					@Override
					public void onException(com.android.volley.VolleyError e) {
						if (isShowLoading) {
							// 加载失败
							emptyLayout.showError();
						} else {
							MessageToast.showToast(R.string.net_error, 0);
						}
						mListView.setRefreshFootComplet();
						swipeRereshLayout.setRefreshing(false);
					}
				});
	}

	public void getDirectPlayMoreNewList(Object typeToken, String startId,
			String action, String type) {
		PlayRoomApi.getDirectPlayMoreNewList(ServiceDetailActivity.TAG, userId,
				roomId, startId, action, type, new ResultCallback() {
					@Override
					public void onEntitySuccess(String json) {
						TypeToken obj = new TypeToken<AllData<DirectPlay>>() {
						};

						try {
							AllData<DirectPlay> allData = (AllData<DirectPlay>) JsonUtil
									.parse(json, obj);
							if (allData != null && allData.Table != null
									&& allData.Table.size() > 0) {
								getData(index).addAll(allData.Table);
								adapter.notifyDataSetChanged();
							} else {
								MessageToast
										.showToast(R.string.no_more_data, 0);
							}
						} finally {
							mListView.setRefreshFootComplet();
							swipeRereshLayout.setRefreshing(false);
						}

					}

					@Override
					public void onException(com.android.volley.VolleyError e) {
						mListView.setRefreshFootComplet();
						swipeRereshLayout.setRefreshing(false);
						MessageToast.showToast(R.string.net_error, 0);
					}
				});
	}
	

}
