package com.mdt.interceptor.cache;

import com.mdt.entity.system.Menu;
import com.mdt.service.system.menu.MenuService;
import com.mdt.util.cache.CacheUtil;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.ArrayList;
import java.util.List;
/**
 * 菜单缓存更新aop
 * @ClassName: MenusCacheInterceptor 
 * @Description: TODO
 * @author "PangLin"
 * @date 2015年12月3日 上午11:06:25 
 *
 */
@Aspect
public class MenusCacheInterceptor {
	private Cache cache;
	private MenuService menuService;
	
	@Pointcut("execution(* com.mdt.service.system.menu.MenuService.insert*(..))")
	private void insertMethod(){}
	@Pointcut("execution(* com.mdt.service.system.menu.MenuService.update*(..))")
	private void updateMethod(){}
	@Pointcut("execution(* com.mdt.service.system.menu.MenuService.delete*(..))")
	private void deleteMethod(){}
      
    @After("insertMethod()")  
    public void afterInsert(){  
    	makeCache();
    }  
    
    @After("updateMethod()")  
    public void afterUPdate(){  
    	makeCache();
    } 
    
    @After("deleteMethod()")  
    public void afterDelete(){ 
    	makeCache();
    }
    
    private void makeCache(){
    	synchronized (this) {
			try {
				List<Menu> menusList = menuService.listAllMenus();
				List<Element> elist = new ArrayList<Element>();
				
				for(Menu menu:menusList){
					
					String url = menu.getMENU_URL().split(".do")[0];
					if(url.startsWith("/")||url.startsWith("\\"))
						url = url.substring(1);
					
					
					Element e = new Element(url,1);
					elist.add(e);
				}
				
				CacheUtil.initCache(elist,cache);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
    
    
	public Cache getCache() {
		return cache;
	}
	public void setCache(Cache cache) {
		this.cache = cache;
	}
	public MenuService getMenuService() {
		return menuService;
	}
	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}
	
    
}
