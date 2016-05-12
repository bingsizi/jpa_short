package systopbase.secrity.user;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.systop.base.security.menu.entity.Menu;
import com.systop.core.dao.jpa.Page;

import systopbase.BaseTest;

/**  
 * 
 * @company 新龙科技
 * @author zhangpeiran
 * @version 
 * @date 2016年5月10日 上午11:37:38
 */
public class UserServiceTest extends BaseTest{
	 @Test
     public void findMenus(){
    	 List<Menu> menuList = serviceManager.userSerivce.findMenus(1l);
    	 System.out.println(menuList.size());
     }
	 @Test
	 public void findPage(){
		 Page page = new Page(1,20);
		 page.addOrders(null,"createTime");
		 Page returnPage = serviceManager.userSerivce.findPage(page,1l,null,null);
		 Assert.assertTrue(returnPage.getTotal()>0);
	 }
}
