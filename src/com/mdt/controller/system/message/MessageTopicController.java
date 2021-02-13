package com.mdt.controller.system.message;

import com.mdt.controller.base.BaseController;
import com.mdt.entity.Page;
import com.mdt.service.system.message.MessageTopicService;
import com.mdt.util.AppUtil;
import com.mdt.util.PageData;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息订阅
 * @ClassName: MessageTopicController 
 * @Description: TODO
 * @author "PangLin"
 * @date 2016年1月28日 下午2:22:38 
 *
 */
@Controller
@RequestMapping(value = "/msgTopic")
public class MessageTopicController extends BaseController {
	String menuUrl = "msgTopic/listTopics.do"; // 菜单地址(权限用)
	
	@Resource(name = "messageTopicService")
	private MessageTopicService messageTopicService;
	

	@RequestMapping(value = "/listTopics")
	public ModelAndView listRoles() {
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("system/message/msgTopic_list");
		return mv;
	}

	@RequestMapping(value = "/pageSearch")
	@ResponseBody
	public Object pageSearch() {
		PageData pd = this.getPageData();
		Page page = Page.parseBootstrmpTable2Page(pd);
		List<PageData> topicList = null;
		try {
			topicList = messageTopicService.listPdPageTopic(page);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map dataTable = Page.parsePage2BootstrmpTable(page, topicList);
		return dataTable;
	}
	
	@RequestMapping(value = "/toUser")
	@RequiresPermissions("msgTopic:config")
	public ModelAndView toUser() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		List lUserList = messageTopicService.findNewUserByTopic(pd);
		List rUserList = messageTopicService.findUserByTopic(pd);

		mv.addObject("TOPICID", pd.getString("TOPICID"));
		mv.addObject("lUserList", lUserList);
		mv.addObject("rUserList", rUserList);
		mv.setViewName("system/message/msgTopic_user");
		return mv;
	}
	
	/**
	 * 用户操作
	 */
	@RequestMapping(value = "/editUser")
	@RequiresPermissions("msgTopic:config")
	@ResponseBody
	public Object editUser() {
		PageData pd = new PageData();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			pd = this.getPageData();
			List<PageData> pdList = new ArrayList<PageData>();
			String USER_IDS = pd.getString("IDS");

			if (null != USER_IDS && !"".equals(USER_IDS)) {
				String ArrayUSER_IDS[] = USER_IDS.split(",");
				String method = pd.getString("method");
				String topic = pd.getString("TOPIC_ID");

				if ("add".equals(method)) {
					List urList = new ArrayList();
					for (String id : ArrayUSER_IDS) {
						Map ur = new HashMap();
						ur.put("TUID", get32UUID());
						ur.put("USERID", id);
						ur.put("TOPICID", topic);
						urList.add(ur);
					}

					messageTopicService.batchAddUser(urList);
				} else {
					PageData param = new PageData();
					param.put("IDS", ArrayUSER_IDS);
					param.put("TOPICID", topic);
					messageTopicService.batchDeleteUser(param);
				}

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
