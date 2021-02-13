package com.mdt.service.system.monitor;

import com.mdt.dao.PostgresDaoSupport;
import com.mdt.entity.Page;
import com.mdt.util.DateUtil;
import com.mdt.util.PageData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.*;

@Service("monitorService")
public class MonitorService {

	@Resource(name = "postgresDaoSupport")
	private PostgresDaoSupport postgresDaoSupport;
	
	
	/**
	 * 获取硬件服务器
	 * @return
	 */
	public List<PageData> getAllPlatform(){
		StringBuffer sql = new StringBuffer()
		.append("select p.id,p.description,p.cpu_count,pt.name ptype,r.name rname,rt.name rtype,ag.address,ag.port,ag.version ")
		.append("from eam_platform p ")
		.append("left join eam_platform_type pt ")
		.append("on p.platform_type_id = pt.id ")
		.append("left join eam_agent ag ")
		.append("on p.agent_id=ag.id ")
		.append("left join eam_resource r ")
		.append("on p.resource_id=r.id ")
		.append("left join eam_resource_type rt ")
		.append("on r.resource_type_id=rt.id");
		
		return postgresDaoSupport.queryForList(sql.toString());
	}	
	
	/**
	 * 获取应用服务器
	 * @return
	 */
	public List<PageData> getServersByPlatform(String platform){
		StringBuffer sql = new StringBuffer()
		.append("select se.id,se.installpath,set.name,rs.sort_name ")
		.append("from eam_server se ")
		.append("left join eam_server_type set ")
		.append("on se.server_type_id =set.id ")
		.append("left join eam_resource rs ")
		.append("on se.resource_id=rs.id ")
		.append("where set.fvirtual='f' ")
		.append("and se.platform_id="+platform);
		
		return postgresDaoSupport.queryForList(sql.toString());
	}
	
	/**
	 * 获取platform
	 * @param platform
	 * @return
	 */
	public PageData getPlatform(String platform){
		StringBuffer sql = new StringBuffer()
		.append("select * from  eam_platform where id="+platform);
		
		return postgresDaoSupport.queryForObject(sql.toString());
	}
	
	/**
	 * 获取系统服务
	 * 结果是List<Map<String,List<PageData>>>结构,key为服务分类名称,List为同类型的服务
	 * @return 
	 */
	public List<Map<String,Object>> getServiceByPlatform(String platform){
		List<Map<String,Object>> retList = new ArrayList<Map<String,Object>>();
		StringBuffer sql = new StringBuffer()
		 .append("select sv.id,sv.server_id,svt.id server_type_id,svt.name,rs.sort_name from eam_service sv ")
		 .append("left join eam_service_type svt ")
		 .append("on sv.service_type_id = svt.id ")
		 .append("left join eam_resource rs ")
		 .append("on sv.resource_id=rs.id ")
		 .append("left join eam_resource_type rst ")
		 .append("on rs.resource_type_id = rst.id ")
		 .append("where server_id in ")
		 .append("(")
		 .append("select se.id from eam_server se ")
		 .append("left join ")
		 .append("eam_server_type set ")
		 .append("on se.server_type_id =set.id ")
		 .append("where set.fvirtual='t' ")
		 .append("and se.platform_id="+platform)
		 .append(")")
		 .append("order by name asc");
		
		
		List<PageData> serviceList = postgresDaoSupport.queryForList(sql.toString());
		List<String> nameList = new ArrayList<String>();
		String tag = "";
		Map<String,List<PageData>> temp = new HashMap<String, List<PageData>>();
		for(PageData pd:serviceList){
			String name = pd.get("name").toString();
			if(name.equals(tag)){
				if(temp.containsKey(name)){
					temp.get(name).add(pd);
				}else{
					List<PageData> tList = new ArrayList<PageData>();
					tList.add(pd);
					temp.put(name, tList);
				}
			}else{
				tag = name;
				nameList.add(name);
				if(temp.containsKey(name)){
					temp.get(name).add(pd);
				}else{
					List<PageData> tList = new ArrayList<PageData>();
					tList.add(pd);
					temp.put(name, tList);
				}
			}
		}
		
		for(String name:nameList){
			Map<String,Object> tempMap = new HashMap<String, Object>();
			tempMap.put("group", name);
			tempMap.put("service", temp.get(name));
			retList.add(tempMap);
		}
		
		return retList;
	}
	
