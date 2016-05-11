package com.systop.core.dao.jpa;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import com.systop.core.entity.BaseEntity;
import com.systop.core.utils.Reflections;

@Repository
public class BaseJPADao {
	
	@PersistenceContext 
	private EntityManager em;
	
	@Transactional
	public void save(Object entity){
		em.persist(entity);
	}
	
	@Transactional
	public void update(Object entity){
		em.merge(entity);
	}
	
	
	public <T> T find(Class<T> beanClass, Serializable id) {
		return em.find(beanClass, id);
	}
	
	@Transactional
	public void remove(Object obj) {
		if (obj != null) {
			em.remove(obj);
		}
	}
	
	/**
	 * 执行查询语句进行修改/删除操作.
	 *
	 * @param queryString 批量执行语句
	 * @param values 匹配参数
	 * @return 更新记录数
	 */
	@Transactional
	public int executeUpdate(final String queryString, final Object... values) {
		Assert.hasText(queryString, "queryString Can not NULL");
		return buildQueryParams(queryString, values).executeUpdate();
	}

	/**
	 * 执行查询语句进行修改/删除操作.
	 * 
	 * @param queryString 批量执行语句
	 * @param values 匹配参数
	 * @return 更新记录数.
	 */
	@Transactional
	public int executeUpdate(final String queryString, final Map<String,Object> values) {
		Assert.hasText(queryString, "queryString Can not NULL");
		return buildQuery(queryString, values).executeUpdate();
	}
	/**
	 * 查询是否有重复值 
	 * @param obj
	 * @param names
	 * @return
	 */
	public boolean exists(BaseEntity obj, String... names){
		if (obj == null || names == null){
			return false;
		}
		String entityName = obj.getClass().getSimpleName();
		StringBuilder hql = new StringBuilder("from " + entityName + " where ");
		Map<String, Object> args = new HashMap<String, Object>();
		for(String p : names){
			hql.append(p).append("=:").append(p).append(" and ");
			args.put(p, Reflections.invokeGetter(obj, p));
		}
		if(obj.getId() != null){//id不为空排除自身
			hql.append("id <> :id");
			args.put("id", obj.getId());
		}else{
			hql.delete(hql.length() - 5, hql.length());
		}
		
		Query query = buildQuery(hql.toString(), args);
		return query.getResultList().size() > 0;
	}
	
	@SuppressWarnings("rawtypes")
	public List query(String hql, Map<String, Object> args) {
		return buildQuery(hql, args).getResultList();
	}
	
	@SuppressWarnings("rawtypes")
	public List queryParams(String hql,Object...args){
		return buildQueryParams(hql,args).getResultList();
	}
	
	// 构建查询对象
	private Query buildQuery(String hql, Map<String, Object> args) {
		Query query = em.createQuery(hql);
		if (args != null) {
			Set<Entry<String, Object>> entrys = args.entrySet();
			for(Entry<String, Object> entry : entrys){
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
		return query;
	}
	
	private Query buildQueryParams(String hql,Object...args){
		Query query = em.createQuery(hql);
		if (args.length>0) {
			for(int i=0;i<args.length;i++){
				query.setParameter(i+1,args[i]);
			}
		}
		return query;
	}
	
	public Page pageQuery(final Page page, String hql, Map<String, Object> args) {
		// 统计查询
		page.setTotal(count(hql, args));
		if (page.getTotal() == 0) {
			return Page.EMPTY_PAGE;
		}
		// 创建查询
		Query query = buildQuery(hql, args);
		query.setFirstResult(page.getStartIndex());
		query.setMaxResults(page.getPageSize());
		page.setData(query.getResultList());
		return page;
	}
	public Page pageQueryParams(final Page page, String hql,Object...args) {
		// 统计查询
		page.setTotal(countParams(hql, args));
		if (page.getTotal() == 0) {
			return Page.EMPTY_PAGE;
		}
		// 创建查询
		Query query = buildQueryParams(hql, args);
		query.setFirstResult(page.getStartIndex());
		query.setMaxResults(page.getPageSize());
		page.setData(query.getResultList());
		return page;
	}

	@SuppressWarnings("rawtypes")
	public int countParams(String oriHql,Object...args) {
		String sql = "select count (*) "
				+ removeSelect(removeOrders(removeFetchs(oriHql)));

		List countList = queryParams(sql, args);
		int rows = 0;
		if (countList.size() > 1) {
			rows = countList.size(); // Group By的情景...
		} else if (countList.size() == 1) {
			rows = ((Long) countList.get(0)).intValue();
		}
		return rows;
	}
	
	@SuppressWarnings("rawtypes")
	public int count(String oriHql, Map<String, Object> args) {
		String sql = "select count (*) "
				+ removeSelect(removeOrders(removeFetchs(oriHql)));

		List countList = query(sql, args);
		int rows = 0;
		if (countList.size() > 1) {
			rows = countList.size(); // Group By的情景...
		} else if (countList.size() == 1) {
			rows = ((Long) countList.get(0)).intValue();
		}
		return rows;
	}

	/**
	 * 去除jql的select 子句，未考虑union的情况
	 */
	public String removeSelect(String jql) {
		int beginPos = jql.toLowerCase().indexOf("from");
		return jql.substring(beginPos);
	}

	/**
	 * 去除jql的order by子句
	 */
	public String removeOrders(String jql) {
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(jql);
		StringBuffer buffer = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(buffer, "");
		}
		m.appendTail(buffer);
		return buffer.toString();
	}

	/**
	 * 删除JQL中的fetch
	 */
	public String removeFetchs(String jql) {
		return jql.replaceAll("fetch ", "");
	}

	public EntityManager getEm() {
		return em;
	}


	public void setEm(EntityManager em) {
		this.em = em;
	}
	
	public static String getClassName(Object o){
		return o.getClass().getSimpleName();
	}
	
}
