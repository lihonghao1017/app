package com.jin91.preciousmetal.util;

import android.content.Context;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Tienfook
 * @version 2011-8-19 下午04:14:20 微薄列表时间格式化
 */
public class DateUtil {

	public static int getFullDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int day = cal.get(Calendar.DATE);
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);
		String da = "";
		da = da + year;
		if (month < 10) {
			da = da + "0" + month;
		} else {
			da = da + month;
		}
		if (day < 10) {
			da = da + "0" + day;
		} else {
			da = da + day;
		}
		return Integer.parseInt(da);
	}

	public static String converTime(Context context, double timestamp) {
		DecimalFormat df = new DecimalFormat("0");
		// 发布时间
		long weiboTime = Long.parseLong(df.format(timestamp));
		// 当前时间
		long currentSeconds = System.currentTimeMillis() / 1000;
		long timeGap = currentSeconds - weiboTime;// 与现在时间相差秒数
		String timeStr = null;
		if (timeGap > 365 * 24 * 60 * 60) {// 1年以上
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			timeStr = sdf.format(new Date(weiboTime * 1000));
		}
		if (timeGap > 24 * 60 * 60) {// 1天以上
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
			timeStr = sdf.format(new Date(weiboTime * 1000));
		} else if (timeGap > 60 * 60) {// 1小时-24小时
//			timeStr = timeGap / (60 * 60) + context.getString(R.string.hour_label_plural); // 小时前
		} else if (timeGap > 60) {// 1分钟-59分钟
//			timeStr = timeGap / 60 + context.getString(R.string.minute_label); // 分钟前
		} else {// 1秒钟-59秒钟
//			timeStr = context.getString(R.string.less_than_one_minute); // 小于1分钟
		}
		return timeStr;
	}

	/**
	 * @Description:
	 * @param time
	 *            返回时间戳
	 * @param format
	 *            目标时间格式
	 * @param interval
	 *            时间加减
	 * @return
	 * @author: qiaoshl
	 * @date:2014-6-9
	 */

	public static String converTime(long time, String format, int interval) {
		DateFormat sdf = new SimpleDateFormat(format);
		try {
			Date d = new Date(time);
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			c.add(Calendar.HOUR, interval);
			Date temp_date = c.getTime();
			return sdf.format(temp_date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String converTime(long time, String format) {
		DateFormat sdf = new SimpleDateFormat(format);
		try {
			Date d = new Date(time);
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			c.add(Calendar.HOUR, 0);
			Date temp_date = c.getTime();
			return sdf.format(temp_date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * @Description:
	 * @param time
	 * @return 2014-6-11 9:32:04
	 * @author: qiaoshl
	 * @date:2014-6-11
	 */

	public static String converTimeToLocale(long time) {
		return new Date().toLocaleString();
	}

	/**
	 * @Description:11 Jun 2014 05:21:07 GMT
	 * @param time
	 * @return
	 * @author: qiaoshl
	 * @date:2014-6-11
	 */

	public static String converTimeToGMT(long time) {
		return new Date(time).toGMTString();
	}

	// public static String converTime(long time, String format) {
	// SimpleDateFormat sdf = new SimpleDateFormat(format);
	// return sdf.format(new Date(time));
	// }

	/**
	 * @Description:
	 * @param date
	 *            日期字符串
	 * @param srcFormat
	 *            日期源格式
	 * @return
	 * @author: qiaoshl
	 * @date:2014年1月21日
	 */

	public static String converTime(String date, String srcFormat, String toFormat) {
		SimpleDateFormat srcFormatDate = new SimpleDateFormat(srcFormat);
		SimpleDateFormat toFormatDate = new SimpleDateFormat(toFormat);
		try {
			Date time = srcFormatDate.parse(date);
			return toFormatDate.format(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @Description:精确到秒
	 * @return yyyy-MM-dd HH:mm:ss
	 * @author: qiaoshl
	 * @date:2014年1月13日
	 */

	public static String getTodayToSecond() {
		// 12小时进制
		// SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		// 24小时进制 如果HH为大写为24小时进制,hh为小写为12小时进制
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(new Date(System.currentTimeMillis()));
	}

	/**
	 * @Description:
	 * @param format
	 *            eg:yyyy-MM-dd HH:mm:ss
	 * @return
	 * @author: qiaoshl
	 * @date:2014年1月13日
	 */

	public static String getTodayToSecond(String format) {
		// 12小时进制
		// SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		// 24小时进制 如果HH为大写为24小时进制,hh为小写为12小时进制
		if (format == null || format.equals(""))
			return getTodayToSecond();
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(new Date(System.currentTimeMillis()));
	}

	/**
	 * @Description:精确到毫秒
	 * @return yyyy-MM-dd HH:mm:ss SSS
	 * @author: qiaoshl
	 * @date:2014年1月13日
	 */

	public static String getTodayToMillisecond() {
		// 12小时进制
		// SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		// 24小时进制 如果HH为大写为24小时进制,hh为小写为12小时进制
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
		return df.format(new Date(System.currentTimeMillis()));
	}

	/**
	 * @Description:
	 * @param format
	 * @return yyyy-MM-dd HH:mm:ss SSS
	 * @author: qiaoshl
	 * @date:2014年1月13日
	 */

	public static String getTodayToMillisecond(String format) {
		// 12小时进制
		// SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		// 24小时进制 如果HH为大写为24小时进制,hh为小写为12小时进制
		if (format == null || format.equals(""))
			return getTodayToMillisecond();
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(new Date(System.currentTimeMillis()));
	}

	// public static String converTime(Context context, String datestr) {
	// long timestamp = 0;
	// String timeStr = context.getString(R.string.not_time);
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
	// Date date = null;
	// if (!"".equals(datestr)) {
	// try {
	// date = sdf.parse(datestr);
	// timestamp = date.getTime() / 1000;
	// } catch (ParseException e) {
	// // TODO Auto-generated catch block
	// }
	// long currentSeconds = System.currentTimeMillis() / 1000;
	// long timeGap = currentSeconds - timestamp;// 与现在时间相差秒数
	// if (timeGap > 24 * 60 * 60) {// 1天以上
	// timeStr = timeGap / (24 * 60 * 60) + context.getString(R.string.day_label);
	// } else if (timeGap > 60 * 60) {// 1小时-24小时
	// timeStr = timeGap / (60 * 60) + context.getString(R.string.hour_label_plural);
	// } else if (timeGap > 60) {// 1分钟-59分钟
	// timeStr = timeGap / 60 + context.getString(R.string.minute_label);
	// } else {// 1秒钟-59秒钟
	// timeStr = context.getString(R.string.now);
	// }
	// }
	// return timeStr;
	// }

	public static String parseRoomDate(String date) {
		SimpleDateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat toFormat = new SimpleDateFormat("MM-dd HH:mm");
		try {
			Date fromDate = fromFormat.parse(date);
			return toFormat.format(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String parseRoomDate(String date, String fromFormat, String toFormat) {
		SimpleDateFormat simpFromFormat = new SimpleDateFormat(fromFormat);
		SimpleDateFormat simpToFormat = new SimpleDateFormat(toFormat);
		try {
			Date fromDate = simpFromFormat.parse(date);
			return simpToFormat.format(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String parseStudyDate(String date) {
		SimpleDateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat toFormat = new SimpleDateFormat("MM-dd");
		try {
			Date fromDate = fromFormat.parse(date);
			return toFormat.format(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static final ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<SimpleDateFormat>();

	private static final Object object = new Object();

	/**
	 * 获取SimpleDateFormat
	 * 
	 * @param pattern
	 *            日期格式
	 * @return SimpleDateFormat对象
	 * @throws RuntimeException
	 *             异常：非法日期格式
	 */
	private static SimpleDateFormat getDateFormat(String pattern) throws RuntimeException {
		SimpleDateFormat dateFormat = threadLocal.get();
		if (dateFormat == null) {
			synchronized (object) {
				if (dateFormat == null) {
					dateFormat = new SimpleDateFormat(pattern);
					dateFormat.setLenient(false);
					threadLocal.set(dateFormat);
				}
			}
		}
		dateFormat.applyPattern(pattern);
		return dateFormat;
	}

	/**
	 * 获取日期中的某数值。如获取月份
	 * 
	 * @param date
	 *            日期
	 * @param dateType
	 *            日期格式
	 * @return 数值
	 */
	private static int getInteger(Date date, int dateType) {
		int num = 0;
		Calendar calendar = Calendar.getInstance();
		if (date != null) {
			calendar.setTime(date);
			num = calendar.get(dateType);
		}
		return num;
	}

	/**
	 * 增加日期中某类型的某数值。如增加日期
	 * 
	 * @param date
	 *            日期字符串
	 * @param dateType
	 *            类型
	 * @param amount
	 *            数值
	 * @return 计算后日期字符串
	 */
	private static String addInteger(String date, int dateType, int amount) {
		String dateString = null;
		DateStyle dateStyle = getDateStyle(date);
		if (dateStyle != null) {
			Date myDate = StringToDate(date, dateStyle);
			myDate = addInteger(myDate, dateType, amount);
			dateString = DateToString(myDate, dateStyle);
		}
		return dateString;
	}

	/**
	 * 增加日期中某类型的某数值。如增加日期
	 * 
	 * @param date
	 *            日期
	 * @param dateType
	 *            类型
	 * @param amount
	 *            数值
	 * @return 计算后日期
	 */
	private static Date addInteger(Date date, int dateType, int amount) {
		Date myDate = null;
		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(dateType, amount);
			myDate = calendar.getTime();
		}
		return myDate;
	}

	/**
	 * 获取精确的日期
	 * 
	 * @param timestamps
	 *            时间long集合
	 * @return 日期
	 */
	private static Date getAccurateDate(List<Long> timestamps) {
		Date date = null;
		long timestamp = 0;
		Map<Long, long[]> map = new HashMap<Long, long[]>();
		List<Long> absoluteValues = new ArrayList<Long>();

		if (timestamps != null && timestamps.size() > 0) {
			if (timestamps.size() > 1) {
				for (int i = 0; i < timestamps.size(); i++) {
					for (int j = i + 1; j < timestamps.size(); j++) {
						long absoluteValue = Math.abs(timestamps.get(i) - timestamps.get(j));
						absoluteValues.add(absoluteValue);
						long[] timestampTmp = { timestamps.get(i), timestamps.get(j) };
						map.put(absoluteValue, timestampTmp);
					}
				}

				// 有可能有相等的情况。如2012-11和2012-11-01。时间戳是相等的。此时minAbsoluteValue为0
				// 因此不能将minAbsoluteValue取默认值0
				long minAbsoluteValue = -1;
				if (!absoluteValues.isEmpty()) {
					minAbsoluteValue = absoluteValues.get(0);
					for (int i = 1; i < absoluteValues.size(); i++) {
						if (minAbsoluteValue > absoluteValues.get(i)) {
							minAbsoluteValue = absoluteValues.get(i);
						}
					}
				}

				if (minAbsoluteValue != -1) {
					long[] timestampsLastTmp = map.get(minAbsoluteValue);

					long dateOne = timestampsLastTmp[0];
					long dateTwo = timestampsLastTmp[1];
					if (absoluteValues.size() > 1) {
						timestamp = Math.abs(dateOne) > Math.abs(dateTwo) ? dateOne : dateTwo;
					}
				}
			} else {
				timestamp = timestamps.get(0);
			}
		}

		if (timestamp != 0) {
			date = new Date(timestamp);
		}
		return date;
	}

	/**
	 * 判断字符串是否为日期字符串
	 * 
	 * @param date
	 *            日期字符串
	 * @return true or false
	 */
	public static boolean isDate(String date) {
		boolean isDate = false;
		if (date != null) {
			if (getDateStyle(date) != null) {
				isDate = true;
			}
		}
		return isDate;
	}

	/**
	 * 获取日期字符串的日期风格。失敗返回null。
	 * 
	 * @param date
	 *            日期字符串
	 * @return 日期风格
	 */
	public static DateStyle getDateStyle(String date) {
		DateStyle dateStyle = null;
		Map<Long, DateStyle> map = new HashMap<Long, DateStyle>();
		List<Long> timestamps = new ArrayList<Long>();
		for (DateStyle style : DateStyle.values()) {
			if (style.isShowOnly()) {
				continue;
			}
			Date dateTmp = null;
			if (date != null) {
				try {
					ParsePosition pos = new ParsePosition(0);
					dateTmp = getDateFormat(style.getValue()).parse(date, pos);
					if (pos.getIndex() != date.length()) {
						dateTmp = null;
					}
				} catch (Exception e) {
				}
			}
			if (dateTmp != null) {
				timestamps.add(dateTmp.getTime());
				map.put(dateTmp.getTime(), style);
			}
		}
		Date accurateDate = getAccurateDate(timestamps);
		if (accurateDate != null) {
			dateStyle = map.get(accurateDate.getTime());
		}
		return dateStyle;
	}

	/**
	 * 将日期字符串转化为日期。失败返回null。
	 * 
	 * @param date
	 *            日期字符串
	 * @return 日期
	 */
	public static Date StringToDate(String date) {
		DateStyle dateStyle = getDateStyle(date);
		return StringToDate(date, dateStyle);
	}

	/**
	 * 将日期字符串转化为日期。失败返回null。
	 * 
	 * @param date
	 *            日期字符串
	 * @param pattern
	 *            日期格式
	 * @return 日期
	 */
	public static Date StringToDate(String date, String pattern) {
		Date myDate = null;
		if (date != null) {
			try {
				myDate = getDateFormat(pattern).parse(date);
			} catch (Exception e) {
			}
		}
		return myDate;
	}

	/**
	 * 将日期字符串转化为日期。失败返回null。
	 * 
	 * @param date
	 *            日期字符串
	 * @param dateStyle
	 *            日期风格
	 * @return 日期
	 */
	public static Date StringToDate(String date, DateStyle dateStyle) {
		Date myDate = null;
		if (dateStyle != null) {
			myDate = StringToDate(date, dateStyle.getValue());
		}
		return myDate;
	}

	/**
	 * 将日期转化为日期字符串。失败返回null。
	 * 
	 * @param date
	 *            日期
	 * @param pattern
	 *            日期格式
	 * @return 日期字符串
	 */
	public static String DateToString(Date date, String pattern) {
		String dateString = null;
		if (date != null) {
			try {
				dateString = getDateFormat(pattern).format(date);
			} catch (Exception e) {
			}
		}
		return dateString;
	}

	/**
	 * 将日期转化为日期字符串。失败返回null。
	 * 
	 * @param date
	 *            日期
	 * @param dateStyle
	 *            日期风格
	 * @return 日期字符串
	 */
	public static String DateToString(Date date, DateStyle dateStyle) {
		String dateString = null;
		if (dateStyle != null) {
			dateString = DateToString(date, dateStyle.getValue());
		}
		return dateString;
	}

	/**
	 * 将日期字符串转化为另一日期字符串。失败返回null。
	 * 
	 * @param date
	 *            旧日期字符串
	 * @param newPattern
	 *            新日期格式
	 * @return 新日期字符串
	 */
	public static String StringToString(String date, String newPattern) {
		DateStyle oldDateStyle = getDateStyle(date);
		return StringToString(date, oldDateStyle, newPattern);
	}

	/**
	 * 将日期字符串转化为另一日期字符串。失败返回null。
	 * 
	 * @param date
	 *            旧日期字符串
	 * @param newDateStyle
	 *            新日期风格
	 * @return 新日期字符串
	 */
	public static String StringToString(String date, DateStyle newDateStyle) {
		DateStyle oldDateStyle = getDateStyle(date);
		return StringToString(date, oldDateStyle, newDateStyle);
	}

	/**
	 * 将日期字符串转化为另一日期字符串。失败返回null。
	 * 
	 * @param date
	 *            旧日期字符串
	 * @param olddPattern
	 *            旧日期格式
	 * @param newPattern
	 *            新日期格式
	 * @return 新日期字符串
	 */
	public static String StringToString(String date, String olddPattern, String newPattern) {
		return DateToString(StringToDate(date, olddPattern), newPattern);
	}

	/**
	 * 将日期字符串转化为另一日期字符串。失败返回null。
	 * 
	 * @param date
	 *            旧日期字符串
	 * @param olddDteStyle
	 *            旧日期风格
	 * @param newParttern
	 *            新日期格式
	 * @return 新日期字符串
	 */
	public static String StringToString(String date, DateStyle olddDteStyle, String newParttern) {
		String dateString = null;
		if (olddDteStyle != null) {
			dateString = StringToString(date, olddDteStyle.getValue(), newParttern);
		}
		return dateString;
	}

	/**
	 * 将日期字符串转化为另一日期字符串。失败返回null。
	 * 
	 * @param date
	 *            旧日期字符串
	 * @param olddPattern
	 *            旧日期格式
	 * @param newDateStyle
	 *            新日期风格
	 * @return 新日期字符串
	 */
	public static String StringToString(String date, String olddPattern, DateStyle newDateStyle) {
		String dateString = null;
		if (newDateStyle != null) {
			dateString = StringToString(date, olddPattern, newDateStyle.getValue());
		}
		return dateString;
	}

	/**
	 * 将日期字符串转化为另一日期字符串。失败返回null。
	 * 
	 * @param date
	 *            旧日期字符串
	 * @param olddDteStyle
	 *            旧日期风格
	 * @param newDateStyle
	 *            新日期风格
	 * @return 新日期字符串
	 */
	public static String StringToString(String date, DateStyle olddDteStyle, DateStyle newDateStyle) {
		String dateString = null;
		if (olddDteStyle != null && newDateStyle != null) {
			dateString = StringToString(date, olddDteStyle.getValue(), newDateStyle.getValue());
		}
		return dateString;
	}

	/**
	 * 增加日期的年份。失败返回null。
	 * 
	 * @param date
	 *            日期
	 * @param yearAmount
	 *            增加数量。可为负数
	 * @return 增加年份后的日期字符串
	 */
	public static String addYear(String date, int yearAmount) {
		return addInteger(date, Calendar.YEAR, yearAmount);
	}

	/**
	 * 增加日期的年份。失败返回null。
	 * 
	 * @param date
	 *            日期
	 * @param yearAmount
	 *            增加数量。可为负数
	 * @return 增加年份后的日期
	 */
	public static Date addYear(Date date, int yearAmount) {
		return addInteger(date, Calendar.YEAR, yearAmount);
	}

	/**
	 * 增加日期的月份。失败返回null。
	 * 
	 * @param date
	 *            日期
	 * @param monthAmount
	 *            增加数量。可为负数
	 * @return 增加月份后的日期字符串
	 */
	public static String addMonth(String date, int monthAmount) {
		return addInteger(date, Calendar.MONTH, monthAmount);
	}

	/**
	 * 增加日期的月份。失败返回null。
	 * 
	 * @param date
	 *            日期
	 * @param monthAmount
	 *            增加数量。可为负数
	 * @return 增加月份后的日期
	 */
	public static Date addMonth(Date date, int monthAmount) {
		return addInteger(date, Calendar.MONTH, monthAmount);
	}

	/**
	 * 增加日期的天数。失败返回null。
	 * 
	 * @param date
	 *            日期字符串
	 * @param dayAmount
	 *            增加数量。可为负数
	 * @return 增加天数后的日期字符串
	 */
	public static String addDay(String date, int dayAmount) {
		return addInteger(date, Calendar.DATE, dayAmount);
	}

	/**
	 * 增加日期的天数。失败返回null。
	 * 
	 * @param date
	 *            日期
	 * @param dayAmount
	 *            增加数量。可为负数
	 * @return 增加天数后的日期
	 */
	public static Date addDay(Date date, int dayAmount) {
		return addInteger(date, Calendar.DATE, dayAmount);
	}

	/**
	 * 增加日期的小时。失败返回null。
	 * 
	 * @param date
	 *            日期字符串
	 * @param hourAmount
	 *            增加数量。可为负数
	 * @return 增加小时后的日期字符串
	 */
	public static String addHour(String date, int hourAmount) {
		return addInteger(date, Calendar.HOUR_OF_DAY, hourAmount);
	}

	/**
	 * 增加日期的小时。失败返回null。
	 * 
	 * @param date
	 *            日期
	 * @param hourAmount
	 *            增加数量。可为负数
	 * @return 增加小时后的日期
	 */
	public static Date addHour(Date date, int hourAmount) {
		return addInteger(date, Calendar.HOUR_OF_DAY, hourAmount);
	}

	/**
	 * 增加日期的分钟。失败返回null。
	 * 
	 * @param date
	 *            日期字符串
	 * @param minuteAmount
	 *            增加数量。可为负数
	 * @return 增加分钟后的日期字符串
	 */
	public static String addMinute(String date, int minuteAmount) {
		return addInteger(date, Calendar.MINUTE, minuteAmount);
	}

	/**
	 * 增加日期的分钟。失败返回null。
	 * 
	 * @param date
	 *            日期
	 *            增加数量。可为负数
	 * @return 增加分钟后的日期
	 */
	public static Date addMinute(Date date, int minuteAmount) {
		return addInteger(date, Calendar.MINUTE, minuteAmount);
	}

	/**
	 * 增加日期的秒钟。失败返回null。
	 * 
	 * @param date
	 *            日期字符串
	 *            增加数量。可为负数
	 * @return 增加秒钟后的日期字符串
	 */
	public static String addSecond(String date, int secondAmount) {
		return addInteger(date, Calendar.SECOND, secondAmount);
	}

	/**
	 * 增加日期的秒钟。失败返回null。
	 * 
	 * @param date
	 *            日期
	 *            增加数量。可为负数
	 * @return 增加秒钟后的日期
	 */
	public static Date addSecond(Date date, int secondAmount) {
		return addInteger(date, Calendar.SECOND, secondAmount);
	}

	/**
	 * 获取日期的年份。失败返回0。
	 * 
	 * @param date
	 *            日期字符串
	 * @return 年份
	 */
	public static int getYear(String date) {
		return getYear(StringToDate(date));
	}

	/**
	 * 获取日期的年份。失败返回0。
	 * 
	 * @param date
	 *            日期
	 * @return 年份
	 */
	public static int getYear(Date date) {
		return getInteger(date, Calendar.YEAR);
	}

	/**
	 * 获取日期的月份。失败返回0。
	 * 
	 * @param date
	 *            日期字符串
	 * @return 月份
	 */
	public static int getMonth(String date) {
		return getMonth(StringToDate(date));
	}

	/**
	 * 获取日期的月份。失败返回0。
	 * 
	 * @param date
	 *            日期
	 * @return 月份
	 */
	public static int getMonth(Date date) {
		return getInteger(date, Calendar.MONTH) + 1;
	}

	/**
	 * 获取日期的天数。失败返回0。
	 * 
	 * @param date
	 *            日期字符串
	 * @return 天
	 */
	public static int getDay(String date) {
		return getDay(StringToDate(date));
	}

	/**
	 * 获取日期的天数。失败返回0。
	 * 
	 * @param date
	 *            日期
	 * @return 天
	 */
	public static int getDay(Date date) {
		return getInteger(date, Calendar.DATE);
	}

	/**
	 * 获取日期的小时。失败返回0。
	 * 
	 * @param date
	 *            日期字符串
	 * @return 小时
	 */
	public static int getHour(String date) {
		return getHour(StringToDate(date));
	}

	/**
	 * 获取日期的小时。失败返回0。
	 * 
	 * @param date
	 *            日期
	 * @return 小时
	 */
	public static int getHour(Date date) {
		return getInteger(date, Calendar.HOUR_OF_DAY);
	}

	/**
	 * 获取日期的分钟。失败返回0。
	 * 
	 * @param date
	 *            日期字符串
	 * @return 分钟
	 */
	public static int getMinute(String date) {
		return getMinute(StringToDate(date));
	}

	/**
	 * 获取日期的分钟。失败返回0。
	 * 
	 * @param date
	 *            日期
	 * @return 分钟
	 */
	public static int getMinute(Date date) {
		return getInteger(date, Calendar.MINUTE);
	}

	/**
	 * 获取日期的秒钟。失败返回0。
	 * 
	 * @param date
	 *            日期字符串
	 * @return 秒钟
	 */
	public static int getSecond(String date) {
		return getSecond(StringToDate(date));
	}

	/**
	 * 获取日期的秒钟。失败返回0。
	 * 
	 * @param date
	 *            日期
	 * @return 秒钟
	 */
	public static int getSecond(Date date) {
		return getInteger(date, Calendar.SECOND);
	}

	/**
	 * 获取日期 。默认yyyy-MM-dd格式。失败返回null。
	 * 
	 * @param date
	 *            日期字符串
	 * @return 日期
	 */
	public static String getDate(String date) {
		return StringToString(date, DateStyle.YYYY_MM_DD);
	}

	/**
	 * 获取日期。默认yyyy-MM-dd格式。失败返回null。
	 * 
	 * @param date
	 *            日期
	 * @return 日期
	 */
	public static String getDate(Date date) {
		return DateToString(date, DateStyle.YYYY_MM_DD);
	}

	/**
	 * 获取日期的时间。默认HH:mm:ss格式。失败返回null。
	 * 
	 * @param date
	 *            日期字符串
	 * @return 时间
	 */
	public static String getTime(String date) {
		return StringToString(date, DateStyle.HH_MM_SS);
	}

	/**
	 * 获取日期的时间。默认HH:mm:ss格式。失败返回null。
	 * 
	 * @param date
	 *            日期
	 * @return 时间
	 */
	public static String getTime(Date date) {
		return DateToString(date, DateStyle.HH_MM_SS);
	}

	/**
	 * 获取日期的星期。失败返回null。
	 * 
	 * @param date
	 *            日期字符串
	 * @return 星期
	 */
	public static Week getWeek(String date) {
		Week week = null;
		DateStyle dateStyle = getDateStyle(date);
		if (dateStyle != null) {
			Date myDate = StringToDate(date, dateStyle);
			week = getWeek(myDate);
		}
		return week;
	}

	/**
	 * 获取日期的星期。失败返回null。
	 * 
	 * @param date
	 *            日期
	 * @return 星期
	 */
	public static Week getWeek(Date date) {
		Week week = null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int weekNumber = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		switch (weekNumber) {
		case 0:
			week = Week.SUNDAY;
			break;
		case 1:
			week = Week.MONDAY;
			break;
		case 2:
			week = Week.TUESDAY;
			break;
		case 3:
			week = Week.WEDNESDAY;
			break;
		case 4:
			week = Week.THURSDAY;
			break;
		case 5:
			week = Week.FRIDAY;
			break;
		case 6:
			week = Week.SATURDAY;
			break;
		}
		return week;
	}

	/**
	 * 获取两个日期相差的天数
	 * 
	 * @param date
	 *            日期字符串
	 * @param otherDate
	 *            另一个日期字符串
	 * @return 相差天数。如果失败则返回-1
	 */
	public static int getIntervalDays(String date, String otherDate) {
		return getIntervalDays(StringToDate(date), StringToDate(otherDate));
	}

	/**
	 * @param date
	 *            日期
	 * @param otherDate
	 *            另一个日期
	 * @return 相差天数。如果失败则返回-1
	 */
	public static int getIntervalDays(Date date, Date otherDate) {
		int num = -1;
		Date dateTmp = DateUtil.StringToDate(DateUtil.getDate(date), DateStyle.YYYY_MM_DD);
		Date otherDateTmp = DateUtil.StringToDate(DateUtil.getDate(otherDate), DateStyle.YYYY_MM_DD);
		if (dateTmp != null && otherDateTmp != null) {
			long time = Math.abs(dateTmp.getTime() - otherDateTmp.getTime());
			num = (int) (time / (24 * 60 * 60 * 1000));
		}
		return num;
	}

	public static long getSecondByString(String t, String pattern) {
		// 传入的参数要与yyyyMMddHH的格式相同 "yyyyMMddHH"
		SimpleDateFormat simpledateformat = new SimpleDateFormat(pattern, Locale.SIMPLIFIED_CHINESE);
		Date date = null;
		try {
			date = simpledateformat.parse(t);// 将参数按照给定的格式解析参数
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println(date.getTime());
		return date.getTime();
	}

	public enum DateStyle {

		YYYY_MM("yyyy-MM", false), YYYY_MM_DD("yyyy-MM-dd", false), YYYY_MM_DD_HH_MM("yyyy-MM-dd HH:mm",
				false), YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss", false),

		YYYY_MM_EN("yyyy/MM", false), YYYY_MM_DD_EN("yyyy/MM/dd", false), YYYY_MM_DD_HH_MM_EN(
				"yyyy/MM/dd HH:mm", false), YYYY_MM_DD_HH_MM_SS_EN("yyyy/MM/dd HH:mm:ss", false),

		YYYY_MM_CN("yyyy年MM月", false), YYYY_MM_DD_CN("yyyy年MM月dd日", false), YYYY_MM_DD_HH_MM_CN(
				"yyyy年MM月dd日 HH:mm", false), YYYY_MM_DD_HH_MM_SS_CN("yyyy年MM月dd日 HH:mm:ss", false),

		HH_MM("HH:mm", true), HH_MM_SS("HH:mm:ss", true),

		MM_DD("MM-dd", true), MM_DD_HH_MM("MM-dd HH:mm", true), MM_DD_HH_MM_SS("MM-dd HH:mm:ss", true),

		MM_DD_EN("MM/dd", true), MM_DD_HH_MM_EN("MM/dd HH:mm", true), MM_DD_HH_MM_SS_EN("MM/dd HH:mm:ss",
				true),

		MM_DD_CN("MM月dd日", true), MM_DD_HH_MM_CN("MM月dd日 HH:mm", true), MM_DD_HH_MM_SS_CN("MM月dd日 HH:mm:ss",
				true);

		private String value;

		private boolean isShowOnly;

		DateStyle(String value, boolean isShowOnly) {
			this.value = value;
			this.isShowOnly = isShowOnly;
		}

		public String getValue() {
			return value;
		}

		public boolean isShowOnly() {
			return isShowOnly;
		}
	}

	public enum Week {

		MONDAY("星期一", "Monday", "Mon.", 1), TUESDAY("星期二", "Tuesday", "Tues.", 2), WEDNESDAY("星期三",
				"Wednesday", "Wed.", 3), THURSDAY("星期四", "Thursday", "Thur.", 4), FRIDAY("星期五", "Friday",
				"Fri.", 5), SATURDAY("星期六", "Saturday", "Sat.", 6), SUNDAY("星期日", "Sunday", "Sun.", 7);

		String name_cn;
		String name_en;
		String name_enShort;
		int number;

		Week(String name_cn, String name_en, String name_enShort, int number) {
			this.name_cn = name_cn;
			this.name_en = name_en;
			this.name_enShort = name_enShort;
			this.number = number;
		}

		public String getChineseName() {
			return name_cn;
		}

		public String getName() {
			return name_en;
		}

		public String getShortName() {
			return name_enShort;
		}

		public int getNumber() {
			return number;
		}
	}
}
