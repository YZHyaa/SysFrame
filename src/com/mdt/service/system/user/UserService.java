package com.mdt.service.system.user;

import com.mdt.dao.DaoSupport;
import com.mdt.entity.Page;
import com.mdt.entity.system.User;
import com.mdt.util.PageData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("userService")
public class UserService {
	@Resource(name = "daoSupport")
	private DaoSupport dao;

	// ======================================================================================
	/*
	 * 通过id获取数据
	 */
	public PageData findByUiId(PageData pd) throws Exception {
		return (PageData) dao.findForObject("UserMapper.findByUiId", pd);
	}

	/*
	 * 通过loginname获取数据
	 */
	public PageData findByUId(PageData pd) throws Exception {
		return (PageData) dao.findForObject("UserMapper.findByUId", pd);
	}

	/*
	 * 通过邮箱获取数据
	 */
	public PageData findByUE(PageData pd) throws Exception {
		return (PageData) dao.findForObject("UserMapper.findByUE", pd);
	}

	/*
	 * 通过编号获取数据
	 */
	public PageData findByUN(PageData pd) throws Exception {
		return (PageData) dao.findForObject("UserMapper.findByUN", pd);
	}

	/*
	 * 保存用户
	 */
	public void saveU(PageData pd) throws Exception {
		dao.save("UserMapper.saveU", pd);
	}

	/*
	 * 修改用户
	 */
	public void editU(PageData pd) throws Exception {
		dao.update("UserMapper.editU", pd);
	}

	/*
	 * 换皮肤
	 */
	public void setSKIN(PageData pd) throws Exception {
		dao.update("UserMapper.setSKIN", pd);
	}

	/*
	 * 删除用户
	 */
	public void deleteU(PageData pd) throws Exception {
		dao.delete("UserMapper.deleteU", pd);
	}

	/*
	 * 批量删除用户
	 */
	public void deleteAllU(String[] USER_IDS) throws Exception {
		dao.delete("UserMapper.deleteAllU", USER_IDS);
	}
	/*
	 * 批量删除用户角色关系
	 */
	public void deleteUserRole(String[] USER_IDS) throws Exception {
		dao.delete("UserMapper.deleteUserRole", USER_IDS);
	}

	/*
	 * 用户列表(用户组)
	 */
	public List<PageData> listPdPageUser(Page page) throws Exception {
		return (List<PageData>) dao.findForList("UserMapper.userlistPage", page);
	}

	/*
	 * 用户列表(全部)
	 */
	public List<PageData> listAllUser(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("UserMapper.listAllUser", pd);
	}

	/*
	 * 用户列表(供应商用户)
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listGPdPageUser(Page page) throws Exception {
		return (List<PageData>) dao.findForList("UserMapper.userGlistPage", page);
	}

	/*
	 * 保存用户IP
	 */
	public void saveIP(PageData pd) throws Exception {
		dao.update("UserMapper.saveIP", pd);
	}

	/*
	 * 登录判断
	 */
	public PageData getUserByNameAndPwd(PageData pd) throws Exception {
		return (PageData) dao.findForObject("UserMapper.getUserInfo", pd);
	}

	/*
	 * 跟新登录时间
	 */
	public void updateLastLogin(PageData pd) throws Exception {
		dao.update("UserMapper.updateLastLogin", pd);
	}

	/*
	 * 通过id获取数据
	 */
	public User getUserAndRoleById(String USER_ID) throws Exception {
		return (User) dao.findForObject("UserMapper.getUserAndRoleById", USER_ID);
	}

	/**
	 * 修改密码
	 */
	public void updatePwd(PageData pd) throws Exception {
		dao.update("UserMapper.editPwd", pd);
	}

	public PageData getUserByUsername(PageData pd) throws Exception {
		return (PageData) dao.findForObject("UserMapper.getUserInfoByUsername", pd);
	}

	public void updateGuide(PageData pd) throws Exception {
		dao.update("UserMapper.updateGuide", pd);
	}
	
	public void resetPwd(PageData pd) throws Exception {
		dao.update("UserMapper.resetPwd", pd);
	}
	
	
	/**
	 * 获取用户的当前角色 
	 */
	public List<PageData> findRoleByUser(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("UserMapper.findRoleByUser", pd);
	}
	

	/**
	 * 获取用户尚未选择的角色 
	 */
	public List<PageData> findNewRoleByUser(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("UserMapper.findNewRoleByUser", pd);
	}
	
	public void deleteRoleByUser(PageData pd) throws Exception {
		dao.delete("UserMapper.deleteRoleByUser", pd);
	}
	
	
	/**
	 * 批量增加角色
	 */
	public void batchAddRole(List list) throws Exception {
		dao.batchSave("UserMapper.addRoleByUser", list);
	}
	
	
	
}
