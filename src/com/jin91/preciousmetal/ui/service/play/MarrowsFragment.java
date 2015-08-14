package com.jin91.preciousmetal.ui.service.play;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.google.gson.reflect.TypeToken;
import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.adapter.MarrorAdapter;
import com.jin91.preciousmetal.common.api.entity.AllData;
import com.jin91.preciousmetal.common.api.entity.LiveRoom;
import com.jin91.preciousmetal.common.api.entity.Marrow;
import com.jin91.preciousmetal.common.util.Logger;
import com.jin91.preciousmetal.customview.ExpandListView;
import com.jin91.preciousmetal.ui.Constants;
import com.jin91.preciousmetal.ui.service.ServiceDetailActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by lijinhua on 2015/5/14.
 * 精华
 */
public class MarrowsFragment extends BasePlayFragment<Marrow> implements View.OnClickListener {

    public static final String type = "4";
    public static final String action = "getmarrows";
    MarrorAdapter adapter;
    @ViewInject(R.id.fl_price_content)
    FrameLayout fl_price_content;
    @ViewInject(R.id.id_stickynavlayout_innerscrollview)
    ExpandListView mListView;
    @ViewInject(R.id.swipeRereshLayout)
    SwipeRefreshLayout swipeRereshLayout;

    List<Marrow> mList;
    TypeToken<LiveRoom<Marrow>> firstTypeToken = new TypeToken<LiveRoom<Marrow>>() {
    };
    TypeToken<AllData<Marrow>> moreTypeToken = new TypeToken<AllData<Marrow>>() {
    };

    public MarrowsFragment() {
    }

    public static MarrowsFragment newInstance() {
        MarrowsFragment fragment = new MarrowsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_swipe_sticklist, container, false);
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
        emptyLayout.setEmptyButtonClickListener(this);
        emptyLayout.setErrorButtonClickListener(this);
        mList = new ArrayList<>();
        adapter = new MarrorAdapter(mContext, mList);
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
                if (!threadStart) {
                    threadStart = true;
                    isAutoRefresh = false;
                    presenter.getDirectPlayMoreList(moreTypeToken, mList.get(mList.size() - 1).ID, action);
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
                    presenter.getDirectPlayFirstList(firstTypeToken, false, type, startId);
                } else {
                    setRefreshComplete();
                }

            }
        });
        presenter.getDirectPlayFirstList(firstTypeToken, true, type, startId);
    }

    @Override
    public void setFistDataList(List<Marrow> list) {
        if (fl_price_content != null && fl_price_content.getVisibility() == View.VISIBLE) {
            fl_price_content.setVisibility(View.GONE);
        }
        startId = list.get(0).ID;
        presenter.setMarrowid(startId);
        mList.addAll(0, list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setMoreDataList(List<Marrow> list) {
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
            detailActivity.setBottomViewIsVisble(View.GONE);
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
        },Constants.ROOM_INTERVAL);
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
