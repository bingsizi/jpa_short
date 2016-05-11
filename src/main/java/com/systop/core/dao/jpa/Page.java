package com.systop.core.dao.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.systop.core.Constants;

/**
 * 分页对象.包含数据及分页信息.
 * 
 * @author Sam
 */

@SuppressWarnings({ "serial", "rawtypes" })
public final class Page implements Serializable {

	/** 第一页 */
	public static final int FIRST_PAGE_INDEX = 1;

	/** 空Page对象 */
	public static final Page EMPTY_PAGE = new Page();

	/** 当前页第一条数据的位置,从0开始 */
	private int startIndex = 0;

	/** 每页的记录数 */
	private int pageSize = Constants.DEF_PAGE_SIZE;

	/** 当前页中存放的记录 */
	private List data;

	/** 总记录数 */
	private int total = 0;
	
	private String direction = Constants.DESC;

	private List<String> sortProperties = null;
	/**
	 * 构造方法，只构造空页
	 */
	public Page() {
		data = Collections.emptyList();
	}

	/**
	 * 构造方法，通常用于执行分页查询前构建分页信息。常见的用法：<br>
	 * 
	 * @param startIndex
	 *            起始行索引，从0开始。
	 * @param pageSize
	 *            本页容量。
	 */
	public Page(int pageNumber, int pageSize) {
		this.startIndex = start(pageNumber, pageSize);
		this.pageSize = pageSize;
	}

	/**
	 * 取数据库中包含的总记录数
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * 取总页数
	 */
	public int getPages() {
		if (total % pageSize == 0) {
			return total / pageSize;
		} else {
			return total / pageSize + 1;
		}
	}


	/**
	 * 取每页数据容量
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 获取当前页码,如果查询结果为空(Empty List),则总是返回1
	 * 
	 * @return 第一页是1，第二页是2...
	 */
	public int getPageNo() {
		return (startIndex / pageSize) + 1;
	}

	/**
	 * 是否有下一页
	 */
	public boolean getHasNextPage() {
		return this.getPageNo() < this.getPages();
	}

	/**
	 * 是否有上一页
	 */
	public boolean getHasPreviousPage() {
		return (this.getPageNo() > 1);
	}

	/**
	 * 获取任一页第一条数据的位置,startIndex从0开始
	 */
	public static int start(int pageNumber, int pageSize) {
		return (pageNumber - 1) * pageSize;
	}
    /**
     * 获得list数据
     * @return
     * @author zhangpeiran 2016年4月28日 上午9:48:57
     */
	public List getData() {
		return data;
	}
    /**
     * 获得list数据,主要用于easyui grid page 插件
     * @return
     * @author zhangpeiran 2016年4月28日 上午9:48:57
     */
	public List getRows(){
		return getData();
	}
	
	public void setData(List data) {
		this.data = data;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getStartIndex() {
		if (startIndex < 0) {
			return 0;
		}
		return startIndex;
	}
	
	/**
	 * 添加排序属性
	 * @param dir 排序方式，ASC/DESC
	 * @param sorts 排序的属性字段，以','分割
	 */
	public void addOrders(String dir, String sorts){
		this.direction = dir;
		if(StringUtils.isNoneBlank(sorts)){
			if (sortProperties == null){
				sortProperties = new ArrayList<String>();
			}
			for(String sort : sorts.split(",")){
				sortProperties.add(sort);
			}
		}
	}
	
	public String getOrderStr(){
		if (StringUtils.isBlank(direction)){
			direction =  Constants.DESC;
		}
		if (sortProperties != null && sortProperties.size() > 0) {
			StringBuilder order = new StringBuilder("order by ");
			for(String sort : sortProperties){
				order.append(sort).append(" ").append(direction).append(", ");
			}
			order.delete(order.length() -2 , order.length());
			return order.toString();
		}else{
			return "";
		}
	}
}
