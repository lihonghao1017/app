package com.jin91.preciousmetal.services;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.WindowManager;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.api.entity.AllNotify;
import com.jin91.preciousmetal.common.api.entity.User;
import com.jin91.preciousmetal.common.api.entity.ValidRoom;
import com.jin91.preciousmetal.common.database.api.IDBCallback;
import com.jin91.preciousmetal.common.util.Logger;
import com.jin91.preciousmetal.customview.CustomDialog;
import com.jin91.preciousmetal.db.DBStringApi;
import com.jin91.preciousmetal.db.DBUserApi;
import com.jin91.preciousmetal.ui.Constants;
import com.jin91.preciousmetal.ui.MainActivity;
import com.jin91.preciousmetal.ui.mine.CounselorReplyActivity;
import com.jin91.preciousmetal.ui.mine.GuoXinMessageActivity;
import com.jin91.preciousmetal.ui.mine.ImmedSuggestActivity;
import com.jin91.preciousmetal.ui.mine.LoginActivity;
import com.jin91.preciousmetal.ui.news.NewsDetailActivity;
import com.jin91.preciousmetal.ui.service.PriceAlarmActivity;
import com.jin91.preciousmetal.util.DateUtil;
import com.jin91.preciousmetal.util.JsonUtil;
import com.jin91.preciousmetal.util.MessageNotification;
import com.jin91.preciousmetal.util.UserHelper;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lijinhua on 2015/5/6.
 */
public class MqttService extends Service {

    public static final String TAG = "MqttService";
    public static final int type_msg_idea = 1;// 即时建议
    public static final int type_msg_reply = 2;// 顾问回复
    public static final int type_msg_all = 3;// 所有消息
    public static final int type_msg_guoxin = 4;// 国鑫消息
    public static final int type_msg_study = 5;// 国鑫研究
    public static final int type_msg_price = 6;// 行情(报价提醒)
    public static final int type_msg_login = 10;// 多个地方登录了

