package com.jin91.preciousmetal.ui.mine;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.util.BuildApiUtil;
import com.jin91.preciousmetal.util.MessageToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;


/**
 * Created by lijinhua on 2015/5/7.
 */
public class RegisterTwoActivity extends BaseRegisterActivity {

    public final static String TAG = "RegisterTwoActivity";
    @ViewInject(R.id.tv_reg_msg)
    TextView tv_reg_msg;
    @ViewInject(R.id.et_msg_code)
    EditText et_msg_code;
    @ViewInject(R.id.tv_again_code)
    TextView tv_again_code;
    @ViewInject(R.id.tv_title_back)
    public  TextView tv_title_back;
    @ViewInject(R.id.tv_title_option)
    public TextView tv_title_option;
    @ViewInject(R.id.tv_title_title)
    public TextView tv_title_title;


    private int countDown = 60; // 倒计时
    private Timer againTimer; // 再一次获取验证码
    private String phone;
    private String captchaKey;
    private String imgCode;
    private String uid;
    private CodeHandler handler;
    private int type; // 0表示注册 1表示忘记密码

    public static class CodeHandler extends Handler {
        WeakReference<RegisterTwoActivity> reference;

        public CodeHandler(RegisterTwoActivity activity) {
            reference = new WeakReference<RegisterTwoActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case -1:
                    RegisterTwoActivity activity = reference.get();
                    if (activity != null) {
                        activity.tv_again_code.setText("重新获取验证码");
                        activity.tv_again_code.setClickable(true);
                        activity.countDown = 60;
                        activity.againTimer.cancel();
                        activity.tv_again_code.setBackgroundColor(activity.getResources().getColor(R.color.light_blue));
                        activity.tv_again_code.setTextColor(activity.getResources().getColor(R.color.gray_blue));
                        break;
                    }
                default:
                    RegisterTwoActivity activity1 = reference.get();
                    if (activity1 != null) {
                        activity1.tv_again_code.setText(msg.what + "秒后重发");
                    }
                    break;
            }
        }
    }

    /**
     * @param context
     * @param phone      手机号
     * @param captchaKey 图片验证码的key
     * @param imgCode    图片的验证码
     * @param uid        发送验证码时返回的的uid
     * @param type       0表示注册 1表示忘记密码
     */
    public static void actionLaunch(Context context, String phone, String captchaKey, String imgCode, String uid,int type) {
        Intent intent = new Intent(context, RegisterTwoActivity.class);
        intent.putExtra("phone", phone);
        intent.putExtra("captchaKey", captchaKey);
        intent.putExtra("imgCode", imgCode);
        intent.putExtra("uid", uid);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_two);
        ViewUtils.inject(this);
        initialize();
    }

    @OnClick({R.id.tv_title_back, R.id.tv_again_code, R.id.tv_msg_val})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title_back:
                finish();
                break;
            case R.id.tv_again_code: // 再一次获取验证码
                presenter.regSendPhoneCode(TAG, phone, captchaKey, imgCode);
                break;
            case R.id.tv_msg_val: // 短信验证下一步
                String smCode = et_msg_code.getText().toString().trim();
                if (TextUtils.isEmpty(smCode)) {
                    tv_reg_msg.setText("请输入短信验证码");
                    return;
                } else {
                    tv_reg_msg.setText("");
                }
                if (handler != null) {
                    handler.sendEmptyMessage(-1);
                }
                if (againTimer != null) {
                    againTimer.cancel();
                }
                presenter.validateSms(TAG,smCode,phone,uid);
                break;
        }
    }

    @Override
    public String getRequestTag() {
        return TAG;
    }

    @Override
    public void initialize() {
        super.initialize();
        BuildApiUtil.setCursorDraw(et_msg_code);
        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        captchaKey = intent.getStringExtra("captchaKey");
        imgCode = intent.getStringExtra("imgCode");
        uid = intent.getStringExtra("uid");
        type = intent.getIntExtra("type",0);
        if (type == 0) {
            tv_title_title.setText("注册");
        } else {
            tv_title_title.setText("忘记密码");
        }

    }

    @Override
    public void smsValidateSuccess() {
        String smCode = et_msg_code.getText().toString().trim();
        RegisterThreeActivity.actionLaunch(mContext, phone, smCode, uid,type);
    }

    @Override
    public void setCaptchaKey(String key) {

    }

    @Override
    public void handleSuccess(String uid) {
        MessageToast.showToast("发送成功", 0);
        this.uid = uid;
        if (handler == null) {
            handler = new CodeHandler(this);
        }
        tv_again_code.setClickable(false);
        tv_again_code.setBackgroundColor(getResources().getColor(R.color.count_down_bg));
        tv_again_code.setTextColor(getResources().getColor(R.color.tv_count_down_color));
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
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        if (againTimer != null) {
            againTimer.cancel();
        }
    }
}
