package com.systop.core.utils;

import org.apache.commons.lang3.StringUtils;

public final class StringUtil {

	/**
	 * 将一个数字转换为字符串，并按传入的length做补0
	 * @param serial	待转换的数字
	 * @param length	返回字符串的长度
	 * @return
	 */
	public static String zeroPadding(Integer serial, Integer length){
		if(serial == null){
			return StringUtils.leftPad("", length, "0");
		}
		
		String serialNo = serial.toString();
		return StringUtils.leftPad(serialNo, length, "0");
	}
	
	/**
	 * 截取字符串
	 * @param str	待处理的字符串
	 * @param begin	开始位置
	 * @param end	结束位置
	 * @return	如果传入字符串为空 则返回 null
	 */
	public static String subString(String str, Integer begin, Integer end){
		if(StringUtils.isBlank(str)){
			return null;
		}else{
			return str.substring(begin, end);
		}
	}
	
	/**
	 * 将字符串转换为数字
	 * @param serial	带转换的字符串
	 * @return			如果传入的字符串为空或不能转换为数字则返回null
	 */
	public static Integer getNumberFromString(String serial){
		if(StringUtils.isBlank(serial) || !StringUtils.isNumeric(serial)){
			return null;
		}
		return Integer.valueOf(serial);
	}
	/**
	 * 将Long数组进行逗号分隔并返回
	 * @param ids
	 * @return  如:22,33,55
	 * @author zhangpeiran 2016年5月9日 上午10:45:53
	 */
	public static String getSplitComma(Long[] ids){
		String inStr = "";
		for(Long id:ids){
			if(inStr.length()>0)
				inStr +=","+id;
			else 
				inStr +=id;
		}
		return inStr;
	}
}
