package com.jin91.preciousmetal.ui.service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.adapter.FinanceCalenAdapter;
import com.jin91.preciousmetal.adapter.FragmentsPagerAdapter;
import com.jin91.preciousmetal.common.Config;
import com.jin91.preciousmetal.common.api.entity.Finance;
import com.jin91.preciousmetal.common.api.entity.FinanceCalen;
import com.jin91.preciousmetal.common.api.entity.FinanceEvent;
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
public class FinanceCalenActivity extends BaseActivity implements FinanceCalenView, View.OnClickListener,OnPageChangeListener {

    public static final String TAG = "FinanceCalenActivity";
    @ViewInject(R.id.fl_price_content)
    FrameLayout fl_price_content;
    @ViewInject(R.id.swipeRereshLayout)
    SwipeRefreshLayout swipeRereshLayout;
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
    @ViewInject(R.id.FinanceCalenActivity_data_tv)
    private TextView data_tv;
    @ViewInject(R.id.FinanceCalenActivity_event_tv)
    private TextView event_tv;
    @ViewInject(R.id.FinanceCalenActivity_index)
    private View indexView;
    
    private FinanceDataFragment fdf;
    private FinanceEventFragment fef;

    public List<FinanceCalen> Finances;
    public List<FinanceEvent> Events;
    FinanceCalenPre financeCalenPre;
    EmptyLayout emptyLayout;
    LoadingDialog loadingDialog;
    private int indexWidth,currentIndex;

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
        initLineWidth();
        initialize();
    }
    public void initLineWidth() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		indexWidth = dm.widthPixels / 2;
		indexView.getLayoutParams().width=indexWidth;
		indexView.setLayoutParams(indexView.getLayoutParams());
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

    @SuppressWarnings("deprecation")
	@Override
    public void initialize() {
    	ArrayList<Fragment> fragments=new ArrayList<Fragment>();
    	fdf=new FinanceDataFragment();
    	fef=new FinanceEventFragment();
    	fragments.add(fdf);
    	fragments.add(fef);
    	FinanceCalenActivity_pager.setAdapter(new FragmentsPagerAdapter(getSupportFragmentManager(), fragments));
        financeCalenPre = new FinanceCalenPreImpl(this);
        Finances = new ArrayList<>();
        Events=new ArrayList<>();
        emptyLayout = new EmptyLayout(mContext, fl_price_content);
        emptyLayout.setErrorButtonClickListener(this);
        tv_title_title.setText(getString(R.string.finance_calcenda));
        tv_title_option.setBackgroundResource(R.drawable.btn_refresh_selecter);
        financeCalenPre.getFinCalList(TAG,"20150808");
        swipeRereshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                financeCalenPre.getFinCalList(TAG,"20150808");
            }
        });
        FinanceCalenActivity_pager.setOnPageChangeListener(this);
//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                FinanceCalen financeCalen = Finances.get(position);
//                financeCalenPre.shareDialog(FinanceCalenActivity.this, Config.JIN91_HOST, getString(R.string.finance_calcenda), financeCalen.Event);
//            }
//        });
    }

    @Override
    public void setItems(Finance list) {
//        if (list != null && list.size() > 0) {
//            tv_current_date.setText(list.get(0).Date);
//        }
        swipeRereshLayout.setRefreshing(false);
        Finances.clear();
        Finances.addAll(list.Finance);
        Events.clear();
        Events.addAll(list.Events);
        
        fdf.adapter.notifyDataSetChanged();
        fef.adapter.notifyDataSetChanged();
        
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

	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int arg0) {
		setTabText(arg0);
		currentIndex=arg0;
	}
	private void setTabText( int flag) {
		data_tv.setTextColor(flag == 0 ? 0xFFF34F4F : 0xFF737880);
		event_tv.setTextColor(flag == 1 ? 0xFFF34F4F : 0xFF737880);
		Animation animation  = new TranslateAnimation(indexWidth*currentIndex, indexWidth*flag, 0,0);;
		animation.setFillAfter(true);
		animation.setDuration(300);
		indexView.startAnimation(animation);
	}
}
