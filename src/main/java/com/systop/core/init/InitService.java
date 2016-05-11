package com.systop.core.init;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import com.systop.base.security.menu.entity.Menu;
import com.systop.base.security.menu.support.MenuType;
import com.systop.base.security.role.entity.Role;
import com.systop.base.security.user.entity.User;
import com.systop.core.Constants;
import com.systop.core.service.ServiceManager;


/**
 * 初始化完成之后执行
 * @author Nice
 */
@Service
public class InitService implements ApplicationListener<ContextRefreshedEvent> {
	
	private final Logger log = LoggerFactory.getLogger(InitService.class);
	
	@Resource
	private ServiceManager serviceManagerManager;
	

	/**
	 * 当初始化完成后，将触发事件，调用以下代码。SpringMVC所以与Spring初始化完成后都会调用，所以增加判断
	 */
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (event.getApplicationContext().getParent() == null){
			if(Constants.INIT_SQL_DATA){
				log.info("初始化sql数据...................................................");
				//初始化菜单,系统管理
				Menu systemTop = new Menu();
				systemTop.setName("系统设置");
				systemTop.setSeq(1);
				systemTop.setType(MenuType.菜单);
				serviceManagerManager.menuService.save(systemTop);
				/******系统菜单的子菜单*******************/
				String systemTopParentIds =systemTop.getId()+"/";
				
				Menu userManager = new Menu();
				userManager.setName("用户管理");
				userManager.setParentId(systemTop.getId());
				userManager.setParentIds(systemTopParentIds);
				userManager.setType(MenuType.菜单);
				userManager.setSeq(1);
				userManager.setIcon("icon-user");
				userManager.setPermission("/security/user");
				serviceManagerManager.menuService.save(userManager);
				
				Menu roleManager = new Menu();
				roleManager.setName("角色管理");
				roleManager.setParentId(systemTop.getId());
				roleManager.setParentIds(systemTopParentIds);
				roleManager.setType(MenuType.菜单);
				roleManager.setSeq(2);
				roleManager.setIcon("icon-role");
				roleManager.setPermission("/security/role");
				serviceManagerManager.menuService.save(roleManager);
				
				Menu menuManager = new Menu();
				menuManager.setName("菜单管理");
				menuManager.setParentId(systemTop.getId());
				menuManager.setParentIds(systemTopParentIds);
				menuManager.setType(MenuType.菜单);
				menuManager.setSeq(3);
				menuManager.setIcon("icon-menu");
				menuManager.setPermission("/security/menu");
				menuManager.setUrl("/security/menu/index");
				serviceManagerManager.menuService.save(menuManager);
				
				Menu onUserManager = new Menu();
				onUserManager.setName("在线用户管理");
				onUserManager.setParentId(systemTop.getId());
				onUserManager.setParentIds(systemTopParentIds);
				onUserManager.setType(MenuType.菜单);
				onUserManager.setSeq(4);
				onUserManager.setIcon("icon-online");
				onUserManager.setPermission("/security/online");
				serviceManagerManager.menuService.save(onUserManager);
				
				//初始化角色
				Role role = new Role();
				role.setName("admin");
				role.setDescription("系统管理员");
				serviceManagerManager.roleService.save(role);
				
				//初始化用户
				User user = new User();
				user.setUsername("admin");
				user.setPassword("manager");
				serviceManagerManager.userSerivce.save(user);
				
				//初始化用户角色
				serviceManagerManager.roleService.saveUserRole(user.getId(),role.getId());
				
				//初始化角色菜单
				List<Menu> menuList = new ArrayList<>();
				menuList.add(systemTop);
				menuList.add(userManager);
				menuList.add(roleManager);
				menuList.add(menuManager);
				menuList.add(onUserManager);
				serviceManagerManager.menuService.saveRoleMenu(role.getId(), menuList);
				
			}
		}
	}
}
