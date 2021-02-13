package com.mdt.service.system.button;

import com.mdt.dao.DaoSupport;
import com.mdt.entity.Page;
import com.mdt.util.PageData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("buttonService")
public class ButtonService{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/*
	*列表
	*/
	public List<PageData> listPdPageRole(Page page)throws Exception{
		return (List<PageData>) dao.findForList("ButtonMapper.buttonlistPage", page);
	}
	
	/*
	* 查找按钮
	*/
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("ButtonMapper.findById", pd);
	}
	
	/*
	* 保存按钮
	*/
	public void saveButton(PageData pd)throws Exception{
		dao.save("ButtonMapper.saveButton", pd);
	}
	
	/*
	* 修改按钮
	*/
	public void editButton(PageData pd)throws Exception{
		dao.update("ButtonMapper.editButton", pd);
	}
	
	/*
	* 批量删除按钮
	*/
	public void deleteAllButtons(String[] BUTTON_IDS)throws Exception{
		dao.delete("ButtonMapper.deleteButton", BUTTON_IDS);
	}
	
	

}
