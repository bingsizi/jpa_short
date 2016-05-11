package com.systop.core.dao.jpa.search;

import org.hibernate.criterion.MatchMode;

import com.systop.core.Constants;

public class SearchFilter {

	public String fieldName;

	public String operator;

	public Object value;

	public String paramName;

	public SearchFilter(String fieldName, String operator, Object value) {
		this.fieldName = fieldName;
		this.operator = operator.toUpperCase();
		if(Constants.LIKE.equals(this.operator)){
			this.value = MatchMode.ANYWHERE.toMatchString(value.toString());
		}else{
			this.value = value;
		}
		paramName = fieldName.replace(".", "_");
	}

	public String getSearchStr() {
		StringBuilder searchStr = new StringBuilder(fieldName);
		searchStr.append(Constants.OPT_MAPS.get(operator)).append(":").append(paramName);
		return searchStr.toString();
	}
}
