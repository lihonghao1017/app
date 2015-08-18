package com.jin91.preciousmetal.ui.service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.adapter.FinanceCalenAdapter;
import com.jin91.preciousmetal.adapter.FragmentsPagerAdapter;
import com.jin91.preciousmetal.common.Config;
import com.jin91.preciousmetal.common.api.entity.FinanceCalen;
import com.jin91.preciousmetal.customview.EmptyLayout;
import com.jin91.preciousmetal.customview.LoadingDialog;
import com.jin91.preciousmetal.ui.base.BaseActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by lijinhua on 2015/5/4.
 * 财经日历
 */
public class FinanceCalenActivity extends BaseActivity implements FinanceCalenView, View.OnClickListener {

    public static final String TAG = "FinanceCalenActivity";
    @ViewInject(R.id.fl_price_content)
    FrameLayout fl_price_content;
    @ViewInject(R.id.swipeRereshLayout)
    SwipeRefreshLayout swipeRereshLayout;
    @ViewInject(R.id.mListView)
    ListView listview;
    @ViewInject(R.id.tv_current_date)
    TextView tv_current_date;
    @ViewInject(R.id.tv_title_back)
    public  TextView tv_title_back;
    @ViewInject(R.id.tv_title_option)
    public TextView tv_title_option;
    @ViewInject(R.id.tv_title_title)
    public TextView tv_title_title;
    @ViewInject(R.id.FinanceCalenActivity_pager)
    private ViewPager FinanceCalenActivity_pager;
    
    private FinanceDataFragment fdf;
    private FinanceEventFragment fef;

    FinanceCalenAdapter adapter;
    List<FinanceCalen> mList;
    FinanceCalenPre financeCalenPre;
    EmptyLayout emptyLayout;
    LoadingDialog loadingDialog;

    /**
     * @param context
     */
    public static void actionLaunch(Context context) {
        Intent i = new Intent(context, FinanceCalenActivity.class);
        context.startActivity(i);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finance_calenda_list);
        ViewUtils.inject(this);
        initialize();
    }

    @OnClick({R.id.tv_title_back, R.id.tv_title_option})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title_back:
                finish();
                break;
            case R.id.tv_title_option:
                if (loadingDialog == null) {
                    loadingDialog = new LoadingDialog(mContext, false);
                }
                loadingDialog.show();
                financeCalenPre.getFinCalList(TAG,"20150808");
                break;
            case R.id.ll_view_error:
                financeCalenPre.getFinCalList(TAG,"20150808");
                break;
        }
    }


    @Override
    public String getRequestTag() {
        return TAG;
    }

    @Override
    public void initialize() {
    	ArrayList<Fragment> fragments=new ArrayList<Fragment>();
    	fdf=new FinanceDataFragment();
    	fef=new FinanceEventFragment();
    	fragments.add(fdf);
    	fragments.add(fef);
    	FinanceCalenActivity_pager.setAdapter(new FragmentsPagerAdapter(getSupportFragmentManager(), fragments));
        financeCalenPre = new FinanceCalenPreImpl(this);
        mList = new ArrayList<>();
        adapter = new FinanceCalenAdapter(mList, mContext);
        listview.setAdapter(adapter);
        emptyLayout = new EmptyLayout(mContext, fl_price_content);
        emptyLayout.setErrorButtonClickListener(this);
        tv_title_title.setText(getString(R.string.finance_calcenda));
        listview.setDivider(null);
        tv_title_option.setBackgroundResource(R.drawable.btn_refresh_selecter);
        financeCalenPre.getFinCalList(TAG,"20150808");
        swipeRereshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                financeCalenPre.getFinCalList(TAG,"20150808");
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FinanceCalen financeCalen = mList.get(position);
                financeCalenPre.shareDialog(FinanceCalenActivity.this, Config.JIN91_HOST, getString(R.string.finance_calcenda), financeCalen.Event);
            }
        });
    }

    @Override
    public void setItems(List<FinanceCalen> list) {
        if (list != null && list.size() > 0) {
            tv_current_date.setText(list.get(0).Date);
        }
        swipeRereshLayout.setRefreshing(false);
        mList.clear();
        mList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading() {
        emptyLayout.showLoading();
    }

    @Override
    public void hideLoading() {
        emptyLayout.hideLoadinAnim();
        fl_price_content.setVisibility(View.GONE);
    }

    @Override
    public void showNetErrView() {

        emptyLayout.showError();
    }

    @Override
    public void hideProcessDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }
}
