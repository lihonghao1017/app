package com.jin91.preciousmetal.ui.mine;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.api.entity.User;
import com.jin91.preciousmetal.common.api.entity.UserInfo;
import com.jin91.preciousmetal.customview.LoadingDialog;
import com.jin91.preciousmetal.ui.PreciousMetalAplication;
import com.jin91.preciousmetal.ui.base.BaseActivity;
import com.jin91.preciousmetal.util.BuildApiUtil;
import com.jin91.preciousmetal.util.InputUtil;
import com.jin91.preciousmetal.util.MessageToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by lijinhua on 2015/5/8.
 * 修改密码
 */
public class ModifyPassActivity extends BaseActivity implements AccountMangerView {

    public static final String TAG = "ModifyPassActivity";
    @ViewInject(R.id.et_old_pass)
    EditText et_old_pass;
    @ViewInject(R.id.et_new_pass)
    EditText et_new_pass;
    @ViewInject(R.id.et_new_repass)
    EditText et_new_repass;
    @ViewInject(R.id.tv_pass_msg)
    TextView tv_pass_msg;

    @ViewInject(R.id.ll_pass_code)
    LinearLayout ll_pass_code;
    @ViewInject(R.id.et_pass_code)
    EditText et_pass_code;
    @ViewInject(R.id.tv_get_pass_code)
    TextView tv_get_pass_code;
    @ViewInject(R.id.tv_title_back)
    public  TextView tv_title_back;
    @ViewInject(R.id.tv_title_option)
    public TextView tv_title_option;
    @ViewInject(R.id.tv_title_title)
    public TextView tv_title_title;

    LoadingDialog loadingDialog;
    AccountMangerPre presenter;
    User user;
    private int countDown = 60; // 倒计时
    private Timer againTimer; // 再一次获取验证码
    private CodeHandler handler;

    public static class CodeHandler extends Handler {
        WeakReference<ModifyPassActivity> reference;

        public CodeHandler(ModifyPassActivity fragment) {
            reference = new WeakReference<ModifyPassActivity>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case -1:
                    ModifyPassActivity activity = reference.get();
                    if (activity != null) {
                        activity.tv_get_pass_code.setText("重新获取验证码");
                        activity.tv_get_pass_code.setClickable(true);
                        activity.countDown = 60;
                        activity.againTimer.cancel();
                        activity.tv_get_pass_code.setBackgroundColor(activity.mContext.getResources().getColor(R.color.light_blue));
                        activity.tv_get_pass_code.setTextColor(activity.mContext.getResources().getColor(R.color.gray_blue));
                        break;
                    }
                default:
                    ModifyPassActivity fragment1 = reference.get();
                    if (fragment1 != null) {
                        fragment1.tv_get_pass_code.setText(msg.what + "秒后重发");
                    }
                    break;
            }
        }
    }

    public static void actionLaunch(Context context) {
        Intent intent = new Intent(context, ModifyPassActivity.class);
        ((Activity) context).startActivityForResult(intent, 100);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_modify_pass);
        ViewUtils.inject(this);
        initialize();
    }

    @OnClick({R.id.tv_title_back, R.id.tv_confirm_modify,R.id.tv_get_pass_code})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title_back:
                finish();
                break;
            case R.id.tv_confirm_modify:  // 修改密码 或者设置密码
                String pass = et_new_pass.getText().toString().trim();
                String repass = et_new_repass.getText().toString().trim();
                if (TextUtils.isEmpty(user.Upwd)) {
                    // 设置密码
                    String smsCode = et_pass_code.getText().toString().trim();
                    presenter.setPwd(TAG, pass, repass, user.GUID, smsCode);
                } else {
                    // 修改密码
                    String old_pass = et_old_pass.getText().toString().trim();
                    presenter.modifyPwd(TAG, old_pass, pass, repass, user.GUID);
                }
                break;
            case R.id.tv_get_pass_code: // 获取手机验证码
                presenter.getPassCode(TAG, user.GUID);
                break;


        }
    }

    @Override
    public String getRequestTag() {
        return TAG;
    }

    @Override
    public void initialize() {
        BuildApiUtil.setCursorDraw(et_pass_code);
        BuildApiUtil.setCursorDraw(et_old_pass);
        BuildApiUtil.setCursorDraw(et_new_pass);
        BuildApiUtil.setCursorDraw(et_new_repass);
        presenter = new AccountMangerPreImpl(this);
        user = PreciousMetalAplication.INSTANCE.user;
        if (TextUtils.isEmpty(user.Upwd)) {
            tv_title_title.setText("设置密码");
            et_old_pass.setVisibility(View.GONE);
        } else {
            tv_title_title.setText("修改密码");
            ll_pass_code.setVisibility(View.GONE);
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
        tv_pass_msg.setText(getString(R.string.net_error));
    }

    @Override
    public void hidLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void showErrorMsg(String errMsg) {
        tv_pass_msg.setText(errMsg);
    }

    @Override
    public void hideSoftInput() {
        InputUtil.hideSoftInput(mContext, et_new_pass.getWindowToken());
    }

    @Override
    public void setUserInfo(UserInfo userInfo) {

    }

    @Override
    public void handSuccess() {
        if (TextUtils.isEmpty(user.Upwd)) {
            MessageToast.showToast("设置密码成功", 1);
            setResult(RESULT_OK, new Intent());
        } else {
            MessageToast.showToast("修改密码成功", 1);
        }
        finish();
    }

    @Override
    public void sendSmsSuccess() {
        tv_pass_msg.setText("手机验证码发送成功");
        MessageToast.showToast("手机验证码发送成功", 1);
        if (handler == null) {
            handler = new CodeHandler(this);
        }
        tv_get_pass_code.setClickable(false);
        tv_get_pass_code.setBackgroundColor(getResources().getColor(R.color.count_down_bg));
        tv_get_pass_code.setTextColor(getResources().getColor(R.color.tv_count_down_color));

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

    @Override
    public void logoutSuccess() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        if (againTimer != null) {
            againTimer.cancel();
        }
    }
}
