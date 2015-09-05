package com.jin91.preciousmetal.ui.service.play;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.google.gson.reflect.TypeToken;
import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.adapter.ExchangeAdapter;
import com.jin91.preciousmetal.common.api.entity.AllData;
import com.jin91.preciousmetal.common.api.entity.Exchange;
import com.jin91.preciousmetal.common.api.entity.LiveRoom;
import com.jin91.preciousmetal.common.util.Logger;
import com.jin91.preciousmetal.customview.ExpandListView;
import com.jin91.preciousmetal.ui.Constants;
import com.jin91.preciousmetal.ui.service.ServiceDetailActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by lijinhua on 2015/5/14.
 * 交流的fragment
 */
public class ExchangeFragment extends BasePlayFragment<Exchange> implements View.OnClickListener {

    public static final String type = "1";
    public static final String action = "getspeaksdata";
    @ViewInject(R.id.fl_price_content)
    FrameLayout fl_price_content;
    @ViewInject(R.id.id_stickynavlayout_innerscrollview)
    ExpandListView mListView;
    @ViewInject(R.id.swipeRereshLayout)
    SwipeRefreshLayout swipeRereshLayout;

    ExchangeAdapter adapter;
    List<Exchange> mList;
    TypeToken<LiveRoom<Exchange>> firstTypeToken = new TypeToken<LiveRoom<Exchange>>() {
    };
    TypeToken<AllData<Exchange>> moreTypeToken = new TypeToken<AllData<Exchange>>() {
    };
    protected String replyType; // 回复的类型 // 2--回复或者交流 1 提问
    protected String replyToId; // 回复的id  如果是交流传0,回复传回复的id,提问的时候不传

    public ExchangeFragment() {
    }

    public static ExchangeFragment newInstance() {
        ExchangeFragment fragment = new ExchangeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.jiaoliu_fragment_layout, container, false);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
    }

    @Override
    public void initialize() {
        super.initialize();
        replyType = "2";
        replyToId = "0";
        emptyLayout.setEmptyButtonClickListener(this);
        emptyLayout.setErrorButtonClickListener(this);
        mList = new ArrayList<>();
        adapter = new ExchangeAdapter(mContext, mList);
        mListView.setAdapter(adapter);

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
                isAutoRefresh = false;
                if (!threadStart) {
                    threadStart = true;
                    presenter.getDirectPlayMoreList(moreTypeToken, mList.get(mList.size() - 1).ID, action,type);
                } else {
                    setRefreshComplete();
                }


            }
        }, true);
        swipeRereshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!threadStart) {
                    threadStart = true;
                    isAutoRefresh = false;
                    presenter.getDirectPlayFirstList(firstTypeToken, true, type, startId);
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
                        // 判断软键盘弹起
                        replyToId = "0";
                        if (detailActivity != null) {
                            detailActivity.setExchangeComInfo(replyType,replyToId);
                        }
                        break;
                }

                return false;
            }
        });
        adapter.setCommReplyListener(new ExchangeAdapter.commReplyListener() {
            @Override
            public void comClick(int position) {
                Exchange exchange = mList.get(position);
                replyToId = exchange.ID;
                if (detailActivity != null) {
                    detailActivity.setEtCommentContentText("");
                    detailActivity.setEtCommentContentHintText("回复:" + exchange.UserName);
                    detailActivity.setReplyToId(replyToId);
                    detailActivity.setOpenKeyBoarder();
                }
            }
        });
        presenter.getDirectPlayFirstList(firstTypeToken, true, type, startId);
    }

    @Override
    public void setFistDataList(List<Exchange> list) {
        if (fl_price_content != null && fl_price_content.getVisibility() == View.VISIBLE) {
            fl_price_content.setVisibility(View.GONE);
        }
        startId = list.get(0).ID;
        presenter.setSpeakid(startId);
        mList.addAll(0, list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setMoreDataList(List<Exchange> list) {
        mList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        isVisable = true;
        if (!threadStart) {
            Logger.i(ServiceDetailActivity.TAG, "onresume中获取数据");
            threadStart = true;
            presenter.getDirectPlayFirstList(firstTypeToken, false, type, startId);
        }
        if (detailActivity != null) {
            detailActivity.setBottomViewIsVisble(View.VISIBLE);
            detailActivity.hideSoftInput();
            detailActivity.setReplyToId(replyToId);
            detailActivity.setReplyType(replyType);
            detailActivity.setEtCommentContentText("");
            detailActivity.setEtCommentContentHintText("鑫友交流...");
        }

    }

    @Override
    public void startThreadGetList() {
        refreshHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!threadStart && isVisable) {
                    threadStart = true;
                    isAutoRefresh = true;
                    presenter.getDirectPlayFirstList(firstTypeToken, false, type, startId);
                }
            }
        }, Constants.ROOM_INTERVAL);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_view_error: // 网络错误
                presenter.getDirectPlayFirstList(firstTypeToken, true, type, startId);
                break;
            case R.id.btn_empty_data: // 数据为null
                presenter.getDirectPlayFirstList(firstTypeToken, true, type, startId);
                break;
        }
    }
}