	/**
	 * 获取监控参数
	 * @param instanceid
	 * @return
	 */
	public List<PageData> getMeasurementByInstance(String instanceid){
		StringBuffer sql = new StringBuffer()
			.append("select m.id,m.coll_interval,tpl.name tplname,tpl.units,cat.name catname from eam_measurement m ")
			.append("left JOIN eam_measurement_templ tpl ")
			.append("on m.template_id = tpl.id ")
			.append("left join eam_measurement_cat cat ")
			.append("on tpl.category_id = cat.id ")
			.append("where m.instance_id = "+instanceid)
			.append(" and m.enabled='t' ")
			.append("order by cat.name asc");
		
		return postgresDaoSupport.queryForList(sql.toString());
	}
	
	public List<PageData> getMeasurementData(String sql){
		return postgresDaoSupport.queryForList(sql);
	}
	
	public Double getPostgresDate(){
		String sql = "select extract(epoch from now())*1000 nowtime";
		PageData pd = postgresDaoSupport.queryForObject(sql);
		return (Double)pd.get("nowtime");
	}
	
	/**
	 * 根据参数id查询当前可用性
	 * @param ids
	 * @return
	 */
	public Map<String,PageData> getAvailvalByMeasurement(String ids){
		Map<String,PageData> retMap = new HashMap<String, PageData>();
		double now = getPostgresDate();
		StringBuffer sql = new StringBuffer()
						   .append("select * from hq_avail_data_rle ")
						   .append("where startime<="+now+" and endtime>="+now+" ")
						   .append("and measurement_id in ("+ids+")");
		List<PageData> list = postgresDaoSupport.queryForList(sql.toString());
		for(PageData pd:list){
			retMap.put(pd.get("measurement_id").toString(), pd);
		}
		return retMap;
	}
	
	/**
	 * 
	 * 根据指标获取报警信息
	 * @param id
	 * @return
	 */
	public List<PageData> getAlertByMeasurement(String id){
		StringBuffer sql = new StringBuffer()
						   .append("select ad.resource_id,")
						   .append("rs.sort_name resource_name,")
						   .append("m.id measurement_id,")
						   .append("tpl.name measurement_name,")
						   .append("ad.name alert_name,")
						   .append("acl.value,")
						   .append("a.ctime ")
						   .append("from eam_alert_condition_log acl ")
						   .append("left join eam_alert a ")
						   .append("on acl.alert_id=a.id ")
						   .append("LEFT join eam_alert_definition ad ")
						   .append("on a.alert_definition_id=ad.id ")
						   .append("LEFT join eam_alert_condition ac ")
						   .append("on acl.condition_id = ac.id ")
						   .append("left join eam_measurement m ")
						   .append("on ac.measurement_id = m.id ")
						   .append("left JOIN eam_measurement_templ tpl ")
						   .append("on m.template_id = tpl.id ")
						   .append("left join eam_resource rs ")
						   .append("on m.resource_id=rs.id ")
						   .append("where m.id = "+id+" ")
						   .append("ORDER BY a.ctime desc");
	        
		return postgresDaoSupport.queryForList(sql.toString());
		
	}
	
	/**
	 * 
	 * 根据指标获取分页报警信息
	 * @param id
	 * @return
	 */
	public List<PageData> getPageAlertByMeasurement(Page page, String id){
		
		page.setTotalResult(getPageAlertByMeasurementTotal(id));
		//当前页
		long pageNo = page.getCurrentPage();
		//每页记录数量
		long pageSize = page.getShowCount();
		
		if(pageNo==0)
			return new ArrayList<PageData>();
		
		long offset = (pageNo-1)*pageSize;
		
		StringBuffer sql = new StringBuffer()
						   .append("select ad.resource_id,")
						   .append("rs.sort_name resource_name,")
						   .append("m.id measurement_id,")
						   .append("tpl.name measurement_name,")
						   .append("ad.name alert_name,")
						   .append("acl.value,")
						   .append("a.ctime ")
						   .append("from eam_alert_condition_log acl ")
						   .append("left join eam_alert a ")
						   .append("on acl.alert_id=a.id ")
						   .append("LEFT join eam_alert_definition ad ")
						   .append("on a.alert_definition_id=ad.id ")
						   .append("LEFT join eam_alert_condition ac ")
						   .append("on acl.condition_id = ac.id ")
						   .append("left join eam_measurement m ")
						   .append("on ac.measurement_id = m.id ")
						   .append("left JOIN eam_measurement_templ tpl ")
						   .append("on m.template_id = tpl.id ")
						   .append("left join eam_resource rs ")
						   .append("on m.resource_id=rs.id ")
						   .append("where m.id = "+id+" ")
						   .append("ORDER BY a.ctime desc ")
						   .append("offset "+offset+" limit "+pageSize);
	        
		return postgresDaoSupport.queryForList(sql.toString());
		
	}
	
