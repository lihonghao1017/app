package com.jin91.preciousmetal.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.api.entity.User;
import com.jin91.preciousmetal.common.api.entity.UserInfo;
import com.jin91.preciousmetal.common.util.Logger;
import com.jin91.preciousmetal.customview.CustomDialog;
import com.jin91.preciousmetal.customview.GlideCircleTransform;
import com.jin91.preciousmetal.customview.LoadingDialog;
import com.jin91.preciousmetal.ui.MainActivity;
import com.jin91.preciousmetal.ui.PreciousMetalAplication;
import com.jin91.preciousmetal.ui.base.BaseActivity;
import com.jin91.preciousmetal.util.MessageToast;
import com.jin91.preciousmetal.util.UserHelper;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by lijinhua on 2015/5/8.
 * 账户管理
 */
public class AccountMangerActivity extends BaseActivity implements AccountMangerView {

    public static final String TAG = "AccountMangerActivity";
    @ViewInject(R.id.tv_title_back)
    public  TextView tv_title_back;
    @ViewInject(R.id.tv_title_option)
    public TextView tv_title_option;
    @ViewInject(R.id.tv_title_title)
    public TextView tv_title_title;
    @ViewInject(R.id.tv_user_level)
    TextView tv_user_level;
    @ViewInject(R.id.tv_account_username)
    TextView tv_account_username;
    @ViewInject(R.id.tv_account_nickname)
    TextView tv_account_nickname;
    @ViewInject(R.id.tv_account_sex)
    TextView tv_account_sex;
    @ViewInject(R.id.tv_account_email)
    TextView tv_account_email;
    @ViewInject(R.id.tv_account_phone)
    TextView tv_account_phone;
    @ViewInject(R.id.tv_simulate_account1)
    TextView tv_simulate_account1;
    @ViewInject(R.id.tv_simulate_account2)
    TextView tv_simulate_account2;
    @ViewInject(R.id.tv_actual_account)
    TextView tv_actual_account;
    @ViewInject(R.id.iv_user_avtar)
    ImageView iv_user_avtar;
    @ViewInject(R.id.tv_modifyorset_pass)
    TextView tv_modifyorset_pass;

    LoadingDialog loadingDialog;
    AccountMangerPre presenter;
    CustomDialog customDialog;
    User user;

    public static void actionLaunch(Context context) {
        Intent intent = new Intent(context, AccountMangerActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_account_manager);
        ViewUtils.inject(this);
        initialize();
    }

    @com.lidroid.xutils.view.annotation.event.OnClick({R.id.tv_title_back, R.id.rl_modify_pass, R.id.rl_account_logout})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title_back:
                finish();
                break;
            case R.id.rl_modify_pass:  // 修改密码
                ModifyPassActivity.actionLaunch(mContext);
                break;
            case R.id.rl_account_logout: // 账户注销
                if (customDialog == null) {
                    customDialog = new CustomDialog(mContext, new CustomDialog.CustomDialogListener() {
                        @Override
                        public void customButtonListener(int postion) {
                            if (postion == 2) {
                                presenter.loginOut(TAG, user.ID);
                            }
                        }
                    }, false, true);
                    customDialog.setTvDialogTitle("注销");
                    customDialog.setTvDialogContent("您确定要注销吗?");
                    customDialog.setTvOkText("确定");
                }
                customDialog.show();
                break;


        }
    }

    @Override
    public void initialize() {
        tv_title_title.setText("账号管理");
        loadingDialog = new LoadingDialog(mContext, false);
        presenter = new AccountMangerPreImpl(this);
        user = PreciousMetalAplication.INSTANCE.user;
        presenter.getUserInfo(TAG, user.ID);
        Logger.i(TAG,"用户的密码---"+user.Upwd);
        if (TextUtils.isEmpty(user.Upwd)) {
            tv_modifyorset_pass.setText("设置密码");
        } else {
            tv_modifyorset_pass.setText("修改密码");
        }
    }

    @Override
    public String getRequestTag() {
        return TAG;
    }

    @Override
    public void showLoading() {
        loadingDialog.show();
    }

    @Override
    public void showNetError() {
        MessageToast.showToast(getString(R.string.net_error), 1);
    }

    @Override
    public void hidLoading() {
        loadingDialog.dismiss();
    }

    @Override
    public void showErrorMsg(String errMsg) {
        MessageToast.showToast(errMsg, 1);
    }

    @Override
    public void hideSoftInput() {

    }


    @Override
    public void setUserInfo(UserInfo userInfo) {
        tv_user_level.setText(userInfo.UserLevel);
        tv_account_username.setText(userInfo.RealName);
        tv_account_nickname.setText(userInfo.NickName);
        if ("0".equals(userInfo.Sex)) {
            tv_account_sex.setText("女");
        } else if ("1".equals(userInfo.Sex)) {
            tv_account_sex.setText("男");
        } else {
            tv_account_sex.setText("未知");
        }
        tv_account_email.setText(userInfo.Email);
        tv_account_phone.setText(userInfo.Mobile);
        tv_simulate_account1.setText(userInfo.ImitateAccountF);
        tv_simulate_account2.setText(userInfo.ImitateAccountS);
        tv_actual_account.setText(userInfo.RealAccount);
        Glide.with(mContext).load(userInfo.UserPicFull).transform(new GlideCircleTransform(mContext)).error(R.mipmap.default_error_avatar).placeholder(R.mipmap.default_normal_avatar).into(iv_user_avtar);
    }

    @Override
    public void handSuccess() {

    }

    @Override
    public void sendSmsSuccess() {

    }

    @Override
    public void logoutSuccess() {
        MessageToast.showToast("注销成功", 0);
        Intent sendIntent = new Intent(MainActivity.LOGOUT_SUCCESS_MESSAGE);
        mContext.sendBroadcast(sendIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == RESULT_OK) {
            // 更新sharepreence里面的用户的密码
            user.Upwd = "123"; // 这里面只要不为null就可以，这里密码用不着
            UserHelper.getInstance().SavaUserToShare(new Gson().toJson(user));
            tv_modifyorset_pass.setText("修改密码");

        }
    }
}
