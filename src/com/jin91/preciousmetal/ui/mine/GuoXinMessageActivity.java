package com.jin91.preciousmetal.ui.mine;

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

import com.google.gson.reflect.TypeToken;
import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.adapter.GuoXinMessageAdapter;
import com.jin91.preciousmetal.common.api.entity.AllNotify;
import com.jin91.preciousmetal.common.api.entity.GuoXinMessage;
import com.jin91.preciousmetal.common.api.entity.MessageAll;
import com.jin91.preciousmetal.customview.ExpandListView;
import com.jin91.preciousmetal.ui.MainActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by Administrator on 2015/5/2.
 * 国鑫消息
 */
public class GuoXinMessageActivity extends BaseMineMessageActivity<GuoXinMessage> implements View.OnClickListener {

    public static final String TAG = "JinMessageActivity";
    @ViewInject(R.id.tv_title_back)
    public  TextView tv_title_back;
    @ViewInject(R.id.tv_title_option)
    public TextView tv_title_option;
    @ViewInject(R.id.tv_title_title)
    public TextView tv_title_title;
    @ViewInject(R.id.fl_price_content)
    FrameLayout fl_price_content;
    @ViewInject(R.id.mListView)
    ExpandListView mListView;
    @ViewInject(R.id.swipeRereshLayout)
    SwipeRefreshLayout swipeRereshLayout;
    private List<GuoXinMessage> mList;
    private GuoXinMessageAdapter adapter;
    TypeToken<MessageAll<GuoXinMessage>> type = new TypeToken<MessageAll<GuoXinMessage>>() {
    };

    public static void actionLaunch(Context context) {
        Intent intent = new Intent(context, GuoXinMessageActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_title_list);
        ViewUtils.inject(this);
        initialize();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ClearNewMsg();
    }

    /**
     * 清空小红点
     */
    private void ClearNewMsg()
    {
        AllNotify allNotify = new AllNotify();
        allNotify.t = 4;
        allNotify.currentCount = 0;
        Intent sendIntent = new Intent(MainActivity.REFRESH_MESSAGE);
        sendIntent.putExtra("allNotify",allNotify);
        sendBroadcast(sendIntent);
    }
    @Override
    public String getRequestTag() {
        return TAG;
    }

    @Override
    public void initialize() {
        super.initialize();
        tv_title_title.setText(getString(R.string.jin_message));
        ClearNewMsg();
        presenter.getGuoXinMsgList(type, true, TAG, "4", startId, "1");
        emptyLayout.setErrorButtonClickListener(this);
        mList = new ArrayList<GuoXinMessage>();
        adapter = new GuoXinMessageAdapter(mContext, mList);
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
                startId = mList.get(mList.size() - 1).ID;
                presenter.getGuoXinMsgList(type, false, TAG, "4", startId, "0");
            }
        }, true);
        swipeRereshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startId = "0";
                presenter.getGuoXinMsgList(type, false, TAG, "4", startId, "1");
            }
        });
    }


    @Override
    public void setItems(String suggestNoet, List<GuoXinMessage> list) {
        if ("0".equals(startId)) {
            mList.clear();
            swipeRereshLayout.setRefreshing(false);
        } else {
            mListView.setRefreshFootComplet();
        }
        mList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @OnClick({R.id.tv_title_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title_back:
                finish();
                break;
            case R.id.ll_view_error:
                presenter.getGuoXinMsgList(type, true, TAG, "4", startId, "1");
                break;
        }
    }

}