	private long getPageAlertByMeasurementTotal(String id){
		StringBuffer sql = new StringBuffer()
		   .append("select count(acl.id) totalsize ")
		   .append("from eam_alert_condition_log acl ")
		   .append("LEFT join eam_alert_condition ac ")
		   .append("on acl.condition_id = ac.id ")
		   .append("left join eam_measurement m ")
		   .append("on ac.measurement_id = m.id ")
		   .append("where m.id = "+id);
		
		PageData result = postgresDaoSupport.queryForObject(sql.toString());
		return Long.parseLong(result.get("totalsize").toString());
	}
	
	
	/**
	 * 
	 * 根据平台或服务获取报警信息
	 * @param id
	 * @return
	 */
	public List<PageData> getAlertByInstance(String id){
		StringBuffer sql = new StringBuffer()
						   .append("select ad.resource_id,")
						   .append("rs.sort_name resource_name,")
						   .append("m.id measurement_id,")
						   .append("tpl.name measurement_name,")
						   .append("ad.name alert_name,")
						   .append("acl.value,")
						   .append("TIMESTAMP WITH TIME ZONE 'epoch'+a.ctime*INTERVAL '1 MILLISECONDS' ctime ")
						   .append("from eam_alert_condition_log acl ")
						   .append("left join eam_alert a ")
						   .append("on acl.alert_id=a.id ")
						   .append("LEFT join eam_alert_definition ad ")
						   .append("on a.alert_definition_id=ad.id ")
						   .append("LEFT join eam_alert_condition ac ")
						   .append("on acl.condition_id = ac.id ")
						   .append("left join eam_measurement m ")
						   .append("on ac.measurement_id = m.id ")
						   .append("left JOIN eam_measurement_templ tpl ")
						   .append("on m.template_id = tpl.id ")
						   .append("left join eam_resource rs ")
						   .append("on m.resource_id=rs.id ")
						   .append("where m.instance_id = "+id+" and m.enabled='t' ")
						   .append("ORDER BY a.ctime desc");
	        
		return postgresDaoSupport.queryForList(sql.toString());
		
	}
	
	/**
	 * 根据实例获取报警分页
	 * @param page
	 * @param id
	 * @return
	 */
	public List<PageData> getPageAlertByInstance(Page page,String id){
		page.setTotalResult(getPageAlertByInstanceTotal(id));
		//当前页
		long pageNo = page.getCurrentPage();
		//每页记录数量
		long pageSize = page.getShowCount();
		
		if(pageNo==0)
			return new ArrayList<PageData>();
		
		long offset = (pageNo-1)*pageSize;
		
		StringBuffer sql = new StringBuffer()
						   .append("select ad.resource_id,")
						   .append("rs.sort_name resource_name,")
						   .append("m.id measurement_id,")
						   .append("tpl.name measurement_name,")
						   .append("ad.name alert_name,")
						   .append("acl.value,")
						   .append("TIMESTAMP WITH TIME ZONE 'epoch'+a.ctime*INTERVAL '1 MILLISECONDS' ctime ")
						   .append("from eam_alert_condition_log acl ")
						   .append("left join eam_alert a ")
						   .append("on acl.alert_id=a.id ")
						   .append("LEFT join eam_alert_definition ad ")
						   .append("on a.alert_definition_id=ad.id ")
						   .append("LEFT join eam_alert_condition ac ")
						   .append("on acl.condition_id = ac.id ")
						   .append("left join eam_measurement m ")
						   .append("on ac.measurement_id = m.id ")
						   .append("left JOIN eam_measurement_templ tpl ")
						   .append("on m.template_id = tpl.id ")
						   .append("left join eam_resource rs ")
						   .append("on m.resource_id=rs.id ")
						   .append("where m.instance_id = "+id+" and m.enabled='t' ")
						   .append("ORDER BY a.ctime desc ")
						   .append("offset "+offset+" limit "+pageSize);
	        
		return postgresDaoSupport.queryForList(sql.toString());
		
	}
	
