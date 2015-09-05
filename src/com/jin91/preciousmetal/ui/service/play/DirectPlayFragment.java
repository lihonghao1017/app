package com.jin91.preciousmetal.ui.service.play;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.adapter.DirectPlayAdapter;
import com.jin91.preciousmetal.common.api.entity.AllData;
import com.jin91.preciousmetal.common.api.entity.DirectPlay;
import com.jin91.preciousmetal.common.api.entity.LiveRoom;
import com.jin91.preciousmetal.common.util.Logger;
import com.jin91.preciousmetal.customview.ExpandListView;
import com.jin91.preciousmetal.ui.Constants;
import com.jin91.preciousmetal.ui.service.ServiceDetailActivity;
import com.jin91.preciousmetal.util.InputUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by lijinhua on 2015/5/12.
 */
public class DirectPlayFragment extends BasePlayFragment<DirectPlay> implements View.OnClickListener {

    public static final String type = "0";
    public static final String action = "getlivedata";
    @ViewInject(R.id.fl_price_content)
    FrameLayout fl_price_content;
    @ViewInject(R.id.id_stickynavlayout_innerscrollview)
    ExpandListView mListView;
    @ViewInject(R.id.swipeRereshLayout)
    SwipeRefreshLayout swipeRereshLayout;
    @ViewInject(R.id.liveRoom_all)
    TextView all_tv;
    @ViewInject(R.id.liveRoom_baiyin)
    TextView baiyin_tv;
    @ViewInject(R.id.liveRoom_huangjin)
    TextView huangjin_tv;
    @ViewInject(R.id.liveRoom_tonglvge)
    TextView tonglvnue_tv;
    @ViewInject(R.id.liveRoom_shiyou)
    TextView shiyou_tv;
    @ViewInject(R.id.liveRoom_index)
    ImageView index;


    protected String replyType; // 回复的类型 // 2--回复或者交流 1 提问
    protected String replyToId; // 回复的id  如果是交流传0,回复传回复的id,提问的时候不传

    DirectPlayAdapter adapter;
    List<DirectPlay> mList;
    private int indexWidth;
    private int currentIndex;
    TypeToken<LiveRoom<DirectPlay>> firstTypeToken = new TypeToken<LiveRoom<DirectPlay>>() {
    };
    TypeToken<AllData<DirectPlay>> moreTypeToken = new TypeToken<AllData<DirectPlay>>() {
    };


    public DirectPlayFragment() {
    }

