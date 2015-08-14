package com.jin91.preciousmetal.util;

public class MathUtil {

	/**
	 * @Description:最多保留两位小数，字符串最长为6
	 * @param value
	 * @return
	 * @author: qiaoshl
	 * @date:2014-5-20
	 */
	public static String keepDecimal(double value, int length) {
		if (String.valueOf(value).length() > length) {
			String roundValue = String.format("%.2f", value);
			if (roundValue.length() > length) {
				roundValue = String.format("%.1f", value);
				if (roundValue.length() > length) {
					roundValue = String.format("%.0f", value);
				}
			}
			return roundValue;
		} else {
			return String.valueOf(value);
		}
	}

	public static String keep2Decimal(double value) {
		return String.format("%.2f", value);
	}
}
