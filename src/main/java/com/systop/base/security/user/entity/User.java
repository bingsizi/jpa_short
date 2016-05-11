package com.systop.base.security.user.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.systop.core.Constants;
import com.systop.core.entity.IdEntity;

/**
 * user用户类
 * 
 * @company 新龙科技
 * @author zhangpeiran
 * @version
 * @date 2016年5月3日 上午10:42:41
 */
@Entity
@Table(name="sys_user")
public class User extends IdEntity implements Serializable{
	
	/**
	 * serialVersionUID long
	 * created by zhangpeiran 2016年5月10日 上午11:27:54
	 */
	private static final long serialVersionUID = 7826302322272727318L;
	
	/**必要属性**/
	private String username;//用户名
	private String password;//密码
	private String salt;//盐加密用的
	private String locked = Constants.NO;//是否锁定
	/** 其他属性**/
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getLocked() {
		return locked;
	}

	public void setLocked(String locked) {
		this.locked = locked;
	}
}
