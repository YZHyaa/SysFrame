package com.mdt.service.system.auth;

import com.mdt.dao.DaoSupport;
import com.mdt.util.PageData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service("authService")
public class AuthService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	
	/**
	 * 为shiro获取用户的全部的permission
	 * 按钮以 "user:add" "user:edit" "user:del"的形式授权
	 * 菜单以 "url:user/listUsers" 的形式授权
	 * @param userName
	 * @return
	 * @author Hrq
	 */
	public List<String> getAuthListByUser(String userName){
		List<String> authList = new ArrayList<String>();
		PageData pd = new PageData();
		pd.put("USERNAME", userName);
		
		//获取按钮权限
		Map btnMap = getButtonAuthByUser(pd);
		
		Iterator<String> it = btnMap.keySet().iterator();
		while(it.hasNext()){
			authList.add(it.next());
		}
		//获取菜单权限
		Map urlMap = getUrlAuthByUser(pd);
		
		Iterator<String> it2 = urlMap.keySet().iterator();
		while(it2.hasNext()){
			authList.add(it2.next());
		}
		
		return authList;
	}
	
	public List<String> getGlobalBtnListByUser(String userName){
		List<String> buttonList = new ArrayList<String>();
		PageData pd = new PageData();
		pd.put("USERNAME", userName);
		
		Map<String,String> map = getGlobalBtnAuthByUser(pd);
		Iterator<String> it = map.keySet().iterator();
		while(it.hasNext()){
			buttonList.add(it.next());
		}
		return buttonList;
	}
	
	public List<String> getURLBtnListByUser(String userName){
		List<String> buttonList = new ArrayList<String>();
		PageData pd = new PageData();
		pd.put("USERNAME", userName);
		
		Map<String,String> map = getURLBtnAuthByUser(pd);
		Iterator<String> it = map.keySet().iterator();
		while(it.hasNext()){
			buttonList.add(it.next());
		}
		return buttonList;
	}
	
	/**
	 * 获取用户的菜单权限
	 * @param pd
	 * @return
	 */
	public Map<String,String> getUrlAuthByUser(PageData pd){
		Map<String,String> authMap = new HashMap<String, String>();
		try {
			List<PageData> urlList = (List<PageData>)dao.findForList("AuthMapper.findUrlsByUser", pd);
			for(PageData pb:urlList){
				String url = pb.getString("MENU_URL");
				if(url==null||"".equals(url))
					continue;
				if(url.startsWith("/"))
					url = url.substring(1);
				
				authMap.put("url:"+url.split(".do")[0], "1");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return authMap;
	}
	
	public List<String> getUrlAuthListByUser(String userName){
		PageData pd = new PageData();
		pd.put("USERNAME", userName);
		List<String> retList = new ArrayList<String>();
		try {
			List<PageData> urlList = (List<PageData>)dao.findForList("AuthMapper.findUrlsByUser", pd);
			for(PageData pb:urlList){
				String url = pb.getString("MENU_URL");
				if(url==null||"".equals(url))
					continue;
				retList.add(pb.getString("MENU_NAME"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return retList;
	}
	
	/**
	 * 使用用户名获取用户的按钮权限
	 * @param pd
	 * @return
	 * @author panglin
	 */
	public Map<String,String> getButtonAuthByUser(PageData pd){
		//获取全局设置的权限
		Map allMap = getGlobalBtnAuthByUser(pd);
		Map urlMap = getURLBtnAuthByUser(pd);
		
		allMap.putAll(urlMap);
		
		return allMap;
	}
	
	/**
	 * 获取用户的角色
	 * @param userName
	 * @return
	 */
	public HashSet<String> getRolesByUser(String userName){
		HashSet<String> rolesSet = new HashSet<String>();
		PageData pd = new PageData();
		pd.put("USERNAME", userName);
		try {
			List<PageData> rList = (List<PageData>) dao.findForList("AuthMapper.findRolesByUser", pd);
			for(PageData r : rList){
				rolesSet.add(r.getString("ROLE_CODE"));
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rolesSet;
	}
	
	public List<String> getRolesListByUser(String userName){
		List<String> retStr = new ArrayList<String>();
		PageData pd = new PageData();
		pd.put("USERNAME", userName);
		try {
			List<PageData> rList = (List<PageData>) dao.findForList("AuthMapper.findRolesByUser", pd);
			for(PageData r : rList){
				retStr.add(r.getString("ROLE_NAME"));
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retStr;
	}
	
	/**
	 * 使用用户名获取用户的在每个角色中全局设置的权限
	 * @param pd
	 * @return
	 * @author panglin
	 */
	private Map<String,String> getGlobalBtnAuthByUser(PageData pd){
		Map<String,String> authMap = new HashMap<String, String>();
		try {
			//获取全局设置的按钮
			List<PageData> bList = (List<PageData>) dao.findForList("AuthMapper.findAllButtonByRoleUser", pd);
			
			//获取全局设置的url
			List<PageData> uList = (List<PageData>) dao.findForList("AuthMapper.findAllUrlByRoleUser", pd);
			
			for(PageData pu:uList){
				for(PageData pb:bList){
					authMap.put(pu.getString("MENU_CODE")+":"+pb.getString("BUTTON_CODE"), "1");
				}
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return authMap;
	}
	
	/**
	 * 使用用户名获取用户的在每个角色中单独设置的权限
	 * @param pd
	 * @return
	 * @author panglin
	 */
	private Map<String,String> getURLBtnAuthByUser(PageData pd){
		Map<String,String> authMap = new HashMap<String, String>();
		try {
			List<PageData> list = (List<PageData>) dao.findForList("AuthMapper.findUrlAuthCodeByRoleUser", pd);
			
			for(PageData u:list){
				authMap.put(u.getString("MENU_CODE")+":"+u.getString("BUTTON_CODE"), "1");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return authMap;
	}
	
}
