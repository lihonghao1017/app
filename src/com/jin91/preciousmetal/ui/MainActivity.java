package com.jin91.preciousmetal.ui;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bloomplus.tradev2.BloomplusTradeV2;
import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.adapter.MainAdapter;
import com.jin91.preciousmetal.common.api.entity.AllNotify;
import com.jin91.preciousmetal.common.api.entity.User;
import com.jin91.preciousmetal.common.util.Logger;
import com.jin91.preciousmetal.customview.CustomDialog;
import com.jin91.preciousmetal.customview.DrawableSubscriptButton;
import com.jin91.preciousmetal.customview.ViewPaggerNoScoll;
import com.jin91.preciousmetal.db.DBUserApi;
import com.jin91.preciousmetal.services.MqttService;
import com.jin91.preciousmetal.ui.base.BaseActivity;
import com.jin91.preciousmetal.ui.base.BaseFragment;
import com.jin91.preciousmetal.ui.mine.LoginPresenter;
import com.jin91.preciousmetal.ui.mine.LoginPresenterImpl;
import com.jin91.preciousmetal.ui.mine.LoginView;
import com.jin91.preciousmetal.ui.mine.MineFragment;
import com.jin91.preciousmetal.ui.mine.OpenAccountActivity;
import com.jin91.preciousmetal.ui.news.NewsFragment;
import com.jin91.preciousmetal.ui.price.PriceFragment;
import com.jin91.preciousmetal.ui.price.TradeFragment;
import com.jin91.preciousmetal.ui.service.ServiceFragment;
import com.jin91.preciousmetal.util.ClientUtil;
import com.jin91.preciousmetal.util.IntentUtil;
import com.jin91.preciousmetal.util.MessageToast;
import com.jin91.preciousmetal.util.UserHelper;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;


@SuppressLint("NewApi")
public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, LoginView {

    public static final String TAG = "MainActivity";
    public static final String REFRESH_MESSAGE = "refresh_message"; // 刷新消息
    public static final String LOGIN_SUCCESS_MESSAGE = "login_success_message";// 登录成功的广播
    public static final String LOGOUT_SUCCESS_MESSAGE = "logout_success_message";// 退出成功的广播

    @ViewInject(R.id.view_pagger)
    ViewPaggerNoScoll view_pagger;

    @ViewInject(R.id.rg_main_radio)
    RadioGroup rg_main_radio;
    @ViewInject(R.id.rb_price)
    DrawableSubscriptButton rbPrice;
    @ViewInject(R.id.rb_trade)
    DrawableSubscriptButton rbTrade;
    @ViewInject(R.id.rb_news)
    DrawableSubscriptButton rbNews;
    @ViewInject(R.id.rb_service)
    DrawableSubscriptButton rbService;
    @ViewInject(R.id.rb_personal)
    DrawableSubscriptButton rbPersonal;
    @ViewInject(R.id.tv_title_back)
    public  TextView tv_title_back;
    @ViewInject(R.id.tv_title_option)
    public TextView tv_title_option;
    @ViewInject(R.id.tv_title_title)
    public TextView tv_title_title;

    private List<BaseFragment> mList;
    CustomDialog customDialog;
    private UserHelper userHelper;
    RefreshMsgBroadcast refreshMsgBroadcast;
    private String currentVersion; // 当前版本的version
    private String preVersion;// 上一个版本的verion
    LoginPresenter presenter;
    private int preCheckId;
    private long exitTime = 0;

    public static void actionLaunch(Context mContext, boolean isExit) {
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("isExit", isExit);
        mContext.startActivity(intent);
        ((Activity) mContext).overridePendingTransition(0, 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getBooleanExtra("isExit", false)) {
                finish();
                return;
            }
        }
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);
        initialize();
        initView();
        refreshMsgBroadcast = new RefreshMsgBroadcast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(REFRESH_MESSAGE);
        intentFilter.addAction(LOGIN_SUCCESS_MESSAGE);
        intentFilter.addAction(LOGOUT_SUCCESS_MESSAGE);
