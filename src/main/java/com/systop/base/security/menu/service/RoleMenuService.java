package com.systop.base.security.menu.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import com.systop.base.security.menu.entity.RoleMenu;
import com.systop.core.service.BaseGenericsService;

/**  
 * 角色资源业务层
 * @company 新龙科技
 * @author zhangpeiran
 * @version 
 * @date 2016年5月9日 上午11:03:01
 */
@Service
public class RoleMenuService extends BaseGenericsService<RoleMenu>{
	/**
	 * 判断menu是否使用过
	 * @param menuId
	 * @return
	 * @author zhangpeiran 2016年5月11日 上午9:49:37
	 */
	public boolean isMenuUsed(Long menuId){
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("menuId",menuId);
		int count = baseDao.count("from RoleMenu where menuId = :menuId", paramMap);
		return (count>0)?true:false;
	}
}