	private long getPageAlertByInstanceTotal(String id){
		StringBuffer sql = new StringBuffer()
		   .append("select count(acl.id) totalsize ")
		   .append("from eam_alert_condition_log acl ")
		   .append("LEFT join eam_alert_condition ac ")
		   .append("on acl.condition_id = ac.id ")
		   .append("left join eam_measurement m ")
		   .append("on ac.measurement_id = m.id ")
		   .append("where m.instance_id = "+id+" and m.enabled='t' ");
		
		PageData result = postgresDaoSupport.queryForObject(sql.toString());
		return Long.parseLong(result.get("totalsize").toString());
	}
	
	public String getMeasurementSelectOption(String instanceid){
		StringBuffer retStr = new StringBuffer()
		   .append("<option value=\"\">参数</option>");
		
		StringBuffer sql = new StringBuffer()
		   .append("select m.id,emt.name,m.coll_interval from eam_measurement m ")
		   .append("left JOIN eam_measurement_templ emt ")
		   .append("on m.template_id=emt.id ")
		   .append("where instance_id=")
		   .append(instanceid)
		   .append(" and enabled='t' ")
		   .append("ORDER BY emt.name asc,coll_interval asc");
		
		List<PageData> mList = postgresDaoSupport.queryForList(sql.toString());
		
		Map<String,String> tempMap = new HashMap<String, String>();
		
		for(PageData pd:mList){
			String name = pd.getString("name");
			if(tempMap.containsKey(name))
				continue;
			else{
				tempMap.put(name, name);
				retStr.append("<option value=\""+pd.get("id")+"\">"+name+"</option>");
			}
		}
		
		return retStr.toString();
		
	}
	
