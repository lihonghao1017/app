package com.jin91.preciousmetal.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.jin91.preciousmetal.common.util.Logger;
import com.jin91.preciousmetal.services.MqttService;
import com.jin91.preciousmetal.util.ClientUtil;
import com.jin91.preciousmetal.util.UserHelper;

/**
 * Created by lijinhua on 2015/5/27.
 * 开机启动接收广播
 * 和网络变化
 */
public class BootNetReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        String currentAction = intent.getAction();
        if (currentAction.equals(Intent.ACTION_BOOT_COMPLETED)) {
            // 开机
            if (UserHelper.getInstance().isLogin()) {
                MqttService.actionStart(context);
            }
        } else {
            String netInfo = "";
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
            NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (activeNetInfo != null && activeNetInfo.isConnected() && activeNetInfo.isAvailable()) {
                netInfo = "有网络";
                checkMqttService(context);
                return;
            }
            if (mobNetInfo != null && mobNetInfo.isConnected() && mobNetInfo.isAvailable()) {
                netInfo = "移动网络";
                checkMqttService(context);
                return;
            }
            netInfo = "没有网络";
            Logger.i(MqttService.TAG, "BootNetReceiver---" + netInfo);
        }
    }

    /**
     * 检测mqtt服务
     *
     * @param context
     */
    public void checkMqttService(Context context) {
        if (UserHelper.getInstance().isLogin()) {
            if (!ClientUtil.serviceIsStart(MqttService.class.getName(), context)) {
                MqttService.actionStart(context);
            }

        }
    }
}
