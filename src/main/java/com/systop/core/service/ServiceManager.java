package com.systop.core.service;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.systop.base.security.menu.service.MenuService;
import com.systop.base.security.organization.service.OrganizationService;
import com.systop.base.security.role.service.RoleService;
import com.systop.base.security.user.service.UserSerivce;

@Service
public class ServiceManager {
	@Resource
	public UserSerivce userSerivce;
	@Resource
	public MenuService menuService;
	@Resource
	public RoleService roleService;
	@Resource
	public OrganizationService organizationService;
}
