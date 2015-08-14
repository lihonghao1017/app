package com.jin91.preciousmetal.ui.mine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.customview.LoadingDialog;
import com.jin91.preciousmetal.services.MqttService;
import com.jin91.preciousmetal.ui.MainActivity;
import com.jin91.preciousmetal.ui.base.BaseFragment;
import com.jin91.preciousmetal.util.BuildApiUtil;
import com.jin91.preciousmetal.util.InputUtil;
import com.jin91.preciousmetal.util.MessageToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by lijinhua on 2015/5/4.
 * 账号登录
 */
@SuppressLint("NewApi")
public class LoginFragment extends BaseFragment implements LoginView {

    @ViewInject(R.id.et_username)
    EditText et_username;
    @ViewInject(R.id.et_password)
    EditText et_password;
    @ViewInject(R.id.tv_error_msg)
    TextView tv_error_msg;


    LoadingDialog loadingDialog;
    LoginPresenter presenter;

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mine_login_account, container, false);
        ViewUtils.inject(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
    }

    @Override
    public void onResume() {
        super.onResume();
        et_username.requestFocus();
        et_username.setFocusable(true);
        et_username.setFocusableInTouchMode(true);
    }

    @OnClick({R.id.tv_account_login, R.id.tv_free_regitster, R.id.tv_forget_pwd})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_account_login:
                String username = et_username.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                String drvieID = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
                presenter.accountLogin(LoginActivity.TAG, drvieID, username, password);
                break;
            case R.id.tv_free_regitster:
                RegisterOneActivity.actionLaunch(mContext, 0);
                break;
            case R.id.tv_forget_pwd:
                RegisterOneActivity.actionLaunch(mContext, 1);
                break;
        }
    }

    @Override
    public void initialize() {
        BuildApiUtil.setCursorDraw(et_username);
        BuildApiUtil.setCursorDraw(et_password);
        loadingDialog = new LoadingDialog(mContext, false);
        presenter = new LoginPresenterImpl(this);
        SharedPreferences preferences = mContext.getSharedPreferences("userName", Context.MODE_PRIVATE);
        et_username.setText(preferences.getString("userName", ""));
    }

    /**
     * 在LoginActivity里面设置登录的用户名
     *
     * @param loginName
     */
    public void setEt_username(String loginName) {
        if (et_username != null) {
            et_username.setText(loginName);
        }

    }

    @Override
    public void setTitleNav() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void showLoading() {
        loadingDialog.show();
    }

    @Override
    public void showNetError() {
        MessageToast.showToast(getString(R.string.net_error), 1);
        tv_error_msg.setText(getString(R.string.net_error));
    }

    @Override
    public void hidLoading() {
        loadingDialog.dismiss();
    }

    @Override
    public void showErrorMsg(String errMsg) {
        tv_error_msg.setText(errMsg);
    }

    @Override
    public void hideSoftInput() {
        InputUtil.hideSoftInput(mContext, et_username.getWindowToken());
    }

    @Override
    public void loginSuccess() {
        MessageToast.showToast("登录成功", 0);
        MqttService.actionStart(mContext);
        Intent sendIntent = new Intent(MainActivity.LOGIN_SUCCESS_MESSAGE);
        mContext.sendBroadcast(sendIntent);
        // 这里面为了兼容以前的使用单独的shareprefener
        SharedPreferences preferences = mContext.getSharedPreferences("userName", Context.MODE_PRIVATE);
        preferences.edit().putString("userName", et_username.getText().toString().trim()).apply();
        getActivity().finish();
    }

    @Override
    public void setUid(String uid) {

    }
}
