package com.mdt.controller.system.dept;

import com.mdt.controller.base.BaseController;
import com.mdt.service.system.dept.DeptService;
import com.mdt.util.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dept")
public class DepartmentController extends BaseController {
	String menuUrl = "dept/listDepts.do"; //菜单地址(权限用)
	
	@Resource(name="deptService")
	private DeptService deptService;
	
	@RequestMapping(value="/listDepts")
	public String listDepts(Model model) {
		return "system/dept/dept_list";
	}
	
	@RequestMapping(value="/toAdd")
	@RequiresPermissions("dept:add")
	public String toAdd(Model model){
		PageData pd = this.getPageData();
		model.addAttribute("pid", pd.getString("pid"));
		return "system/dept/dept_add";
	}
	
	@RequestMapping(value="/toEdit")
	@RequiresPermissions("dept:edit")
	public String  toEdit(Model model) throws Exception{
		PageData pd = this.getPageData();
		List deptList = deptService.listDept(pd);
		if(deptList.size()>0)
			model.addAttribute("dept",deptList.get(0));
		else
			model.addAttribute("dept",new HashMap());
		
		return "system/dept/dept_edit";
	}
	
	
	
	@RequestMapping(value="/getTree")
	@ResponseBody
	public Object getDeptTree() throws Exception{
		
		List deptTreeList = BootstrapTreeViewUtil.getTree(deptService.listDept(null), "0", "组织结构", "DEPT_ID", "DEPT_NAME", "PARENT_ID");
		return deptTreeList;
	}
	
	
	@RequestMapping(value = "/getSubTree")
	@ResponseBody
	public Object getSubTree() throws Exception {
		PageData pd = this.getPageData();
		List list = deptService.listSubDept(pd);
		return list;
	}
	
	
	
	@RequiresPermissions("dept:add")
	@RequestMapping(value="/saveAdd")
	@ResponseBody
	public Object saveAdd() throws Exception{
		PageData pd = this.getPageData();
		pd.put("id", UuidUtil.get32UUID());
		deptService.insertDept(pd);
		return null;
	}
	
	@RequiresPermissions("dept:edit")
	@RequestMapping(value="/saveEdit")
	@ResponseBody
	public Object saveEdit() throws Exception{
		PageData pd = this.getPageData();
		deptService.updateDept(pd);
		return true;
	}
	
	@RequiresPermissions("dept:delete")
	@RequestMapping(value="/delete")
	@ResponseBody
	public Object delete(){
		
		PageData pd = this.getPageData();
		Map<String,Object> msg = new HashMap<String,Object>();
		try {
			String[] idsArr = ListUtil.getSubtreeIdsArr(ListUtil.List2TreeMap(deptService.listDept(null), "DEPT_ID", "PARENT_ID"),
							                            "DEPT_ID", 
							                            pd.getString("id")
							                            );
		
			deptService.deleteDepts(idsArr);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msg.put("msg", "no");
		}
		msg.put("msg", "ok");
		
		return AppUtil.returnObject(pd, msg);
	}
	
	
}
