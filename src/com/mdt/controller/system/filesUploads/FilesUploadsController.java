package com.mdt.controller.system.filesUploads;

import com.mdt.controller.base.BaseController;
import com.mdt.entity.system.User;
import com.mdt.listener.PropertyListener;
import com.mdt.service.system.filesUploads.FilesUploadsService;
import com.mdt.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * FilesUploadsController
 * @author lgq
 * @version 2016-02-15
 */
@Controller
@RequestMapping(value = "filesUploads")
public class FilesUploadsController extends BaseController {

	@Resource(name="filesUploadsService")
	private FilesUploadsService filesUploadsService;
	public static final String BASE_PATH = PropertyListener.getPropertyValue("${file.basePath}");
	
	/**
	 * 文件上传
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/fileUpd")
	@RequiresPermissions("filesUploads:upload")
	public Object fileUpd(HttpServletRequest request) throws Exception {
			List list=new ArrayList();
			try {
				Subject currentUser = SecurityUtils.getSubject();  
				Session session = currentUser.getSession();
				User user = (User)session.getAttribute(Const.SESSION_USER);
				
				String MASTER_ID=request.getParameter("MASTER_ID")+"";
				String yyydd= DateUtil.getMonth();
				//创建一个通用的多部分解析器  
		        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());  
		        //判断 request 是否有文件上传,即多部分请求  
		        if(multipartResolver.isMultipart(request)){  
		            //转换成多部分request    
		            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;  
		            //取得request中的所有文件名  
		            Iterator<String> iter = multiRequest.getFileNames();  
		            while(iter.hasNext()){  
		                //取得上传文件  
		               List<MultipartFile> files= multiRequest.getFiles(iter.next());
		                if(files!= null&&files.size()>0){
		                	for (MultipartFile multipartFile : files) {
		                		//取得当前上传文件的文件名称  
		                        String fileName = multipartFile.getOriginalFilename();
		                        //如果名称不为“”,说明该文件存在，否则说明该文件不存在  
		                        if(fileName.trim() !=""){  
		                        	String id= UuidUtil.get32UUID();
		                            //重命名上传后的文件名  
		                            //String filename = fileName.substring(0,fileName.lastIndexOf("."));
		                            String fileType = fileName.substring(fileName.lastIndexOf("."));
		                            
		                            String newFileName = id+fileType;  
		                            String path=File.separator+yyydd+File.separator+newFileName;
		                            
		                            //硬盘路径是否存在，如果不存在则创建
		                            String dstPath=BASE_PATH +File.separator+yyydd;
		                            File dstFile = new File(dstPath);
		                            if(!dstFile.exists()){
		                                dstFile.mkdirs();
		                            }
		                            //定义上传路径  
		                            String filePath = BASE_PATH + path;  
		                            File localFile = new File(filePath);  
									multipartFile.transferTo(localFile);
									//保存数据库
									PageData pd = this.getPageData();
			                		pd.put("ID", id);
			                		pd.put("FILE_NAME",fileName);
			                		pd.put("BASE_PATH", BASE_PATH);
			                		pd.put("PATH", path);
			                		pd.put("FILE_SIZE", multipartFile.getSize());
			                		pd.put("FILE_TYPE", multipartFile.getContentType());
			                		pd.put("MASTER_ID", MASTER_ID);
			                		pd.put("STATUS", "0");
			                		pd.put("CREATE_ID", user.getUSER_ID());
			                		pd.put("CREATE_TIME", new Date());
			                		filesUploadsService.save(pd);
									Map map=new HashMap();
									map.put("id", id);
									list.add(map);
		                        } 
							}
		                }  
		            }  
		              
		        }  
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		return list;
		}
	
	
	/**
	 * 列表页面
	 * @param model
	 * @return
	 */
	@RequiresPermissions("filesUploads:view")
	@RequestMapping(value = {"list", ""})
	public String list(Model model) {
		return "system/filesUploads/filesUploads_list";
	}
	
