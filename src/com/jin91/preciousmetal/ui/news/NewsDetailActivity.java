package com.jin91.preciousmetal.ui.news;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.Config;
import com.jin91.preciousmetal.common.api.entity.Study;
import com.jin91.preciousmetal.customview.EmptyLayout;
import com.jin91.preciousmetal.ui.base.BaseActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by lijinhua on 2015/4/28.
 * 资讯的详情页
 */
@SuppressLint("NewApi")
public class NewsDetailActivity extends BaseActivity implements NewsDetailView, View.OnClickListener {
    public static final String TAG = "NewsDetailActivity";

    @ViewInject(R.id.tv_title_back)
    public  TextView tv_title_back;
    @ViewInject(R.id.tv_title_option)
    public TextView tv_title_option;
    @ViewInject(R.id.tv_title_title)
    public TextView tv_title_title;
    @ViewInject(R.id.tv_author)
    TextView tv_author;
    @ViewInject(R.id.tv_time)
    TextView tv_time;
    @ViewInject(R.id.tv_outline)
    TextView tv_outline;
    @ViewInject(R.id.tv_title)
    TextView tv_title;
    @ViewInject(R.id.webView)
    WebView webView;
    @ViewInject(R.id.fl_price_content)
    FrameLayout fl_price_content;

    EmptyLayout emptyLayout;
    private NewsDetailPresenter presenter;
    private String id;
    private Study study;

    private String share_url; // 分享的url
    private String share_type_title; // 分享类型的title

    /**
     * @param context
     * @param id
     * @param title
     */
    public static void actionLaunch(Context context, String id, String title, String content_title) {
        Intent i = new Intent(context, NewsDetailActivity.class);
        i.putExtra("id", id);
        i.putExtra("title", title);
        i.putExtra("content_title", content_title);
        context.startActivity(i);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail);
        ViewUtils.inject(this);
        initialize();
    }

    @Override
    public void initialize() {
        presenter = new NewsDetailPresenterImpl(this);
        emptyLayout = new EmptyLayout(mContext, fl_price_content);
        emptyLayout.setErrorButtonClickListener(this);
        WebSettings settings = webView.getSettings();
        settings.setLoadWithOverviewMode(true);
        webView.setVerticalScrollbarOverlay(true);
        webView.setHorizontalScrollbarOverlay(true);
        settings.setJavaScriptEnabled(true);
        // 设置可以支持缩放
//        settings.setSupportZoom(true);
        // 设置默认缩放方式尺寸是far
//        settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        // 设置出现缩放工具
//        settings.setBuiltInZoomControls(true);
        // 让网页自适应屏幕宽度
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        share_type_title = intent.getStringExtra("title");
        tv_title_title.setText(share_type_title);
        tv_title_option.setBackgroundResource(R.mipmap.title_share_pre);
        tv_title.setText(intent.getStringExtra("content_title"));
        // 加载详情页的数据
        presenter.getNewsDetail(TAG, id);
    }

    @OnClick({R.id.tv_title_back, R.id.tv_title_option})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title_back: // 返回
                finish();
                break;
            case R.id.tv_title_option: // 分享
                if (study != null) {
                    presenter.shareDialog(this, share_url, share_type_title, study.title);
                }
            case R.id.ll_view_error: // 网络错误
                presenter.getNewsDetail(TAG, id);
                break;
        }
    }

    @Override
    public String getRequestTag() {
        return TAG;
    }

    @Override
    public void setStudy(Study study) {
        this.study = study;
        tv_title_option.setBackgroundResource(R.drawable.title_share_selecter);
        tv_author.setText(study.source + "   " + study.author);
        tv_time.setText(study.adddate);
        tv_outline.setText(study.summary);
        String content = study.content.replace("src=\'", "src=\'" + Config.JIN91_HOST);
        webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
        share_url = Config.JIN91_G_HOST + "p-" + id + ".html";
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
}
