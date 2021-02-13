package com.mdt.interceptor.cache;

import com.mdt.service.system.dictionaries.DictionariesService;
import com.mdt.util.cache.CacheUtil;
import com.mdt.util.cache.DictionaryCacheUtil;
import net.sf.ehcache.Cache;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.List;

/**
 * 数据字典缓存更新aop
 * @ClassName: DictionariesCacheInterceptor 
 * @Description: TODO
 * @author "PangLin"
 * @date 2015年12月3日 上午11:06:25 
 *
 */
@Aspect
public class DictionariesCacheInterceptor {
	private Cache cache;
	private DictionariesService dictionariesService;
	
	@Pointcut("execution(* com.mdt.service.system.dictionaries.DictionariesService.insert*(..))")
	private void insertMethod(){}
	@Pointcut("execution(* com.mdt.service.system.dictionaries.DictionariesService.update*(..))")
	private void updateMethod(){}
	@Pointcut("execution(* com.mdt.service.system.dictionaries.DictionariesService.delete*(..))")
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
				List dicDataList = dictionariesService.listDic(null);
				CacheUtil.initCache(CacheUtil.makeSelectDataCache(dicDataList, "ID", "PID", "NAME", "CODE", "0", "root",true,DictionaryCacheUtil.DICTIONARIE_MAP_POSTFIX), cache);
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
	public DictionariesService getDictionariesService() {
		return dictionariesService;
	}
	public void setDictionariesService(DictionariesService dictionariesService) {
		this.dictionariesService = dictionariesService;
	} 
    
}
