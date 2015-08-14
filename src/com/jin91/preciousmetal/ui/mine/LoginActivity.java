package com.jin91.preciousmetal.ui.mine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.ui.base.BaseActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by lijinhua on 2015/5/4.
 * 登录
 */
public class LoginActivity extends BaseActivity {

    public static final String TAG = "LoginActivity";
    @ViewInject(R.id.rg_mine_login)
    RadioGroup rg_mine_login;
    @ViewInject(R.id.viewPager)
    ViewPager viewPager;
    @ViewInject(R.id.iv_login_cursor)
    ImageView iv_login_cursor;
    @ViewInject(R.id.tv_title_back)
    public  TextView tv_title_back;
    @ViewInject(R.id.tv_title_option)
    public TextView tv_title_option;
    @ViewInject(R.id.tv_title_title)
    public TextView tv_title_title;
    int screenWidthThrid; // 屏幕长度的2/1
    int currentIndex; // 当前的索引
    List<Fragment> mList;

    /**
     * service中启动loginActivity
     * @param mContext
     */
    public static void actionLaunchService(Context mContext)
    {
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    public static void actionLaunch(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    /**
     * 这个主要是用于注册账号和忘记密码的时候跳转用到
     *
     * @param context
     * @param loginName
     */
    public static void actionLaunch(Context context, String loginName) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("loginName", loginName);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ViewUtils.inject(this);
        initialize();
    }

    @Override
    public void initialize() {
        mList = new ArrayList<>();
        mList.add(LoginFragment.newInstance());
        mList.add(PhoneLoginFragment.newInstance());
        viewPager.setAdapter(new LoginFragmentAdapter(getSupportFragmentManager()));
        tv_title_title.setText("登录");
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidthThrid = dm.widthPixels / 2;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) iv_login_cursor.getLayoutParams();
        layoutParams.width = screenWidthThrid;
        viewPagerListener();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            LoginFragment loginFragment = (LoginFragment) mList.get(0);
            loginFragment.setEt_username(intent.getStringExtra("loginName"));
        }
    }

    @OnClick({R.id.tv_title_back, R.id.rb_account_login, R.id.rb_account_phone})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title_back:
                finish();
                break;
            case R.id.rb_account_login:
                viewPager.setCurrentItem(0, false);
                break;
            case R.id.rb_account_phone:
                viewPager.setCurrentItem(1, false);
                break;


        }
    }

    public void viewPagerListener() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    rg_mine_login.check(R.id.rb_account_login);
                } else if (position == 1) {
                    rg_mine_login.check(R.id.rb_account_phone);
                }
                executorAnim(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public String getRequestTag() {
        return TAG;
    }


    private void executorAnim(final int pageSelectedIndex) {
        TranslateAnimation animation = new TranslateAnimation(screenWidthThrid * currentIndex, screenWidthThrid * pageSelectedIndex, 0, 0);// 显然这个比较简洁，只有一行代码。
        currentIndex = pageSelectedIndex;
        animation.setFillAfter(true);// True:图片停在动画结束位置
        animation.setDuration(100);
        iv_login_cursor.startAnimation(animation);

    }

    class LoginFragmentAdapter extends FragmentPagerAdapter {
        public LoginFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mList.get(position);
        }

        @Override
        public int getCount() {
            return mList.size();
        }
    }
}
