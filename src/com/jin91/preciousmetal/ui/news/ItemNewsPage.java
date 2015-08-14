package com.jin91.preciousmetal.ui.news;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.adapter.NewsAdapter;
import com.jin91.preciousmetal.common.api.entity.Study;
import com.jin91.preciousmetal.customview.EmptyLayout;
import com.jin91.preciousmetal.customview.ExpandListView;
import com.jin91.preciousmetal.ui.MainActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by lijinhua on 2015/4/27.
 * 资讯的item页
 */
public class ItemNewsPage implements NewsView, View.OnClickListener {


    @ViewInject(R.id.mListView)
    ExpandListView mListView;
    @ViewInject(R.id.fl_price_content)
    FrameLayout fl_price_content;
    @ViewInject(R.id.swipeRereshLayout)
    SwipeRefreshLayout swipeRereshLayout;
    View view;
    protected boolean isLoadSuccess; // 是否加载成功
    EmptyLayout emptyLayout;
    protected Context mContext;
    protected NewsPresenter presenter;
    private int flag;
    private int pageNumber = 1;// 当前第几页,默认为第一页
    private boolean isShowLoading = true;
    private List<Study> mList;
    private NewsAdapter adapter;


    /**
     * @param mContext
     * @param flag     0 国鑫日评
     *                 1 分析研究
     *                 2 机构观点
     *                 3 资讯导读
     *                 4 市场动态
     */
    public ItemNewsPage(Context mContext, int flag) {
        this.flag = flag;
        presenter = new NewsPresenterImpl(this);
        this.mContext = mContext;
        view = LayoutInflater.from(mContext).inflate(R.layout.news_list, null);
        ViewUtils.inject(this, view);
        emptyLayout = new EmptyLayout(mContext, fl_price_content);
        emptyLayout.setErrorButtonClickListener(this);
        initView();
    }

    public void initView() {
        mList = new ArrayList<Study>();
        adapter = new NewsAdapter(mContext, mList);
        mListView.setAdapter(adapter);
        swipeRereshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNumber = 1;
                initData();

            }
        });
        mListView.setOnRefreshListener(new ExpandListView.OnRefreshListener() {
            @Override
            public void onFootRefresh() {
                pageNumber++;
                initData();
            }

            @Override
            public void onScrollListener(int firstitem, int visibleitem, int totalitem) {

            }

            @Override
            public void onItemClickListener(AdapterView<?> parent, View view, int position, long id) {
                String title = "国鑫日评";
                switch (flag) {
                    case 1:
                        title = "分析研究";
                        break;
                    case 2:
                        title = "机构观点";
                        break;
                    case 3:
                        title = "资讯导读";
                        break;
                    case 4:
                        title = "市场动态";
                        break;
                }
                NewsDetailActivity.actionLaunch(mContext, mList.get(position).id, title, mList.get(position).title);
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
        }, true);
    }

    public void initData() {
        presenter.getNewsList(MainActivity.TAG, isShowLoading, flag, pageNumber);
    }

    public View getContentView() {
        return view;
    }

    @Override
    public void setItems(List<Study> list) {
        if (pageNumber == 1) {
            mList.clear();
        }
        isLoadSuccess = true;
        isShowLoading = false;
        mList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading() {
        emptyLayout.showLoading();
    }

    @Override
    public void hideLoading() {
        fl_price_content.setVisibility(View.GONE);
    }

    @Override
    public void showNetErrView() {
        emptyLayout.showError();
    }

    @Override
    public void refreshComplete() {
        mListView.setRefreshFootComplet();
        swipeRereshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_view_error:
                presenter.getNewsList(MainActivity.TAG, isShowLoading, flag, pageNumber);
                break;
        }
    }
}
