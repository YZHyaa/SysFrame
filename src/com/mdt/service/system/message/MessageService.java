package com.mdt.service.system.message;

import com.mdt.dao.MongoDaoSupport;
import com.mdt.entity.Page;
import com.mdt.listener.PropertyListener;
import com.mdt.util.DateUtil;
import com.mdt.util.PageData;
import com.mdt.util.StringUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Sorts.orderBy;

/**
 * 
 * @ClassName: MessageService 
 * @Description: 消息service，使用mongodb
 * @author "PangLin"
 * @date 2016年2月16日 下午1:54:39 
 *
 */
@Service("messageService")
public class MessageService {

	@Resource(name = "mongoDaoSupport")
	private MongoDaoSupport mongoDao;
	
	public List<PageData> listPdPageMsg(Page page)throws Exception{
		//消息发送集合
		String sendcoll = PropertyListener.getPropertyValue("${message.msgSendColl}");
		//消息内容集合
		String msgColl = PropertyListener.getPropertyValue("${message.msgColl}");
		
		List<PageData> retList = new ArrayList<PageData>();
		
		String user = page.getPd().getString("user");
		page.setEntityOrField(true);	
		
		String title = page.getPd().getString("title");
		
		if(StringUtil.isEmpty(title))
			return getPageList(page, sendcoll, msgColl, user);
		else
			return getPageList(page, sendcoll, msgColl, user, title);
	}
	
	
	//在不使用模糊插叙的情况下获取用户分页列表
	private List<PageData> getPageList(Page page,String sendcoll,String msgColl,String user){
		List<PageData> retList = new ArrayList<PageData>();
		//获取记录总数
		page.setTotalResult(getTotal(sendcoll,user));
		
		//当前页
		long pageNo = page.getCurrentPage();
		//每页记录数量
		long pageSize = page.getShowCount();
		
		Map<String,PageData> tempMap = new HashMap<String, PageData>();
		//分页查询
		MongoCursor<Document> sendCursor = mongoDao.findByPage(null, sendcoll, eq("user",user), null, pageNo, pageSize).iterator();
		List<ObjectId> msgidList = new ArrayList<ObjectId>();
		
		while(sendCursor.hasNext()){
			Document doc = sendCursor.next();
			PageData pd = new PageData();
			ObjectId msgid = doc.getObjectId("msgid");
			msgidList.add(msgid);
			
			pd.put("sendid", doc.getObjectId("_id").toString());
			pd.put("sendTime", DateUtil.purseLongToDate("yyyy-MM-dd HH:mm:ss",doc.getLong("sendTime")));
			pd.put("status", doc.getInteger("status")==0?"未读":"已读");
			tempMap.put(msgid.toString(), pd);
		}
		
		MongoCursor<Document> msgCursor = mongoDao.find(null, msgColl,in("_id",msgidList), orderBy(descending("geneTime"))).iterator();
		
		while(msgCursor.hasNext()){
			Document doc = msgCursor.next();
			String msgid = doc.getObjectId("_id").toString();
			
			if(tempMap.containsKey(msgid)){
				PageData pd = tempMap.get(msgid);
				pd.put("title", doc.getString("title"));
				pd.put("content", doc.getString("content"));
				pd.put("url", doc.getString("url"));
				pd.put("source", doc.getString("source"));
				pd.put("sender", doc.getString("sender"));
				pd.put("msgType", doc.getString("msgType"));
				pd.put("msgid", msgid);
				
				retList.add(pd);
			}
			
		}
		
		return retList;
	}
	
	//在使用模糊插叙的情况下获取用户分页列表
	private List<PageData> getPageList(Page page,String sendcoll,String msgColl,String user,String title){
		List<PageData> retList = new ArrayList<PageData>();
		//根据title查询所有的消息
		BasicDBObject query  = new BasicDBObject();
		query.put("title", Pattern.compile("^.*"+title+".*$",Pattern.CASE_INSENSITIVE));
		MongoCursor<Document>  msgList = mongoDao.find(null, msgColl, query,null).projection(include("_id")).iterator();
		List<ObjectId> msgidList = new ArrayList<ObjectId>();
		while(msgList.hasNext()){
			Document doc = msgList.next();
			msgidList.add(doc.getObjectId("_id"));
		}
		
		//获取记录总数
		page.setTotalResult(getTotal(sendcoll,user,msgidList));
		
		//当前页
		long pageNo = page.getCurrentPage();
		//每页记录数量
		long pageSize = page.getShowCount();
		
		Map<String,PageData> tempMap = new HashMap<String, PageData>();
		//分页查询
		MongoCursor<Document> sendCursor = mongoDao.findByPage(null, sendcoll, and(eq("user",user),in("msgid",msgidList)), null, pageNo, pageSize).iterator();
		
		msgidList.clear();
		
		while(sendCursor.hasNext()){
			Document doc = sendCursor.next();
			PageData pd = new PageData();
			ObjectId msgid = doc.getObjectId("msgid");
			msgidList.add(msgid);
			
			pd.put("sendid", doc.getObjectId("_id").toString());
			pd.put("sendTime", DateUtil.purseLongToDate("yyyy-MM-dd HH:mm:ss",doc.getLong("sendTime")));
			pd.put("status", doc.getInteger("status")==0?"未读":"已读");
			tempMap.put(msgid.toString(), pd);
		}
		
		MongoCursor<Document> msgCursor = mongoDao.find(null, msgColl,in("_id",msgidList), orderBy(descending("geneTime"))).iterator();
		
		while(msgCursor.hasNext()){
			Document doc = msgCursor.next();
			String msgid = doc.getObjectId("_id").toString();
			
			if(tempMap.containsKey(msgid)){
				PageData pd = tempMap.get(msgid);
				pd.put("title", doc.getString("title"));
				pd.put("content", doc.getString("content"));
				pd.put("url", doc.getString("url"));
				pd.put("source", doc.getString("source"));
				pd.put("sender", doc.getString("sender"));
				pd.put("msgType", doc.getString("msgType"));
				pd.put("msgid", msgid);
				
				retList.add(pd);
			}
			
		}
		
		return retList;
	}
	
	//在不使用模糊查询的情况下，统计分页总数
	private int getTotal(String coll,String user){
		int total = 0;
		List<Bson> pipeline = new ArrayList<Bson>();
		
		pipeline.add(match(eq("user",user)));
		
		pipeline.add(project(include("user")));
		
		pipeline.add(group("$user",sum("total",1)));
		
		MongoCursor<Document> cursor = mongoDao.aggregate(null, coll, pipeline).iterator();
		
		if(cursor.hasNext()){
			Document doc = cursor.next();
			total = doc.getInteger("total");
		}
		return total;
	}
	
	//在使用模糊查询的情况下，统计分页总数
	private int getTotal(String coll,String user,List<ObjectId> msgidList){
		int total = 0;
		List<Bson> pipeline = new ArrayList<Bson>();
		
		pipeline.add(match(and(eq("user",user),in("msgid",msgidList))));
		
		pipeline.add(project(include("user")));
		
		pipeline.add(group("$user",sum("total",1)));
		
		MongoCursor<Document> cursor = mongoDao.aggregate(null, coll, pipeline).iterator();
		
		if(cursor.hasNext()){
			Document doc = cursor.next();
			total = doc.getInteger("total");
		}
		return total;
	}
}
