package com.mdt.service.system.filesUploads;

import com.mdt.dao.DaoSupport;
import com.mdt.entity.Page;
import com.mdt.listener.PropertyListener;
import com.mdt.util.PageData;
import com.mdt.util.RequestUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FilesUploadsService
 * @author lgq
 * @version 2016-02-15
 */

@Service("filesUploadsService")
@Transactional(readOnly = true)
public class FilesUploadsService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;

	/**
	 * 获取单条数据
	 * @param filesUploads
	 * @return
	 */
	public PageData get(PageData pd) throws Exception {
		return (PageData) dao.findForObject("FilesUploadsMapper.get", pd);
	}

	/**
	 * 查询列表
	 */
	public List<PageData> findListPage(Page pd) throws Exception {
		return (List<PageData>) dao.findForList("FilesUploadsMapper.listPage", pd);
	}

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<PageData> findAllList(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("FilesUploadsMapper.findAllList", pd);
	}

	/**
	 * 保存数据（插入或更新）
	 */
	@Transactional(readOnly = false)
	public void save(PageData pd) throws Exception {
		dao.save("FilesUploadsMapper.insert", pd);
	}

	/**
	 * 保存素材
	 */
	@Transactional(readOnly = false)
	public void saveMaterial(PageData pd) throws Exception {
		dao.save("FilesUploadsMapper.insertMaterial", pd);
	}

	/**
	 * 保存数据（更新）
	 */
	@Transactional(readOnly = false)
	public void update(PageData pd) throws Exception {
		dao.update("FilesUploadsMapper.update", pd);
	}

	/**
	 * 更新数据
	 */
	@Transactional(readOnly = false)
	public void updateMasterId(PageData pd) throws Exception {
		dao.update("FilesUploadsMapper.updateMasterId", pd);
	}

	/**
	 * 保存数据（删除）
	 */
	@Transactional(readOnly = false)
	public void delete(String[] ids) throws Exception {
		dao.delete("FilesUploadsMapper.delete", ids);
	}

	/**
	 * 从云平台下载图片
	 * @throws Exception
	 */
	public void downloadCloudImage(List<PageData> list_syncAlarmPicData, String s_downloadUrl) throws Exception {
		Map<String, String> m_downloadParam = new HashMap<String, String>();
		if (list_syncAlarmPicData != null && list_syncAlarmPicData.size() > 0) {
			for (PageData pd_temp : list_syncAlarmPicData) {
				m_downloadParam.clear();
				m_downloadParam.put("MASTER_ID", pd_temp.getString("MASTER_ID"));
				m_downloadParam.put("FILE_NAME", pd_temp.getString("FILE_NAME"));
				m_downloadParam.put("PATH", PropertyListener.getPropertyValue("${file.basePath}") + pd_temp.getString("PATH"));
				RequestUtil.sendDownload(s_downloadUrl, m_downloadParam);
			}
		}
	}
}