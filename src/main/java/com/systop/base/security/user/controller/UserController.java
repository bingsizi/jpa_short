package com.systop.base.security.user.controller;

import javax.servlet.ServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.systop.core.controller.BaseController;
import com.systop.core.dao.jpa.Page;

/**  
 * 用户管理
 * @company 新龙科技
 * @author zhangpeiran
 * @version 
 * @date 2016年5月12日 上午10:38:58
 */
@RequestMapping("/security/user")
@Controller
public class UserController extends BaseController{
	/**
	 * 跳转页面
	 * @return
	 */
	@RequestMapping(value = "index")
	public String index() {
		return "security/user/index";
	}
	/**
	 * 返回用户
	 * @param request
	 * @param orgId
	 * @param username
	 * @param realName
	 * @return
	 * @author zhangpeiran 2016年5月12日 上午11:10:22
	 */
	@RequestMapping(value = "list")
	@ResponseBody
	public Object list(ServletRequest request,Long orgId,String username,String realName){
		Page page = buildEasyUiPage(request);
		page.addOrders(null,"createTime");
		return serviceManager.userSerivce.findPage(page,orgId, username, realName);
	}
	/**
	 * 返回全部组织机构
	 * @return
	 * @author zhangpeiran 2016年5月12日 上午11:11:37
	 */
	@RequestMapping(value = "orgTree")
	@ResponseBody
	public Object orgTree(){
		return serviceManager.organizationService.orgTreeList(null);
	}
}
