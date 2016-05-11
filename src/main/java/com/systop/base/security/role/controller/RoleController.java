package com.systop.base.security.role.controller;

import javax.servlet.ServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.systop.base.security.role.entity.Role;
import com.systop.core.controller.BaseController;

/**  
 * 角色管理
 * @company 新龙科技
 * @author zhangpeiran
 * @version 
 * @date 2016年5月11日 下午1:54:44
 */
@RequestMapping("/security/role")
@Controller
public class RoleController extends BaseController{
	/**
	 * 跳转页面
	 * @return
	 */
	@RequestMapping(value = "index")
	public String index() {
		return "security/role/index";
	}
	
	/**
	 * 获得全部角色
	 * @return
	 */
	@RequestMapping(value = "list")
	@ResponseBody
	public Object list(ServletRequest request){
		return serviceManager.roleService.findAll();
	}
	
	/**
	 * 增加一个角色
	 * @param role
	 * @return
	 */
	@RequestMapping(value="save")
	@ResponseBody
	public Object saveRole(Role role){
	    try{
	    	//验证角色信息是否为空
	    	if(role==null){
	    		return getErrorMsg("角色信息为空");
	    	}
	    	//验证角色名称是否为空
	    	if(StringUtils.isEmpty(role.getName())){
	    		return getErrorMsg("角色名称不能为空");
	    	}
	    	if(StringUtils.isEmpty(role.getDescription())){
	    		return getErrorMsg("角色描述不能为空");
	    	}
	    	serviceManager.roleService.save(role);
	    	return getSuccessMsg("保存角色成功,角色名称为:"+role.getName());
	    }catch(Exception e){
	    	e.printStackTrace();
	    	return getErrorMsg(e.getMessage());
	    }
	}
	/**
	 * 修改一个角色
	 * @param role
	 * @return
	 */
	@RequestMapping(value="update")
	@ResponseBody
	public Object updateRole(Role role){
	    try{
	    	//验证角色信息是否为空
	    	if(role==null){
	    		return getErrorMsg("角色信息为空");
	    	}
	    	//验证角色ID是否为空
	    	if(role.getId()==null){
	    		return getErrorMsg("ID不能为空");
	    	}
	    	//验证角色名称是否为空
	    	if(StringUtils.isEmpty(role.getName())){
	    		return getErrorMsg("角色标识不能为空");
	    	}
	    	//验证角色是否存在
	    	Role oldRole = serviceManager.roleService.find(role.getId());
	    	if(oldRole==null)
	    		return getErrorMsg("未找到要修改的角色");
	    	oldRole.setDescription(role.getDescription());
	    	//修改角色信息
	    	serviceManager.roleService.save(oldRole);
	    	return getSuccessMsg("修改角色成功,角色名称为:"+role.getName());
	    }catch(Exception e){
	    	e.printStackTrace();
	    	return getErrorMsg(e.getMessage());
	    }
	}
	
	/**
	 * 删除一个角色
	 * @param id
	 * @return
	 */
	@RequestMapping(value="delete")
	@ResponseBody
	public Object delete(Long id){
		try{
			//验证ID是否为空
			if(id==null){
				return getErrorMsg("请选择要删除的角色");
			}
			//验证角色是否存在
			Role role = serviceManager.roleService.find(id);
			if(role==null){
				return getErrorMsg("未找到要删除的角色");
			}
			//验证角色下是否有用户
			if(serviceManager.roleService.isUsed(id))
				return getErrorMsg("角色下有用户,无法删除角色");
			//删除角色
			serviceManager.roleService.remove(role);
			return getSuccessMsg("删除角色["+role.getName()+"]成功");
		}
		catch(Exception e){
			e.printStackTrace();
			return getErrorMsg("系统错误:"+e.getMessage());
		}
	}
}
