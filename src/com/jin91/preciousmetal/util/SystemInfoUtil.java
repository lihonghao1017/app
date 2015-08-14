package com.jin91.preciousmetal.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;

import com.jin91.preciousmetal.common.util.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

public class SystemInfoUtil {

    // 获取CPU最大频率（单位KHZ）
    // "/system/bin/cat" 命令行
    // "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" 存储最大频率的文件的路径
    public static String getMaxCpuFreq() {
        String result = "";
        ProcessBuilder cmd;
        try {
            String[] args = {"/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"};
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1) {
                result = result + new String(re);
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            result = "N/A";
        }
        return result.trim();
    }

    // 获取CPU最小频率（单位KHZ）
    public static String getMinCpuFreq() {
        String result = "";
        ProcessBuilder cmd;
        try {
            String[] args = {"/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq"};
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1) {
                result = result + new String(re);
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            result = "N/A";
        }
        return result.trim();
    }

    // 实时获取CPU当前频率（单位KHZ）
    public static String getCurCpuFreq() {
        String result = "N/A";
        try {
            FileReader fr = new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            result = text.trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    // 获取CPU名字

    public String getCpuName() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            for (int i = 0; i < array.length; i++) {
            }
            return array[1];
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // CPU个数
    private int getNumCores() {
        // Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {

            @Override
            public boolean accept(File pathname) {
                // Check if filename is "cpu", followed by a single digit number
                if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }

        try {
            // Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            // Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            Logger.i("SystemInfoUtil", "CPU Count: " + files.length);

            // Return the number of cores (virtual CPU devices)
            return files.length;
        } catch (Exception e) {
            // Print exception
            Logger.i("SystemInfoUtil", "CPU Count: Failed.");
            e.printStackTrace();
            // Default to return 1 core
            return 1;
        }
    }

    // 2、内存：/proc/meminfo
    public void getTotalMemory() {
        String str1 = "/proc/meminfo";
        String str2 = "";
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            while ((str2 = localBufferedReader.readLine()) != null) {
                Logger.i("SystemInfoUtil", "---" + str2);
            }
        } catch (IOException e) {
        }
    }

    // 3、Rom大小
    public long[] getRomMemroy() {
        long[] romInfo = new long[2];
        // Total rom memory
        romInfo[0] = getTotalInternalMemorySize();

        // Available rom memory
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        romInfo[1] = blockSize * availableBlocks;
        getSystemVersion();
        return romInfo;
    }

    public long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    // 4、sdCard大小

    public long[] getSDCardMemory() {
        long[] sdCardInfo = new long[2];
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            long bSize = sf.getBlockSize();
            long bCount = sf.getBlockCount();
            long availBlocks = sf.getAvailableBlocks();

            sdCardInfo[0] = bSize * bCount;// 总大小
            sdCardInfo[1] = bSize * availBlocks;// 可用大小
        }
        return sdCardInfo;
    }

    // 5、电池电量

    private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level", 0);
            // level加%就是当前电量了
            // registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        }
    };

    // 6、系统的版本信息

    public String[] getSystemVersion() {
        String[] version = {"null", "null", "null", "null"};
        String str1 = "/proc/version";
        String str2;
        String[] arrayOfString;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            version[0] = arrayOfString[2];// KernelVersion
            localBufferedReader.close();
        } catch (IOException e) {
        }
        version[1] = Build.VERSION.RELEASE;// firmware version
        version[2] = Build.MODEL;// model
        version[3] = Build.DISPLAY;// system version
        return version;
    }

    // 7、mac地址和开机时间

//	public String[] getOtherInfo(Context mContext) {
//		String[] other = { "null", "null" };
//		WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
//		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//		if (wifiInfo.getMacAddress() != null) {
//			other[0] = wifiInfo.getMacAddress();
//		} else {
//			other[0] = "Fail";
//		}
//		other[1] = getTimes(mContext);
//		return other;
//	}

