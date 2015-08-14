package com.jin91.preciousmetal.ui.mine;

import java.util.List;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.FrameLayout;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.customview.EmptyLayout;
import com.jin91.preciousmetal.customview.ExpandListView;
import com.jin91.preciousmetal.ui.base.BaseActivity;
import com.jin91.preciousmetal.util.MessageToast;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by lijinhua on 2015/5/12.
 */
public abstract class BaseMineMessageActivity<T> extends BaseActivity implements GuoXinMsgView<T> {

//    @ViewInject(R.id.fl_price_content)
//    FrameLayout fl_price_content;
//    @ViewInject(R.id.mListView)
//    ExpandListView mListView;
//    @ViewInject(R.id.swipeRereshLayout)
//    SwipeRefreshLayout swipeRereshLayout;

    protected EmptyLayout emptyLayout;
    protected GuoXinMsgPresenter presenter;
    protected String startId = "0"; // 开始的id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void initialize() {
        emptyLayout = new EmptyLayout(mContext, ((FrameLayout)findViewById(R.id.fl_price_content)));
        presenter = new GuoXinMsgPresenterImpl<T>(this);
    }

    @Override
    public void setItems(String suggestNoet, List<T> list) {

    }

    @Override
    public void showLoading() {
        emptyLayout.showLoading();
    }

    @Override
    public void hideLoading() {
        emptyLayout.hideLoadinAnim();
        ((FrameLayout)findViewById(R.id.fl_price_content)).setVisibility(View.GONE);
    }

    @Override
    public void showNetErrView() {
        emptyLayout.showError();
    }

    @Override
    public void showToastNetErr() {
    	
    	 
        MessageToast.showToast(R.string.net_error, 0);
        ((ExpandListView)findViewById(R.id.mListView)).setRefreshFootComplet();
        ((SwipeRefreshLayout)findViewById(R.id.swipeRereshLayout)).setRefreshing(false);
    }

    @Override
    public void showToastNoMoreData() {
        MessageToast.showToast(R.string.no_more_data, 0);
        ((ExpandListView)findViewById(R.id.mListView)).setRefreshFootComplet();
        ((SwipeRefreshLayout)findViewById(R.id.swipeRereshLayout)).setRefreshing(false);
    }

    @Override
    public void showNoDataView() {
        emptyLayout.showEmpty();
    }
}
