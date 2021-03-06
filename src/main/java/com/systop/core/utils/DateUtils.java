/**
 * Copyright 2008 - 2011 Simcore.org.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.systop.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

/**
 * 日期、时间工具类
 * <p>
 * 集成Commons-Lang对时间、日期的操作方法.
 * 
 * @author ray
 */
public class DateUtils {
	/**
	 * 比较两个日期是否相同，不包含时间
	 * 
	 * @param start
	 *            日期
	 * @param end
	 *            日期
	 * @return 负数：start<end；0：start=end；整数：start>end
	 */
	public static int compare(Date start, Date end) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(start);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(end);
		int year1 = c1.get(Calendar.YEAR);
		int year2 = c2.get(Calendar.YEAR);
		if (year1 != year2) {
			return year1 - year2;
		}
		int month1 = c1.get(Calendar.MONTH);
		int month2 = c2.get(Calendar.MONTH);
		if (month1 != month2) {
			return month1 - month2;
		}
		int day1 = c1.get(Calendar.DAY_OF_MONTH);
		int day2 = c2.get(Calendar.DAY_OF_MONTH);
		return day1 - day2;
	}

	/**
	 * 获得系统当前时间
	 * 
	 * @return
	 */
	public static Date getSystemTime() {
		return new Date();
	}

	/**
	 * 将日期类型转为字符串,按照 yyyy-MM-dd HH:mm:ss 格式转换
	 * 
	 * @param strDate
	 *            字符串型日期
	 * @param format
	 *            转换格
	 * @return
	 */
	public static String dateStringFormat(Date date, String format) {
		if (date == null)
			return "";
		SimpleDateFormat sdf = null;
		if (StringUtils.isNotBlank(format))
			sdf = new SimpleDateFormat(format);
		else
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	/**
	 * 将日期类型转为字符串,按照 yyyy-MM-dd HH:mm:ss 格式转换
	 * 
	 * @param strDate
	 *            字符串型日期
	 * @param format
	 *            转换格
	 * @return
	 */
	public static String dateStringFormat(Date strDate) {
		if (strDate == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(strDate);
	}

	/**
	 * 将日期类型转为字符串,按照 yyyyMMddHHmmss 格式转换
	 * 
	 * @param strDate
	 *            字符串型日期
	 * @param format
	 *            转换格
	 * @return
	 */
	public static String dateStringFormatF(Date strDate) {
		if (strDate == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(strDate);
	}

	/**
	 * 将字符串转为日期类型,format 为null时,按照 yyyy-MM-dd HH:mm:ss 格式转换
	 * 
	 * @param strDate
	 *            字符串型日期
	 * @param format
	 *            转换格
	 * @return
	 */
	public static Date stringDateFormat(String strDate, String format) {
		if (!StringUtils.isNotBlank(strDate)) {
			return null;
		}
		SimpleDateFormat sdf = null;
		if (format == null)
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		else
			sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(strDate);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获得参考日期前一个交易日(周六日不计).
	 * 
	 * @param date
	 *            参考日期
	 * @return Date 参考日期前一个交易日
	 */
	public static Date getBeforeTrad(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		do {
			calendar.roll(Calendar.DATE, false);
		} while (calendar.get(Calendar.DAY_OF_WEEK) == 1 || calendar.get(Calendar.DAY_OF_WEEK) == 7);

		return calendar.getTime();
	}
	
	/**
	 * 获得参考日期前一天
	 * 
	 * @param date  参考日期
	 * @return Date 参考日期前一天
	 * @author ma
	 */
	public static Date getBeforeDate(Date date) {
		
		Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        calendar.add(Calendar.DAY_OF_MONTH, -1);  
		
		return calendar.getTime();
	}

	/**
	 * 返回两个时间相减的分钟数
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static long dateReduction(Date start, Date end) {
		long l = end.getTime() - start.getTime();
		return l / 1000 / 60;
	}

	/**
	 * 取得参考时间（若为空则取当前时间）加上指定秒
	 * 
	 * @param date
	 * @param second
	 * @return date
	 * @authorJiaYunqi
	 */
	public static Date getAddSecond(Date date, int second) {
		Calendar calendar = Calendar.getInstance();
		if (date != null) {
			calendar.setTime(date);
		}
		calendar.add(Calendar.SECOND, second);
		return calendar.getTime();
	}

	/**
	 * 取得当前时间之前分钟的时间
	 * 
	 * @param time
	 * @return
	 */
	public static Date getBeforeDate(int time) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE, (-1 * time));
		return c.getTime();
	}
    /**
     * 根据年月,获得这个月的最后一天
     * @param year
     * @param month
     * @return
     * @author zhangpeiran 2016年4月25日 下午1:34:44
     */
	public static int getMaxDayByYearMonth(int year,int month) {
		Calendar calendar = Calendar.getInstance(); 
		calendar.set(year,month - 1,1);
		return calendar.getActualMaximum(Calendar.DATE);
	}
	/**
	 * 验证指定时间是否在两个时间之间,精确到日
	 * 
	 * @param dest
	 * @param start
	 * @param end
	 * @return boolean
	 * @author zhangpeiran 2016年4月25日 下午1:46:44
	 */
	@SuppressWarnings("deprecation")
	public static boolean isBetween(Date dest,Date start, Date end) {
		if (start == null || end == null || dest == null) {
			return false;
		}
		dest = new Date(dest.getYear(),dest.getMonth(),dest.getDate());
		start = new Date(start.getYear(),start.getMonth(),start.getDate());
		end = new Date(end.getYear(),end.getMonth(),end.getDate());

		return dest.getTime()>=start.getTime()&&dest.getTime()<=end.getTime();
	}
	/**
	 * 根据年月日获得date
	 * @param fullYear
	 * @param month
	 * @param day
	 * @return
	 * @author zhangpeiran 2016年4月25日 下午2:17:58
	 */
	@SuppressWarnings("deprecation")
	public static Date getDate(int fullYear,int month,int day){
		return new Date(fullYear-1900,month-1,day);
	}
}
