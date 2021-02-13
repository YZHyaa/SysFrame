package com.mdt.controller.system.monitor;

import com.mdt.controller.base.BaseController;
import com.mdt.entity.Page;
import com.mdt.entity.system.User;
import com.mdt.service.system.monitor.MonitorService;
import com.mdt.util.Const;
import com.mdt.util.PageData;
import com.mdt.util.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 系统运行监控
 * @ClassName: MonitorController 
 * @Description: TODO
 * @author "PangLin"
 * @date 2016年4月14日 上午11:02:19 
 *
 */
@Controller
@RequestMapping("/monitor")
public class MonitorController extends BaseController {

	@Resource(name="monitorService")
	MonitorService monitorService;
	
	@RequestMapping(value = "/listMonitors")
	public ModelAndView listMenus() {
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("system/monitor/monitor_list");
		return mv;
	}
	
	@RequestMapping(value="/listPlatforms")
	public ModelAndView listPlatforms(){
		List<PageData> list = monitorService.getAllPlatform();
		ModelAndView mv = this.getModelAndView();
		mv.addObject("list", list);
		mv.setViewName("system/monitor/platforms");
		return mv;
	}
	
	@RequestMapping(value="/listServerAndService")
	public ModelAndView listServerAndService(HttpServletRequest request,HttpServletResponse response){
		String platformid =  request.getParameter("id");
		PageData platform = monitorService.getPlatform(platformid);
		List<PageData> serverList = monitorService.getServersByPlatform(platformid);
		List<Map<String,Object>> serviceList = monitorService.getServiceByPlatform(platformid);
		
		ModelAndView mv = this.getModelAndView();
		mv.addObject("platform", platform);
		mv.addObject("serverList", serverList);
		mv.addObject("serviceList", serviceList);
		mv.setViewName("system/monitor/servers");
		return mv;
	}
	
	/**
	 * 获取实时数据表格
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/getMeasurementData")
	@ResponseBody
	public Object getMeasurementData(HttpServletRequest request,HttpServletResponse response){
		String instanceid =  request.getParameter("id");
		
		return monitorService.getMeasurementDataHtmlTable(instanceid);
	}
	
	/**
	 * 获取实时数据表格
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/getMeasurementChart")
	@ResponseBody
	public Object getMeasurementChart(HttpServletRequest request,HttpServletResponse response){
		String instanceid =  request.getParameter("id");
		
		List list = monitorService.getMeasurementChart(instanceid);
		
		return list;
	}
	
	/**
	 * 获取报警数据分页查询
	 * @return
	 */
	@RequestMapping(value = "/pageSearch")
	@ResponseBody
	public Object pageSearch() {
		PageData pd = this.getPageData();
		Subject currentUser = SecurityUtils.getSubject();  
		Session session = currentUser.getSession();
		pd.put("user", ((User)session.getAttribute(Const.SESSION_USER)).getUSERNAME());
		
		Page page = Page.parseBootstrmpTable2Page(pd);
		List<PageData> alertList = null;
		String insid = page.getPd().getString("insid");
		String meaid = page.getPd().getString("meaid");
		
		if(StringUtil.isEmpty(insid)){
			alertList = new ArrayList<PageData>();
		}else{
			try {
				
				if(StringUtil.isEmpty(meaid))
					alertList = monitorService.getPageAlertByInstance(page, insid);
				else
					alertList = monitorService.getPageAlertByMeasurement(page, meaid);
					
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Map dataTable = Page.parsePage2BootstrmpTable(page, alertList);
		return dataTable;
	}
	
	@RequestMapping(value="/getMeasurementSelect")
	@ResponseBody
	public Object getMeasurementSelect(HttpServletRequest request,HttpServletResponse response){
		String instanceid =  request.getParameter("id");
		return monitorService.getMeasurementSelectOption(instanceid);
	}
	
}
