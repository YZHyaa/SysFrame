package com.mdt.service.system.role;

import com.mdt.dao.DaoSupport;
import com.mdt.entity.Page;
import com.mdt.util.PageData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("roleService")
public class RoleService{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/*
	*角色列表(用户组)
	*/
	public List<PageData> listPdPageRole(Page page)throws Exception{
		return (List<PageData>) dao.findForList("RoleMapper.rolelistPage", page);
	}
	
	/*
	* 查找角色
	*/
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("RoleMapper.findById", pd);
	}
	
	/*
	* 保存角色
	*/
	public void saveRole(PageData pd)throws Exception{
		dao.save("RoleMapper.saveRole", pd);
	}
	
	/*
	* 修改角色
	*/
	public void editRole(PageData pd)throws Exception{
		dao.update("RoleMapper.editRole", pd);
	}
	
	/*
	* 批量删除角色
	*/
	public void deleteAllRoles(String[] ROLE_IDS)throws Exception{
		dao.delete("RoleMapper.deleteRole", ROLE_IDS);
	}
	
	
	/*
	 * 根据角色查询已经分配的用户
	 */
	public List findUserByRole(PageData pd) throws Exception{
		return (List<PageData>)dao.findForList("RoleMapper.findUserByRole", pd);
	}
	
	/*
	 * 根据角色查询未分配的用户
	 */
	public List findNewUserByRole(PageData pd) throws Exception{
		return (List<PageData>)dao.findForList("RoleMapper.findNewUserByRole", pd);
	}
	
	/*
	 * 批量增加用户
	 */
	public void batchAddUser(List list) throws Exception {
		dao.batchSave("RoleMapper.addUser", list);
	}
	
	/*
	* 批量删除用户
	*/
	public void batchDeleteUser(PageData param)throws Exception{
		dao.delete("RoleMapper.deleteUser", param);
	}
	
	/**
	 * 根据角色删除用户
	 * @param param
	 * @throws Exception
	 */
	public void deleteUserByRole(PageData param)throws Exception{
		dao.delete("RoleMapper.deleteUserByRole", param);
	}
	
	/*
	 * 根据角色查询已有的菜单
	 * 
	 */
	public List findMenuByRole(PageData pd) throws Exception{
		return (List<PageData>)dao.findForList("RoleMapper.findMenuByRole", pd);
	}
	
	
	/*
	 * 根据角色查询已有的全局按钮
	 * 
	 */
	public List findGlobaLButtonByRole(PageData pd) throws Exception{
		return (List<PageData>)dao.findForList("RoleMapper.findGlobaLButtonByRole", pd);
	}
	
	
	/*
	 * 根据角色查询已有的按钮
	 * 
	 */
	public List findButtonByRole(PageData pd) throws Exception{
		return (List<PageData>)dao.findForList("RoleMapper.findButtonByRole", pd);
	}

	/*
	 * 批量增加菜单
	 */
	public void batchAddMenu(List list) throws Exception {
		dao.batchSave("RoleMapper.addMenu", list);
	}
	
	
	/*
	* 根据角色删除菜单
	*/
	public void deleteMenuByRole(PageData param)throws Exception{
		dao.delete("RoleMapper.deleteMenu", param);
	}

	
	
	/*
	* 根据角色和按钮删除角色按钮关联记录（全局的）
	*/
	public void DeleteButtonByRoleAndButton(PageData param)throws Exception{
		dao.delete("RoleMapper.DeleteButtonByRoleAndButton", param);
	}
	
	/*
	* 根据角色和按钮添加角色按钮关联记录（全局的）
	*/
	public void AddButtonByRoleAndButton(PageData param)throws Exception{
		dao.save("RoleMapper.AddButtonByRoleAndButton", param);
	}
	
	
	/*
	* 根据角色、菜单、按钮删除角色按钮关联记录
	*/
	public void DeleteButtonByRoleAndMenuAndButton(PageData param)throws Exception{
		dao.delete("RoleMapper.DeleteButtonByRoleAndMenuAndButton", param);
	}
	
	/*
	* 根据角色、菜单、按钮添加角色按钮关联记录
	*/
	public void AddButtonByRoleAndMenuAndButton(PageData param)throws Exception{
		dao.save("RoleMapper.AddButtonByRoleAndMenuAndButton", param);
	}
	
	/*
	 * 根据角色删除全局按钮
	 * */
	public void deleteGlobalButtonByRole(PageData param) throws Exception{
		dao.delete("RoleMapper.deleteGlobalButtonByRole", param);
	}
	
	/*
	 * 根据角色删除菜单按钮
	 * */
	public void deleteMenuButtonByRole(PageData param) throws Exception{
		dao.delete("RoleMapper.deleteMenuButtonByRole", param);
	}
	
	/*
	 * 批量增加全局按钮
	 */
	public void batchAddGlobalButton(List list) throws Exception{
		dao.batchSave("RoleMapper.addGlobalButton", list);
	}
	
	/*
	 * 批量增加菜单按钮
	 */
	public void batchAddMenuButton(List list) throws Exception{
		dao.batchSave("RoleMapper.addMenuButton", list);
	}
	
	
	/*
	 * 根据角色查询拥有该角色的用户
	 * 
	 */
	public List findUsersByRoleId(String role_id) throws Exception{
		return (List<String>)dao.findForList("RoleMapper.findUsersByRoleId", role_id);
	}
	
	
}