//	private String getTimes(Context mContext) {
//		long ut = SystemClock.elapsedRealtime() / 1000;
//		if (ut == 0) {
//			ut = 1;
//		}
//		int m = (int) ((ut / 60) % 60);
//		int h = (int) ((ut / 3600));
//		return h + " " + mContext.getString(R.string.hour) + m + " " + mContext.getString(R.string.minute);
//	}

    private String getAvailMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        MemoryInfo mi = new MemoryInfo();
        am.getMemoryInfo(mi);
        return Formatter.formatFileSize(context, mi.availMem);
    }

    private String getTotalMemory(Context context) {
        String str1 = "/proc/meminfo";
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
                initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;
                localBufferedReader.close();
            }
        } catch (IOException e) {

        }
        return Formatter.formatFileSize(context, initial_memory);
    }

    private String readCpuInfo() {
        String result = "";
        ProcessBuilder cmd;
        try {
            String[] args = {"system/bin/cat", "proc/cpuinfo"};
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[1024];
            while (in.read(re) != -1) {
                System.out.println(new String(re));
                result = result + new String(re);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String show_ROM_storage() {
        String path = Environment.getDataDirectory().getPath();
        StatFs statFs = new StatFs(path);
        long blockSize = statFs.getBlockSize();
        long totalBlocks = statFs.getBlockCount();
        long availableBlocks = statFs.getAvailableBlocks();
        long usedBlocks = totalBlocks - availableBlocks;

        // 处理存储容量格式
        String[] total = fileSize(totalBlocks * blockSize);
        String[] available = fileSize(availableBlocks * blockSize);
        String[] used = fileSize(usedBlocks * blockSize);

        // int ss = Integer.parseInt(used[0]);
        // int mm = Integer.parseInt(total[0]);
        // int tt = ss * 100 / mm;
        // LogUtil.e(Constant.TAG, tt + "");

        return available[0] + available[1];

    }

    // 处理存储容量格式

    private String[] fileSize(long size) {
        String str = "";
        if (size >= 1024) {
            str = "KB";
            size /= 1024;
            if (size >= 1024) {
                str = "MB";
                size /= 1024;
                if (size >= 1024) {
                    str = "GB";
                    size /= 1024;
                }
            }

        }
        DecimalFormat formatter = new DecimalFormat();
        formatter.setGroupingSize(3);
        String[] result = new String[2];
        result[0] = formatter.format(size);
        result[1] = str;

        return result;
    }

    public String getSystemInfo(Context context) {

        netInfo = "";
        // 获取网络连接管理者
        ConnectivityManager connectionManager = (ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE);
        // 获取网络的状态信息，有下面三种方式
        NetworkInfo info = connectionManager.getActiveNetworkInfo();
        netInfo = "|网络详细状态:" + info.getDetailedState()//
                + "|连接失败的原因:" + info.getReason() + "|网络是否可用:" + info.isAvailable() + "|是否已经连接:" + info.isConnected() + "|是否已经连接或正在连接:" + info.isConnectedOrConnecting() + "|是否连接失败:" + info.isFailover() + "|是否漫游:" + info.isRoaming();
        switch (info.getType()) {
            case ConnectivityManager.TYPE_WIFI:
                // getTypeName()：获取网络类型名称(一般取值“WIFI”或“MOBILE”)。
                netInfo = netInfo + "|连接方式:WIFI";
                netInfo = getWifiInfo(context, netInfo);
                break;
            case ConnectivityManager.TYPE_MOBILE:
                netInfo = netInfo + "|连接方式:MOBILE";
                tm.listen(myPhoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
                // getTypeName()：获取网络类型名称(一般取值“WIFI”或“MOBILE”)。
                // getExtraInfo()：获取附加信息。
                String extraInfo = info.getExtraInfo();
                if (extraInfo.equals("cmnet")) {
                    netInfo = netInfo + "移动方式:移动CMNET";
                } else if (extraInfo.equals("cmwap")) {
                    netInfo = netInfo + "移动方式:移动CMWAP";
                } else if (extraInfo.equals("3gwap")) {
                    netInfo = netInfo + "移动方式:联通3gwap";
                } else if (extraInfo.equals("3gnet")) {
                    netInfo = netInfo + "移动方式:联通3gnet";
                } else if (extraInfo.equals("uniwap")) {
                    netInfo = netInfo + "移动方式:联通uniwap";
                } else if (extraInfo.equals("uninet")) {
                    netInfo = netInfo + "移动方式:联通uninet";
                } else {
                    netInfo = netInfo + "移动方式:未知,可能为电信";
                }
                netInfo = getTelNetworkType(context, netInfo, tm);
                break;
            case ConnectivityManager.TYPE_WIMAX:
                netInfo = netInfo + "|连接方式:全球微波互联接入";
                break;
        }
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        // 判断是否存在SD卡
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        }
        String sdPath = sdDir.toString();
        return "|操作平台:" + Build.VERSION.RELEASE //
                + "|设备屏幕分辨率:" + metrics.widthPixels + "*" + metrics.heightPixels//
                + "|设备型号:" + Build.MODEL //
                + "|API Level:" + Build.VERSION.SDK //
                + "|cpu当前频率:" + getCurCpuFreq() //
                + "|可用内存:" + getAvailMemory(context) //
                + getNetWork(context); //
    }

    public String getSystemInfoDetail(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        // 判断是否存在SD卡
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        }
        String sdPath = sdDir.toString();
        return "|操作平台:" + Build.VERSION.RELEASE //
                + "|设备屏幕分辨率:" + metrics.widthPixels + "*" + metrics.heightPixels//
                + "|SD卡路径:" + sdPath //
                + "|设备型号:" + Build.MODEL //
                + "|API Level:" + Build.VERSION.SDK //
                + "|cup名称:" + getCpuName() //
                + "|cpu核数:" + getNumCores() //
                + "|cpu最小频率:" + getMinCpuFreq() //
                + "|cpu最大频率:" + getMaxCpuFreq() //
                + "|cpu当前频率:" + getCurCpuFreq() //
                + "|运行内存:" + getTotalMemory(context)//
                + "|可用内存:" + getAvailMemory(context) //
                + "|运行内存:" + show_ROM_storage() //
                + getNetWork(context); //
    }

    MyPhoneStateListener myPhoneStateListener = new MyPhoneStateListener();
    TelephonyManager tm;

    String netInfo = "";
    Context context;


    public  String getSystemInfoLow(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
       return "|操作平台:" + Build.VERSION.RELEASE
                + "|设备屏幕分辨率:" + metrics.widthPixels + "*" + metrics.heightPixels//
               + "|运行内存:" + getTotalMemory(context)//
               + "|可用内存:" + getAvailMemory(context) //
               + "|运行内存:" + show_ROM_storage() //
               + "|设备型号:" + Build.MODEL; //
    }

    private String getNetWork(Context context) {
        this.context = context;
        netInfo = "";
        // 获取网络连接管理者
        ConnectivityManager connectionManager = (ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE);
        // 获取网络的状态信息，有下面三种方式
        NetworkInfo info = connectionManager.getActiveNetworkInfo();
        netInfo = "|网络详细状态:" + info.getDetailedState()//
                + "|连接失败的原因:" + info.getReason() + "|网络是否可用:" + info.isAvailable() + "|是否已经连接:" + info.isConnected() + "|是否已经连接或正在连接:" + info.isConnectedOrConnecting() + "|是否连接失败:" + info.isFailover() + "|是否漫游:" + info.isRoaming();
        tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        netInfo = netInfo + "|设备 ID:" + tm.getDeviceId() //
                + "|手机号:" + tm.getLine1Number() //
                + "|软件版本号:" + tm.getDeviceSoftwareVersion() //
                + "|服务商名称:" + tm.getSimOperatorName() //
                + "|手机类型:" + tm.getPhoneType()//

                + "|当前使用的网络类型:" + tm.getNetworkType() //
                + "|国际长途区号:" + tm.getNetworkCountryIso()

                + "|SIM状态信息:" + tm.getSimState(); //
        // 用移动CMNET方式
        // getExtraInfo 的值是cmnet
        // 用移动CMWAP方式
        // getExtraInfo 的值是cmwap 但是不在代理的情况下访问普通的网站访问不了
        // 用联通3gwap方式
        // getExtraInfo 的值是3gwap
        // 用联通3gnet方式
        // getExtraInfo 的值是3gnet
        // 用联通uniwap方式
        // getExtraInfo 的值是uniwap
        // 用联通uninet方式
        // getExtraInfo 的值是uninet
        //
        // 用电信方式待定
        // 下面解决cmwap联网
        switch (info.getType()) {
            // getType()：获取网络类型(一般为移动或Wi-Fi)。
            // 当用wifi上的时候
            // getType 是WIFI
            // getExtraInfo是空的
            // 当用手机上的时候
            // getType 是MOBILE
            //
            case ConnectivityManager.TYPE_WIFI:
                // getTypeName()：获取网络类型名称(一般取值“WIFI”或“MOBILE”)。
                netInfo = netInfo + "|连接方式:WIFI";
                netInfo = getWifiInfo(context, netInfo);
                break;
            case ConnectivityManager.TYPE_MOBILE:
                netInfo = netInfo + "|连接方式:MOBILE";
                tm.listen(myPhoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
                // getTypeName()：获取网络类型名称(一般取值“WIFI”或“MOBILE”)。
                // getExtraInfo()：获取附加信息。
                String extraInfo = info.getExtraInfo();
                if (extraInfo.equals("cmnet")) {
                    netInfo = netInfo + "移动方式:移动CMNET";
                } else if (extraInfo.equals("cmwap")) {
                    netInfo = netInfo + "移动方式:移动CMWAP";
                } else if (extraInfo.equals("3gwap")) {
                    netInfo = netInfo + "移动方式:联通3gwap";
                } else if (extraInfo.equals("3gnet")) {
                    netInfo = netInfo + "移动方式:联通3gnet";
                } else if (extraInfo.equals("uniwap")) {
                    netInfo = netInfo + "移动方式:联通uniwap";
                } else if (extraInfo.equals("uninet")) {
                    netInfo = netInfo + "移动方式:联通uninet";
                } else {
                    netInfo = netInfo + "移动方式:未知,可能为电信";
                }
                netInfo = getTelNetworkType(context, netInfo, tm);
                break;
            case ConnectivityManager.TYPE_WIMAX:
                netInfo = netInfo + "|连接方式:全球微波互联接入";
                break;
        }
        return netInfo;
    }

    private class MyPhoneStateListener extends PhoneStateListener {

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            netInfo = netInfo + "|移动信号强度:" + String.valueOf(signalStrength.getGsmSignalStrength());
            tm.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE);
        }

    }

    ;/* End of private Class */

    private String getWifiInfo(Context context, String netInfo) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String maxText = info.getMacAddress();
        String ipText = intToIp(info.getIpAddress());
        String status = "";
        if (wifi.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
            status = "WIFI_STATE_ENABLED";
        }
        String ssid = info.getSSID();
        int networkID = info.getNetworkId();
        int speed = info.getLinkSpeed();
        return netInfo + "|Max地址：" + maxText + "|" + "Ip地址：" + ipText + "|" + "Wifi status :" + status + "|" + "ssid(Wifi名称):" + ssid + "|" + "net work id :" + networkID + "|" + "网络速度:" + speed;
    }

    private String intToIp(int ip) {
        return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "." + ((ip >> 24) & 0xFF);
    }

    /**
     * Network type is unknown
     */
    public static final int NETWORK_TYPE_UNKNOWN = 0;
    /**
     * Current network is GPRS
     */
    public static final int NETWORK_TYPE_GPRS = 1;
    /**
     * Current network is EDGE
     */
    public static final int NETWORK_TYPE_EDGE = 2;
    /**
     * Current network is UMTS
     */
    public static final int NETWORK_TYPE_UMTS = 3;
    /**
     * Current network is CDMA: Either IS95A or IS95B
     */
    public static final int NETWORK_TYPE_CDMA = 4;
    /**
     * Current network is EVDO revision 0
     */
    public static final int NETWORK_TYPE_EVDO_0 = 5;
    /**
     * Current network is EVDO revision A
     */
    public static final int NETWORK_TYPE_EVDO_A = 6;
    /**
     * Current network is 1xRTT
     */
    public static final int NETWORK_TYPE_1xRTT = 7;
    /**
     * Current network is HSDPA
     */
    public static final int NETWORK_TYPE_HSDPA = 8;
    /**
     * Current network is HSUPA
     */
    public static final int NETWORK_TYPE_HSUPA = 9;
    /**
     * Current network is HSPA
     */
    public static final int NETWORK_TYPE_HSPA = 10;
    /**
     * Current network is iDen
     */
    public static final int NETWORK_TYPE_IDEN = 11;
    /**
     * Current network is EVDO revision B
     */
    public static final int NETWORK_TYPE_EVDO_B = 12;
    /**
     * Current network is LTE
     */
    public static final int NETWORK_TYPE_LTE = 13;
    /**
     * Current network is eHRPD
     */
    public static final int NETWORK_TYPE_EHRPD = 14;
    /**
     * Current network is HSPA+
     */
    public static final int NETWORK_TYPE_HSPAP = 15;

    /**
     * Unknown network class. {@hide}
     */
    public static final int NETWORK_CLASS_UNKNOWN = 0;
    /**
     * Class of broadly defined "2G" networks. {@hide}
     */
    public static final int NETWORK_CLASS_2_G = 1;
    /**
     * Class of broadly defined "3G" networks. {@hide}
     */
    public static final int NETWORK_CLASS_3_G = 2;
    /**
     * Class of broadly defined "4G" networks. {@hide}
     */
    public static final int NETWORK_CLASS_4_G = 3;

    /**
     * Return general class of network type, such as "3G" or "4G". In cases where classification is
     * contentious, this method is conservative.
     *
     * @hide
     */
    public static int getNetworkClassType(int networkType) {
        switch (networkType) {
            case NETWORK_TYPE_GPRS:
            case NETWORK_TYPE_EDGE:
            case NETWORK_TYPE_CDMA:
            case NETWORK_TYPE_1xRTT:
            case NETWORK_TYPE_IDEN:
                return NETWORK_CLASS_2_G;
            case NETWORK_TYPE_UMTS:
            case NETWORK_TYPE_EVDO_0:
            case NETWORK_TYPE_EVDO_A:
            case NETWORK_TYPE_HSDPA:
            case NETWORK_TYPE_HSUPA:
            case NETWORK_TYPE_HSPA:
            case NETWORK_TYPE_EVDO_B:
            case NETWORK_TYPE_EHRPD:
            case NETWORK_TYPE_HSPAP:
                return NETWORK_CLASS_3_G;
            case NETWORK_TYPE_LTE:
                return NETWORK_CLASS_4_G;
            default:
                return NETWORK_CLASS_UNKNOWN;
        }
    }

    /**
     * 获取当前网络类型 0："unknown" 1："GPRS" 2："EDGE" 3："UMTS" 4："CDMA: Either IS95A or IS95B" 5："EVDO revision 0"
     * 6："EVDO revision A" 7："1xRTT" 8："HSDPA" 9："HSUPA" 10："HSPA" 11："iDen" 12： "EVDO revision B" 13："LTE"
     * 14："eHRPD" 15："HSPA+"
     */
    public String getTelNetworkType(Context context, String netInfo, TelephonyManager tm) {
        int networkType = tm.getNetworkType();
        switch (getNetworkClassType(networkType)) {
            case NETWORK_CLASS_2_G:
                netInfo = "2G网络";
                break;
            case NETWORK_CLASS_3_G:
                netInfo = "3G网络";
                break;
            case NETWORK_CLASS_4_G:
                netInfo = "4G网络";
                break;
            case NETWORK_CLASS_UNKNOWN:
                netInfo = "网络类型未知";
                break;
            default:
                break;
        }
        netInfo = netInfo + "|网络详细类型为:";
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                netInfo = netInfo + "unknown";
                break;
            case TelephonyManager.NETWORK_TYPE_GPRS:
                netInfo = netInfo + "GPRS";
                break;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                netInfo = netInfo + "EDGE";
                break;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                netInfo = netInfo + "UMTS";
                break;
            case TelephonyManager.NETWORK_TYPE_CDMA:
                netInfo = netInfo + "CDMA: Either IS95A or IS95B";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                netInfo = netInfo + "EVDO revision 0";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                netInfo = netInfo + "EVDO revision A";
                break;
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                netInfo = netInfo + "1xRTT";
                break;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                netInfo = netInfo + "HSDPA";
                break;
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                netInfo = netInfo + "HSUPA";
                break;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                netInfo = netInfo + "HSPA";
                break;
            case TelephonyManager.NETWORK_TYPE_IDEN:
                netInfo = netInfo + "iDen";
                break;
            // 一下2.2不支持
            // case TelephonyManager.NETWORK_TYPE_EVDO_B:
            // netInfo= netType+"EVDO revision B";
            // break;
            // case TelephonyManager.NETWORK_TYPE_LTE:
            // netInfo= netType+"LTE";
            // break;
            // case TelephonyManager.NETWORK_TYPE_EHRPD:
            // netInfo= netType+"eHRPD";
            // break;
            // case TelephonyManager.NETWORK_TYPE_HSPAP:
            // netInfo= netType+"HSPA+";
            // break;
            //
            default:
                return netInfo + "获取网络类型错误";

        }
        return netInfo;
    }
}
