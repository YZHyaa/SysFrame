package com.mdt.service.system.dictionaries;


import com.mdt.dao.DaoSupport;
import com.mdt.util.PageData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service("dictionariesService")
public class DictionariesService{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	public List<PageData> listDic(PageData pageData)throws Exception{
		return (List<PageData>) dao.findForList("DictionariesMapper.getDicList", pageData);
	}
	
	public List<PageData> listSubDic(PageData pageData)throws Exception{
		return (List<PageData>) dao.findForList("DictionariesMapper.getSubDicList", pageData);
	}
	
	public List<PageData> lsitSubDicByParentCode(PageData pageData)throws Exception{
		return (List<PageData>) dao.findForList("DictionariesMapper.getSubDicListByParentCode", pageData);
	}
	
	public void insertDic(PageData pageData) throws Exception{
		dao.save("DictionariesMapper.insertDic", pageData);
	}
	
	public void updateDic(PageData pageData) throws Exception{
		dao.save("DictionariesMapper.updateDic", pageData);
	}
	
	public void deleteDics(String[] args) throws Exception{
		dao.delete("DictionariesMapper.deleteDic", args);
	}
	
	public List<PageData> findSameCode(PageData pageData)throws Exception{
		return (List<PageData>) dao.findForList("DictionariesMapper.findSameCode", pageData);
	}

}