	/**
	 * 查询列表
	 * @return
	 */
	@RequiresPermissions("filesUploads:view")
	@ResponseBody
	@RequestMapping(value = "listData")
	public Object listData() {
		PageData pd = this.getPageData();
		List<PageData> filesUploadsList = null;
		try {
			filesUploadsList = filesUploadsService.findAllList(pd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filesUploadsList;
	}
	
	/**
	 * 新增页面
	 * @param model
	 * @return String
	 */
	@RequiresPermissions("filesUploads:add")
	@RequestMapping(value = "toAdd")
	public String toAdd(Model model) {
		PageData pd = new PageData();
		model.addAttribute("pd", pd);
		return "system/filesUploads/filesUploads_add";
	}
	
	/**
	 * 新增
	 * @param model
	 * @return String
	 */
	@RequiresPermissions("filesUploads:add")
	@RequestMapping(value="saveAdd")
	@ResponseBody
	public String saveAdd() throws Exception{
		PageData pd = this.getPageData();
		pd.put("id", UuidUtil.get32UUID());
		filesUploadsService.save(pd);
		//return "system/filesUploads/filesUploads_list";
		return null;
	}
	

	/**
	 * 查看编辑表单
	 * @param filesUploads
	 * @param model
	 * @return
	 */
	@RequiresPermissions("filesUploads:view")
	@RequestMapping(value = "toEdit")
	public String toEdit(Model model) throws Exception{
		PageData pd = this.getPageData();
		PageData filesUploadsData=filesUploadsService.get(pd);
		model.addAttribute("filesUploads",filesUploadsData);
		return "system/filesUploads/filesUploads_edit";
	}
	
	/**
	 * 根据文件ID修改主键ID
	 * @param filesUploads
	 * @param model
	 * @return
	 */
	@RequiresPermissions("filesUploads:edit")
	@RequestMapping(value = "updateMasterId")
	@ResponseBody
	public Object updateMasterId(Model model) throws Exception{
		PageData pd = this.getPageData();
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			String updId = pd.getString("IDS");
			if(null != updId && !"".equals(updId)){
				String[] updIds = updId.split(",");
				pd.put("IDS", Arrays.asList(updIds));
				filesUploadsService.updateMasterId(pd);
				map.put("msg", "ok");
			}else{
				map.put("msg", "no");
			}
		} catch (Exception e) {
			map.put("msg", "no");
			logger.error(e.toString(), e);
		} finally {
			logAfter(logger);
		}
		return AppUtil.returnObject(pd, map);
	}

	/**
	 * 保存数据
	 * @param filesUploads
	 * @return
	 */
	@RequiresPermissions("filesUploads:edit")
	@RequestMapping(value = "saveEdit")
	//@ResponseBody
	public String saveEdit(Model model) throws Exception{
		PageData pd = this.getPageData();
		filesUploadsService.update(pd);
		return "system/filesUploads/filesUploads_list";
		//return null;
	}
	
	/**
	 * 删除数据
	 * @return
	 */
	@RequiresPermissions("filesUploads:delete")
	@ResponseBody
	@RequestMapping(value = "delete")
	public Object delete(Model model) {
		PageData pd = this.getPageData();
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			String delId = pd.getString("IDS");
			if(null != delId && !"".equals(delId)){
				String[] delIds = delId.split(",");
				filesUploadsService.delete(delIds);
				map.put("msg", "ok");
			}else{
				map.put("msg", "no");
			}
		} catch (Exception e) {
			logger.error(e.toString(), e);
		} finally {
			logAfter(logger);
		}
		return AppUtil.returnObject(pd, map);
	}
	/**
	 * 下载文件
	 * @return
	 */
//	@RequiresPermissions("filesUploads:download")
	@RequestMapping(value = "download")
	public Object download(Model model,HttpServletRequest request,HttpServletResponse response) {
		PageData pd = this.getPageData();
		try {
			PageData filesUploadsData=filesUploadsService.get(pd);
			if(filesUploadsData==null){
				return null;
			}
			String fileName=filesUploadsData.getString("FILE_NAME")+"";
			if(StringUtils.isNotBlank(fileName)){
				response.setCharacterEncoding("utf-8");
		        response.setContentType("multipart/form-data");
		        response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
	            String path = filesUploadsData.getString("BASE_PATH")+filesUploadsData.getString("PATH");
	            InputStream inputStream = new FileInputStream(new File(path));
	            OutputStream os = response.getOutputStream();
	            byte[] b = new byte[2048];
	            int length;
	            while ((length = inputStream.read(b)) > 0) {
	                os.write(b, 0, length);
	            }
	             //关闭。
	            os.close();
	            inputStream.close();
			}
			//filesUploadsData.g
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}