	public String getMeasurementDataHtmlTable(String instanceid){
		//获取全部指标
		List<PageData> measurements = getMeasurementByInstance(instanceid);
		String mids = "";
		Double time = getPostgresDate();
		
		Map<String,String> catNameMap = new HashMap<String, String>();
		catNameMap.put("AVAILABILITY", "可用性");
		catNameMap.put("PERFORMANCE", "性能");
		catNameMap.put("THROUGHPUT", "吞吐量");
		catNameMap.put("UTILIZATION", "使用率");
		
		
		StringBuffer sql = new StringBuffer();
		
		//获取每个指标的实时数据
		for(int i=0;i<measurements.size();i++){
			PageData m = measurements.get(i);
			String id = m.get("id").toString();
			
			if(i>0){
				sql.append(" union all ");
				mids +=",";
			}
			
			mids += id;
			sql.append("select * from eam_measurement_data ")
			   .append("where measurement_id="+id+" ")
			   .append("and timestamp>="+(time-Double.parseDouble(m.get("coll_interval").toString())));
		}
		
		List<PageData> measurmentData = getMeasurementData(sql.toString());
		
		Map<String,Double> measurmentDataMap = new HashMap<String, Double>();
		for(PageData pd:measurmentData){
			measurmentDataMap.put(pd.get("measurement_id").toString(), Double.parseDouble(pd.get("value").toString()));
		}
		
		//获取统计值
		StringBuffer grpSql = new StringBuffer()
		                          .append("select measurement_id,max(value),min(value),avg(value) from eam_measurement_data ")
		                          .append("where measurement_id in ("+mids+") ")
		                          .append("group by measurement_id");
		
		List<PageData> grpData = getMeasurementData(grpSql.toString());
		
		Map<String,List<Double>> grpDataMap = new HashMap<String, List<Double>>();
		for(PageData pd:grpData){
			List<Double> dataList = new ArrayList<Double>();
			dataList.add(Double.parseDouble(pd.get("min").toString()));
			dataList.add(Double.parseDouble(pd.get("avg").toString()));
			dataList.add(Double.parseDouble(pd.get("max").toString()));
			grpDataMap.put(pd.get("measurement_id").toString(), dataList);
		}
		
		//生成表格
		List<String> catNameList = new ArrayList<String>();
		Map<String,List<PageData>> catMap = new HashMap<String, List<PageData>>();
		for(PageData pd:measurements){
			String tmpCatName = pd.get("catname").toString();
			if(catMap.containsKey(tmpCatName)){
				catMap.get(tmpCatName).add(pd);
			}else{
				catNameList.add(tmpCatName);
				List<PageData> tempList = new ArrayList<PageData>();
				tempList.add(pd);
				catMap.put(tmpCatName, tempList);
			}
		}
		
		StringBuffer htmlBuilder = new StringBuffer();
		
		for(String cat:catNameList){
			htmlBuilder.append("<tr style=\"background-color:antiquewhite;\"><td colspan=6>").append(catNameMap.get(cat)).append("</td></tr>");
			
			List<PageData> mList = catMap.get(cat);
			
			//可用性只保留一个采集频率最快的即可
			if("AVAILABILITY".equals(cat)){
				PageData one = null;
				long intv = 0;
				
				for(int i=0;i<mList.size();i++){
					PageData m = mList.get(0);
					long intv2 = (Long)m.get("coll_interval");
					if(i==0){
						one = m;
						intv = intv2;
					}else{
						if(intv2<intv){
							one = m;
							intv = intv2;
						}
					}
				}
				
				String mid = one.get("id").toString();
				Map<String,PageData> am = getAvailvalByMeasurement(mid);
				if(am.containsKey(mid))
					htmlBuilder.append("<tr><td>").append(one.get("tplname")).append("</td>")
					   .append("<td>-</td>")
					   .append("<td>-</td>")
					   .append("<td>-</td>")
					   .append("<td>可用</td>")
					   .append("<td>").append((Long)one.get("coll_interval")/1000).append("</td></tr>");
				else
					htmlBuilder.append("<tr><td>").append(one.get("tplname")).append("</td>")
					   .append("<td>-</td>")
					   .append("<td>-</td>")
					   .append("<td>-</td>")
					   .append("<td>不可用</td>")
					   .append("<td>").append((Long)one.get("coll_interval")/1000).append("</td></tr>");
				
				
			}else{
				for(PageData m:mList){
					String mid = m.get("id").toString();
					String unit = m.get("units").toString();
					String minStr = "";
					String avgStr = "";
					String maxStr = "";
					String curStr = "";
					
					DecimalFormat df=new DecimalFormat("#.##");
					DecimalFormat df3=new DecimalFormat("#.###");
					
					if(grpDataMap.containsKey(mid)){
						Double min = grpDataMap.get(mid).get(0);
						Double avg = grpDataMap.get(mid).get(1);
						Double max = grpDataMap.get(mid).get(2);
						
						if("percentage".equals(unit)||"percent".equals(unit)){
							minStr = df.format(min*100)+"%";
							avgStr = df.format(avg*100)+"%";
							maxStr = df.format(max*100)+"%";
						}else if("b".equals(unit)){
							minStr = df.format(min/(8*1024*1024))+"MB";
							avgStr = df.format(avg/(8*1024*1024))+"MB";
							maxStr = df.format(max/(8*1024*1024))+"MB";
						}else if("B".equals(unit)){
							minStr = df.format(min/(1024*1024))+"MB";
							avgStr = df.format(avg/(1024*1024))+"MB";
							maxStr = df.format(max/(1024*1024))+"MB";
						}else if("KB".equals(unit)){
							minStr = df.format(min/1024)+"MB";
							avgStr = df.format(avg/1024)+"MB";
							maxStr = df.format(max/1024)+"MB";
						}else if("ms".equals(unit)){
							minStr = df3.format(min/1000)+"S";
							avgStr = df3.format(avg/1000)+"S";
							maxStr = df3.format(max/1000)+"S";
						}else if("sec".equals(unit)){
							minStr = df3.format(min)+"S";
							avgStr = df3.format(avg)+"S";
							maxStr = df3.format(max)+"S";
						}else if("epoch-millis".equals(unit)){
							minStr = DateUtil.purseLongToDate(min.longValue());
							avgStr = DateUtil.purseLongToDate(avg.longValue());
							maxStr = DateUtil.purseLongToDate(max.longValue());
						}else if("epoch-seconds".equals(unit)){
							minStr = DateUtil.purseLongToDate(min.longValue()*1000);
							avgStr = DateUtil.purseLongToDate(avg.longValue()*1000);
							maxStr = DateUtil.purseLongToDate(max.longValue()*1000);
						}else{
							minStr = df3.format(min)+("none".equals(unit)?"":unit);
							avgStr = df3.format(avg)+("none".equals(unit)?"":unit);
							maxStr = df3.format(max)+("none".equals(unit)?"":unit);
						}
					}else{
						minStr = "-";
						avgStr = "-";
						maxStr = "-";
					}
					
					if(measurmentDataMap.containsKey(mid)){
						Double cur = measurmentDataMap.get(mid);
						
						if("percentage".equals(unit)||"percent".equals(unit)){
							curStr = df.format(cur*100)+"%";
						}else if("b".equals(unit)){
							curStr = df.format(cur/(8*1024*1024))+"MB";
						}else if("B".equals(unit)){
							curStr = df.format(cur/(1024*1024))+"MB";
						}else if("KB".equals(unit)){
							curStr = df.format(cur/1024)+"MB";
						}else if("ms".equals(unit)){
							curStr = df3.format(cur/1000)+"S";
						}else if("sec".equals(unit)){
							curStr = df3.format(cur)+"S";
						}else if("epoch-millis".equals(unit)){
							curStr = DateUtil.purseLongToDate(cur.longValue());
						}else if("epoch-seconds".equals(unit)){
							curStr = DateUtil.purseLongToDate(cur.longValue()*1000);
						}else{
							curStr = df3.format(cur)+("none".equals(unit)?"":unit);
						}
					}else{
						curStr = "-";
					}
					
					
					htmlBuilder.append("<tr><td>").append(m.get("tplname")).append("</td>")
							   .append("<td>").append(minStr).append("</td>")
							   .append("<td>").append(avgStr).append("</td>")
							   .append("<td>").append(maxStr).append("</td>")
							   .append("<td>").append(curStr).append("</td>")
							   .append("<td>").append((Long)m.get("coll_interval")/1000).append("</td></tr>");
				}
			}
			
		}
		
		return htmlBuilder.toString();
	}
	
