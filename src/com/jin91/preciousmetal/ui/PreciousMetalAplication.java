package com.jin91.preciousmetal.ui;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.jin91.preciousmetal.common.CommonLib;
import com.jin91.preciousmetal.common.Config;
import com.jin91.preciousmetal.common.api.base.BaseApi;
import com.jin91.preciousmetal.common.api.entity.User;
import com.jin91.preciousmetal.util.ClientUtil;
import com.jin91.preciousmetal.util.UserHelper;

/**
 * Created by Administrator on 2015/4/25.
 */
public class PreciousMetalAplication extends Application {

    public final static String TAG = "PreciousMetalAplication";
    public static Context mContext;
    public static PreciousMetalAplication INSTANCE;

    public RequestQueue requestQueue;
    private UserHelper userHelper;
    public User user; //

    @Override
    public void onCreate() {
        super.onCreate();
        if (Config.DEBUG) {
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(getApplicationContext());
        }
        mContext = this;
        INSTANCE = this;
        initView();
    }

    public void initView() {
        CommonLib.init(mContext);
        Config.version = ClientUtil.getVersionName(mContext);
        requestQueue = BaseApi.getInstance().mVolleyRestClient.getRequestQueue();
        userHelper = UserHelper.getInstance();
        user = userHelper.fromShareUserInfo();
    }

    public static Context getContext() {
        return mContext;
    }

    public static PreciousMetalAplication getINSTANCE() {

        return INSTANCE;
    }

    /**
     * 判断用户是否登录
     *
     * @return
     */
    public boolean isLogin() {
        return userHelper.isLogin();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.with(mContext).onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
//        String memoryMsg = "";
//        switch (level) {
//            case TRIM_MEMORY_COMPLETE:
//                memoryMsg = "内存不足，并且该进程在后台进程列表最后一个，马上就要被清理";
//                break;
//            case TRIM_MEMORY_MODERATE:
//                memoryMsg = "内存不足，并且该进程在后台进程列表的中部";
//                break;
//            case TRIM_MEMORY_BACKGROUND:
//                memoryMsg = "内存不足，并且该进程是后台进程";
//                break;
//            case TRIM_MEMORY_UI_HIDDEN:
//                memoryMsg = "内存不足，并且该进程的UI已经不可见了";
//                break;
//            case TRIM_MEMORY_RUNNING_CRITICAL:
//                memoryMsg = "内存不足(后台进程不足3个)，并且该进程优先级比较高，需要清理内存";
//                break;
//            case TRIM_MEMORY_RUNNING_LOW:
//                memoryMsg = "内存不足(后台进程不足5个)，并且该进程优先级比较高，需要清理内存";
//                break;
//            case TRIM_MEMORY_RUNNING_MODERATE:
//                memoryMsg = "内存不足(后台进程超过5个)，并且该进程优先级比较高，需要清理内存";
//                break;
//            default:
//                break;
//        }
        Glide.with(mContext).onTrimMemory(level);
//        Logger.d(TAG, "memoryMsg:" + memoryMsg);
    }
}
