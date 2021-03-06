package com.systop.base.security.organization.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.systop.base.security.organization.entity.Organization;
import com.systop.base.security.organization.support.OrgTreeGridVO;
import com.systop.core.Constants;
import com.systop.core.controller.vo.TreeVo;
import com.systop.core.service.BaseGenericsService;
import com.systop.core.utils.DateUtils;

/**  
 * 组织机构业务方法
 * @company 新龙科技
 * @author zhangpeiran
 * @version 
 * @date 2016年5月12日 上午8:48:39
 */
@Service("organizationService")
public class OrganizationService extends BaseGenericsService<Organization>{
	
	/**
	 * 保存部门方法
	 */
	@Override
	public void save(Organization org) {
		if(org.getCreateTime()==null){
			org.setCreateTime(DateUtils.getSystemTime());
		}
		if(org.getParentId()!=null){
			Organization parentOrg = find(org.getParentId());
			if(parentOrg!=null){
				String parentIds = "";
				if(StringUtils.isNoneBlank(parentOrg.getParentIds())){
					parentIds = parentOrg.getParentIds()+parentOrg.getId()+"/";
				}else{
					parentIds = "/"+parentOrg.getId()+"/";
				}
				org.setParentIds(parentIds);
			}
		}
		super.save(org);
	}
	/**
	 * 根据id删除
	 * @param id
	 * @author zhangpeiran 2016年5月12日 上午9:44:22
	 */
	public void deleteById(Long id){
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("id",id);
		paramMap.put("available",Constants.NO);
		super.executeUpdate("update Organization set available = :available where id = :id", paramMap);
	}
	/**
	 * 重写findId
	 */
	@Override
	public Organization find(Serializable id) {
		Organization org = super.find(id);
		if(org!=null){
			if(org.getAvailable().trim().equals(Constants.YES)){
				return org;
			}
		}
		return null;
	}
	/**
	 * 根据id获得子treegrid数据
	 * @param id
	 * @param compId
	 * @return
	 * @author gql 2015-9-22 上午11:29:51
	 */
	public List<OrgTreeGridVO> orgTreeGridList(Long id){
		List<OrgTreeGridVO> otgvList = new ArrayList<OrgTreeGridVO>();
		List<Organization> list = new ArrayList<Organization>();
		// 如果是根节点部门
		if (id==null) {
			list = findRoot();
		} else {
			list = findChilds(id);
		}
		for (Organization org : list) {
			OrgTreeGridVO otgv = new OrgTreeGridVO();
			BeanUtils.copyProperties(org, otgv,new String[]{"id"});
			otgv.set_parentId(org.getParentId()+"");
			otgv.setId(org.getId()+"");
			boolean flag = isLeafNode(org.getId());
			if (flag)
				otgv.setState("closed");
			else
				otgv.setState("open");
			otgvList.add(otgv);
		}
		return otgvList;
	}
	/**
	 * 一次性全部获取部门树结构的方法,如果id有值以当前id为七点
	 * @param id
	 * @return
	 * @author zhangpeiran 2016年5月12日 上午9:09:27
	 */
	public List<TreeVo> orgTreeList(Long id){
		List<TreeVo> list = new ArrayList<TreeVo>();
		//获得根节点
		List<Organization> rootList = new ArrayList<>();
		if(id==null){
			rootList = findRoot();
		}else{
			rootList.add(super.find(id));
		}
		if (!rootList.isEmpty()) {
			for (Organization org : rootList) {
				TreeVo tv = new TreeVo();
				tv.setId(org.getId()+"");
				tv.setText(org.getName());
				setChildrenList(org, tv);
				list.add(tv);
			}
		}
		return list;
	}
	/**
	 * 设置子部门List
	 * @param org
	 * @param deptTreeVo
	 * @return
	 * @author zhangpeiran 2016年5月12日 上午9:14:57
	 */
	private Organization setChildrenList(Organization org, TreeVo deptTreeVo) {
		List<Organization> childrenList = findChilds(org.getId());
		if (childrenList.size() > 0) {
			List<TreeVo> childTreeList = new ArrayList<TreeVo>();
			for (Organization subOrg : childrenList) {
				TreeVo tv = new TreeVo();
				tv.setId(subOrg.getId()+"");
				tv.setText(subOrg.getName());
				childTreeList.add(tv);
				setChildrenList(subOrg, tv);
			}
			deptTreeVo.setChildren(childTreeList);
		}
		return org;
	}
	/**
	 * 查询所有子部门，不包含本部门
	 * @param departId
	 * @return
	 * @author gql 2015-10-13 下午3:20:11
	 */
	public List<Organization> findAllChilds(Long id){
		List<Organization> returnList = new ArrayList<Organization>();
		fillChildData(id, returnList);
		return returnList;
	}
	/**
	 * 遍历部门
	 * @param departId
	 * @param returnList
	 * @author gql 2015-10-13 下午3:42:09
	 */
	private void fillChildData(Long id,List<Organization> returnList){
		List<Organization> list = findChilds(id);
		if(null != list && list.size()>0){
			for (Organization org : list) {
				returnList.add(org);
				findAllChilds(org.getId());
			}
		}
	}
	/**
	 * 获得根节点
	 * @return
	 * @author zhangpeiran 2016年5月12日 上午8:57:21
	 */
	public List<Organization> findRoot(){
		Map<String,Object> map = new HashMap<>();
		map.put("available",Constants.YES);
		return super.query("from Organization where parentId is null and available=:available",map);
	}
	/**
	 * 根据id获得子节点
	 * @param id
	 * @return
	 * @author zhangpeiran 2016年5月12日 上午8:59:13
	 */
	public List<Organization> findChilds(Long id){
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("parentId",id);
		paramMap.put("available",Constants.YES);
		return super.query("from Organization where parentId=:parentId and available=:available",paramMap);
	}
	/**
	 * 判断这个id节点下是否含有子节点.
	 * @param id
	 * @return
	 * @author zhangpeiran 2016年5月12日 上午9:02:11
	 */
	public boolean isLeafNode(Long id){
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("parentId",id);
		paramMap.put("available",Constants.YES);
	    int count = baseDao.count("from Organization where parentId=:parentId and available=:available",paramMap);
	    return (count>0)?true:false;
	}
}
