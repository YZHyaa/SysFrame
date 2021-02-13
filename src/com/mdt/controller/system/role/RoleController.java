package com.mdt.controller.system.role;

import com.mdt.controller.base.BaseController;
import com.mdt.entity.Page;
import com.mdt.service.system.button.ButtonService;
import com.mdt.service.system.menu.MenuService;
import com.mdt.service.system.role.RoleService;
import com.mdt.util.AppUtil;
import com.mdt.util.BootstrapTreeGridUtil;
import com.mdt.util.BootstrapTreeViewUtil;
import com.mdt.util.PageData;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/role")
public class RoleController extends BaseController {
	String menuUrl = "role/listRoles.do"; // 菜单地址(权限用)

	@Resource(name = "roleService")
	private RoleService roleService;
	@Resource(name = "menuService")
	private MenuService menuService;
	@Resource(name = "buttonService")
	private ButtonService buttonService;

	@RequestMapping(value = "/listRoles")
	public ModelAndView listRoles() {
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("system/role/role_list");
		return mv;
	}

	@RequestMapping(value = "/pageSearch")
	@ResponseBody
	public Object pageSearch() {
		PageData pd = this.getPageData();
		Page page = Page.parseBootstrmpTable2Page(pd);
		List<PageData> userList = null;
		try {
			userList = roleService.listPdPageRole(page);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map dataTable = Page.parsePage2BootstrmpTable(page, userList);
		return dataTable;
	}

	@RequestMapping(value = "/toAdd")
	@RequiresPermissions("role:add")
	public ModelAndView toAdd() {
		PageData pd = new PageData();
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("system/role/role_add");
		mv.addObject("pd", pd);
		return mv;
	}

	/**
	 * 去修改页面
	 */
	@RequestMapping(value = "/toEdit")
	@RequiresPermissions("role:edit")
	public ModelAndView toEdit() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		pd = roleService.findById(pd); // 根据ID读取
		mv.setViewName("system/role/role_edit");
		mv.addObject("pd", pd);
		return mv;
	}

	/**
	 * 保存角色
	 */
	@RequestMapping(value = "/saveAdd")
	@RequiresPermissions("role:add")
	public ModelAndView saveAdd() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();

		pd.put("ROLE_ID", this.get32UUID()); // ID

