package com.systop.base.security.menu.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.systop.base.security.menu.entity.Menu;
import com.systop.base.security.menu.entity.RoleMenu;
import com.systop.base.security.menu.support.MenuSeqComparator;
import com.systop.core.controller.vo.TreeVo;
import com.systop.core.dao.jpa.Page;
import com.systop.core.service.BaseGenericsService;
import com.systop.core.utils.StringUtil;

/**  
 * 资源业务层
 * @company 新龙科技
 * @author zhangpeiran
 * @version 
 * @date 2016年5月9日 上午11:03:36
 */
@Service
public class MenuService extends BaseGenericsService<Menu>{
	
	@Resource
	private RoleMenuService roleMenuService;
	
	/**
	 * 保存菜单
	 */
	@Override
	public void save(Menu menu){
		if(menu.getParentId()!=null){
			Menu pareMenu = find(menu.getParentId());
			if(pareMenu!=null){
				String parentIds = "";
				if(StringUtils.isNoneBlank(pareMenu.getParentIds())){
					parentIds = pareMenu.getParentIds()+pareMenu.getId()+"/";
				}else{
					parentIds = "/"+pareMenu.getId()+"/";
				}
				menu.setParentIds(parentIds);
			}
		}
		super.save(menu);
	}
	
	/**
	 * 判断menu是否使用过
	 * @param id
	 * @return
	 * @author zhangpeiran 2016年5月11日 上午9:52:06
	 */
	public boolean isUsed(Long id){
		return roleMenuService.isMenuUsed(id);
	}
	
	/**
	 * 根据id,获得所有子
	 * @param id
	 * @return
	 * @author zhangpeiran 2016年5月11日 上午9:44:20
	 */
	public List<Menu> findChilds(Long id){
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("parentId",id);
		return super.query("from Menu where parentId=:parentId order by seq",paramMap);
	}
	
	/**
	 * 获得全部根菜单
	 * @return
	 * @author zhangpeiran 2016年5月11日 上午9:41:46
	 */
	public List<Menu> findRootMenus(){
		String queryString = "from Menu where parentId is null or parentId='' order by seq";
		return super.query(queryString,new HashMap<String,Object>());
	}
	
	/**
	 * 分页查询菜单
	 * @param page
	 * @param name
	 * @return
	 * @author zhangpeiran 2016年5月11日 上午9:26:20
	 */
	public Page findByPage(Page page,String name){
		Map<String,Object> paramMap = new HashMap<>();
		String hql = "from Menu where 1=1 ";
		if(StringUtils.isNoneBlank(name)){
			hql+=" and name = :name";
			paramMap.put("name",name);
		}
		return super.pageQuery(page, hql, paramMap);
	}
	
	/**
	 * 保存角色菜单
	 * @param roleId
	 * @param menuIds
	 * @author zhangpeiran 2016年5月10日 上午8:57:22
	 */
	public void saveRoleMenu(Long roleId,Long[] menuIds){
		//首先删除角色原有菜单配置
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("roleId", roleId);
		roleMenuService.executeUpdate("delete from RoleMenu where roleId=:roleId", paramMap);
		//保存新的菜单项
		for(Long menuId:menuIds){
			RoleMenu rm = new RoleMenu();
			rm.setRoleId(roleId);
			rm.setMenuId(menuId);
			roleMenuService.save(rm);
		}
	}
	 /**
	  * 根据ids获得资源
	  * @return
	  * @author zhangpeiran 2016年5月9日 上午11:16:59
	  */
     public List<Menu> findByIds(Long...menuIds){
    	 Map<String,Object> paramMap = new HashMap<>();
    	 return super.query("from Menu where id in ("+StringUtil.getSplitComma(menuIds)+")",paramMap);
     }
     
	  /**
	   * 根据roleIds获得拥有的menuIds
	   * @param userId
	   * @return
	   * @author zhangpeiran 2016年5月9日 上午10:35:50
	   */
	  public Long[] findMenuIds(Long...roleIds){
		  Map<String,Object> paramMap = new HashMap<>();
		  List<RoleMenu> list = roleMenuService.query("from RoleMenu where roleId in ("+StringUtil.getSplitComma(roleIds)+")",paramMap);
		  Long[] menuIds = new Long[list.size()];
		  for(int i=0;i<list.size();i++){
			  menuIds[i] = list.get(i).getMenuId();
		  }
		  //排重
		  if(menuIds.length>1){
			  Set<Long> set = new TreeSet<>();
			  for (Long id : menuIds) {
			     set.add(id);
			  }
			  Long[] ids = new Long[set.size()];
			  int j = 0;
			  for (Long id : set) {
			    	ids[j++] = id;
			  }
			  return ids;
		  }
		  return menuIds;
	  }
     /**
      * 获得全部菜单
      * @return
      * @author zhangpeiran 2016年5月9日 下午4:18:24
      */
     public int countNum(){
    	return  super.baseDao.count("from Menu",null);
     }
 	/**
 	 * 获得所有菜单,以属性结构显示
 	 */
 	public List<TreeVo> findTreeMenus() {
 		List<TreeVo> treeList = new ArrayList<TreeVo>();
 		List<Menu> rootMenuList = findRootMenus();
 	    if(rootMenuList.size()>0){
 	    	for(Menu menu:rootMenuList){
 	    		TreeVo tv = new TreeVo();
 	    		tv.setId(menu.getId()+"");
 	    		tv.setText("["+menu.getType()+"]"+menu.getName());
 	    		tv.setIconCls(menu.getIcon());
 	    		setChildrenList(menu,tv);
 	    		treeList.add(tv);
 	    	}
 	    }
 	    return treeList;
 	}
 	
