package com.jin91.preciousmetal.util;

import android.content.Context;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.widget.TextView;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lijinhua on 2015/5/11.
 */
public class StringUtil {

    public final static String buildId = "B136";
    public final static int GUIDE_VERSION = 2;

    /**
     * 根据时间和格式转换为字符串
     *

     * @return
     */
    public static String getLastDauTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(date == null ? new Date() : date);
    }

    /**
     * 根据时间和格式转换为字符串
     *
     * @param pattern
     * @param date
     * @return
     */
    public static String getFormattTime(String pattern, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date == null ? new Date() : date);
    }

    public static final String FORMAT_YEAR_MONTH_DAY = "yyyy-mm-dd";
    public static final String FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE = "yyyy-mm-dd hh:mm";
    public static final String FORMAT_MONTH_DAY_HOUR_MINUTE = "mm-dd hh:mm";
    public static final String FORMAT_MONTH_DAY = "mm-dd";
    public static final String FORMAT_HOUR_MINUTE = "hh:mm";
    public static final String FORMAT_HOUR_MINUTE_SECOND = "hh:mm:ss";

    public static String getFormatTime(String timeStr, String formatStr) {
        String timeFormat = "";
        if (TextUtils.isEmpty(timeStr) || timeStr.equals("null")) {
            return timeFormat = "";
        }
        String timeYear = timeStr.substring(0, 4);
        String timeMonth = timeStr.substring(5, 7);
        String timeDay = timeStr.substring(8, 10);
        String timeHour = timeStr.substring(11, 13);
        String timeMinute = timeStr.substring(14, 16);
        String timeSecond = timeStr.substring(17, 19);
        if (formatStr.equals(FORMAT_YEAR_MONTH_DAY)) {
            timeFormat = timeYear + "-" + timeMonth + "-" + timeDay;
        } else if (formatStr.equals(FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE)) {
            timeFormat = timeYear + "-" + timeMonth + "-" + timeDay + " " + timeHour + ":" + timeMinute;
        } else if (formatStr.equals(FORMAT_MONTH_DAY_HOUR_MINUTE)) {
            timeFormat = timeMonth + "-" + timeDay + " " + timeHour + ":" + timeMinute;
        } else if (formatStr.equals(FORMAT_MONTH_DAY)) {
            timeFormat = timeMonth + "-" + timeDay;
        } else if (formatStr.equals(FORMAT_HOUR_MINUTE)) {
            timeFormat = timeHour + ":" + timeMinute;
        } else if (formatStr.equals(FORMAT_HOUR_MINUTE_SECOND)) {
            timeFormat = timeHour + ":" + timeMinute + ":" + timeSecond;
        }
        return timeFormat;
    }

    public static String getSectionTime(String timeStr) {
//        String time = "";
//        if (TextUtils.isEmpty(timeStr) || timeStr.equals("null")) {
//            return time = "";
//        }
//        try {
//            String timestamp = "" + getTimestamp(timeStr);//""+(date.getTime()*1000);
//            time = fixTime(timestamp);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return timeStr;
    }

    /**
     * 处理时间
     *
     * @param timestamp
     * @return
     */
    public static String fixTime(String timestamp) {
        if (timestamp == null || "".equals(timestamp)) {
            return "";
        }

        try {
            long _timestamp = Long.parseLong(timestamp);
            if (System.currentTimeMillis() - _timestamp < 1 * 60 * 1000) {
                return "刚刚";
            } else if (System.currentTimeMillis() - _timestamp < 30 * 60 * 1000) {
                return ((System.currentTimeMillis() - _timestamp) / 1000 / 60) + "分钟前";
            } else {
                Calendar now = Calendar.getInstance();
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(_timestamp);

                if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR) && c.get(Calendar.MONTH) == now.get(Calendar.MONTH) && c.get(Calendar.DATE) == now.get(Calendar.DATE)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("今天 HH:mm");
                    return sdf.format(c.getTime());
                }
                if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR) && c.get(Calendar.MONTH) == now.get(Calendar.MONTH) && c.get(Calendar.DATE) == now.get(Calendar.DATE) - 1) {
                    SimpleDateFormat sdf = new SimpleDateFormat("昨天 HH:mm");
                    return sdf.format(c.getTime());
                } else if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("M月d日 HH:mm");
                    return sdf.format(c.getTime());
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日 HH:mm:ss");
                    return sdf.format(c.getTime());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 动态的时间转化
     * 2012-02-11 13:51:13
     * @return
     */
    public static String fixTimeFormateFromYMD(String time) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Long t = format.parse(time).getTime();
            return fixTimeFormate("" + t);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 处理时间
     *
     * @param timestamp
     * @return
     */
    public static String fixTimeFormate(String timestamp) {
        if (TextUtils.isEmpty(timestamp) || timestamp.equals("null")) {
            return "";
        }
        try {
            long _timestamp = Long.parseLong(timestamp);
            Calendar now = Calendar.getInstance();
            Calendar c = Calendar.getInstance();
            if (System.currentTimeMillis() - _timestamp < 1 * 60 * 1000) {
                SimpleDateFormat sdf = new SimpleDateFormat("今天 HH:mm");
                return sdf.format(_timestamp);
            } else if (System.currentTimeMillis() - _timestamp < 30 * 60 * 1000) {
                SimpleDateFormat sdf = new SimpleDateFormat("今天 HH:mm");
                return sdf.format(_timestamp);
            } else {

                c.setTimeInMillis(_timestamp);
                if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR) && c.get(Calendar.MONTH) == now.get(Calendar.MONTH) && c.get(Calendar.DATE) == now.get(Calendar.DATE)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("今天 HH:mm");
                    return sdf.format(_timestamp);
                }
                if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR) && c.get(Calendar.MONTH) == now.get(Calendar.MONTH) && c.get(Calendar.DATE) == now.get(Calendar.DATE) - 1) {
                    SimpleDateFormat sdf = new SimpleDateFormat("昨天 HH:mm");
                    return sdf.format(_timestamp);
                } else if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("M月d日 HH:mm");
                    return sdf.format(_timestamp);
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日 HH:mm");
                    return sdf.format(_timestamp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public static long getTimestamp(String time) throws ParseException {
        Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
        Date date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1970-01-01 08:00:00");
        long l = date1.getTime() - date2.getTime() > 0 ? date1.getTime() - date2.getTime() : date2.getTime() - date1.getTime();
        //long rand = (int)(Math.random()*1000);
        //return rand;
        return l;
    }

    /**
     * 参数编码，默认为utf-8
     *
     * @param s
     * @return
     */
    public static String encode(String s) {
        if (s == null) {
            return "";
        }
        return URLEncoder.encode(s);
    }

    /**
     * 参数反编码,默认为utf-8
     *
     * @param s
     * @return
     */
    public static String decode(String s) {
        if (s == null) {
            return "";
        }
        return URLDecoder.decode(s);
    }

    /** 检查@昵称 跳转个人主页 */
    public static void checkLink(Context context, TextView v, String host_scheme) {
        // 锟斤拷取@锟角筹拷
        Pattern mentionsPattern = Pattern.compile("@(\\w+?)(?=\\W|$)");
        String mentionsScheme = String.format("%s/?%s=", host_scheme, "&page=guest_info" + "&uid");
        v.setAutoLinkMask(0);
        Linkify.addLinks(v, mentionsPattern, mentionsScheme, new Linkify.MatchFilter() {
            @Override
            public boolean acceptMatch(CharSequence s, int start, int end) {
                return s.charAt(end - 1) != '.';
            }
        }, new Linkify.TransformFilter() {
            @Override
            public String transformUrl(Matcher match, String url) {
                return match.group(1);
            }
        });
    }

    public static String formatTime(long time) {
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
        String timeString = sf.format(new Date(time));
        return timeString;
    }

    /**
     * 时间转换(相距多少时间)
     *
     * @param date
     *            时间
     * @return 时间字符串
     * @author zhanghe
     * @date 2011-11-7
     */
    public static String getCreateAt24(String date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar nowCal = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(df.parse(date));
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        String str = "";
        try {
            int diffyear = nowCal.get(Calendar.YEAR) - cal.get(Calendar.YEAR);
            if (diffyear == 0) {
                int diffmonth = nowCal.get(Calendar.MONTH) - cal.get(Calendar.MONTH);
                if (diffmonth == 0) {
                    int diffday = nowCal.get(Calendar.DAY_OF_MONTH) - cal.get(Calendar.DAY_OF_MONTH);
                    String hour = "";
                    String minute = "";
                    String hourTemp = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
                    String minuteTemp = String.valueOf(cal.get(Calendar.MINUTE));
                    if (hourTemp.length() == 1) {
                        hour = "0" + hourTemp;
                    } else {
                        hour = hourTemp;
                    }
                    if (minuteTemp.length() == 1) {
                        minute = "0" + minuteTemp;
                    } else {
                        minute = minuteTemp;
                    }
                    if (diffday == 0) {
                        str = "今天" + hour + ":" + minute;
                    } else if (diffday == 1) {
                        str = "昨天" + hour + ":" + minute;
                    } else if (diffday == 2) {
                        str = "前天" + hour + ":" + minute;
                    } else {
                        str = Math.abs(diffday) + "天以前" + hour + ":" + minute;
                    }
                } else {
                    str = Math.abs(diffmonth) + "月以前";
                }
            } else {
                str = Math.abs(diffyear) + "年以前";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 得到两个年差值
     * @param date
     * @return
     */
    public static int getYearAt(String date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar nowCal = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        int diffyear = 0;
        try {
            cal.setTime(df.parse(date));

            diffyear = nowCal.get(Calendar.YEAR) - cal.get(Calendar.YEAR);
            int diffmonth = nowCal.get(Calendar.MONTH) - cal.get(Calendar.MONTH);
            if (diffmonth < 0) {
                if (diffyear > 0) {
                    --diffyear;
                }
            }
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return diffyear;
    }

    /**
     * 时间转换(相距多少时间)
     *
     * @param date
     *            时间
     * @return 时间字符串
     * @author zhanghe
     * @date 2011-11-7
     */
    public static String getCreateAt(String date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar nowCal = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(df.parse(date));
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        String str = "";
        try {
            int diffyear = nowCal.get(Calendar.YEAR) - cal.get(Calendar.YEAR);
            if (diffyear == 0) {
                int diffmonth = nowCal.get(Calendar.MONTH) - cal.get(Calendar.MONTH);
                if (diffmonth == 0) {
                    int diffday = nowCal.get(Calendar.DAY_OF_MONTH) - cal.get(Calendar.DAY_OF_MONTH);
                    String hour = "";
                    String minute = "";
                    String hourTemp = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
                    String minuteTemp = String.valueOf(cal.get(Calendar.MINUTE));
                    if (hourTemp.length() == 1) {
                        hour = "0" + hourTemp;
                    } else {
                        hour = hourTemp;
                    }
                    if (minuteTemp.length() == 1) {
                        minute = "0" + minuteTemp;
                    } else {
                        minute = minuteTemp;
                    }
                    if (diffday == 0) {
                        str = "今天" + hour + ":" + minute;
                    } else if (diffday == 1) {
                        str = "昨天" + hour + ":" + minute;
                    } else if (diffday == 2) {
                        str = "前天" + hour + ":" + minute;
                    } else {
                        str = date.substring(0, date.length() - 3);
                    }
                } else {
                    str = date.substring(0, date.length() - 3);
                }
            } else {
                str = date.substring(0, date.length() - 3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 实现文本复制功能 add by liwei
     *
     * @param content
     */
    public static void copy(String content, Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    /**
     * 实现粘贴功能 add by liwei
     *
     * @param context
     * @return
     */
    public static String paste(Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        return cmb.getText().toString().trim();
    }

    public static String getPercentage(int num, int Sum) {
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        String result = numberFormat.format((float) num / (float) Sum * 100);
        result = result + "%";
        return result;
    }

    public static int getPercentageInt(int num, int Sum) {
        int result = (int) ((float) num / (float) Sum * 100);
        return result;
    }

}
