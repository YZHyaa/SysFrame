package com.mdt.interceptor.cache;

import com.mdt.service.system.message.MessageTopicService;
import com.mdt.util.cache.CacheUtil;
import net.sf.ehcache.Cache;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.List;

/**
 * messageTopic更新缓存
 * @ClassName: MessageTopicCacheInterceptor 
 * @Description: TODO
 * @author "PangLin"
 * @date 2016年1月29日 上午8:54:04 
 *
 */
@Aspect
public class MessageTopicCacheInterceptor {
	private Cache cache;
	private MessageTopicService messageTopicService;
	
	@Pointcut("execution(* com.mdt.service.system.message.MessageTopicService.batchAddUser*(..))")
	private void addMethod(){}
	@Pointcut("execution(* com.mdt.service.system.message.MessageTopicService.batchDeleteUser*(..))")
	private void deleteMethod(){}
	
	@After("addMethod()")  
    public void afterInsert(){  
		makeCache();
    }  
    
    @After("deleteMethod()")  
    public void afterDelete(){ 
    	makeCache();
    }
    
    private void makeCache(){
    	synchronized (this) {
			try {
				List infoList = messageTopicService.findUserGroupByTopic();
				CacheUtil.initCache(CacheUtil.makeSimpleDataCache(infoList, "TOPICCODE", "TOPICUSER"), cache);
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
	public MessageTopicService getMessageTopicService() {
		return messageTopicService;
	}
	public void setMessageTopicService(MessageTopicService messageTopicService) {
		this.messageTopicService = messageTopicService;
	}
	
	
}
