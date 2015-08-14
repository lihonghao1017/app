package com.jin91.preciousmetal.ui.service;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.customview.ExpandListView;
import com.jin91.preciousmetal.ui.base.BaseFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by lijinhua on 2015/5/4.
 */
@SuppressLint("NewApi")
public class TabFragment extends BaseFragment {

    @ViewInject(R.id.fl_price_content)
    FrameLayout fl_price_content;
    @ViewInject(R.id.id_stickynavlayout_innerscrollview)
    ExpandListView mListView;
    @ViewInject(R.id.swipeRereshLayout)
    SwipeRefreshLayout swipeRereshLayout;

    private List<String> textList;
    private MyAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.common_swipe_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewUtils.inject(this, view);
        initialize();
    }

    @Override
    public void initialize() {
        fl_price_content.setVisibility(View.GONE);
        textList = new ArrayList<String>();

        for (int i = 0; i < 30; i++) {
            textList.add("listviewitem " + i);
        }
        adapter = new MyAdapter();
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
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        SystemClock.sleep(2000);
                        textList.add("加载更多 的数据1");
                        textList.add("加载更多 的数据2");
                        textList.add("加载更多 的数据3");
                        textList.add("加载更多 的数据4");
                        textList.add("加载更多 的数据5");
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        adapter.notifyDataSetChanged();
                        mListView.setRefreshFootComplet();
                    }
                }.execute(new Void[]{});
            }
        }, true);
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return textList.size();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = new TextView(mContext);
            tv.setText(textList.get(position));
            return tv;
        }

    }

    @Override
    public void setTitleNav() {

    }
}