		if (null == roleService.findById(pd)) {
			roleService.saveRole(pd);
			mv.addObject("msg", "success");
		} else {
			mv.addObject("msg", "failed");
		}
		mv.setViewName("system/role/role_list");
		return mv;
	}

	/**
	 * 修改用户
	 */
	@RequestMapping(value = "/saveEdit")
	@RequiresPermissions("role:edit")
	public ModelAndView saveEdit(HttpServletRequest request) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		roleService.editRole(pd);
		mv.addObject("msg", "success");
		mv.setViewName("system/role/role_list");
		// fileUpload(request);
		return mv;
	}

	/**
	 * 批量删除
	 */
	@RequestMapping(value = "/delete")
	@RequiresPermissions("role:delete")
	@ResponseBody
	public Object delete() {
		PageData pd = new PageData();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			pd = this.getPageData();
			List<PageData> pdList = new ArrayList<PageData>();
			String USER_IDS = pd.getString("IDS");

			if (null != USER_IDS && !"".equals(USER_IDS)) {
				String ArrayUSER_IDS[] = USER_IDS.split(",");
				roleService.deleteAllRoles(ArrayUSER_IDS);
				map.put("msg", "ok");
			} else {
				map.put("msg", "no");
			}
		} catch (Exception e) {
			logger.error(e.toString(), e);
		} finally {
			logAfter(logger);
		}
		return AppUtil.returnObject(pd, map);
	}

	/**
	 * 角色用户
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toUser")
	@RequiresPermissions("role:config")
	public ModelAndView toUser() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		List lUserList = roleService.findNewUserByRole(pd);
		List rUserList = roleService.findUserByRole(pd);

		mv.addObject("roleid", pd.getString("ROLEID"));
		mv.addObject("lUserList", lUserList);
		mv.addObject("rUserList", rUserList);
		mv.setViewName("system/role/role_user");
		return mv;
	}

	/**
	 * 角色菜单
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toMenu")
	@RequiresPermissions("role:config")
	public ModelAndView toMenu() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		mv.addObject("roleid", pd.getString("ROLEID"));
		mv.setViewName("system/role/role_menu");
		return mv;
	}

	/**
	 * 构造角色按钮
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toButton")
	@RequiresPermissions("role:config")
	public ModelAndView toButton() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		mv.addObject("roleid", pd.getString("ROLE_ID"));
		mv.setViewName("system/role/role_button");
		return mv;
	}

	/**
	 * 构造treegrid结构
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getTreeGrid")
	@RequiresPermissions("role:config")
	@ResponseBody
	public Object getButtonTreeGrid() throws Exception {
		PageData pd = this.getPageData();
		// 利用util中的方法类构造treeGrid返回String直接拼入前台table标签中
		String treeGridData = BootstrapTreeGridUtil.getTreeGrid(
				menuService.listAllMenus(), // 所有的菜单项
				menuService.listRoleMenu(pd), // 当前角色的菜单
				buttonService.listPdPageRole(new Page()), // 所有的按钮
				roleService.findGlobaLButtonByRole(pd), // 全局的勾选按钮
				roleService.findButtonByRole(pd)); // 当前角色所属菜单勾选按钮
		String data = URLEncoder.encode(treeGridData.toString(), "UTF-8") // 中文出现乱码则先编码，空格被编码成“+”
																			// 所以替换+号成空格所编码：
				.replace("+", "%20");
		return data;

	}

	/**
	 * 够造角色菜单bootstrap treeview插件
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getTree")
	@RequiresPermissions("role:config")
	@ResponseBody
	public Object getMenuTree() throws Exception {
		PageData pd = this.getPageData();
		// System.out.println(pd.get("ROLE_ID"));
		// List dicTreeList =
		// JQueryJsTreeUtil.getTree(menuService.listMenu(null),
		// roleService.findMenuByRole(pd), "MENU_ID", "PARENT_ID",
		// "MENU_NAME");

		List dicTreeList = BootstrapTreeViewUtil.getCheckTree(
				menuService.listMenu(null), roleService.findMenuByRole(pd),
				"0", "菜单", "MENU_ID", "MENU_NAME", "PARENT_ID");
		return dicTreeList;
	}

	/**
	 * 用户操作
	 */
	@RequestMapping(value = "/editUser")
	@RequiresPermissions("role:config")
	@ResponseBody
	public Object editUser() {
		PageData pd = new PageData();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			pd = this.getPageData();
			// 删除原有用户
			roleService.deleteUserByRole(pd);
			// 重新保存
			List<PageData> pdList = new ArrayList<PageData>();
			String USER_IDS = pd.getString("IDS");
			if (null != USER_IDS && !"".equals(USER_IDS)) {
				String ArrayUSER_IDS[] = USER_IDS.split(",");
				String role = pd.getString("ROLE_ID");
				List urList = new ArrayList();
				for (String id : ArrayUSER_IDS) {
					Map ur = new HashMap();
					ur.put("URID", get32UUID());
					ur.put("USERID", id);
					ur.put("ROLEID", role);
					urList.add(ur);
				}
				if (urList.size() > 0)
					roleService.batchAddUser(urList);
				map.put("msg", "ok");
			} else {
				map.put("msg", "ok");
			}
		} catch (Exception e) {
			map.put("msg", "no");
			logger.error(e.toString(), e);
		}
		return AppUtil.returnObject(pd, map);
	}

	// public Object editUser() {
	// PageData pd = new PageData();
	// Map<String, Object> map = new HashMap<String, Object>();
	// try {
	// pd = this.getPageData();
	// List<PageData> pdList = new ArrayList<PageData>();
	// String USER_IDS = pd.getString("IDS");
	//
	// if (null != USER_IDS && !"".equals(USER_IDS)) {
	// String ArrayUSER_IDS[] = USER_IDS.split(",");
	// String method = pd.getString("method");
	// String role = pd.getString("ROLE_ID");
	//
	// if ("add".equals(method)) {
	// List urList = new ArrayList();
	// for (String id : ArrayUSER_IDS) {
	// Map ur = new HashMap();
	// ur.put("URID", get32UUID());
	// ur.put("USERID", id);
	// ur.put("ROLEID", role);
	// urList.add(ur);
	// }
	//
	// roleService.batchAddUser(urList);
	// } else {
	// PageData param = new PageData();
	// param.put("IDS", ArrayUSER_IDS);
	// param.put("ROLEID", role);
	// roleService.batchDeleteUser(param);
	// }
	//
	// map.put("msg", "ok");
	// } else {
	// map.put("msg", "no");
	// }
	// } catch (Exception e) {
	// logger.error(e.toString(), e);
	// }
	// return AppUtil.returnObject(pd, map);
	// }

	/**
	 * 菜单操作
	 */
	@RequestMapping(value = "/editMenu")
	@RequiresPermissions("role:config")
	@ResponseBody
	public Object editMenu() {
		PageData pd = new PageData();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			pd = this.getPageData();
			String MENU_IDS = pd.getString("MENU_IDS");
			String role = pd.getString("ROLEID");
			
			if (null != MENU_IDS) {
				roleService.deleteMenuByRole(pd);  //删除所有关联关系
				if (!MENU_IDS.equals("")) {
					String ArrayMenu_IDS[] = MENU_IDS.split(",");
					//增加操作
					List rmList = new ArrayList();
					for (String id : ArrayMenu_IDS) {
					    Map rm = new HashMap();
						rm.put("AUTH_ID", get32UUID());
						rm.put("ROLE_ID", role);
						rm.put("MENU_ID", id);
						rmList.add(rm);
					}
					roleService.batchAddMenu(rmList);
				}
				map.put("msg", "ok");

			} else {
				map.put("msg", "no");
			}
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
		return AppUtil.returnObject(pd, map);
	}
	
	

	/**
	 * 按钮操作
	 */
	@RequestMapping(value = "/editButton")
	@RequiresPermissions("role:config")
	@ResponseBody
	public Object editButton() {
		PageData pd = new PageData();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			pd = this.getPageData();
			String gb = pd.getString("gb");
			String mb = pd.getString("mb");
			String role = pd.getString("ROLE_ID");

			if (null == gb)
				gb = "";
			if (null == mb)
				mb = "";

			// 删除全局按钮
			roleService.deleteGlobalButtonByRole(pd);
			// 删除菜单按钮
			roleService.deleteMenuButtonByRole(pd);
			// 新增全局按钮
			String[] gbArr = gb.split(",");
			List<PageData> gList = new ArrayList<PageData>();
			for (String id : gbArr) {
				if (!"".equals(id)) {
					PageData pdData = new PageData();
					pdData.put("AUTH_ID", get32UUID());
					pdData.put("ROLE_ID", role);
					pdData.put("BUTTON_ID", id);
					gList.add(pdData);
				}
			}

			if (gList.size() > 0)
				roleService.batchAddGlobalButton(gList);

			// 新增菜单按钮

			String[] mbArr = mb.split(":");
			List<PageData> mList = new ArrayList<PageData>();
			for (String id : mbArr) {
				if (!"".equals(id)) {
					String[] idArr = id.split(",");
					PageData pdData = new PageData();
					pdData.put("AUTH_ID", get32UUID());
					pdData.put("ROLE_ID", role);
					pdData.put("MENU_ID", idArr[0]);
					pdData.put("BUTTON_ID", idArr[1]);
					mList.add(pdData);
				}
			}

			if (mList.size() > 0)
				roleService.batchAddMenuButton(mList);

			map.put("msg", "ok");
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
		return AppUtil.returnObject(pd, map);
	}
	
	
	
	
	/**
	 * 角色——用户：查询用户
	 * @return
	 */	
	@RequestMapping(value = "/getSerachUser")
	@RequiresPermissions("role:config")
	@ResponseBody
	public Object getSerachUser() throws Exception{
		PageData pd = this.getPageData();
		return roleService.findNewUserByRole(pd);	
	}
	// public Object editButton() {
	// PageData pd = new PageData();
	// Map<String, Object> map = new HashMap<String, Object>();
	// try {
	// pd = this.getPageData();
	// String method = pd.getString("method");
	// String idStr = pd.getString("idStr");
	// String role = pd.getString("ROLE_ID");
	//
	// if (null != idStr) {
	// if (idStr.indexOf(',') == -1) {
	// PageData pdData = new PageData();
	// pdData.put("AUTH_ID", get32UUID());
	// pdData.put("ROLE_ID", role);
	// pdData.put("BUTTON_ID", idStr);
	// if ("add".equals(method)) {
	// roleService.AddButtonByRoleAndButton(pdData);
	// map.put("msg", "ok");
	// } else {
	// roleService.DeleteButtonByRoleAndButton(pdData);
	// map.put("msg", "ok");
	// }
	// } else {
	// PageData pdData = new PageData();
	// pdData.put("AUTH_ID", get32UUID());
	// pdData.put("ROLE_ID", role);
	// pdData.put("MENU_ID", idStr.split(",")[0]);
	// pdData.put("BUTTON_ID", idStr.split(",")[1]);
	// if ("add".equals(method)) {
	// roleService.AddButtonByRoleAndMenuAndButton(pdData);
	// map.put("msg", "ok");
	// } else {
	// roleService.DeleteButtonByRoleAndMenuAndButton(pdData);
	// map.put("msg", "ok");
	// }
	// }
	// } else {
	// map.put("msg", "no");
	// }
	// } catch (Exception e) {
	// logger.error(e.toString(), e);
	// }
	// return AppUtil.returnObject(pd, map);
	// }
}



