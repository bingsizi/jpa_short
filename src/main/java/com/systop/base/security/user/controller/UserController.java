package com.systop.base.security.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.systop.core.controller.BaseController;

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
}
