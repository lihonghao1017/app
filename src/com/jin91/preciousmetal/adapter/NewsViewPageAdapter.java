package com.jin91.preciousmetal.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.jin91.preciousmetal.ui.news.ItemNewsPage;

import java.util.List;

/**
 * Created by lijinhua on 2015/4/28.
 * 新闻的viewpage
 */
public class NewsViewPageAdapter extends PagerAdapter {

    private List<ItemNewsPage> mListPages;
    String[] newsArray;

    public NewsViewPageAdapter(List<ItemNewsPage> mListPages, String[] newsArray) {
        this.mListPages = mListPages;
        this.newsArray = newsArray;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mListPages.get(position).getContentView());
        return mListPages.get(position).getContentView();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mListPages.get(position).getContentView());
    }

    @Override
    public int getCount() {
        return mListPages.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return newsArray[position];
    }
}
