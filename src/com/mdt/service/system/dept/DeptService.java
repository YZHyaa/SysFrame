package com.mdt.service.system.dept;


import com.mdt.dao.DaoSupport;
import com.mdt.util.PageData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("deptService")
public class DeptService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**
	 * 获取部门列表
	 * @param pageData
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listDept(PageData pageData)throws Exception{
		return (List<PageData>) dao.findForList("DeptMapper.getDeptList", pageData);
	}
	
	/**
	 * 获取组织子节点列表
	 * @param pageData
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listSubDept(PageData pageData)throws Exception{
		return (List<PageData>) dao.findForList("DeptMapper.getSubDeptList", pageData);
	}
	
	/**
	 * 生成最新的组织ID
	 * @return
	 * @throws Exception
	 */
	public int getNewDeptId() throws Exception{
		List<PageData> list = (List<PageData>) dao.findForList("DeptMapper.getMaxDeptID", null);
		int id = Integer.parseInt(list.get(0).get("ID").toString())+1;
		return id;
	}
	
	/**
	 * 插入组织
	 * @param pageData
	 * @throws Exception
	 */
	public void insertDept(PageData pageData) throws Exception{
		dao.save("DeptMapper.insertDept", pageData);
	}
	
	/**
	 * 更新组织
	 * @param pageData
	 * @throws Exception
	 */
	public void updateDept(PageData pageData) throws Exception{
		dao.save("DeptMapper.updateDept", pageData);
	}
	
	/**
	 * 删除组织
	 * @param args
	 * @throws Exception
	 */
	public void deleteDepts(String[] args) throws Exception{
		dao.delete("DeptMapper.deleteDept", args);
	}
	
	
	/**
	 * 通过组织机构id获取组织
	 * @param pageData
	 * @return
	 * @throws Exception
	 */
	public List<PageData> findDeptByDeptId(String[] args)throws Exception{
		return (List<PageData>) dao.findForList("DeptMapper.findDeptByDeptId", args);
	}
	
}
