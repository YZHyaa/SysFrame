package com.mdt.util.mongodb;

import com.mdt.dao.MongoDaoSupport;
import com.mdt.listener.PropertyListener;
import com.mdt.listener.WebAppContextListener;
import com.mdt.util.cache.MsgTopicCacheUtil;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 消息工具类
 * @ClassName: MessageUtil 
 * @Description: TODO
 * @author "PangLin"
 * @date 2016年1月28日 下午1:57:49 
 *
 */
public class MessageUtil {
	
	/**
	 * 发送success消息
	 * @param title
	 * @param content
	 * @param users
	 */
	public static void sendSuccess(String title,String content,List<String> users){
		sendSimple("success", title, content, users);
	}
	
	/**
	 * 发送info消息
	 * @param title
	 * @param content
	 * @param users
	 */
	public static void sendInfo(String title,String content,List<String> users){
		sendSimple("info", title, content, users);
	}
	
	/**
	 * 发送warning消息
	 * @param title
	 * @param content
	 * @param users
	 */
	public static void sendWarning(String title,String content,List<String> users){
		sendSimple("warning", title, content, users);
	}
	
	/**
	 * 发送error消息
	 * @param title
	 * @param content
	 * @param users
	 */
	public static void sendError(String title,String content,List<String> users){
		sendSimple("error", title, content, users);
	}
	
	/**
	 * 发送消息
	 * @param type
	 * @param title
	 * @param content
	 * @param users
	 */
	public static void sendSimple(String type,String title,String content,List<String> users){
		sendMsg(type, "", "", new Date().getTime(), title, content, "", users);
	}
	
	/**
	 * 发送消息
	 * @param type
	 * @param source
	 * @param sender
	 * @param sendtime
	 * @param title
	 * @param content
	 * @param url
	 * @param users
	 */
	public static void sendMsg(String type,String source,String sender,long sendtime,String title,String content,String url,List<String> users){
		String sendcoll = PropertyListener.getPropertyValue("${message.msgSendColl}");
		String msgColl = PropertyListener.getPropertyValue("${message.msgColl}");
		
		
		MongoDaoSupport dao = (MongoDaoSupport) WebAppContextListener.getApplicationContext().getBean("mongoDaoSupport");
		
		Document doc = new Document().append("title", title)
				 .append("content", content)
				 .append("url", url)
				 .append("geneTime", sendtime)
				 .append("source", source)
				 .append("sender", sender)
				 .append("msgType",type);
		
		dao.insert(null, msgColl, doc);
		
		String id = doc.get("_id").toString();
		List docList = new ArrayList();
		
		for(String user:users){
			Document sendDoc = new Document().append("msgid", new ObjectId(id))
					.append("user", user)
					.append("sendTime", sendtime)
					.append("status", 0);
			docList.add(sendDoc);
		}
		
		dao.insertMany(null, sendcoll, docList);
	}
	
	/**
	 * 向消息通道topic中广播消息
	 * @param topicCode topic代码
	 * @param type
	 * @param title
	 * @param content
	 */
	public static void publicMsg(String topicCode,String type,String title,String content){
		String userStr = MsgTopicCacheUtil.getUserNameByTopic(topicCode);
		
		if(!(userStr==null||"".equals(userStr))){
			List<String> users = Arrays.asList(userStr.split(","));
			
			sendSimple(type, title, content, users);
		}
	}
	
}
