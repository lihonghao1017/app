package com.jin91.preciousmetal.ui.mine;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.api.UserApi;
import com.jin91.preciousmetal.common.api.base.ResultCallback;
import com.jin91.preciousmetal.common.api.entity.User;
import com.jin91.preciousmetal.customview.LoadingDialog;
import com.jin91.preciousmetal.ui.PreciousMetalAplication;
import com.jin91.preciousmetal.ui.base.BaseActivity;
import com.jin91.preciousmetal.util.BuildApiUtil;
import com.jin91.preciousmetal.util.ClientUtil;
import com.jin91.preciousmetal.util.MessageToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by lijinhua on 2015/5/5.
 * 意见反馈
 */
public class FeedBackActivity extends BaseActivity {

    @ViewInject(R.id.et_feedback)
    EditText et_feedback;
    @ViewInject(R.id.tv_number_limt)
    TextView tv_number_limt;
    @ViewInject(R.id.tv_title_back)
    public  TextView tv_title_back;
    @ViewInject(R.id.tv_title_option)
    public TextView tv_title_option;
    @ViewInject(R.id.tv_title_title)
    public TextView tv_title_title;

    private LoadingDialog loadingDialog;
    public static final int MAX_LIMT = 500; // 最大的字符限制

    public static void actionLaunch(Context context) {
        Intent intent = new Intent(context, FeedBackActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_feedback);
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
                String feedback = et_feedback.getText().toString().trim();
                if (TextUtils.isEmpty(feedback)) {
                    MessageToast.showToast("给点意见吧", 0);
                    return;
                }
                if (feedback.length() > MAX_LIMT) {
                    MessageToast.showToast("最多输入500个字符", 0);
                    return;
                }
                // 发送反馈
                userFeedBack(feedback);
                break;
        }
    }

    @Override
    public String getRequestTag() {
        return "FeedBackActivity";
    }

    @Override
    public void initialize() {
        tv_title_title.setText("意见反馈");
        tv_title_option.setText("发送");
        BuildApiUtil.setCursorDraw(et_feedback);
        et_feedback.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    int inputLenght = s.toString().length();
                    if (inputLenght > 0 && inputLenght < MAX_LIMT) {
                        tv_title_option.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        tv_title_option.setTextColor(getResources().getColor(R.color.hint_color));
                    }
                    tv_number_limt.setText((MAX_LIMT - inputLenght) + "字");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void userFeedBack(String content) {
        // 这里get请求的参数不能太长
//        String systemInfo = "终端:android" + "|版本:" + ClientUtil.getVersionName(mContext) + new SystemInfoUtil().getSystemInfoDetail(mContext);
        String systemInfo = "终端:android" + "|版本:" + ClientUtil.getVersionName(mContext);// + new SystemInfoUtil().getSystemInfoLow(mContext);
        User user = PreciousMetalAplication.INSTANCE.user;
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(mContext, false);
        }
        loadingDialog.show();
        try {
            content = URLEncoder.encode(content,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        UserApi.userFeedBack("FeedBackActivity", content, systemInfo, user.ID, new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {
                loadingDialog.dismiss();
                finish();
                MessageToast.showToast("反馈成功", 0);
            }

            @Override
            public void onException(VolleyError e) {
                MessageToast.showToast(getString(R.string.net_error), 0);
                loadingDialog.dismiss();
            }
        });
    }
}
