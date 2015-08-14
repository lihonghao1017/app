package com.jin91.preciousmetal.ui.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.adapter.PriceAlarmAdapter;
import com.jin91.preciousmetal.common.api.entity.Alarm;
import com.jin91.preciousmetal.customview.EmptyLayout;
import com.jin91.preciousmetal.customview.LoadingDialog;
import com.jin91.preciousmetal.customview.swipemenulistview.SwipeMenu;
import com.jin91.preciousmetal.customview.swipemenulistview.SwipeMenuCreator;
import com.jin91.preciousmetal.customview.swipemenulistview.SwipeMenuItem;
import com.jin91.preciousmetal.customview.swipemenulistview.SwipeMenuListView;
import com.jin91.preciousmetal.ui.base.BaseFragment;
import com.jin91.preciousmetal.util.MessageToast;
import com.jin91.preciousmetal.util.ScreenUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by lijinhua on 2015/5/21.
 * 提醒
 */
public class AlarmFragment extends BaseFragment implements PriceAlarmView, View.OnClickListener {

    @ViewInject(R.id.fl_price_content)
    FrameLayout flPriceContent;
    @ViewInject(R.id.swipeMenuListView)
    SwipeMenuListView swipeMenuListView;

    private int alarmType;
    private List<Alarm> mList;
    private PriceAlarmAdapter adapter;
    private EmptyLayout emptyLayout;
    private PriceAlarmPresenter presenter;
    private LoadingDialog loadingDialog;

    public AlarmFragment() {
    }

    public static AlarmFragment getInstance(int alarmType) {
        AlarmFragment fragment = new AlarmFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("alarmType", alarmType);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.swipemenu_listview, container, false);
        ViewUtils.inject(this, view);
        alarmType = getArguments().getInt("alarmType");
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
    }

    @Override
    public void initialize() {
        emptyLayout = new EmptyLayout(mContext, flPriceContent);
        emptyLayout.setEmptyButtonClickListener(this);
        emptyLayout.setErrorButtonClickListener(this);
        presenter = new PriceAlarmPresenterImpl(this);
        mList = new ArrayList<>();
        adapter = new PriceAlarmAdapter(mContext, mList);
        swipeMenuListView.setAdapter(adapter);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(mContext.getApplicationContext());
                openItem.setWidth(ScreenUtil.dip2px(mContext, 90));
                openItem.setTitle("删除");
                openItem.setBackground(R.color.red);
                openItem.setTitleColor(getResources().getColor(R.color.white));
                openItem.setTitleSize(18);
                menu.addMenuItem(openItem);
            }
        };
        swipeMenuListView.setMenuCreator(creator);
        swipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        try {
                            presenter.deleteAlarm(PriceAlarmActivity.TAG, position, URLEncoder.encode(mList.get(position).K,"utf-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                return false;
            }
        });
        presenter.getAlarmList(PriceAlarmActivity.TAG,true, alarmType);
    }

    @Override
    public void setTitleNav() {

    }

    /**
     * 刷新数据
     */
    public void refreshData()
    {
        presenter.getAlarmList(PriceAlarmActivity.TAG,false, alarmType);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void setAlarmList(List<Alarm> list) {
        mList.clear();
        mList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading() {
        emptyLayout.showLoading();
    }

    @Override
    public void showProcessDialog() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(mContext, false);
        }
        loadingDialog.show();
    }

    @Override
    public void hideProcessDialog() {
        if (loadingDialog != null) {
            loadingDialog.hide();
        }
    }

    @Override
    public void delAlarmSuc(int position) {
        mList.remove(position);
        adapter.notifyDataSetChanged();
        if (mList.size() == 0) {
            showNoDataView();
        }
    }

    @Override
    public void showToastMessage(String message) {
        MessageToast.showToast(message, 0);
    }

    @Override
    public void hideLoading() {
        emptyLayout.hideLoadinAnim();
        flPriceContent.setVisibility(View.GONE);
    }

    @Override
    public void showNetErrView() {
        emptyLayout.showError();
    }

    @Override
    public void showNoDataView() {
        flPriceContent.setVisibility(View.VISIBLE);
        emptyLayout.showEmpty();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_view_error: // 网络错误
                presenter.getAlarmList(PriceAlarmActivity.TAG,true, alarmType);
                break;
            case R.id.btn_empty_data: // 数据为null
                presenter.getAlarmList(PriceAlarmActivity.TAG,true, alarmType);
                break;
        }
    }
}
