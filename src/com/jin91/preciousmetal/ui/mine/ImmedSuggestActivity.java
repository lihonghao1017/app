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
import com.jin91.preciousmetal.adapter.ImmdeSuggestAdapter;
import com.jin91.preciousmetal.common.api.entity.AllNotify;
import com.jin91.preciousmetal.common.api.entity.ImmedSuggest;
import com.jin91.preciousmetal.common.api.entity.MessageAll;
import com.jin91.preciousmetal.customview.ExpandListView;
import com.jin91.preciousmetal.ui.MainActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2015/5/9.
 * 即时建议
 */
public class ImmedSuggestActivity extends BaseMineMessageActivity<ImmedSuggest> implements View.OnClickListener {
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
    private List<ImmedSuggest> mList;
    private ImmdeSuggestAdapter adapter;
    TypeToken<MessageAll<ImmedSuggest>> type = new TypeToken<MessageAll<ImmedSuggest>>() {
    };

    public static void actionLaunch(Context context) {
        Intent intent = new Intent(context, ImmedSuggestActivity.class);
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
        ClearNewsMsg();
    }

    @Override
    public String getRequestTag() {
        return TAG;
    }

    /**
     * 清空小红点
     */
    private void ClearNewsMsg()
    {
        // 清空小红点
        AllNotify allNotify = new AllNotify();
        allNotify.t = 1;
        allNotify.currentCount = 0;
        Intent sendIntent = new Intent(MainActivity.REFRESH_MESSAGE);
        sendIntent.putExtra("allNotify",allNotify);
        sendBroadcast(sendIntent);
    }
    @Override
    public void initialize() {
        super.initialize();
        tv_title_title.setText(getString(R.string.immde_message));
        ClearNewsMsg();
        //
        tv_title_back.setOnClickListener(this);
        emptyLayout.setEmptyButtonClickListener(this);
        emptyLayout.setErrorButtonClickListener(this);
        mList = new ArrayList<ImmedSuggest>();
        adapter = new ImmdeSuggestAdapter(mContext, mList);
        mListView.setAdapter(adapter);
        // 加载数据
        presenter.getGuoXinMsgList(type, true, TAG, "1", startId, "1");
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
                presenter.getGuoXinMsgList(type, false, TAG, "1", startId, "0");
            }
        }, true);
        swipeRereshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startId = "0";
                presenter.getGuoXinMsgList(type, false, TAG, "1", startId, "1");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title_back:
                finish();
                break;
            case R.id.btn_empty_data:
                presenter.getGuoXinMsgList(type, true, TAG, "1", startId, "1");
                break;
            case R.id.ll_view_error:
                presenter.getGuoXinMsgList(type, true, TAG, "1", startId, "1");
                break;
        }
    }

    @Override
    public void setItems(String suggestNoet, List<ImmedSuggest> list) {
        if ("0".equals(startId)) {
            mList.clear();
            swipeRereshLayout.setRefreshing(false);
        } else {
            mListView.setRefreshFootComplet();
        }
        mList.addAll(list);
        adapter.setSuggestNote(suggestNoet);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void showNoDataView() {
        emptyLayout.showEmpty();
    }
}
