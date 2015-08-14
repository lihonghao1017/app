package com.jin91.preciousmetal.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.api.entity.Update;
import com.jin91.preciousmetal.common.util.Logger;
import com.jin91.preciousmetal.ui.Constants;
import com.jin91.preciousmetal.util.FileUtils;
import com.jin91.preciousmetal.util.JsonUtil;
import com.jin91.preciousmetal.util.MessageToast;
import com.jin91.preciousmetal.util.UserHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

/**
 * Created by Administrator on 2015/5/24.
 * 升级包下载
 */
public class DownLoadService extends Service {

    public static final String TAG = "DownLoadService";
    private static final int NOTIFY_ID = 0;
    private NotificationManager mNotificationManager;
    private static final int UPDATE_DB_PER_SIZE = 102400;
    private Notification mNotification;
    private String apkPath;
    private Update currentUpdate;
    private Context mContext;
    DecimalFormat df = new DecimalFormat("#.00");

    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    int compeleted = msg.arg1;
                    if (compeleted == currentUpdate.fl) {
                        // 下载完成
                        mNotification.flags = Notification.FLAG_AUTO_CANCEL;
                        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, getApkFileIntent(apkPath), PendingIntent.FLAG_UPDATE_CURRENT);
                        mNotification.setLatestEventInfo(mContext, getString(R.string.down_comple), "点击安装", contentIntent);
                        startActivity(getApkFileIntent(apkPath));
                        stopSelf();
                    } else {
                        RemoteViews contentView = mNotification.contentView;
                        contentView.setTextViewText(R.id.progress, df.format((float) compeleted / currentUpdate.fl * 100) + "%");
                        contentView.setProgressBar(R.id.pb, (int) currentUpdate.fl, compeleted, false);
                    }

                    // 最后别忘了通知一下,否则不会更新
                    mNotificationManager.notify(NOTIFY_ID, mNotification);
                    break;
                case -1:
                    // 下载出错
                    MessageToast.showToast(R.string.net_error, 0);
                    RemoteViews contentView = mNotification.contentView;
                    contentView.setTextViewText(R.id.tv_opration_name, "下载失败");
                    stopSelf();
                    break;
            }
            mNotificationManager.notify(NOTIFY_ID, mNotification);
        }

        ;
    };

    public static  void actionLaunch(Context mContext)
    {
            mContext.startService(new Intent(mContext,DownLoadService.class));
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.i(TAG, "DownLoadService---onCreate");
        mContext = this;
        mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        UserHelper userHelper = UserHelper.getInstance();
        String updateJson = userHelper.getUpdateJson();
        ;
        if (!TextUtils.isEmpty(updateJson)) {
            currentUpdate = JsonUtil.parseUpdate(updateJson);
            if (currentUpdate != null) {
                setUpNotification();
                startDownload();
            } else {
                stopSelf();
            }
        } else {
            stopSelf();
        }
    }

    /**
     * 创建通知
     */
    private void setUpNotification() {
        int icon = R.mipmap.notify_icon;
        CharSequence tickerText = getString(R.string.begin_down);
        long when = System.currentTimeMillis();
        mNotification = new Notification(icon, tickerText, when);
        // 放置在"正在运行"栏目中
        mNotification.flags = Notification.FLAG_AUTO_CANCEL;

        RemoteViews contentView = new RemoteViews(mContext.getPackageName(), R.layout.downnotify_new);
        contentView.setTextViewText(R.id.tv_opration_name, "正在下载");
        // 指定个性化视图
        mNotification.contentView = contentView;
        mNotificationManager.notify(NOTIFY_ID, mNotification);
        Message message = handler.obtainMessage();
        message.what = 1;
        message.arg1 = 0;
        handler.sendMessage(message);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 下载模块
     */
    private void startDownload() {
        new Thread(new DownloadThread()).start();
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class DownloadThread extends Thread {
        @Override
        public void run() {

            // 1. create file if not exist.
            createFile();

            HttpURLConnection conn = null;
            RandomAccessFile accessFile = null;
            InputStream is = null;
            int preFinishedSize = 0; // 上一个完成的大小
            int currentFinishedSize = 0;// 当前已经完成的大小
            try {
                URL url = new URL(currentUpdate.url);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestProperty("Accept-Encoding", "musixmatch");
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Range", "bytes=" + preFinishedSize + "-" + currentUpdate.fl);
                conn.setRequestProperty("User-Agent",
                        "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");

                accessFile = new RandomAccessFile(apkPath, "rwd");
                accessFile.seek(preFinishedSize);

                is = conn.getInputStream();

                //int size = totalSize / 200 > UPDATE_DB_PER_SIZE ? UPDATE_DB_PER_SIZE : totalSize / 200; //decrease refresh frequency
                byte[] buffer = new byte[4096];
                int length = -1;
                while ((length = is.read(buffer)) != -1) {

                    currentFinishedSize += length;
                    accessFile.write(buffer, 0, length);
                    if (currentFinishedSize - preFinishedSize > UPDATE_DB_PER_SIZE || currentFinishedSize == currentUpdate.fl) {
                        // 大于100kb才更新进度条 或者已经下载完成
                        Message msg = handler.obtainMessage();
                        msg.what = 1;
                        msg.arg1 = currentFinishedSize;
                        preFinishedSize = currentFinishedSize;
                        handler.sendMessage(msg);
                    }
                }
                conn.disconnect();

            } catch (Exception e) {
                Logger.i(TAG, "download exception : " + e.getMessage());
                e.printStackTrace();
                // 下载失败
                Message msg = handler.obtainMessage();
                msg.what = -1;
                handler.sendMessage(msg);
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                    if (accessFile != null) {
                        accessFile.close();
                    }
                    if (conn != null) {
                        conn.disconnect();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return;
        }
    }

    // Android获取一个用于打开APK文件的intent
    public static Intent getApkFileIntent(String param) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + param), "application/vnd.android.package-archive");
        return intent;
    }

    /**
     * createFile
     */
    private void createFile() {
        HttpURLConnection conn = null;
        RandomAccessFile accessFile = null;
        try {
            URL url = new URL(currentUpdate.url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);

            conn.setRequestMethod("GET");

            int fileSize = conn.getContentLength();
            conn.disconnect();

            File downFilePath = new File(FileUtils.getFailPath(mContext, Constants.DOWN_PATH));
            if (!downFilePath.exists()) {
                downFilePath.mkdirs();
            }

            File file = new File(downFilePath.getAbsolutePath() + "/" + getFileName(currentUpdate.url));
            if (!file.exists()) {
                file.createNewFile();
                // if new file created, then reset the finished size.
            }
            apkPath = file.getAbsolutePath();
            Logger.i(TAG, "apkPath===="+apkPath);
            accessFile = new RandomAccessFile(file, "rwd");
            if (fileSize > 0) {
                accessFile.setLength(fileSize);
            }
            accessFile.close();
        } catch (MalformedURLException e) {
        } catch (FileNotFoundException e) {
            Logger.i(TAG, "createFile FileNotFoundException---" + e);
        } catch (IOException e) {
            Logger.i(TAG, "createFile IOException---" + e);

        }
    }

    public static String getFileName(String url) {
        String temp[] = url.replaceAll("\\\\", "/").split("/");
        String myFileName = "";
        if (temp.length > 1) {
            myFileName = temp[temp.length - 1];
        }
        return myFileName;
    }
}
