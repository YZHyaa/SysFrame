package com.mdt.controller.system.user;

import com.mdt.controller.base.BaseController;
import com.mdt.entity.Page;
import com.mdt.entity.system.User;
import com.mdt.service.system.auth.AuthService;
import com.mdt.service.system.dept.DeptService;
import com.mdt.service.system.menu.MenuService;
import com.mdt.service.system.role.RoleService;
import com.mdt.service.system.user.UserService;
import com.mdt.util.*;
import com.mdt.util.redis.MsgQueue;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 类名称：UserController 创建人：PEGOE 创建时间：2014年6月28日
 * 
 * @version
 */
@Controller
@RequestMapping(value = "/user")
public class UserController extends BaseController {

	String menuUrl = "user/listUsers.do"; // 菜单地址(权限用)

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "roleService")
	private RoleService roleService;

	@Resource(name = "menuService")
	private MenuService menuService;

	@Resource(name = "authService")
	private AuthService authService;

	@Resource(name = "deptService")
	private DeptService deptService;

	@RequestMapping(value = "/listUsers")
	public ModelAndView listUsers() {
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("system/user/user_list");
		return mv;
	}

	@RequestMapping(value = "/pageSerach")
	@ResponseBody
	public Object pageSerach() {
		PageData pd = this.getPageData();
		Page page = Page.parseBootstrmpTable2Page(pd);
		List<PageData> userList = null;
		try {
			userList = userService.listPdPageUser(page);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Map dataTable = Page.parsePage2BootstrmpTable(page, userList);
		return dataTable;
	}

	@RequestMapping(value = "/toAdd")
	@RequiresPermissions("user:add")
	public ModelAndView toAdd() {
		PageData pd = this.getPageData();
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("system/user/user_add");
		mv.addObject("pd", pd);
		return mv;
	}

	/**
	 * 去修改用户页面
	 */
	@RequestMapping(value = "/toEdit")
	@RequiresPermissions("user:edit")
	public ModelAndView toEdit() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		pd = userService.findByUiId(pd); // 根据ID读取
		String[] deptId = pd.getString("DEPT_ID").split(",");
		List<PageData> pdDatas = deptService.findDeptByDeptId(deptId);
		String deptName = "";
		for (PageData pageData : pdDatas) {
			deptName += pageData.getString("DEPT_NAME") + ",";
		}
		if (pdDatas.size() > 0)
			deptName = deptName.substring(0, deptName.length() - 1);
		pd.put("DEPT_NAME", deptName);
		mv.setViewName("system/user/user_edit");
		mv.addObject("pd", pd);
		return mv;
	}

	/**
	 * 保存用户
	 */
	@RequestMapping(value = "/saveAdd")
	@RequiresPermissions("user:add")
	public ModelAndView saveAdd() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();

		pd.put("USER_ID", this.get32UUID()); // ID
		pd.put("LAST_LOGIN", ""); // 最后登录时间
		pd.put("IP", ""); // IP
		pd.put("STATUS", "0"); // 状态
		pd.put("SKIN", "default"); // 默认皮肤

		pd.put("PASSWORD", new SimpleHash("SHA-1", pd.getString("USERNAME"), pd.getString("PASSWORD")).toString());

		if (null == userService.findByUId(pd)) {
			userService.saveU(pd);
			mv.addObject("msg", "success");
		}
		else {
			mv.addObject("msg", "failed");
		}
		mv.setViewName("system/user/user_list");
		return mv;
	}

	/**
	 * 修改用户
	 */
	@RequestMapping(value = "/saveEdit")
	@RequiresPermissions("user:edit")
	public ModelAndView saveEdit() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		if (pd.getString("PASSWORD") != null && !"".equals(pd.getString("PASSWORD"))) {
			pd.put("PASSWORD", new SimpleHash("SHA-1", pd.getString("USERNAME"), pd.getString("PASSWORD")).toString());
		}
		userService.editU(pd);
		mv.addObject("msg", "success");
		mv.setViewName("system/user/user_list");
		return mv;
	}

	/**
	 * 批量删除
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	@RequiresPermissions("user:delete")
	public Object delete() {
		PageData pd = new PageData();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			pd = this.getPageData();
			List<PageData> pdList = new ArrayList<PageData>();
			String USER_IDS = pd.getString("IDS");

			if (null != USER_IDS && !"".equals(USER_IDS)) {
				String ArrayUSER_IDS[] = USER_IDS.split(",");
				userService.deleteAllU(ArrayUSER_IDS);
				userService.deleteUserRole(ArrayUSER_IDS);
				map.put("msg", "ok");
			}
			else {
				map.put("msg", "no");
			}
		}
		catch (Exception e) {
			logger.error(e.toString(), e);
		}
		finally {
			logAfter(logger);
		}
		return AppUtil.returnObject(pd, map);
	}

	@RequestMapping(value = "/export")
	@RequiresPermissions("user:exp")
	public Object export(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		List<PageData> allUser = (List<PageData>) userService.listAllUser(null);
		String cols = "USERNAME:登录名,NAME:姓名,EMAIL:邮件,PHONE:电话,BZ:备注";

		ExcelUtil.exportExcel(allUser, "用户信息", 0, response, cols);

		return null;
	}

	/**
	 * 判断用户名是否存在
	 */
	@RequestMapping(value = "/hasU")
	@ResponseBody
	public Object hasU() {
		Map<String, String> map = new HashMap<String, String>();
		String errInfo = "success";
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			if (userService.findByUId(pd) != null) {
				errInfo = "error";
			}
		}
		catch (Exception e) {
			logger.error(e.toString(), e);
		}
		map.put("result", errInfo); // 返回结果
		return AppUtil.returnObject(new PageData(), map);
	}

	/**
	 * 判断用户是否有新消息
	 */
	@RequestMapping(value = "/hasNewMsg")
	@ResponseBody
	public Object hasNewMsg() {
		MsgQueue msgQueue = new MsgQueue();
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		PageData pd = new PageData();
		String obj = "";
		try {
			pd = this.getPageData();
			User crtUser = (User) session.getAttribute(Const.SESSION_USER);
			obj = msgQueue.getNewMsg(crtUser.getUSER_ID(), (String) pd.get("msgNo"));
			if (obj != null && !obj.equals("") && obj.indexOf("###") != -1) {
				obj = obj.split("###")[0] + "###" + URLEncoder.encode(obj.split("###")[1], "UTF-8") + "###" + obj.split("###")[2];
			}
			else {
				obj = URLEncoder.encode(obj, "UTF-8");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	/**
	 * 判断邮箱是否存在
	 */
	@RequestMapping(value = "/hasE")
	@ResponseBody
	public Object hasE() {
		Map<String, String> map = new HashMap<String, String>();
		String errInfo = "success";
		PageData pd = new PageData();
		try {
			pd = this.getPageData();

			if (userService.findByUE(pd) != null) {
				errInfo = "error";
			}
		}
		catch (Exception e) {
			logger.error(e.toString(), e);
		}
		map.put("result", errInfo); // 返回结果
		return AppUtil.returnObject(new PageData(), map);
	}

	/**
	 * 判断编码是否存在
	 */
	@RequestMapping(value = "/hasN")
	@ResponseBody
	public Object hasN() {
		Map<String, String> map = new HashMap<String, String>();
		String errInfo = "success";
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			if (userService.findByUN(pd) != null) {
				errInfo = "error";
			}
		}
		catch (Exception e) {
			logger.error(e.toString(), e);
		}
		map.put("result", errInfo); // 返回结果
		return AppUtil.returnObject(new PageData(), map);
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format, true));
	}

	@RequestMapping(value = "/updateGuide")
	@ResponseBody
	public void updateGuide() {
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			userService.updateGuide(pd);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 班组选择菜单
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toDeptSelect")
	public ModelAndView toDeptSelect() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		mv.addObject("DEPT_ID", pd.getString("DEPT_ID"));
		mv.setViewName("system/user/user_dept");
		return mv;
	}

	/**
	 * 够造班组树bootstrap treeview插件
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getTree")
	@ResponseBody
	public Object getDeptTree() throws Exception {
		PageData pd = this.getPageData();
		// System.out.println(pd.get("ROLE_ID"));
		// List dicTreeList =
		// JQueryJsTreeUtil.getTree(menuService.listMenu(null),
		// roleService.findMenuByRole(pd), "MENU_ID", "PARENT_ID",
		// "MENU_NAME");
		String[] deptIds = pd.getString("DEPT_ID").split(",");
		List dicTreeList = BootstrapTreeViewUtil.getCheckTree(deptService.listDept(pd), deptService.findDeptByDeptId(deptIds), "0", "班组列表", "DEPT_ID",
				"DEPT_NAME", "PARENT_ID");
		return dicTreeList;
	}

	/**
	 * 修改用户
	 */
	@RequestMapping(value = "/showRP")
	public ModelAndView showRP() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		PageData user = userService.findByUiId(pd);
		String loginName = user.getString("USERNAME");

		List<String> roles = authService.getRolesListByUser(loginName);
		List<String> urls = authService.getUrlAuthListByUser(loginName);
		List<String> globalButton = authService.getGlobalBtnListByUser(loginName);
		List<String> urlButton = authService.getURLBtnListByUser(loginName);

		mv.addObject("roles", roles);
		mv.addObject("urls", urls);
		mv.addObject("globalButton", globalButton);
		mv.addObject("urlButton", urlButton);

		mv.setViewName("system/user/user_rpList");
		return mv;
	}
	
	
	/**
	 * 重置密码
	 */
	@RequestMapping(value = "/resetPwd")
	@ResponseBody
	public Object resetPwd() {
		PageData pd = new PageData();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			pd = this.getPageData();
			String USER_IDS = pd.getString("IDS");
			pd.put("USER_ID", USER_IDS);
			if (null != USER_IDS && !"".equals(USER_IDS)) {
				PageData pData=userService.findByUiId(pd);
				String username=pData.get("USERNAME").toString();
				String passwd = new SimpleHash("SHA-1", username, "123456").toString();	//密码加密
				pd.put("PASSWORD", passwd);
				userService.resetPwd(pd);
				map.put("msg", "ok");
			}
			else {
				map.put("msg", "no");
			}
		}
		catch (Exception e) {
			logger.error(e.toString(), e);
		}
		finally {
			logAfter(logger);
		}
		return AppUtil.returnObject(pd, map);
	}

	
	
	/**
	 * 用户 -角色
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toAddRole")
	@RequiresPermissions("user:config")
	public ModelAndView toAddRole() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		List lRoleList = userService.findNewRoleByUser(pd);
		List rRoleist = userService.findRoleByUser(pd);
		mv.addObject("userId", pd.getString("USER_ID"));
		mv.addObject("lRoleList", lRoleList);
		mv.addObject("rRoleist", rRoleist);
		mv.setViewName("system/user/user_role");
		return mv;
	}
	
	
	/**
	 * 用户_角色：查询角色
	 * @return
	 */	
	@RequestMapping(value = "/getSerachRole")
	@RequiresPermissions("user:config")
	@ResponseBody
	public Object getSerachUser() throws Exception{
		PageData pd = this.getPageData();
		return userService.findNewRoleByUser(pd);
	}
	
	
	
	
	/**
	 * 用户 -角色保存
	 */
	@RequestMapping(value = "/addUserRole")
	@RequiresPermissions("user:config")
	@ResponseBody
	public Object editUser() {
		PageData pd = new PageData();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			pd = this.getPageData();
			// 删除原有角色
			userService.deleteRoleByUser(pd);
			// 重新保存
			List<PageData> pdList = new ArrayList<PageData>();
			String ROLE_IDS = pd.getString("IDS");
			if (null != ROLE_IDS && !"".equals(ROLE_IDS)) {
				String ArrayROLE_IDS[] = ROLE_IDS.split(",");
				String user = pd.getString("USER_ID");
				List urList = new ArrayList();
				for (String id : ArrayROLE_IDS) {
					Map ur = new HashMap();
					ur.put("URID", get32UUID());
					ur.put("USERID", user);
					ur.put("ROLEID", id);
					urList.add(ur);
				}
				if (urList.size() > 0)
					userService.batchAddRole(urList);
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
}
