package com.mdt.controller.system.message;

import com.mdt.controller.base.BaseController;
import com.mdt.entity.Page;
import com.mdt.entity.system.User;
import com.mdt.service.system.message.MessageService;
import com.mdt.util.Const;
import com.mdt.util.PageData;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 
 * @ClassName: MessageController 
 * @Description: 消息内容窗口
 * @author "PangLin"
 * @date 2016年1月27日 下午3:01:38 
 *
 */
@Controller
@RequestMapping(value="/message")
public class MessageController extends BaseController {
	
	@Resource(name = "messageService")
	private MessageService messageService;
	
	@RequestMapping(value = "/listAllMsg")
	public ModelAndView listAllMsg() {
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("system/message/msg_list");
		return mv;
	}
	
	@RequestMapping(value = "/pageSearch")
	@ResponseBody
	public Object pageSearch() {
		PageData pd = this.getPageData();
		Subject currentUser = SecurityUtils.getSubject();  
		Session session = currentUser.getSession();
		pd.put("user", ((User)session.getAttribute(Const.SESSION_USER)).getUSERNAME());
		
		Page page = Page.parseBootstrmpTable2Page(pd);
		List<PageData> msgList = null;
		try {
			msgList = messageService.listPdPageMsg(page);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Map dataTable = Page.parsePage2BootstrmpTable(page, msgList);
		return dataTable;
	}
	
	
	
}