//        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(refreshMsgBroadcast, intentFilter);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (view_pagger != null && view_pagger.getCurrentItem() == 1) {
            if (rg_main_radio != null) {
                rg_main_radio.check(preCheckId);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (refreshMsgBroadcast != null) {
            unregisterReceiver(refreshMsgBroadcast);
        }
    }

    private void initView() {
        if (!TextUtils.isEmpty(preVersion) && !currentVersion.equals(preVersion)) {
            // 判断版本号
            rbPersonal.showSubscript();
        } else if (userHelper.getReplyCount() > 0) {
            rbPersonal.showSubscript();
        } else if (userHelper.getGuoXinCount() > 0) {
            rbPersonal.showSubscript();
        } else if (userHelper.getIdeaMsgNumber() > 0) {
            rbPersonal.showSubscript();
        } else {
            rbPersonal.hideSubscript();
        }
    }


    @Override
    public String getRequestTag() {
        return TAG;
    }

    @Override
    public void initialize() {

        userHelper = UserHelper.getInstance();
        presenter = new LoginPresenterImpl(this);
        if (PreciousMetalAplication.getINSTANCE().user != null) {
            User user = PreciousMetalAplication.getINSTANCE().user;
            // 如果已经登录了,获取再获取播间然后重新订阅
            presenter.getVailRoom(TAG, user.ID);

        }
        preCheckId = R.id.rb_price;
        currentVersion = ClientUtil.getVersionName(mContext);
        preVersion = userHelper.getVersion();
        //-----------
        view_pagger.setOffscreenPageLimit(5);
        tv_title_back.setBackgroundColor(0);
        tv_title_back.setText(getString(R.string.open_account));
        tv_title_title.setText(getString(R.string.price));
        tv_title_option.setBackgroundResource(R.drawable.btn_callphone_selecter);
        tv_title_back.setOnClickListener(this);
        tv_title_option.setOnClickListener(this);
        //----------------
        mList = new ArrayList<BaseFragment>();
        mList.add(PriceFragment.newInstance());
        mList.add(TradeFragment.newInstance());
        mList.add(NewsFragment.newInstance());
        mList.add(ServiceFragment.newInstance());
        mList.add(MineFragment.newInstance());
        MainAdapter adapter = new MainAdapter(getSupportFragmentManager(), view_pagger, mList);
        view_pagger.setAdapter(adapter);
        rg_main_radio.setOnCheckedChangeListener(this);
        view_pagger.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setMainTitle(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        rbPersonal.setSubscriptDrawable(R.mipmap.new_msg_tip);
    }

    public void setMainTitle(int position) {
        switch (position) {
            case 0:
                tv_title_title.setText("行情");
                break;
            case 1:
                tv_title_title.setText("交易");
                break;
            case 2:
                tv_title_title.setText("资讯");
                break;
            case 3:
                tv_title_title.setText("服务");
                break;
            case 4:
                tv_title_title.setText("我的");
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_price:
                preCheckId = R.id.rb_price;
                view_pagger.setCurrentItem(0, false);
                break;
            case R.id.rb_trade:
                if (view_pagger.getCurrentItem() != 1) {
                    BloomplusTradeV2.startTrade(mContext);
                    view_pagger.setCurrentItem(1, false);
                }

                break;
            case R.id.rb_news:
                view_pagger.setCurrentItem(2, false);
                preCheckId = R.id.rb_news;
                break;
            case R.id.rb_service:
                view_pagger.setCurrentItem(3, false);
                preCheckId = R.id.rb_service;
                break;
            case R.id.rb_personal:
                view_pagger.setCurrentItem(4, false);
                preCheckId = R.id.rb_personal;
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title_back:
                OpenAccountActivity.actionLaunch(mContext);
                break;
            case R.id.tv_title_option:
                if (customDialog == null) {
                    customDialog = new CustomDialog(mContext, new CustomDialog.CustomDialogListener() {
                        @Override
                        public void customButtonListener(int postion) {
                            if (postion == 2) {
                                IntentUtil.callPhone(mContext, getString(R.string.official_tel));
                            }
                        }
                    }, false, true);
                }
                customDialog.setHideContent();
                customDialog.show();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                MessageToast.showToast("再按一次退出程序", 0);
                exitTime = System.currentTimeMillis();
            } else {
                actionLaunch(mContext, true);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showNetError() {

    }

    @Override
    public void hidLoading() {
        MqttService.actionStart(mContext);
    }

    @Override
    public void showErrorMsg(String errMsg) {

    }

    @Override
    public void hideSoftInput() {

    }

    @Override
    public void loginSuccess() {

    }

    @Override
    public void setUid(String uid) {

    }

    class RefreshMsgBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (REFRESH_MESSAGE.equals(intent.getAction())) {
                    // 刷新消息
                    AllNotify notify = (AllNotify) intent.getSerializableExtra("allNotify");

                    switch (notify.t) {
                        case 1: // 即时建议
                            userHelper.setIdeaMsgNumber(notify.currentCount);
                            isShowNewMsg();
                            break;
                        case 2: // 顾问回复
                            userHelper.setReplyCount(notify.currentCount);
                            isShowNewMsg();
                            break;
                        case 4: // 国鑫消息
                            userHelper.setGuoXinCount(notify.currentCount);
                            isShowNewMsg();
                            break;
                        case 11:// 显示有更新的
                            if (rbPersonal != null) {
                                rbPersonal.showSubscript();
                            }
                            if (mList != null && mList.size() > 4) {
                                MineFragment mineFragment = (MineFragment) mList.get(4);
                                mineFragment.showSetMsg();
                            }
                            break;

                    }
                } else if (LOGIN_SUCCESS_MESSAGE.equals(intent.getAction())) {
                    // 登录成功过后，刷新服务里面的绑定播音的状态
                    ServiceFragment serviceFragment = (ServiceFragment) mList.get(3);
                    serviceFragment.refreshRoomList();
                    // 更新我的里面的信息
                    MineFragment mineFragment = (MineFragment) mList.get(4);
                    mineFragment.initUserView();
                } else if (LOGOUT_SUCCESS_MESSAGE.equals(intent.getAction())) {
                    // 退出登录
                    UserHelper.getInstance().clearUserJson();
                    PreciousMetalAplication.getINSTANCE().user = null;
                    DBUserApi.deleteTokenFromCache(Constants.DB_STRING_KEY_USER);
                    Logger.i(TAG, DBUserApi.getUserFromCache() + "");
                    Logger.i(TAG, DBUserApi.getUserFromCache() + "sharefre--" + UserHelper.getInstance().fromShareUserInfo());
                    MqttService.actionStop(mContext, TAG);
                    // 登录成功过后，刷新服务里面的绑定播音的状态
                    ServiceFragment serviceFragment = (ServiceFragment) mList.get(3);
                    serviceFragment.refreshRoomList();
                    // 更新我的里面的信息
                    MineFragment mineFragment = (MineFragment) mList.get(4);
                    mineFragment.initUserView();
                }
//                else if(Intent.ACTION_TIME_TICK.equals(intent.getAction()))
//                {
//                    Logger.i(TAG,"boradcast----ACTION_TIME_TICK");
//                    if(UserHelper.getInstance())
//                }
            }


        }

        public void isShowNewMsg() {
            // 显示MainFragment里面的图标
            if (mList != null && mList.size() > 4) {
                MineFragment mineFragment = (MineFragment) mList.get(4);
                mineFragment.initNewsMsg();
                initView();
            }

        }
    }
}
