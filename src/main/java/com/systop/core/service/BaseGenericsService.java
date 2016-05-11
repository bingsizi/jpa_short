package com.systop.core.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.catalina.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import com.systop.core.dao.jpa.BaseJPADao;
import com.systop.core.dao.jpa.Page;
import com.systop.core.dao.jpa.search.SearchFilter;
import com.systop.core.entity.BaseEntity;
import com.systop.core.utils.GenericsUtil;

public class BaseGenericsService<T extends BaseEntity> implements BaseService<T> {

	protected Class<T> entityClass;

	@Autowired
	protected BaseJPADao baseDao;
	
	@Autowired
	protected JdbcTemplate jdbcTemplate;

	/**
	 * @see Manager#find(java.io.Serializable)
	 */
	@Override
	public T find(Serializable id) {
		return baseDao.find(getEntityClass(), id);
	}

	@Override
	public List<T> findAll() {
		return query("from "+getEntityClass().getSimpleName(),new HashMap<String,Object>());
	}
	/**
	 * @ses {@link Manager#save(Object)})
	 */
	@Override
	public void save(T entity) {
		if (entity.getId() == null) {
			baseDao.save(entity);
		} else {
			baseDao.update(entity);
		}
	}

	/**
	 * @ses {@link Manager#delete(Object)}
	 */
	@Override
	public void remove(T entity) {
		baseDao.remove(entity);
	}

	@Override
	public int executeUpdate(String queryString, Object... values) {
		return baseDao.executeUpdate(queryString, values);
	}

	@Override
	public int executeUpdate(String queryString, Map<String,Object> values) {
		return baseDao.executeUpdate(queryString, values);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	public List<T> queryParams(String jql, Object... args) {
		return (List<T>) baseDao.queryParams(jql, args);
	}

	@Override
	@Deprecated
	public Page pageQueryParams(Page page, String jql, Object... args) {
		return baseDao.pageQueryParams(page, jql, args);
	}
	/**
	 * @see Manager#query(String, Object...)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<T> query(String jql, Map<String, Object> args) {
		return (List<T>) baseDao.query(jql, args);
	}

	/**
	 * @see Manager#find(Serializable)
	 */
	@Override
	public T find(String jql, Map<String, Object> args) {
		List<T> result = query(jql, args);
		return (result.isEmpty()) ? null : result.get(0);
	}

	@SuppressWarnings("unchecked")
	protected Class<T> getEntityClass() {
		if (entityClass == null) {
			entityClass = GenericsUtil.getGenericClass(getClass());
		}
		return entityClass;
	}

	@Override
	public Page pageQuery(Page page, String jql, Map<String, Object> args) {
		return baseDao.pageQuery(page, jql, args);
	}

	public Page pageQuery(Page page, List<SearchFilter> filters) {
		StringBuilder query = new StringBuilder(128);
		query.append("from ").append(getEntityClass().getSimpleName()).append(" where ");
		int whereInt = query.length();
		Map<String, Object> args = new HashMap<String, Object>();
		for(SearchFilter filter : filters){
			query.append(filter.getSearchStr()).append(" and ");
			args.put(filter.paramName, filter.value);
		}
		if (query.length() == whereInt) {//相等代表无where条件
			query.delete(query.length() - 6, query.length());//去掉[where ] 
		}else{
			query.delete(query.length() - 4, query.length());//去掉[and ]
		}
		query.append(page.getOrderStr());	
		return pageQuery(page, query.toString(), args);
	}
	
	/**
	 * 查询是否有重复值 
	 * @param obj
	 * @param names
	 * @return
	 */
	public boolean exists(T t, String... names){
		return baseDao.exists(t, names);
	}
	

	public BaseJPADao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseJPADao baseDao) {
		this.baseDao = baseDao;
	}
}
