package com.jin91.preciousmetal.util;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.util.Logger;

/**
 * Created by lijinhua on 2015/5/22.
 * 消息通知
 */
@SuppressLint("NewApi")
public class MessageNotification {


    private NotificationManager mNotificationManager;
    private Context mContext;
    private static MessageNotification dNM;
    private int notifyId;
//    private SharedPreferences sp;
    private MessageNotification(Context context) {
        mContext = context;
       
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public static synchronized MessageNotification getInstance(Context context) {
        if (dNM == null) {
            dNM = new MessageNotification(context);
        }
        return dNM;
    }


    /**
     * 显示通知
     *
     * @param direction 这个主要是在即时建议时设置颜色，其他情况传""
     * @param title
     * @param text
     * @param time
     */
    public void showNofiy(String direction, String title, String text, String time,Intent intent) {
        Notification notification = null;
        SharedPreferences sp=mContext.getSharedPreferences("MsgSetting", Context.MODE_PRIVATE);
        notifyId++;
        if (Build.VERSION.SDK_INT >= 16) {
            notification = new Notification.Builder(mContext).setTicker(title).setSmallIcon(R.mipmap.notify).setWhen(System.currentTimeMillis()).build();
        } else {
            notification = new Notification(R.mipmap.notify, title, System.currentTimeMillis());
        }
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        // 0毫秒后开始振动，振动100毫秒后停止，再过200毫秒后再次振动300毫秒
        // long[] vibrate = {0,100,200,300};
        if (sp.getBoolean("zhendong", true)) {
        	long[] vibrate = {0, 500};
        	notification.vibrate = vibrate;
		}
//        String packageName = null;
//        try {
//            packageName = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).packageName;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//            Logger.i("notify", "发送通知==>设置声音异常" + e.toString());
//            notification.defaults = Notification.DEFAULT_ALL;
//        }
        if (sp.getBoolean("shengyin", true)) {
        	notification.sound = Uri.parse("android.resource://" + "com.jin91.preciousmetal" + "/raw/notification_ring");
		}
        RemoteViews contentView = new RemoteViews(mContext.getPackageName(), R.layout.notify);
        contentView.setTextViewText(R.id.tv_notify_title, title);
        contentView.setTextViewText(R.id.tv_notify_content, text);
        contentView.setTextViewText(R.id.tv_notify_time, DateUtil.converTime(time, "yyyy-MM-dd HH:mm:ss", "HH:mm:ss"));
        if (!TextUtils.isEmpty(direction)) {
            // 即时建议设置不同颜色
            if (direction.equals(mContext.getResources().getString(R.string.direction_more))//
                    || direction.equals(mContext.getResources().getString(R.string.direction_more_g))) {
                contentView.setTextColor(R.id.tv_notify_title, Color.RED);
            } else if (direction.equals(mContext.getResources().getString(R.string.direction_empty))//
                    || direction.equals(mContext.getResources().getString(R.string.direction_empty_g))) {
                contentView.setTextColor(R.id.tv_notify_title, Color.GREEN);
            }

        }
        notification.contentView = contentView;
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.contentIntent = pendingIntent;
        mNotificationManager.notify(notifyId,notification);
    }
}
