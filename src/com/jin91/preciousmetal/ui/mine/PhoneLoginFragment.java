package com.jin91.preciousmetal.ui.mine;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
 * Created by lijinhua on 2015/5/5.
 * 手机号验证登录
 */
@SuppressLint("NewApi")
public class PhoneLoginFragment extends BaseFragment implements LoginView {


    @ViewInject(R.id.et_phone)
    EditText et_phone;
    @ViewInject(R.id.et_val_code)
    EditText et_val_code;
    @ViewInject(R.id.tv_phone_msg)
    TextView tv_phone_msg;

    @ViewInject(R.id.tv_phone_code)
    TextView tv_phone_code;

    private int countDown = 60; // 倒计时
    private Timer againTimer; // 再一次获取验证码
    LoadingDialog loadingDialog;
    LoginPresenter presenter;
    String uid = "";
    private CodeHandler handler;

    public static class CodeHandler extends Handler {
        WeakReference<PhoneLoginFragment> reference;

        public CodeHandler(PhoneLoginFragment fragment) {
            reference = new WeakReference<PhoneLoginFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case -1:
                    PhoneLoginFragment fragment = reference.get();
                    if (fragment != null) {
                        fragment.tv_phone_code.setText("重新获取验证码");
                        fragment.tv_phone_code.setClickable(true);
                        fragment.countDown = 60;
                        fragment.againTimer.cancel();
                        fragment.tv_phone_code.setBackgroundColor(fragment.mContext.getResources().getColor(R.color.light_blue));
                        fragment.tv_phone_code.setTextColor(fragment.mContext.getResources().getColor(R.color.gray_blue));
                        break;
                    }
                default:
                    PhoneLoginFragment fragment1 = reference.get();
                    if (fragment1 != null) {
                        fragment1.tv_phone_code.setText(msg.what + "秒后重发");
                    }
                    break;
            }
        }
    }

    public static PhoneLoginFragment newInstance() {
        PhoneLoginFragment fragment = new PhoneLoginFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mine_phone_login, container, false);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
    }

    @OnClick({R.id.tv_phone_login, R.id.tv_phone_code, R.id.tv_forget_pwd, R.id.tv_free_regitster})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_phone_login: // 登录
                String phone = et_phone.getText().toString().trim();
                String smsCode = et_val_code.getText().toString().trim();
                String drvieID = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
                presenter.phoneLogin(LoginActivity.TAG, drvieID, phone, smsCode, uid);
                break;
            case R.id.tv_free_regitster: // 免费注册
                RegisterOneActivity.actionLaunch(mContext, 0);
                break;
            case R.id.tv_forget_pwd: // 忘记密码
                RegisterOneActivity.actionLaunch(mContext, 1);
                break;
            case R.id.tv_phone_code: // 获取验证码
                presenter.getPhoneCode(LoginActivity.TAG, et_phone.getText().toString().trim());
                break;
        }
    }

    @Override
    public void initialize() {
        presenter = new LoginPresenterImpl(this);
        BuildApiUtil.setCursorDraw(et_phone);
        BuildApiUtil.setCursorDraw(et_val_code);
    }

    @Override
    public void setTitleNav() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        if (againTimer != null) {
            againTimer.cancel();
        }
    }

    @Override
    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(mContext, false);
        }
        loadingDialog.show();
    }

    @Override
    public void showNetError() {
        MessageToast.showToast(getString(R.string.net_error), 1);
        tv_phone_msg.setText(getString(R.string.net_error));
    }

    @Override
    public void hidLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void showErrorMsg(String errMsg) {
        tv_phone_msg.setText(errMsg);
    }

    @Override
    public void hideSoftInput() {
        InputUtil.hideSoftInput(mContext, et_phone.getWindowToken());
    }

    @Override
    public void loginSuccess() {
        MessageToast.showToast("登录成功", 0);
        MqttService.actionStart(mContext);
        Intent sendIntent = new Intent(MainActivity.LOGIN_SUCCESS_MESSAGE);
        mContext.sendBroadcast(sendIntent);
        SharedPreferences preferences = mContext.getSharedPreferences("userName", Context.MODE_PRIVATE);
        preferences.edit().putString("userName", et_phone.getText().toString().trim()).apply();
        getActivity().finish();
    }

    @Override
    public void setUid(String uid) {
        this.uid = uid;
        MessageToast.showToast("验证码发送成功", 1);
        tv_phone_msg.setText("验证码发送成功");
        if (handler == null) {
            handler = new CodeHandler(this);
        }
        tv_phone_code.setClickable(false);
        tv_phone_code.setBackgroundColor(getResources().getColor(R.color.count_down_bg));
        tv_phone_code.setTextColor(getResources().getColor(R.color.tv_count_down_color));
        againTimer = new Timer();
        againTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                countDown--;
                if (countDown > 0) {
                    handler.sendEmptyMessage(countDown);
                } else {
                    handler.sendEmptyMessage(-1);
                }
            }
        }, 0, 1000);

    }
}
