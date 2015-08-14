package com.jin91.preciousmetal.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jin91.preciousmetal.common.util.Logger;
import com.jin91.preciousmetal.services.MqttService;
import com.jin91.preciousmetal.util.ClientUtil;
import com.jin91.preciousmetal.util.UserHelper;

/**
 * Created by lijinhua on 2015/5/27.
 * 所有的广播接收者
 */
public class AllReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.i(MqttService.TAG, "AllReceiver");
//        String currentAction = intent.getAction();
//        // 解锁广播
//        if (currentAction.equals(Intent.ACTION_USER_PRESENT)) {
//            Logger.i(MqttService.TAG, "解锁广播");
//            // 当屏幕超时进行锁屏时,当用户按下电源按钮,长按或短按(不管有没跳出话框)，进行锁屏时,android系统都会广播此Action消息
//        } else if (currentAction.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
//            Logger.i(MqttService.TAG, "当屏幕超时进行锁屏时,当用户按下电源按钮,长按或短按(不管有没跳出话框)，进行锁屏时,android系统都会广播此Action消息");
//            // 去电监听
//        } else if (currentAction.equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
//            Logger.i(MqttService.TAG, "去电监听");
//            // //成功的安装APK之后 设备上新安装了一个应用程序包。 一个新应用包已经安装在设备上，数据包括包名（最新安装的包程序不能接收到这个广播）
//        } else if (currentAction.equals(Intent.ACTION_PACKAGE_ADDED)) {
//            Logger.i(MqttService.TAG, "成功的安装APK之后 设备上新安装了一个应用程序包。 一个新应用包已经安装在设备上，数据包括包名（最新安装的包程序不能接收到这个广播");
//            // 成功的删除某个APK之后发出的广播 一个已存在的应用程序包已经从设备上移除，包括包名（正在被安装的包程序不能接收到这个广播）
//        } else if (currentAction.equals(Intent.ACTION_PACKAGE_REMOVED)) {
//            Logger.i(MqttService.TAG, "成功的删除某个APK之后发出的广播 一个已存在的应用程序包已经从设备上移除，包括包名（正在被安装的包程序不能接收到这个广播）");
//            // 替换一个现有的安装包时发出的广播（不管现在安装的APP比之前的新还是旧，都会发出此广播？）
//        } else if (currentAction.equals(Intent.ACTION_PACKAGE_REPLACED)) {
//            Logger.i(MqttService.TAG, "替换一个现有的安装包时发出的广播（不管现在安装的APP比之前的新还是旧，都会发出此广播？）");
//            // 用户重新开始一个包，包的所有进程将被杀死，所有与其联系的运行时间状态应该被移除，包括包名（重新开始包程序不能接收到这个广播）
//        } else if (currentAction.equals(Intent.ACTION_PACKAGE_RESTARTED)) {
//            Logger.i(MqttService.TAG, "用户重新开始一个包，包的所有进程将被杀死，所有与其联系的运行时间状态应该被移除，包括包名（重新开始包程序不能接收到这个广播");
//            // /清除一个应用程序的数据时发出的广播(在设置－－应用管理－－选中某个应用，之后点清除数据时?) 用户已经清除一个包的数据，包括包名（清除包程序不能接收到这个广播）
//        } else if (currentAction.equals(Intent.ACTION_PACKAGE_DATA_CLEARED)) {
//            Logger.i(MqttService.TAG, "/清除一个应用程序的数据时发出的广播(在设置－－应用管理－－选中某个应用，之后点清除数据时?) 用户已经清除一个包的数据，包括包名（清除包程序不能接收到这个广播）");
//            // 触发一个下载并且完成安装时发出的广播，比如在电子市场里下载应用？
//        } else if (currentAction.equals(Intent.ACTION_PACKAGE_INSTALL)) {
//            Logger.i(MqttService.TAG, intent.getAction());
//            // 系统壁纸更换
//        } else if (currentAction.equals(Intent.ACTION_WALLPAPER_CHANGED)) {
//            Logger.i(MqttService.TAG, intent.getAction());
//            // 设备进入 USB 大容量存储模式
//        } else if (currentAction.equals(Intent.ACTION_UMS_CONNECTED)) {
//            Logger.i(MqttService.TAG, intent.getAction());
//            // 设备从 USB 大容量存储模式退出。
//        } else if (currentAction.equals(Intent.ACTION_UMS_DISCONNECTED)) {
//            Logger.i(MqttService.TAG, intent.getAction());
//            // 屏幕已经被打开 3.0以下有效
//        } else if (currentAction.equals(Intent.ACTION_SCREEN_ON)) {
//            Logger.i(MqttService.TAG, intent.getAction());
//            // 屏幕已经被关闭 3.0以下有效
//        } else if (currentAction.equals(Intent.ACTION_SCREEN_OFF)) {
//            Logger.i(MqttService.TAG, intent.getAction());
//            // 充电状态，或者电池的电量发生变化。
//        } else if (currentAction.equals(Intent.ACTION_BATTERY_CHANGED)) {
//            Logger.i(MqttService.TAG, intent.getAction());
//            // 表示电池电量低。
//        } else if (currentAction.equals(Intent.ACTION_BATTERY_LOW)) {
//            Logger.i(MqttService.TAG, intent.getAction());
//            // 表示电池电量充足，即从电池电量低变化到饱满时会发出广播
//        } else if (currentAction.equals(Intent.ACTION_BATTERY_OKAY)) {
//            Logger.i(MqttService.TAG, intent.getAction());
//            // 插上外部电源时发出的广播
//        } else if (currentAction.equals(Intent.ACTION_POWER_CONNECTED)) {
//            Logger.i(MqttService.TAG, intent.getAction());
//            // 已断开外部电源连接时发出的广播
//        } else if (currentAction.equals(Intent.ACTION_POWER_DISCONNECTED)) {
//            Logger.i(MqttService.TAG, intent.getAction());
//            // 设备的配置信息已经改变，参见 Resources.Configuration.
//        } else if (currentAction.equals(Intent.ACTION_CONFIGURATION_CHANGED)) {
//            Logger.i(MqttService.TAG, intent.getAction());
//            // 在耳机口上插入耳机时发出的广播
//        } else if (currentAction.equals(Intent.ACTION_HEADSET_PLUG)) {
//            Logger.i(MqttService.TAG, intent.getAction());
//            // 改变输入法时发出的广播
//        } else if (currentAction.equals(Intent.ACTION_INPUT_METHOD_CHANGED)) {
//            Logger.i(MqttService.TAG, intent.getAction());
//            // 移动APP完成之后，发出的广播(移动是指:APP2SD)
//        } else if (currentAction.equals(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE)) {
//            Logger.i(MqttService.TAG, intent.getAction());
//            // 正在移动APP时，发出的广播(移动是指:APP2SD)
//        } else if (currentAction.equals(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE)) {
//            Logger.i(MqttService.TAG, intent.getAction());
//            // 按下照相时的拍照按键(硬件按键)时发出的广播
//        } else if (currentAction.equals(Intent.ACTION_CAMERA_BUTTON)) {
//            Logger.i(MqttService.TAG, intent.getAction());
//            // 关闭或打开飞行模式时的广播
//        } else if (currentAction.equals(Intent.ACTION_AIRPLANE_MODE_CHANGED)) {
//            Logger.i(MqttService.TAG, intent.getAction());
//            // 屏日期被改变。
//        } else if (currentAction.equals(Intent.ACTION_DATE_CHANGED)) {
//            Logger.i(MqttService.TAG, intent.getAction());
//            // 用户按下了“Media Button
//        } else if (currentAction.equals(Intent.ACTION_MEDIA_BUTTON)) {
//            Logger.i(MqttService.TAG, intent.getAction());
//            // 扩展介质（扩展卡）已经从 SD 卡插槽拔出，但是挂载点 (mount point) 还没解除 (unmount)。
//        } else if (currentAction.equals(Intent.ACTION_MEDIA_BAD_REMOVAL)) {
//            Logger.i(MqttService.TAG, intent.getAction());
//            // 用户想要移除扩展介质（拔掉扩展卡）。
//        } else if (currentAction.equals(Intent.ACTION_MEDIA_EJECT)) {
//            Logger.i(MqttService.TAG, intent.getAction());
//            // 扩展介质被插入，而且已经被挂载。
//        } else if (currentAction.equals(Intent.ACTION_MEDIA_MOUNTED)) {
//            Logger.i(MqttService.TAG, intent.getAction());
//            // 扩展介质被移除。
//        } else if (currentAction.equals(Intent.ACTION_MEDIA_REMOVED)) {
//            Logger.i(MqttService.TAG, intent.getAction());
//            // 已经扫描完介质的一个目录。
//        } else if (currentAction.equals(Intent.ACTION_MEDIA_SCANNER_FINISHED)) {
//            Logger.i(MqttService.TAG, intent.getAction());
//            // 开始扫描介质的一个目录。
//        } else if (currentAction.equals(Intent.ACTION_MEDIA_SCANNER_STARTED)) {
//            Logger.i(MqttService.TAG, intent.getAction());
//            // 扩展介质的挂载被解除 (unmount)，因为它已经作为 USB 大容量存储被共享。
//        } else if (currentAction.equals(Intent.ACTION_MEDIA_SHARED)) {
//            Logger.i(MqttService.TAG, intent.getAction());
//            // 扩展介质存在，但是还没有被挂载 (mount)。
//        } else if (currentAction.equals(Intent.ACTION_MEDIA_UNMOUNTED)) {
//            Logger.i(MqttService.TAG, intent.getAction());
//            // 更新将要（真正）被安装。
//        } else if (currentAction.equals(Intent.ACTION_PROVIDER_CHANGED)) {
//            Logger.i(MqttService.TAG, intent.getAction());
//        }
        if (intent != null) {
            Logger.i(MqttService.TAG, intent.getAction());
        }
        checkMqttService(context);
    }

    /**
     * 检测mqtt服务
     *
     * @param context
     */
    public void checkMqttService(Context context) {
        if (UserHelper.getInstance().isLogin()) {
            if (!ClientUtil.serviceIsStart(MqttService.class.getName(), context))
            {
                MqttService.actionStart(context);
            }

        }
    }
}
