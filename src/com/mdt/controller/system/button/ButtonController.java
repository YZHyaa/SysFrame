package com.mdt.controller.system.button;

import com.mdt.controller.base.BaseController;
import com.mdt.entity.Page;
import com.mdt.service.system.button.ButtonService;
import com.mdt.service.system.menu.MenuService;
import com.mdt.service.system.user.UserService;
import com.mdt.util.AppUtil;
import com.mdt.util.PageData;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类名称：ButtonController 创建人：luyun 创建时间：2016年1月18日
 * 
 * @version
 */
@Controller
@RequestMapping(value = "/button")
public class ButtonController extends BaseController {

	String menuUrl = "button/listButtons.do"; // 菜单地址(权限用)
	
	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "buttonService")
	private ButtonService buttonService;
	@Resource(name = "menuService")
	private MenuService menuService;

	@RequestMapping(value = "/listButtons")
	public ModelAndView listButtons() {
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("system/button/button_list");
		return mv;
	}


	
	@RequestMapping(value = "/pageSearch")
	@ResponseBody
	public Object pageSearch() {
		PageData pd = this.getPageData();
		Page page = Page.parseBootstrmpTable2Page(pd);
		List<PageData> buttonList = null;
		try {
			buttonList = buttonService.listPdPageRole(page);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map dataTable = Page.parsePage2BootstrmpTable(page, buttonList);
		return dataTable;
	}
	
	

	@RequestMapping(value = "/toAdd")
	@RequiresPermissions("button:add")
	public ModelAndView toAdd() {
		PageData pd = new PageData();
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("system/button/button_add");
		mv.addObject("pd", pd);
		return mv;
	}

	/**
	 * 去修改按钮页面
	 */
	@RequestMapping(value = "/toEdit")
	@RequiresPermissions("button:edit")
	public ModelAndView toEdit() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		pd = buttonService.findById(pd);
		mv.setViewName("system/button/button_edit");
		mv.addObject("pd", pd);
		return mv;
	}

	/**
	 * 保存用户
	 */
	@RequestMapping(value = "/saveAdd")
	@RequiresPermissions("button:add")
	public ModelAndView saveAdd() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("BUTTON_ID", this.get32UUID()); // ID

		if (null == buttonService.findById(pd)) {
			buttonService.saveButton(pd);
			mv.addObject("msg", "success");
		} else {
			mv.addObject("msg", "failed");
		}
		mv.setViewName("system/button/button_list");
		return mv;
	}

	/**
	 * 修改按钮
	 */
	@RequestMapping(value = "/saveEdit")
	@RequiresPermissions("button:edit")
	public ModelAndView saveEdit() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		buttonService.editButton(pd);
		mv.addObject("msg", "success");
		mv.setViewName("system/button/button_list");
		return mv;
	}

	/**
	 * 批量删除
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	@RequiresPermissions("button:delete")
	public Object delete() {
		PageData pd = new PageData();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			pd = this.getPageData();
			String BUTTON_IDS = pd.getString("IDS");
			if (null != BUTTON_IDS && !"".equals(BUTTON_IDS)) {
				String ArrayBUTTON_IDS[] = BUTTON_IDS.split(",");
				buttonService.deleteAllButtons(ArrayBUTTON_IDS);
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





}
