package com.jin91.preciousmetal.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;

/**
 * Created by lijinhua on 2015/5/12.
 */
public class ClientUtil {

    public static final String TAG = ClientUtil.class.getSimpleName();

    /**
            * 设置Listview的高度
    */

    public static  void setListViewHeight(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {

            return;

        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {

            View listItem = listAdapter.getView(i, null, listView);

            listItem.measure(0, 0);

            totalHeight += listItem.getMeasuredHeight();

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        listView.setLayoutParams(params);

    }
    /**
     * get versionCode
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int versionCode = -1;
        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Error while get versionCode ", e.getCause());
        }
        return versionCode;
    }

    /**
     * get versionName
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * @Description:服务是否开启
     */
    public static boolean serviceIsStart(String className, Context context) {
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningServiceInfo> mServiceList = mActivityManager.getRunningServices(300);
        for (int i = 0; i < mServiceList.size(); i++) {
            if (className.equals(mServiceList.get(i).service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取手机的IMEI号
     * @param mContext
     * @return
     */
    public static String getIMEI(Context mContext)
    {
        TelephonyManager TelephonyMgr = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
        return TelephonyMgr.getDeviceId();
    }
}
