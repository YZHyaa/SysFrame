package com.mdt.controller.base;


import com.mdt.entity.Page;
import com.mdt.entity.system.User;
import com.mdt.util.Const;
import com.mdt.util.Logger;
import com.mdt.util.PageData;
import com.mdt.util.UuidUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class BaseController {
	
	protected Logger logger = Logger.getLogger(this.getClass());

	private static final long serialVersionUID = 6357869213649815390L;
	
	/**
	 * 得到PageData
	 */
	public PageData getPageData(){
		return new PageData(this.getRequest());
	}
	
	/**
	 * 得到ModelAndView
	 */
	public ModelAndView getModelAndView(){
		return new ModelAndView();
	}
	
	/**
	 * 得到request对象
	 */
	public HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		return request;
	}

	/**
	 * 得到32位的uuid
	 * @return
	 */
	public String get32UUID(){
		
		return UuidUtil.get32UUID();
	}
	
	/**
	 * 得到分页列表的信息 
	 */
	public Page getPage(){
		
		return new Page();
	}
	
	public static void logBefore(Logger logger, String interfaceName){
		logger.info("");
		logger.info("start");
		logger.info(interfaceName);
	}
	
	public static void logAfter(Logger logger){
		logger.info("end");
		logger.info("");
	}
	
	/**
	 * 获取用户信息
	 * @return
	 */
	public User getUser(){
		Session session = getSession();
		User user = (User)session.getAttribute(Const.SESSION_USER);
		return user;
	}
	
	/**
	 * 获取session信息
	 * @return
	 */
	public Session getSession(){
		Subject currentUser = SecurityUtils.getSubject();  
		Session session = currentUser.getSession();
		return session;
	}
	
	
	public PageData savePage(PageData pd){
		User user=getUser();
		pd.put("DEL_STATE","1");//1 有效，0或者null 无效
		pd.put("CREATE_UID", user.getUSERNAME());
		pd.put("CREATE_TIME", new Date());
		pd.put("CHANGE_UID", user.getUSERNAME());
		pd.put("CHANGE_TIME", new Date());
		return pd;
	}
	
	public PageData updatePage(PageData pd){
		User user=getUser();
		pd.put("CREATE_UID", user.getUSERNAME());
		pd.put("CHANGE_TIME", new Date());
		return pd;
	}
	
}