	public List<Map<String,List<Object>>> getMeasurementChart(String instanceid){
		//获取全部指标
		List<PageData> measurements = getMeasurementByInstance(instanceid);
		String mids = "";
		Double time = getPostgresDate();
		
		Map<String,String> catNameMap = new HashMap<String, String>();
		catNameMap.put("AVAILABILITY", "可用性");
		catNameMap.put("PERFORMANCE", "性能");
		catNameMap.put("THROUGHPUT", "吞吐量");
		catNameMap.put("UTILIZATION", "使用率");
		
		
		List<String> idList = new ArrayList<String>();
		//获取每个指标的实时数据
		for(int i=0;i<measurements.size();i++){
			PageData m = measurements.get(i);
			String id = m.get("id").toString();
			idList.add(id);
			if("".equals(mids)){
				mids = id;
			}else{
				mids += ","+id;
			}
		}
		
		String sql = "select * from eam_measurement_data where measurement_id in ("+mids+") and timestamp>="+(time-86400000)+" order by timestamp asc";
		
		List<PageData> measurmentData = getMeasurementData(sql);
		
		Map<String,Map<String,List<Object>>> dataMap = new HashMap<String, Map<String,List<Object>>>();
		
		for(PageData pd:measurmentData){
			String mid = pd.get("measurement_id").toString();
			Date date = new Date(Long.parseLong(pd.get("timestamp").toString()));
			
			if(dataMap.containsKey(mid)){
				dataMap.get(mid).get("yAxis").add(pd.get("value"));
				dataMap.get(mid).get("xAxis").add(date);
			}else{
				List<Object> dataList = new ArrayList<Object>();
				List<Object> timeList = new ArrayList<Object>();
				dataList.add(pd.get("value"));
				timeList.add(date);
				Map<String,List<Object>> tMap = new HashMap<String, List<Object>>();
				tMap.put("yAxis", dataList);
				tMap.put("xAxis", timeList);
				dataMap.put(mid, tMap);
			}
		}
		
		List<Map<String,List<Object>>> retList = new ArrayList<Map<String,List<Object>>>();
		
		
		for(int i=0;i<measurements.size();i++){
			PageData m = measurements.get(i);
			String id = m.get("id").toString();
			String cat = m.get("catname").toString();
			
			List<Object> tList = new ArrayList<Object>();
			tList.add(catNameMap.get(cat));
			tList.add(m.get("tplname"));
			if(dataMap.containsKey(id)){
				dataMap.get(id).put("name", tList);
				retList.add(dataMap.get(id));
			}
		}
		return retList;
	}
	
}
