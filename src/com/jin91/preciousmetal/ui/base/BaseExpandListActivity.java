package com.jin91.preciousmetal.ui.base;

import java.util.List;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.api.entity.AllData;
import com.jin91.preciousmetal.customview.EmptyLayout;
import com.jin91.preciousmetal.customview.ExpandListView;
import com.jin91.preciousmetal.ui.service.play.PlayView;
import com.jin91.preciousmetal.util.MessageToast;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by lijinhua on 2015/4/23.
 * 列表页activity
 */
public  abstract  class BaseExpandListActivity<T> extends BaseActivity implements PlayView<T>, View.OnClickListener {

    protected EmptyLayout emptyLayout;
    protected String startId = "0";

    /**
     * 初始化方法，在onCreate方法中调用
     */
    public void initialize() {
        emptyLayout = new EmptyLayout(mContext, (FrameLayout)findViewById(R.id.fl_price_content));
        emptyLayout.setEmptyButtonClickListener(this);
        emptyLayout.setErrorButtonClickListener(this);
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
        
        
        if ((FrameLayout)findViewById(R.id.fl_price_content) != null) {
        	((FrameLayout)findViewById(R.id.fl_price_content)).setVisibility(View.GONE);
        }
    }

    @Override
    public void showNetErrView() {
        emptyLayout.showError();
    }

    @Override
    public void showToastNetErr() {
        MessageToast.showToast(R.string.net_error, 0);
	    ((SwipeRefreshLayout)findViewById(R.id.swipeRereshLayout)).setRefreshing(false);
	    ((ExpandListView)findViewById(R.id.mListView)).setRefreshFootComplet();
    }

    @Override
    public void showToastNoMoreData() {
        MessageToast.showToast(R.string.no_more_data, 0);
        ((SwipeRefreshLayout)findViewById(R.id.swipeRereshLayout)).setRefreshing(false);
        ((ExpandListView)findViewById(R.id.mListView)).setRefreshFootComplet();
    }

    @Override
    public void showNoDataView(String noDataStr) {
        emptyLayout.showEmpty();
    }

    @Override
    public void onClick(View v) {

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
    public void setNewMsgCount(AllData<T> allData) {

    }

    @Override
    public void setFistDataList(List<T> list) {

    }

    @Override
    public void setMoreDataList(List<T> list) {

    }
}
