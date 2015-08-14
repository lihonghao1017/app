package com.jin91.preciousmetal.ui.news;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.adapter.NewsViewPageAdapter;
import com.jin91.preciousmetal.customview.pagerindicator.TabPageIndicator;
import com.jin91.preciousmetal.ui.MainActivity;
import com.jin91.preciousmetal.ui.base.BaseFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by lijinhua on 2015/4/27.
 * 资讯的fragment
 */
public class NewsFragment extends BaseFragment implements View.OnClickListener {


    @ViewInject(R.id.tab_pageindicator)
    TabPageIndicator tab_pageindicator;
    @ViewInject(R.id.view_pager)
    ViewPager view_pager;
    private NewsViewPageAdapter adapter;
    private List<ItemNewsPage> mList;

    public NewsFragment() {

    }

    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_fragment, container, false);
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
        mList = new ArrayList<ItemNewsPage>();
        mList.add(new ItemNewsPage(mContext, 0));
        mList.add(new ItemNewsPage(mContext, 1));
        mList.add(new ItemNewsPage(mContext, 2));
        mList.add(new ItemNewsPage(mContext, 3));
        mList.add(new ItemNewsPage(mContext, 4));
        mList.get(0).initData();
        adapter = new NewsViewPageAdapter(mList, mContext.getResources().getStringArray(R.array.news_type));
        view_pager.setAdapter(adapter);
        tab_pageindicator.setViewPager(view_pager);
        tab_pageindicator.setOnPageChangeListener(new IndicatorPageChangeListener());
        view_pager.setCurrentItem(0,false);
    }

    @Override
    public void setTitleNav() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.tv_title_title.setText(getString(R.string.news));
    }

    @Override
    public void onClick(View v) {

    }

    class IndicatorPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {



        }

        @Override
        public void onPageSelected(int position) {
            ItemNewsPage page = mList.get(position);
            if (!page.isLoadSuccess) {
                page.initData();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