 	/**
 	 * 设置子菜单List
 	 * @param menu
 	 * @param tv
 	 * @return
 	 */
 	private Menu setChildrenList(Menu menu,TreeVo tv){
 		List<Menu> childrenList = findChilds(menu.getId());
 		if(childrenList.size()>0){
 			List<TreeVo> treeList = new ArrayList<TreeVo>();
 			for(Menu tempMenu:childrenList){
 				TreeVo treevo = new TreeVo();
 				treevo.setId(tempMenu.getId()+"");
 				treevo.setText("["+menu.getType()+"]"+tempMenu.getName());
 				treevo.setIconCls(tempMenu.getIcon());
 	    		treeList.add(treevo);
 	    		setChildrenList(tempMenu,treevo);
 			}
 			tv.setChildren(treeList);
 		}
 		return menu;
 	}
    /********************************************************************根据菜单List生成菜单html**************************************************/
 	/**
 	 * 根据menuList获得菜单html
 	 * @param menuList
 	 * @return
 	 * @author zhangpeiran 2016年5月10日 上午10:20:49
 	 */
 	public String getMenuHtml(List<Menu> menuList){
 		StringBuffer html = new StringBuffer();
 		html.append("<div class=\"easyui-accordion\" data-options=\"fit:true,border:false\">");
 		//遍历顶级菜单
 		List<Menu> cloneMenuList = new ArrayList<>();
 		cloneMenuList.addAll(menuList);
 		List<Menu> rootMenuList = getRootMenu(menuList);
 		int i=0;
 		for(Menu menu:rootMenuList){
 			html.append("<div title=\""+menu.getName()+"\" style=\"padding:10px;\"");
 			if(i==0){
 				html.append("  data-options=\"selected:true\"");
 			}
 			html.append(">");
 			//遍历子菜单
 			List<Menu> subMenuList = getSubMenu(cloneMenuList,menu);
 			for(Menu subMenu:subMenuList){
 				String name = (subMenu.getName()==null)?"":subMenu.getName();
 				String icon = (subMenu.getIcon()==null)?"":subMenu.getIcon();
 				String url = (subMenu.getIndexUrl()==null)?"":subMenu.getIndexUrl();
 				html.append("<a href=\"javascript:void(0);\" type=\"menuItem\" name=\""+name+"\" icon=\""+icon+"\" url=\""+url+"\" class=\"easyui-linkbutton\" data-options=\"plain:true,iconCls:'"+subMenu.getIcon()+"'\">"+subMenu.getName()+"</a>");
 			    html.append("<br>");
 			}
 			i++;
 			html.append("</div>");
 		}
 		html.append("</div>");
 		return html.toString();
 	}
 	/**
 	 * 得到所有顶级菜单
 	 * @param menuList
 	 * @return
 	 * @author zhangpeiran 2016年5月10日 上午10:34:00
 	 */
 	private List<Menu> getRootMenu(List<Menu> menuList){
 		List<Menu> rootMenuList = new ArrayList<>();
 		//得到所有顶级菜单
 		for(Menu menu:menuList){
 			if(menu.getParentId()==null){
 				rootMenuList.add(menu);
 			}
 		}
 		//排序
 		Collections.sort(rootMenuList,new MenuSeqComparator());
 		return rootMenuList;
 	}
 	/**
 	 * 根据上级菜单,得到子菜单
 	 * @param menuList 全部菜单集合
 	 * @param menu 上级菜单
 	 * @return
 	 * @author zhangpeiran 2016年5月10日 上午10:35:36
 	 */
 	private List<Menu> getSubMenu(List<Menu> menuList,Menu menu){
 		Long parentId = menu.getId();
 		List<Menu> subMenuList = new ArrayList<>();
 		//得到所有子菜单
 		for(Menu m:menuList){
 			if(m.getParentId()==parentId){
 				subMenuList.add(m);
 			}
 		}
 		//排序
 		Collections.sort(subMenuList,new MenuSeqComparator());
 		return subMenuList;
 	}
}
