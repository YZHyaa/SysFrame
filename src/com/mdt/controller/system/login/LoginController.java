package com.mdt.controller.system.login;

import com.mdt.controller.base.BaseController;
import com.mdt.entity.system.Menu;
import com.mdt.entity.system.User;
import com.mdt.listener.PropertyListener;
import com.mdt.service.system.dictionaries.DictionariesService;
import com.mdt.service.system.menu.MenuService;
import com.mdt.service.system.user.UserService;
import com.mdt.util.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * 总入口
 */
@Controller
public class LoginController extends BaseController {

	@Resource(name="userService")
	private UserService userService;
	@Resource(name="menuService")
	private MenuService menuService;
	@Resource(name = "dictionariesService")
	private DictionariesService dictionariesService;
	/**
	 * 获取登录用户的IP
	 * @throws Exception 
	 */
	public void getRemortIP(String USERNAME) throws Exception {  
		PageData pd = new PageData();
		HttpServletRequest request = this.getRequest();
		String ip = "";
		if (request.getHeader("x-forwarded-for") == null) {  
			ip = request.getRemoteAddr();  
	    }else{
	    	ip = request.getHeader("x-forwarded-for");  
	    }
		pd.put("USERNAME", USERNAME);
		pd.put("IP", ip);
		userService.saveIP(pd);
	}  
	
	
	/**
	 * 访问登录页
	 * @return
	 */
	@RequestMapping(value="/login_toLogin")
	public ModelAndView toLogin()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("SYSNAME", PropertyListener.getPropertyValue("${system.name}")); //读取系统名称
		String[] titles=getTitles();
		pd.put("BIG_TITLE", titles[0]);
		pd.put("TITLE", titles[1]);
		pd.put("SUB_TITLE", titles[2]);
		mv.addObject("pd",pd);
		mv.setViewName("system/admin/login");
		return mv;
	}
	
	/**
	 * 请求登录，验证用户
	 */
	@RequestMapping(value="/login_login" ,produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object login()throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		PageData pd = new PageData();
		pd = this.getPageData();
		String errInfo = "";
		String KEYDATA[] = pd.getString("KEYDATA").replaceAll("PKSPKSluyun", "").replaceAll("PKSPKSluyun", "").split(",luyun,");
		
		if(null != KEYDATA && KEYDATA.length == 3){
			//shiro管理的session
			Subject currentUser = SecurityUtils.getSubject();  
			Session session = currentUser.getSession();
//			String sessionCode = (String)session.getAttribute(Const.SESSION_SECURITY_CODE);		//获取session中的验证码
			String sessionCode ="123";		//获取session中的验证码
			String code = KEYDATA[2];
			if(null == code || "".equals(code)){
				errInfo = "nullcode"; //验证码为空
			}else{
				String USERNAME = KEYDATA[0];
				String PASSWORD  = KEYDATA[1];
				pd.put("USERNAME", USERNAME);
				if(Tools.notEmpty(sessionCode) && sessionCode.equalsIgnoreCase(code)){
					String passwd = new SimpleHash("SHA-1", USERNAME, PASSWORD).toString();	//密码加密
					pd.put("PASSWORD", passwd);
					pd = userService.getUserByNameAndPwd(pd);
					if(pd != null){
						pd.put("LAST_LOGIN",DateUtil.getTime().toString());
						userService.updateLastLogin(pd);
						User user = new User();
						user.setUSER_ID(pd.getString("USER_ID"));
						user.setUSERNAME(pd.getString("USERNAME"));
						user.setPASSWORD(pd.getString("PASSWORD"));
						user.setNAME(pd.getString("NAME"));
						user.setLAST_LOGIN(pd.getString("LAST_LOGIN"));
						user.setIP(pd.getString("IP"));
						user.setSTATUS(pd.getString("STATUS"));
						user.setNEED_GUIDE(pd.getString("NEED_GUIDE"));
						session.setAttribute(Const.SESSION_USER, user);
						session.removeAttribute(Const.SESSION_SECURITY_CODE);
						
						//shiro加入身份验证
						Subject subject = SecurityUtils.getSubject(); 
					    UsernamePasswordToken token = new UsernamePasswordToken(USERNAME, PASSWORD); 
					    try { 
					        subject.login(token); 
					    } catch (AuthenticationException e) { 
					    	errInfo = "身份验证失败！";
					    }
					    
					}else{
						errInfo = "usererror"; 				//用户名或密码有误
					}
				}else{
					errInfo = "codeerror";				 	//验证码输入有误
				}
				if(Tools.isEmpty(errInfo)){
					errInfo = "success";					//验证成功
				}
			}
		}else{
			errInfo = "error";	//缺少参数
		}
		map.put("result", errInfo);
		return AppUtil.returnObject(new PageData(), map);
	}
	
	/**
	 * 访问系统首页
	 */
	@RequestMapping(value="/main/{changeMenu}")
	public ModelAndView login_index(@PathVariable("changeMenu") String changeMenu){
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			
			//shiro管理的session
			Subject currentUser = SecurityUtils.getSubject();  
			Session session = currentUser.getSession();
			
			User user = (User)session.getAttribute(Const.SESSION_USER);
			if (user != null) {
				//放入用户名
				session.setAttribute(Const.SESSION_USERNAME, user.getUSERNAME());
				//获取菜单
				
				
				List<Menu> menuList = menuService.listMenuByUserName(user.getUSERNAME());
				
//				
//				List<Menu> allMenuList = new ArrayList<Menu>();
//				List<Menu> menuList = new ArrayList<Menu>();
//				
//				if(null == session.getAttribute(Const.SESSION_menuList)){
//					//生成菜单，未完成----------------------------
//					menuList = menuService.listAllMenu();
//					
//					//生成菜单，未完成----------------------------
//					session.setAttribute(Const.SESSION_allmenuList, allMenuList);
//					session.setAttribute(Const.SESSION_menuList, menuList);			//菜单权限放入session中
//				}else{
//					menuList = (List<Menu>)session.getAttribute(Const.SESSION_menuList);
//				}
				
				this.getRemortIP(user.getUSERNAME());
				
				String theme = PropertyListener.getPropertyValue("${system.theme}");
				String logo = PropertyListener.getPropertyValue("${system.logo}");
				String sysname = PropertyListener.getPropertyValue("${system.name}");
				String homepage = PropertyListener.getPropertyValue("${system.homepage}");
				
				pd.put("LOGO", logo); //读取系统名称
				pd.put("SYSNAME", sysname); //读取系统名称
				pd.put("HOMEPAGE", homepage); //读取系统名称
				
				String[] words=getMainSoftWords();
				pd.put("COPYRIGHT", words[0]);   //版权
				pd.put("COPYRIGHT_GROUP", words[1]);//版权组织
				pd.put("COPYRIGHT_GROUP_LINK", words[2]); //版权组织链接
				if("index".equals(changeMenu)){
					if("v".equals(theme.toLowerCase())){
						mv.setViewName("system/admin/vindex");
					}else{
						mv.setViewName("system/admin/index");
					}
				}else if("indexV".equals(changeMenu)){
					mv.setViewName("system/admin/vindex");
				}else if("indexH".equals(changeMenu)){
					mv.setViewName("system/admin/index");
				}else{
					mv.setViewName("system/admin/index");
				}
				
				mv.addObject("user", user);
				mv.addObject("menuList", menuList);
				
			}else {
				mv.setViewName("system/admin/login");//session失效后跳转登录页面
			}
			
			
		} catch(Exception e){
			mv.setViewName("system/admin/login");
			logger.error(e.getMessage(), e);
		}
		
		mv.addObject("pd",pd);
		return mv;
	}
	
	
	/**
	 * 修改密码
	 * 
	 */
	@RequestMapping(value="/saveChangePwd")
	@ResponseBody
    public	Object saveChangePwd() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		PageData pd = new PageData();
		pd = this.getPageData();
		String errInfo = "";
		String USERNAME=pd.getString("USERNAME");
		String PWD=pd.getString("PWD");
		String NEWPWD=pd.getString("NEWPASSWORD");
		String PASSWORD = new SimpleHash("SHA-1", USERNAME, PWD).toString();	//密码加密
		pd.put("PASSWORD", PASSWORD);
		pd = userService.getUserByNameAndPwd(pd);
	    if(pd!=null){
	    	String newPwd = new SimpleHash("SHA-1", USERNAME, NEWPWD).toString();	//密码加密
	    	pd.put("PASSWORD", newPwd);
	    	try {
	    		userService.updatePwd(pd);
	    		Subject currentUser = SecurityUtils.getSubject();  
				Session session = currentUser.getSession();
				User user = (User)session.getAttribute(Const.SESSION_USER);
				user.setPASSWORD(newPwd);
				session.setAttribute(Const.SESSION_USER, user);
				errInfo="success";
			} catch (Exception e) {
				errInfo="error";
			}
	    }else{
			errInfo = "pwderror"; 				//密码有误
	    }
		map.put("result", errInfo);
		return AppUtil.returnObject(new PageData(), map);
	}
	
	
	
	/**
	 * 去修改用户页面
	 */
	@RequestMapping(value = "/toChangePwd")
	public ModelAndView toChangePwd() throws Exception {
		ModelAndView mv = this.getModelAndView();
		//shiro管理的session
		Subject currentUser = SecurityUtils.getSubject();  
		Session session = currentUser.getSession();
		User user = (User)session.getAttribute(Const.SESSION_USER);
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("USER_ID", user.getUSER_ID()); 
		pd.put("USER_NAME", user.getUSERNAME());
		mv.addObject("pd",pd);
		mv.setViewName("system/user/user_pwd_edit");
		return mv;
	}
	
	
	/**
	 * 用户注销
	 * @param //session
	 * @return
	 */
	@RequestMapping(value="/logout")
	public ModelAndView logout(){
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String  msg = pd.getString("msg");
		pd.put("msg", msg);
		
		pd.put("SYSNAME", PropertyListener.getPropertyValue("${system.name}")); //读取系统名称
		String[] titles=getTitles();
		pd.put("BIG_TITLE", titles[0]);
		pd.put("TITLE", titles[1]);
		pd.put("SUB_TITLE", titles[2]);
		

		//shiro管理的session
		Subject currentUser = SecurityUtils.getSubject();  
		Session session = currentUser.getSession();
		
		session.removeAttribute(Const.SESSION_USER);
		session.removeAttribute(Const.SESSION_ROLE_RIGHTS);
		session.removeAttribute(Const.SESSION_allmenuList);
		session.removeAttribute(Const.SESSION_menuList);
		session.removeAttribute(Const.SESSION_QX);
		session.removeAttribute(Const.SESSION_userpds);
		session.removeAttribute(Const.SESSION_USERNAME);
		session.removeAttribute(Const.SESSION_USERROL);
		
		//shiro销毁登录
		Subject subject = SecurityUtils.getSubject(); 
		subject.logout();
		
		mv.addObject("pd",pd);
		mv.setViewName("system/admin/login");
		return mv;
	}
	/**
	 * 首页
	 * @param //session
	 * @return
	 */
	@RequestMapping(value="/homepage")
	public ModelAndView homepage(){
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("system/admin/homepage");
		return mv;
	}
	/**
	 * 
	 * @return 返回参数数组，数组第一个元素为大标题，第二个参数为标题，第三个为小标题
	 * @throws Exception
	 */
	private  String[]  getTitles() {
		String[] returnParams = new String[3];   
		PageData pd=new PageData();
		pd.put("code", ConstDic.LOGIN_TITLES);
		List<PageData> params;
		try {
			params = dictionariesService.lsitSubDicByParentCode(pd);
			HashMap<String,String> tempMap=new HashMap<String, String>();
			for (PageData param : params) {
			   tempMap.put(param.getString("TEXT"), param.getString("ID"));
			}
			
			if(tempMap.containsKey(ConstDic.LOGIN_BIG_TITLE))  {returnParams[0]=tempMap.get(ConstDic.LOGIN_BIG_TITLE).toString();}
			if(tempMap.containsKey(ConstDic.LOGIN_TITLE)) 	   {returnParams[1]=tempMap.get(ConstDic.LOGIN_TITLE).toString();}
			if(tempMap.containsKey(ConstDic.LOGIN_SUB_TITLE))  {returnParams[2]=tempMap.get(ConstDic.LOGIN_SUB_TITLE).toString();}
		} catch (Exception e1) {
			e1.printStackTrace();
		    returnParams[0]="";	 
		    returnParams[1]=""; 
		    returnParams[1]=""; 
		}
		return returnParams;
	}
	/**
	 * 
	 * @return 返回参数数组，数组第一个元素为版权，第二个参数为版权组织，第三个为版权组织链接
	 * @throws Exception
	 */
	private  String[]  getMainSoftWords() {
		String[] returnParams = new String[3];   
		PageData pd=new PageData();
		pd.put("code", ConstDic.MAIN_SOFT_WORDS);
		List<PageData> params;
		try {
			params = dictionariesService.lsitSubDicByParentCode(pd);
			HashMap<String,String> tempMap=new HashMap<String, String>();
			for (PageData param : params) {
			   tempMap.put(param.getString("TEXT"), param.getString("ID"));
			}
			
			if(tempMap.containsKey(ConstDic.SOFT_COPYRIGHT))  {returnParams[0]=tempMap.get(ConstDic.SOFT_COPYRIGHT).toString();}
			if(tempMap.containsKey(ConstDic.SOFT_COPYRIGHT_GROUP)) 	   {returnParams[1]=tempMap.get(ConstDic.SOFT_COPYRIGHT_GROUP).toString();}
			if(tempMap.containsKey(ConstDic.SOFT_COPYRIGHT_GROUP_LINK))  {returnParams[2]=tempMap.get(ConstDic.SOFT_COPYRIGHT_GROUP_LINK).toString();}
		} catch (Exception e1) {
			e1.printStackTrace();
		    returnParams[0]="";	 
		    returnParams[1]=""; 
		    returnParams[1]=""; 
		}
		return returnParams;
	}
}
