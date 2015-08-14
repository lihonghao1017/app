package com.jin91.preciousmetal.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.Config;
import com.jin91.preciousmetal.util.BuildApiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by lijinhua on 2015/5/6.
 * 注册的第一步
 */
public class RegisterOneActivity extends BaseRegisterActivity {

    public static final String TAG = "RegisterOneActivity";
    @ViewInject(R.id.tv_reg_msg)
    TextView tv_reg_msg;
    @ViewInject(R.id.et_reg_phone)
    EditText et_reg_phone;
    @ViewInject(R.id.et_img_code)
    EditText et_img_code;
    @ViewInject(R.id.iv_imagecode)
    ImageView iv_image_code;
    @ViewInject(R.id.pb_imagecode)
    ProgressBar pb_imagecode;
    @ViewInject(R.id.tv_title_back)
    public  TextView tv_title_back;
    @ViewInject(R.id.tv_title_option)
    public TextView tv_title_option;
    @ViewInject(R.id.tv_title_title)
    public TextView tv_title_title;

    public String captchaKey; // 图片验证码的key
    public String uid;

    /**
     * @param context
     * @param type    0表示注册 1表示忘记密码
     */
    public static void actionLaunch(Context context, int type) {
        Intent intent = new Intent(context, RegisterOneActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_one);
        ViewUtils.inject(this);
        initialize();
    }


    @OnClick({R.id.tv_title_back, R.id.iv_imagecode, R.id.tv_reg_next})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title_back:
                finish();
                break;
            case R.id.iv_imagecode: // 刷新验证码
                pb_imagecode.setVisibility(View.VISIBLE);
                presenter.getCaptchaKey(TAG);
                break;
            case R.id.tv_reg_next: // 下一步
                String phone = et_reg_phone.getText().toString().trim();
                String imageCode = et_img_code.getText().toString().trim();
                if (type == 0) { // 注册
                    // 验证手机号是否存在
                    presenter.checkPhoneExits(TAG, phone, captchaKey, imageCode);
                } else { // 忘记密码
                    presenter.fwdSendPhoneCode(TAG, phone, imageCode, captchaKey);
                }
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

        BuildApiUtil.setCursorDraw(et_reg_phone);
        BuildApiUtil.setCursorDraw(et_img_code);
        presenter.getCaptchaKey(TAG);
        type = getIntent().getIntExtra("type", 0);
        if (type == 0) {
            tv_title_title.setText("注册");
        } else {
            tv_title_title.setText("忘记密码");
        }
    }


    @Override
    public void setCaptchaKey(String key) {
        captchaKey = key;
        loadImageCode();
    }

    public void loadImageCode() {
        String url = Config.ACCOUNT_HOST + "/ajax/verification/captcha?id=" + captchaKey + "&isrefresh=1";
        Glide.with(mContext).load(url).error(R.mipmap.image_code_error).into(new GlideDrawableImageViewTarget(iv_image_code) {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                super.onResourceReady(resource, animation);
                pb_imagecode.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void showErrorMsg(String errMsg) {
        super.showErrorMsg(errMsg);
        pb_imagecode.setVisibility(View.GONE);
    }

    @Override
    public void handleSuccess(String uid) {
        this.uid = uid;
        String phone = et_reg_phone.getText().toString().trim();
        String imageCode = et_img_code.getText().toString().trim();
        RegisterTwoActivity.actionLaunch(mContext, phone, captchaKey, imageCode, uid, type);
    }

    @Override
    public void checkPhoneSuccess() {
        String phone = et_reg_phone.getText().toString().trim();
        String imageCode = et_img_code.getText().toString().trim();
        presenter.regSendPhoneCode(TAG, phone, captchaKey, imageCode);
    }
}
