package com.systop.base.security.role.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import com.systop.base.security.role.entity.UserRole;
import com.systop.core.service.BaseGenericsService;

/**  
 * 用户角色业务层
 * @company 新龙科技
 * @author zhangpeiran
 * @version 
 * @date 2016年5月9日 上午10:30:30
 */
@Service
public class UserRoleService extends BaseGenericsService<UserRole>{
	/**
	 * 判断角色有没有被使用
	 * @param roleId
	 * @return
	 * @author zhangpeiran 2016年5月11日 下午2:22:18
	 */
	public boolean isRoleUsed(Long roleId){
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("roleId",roleId);
		int count = baseDao.count("from UserRole where roleId = :roleId", paramMap);
		return (count>0)?true:false;
	}
}
