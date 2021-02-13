package com.mdt.controller.system.menu;

import com.mdt.controller.base.BaseController;
import com.mdt.service.system.menu.MenuService;
import com.mdt.util.*;
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
 * 
 * @ClassName: MenuController
 * @Description: TODO
 * @author "Houruiqi"
 * @date 2015年11月30日 下午1:45:00
 *
 */
@Controller
@RequestMapping("/menu")
public class MenuController extends BaseController {

	String menuUrl = "menu/listMenus.do"; // 菜单地址(权限用)

	@Resource(name = "menuService")
	private MenuService menuService;

	@RequestMapping(value = "/listMenus")
	public ModelAndView listMenus() {
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("system/menu/menu_list");
		return mv;
	}

	@RequestMapping(value = "/getTree")
	@ResponseBody
	public Object getDicTree() throws Exception {
		List dicTreeList = BootstrapTreeViewUtil.getTree(menuService.listMenu(null), "0", "系统菜单", "MENU_ID", "MENU_NAME", "PARENT_ID");
		return dicTreeList;
	}

	@RequestMapping(value = "/toAdd")
	@RequiresPermissions("menu:add")
	public ModelAndView toAdd() {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		mv.addObject("pid", pd.getString("pid"));

		mv.setViewName("system/menu/menu_add");
		return mv;
	}

	@RequestMapping(value = "/toEdit")
	@RequiresPermissions("menu:edit")
	public ModelAndView toEdit() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		List dicList = menuService.listMenu(pd);

		if (dicList.size() > 0)
			mv.addObject("menu", dicList.get(0));
		else
			mv.addObject("menu", new HashMap());

		mv.setViewName("system/menu/menu_edit");
		return mv;
	}

	@RequestMapping(value = "/saveAdd")
	@RequiresPermissions("menu:add")
	@ResponseBody
	public Object saveAdd() throws Exception {
		PageData pd = this.getPageData();
		pd.put("id", UuidUtil.get32UUID());
		menuService.insertMenu(pd);
		return null;
	}

	@RequestMapping(value = "/saveEdit")
	@RequiresPermissions("menu:edit")
	@ResponseBody
	public Object saveEdit() throws Exception {
		PageData pd = this.getPageData();

		menuService.updateMenu(pd);
		return true;
	}

	@RequestMapping(value = "/delete")
	@RequiresPermissions("menu:delete")
	@ResponseBody
	public Object delete() {

		PageData pd = this.getPageData();
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			String[] idsArr = ListUtil.getSubtreeIdsArr(ListUtil.List2TreeMap(
					menuService.listMenu(null), "MENU_ID", "PARENT_ID"),
					"MENU_ID", pd.getString("id"));

			menuService.deleteMenu(idsArr);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msg.put("msg", "no");
		}
		msg.put("msg", "ok");

		return AppUtil.returnObject(pd, msg);
	}

	@RequestMapping(value = "/getSubTree")
	@ResponseBody
	public Object getSubTree() throws Exception {
		PageData pd = this.getPageData();
		List dicList = menuService.listSubMenu(pd);
		return dicList;
	}

}
