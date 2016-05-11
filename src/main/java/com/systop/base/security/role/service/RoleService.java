package com.systop.base.security.role.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.systop.base.security.role.entity.Role;
import com.systop.base.security.role.entity.UserRole;
import com.systop.core.service.BaseGenericsService;
import com.systop.core.utils.StringUtil;

/**
 * 角色业务层
 * 
 * @company 新龙科技
 * @author zhangpeiran
 * @version
 * @date 2016年5月9日 上午10:36:48
 */
@Service
public class RoleService extends BaseGenericsService<Role> {

	@Resource
	private UserRoleService userRoleService;

	/**
	 * 根据ids获得roles
	 * 
	 * @param ids
	 * @return
	 * @author zhangpeiran 2016年5月9日 上午10:38:28
	 */
	public List<Role> findByIds(Long... ids) {
		Map<String, Object> paramMap = new HashMap<>();
		String hql = "from Role where id in ("+StringUtil.getSplitComma(ids)+")";
		return super.query(hql, paramMap);
	}

	/**
	 * 根据userId获得拥有的roleIds
	 * 
	 * @param userId
	 * @return
	 * @author zhangpeiran 2016年5月9日 上午10:35:50
	 */
	public Long[] getRoleIds(Long userId) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("userId", userId);
		List<UserRole> list = userRoleService.query("from UserRole where userId = :userId ", paramMap);
		Long[] roleIds = new Long[list.size()];
		for (int i = 0; i < list.size(); i++) {
			roleIds[i] = list.get(0).getRoleId();
		}
		return roleIds;
	}

	/**
	 * 保存人员角色
	 * 
	 * @param userId
	 * @param roleIds
	 * @author zhangpeiran 2016年5月9日 下午5:29:56
	 */
	public void saveUserRole(Long userId, Long... roleIds) {
		for (Long id : roleIds) {
			UserRole ur = new UserRole();
			ur.setUserId(userId);
			ur.setRoleId(id);
			userRoleService.save(ur);
		}
	}
}
