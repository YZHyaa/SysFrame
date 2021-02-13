package com.mdt.service.system.menu;

import com.mdt.dao.DaoSupport;
import com.mdt.entity.system.Menu;
import com.mdt.util.PageData;
import com.mdt.util.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("menuService")
public class MenuService{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	public List<PageData> listMenu(PageData pageData)throws Exception{
		return (List<PageData>) dao.findForList("MenuMapper.getMenuList", pageData);
	}
	
	public List<PageData> listSubMenu(PageData pageData)throws Exception{
		return (List<PageData>) dao.findForList("MenuMapper.getSubMenuList", pageData);
	}
	
	/**
	 * 生成最新的部门ID
	 * @return
	 * @throws Exception
	 */
	public int getNewMenuId() throws Exception{
		List<PageData> list = (List<PageData>) dao.findForList("MenuMapper.findMaxId", null);
		int id = Integer.parseInt(list.get(0).get("MID").toString())+1;
		return id;
	}
	
	public void insertMenu(PageData pageData) throws Exception{
		dao.save("MenuMapper.insertMenu", pageData);
	}
	
	public void updateMenu(PageData pageData) throws Exception{
		dao.save("MenuMapper.updateMenu", pageData);
	}
	
	public void deleteMenu(String[] args) throws Exception{
		dao.delete("MenuMapper.deleteMenu", args);
	}
	
	/**
	 * 根据用户名获取用户的所有菜单
	 * @param userName
	 * @return
	 */
	public List<Menu> listMenuByUserName(String userName) throws Exception{
		List<Menu> retList = new ArrayList<Menu>();
		PageData pd = new PageData();
		pd.put("SHOW", "1");
		
		List<Menu> allList = (List<Menu>) dao.findForList("MenuMapper.listAllMenu", pd);
		
		Map<String,Menu> allMap = new HashMap<String,Menu>();
		Map<String,Menu> tempMap = new HashMap<String,Menu>();
		
		for(Menu menu:allList){
			allMap.put(menu.getMENU_ID(), menu);
		}
		
		pd.put("USERNAME", userName);
		
		List<Menu> userList = (List<Menu>)dao.findForList("MenuMapper.listUserMenu", pd);
		
		for(Menu menu:userList){
			if(tempMap.containsKey(menu.getMENU_ID()))
				continue;
			
			if(StringUtil.isEmpty(menu.getPARENT_ID())||"0".equals(menu.getPARENT_ID())){
				if(allMap.containsKey(menu.getMENU_ID()))
					retList.add(allMap.get(menu.getMENU_ID()));
				tempMap.put(menu.getMENU_ID(), menu);
			}else{
				Menu parentMenu = allMap.get(menu.getPARENT_ID());
				Menu tempMenu = menu;
				do{
					if(parentMenu.getSubMenu()==null){
						parentMenu.setSubMenu(new ArrayList<Menu>());
						parentMenu.getSubMenu().add(tempMenu);
					}else{
						parentMenu.getSubMenu().add(tempMenu);
					}
					
					if(StringUtil.isEmpty(parentMenu.getPARENT_ID())||"0".equals(parentMenu.getPARENT_ID())){
						if(!tempMap.containsKey(parentMenu.getMENU_ID())){
							if(allMap.containsKey(parentMenu.getMENU_ID()))
								retList.add(allMap.get(parentMenu.getMENU_ID()));
							tempMap.put(parentMenu.getMENU_ID(), parentMenu);
						}
						break;	
					}
					tempMenu = parentMenu;
					parentMenu = allMap.get(parentMenu.getPARENT_ID());
				}while(true);
			}
			
		}
	
		return retList;
	}
	
	public List listRoleMenu(PageData pd) throws Exception{
		return (List<PageData>)dao.findForList("MenuMapper.listRoleMenu", pd);
	}
	
	
	public List<Menu> listAllMenu() throws Exception {
		List<Menu> rl = this.listAllParentMenu();
		for(Menu menu : rl){
			List<Menu> subList = this.listSubMenuByParentId(menu.getMENU_ID());
			menu.setSubMenu(subList);
		}
		return rl;
	}
	
	public List<Menu> listAllParentMenu() throws Exception {
		return (List<Menu>) dao.findForList("MenuMapper.listAllParentMenu", null);
		
	}
	
	public List<Menu> listSubMenuByParentId(String parentId) throws Exception {
		return (List<Menu>) dao.findForList("MenuMapper.listSubMenuByParentId", parentId);
		
	}
	
	public List<Menu> listAllMenus() throws Exception{
		return (List<Menu>)dao.findForList("MenuMapper.listAllMenu", null);
	
	}
	
	
}
