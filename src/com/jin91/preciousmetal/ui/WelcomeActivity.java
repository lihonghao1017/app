package com.jin91.preciousmetal.ui;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.api.UserApi;
import com.jin91.preciousmetal.common.api.base.ResultCallback;
import com.jin91.preciousmetal.common.api.entity.Update;
import com.jin91.preciousmetal.customview.CustomDialog;
import com.jin91.preciousmetal.services.DownLoadService;
import com.jin91.preciousmetal.ui.base.BaseActivity;
import com.jin91.preciousmetal.util.ClientUtil;
import com.jin91.preciousmetal.util.JsonUtil;
import com.jin91.preciousmetal.util.UserHelper;

/**
 * Created by lijinhua on 2015/5/22.
 * 欢迎页
 */
public class WelcomeActivity extends BaseActivity {

    private static final Long DELAY_TIME = 3000L;
    private long startTime;
    private long endTime;
    CustomDialog customDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        initialize();
    }

    @Override
    public String getRequestTag() {
        return "WelcomeActivity";
    }

    @Override
    public void initialize() {
        startTime = System.currentTimeMillis();
        String versionName = ClientUtil.getVersionName(mContext);
        if (!TextUtils.isEmpty(versionName)) {
            UserApi.updateVersion("WelcomeActivity", versionName, new ResultCallback() {
                @Override
                public void onEntitySuccess(String json) {
                    Update update = JsonUtil.parseUpdate(json);
                    UserHelper userHelper = UserHelper.getInstance();
                    if ("1".equals(update.hn)) {
                        //  有更新下载
                        userHelper.setUpdateJson(json);

                        if ("1".equals(update.ifForce)) {
                            // 强制升级
                            createDialog();
                            customDialog.setHideTvCancel();
                        } else {
                            // 不是强制升级
                            createDialog();
                        }
                        customDialog.setTvDialogTitle(getString(R.string.dialog_title));
                        customDialog.setTvDialogContent(getString(R.string.update));
                        customDialog.setTvOkText(getString(R.string.dialog_ok));
                        customDialog.show();
                    } else {
                        // 没有更新
                        userHelper.setVersion(ClientUtil.getVersionName(mContext));
                        startMainActivity();
                    }
                }

                @Override
                public void onException(VolleyError e) {
                    startMainActivity();
                }
            });
        } else {
            startMainActivity();
        }

    }

    private void startDownload() {
        // 没有在后台运行然后开户
        if (!ClientUtil.serviceIsStart(DownLoadService.class.getName(), mContext)) {
            DownLoadService.actionLaunch(mContext);
        }
    }

    private void createDialog()
    {
        customDialog = new CustomDialog(mContext, new CustomDialog.CustomDialogListener() {
            @Override
            public void customButtonListener(int postion) {
                if (postion == 1) {
                    startMainActivity();
                } else if (postion == 2) {
                    // 启动downloadservice下载
                    startDownload();
                    startMainActivity();
                }
            }
        }, false, false);
    }

    private void startMainActivity() {
        endTime = System.currentTimeMillis();
        long delayMillis = endTime - startTime;
        if (delayMillis < DELAY_TIME) {
            delayMillis = DELAY_TIME - delayMillis;
        } else {
            delayMillis = DELAY_TIME;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.actionLaunch(mContext,false);
                overridePendingTransition(0,0);
                finish();
            }
        }, delayMillis);
    }


}