    private MqttClient mqttClient;
    private Context mContext;
    private static int[] MQTT_QUALITIES_OF_SERVICE;
    public static String MQTT_CLIENT_ID = "client";
    // These are the actions for the service (name are descriptive enough)
    public static final String ACTION_START = MQTT_CLIENT_ID + ".START";
    private static final String ACTION_STOP = MQTT_CLIENT_ID + ".STOP";
    PreciousCallBack callBack = new PreciousCallBack();
    ExecutorService singleThreadPool = Executors. newSingleThreadExecutor();
    /**
     * 启动mqttService,并使用闹钟检测心跳
     *
     * @param context
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MqttService.class);
        intent.setAction(ACTION_START);
        context.startService(intent);
    }

    /**
     * 停止
     *
     * @param context
     */
    public static void actionStop(Context context, String className) {
        Intent intent = new Intent(context, MqttService.class);
        intent.setAction(ACTION_STOP);
        Logger.i(TAG, "actionStop----" + className);
        context.startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Logger.i(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.i(TAG, "onStartCommand");
        if (intent == null) {
            Logger.i(TAG, "onStartCommand-----intent---null");
        }

        if (ACTION_START.equals(intent.getAction())) {

            if ((mqttClient == null) || (mqttClient != null && !mqttClient.isConnected())) {
                setStartForeground();
                Logger.i(TAG, "开始连接---startkeepallive");
                connect(); // 这里面包括监听网络变化和使用闹钟来检测是否连接
            }
        } else if (ACTION_STOP.equals(intent.getAction())) {
            Logger.i(TAG, "停止服务---stoptkeepallive");
            stopKeepAlives();
            stopSelf();
        }

        if (mqttClient != null) {
            Logger.i(TAG, "连接状态---" + mqttClient.isConnected());
        } else {
            Logger.i(TAG, "mqttClient---null");
        }

        return Service.START_NOT_STICKY;
    }

    private void setStartForeground() {
        Notification notification = new Notification(R.drawable.ic_launcher, getString(R.string.app_name), System.currentTimeMillis());
        Intent notificationIntent = new Intent(this, MqttService.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification.setLatestEventInfo(this, "pushService",
                "请保持程序在后台运行", pendingIntent);
        // 让该service前台运行，避免手机休眠时系统自动杀掉该服务
        // 如果 id 为 0 ，那么状态栏的 notification 将不会显示。
        startForeground(0, notification);
    }


    /**
     * 重新连接服务
     */
    private synchronized void connect() {
        singleThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String mqttConnSpec = "tcp://" + Constants.MQTT_HOST + ":" + Constants.MQTT_BROKER_PORT_NUM;
                    Logger.i(TAG, "从数据库里面获取用户信息");
                    User user = DBUserApi.getUserFromCache();
                    if (user == null) {
                        Logger.i(TAG, "用户的uid====nulllll");
                        return;
                    }

                    Logger.i(TAG, "用户的uid====" + user.ID);
                    String clientId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID) + DateUtil.getTodayToMillisecond("mmssSSS");
                    if (!TextUtils.isEmpty(clientId) && clientId.length() > 23) {
                        // 这里面有clientId为唯一标识,这里面的长度不能超过23
                        clientId = clientId.substring(0, 23);
                    }
                    mqttClient = new MqttClient(mqttConnSpec, clientId, new MemoryPersistence());
                    String username = Constants.MQTT_BROKER_USER_NAME;
                    String password = Constants.MQTT_BROKER_USER_PWD;
                    MqttConnectOptions conOpt = new MqttConnectOptions();
                    // 设置清除会话信息
                    conOpt.setCleanSession(true);
                    // 设置超时时间
                    conOpt.setConnectionTimeout(Constants.TIME_OUT / 1000);
                    // 设置会话心跳时间
                    conOpt.setKeepAliveInterval(200);
                    conOpt.setUserName(username);
                    conOpt.setPassword(password.toCharArray());
                    // last will message
//                    String message = "";
//                    String topic = "";
//                    Boolean retained = true;
                    //设置最终端口的通知消息
//                    conOpt.setWill(topic, message.getBytes(), MQTT_QUALITIES_OF_SERVICE[0], retained.booleanValue());
                    mqttClient.setCallback(callBack);
                    mqttClient.connect(conOpt);
                    subscribeToTopic(user);
                    Logger.i(TAG, "conn 连接成功");
                    startKeepAlives();
                    IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
                    registerReceiver(mConnectivityChanged, intentFilter);
                } catch (MqttException e) {
                    e.printStackTrace();
                    Logger.i(TAG, "conn 连接失败");
                }
            }
        });
    }


    /**
     * 订阅的主题
     *
     * @return
     */
    private void subscribeToTopic(final User user) {

        if (mqttClient == null || mqttClient.isConnected() == false) {
            Logger.i(TAG, "connnection subscribeToTopic 失败");
        } else {


            final String userid = user.ID;
            final String userRoleid = user.RoleId;
            DBStringApi.loadRoomListAsync(user.ID, new IDBCallback.SimpleDBCallback<List<ValidRoom>>() {
                @Override
                public void exception(Exception e) {

                }

                @Override
                public void canceled() {

                }

                @Override
                public void success(List<ValidRoom> callback) {
                    String[] topics = null; // 订阅的主题
                    int rooms = 0;
                    if (callback != null) {
                        rooms = callback.size();
                    }

                    topics = new String[rooms + 7];
                    for (int i = 0; i < rooms; i++) {
                        topics[i] = Constants.topic_calls + callback.get(i).RoomId;
                    }
                    topics[rooms] = Constants.topic_myquestions + userid;
                    topics[rooms + 1] = Constants.topic_gxmsgs;
                    topics[rooms + 2] = Constants.topic_gxmsgs_role + userRoleid;
                    topics[rooms + 3] = Constants.topic_gxmsgs_client + userid;
                    topics[rooms + 4] = Constants.topic_night_news;
                    topics[rooms + 5] = Constants.topic_push_price + userid;
                    topics[rooms + 6] = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                    MQTT_QUALITIES_OF_SERVICE = new int[topics.length];
                    for (int i = 0; i < topics.length; i++) {
                        MQTT_QUALITIES_OF_SERVICE[i] = 0;
                    }
                    StringBuilder sb = new StringBuilder();
                    for (String string : topics) {
                        sb.append(string).append(",");
                    }
                    Logger.i(TAG, "订阅主题是:======>" + sb.toString());
                    if (mqttClient != null && topics != null) {
                        try {
                            mqttClient.subscribe(topics, MQTT_QUALITIES_OF_SERVICE);
                        } catch (MqttException e) {
                            e.printStackTrace();
                            Logger.i(TAG, "订阅主题是:===失败===>" + e.toString());
                        }
                    }

                }
            });
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class PreciousCallBack implements MqttCallback {
        @Override
        public void connectionLost(Throwable throwable) {
            StringWriter stringWriter = new StringWriter();
            throwable.printStackTrace(new PrintWriter(stringWriter));
            Logger.i(TAG, "连接失败lost---" + stringWriter.toString());
            try {
                if (mqttClient != null) {
                    mqttClient.disconnect();
                    mqttClient = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                stopKeepAlives();
                connect();
            }
        }

        @Override
        public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
            final String string = new String(mqttMessage.getPayload());
            Logger.i(TAG, "收到推送消息-----" + string);
            showNotification(string);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
            Logger.i(TAG, "deliveryComplete");
        }
    }

    /**
     * 显示通知栏
     *
     * @param json
     */
    private void showNotification(String json) {
        final AllNotify allNotify = JsonUtil.parse(json, AllNotify.class);
        String id = "";
        String text = "";
        String time = "";
        String direction = "";
        String title = "";
        Intent intent;
        switch (allNotify.t) {
            case type_msg_idea:// 即时建议
                allNotify.currentCount++;
                title = getApplicationContext().getString(R.string.message_live);
                direction = allNotify.dr;
                if (!direction.equals(getString(R.string.direction_no))) {
                    title = direction;
                }
                time = allNotify.dt;
                text = allNotify.c;
                intent = new Intent(mContext, ImmedSuggestActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("notice", true);
                MessageNotification.getInstance(mContext).showNofiy(allNotify.dr, title, text, time, intent);
                sendNotifyBorast(allNotify);
                break;
            case type_msg_reply: // 顾问回复
                allNotify.currentCount++;
                title = getApplicationContext().getString(R.string.message_reply);
                time = allNotify.at;
                text = allNotify.a;
                intent = new Intent(mContext, CounselorReplyActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("notice", true);
                MessageNotification.getInstance(mContext).showNofiy(allNotify.dr, title, text, time, intent);
                sendNotifyBorast(allNotify);
                break;
            case type_msg_study: // 资讯
                title = getApplicationContext().getString(R.string.message_study);
                time = allNotify.d;
                text = allNotify.c;
                id = allNotify.i;
                intent = new Intent(mContext, NewsDetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("id", id);
                intent.putExtra("title", title);
                MessageNotification.getInstance(mContext).showNofiy(allNotify.dr, title, text, time, intent);
                sendNotifyBorast(allNotify);
                break;
            case type_msg_guoxin: // 国鑫消息
                allNotify.currentCount++;
                time = allNotify.dt;
                title = getApplicationContext().getString(R.string.message_guoxin);
                intent = new Intent(mContext, GuoXinMessageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                MessageNotification.getInstance(mContext).showNofiy(allNotify.dr, title, allNotify.c, time, intent);
                sendNotifyBorast(allNotify);
                break;
            case type_msg_price:// 报价提醒
                title = getApplicationContext().getString(R.string.message_price);
                text = allNotify.c;
                intent = new Intent(mContext, PriceAlarmActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                MessageNotification.getInstance(mContext).showNofiy(allNotify.dr, title, text, time, intent);
                sendNotifyBorast(allNotify);
                break;
            case type_msg_login: // 一个账号在其他的手机上登录
                ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
                final String topPackagename = rti.get(0).topActivity.getPackageName();
                Logger.i(TAG, "topPackageNmae===" + topPackagename);
                if (topPackagename.equals(mContext.getPackageName())) {
                    Logger.i(TAG, "在前台运行");
                    // 在前台运行
                    showDialog(allNotify.dt);
                } else {
                    // 没有在前台运行,在通知栏显示
                    intent = new Intent();
                    UserHelper.getInstance().clearUserJson();
                    DBUserApi.deleteTokenFromCache(Constants.DB_STRING_KEY_USER);
                    actionStop(mContext, TAG);
                    Intent sendIntent = new Intent(MainActivity.LOGOUT_SUCCESS_MESSAGE);
                    mContext.sendBroadcast(sendIntent);
                    MessageNotification.getInstance(mContext).showNofiy("", "下线通知", "您的账号在其他地方登录", allNotify.dt, intent);
                }
                break;
        }
    }

    /**
     * 发送notifty的广播
     *
     * @param allNotify
     */
    private void sendNotifyBorast(AllNotify allNotify) {
        Intent sendIntent = new Intent(MainActivity.REFRESH_MESSAGE);
        sendIntent.putExtra("allNotify", allNotify);
        sendBroadcast(sendIntent);
    }

    // Schedule application level keep-alives using the AlarmManager
    private void startKeepAlives() {
        Intent i = new Intent();
        i.setClass(this, MqttService.class);
        i.setAction(ACTION_START);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + Constants.MQTT_EXCEPTION_INTERVAL, Constants.MQTT_EXCEPTION_INTERVAL, pi);
    }

    // Remove all scheduled keep alives
    private void stopKeepAlives() {
        Intent i = new Intent();
        i.setClass(this, MqttService.class);
        i.setAction(ACTION_START);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmMgr.cancel(pi);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        Logger.i(TAG, "onDestory");
        try {
            unregisterReceiver(mConnectivityChanged);
            if (mqttClient != null) {
                mqttClient.disconnect();
                mqttClient = null;
            }
        } catch (Exception e) {

        }
    }

    /**
     * 显示异地登录的dialog
     *
     * @param time
     */
    private void showDialog(final String time) {
        new Handler(mContext.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Intent sendIntent = new Intent(MainActivity.LOGOUT_SUCCESS_MESSAGE);
                mContext.sendBroadcast(sendIntent);
                CustomDialog dialog = new CustomDialog(mContext, new CustomDialog.CustomDialogListener() {
                    @Override
                    public void customButtonListener(int postion) {
                        if (postion == 2) {
                            LoginActivity.actionLaunchService(mContext);
                        }
                    }
                }, false, false);
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                dialog.setHideTvCancel();
                dialog.setTvOkText("确定");
                dialog.setTvDialogTitle("下线通知");
                dialog.setTvDialogContent("您的账号在" + time + "时间上登录,如果不是您本人操作，建议您修改密码");
                dialog.show();
            }
        });
    }

    private BroadcastReceiver mConnectivityChanged = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeInfo = manager.getActiveNetworkInfo();
            if (activeInfo != null) {
                Logger.i(TAG, "有网络----");
                reconnectIfNecessary();
            } else if (mqttClient != null) {
                Logger.i(TAG, "无网络----");
                try {
                    mqttClient.disconnect();
                    mqttClient = null;
                    stopKeepAlives();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private synchronized void reconnectIfNecessary() {
        if ((mqttClient == null) || (mqttClient != null && !mqttClient.isConnected())) {
            Logger.i(TAG, "reconnectIfNecessary--网络重新连接---");
            connect();
        }
    }
}
