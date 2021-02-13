package com.mdt.service.system.message;

import com.mdt.dao.DaoSupport;
import com.mdt.entity.Page;
import com.mdt.util.PageData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service("messageTopicService")
public class MessageTopicService {
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/*
	*topic列表
	*/
	public List<PageData> listPdPageTopic(Page page)throws Exception{
		return (List<PageData>) dao.findForList("MsgTopicMapper.topiclistPage", page);
	}
	
	/*
	 * 根据topic查询已经分配的用户
	 */
	public List findUserByTopic(PageData pd) throws Exception{
		return (List<PageData>)dao.findForList("MsgTopicMapper.findUserByTopic", pd);
	}
	
	/*
	 * 根据topic查询未分配的用户
	 */
	public List findNewUserByTopic(PageData pd) throws Exception{
		return (List<PageData>)dao.findForList("MsgTopicMapper.findNewUserByTopic", pd);
	}
	
	/*
	 * 批量增加用户
	 */
	public void batchAddUser(List list) throws Exception {
		dao.batchSave("MsgTopicMapper.addUser", list);
	}
	
	/*
	* 批量删除用户
	*/
	public void batchDeleteUser(PageData param)throws Exception{
		dao.delete("MsgTopicMapper.deleteUser", param);
	}
	
	/*
	 * 根据topic获取订阅用户列表
	 * @return
	 * @throws Exception
	 */
	public List findUserGroupByTopic() throws Exception{
		List<PageData> ret = new ArrayList<PageData>();
		
		List<PageData> userList = (List<PageData>)dao.findForList("MsgTopicMapper.findConfigUserByTopic", null);
		
		Map<String, String> tmp = new HashMap<String,String>();
		for(PageData t:userList){
			String code = t.getString("TOPIC_CODE");
			String user = t.getString("USERNAME");
			if(tmp.containsKey(code)){
				String users = tmp.get(code)+","+user;
				tmp.put(code, users);
			}else{
				tmp.put(code, user);
			}
		}
		
		Iterator<String> it = tmp.keySet().iterator();
		
		while(it.hasNext()){
			String code = it.next();
			String users = tmp.get(code);
			PageData tu = new PageData();
			tu.put("TOPICCODE", code);
			tu.put("TOPICUSER", users);
			ret.add(tu);
		}
		
		
		return ret;
	}
	
}
