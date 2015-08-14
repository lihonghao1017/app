package com.jin91.preciousmetal.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
public class RegisterThreeActivity extends BaseRegisterActivity {

    public static final String TAG = "RegisterThreeActivity";
    @ViewInject(R.id.et_reg_pwd)
    EditText et_reg_pwd;
    @ViewInject(R.id.et_reg_repwd)
    EditText et_reg_repwd;
    @ViewInject(R.id.tv_reg_comple)
    TextView tv_reg_comple;
    @ViewInject(R.id.tv_title_back)
    public  TextView tv_title_back;
    @ViewInject(R.id.tv_title_option)
    public TextView tv_title_option;
    @ViewInject(R.id.tv_title_title)
    public TextView tv_title_title;

    String phone;
    String smsCode;
    String uid;

    /**
     * @param context
     * @param phone   手机号
     * @param smsCode 短信的验证码
     * @param uid     发送验证码时返回的的uid
     * @param type    0表示注册 1表示忘记密码
     */
    public static void actionLaunch(Context context, String phone, String smsCode, String uid, int type) {
        Intent intent = new Intent(context, RegisterThreeActivity.class);
        intent.putExtra("phone", phone);
        intent.putExtra("smsCode", smsCode);
        intent.putExtra("uid", uid);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_three);
        ViewUtils.inject(this);
        initialize();
    }


    @Override
    public void initialize() {
        super.initialize();

        BuildApiUtil.setCursorDraw(et_reg_pwd);
        BuildApiUtil.setCursorDraw(et_reg_repwd);
        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        smsCode = intent.getStringExtra("smsCode");
        uid = intent.getStringExtra("uid");
        type = intent.getIntExtra("type", 0);
        if (type == 0) {
            tv_title_title.setText("设置密码");
        } else {
            tv_title_title.setText("重设密码");
            tv_reg_comple.setText("重设密码");
        }
    }

    @OnClick({R.id.tv_title_back, R.id.tv_reg_comple})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title_back:
                finish();
                break;

            case R.id.tv_reg_comple: // 注册完成
                String pass = et_reg_pwd.getText().toString().trim();
                String rePass = et_reg_repwd.getText().toString().trim();
                if (type == 0) {
                    presenter.regSetPwd(TAG, phone, smsCode, uid, pass, rePass);
                } else {
                    presenter.fwdSetPwd(TAG, phone, smsCode, "", uid, pass, rePass);
                }
                break;
        }
    }

    @Override
    public void setCaptchaKey(String key) {

    }

    @Override
    public void handleSuccess(String uid) {
        super.handleSuccess(uid);
        if (type == 0) {
            MessageToast.showToast("注册成功,请登录", 1);
        } else {
            MessageToast.showToast("重置密码成功,请登录", 1);
        }
        finish();
        LoginActivity.actionLaunch(mContext, phone);
    }

    @Override
    public String getRequestTag() {
        return TAG;
    }
}
