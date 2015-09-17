package com.jin91.preciousmetal.ui.service.play;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.FrameLayout;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.api.entity.AllData;
import com.jin91.preciousmetal.common.util.Logger;
import com.jin91.preciousmetal.customview.EmptyLayout;
import com.jin91.preciousmetal.customview.ExpandListView;
import com.jin91.preciousmetal.ui.base.BaseFragment;
import com.jin91.preciousmetal.ui.service.ServiceDetailActivity;
import com.jin91.preciousmetal.util.MessageToast;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by lijinhua on 2015/5/12.
 */
public abstract class BasePlayFragment<T> extends BaseFragment implements PlayView<T> {

//    @ViewInject(R.id.fl_price_content)
//    FrameLayout fl_price_content;
//    @ViewInject(R.id.id_stickynavlayout_innerscrollview)
//    ExpandListView mListView;
//    @ViewInject(R.id.swipeRereshLayout)
//    SwipeRefreshLayout swipeRereshLayout;

	public EmptyLayout emptyLayout;
    public PlayPresenter presenter;
    public String startId = "0"; // 开始的id;
    public String roomId;
    public Handler refreshHandler = new Handler(); // 自动刷新的handler
    public boolean threadStart = true; // 线程是否开始
    public boolean isVisable; // 这个窗体是否可见
    public boolean isAutoRefresh; // 是否自动刷新
    public  ServiceDetailActivity detailActivity;
    public boolean isUseNempt;
    public boolean isUserShu;
    public BasePlayFragment() {
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            roomId = intent.getStringExtra("roomId");
        }
        detailActivity = (ServiceDetailActivity) getActivity();

    }

    @Override
    public void initialize() {
    	if(!isUseNempt){
    		emptyLayout = new EmptyLayout(getActivity(),  ((FrameLayout)getActivity().findViewById(R.id.fl_price_content)));
    	}
        presenter = new PlayPresenterImpl<T>(this, roomId);
    }

    @Override
    public void onPause() {
        super.onPause();
        isVisable = false;
        Logger.i(ServiceDetailActivity.TAG, "onPause");
    }

    @Override
    public void setTitleNav() {

    }

    @Override
    public void setDataList(List<T> list) {

    }

    @Override
    public void showLoading() {
        emptyLayout.showLoading();
    }

    @Override
    public void hideLoading() {
        emptyLayout.hideLoadinAnim();
        ((FrameLayout)getActivity().findViewById(R.id.fl_price_content)).setVisibility(View.GONE);
    }

    @Override
    public void showNetErrView() {
        emptyLayout.showError();
    }

    @Override
    public void showToastNetErr() {
        if (!isAutoRefresh) {
            MessageToast.showToast(R.string.net_error, 0);
        }

    }

    @Override
    public void showToastNoMoreData() {
        MessageToast.showToast(R.string.no_more_data, 0);
    }

    @Override
    public void showNoDataView(String noDataStr) {
        emptyLayout.showEmpty();
    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void showProcessDialog() {

    }

    @Override
    public void hideProcessDialog() {

    }

    @Override
    public void sendSuccess() {

    }

    @Override
    public void hideInputMethod() {

    }

    @Override
    public void setNewMsgCount(AllData<T> allData) {
        ServiceDetailActivity serviceDetailActivity = (ServiceDetailActivity) getActivity();
        if (serviceDetailActivity != null) {
            serviceDetailActivity.setNewMsgCount((AllData<Object>) allData);
        }
    }

    @Override
    public void setFistDataList(List<T> list) {

    }

    @Override
    public void setMoreDataList(List<T> list) {

    }

    @Override
    public void setRefreshComplete() {
    	
//    	    SwipeRefreshLayout swipeRereshLayout;
    	if(!isUserShu){
    		((ExpandListView)getActivity().findViewById(R.id.id_stickynavlayout_innerscrollview)).setRefreshFootComplet();
    	    ((SwipeRefreshLayout)getActivity().findViewById(R.id.swipeRereshLayout)).setRefreshing(false);
        threadStart = false;
        startThreadGetList();
    	}
    	    
        

    }

    /**
     * 启动线程获取数据
     */
    public abstract void startThreadGetList();

    @Override
    public void setSuggestNote(String suggestNote) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (refreshHandler != null) {
            refreshHandler.removeCallbacksAndMessages(null);
        }
    }
}
