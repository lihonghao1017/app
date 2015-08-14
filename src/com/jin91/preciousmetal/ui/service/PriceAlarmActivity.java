package com.jin91.preciousmetal.ui.service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.customview.ViewPagerNoScroll;
import com.jin91.preciousmetal.ui.PreciousMetalAplication;
import com.jin91.preciousmetal.ui.base.BaseActivity;
import com.jin91.preciousmetal.ui.base.BaseFragment;
import com.jin91.preciousmetal.ui.mine.LoginActivity;
import com.jin91.preciousmetal.ui.price.PriceAlertActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by lijinhua on 2015/5/21.
 * 报价提醒
 */
public class PriceAlarmActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    public static final String TAG = "PriceAlarmActivity";
    @ViewInject(R.id.rg_alarm)
    RadioGroup rgAlarm;
    @ViewInject(R.id.viewpagerNoScroll)
    ViewPagerNoScroll viewpagerNoScroll;
    @ViewInject(R.id.iv_account_cursor)
    ImageView iv_account_cursor;
    @ViewInject(R.id.tv_title_back)
    public  TextView tv_title_back;
    @ViewInject(R.id.tv_title_option)
    public TextView tv_title_option;
    @ViewInject(R.id.tv_title_title)
    public TextView tv_title_title;

    private List<BaseFragment> fragments;
    int screenWidthTwo; // 屏幕长度的2/1
    int currentIndex; // 当前的索引

    public static void actionLuanch(Context mContext) {
        mContext.startActivity(new Intent(mContext, PriceAlarmActivity.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.price_alarm_list);
        ViewUtils.inject(this);
        initialize();
    }

    @OnClick({R.id.tv_title_back, R.id.tv_title_option})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title_back:
                finish();
                break;
            case R.id.tv_title_option:
                if (PreciousMetalAplication.getINSTANCE().user == null) {
                    LoginActivity.actionLaunch(mContext);
                    return;
                }
                PriceAlertActivity.actionLaunch(mContext, "", "", "", "", 1, 300);
                break;
        }
    }

    @Override
    public String getRequestTag() {
        return TAG;
    }

    @Override
    public void initialize() {
        tv_title_title.setText("报价提醒");
        tv_title_option.setBackgroundResource(R.drawable.btn_add_selecter);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidthTwo = dm.widthPixels / 2;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) iv_account_cursor.getLayoutParams();
        layoutParams.width = screenWidthTwo;
        rgAlarm.setOnCheckedChangeListener(this);
        fragments = new ArrayList<>();
        fragments.add(AlarmFragment.getInstance(1));
        fragments.add(AlarmFragment.getInstance(2));
        viewpagerNoScroll.setAdapter(new AlarmAdapter(getSupportFragmentManager()));
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_local_alarm:
                viewpagerNoScroll.setCurrentItem(0);
                executorAnim(0);
                break;
            case R.id.rb_sms_alarm:
                viewpagerNoScroll.setCurrentItem(1);
                executorAnim(1);
                break;
        }
    }

    class AlarmAdapter extends FragmentPagerAdapter {

        public AlarmAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    private void executorAnim(final int pageSelectedIndex) {
        TranslateAnimation animation = new TranslateAnimation(screenWidthTwo * currentIndex, screenWidthTwo * pageSelectedIndex, 0, 0);// 显然这个比较简洁，只有一行代码。
        currentIndex = pageSelectedIndex;
        animation.setFillAfter(true);// True:图片停在动画结束位置
        animation.setDuration(100);
        iv_account_cursor.startAnimation(animation);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 300 && resultCode == RESULT_OK) {
            // 刷新本地提醒和短信提醒
            if (fragments != null && fragments.size() > 0) {
                for (BaseFragment fragment : fragments) {
                    AlarmFragment alarmFragment = (AlarmFragment) fragment;
                    alarmFragment.refreshData();
                }
            }
        }
    }
}
