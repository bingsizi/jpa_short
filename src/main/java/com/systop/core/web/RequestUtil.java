package com.systop.core.web;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

/**
 * 基于SpringSide4的Servlets.java
 * 
 * @author NiceLunch
 */
public class RequestUtil {
	
	private static String PRE_FIX = "Search_";

	/**
	 * 取得带相同前缀的Request Parameters, copy from spring WebUtils.
	 * 
	 * 返回的结果的Parameter名已去除前缀.
	 */
	public static Map<String, Object> getSearchParameters(
			ServletRequest request) {

		Validate.notNull(request, "Request must not be null");
		
		Map<String, Object> params = new TreeMap<String, Object>();
		// 获得request中所有的参数名称
		Enumeration<String> paramNames = request.getParameterNames();
		while (paramNames != null && paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			if (paramName.startsWith(PRE_FIX)) {
				String unprefixed = paramName.substring(PRE_FIX.length());
				String values = request.getParameter(paramName);
				if (StringUtils.isNotBlank(values)) {
					params.put(unprefixed, values);
				}
			}
		}
		return params;
	}

	/**
	 * 组合Parameters生成Query String的Parameter部分, 并在Paramter name上加上PRE_FIX.
	 * 
	 * @see #getSearchParameters
	 */
	public static String encodeSearchParameter(
			Map<String, Object> params) {
		if (params == null || params.isEmpty()) {
			return "";
		}
		StringBuilder queryStr = new StringBuilder();
		
		Iterator<Entry<String, Object>> it = params.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			queryStr.append(PRE_FIX).append(entry.getKey())
					.append('=').append(entry.getValue());
			if (it.hasNext()) {
				queryStr.append('&');
			}
		}
		return queryStr.toString();
	}

	
	public static void main(String[] args){
		
		Map<String, Object> parmMap = new HashMap<String, Object>();
		parmMap.put("LIKE_name", "name");
		parmMap.put("EQ_pwd", "pwd");
		parmMap.put("EQ_loginId", "loginId");
		
		Set<String> pset = parmMap.keySet();
		Iterator<String> i = pset.iterator();
		StringBuilder jql = new StringBuilder();
		String alix = "u";
		String point = ".";
		alix = alix + point;
		jql.append("from User u where ");
		while(i.hasNext()){
			String temp = i.next();
			String [] tarray = temp.split("_");
			jql.append(alix).append(tarray[1]).append(" " + tarray[0]).append(" ? & ");
		}
		
		System.out.println(jql.toString());
		
	}
}
