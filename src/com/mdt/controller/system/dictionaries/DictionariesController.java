package com.mdt.controller.system.dictionaries;

import com.mdt.controller.base.BaseController;
import com.mdt.service.system.dictionaries.DictionariesService;
import com.mdt.util.*;
import com.mdt.util.cache.DictionaryCacheUtil;
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
 * @ClassName: DictionariesController 
 * @Description: TODO
 * @author "PangLin"
 * @date 2015年11月26日 下午4:52:29 
 *
 */
@Controller
@RequestMapping("/dic")
public class DictionariesController extends BaseController {
	String menuUrl = "dic/listDics.do"; //菜单地址(权限用)
	
	@Resource(name="dictionariesService")
	private DictionariesService dictionariesService;
	
	@RequestMapping(value="/listDics")
	public ModelAndView listDics(){
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("system/dictionaries/dic_list");
		return mv;
	}
	
	@RequestMapping(value="/getTree")
	@ResponseBody
	public Object getDicTree() throws Exception{
		List dicTreeList = BootstrapTreeViewUtil.getTree(dictionariesService.listDic(null), "0", "数据字典", "ID", "NAME", "PID","code","CODE","levels","LEVELS");
		return dicTreeList;
	}
	
	@RequestMapping(value="/toAdd")
	@RequiresPermissions("dic:add")
	public ModelAndView toAdd(){
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		mv.addObject("pid", pd.getString("pid"));
		mv.addObject("p_code", pd.getString("pcode"));
		mv.addObject("levels", Integer.parseInt(pd.getString("plevels"))+1);
		
		mv.setViewName("system/dictionaries/dic_add");
		return mv;
	}
	
	@RequestMapping(value="/toEdit")
	@RequiresPermissions("dic:edit")
	public ModelAndView toEdit() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		List dicList = dictionariesService.listDic(pd);
		
		if(dicList.size()>0)
			mv.addObject("dic",dicList.get(0));
		else
			mv.addObject("dic",new HashMap());
		
		mv.setViewName("system/dictionaries/dic_edit");
		return mv;
	}
	
	@RequestMapping(value="/saveAdd")
	@RequiresPermissions("dic:add")
	@ResponseBody
	public Object saveAdd() throws Exception{
		PageData pd = this.getPageData();
		pd.put("id", get32UUID());
		String p_code = pd.getString("p_code");
		
		if(!StringUtil.isEmpty(p_code)){
			pd.put("pcode", p_code+"_"+pd.getString("code"));
		}else{
			pd.put("pcode", pd.getString("code"));
		}
		dictionariesService.insertDic(pd);
		return null;
	}
	
	@RequestMapping(value="/saveEdit")
	@RequiresPermissions("dic:edit")
	@ResponseBody
	public Object saveEdit() throws Exception{
		PageData pd = this.getPageData();
		String code = pd.getString("code");
		String pCode = pd.getString("pcode");
		
		if(pCode.indexOf("_")>0){
			pCode = pCode.substring(0, pCode.lastIndexOf("_"))+"_"+code;
		}else{
			pCode = code;
		}
		pd.put("pcode", pCode);
		
		dictionariesService.updateDic(pd);
		return true;
	}
	
	@RequestMapping(value="/delete")
	@RequiresPermissions("dic:delete")
	@ResponseBody
	public Object delete(){
		
		PageData pd = this.getPageData();
		Map<String,Object> msg = new HashMap<String,Object>();
		try {
			String[] idsArr = ListUtil.getSubtreeStrIdsArr(ListUtil.List2TreeMap(dictionariesService.listDic(null), "ID", "PID"), 
							                            "ID", 
							                            pd.getString("id")
							                            );
		
			dictionariesService.deleteDics(idsArr);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msg.put("msg", "no");
		}
		msg.put("msg", "ok");
		
		return AppUtil.returnObject(pd, msg);
	}
	
	@RequestMapping(value="/getSubTree")
	@ResponseBody
	public Object getSubTree() throws Exception{
		PageData pd = this.getPageData();
		List dicList = dictionariesService.listSubDic(pd);
		return dicList;
	}
	
	/**
	 * 在新增子节点前判断父节点的code是否与其他节点存在重复，如果存在重复则无法进行缓存
	 * @return
	 */
	@RequestMapping(value = "/hasSameCode")
	@ResponseBody
	public Object hasSameCode() {
		Map<String, String> map = new HashMap<String, String>();
		String errInfo = "success";
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			if (dictionariesService.findSameCode(pd).size()>0) {
				errInfo = "error";
			}
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
		map.put("result", errInfo); // 返回结果
		return AppUtil.returnObject(new PageData(), map);
	}
	
	/**
	 * 判断当前节点是否有子节点
	 * @return
	 */
	@RequestMapping(value = "/hasChild")
	@ResponseBody
	public Object hasChild() {
		Map<String, String> map = new HashMap<String, String>();
		String errInfo = "true";
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			if (dictionariesService.listSubDic(pd).size()>0) {
				errInfo = "true";
			}else{
				errInfo = "false";
			}
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
		map.put("result", errInfo); // 返回结果
		return AppUtil.returnObject(new PageData(), map);
	}
	
	/**
	 * 根据数据字典code获取数据字典，不包含{}
	 * @return
	 */
	@RequestMapping(value="/getDicByCode")
	@ResponseBody
	public Object getDicByCode(){
		PageData pd = this.getPageData();
		String code = pd.getString("code");
		String cache = DictionaryCacheUtil.getDictionarieStrCache(code);
		if(cache.length()>1){
			cache = cache.substring(1, cache.length()-1);
		}
		return cache;
	}
	
}