    public static DirectPlayFragment newInstance() {
        DirectPlayFragment fragment = new DirectPlayFragment();
        return fragment;
    }
    private  void initLineWidth() {
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		indexWidth = dm.widthPixels / 5;
		index.setLayoutParams(new LinearLayout.LayoutParams(
				indexWidth, LinearLayout.LayoutParams.WRAP_CONTENT));
	}
    private void setTabText( int flag) {
    	all_tv.setTextColor(flag == 0 ? 0xFFF34F4F : 0xFF333333);
    	baiyin_tv.setTextColor(flag == 1 ? 0xFFF34F4F : 0xFF333333);
    	huangjin_tv.setTextColor(flag == 2 ? 0xFFF34F4F : 0xFF333333);
    	tonglvnue_tv.setTextColor(flag == 3 ? 0xFFF34F4F : 0xFF333333);
    	shiyou_tv.setTextColor(flag == 4 ? 0xFFF34F4F : 0xFF333333);
		Animation animation  = new TranslateAnimation(indexWidth*currentIndex, indexWidth*flag, 0,0);;
		animation.setFillAfter(true);
		animation.setDuration(300);
		index.startAnimation(animation);
		currentIndex=flag;
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.direct_play_list_back, container, false);
        ViewUtils.inject(this, view);
        initLineWidth();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        detailActivity = (ServiceDetailActivity) getActivity();
        initialize();
        setOnClickListener();
    }

    @Override
    public void initialize() {
        super.initialize();
        replyType = "1";
        replyToId = "";
        InputUtil.hideSoftInput(getActivity());
        emptyLayout.setEmptyButtonClickListener(this);
        emptyLayout.setErrorButtonClickListener(this);
        mList = new ArrayList<DirectPlay>();
        adapter = new DirectPlayAdapter(mContext, mList);
        mListView.setAdapter(adapter);

    }

    @SuppressWarnings("unchecked")
	private void setOnClickListener() {
        mListView.setOnRefreshListener(new ExpandListView.OnRefreshListener() {

            @Override
            public void onScrollListener(int firstitem, int visibleitem, int totalitem) {
            }

            @Override
            public void onItemClickListener(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onFootRefresh() {
                if (!threadStart) {
                    threadStart = true;
                    presenter.getDirectPlayMoreNewList(moreTypeToken, mList.get(mList.size() - 1).ID, action,currentIndex+"");
                } else {
                    setRefreshComplete();
                }
                isAutoRefresh = false;

            }
        }, true);
        swipeRereshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!threadStart) {
                    threadStart = true;
                    isAutoRefresh = false;
                    presenter.getDirectPlayFirstNewList(firstTypeToken, false, currentIndex+"", startId);
                } else {
                    setRefreshComplete();
                }

            }
        });
        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (detailActivity != null) {
                            detailActivity.setDirectPlayComInfo(replyType, replyToId);
                        }
                        break;
                }
                return false;
            }
        });
        // 加载数据
        presenter.getDirectPlayFirstNewList(new TypeToken<LiveRoom<DirectPlay>>(){}, true, "0", startId);
    }

    @Override
    public void setFistDataList(List<DirectPlay> list) {
        if (fl_price_content != null && fl_price_content.getVisibility() == View.VISIBLE) {
            fl_price_content.setVisibility(View.GONE);
        }
        
        startId = list.get(0).ID;
        presenter.setLiveid(startId);
        mList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setMoreDataList(List<DirectPlay> list) {
        mList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void startThreadGetList() {
        refreshHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Logger.i(ServiceDetailActivity.TAG, "isVisable===+" + isVisable);
                if (!threadStart && isVisable) {
                    Logger.i(ServiceDetailActivity.TAG, "线程开始获取数据===+");
                    threadStart = true;
                    isAutoRefresh = true;
                    presenter.getDirectPlayFirstNewList(firstTypeToken, false, currentIndex+"", startId);
                }
            }
        }, Constants.ROOM_INTERVAL);
    }


    @Override
    public void onResume() {
        super.onResume();
        isVisable = true;
        if (!threadStart) {
            Logger.i(ServiceDetailActivity.TAG, "onresume中获取数据");
            threadStart = true;
            presenter.getDirectPlayFirstNewList(firstTypeToken, false, "0", startId);
        }
        if (detailActivity != null) {
            detailActivity.setBottomViewIsVisble(View.VISIBLE);
            detailActivity.setReplyToId(replyToId);
            detailActivity.setReplyType(replyType);
            detailActivity.setEtCommentContentText("");
            detailActivity.setEtCommentContentHintText("顾问咨询...");
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    @OnClick({R.id.ll_view_error, R.id.btn_empty_data, R.id.liveRoom_all, R.id.liveRoom_baiyin, R.id.liveRoom_huangjin,
        R.id.liveRoom_tonglvge, R.id.liveRoom_shiyou})
public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_view_error: // 网络错误
                presenter.getDirectPlayFirstNewList(firstTypeToken, true, currentIndex+"", startId);
                break;
            case R.id.btn_empty_data: // 数据为null
                presenter.getDirectPlayFirstNewList(firstTypeToken, true, currentIndex+"", startId);
                break;
            case R.id.liveRoom_all: // 数据为null
            	setTabText(0);
                presenter.getDirectPlayFirstNewList(firstTypeToken, true, "0", startId);
                mList.clear();
                adapter.notifyDataSetChanged();
                break;
            case R.id.liveRoom_baiyin: // 数据为null
            	setTabText(1);
                presenter.getDirectPlayFirstNewList(firstTypeToken, true, "1", startId);
                mList.clear();
                adapter.notifyDataSetChanged();
                break;
            case R.id.liveRoom_huangjin: // 数据为null
            	setTabText(2);
                presenter.getDirectPlayFirstNewList(firstTypeToken, true, "2", startId);
                mList.clear();
                adapter.notifyDataSetChanged();
                break;
            case R.id.liveRoom_tonglvge: // 数据为null
            	setTabText(3);
                presenter.getDirectPlayFirstNewList(firstTypeToken, true, "3", startId);
                mList.clear();
                adapter.notifyDataSetChanged();
                break;
            case R.id.liveRoom_shiyou: // 数据为null
            	setTabText(4);
            	mList.clear();
            	adapter.notifyDataSetChanged();
                presenter.getDirectPlayFirstNewList(firstTypeToken, true, "4", startId);
                break;
                
        }
    }
}
