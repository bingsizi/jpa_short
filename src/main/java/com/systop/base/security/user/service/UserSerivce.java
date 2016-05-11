package com.systop.base.security.user.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.systop.base.security.menu.entity.Menu;
import com.systop.base.security.menu.service.MenuService;
import com.systop.base.security.role.entity.Role;
import com.systop.base.security.role.service.RoleService;
import com.systop.base.security.user.entity.User;
import com.systop.core.service.BaseGenericsService;

/**
 * 用户业务层
 * 
 * @company 新龙科技
 * @author zhangpeiran
 * @version
 * @date 2016年5月9日 上午10:17:15
 */
@Service
public class UserSerivce extends BaseGenericsService<User> {
	
	@Resource
	private RoleService roleService;
	@Resource
	private MenuService menuService;

	/**
	 * 根据用户名得到用户
	 * 
	 * @param username
	 * @return
	 * @author zhangpeiran 2016年5月9日 上午10:19:40
	 */
	public User findByUsername(String username) {
		String hql = "from User where username = :username";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("username", username);
		List<User> list = super.query(hql, paramMap);
		return (list.size() > 0) ? list.get(0) : null;
	}

	/**
	 * 根据用户名获得角色标识集合
	 * 
	 * @param username
	 * @return
	 * @author zhangpeiran 2016年5月9日 上午10:27:13
	 */
	public Set<String> findRoleNameSet(String username) {
		Set<String> roleSet = new HashSet<>();
		User user = findByUsername(username);
		if (user != null) {
			Long[] roleIds = roleService.getRoleIds(user.getId());
			if (roleIds.length > 0) {
				List<Role> roleList = roleService.findByIds(roleIds);
				for (Role role : roleList) {
					roleSet.add(role.getName());
				}
			}
		}
		return roleSet;
	}

	/**
	 * 根据用户id返回角色list
	 * 
	 * @param id
	 * @return
	 * @author zhangpeiran 2016年5月9日 上午10:59:16
	 */
	public List<Role> findRoles(Long id) {
		User user = super.find(id);
		if (user != null) {
			Long[] roleIds = roleService.getRoleIds(user.getId());
			if (roleIds.length > 0) {
				return roleService.findByIds(roleIds);
			}
		}
		return Collections.emptyList();
	}

	/**
	 * 根据用户名得到资源权限标识集合
	 * 
	 * @param username
	 * @return
	 * @author zhangpeiran 2016年5月9日 上午11:00:36
	 */
	public Set<String> findPermissions(String username) {
		Set<String> permissionSet = new HashSet<>();
		User user = findByUsername(username);
		if (user != null) {
			List<Menu> list = findMenus(user.getId());
			for (Menu menu : list) {
				String permission = menu.getPermission();
				if (StringUtils.isNotBlank(permission)) {
					permissionSet.add(permission);
				}
			}
			return permissionSet;
		}
		return permissionSet;
	}

	/**
	 * 根据用户id,返回拥有的所有菜单和权限
	 * 
	 * @param id
	 * @return
	 * @author zhangpeiran 2016年5月9日 上午11:01:17
	 */
	public List<Menu> findMenus(Long id) {
		User user = super.find(id);
		if (user != null) {
			Long[] roleIds = roleService.getRoleIds(user.getId());
			if (roleIds.length > 0) {
				Long[] menuIds = menuService.findMenuIds(roleIds);
				if (menuIds.length > 0) {
					return menuService.findByIds(menuIds);
				}
			}
		}
		return Collections.emptyList();
	}
}
