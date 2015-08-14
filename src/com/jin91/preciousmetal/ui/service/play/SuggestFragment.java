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
import com.jin91.preciousmetal.adapter.ImmdeSuggestAdapter;
import com.jin91.preciousmetal.common.api.entity.AllData;
import com.jin91.preciousmetal.common.api.entity.ImmedSuggest;
import com.jin91.preciousmetal.common.util.Logger;
import com.jin91.preciousmetal.customview.ExpandListView;
import com.jin91.preciousmetal.ui.Constants;
import com.jin91.preciousmetal.ui.service.ServiceDetailActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by lijinhua on 2015/5/14.
 * 即时建议
 */
public class SuggestFragment extends BasePlayFragment<ImmedSuggest> implements View.OnClickListener {

    //    public static final String type = "5";
	 @ViewInject(R.id.fl_price_content)
	    FrameLayout fl_price_content;
	    @ViewInject(R.id.id_stickynavlayout_innerscrollview)
	    ExpandListView mListView;
	    @ViewInject(R.id.swipeRereshLayout)
	    SwipeRefreshLayout swipeRereshLayout;

    public static final String action = "getcalls";
    ImmdeSuggestAdapter adapter;
    List<ImmedSuggest> mList;
    //    TypeToken<LiveRoom<ImmedSuggest>> firstTypeToken = new TypeToken<LiveRoom<ImmedSuggest>>() {
//    };
    TypeToken<AllData<ImmedSuggest>> moreTypeToken = new TypeToken<AllData<ImmedSuggest>>() {
    };

    public SuggestFragment() {
    }

    public static SuggestFragment newInstance() {
        SuggestFragment fragment = new SuggestFragment();
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
        adapter = new ImmdeSuggestAdapter(mContext, mList);
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
                    presenter.getSuggestMoreList(moreTypeToken, mList.get(mList.size() - 1).ID, action);
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
                    presenter.getSuggestFirstList(false, startId);
                } else {
                    setRefreshComplete();
                }

            }
        });
        presenter.getSuggestFirstList(true, startId);
    }

    @Override
    public void setFistDataList(List<ImmedSuggest> list) {
        if (fl_price_content != null && fl_price_content.getVisibility() == View.VISIBLE) {
            fl_price_content.setVisibility(View.GONE);
        }
        startId = list.get(0).ID;
        presenter.setCallid(startId);
        mList.addAll(0, list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setMoreDataList(List<ImmedSuggest> list) {
        mList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setSuggestNote(String suggestNote) {
        adapter.setSuggestNote(suggestNote);
    }

    @Override
    public void onResume() {
        super.onResume();
        isVisable = true;
        if (!threadStart) {
            Logger.i(ServiceDetailActivity.TAG, "onresume中获取数据");
            threadStart = true;
            presenter.getSuggestFirstList(false, startId);
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
                    presenter.getSuggestFirstList(false, startId);
                }
            }
        }, Constants.ROOM_INTERVAL);
    }

    @Override
    public void showNoDataView(String noDataStr) {
        threadStart = false;
        emptyLayout.showEmpty(noDataStr);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_view_error: // 网络错误
                presenter.getSuggestFirstList(true, startId);
                break;
            case R.id.btn_empty_data: // 数据为null
                presenter.getSuggestFirstList(true, startId);
                break;
        }
    }
}
