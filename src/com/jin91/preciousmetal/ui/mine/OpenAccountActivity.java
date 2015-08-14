package com.jin91.preciousmetal.ui.mine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.adapter.OpenAccountAdapter;
import com.jin91.preciousmetal.common.Config;
import com.jin91.preciousmetal.ui.base.BaseActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by Administrator on 2015/5/1.
 * 我要开户
 */
public class OpenAccountActivity extends BaseActivity {


    @ViewInject(R.id.rg_open_account)
    RadioGroup rg_open_account;
    @ViewInject(R.id.iv_account_cursor)
    ImageView iv_account_cursor;
    @ViewInject(R.id.viewPager)
    ViewPager viewPager;
    @ViewInject(R.id.tv_title_back)
    public  TextView tv_title_back;
    @ViewInject(R.id.tv_title_option)
    public TextView tv_title_option;
    @ViewInject(R.id.tv_title_title)
    public TextView tv_title_title;

    int screenWidthThrid; // 屏幕长度的3/1
    int currentIndex; // 当前的索引
    List<OpenAccountPage> mList;

    public static void actionLaunch(Context context) {
        Intent intent = new Intent(context, OpenAccountActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_account);
        ViewUtils.inject(this);
        initialize();
    }

    @Override
    public String getRequestTag() {
        return "OpenAccountActivity";
    }

    @Override
    public void initialize() {

        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().startSync();
        CookieManager.getInstance().removeSessionCookie();

        mList = new ArrayList<OpenAccountPage>();
        mList.add(new OpenAccountPage(mContext, Config.ACCOUNT_NET_HOST));
        mList.add(new OpenAccountPage(mContext, Config.ACCOUNT_SUBSCRIBE_HOST));
        mList.add(new OpenAccountPage(mContext, Config.ACCOUNT_SIMULATE_HOST));
        OpenAccountAdapter adapter = new OpenAccountAdapter(mList);
        viewPager.setAdapter(adapter);
        tv_title_title.setText(getString(com.jin91.preciousmetal.R.string.my_open_account));
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidthThrid = dm.widthPixels / 3;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) iv_account_cursor.getLayoutParams();
        layoutParams.width = screenWidthThrid;
        viewPagerListener();

    }

    public void viewPagerListener() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    rg_open_account.check(R.id.rb_net_account);
                } else if (position == 1) {
                    rg_open_account.check(R.id.rb_subscribe_account);
                } else if (position == 2) {
                    rg_open_account.check(R.id.rb_simulate_account);
                }
                executorAnim(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.tv_title_back, R.id.rb_net_account, R.id.rb_subscribe_account, R.id.rb_simulate_account})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title_back:
                finish();
                break;
            case R.id.rb_net_account:
                viewPager.setCurrentItem(0, false);
                executorAnim(0);
                break;
            case R.id.rb_subscribe_account:
                viewPager.setCurrentItem(1, false);
                executorAnim(1);
                break;
            case R.id.rb_simulate_account:
                viewPager.setCurrentItem(2, false);
                executorAnim(2);
                break;
        }
    }


    private void executorAnim(final int pageSelectedIndex) {
        TranslateAnimation animation = new TranslateAnimation(screenWidthThrid * currentIndex, screenWidthThrid * pageSelectedIndex, 0, 0);// 显然这个比较简洁，只有一行代码。
        currentIndex = pageSelectedIndex;
        animation.setFillAfter(true);// True:图片停在动画结束位置
        animation.setDuration(100);
        iv_account_cursor.startAnimation(animation);
    }
}
