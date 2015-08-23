package com.jin91.preciousmetal.ui.service.play;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.adapter.RoomNoticeAdapter;
import com.jin91.preciousmetal.common.api.entity.RoomNotice;
import com.jin91.preciousmetal.customview.ExpandListView;
import com.jin91.preciousmetal.ui.base.BaseExpandListActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by lijinhua on 2015/5/15.
 * 房间里面的公告
 */
public class RoomNoticeActivity extends BaseExpandListActivity<RoomNotice> {


    public static final String TAG = "RoomNoticeActivity";
    List<RoomNotice> mList;
    RoomNoticeAdapter adapter;
    PlayPresenter presenter;
    private String roomId;
    @ViewInject(R.id.tv_title_back)
    public TextView tv_title_back;
    @ViewInject(R.id.tv_title_option)
    public TextView tv_title_option;
    @ViewInject(R.id.tv_title_title)
    public TextView tv_title_title;


    @ViewInject(R.id.fl_price_content)
    public FrameLayout flPriceContent;
    @ViewInject(R.id.mListView)
    public ExpandListView mListView;
    @ViewInject(R.id.swipeRereshLayout)
    public SwipeRefreshLayout swipeRereshLayout;

    public static void actionLaunch(Context context, String roomId) {
        Intent intent = new Intent(context, RoomNoticeActivity.class);
        intent.putExtra("roomId", roomId);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_title_list);
        ViewUtils.inject(this);
        initialize();
    }


    @OnClick({R.id.tv_title_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title_back:
                finish();
                break;
            case R.id.ll_view_error: // 网络错误
                presenter.getRoomNoticeList(TAG, true, startId, "0");
                break;
            case R.id.btn_empty_data: // 数据为null
                presenter.getRoomNoticeList(TAG, true, startId, "0");
                break;
        }
    }

    @Override
    public void initialize() {
        super.initialize();
        tv_title_title.setText("公告");
        roomId = getIntent().getStringExtra("roomId");
        mList = new ArrayList<>();
        adapter = new RoomNoticeAdapter(mContext, mList);
        mListView.setAdapter(adapter);
        presenter = new PlayPresenterImpl<>(this, roomId);
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
                startId = mList.get(mList.size() - 1).ID;
                presenter.getRoomNoticeList(TAG, false, startId, "1");
            }
        }, true);
        swipeRereshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startId = "0";
                presenter.getRoomNoticeList(TAG, false, startId, "0");
            }
        });
        presenter.getRoomNoticeList(TAG, true, startId, "0");
    }

    @Override
    public String getRequestTag() {
        return "RoomNoticeActivity";
    }

    @Override
    public void setDataList(List<RoomNotice> list) {
        if ("0".equals(startId)) {
            mList.clear();
            swipeRereshLayout.setRefreshing(false);
        } else {
            mListView.setRefreshFootComplet();
        }
        mList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void hideInputMethod() {

    }

    @Override
    public void setRefreshComplete() {

    }

    @Override
    public void setSuggestNote(String suggestNote) {

    }
}
