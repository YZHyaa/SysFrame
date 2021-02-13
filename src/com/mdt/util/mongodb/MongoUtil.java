package com.mdt.util.mongodb;

import com.mdt.listener.PropertyListener;
import com.mdt.util.Const;
import com.mdt.util.StringUtil;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * mongoDb工具类
 * @ClassName: MongoUtil 
 * @Description: TODO
 * @author "PangLin"
 * @date 2016年1月4日 上午11:30:34 
 *
 */
public class MongoUtil {

	private static MongoClient mongoClient = null;
	
	/**
	 * 获取mongoClient
	 * @return
	 */
	public static MongoClient getMongoClient(){
		if(mongoClient==null){
			synchronized (MongoUtil.class) {
				if(mongoClient==null){
					 
					Builder options = new MongoClientOptions.Builder();
					
					String connectionsPerHost = PropertyListener.getPropertyValue("${mongodb.connectionsPerHost}");
					if(!StringUtil.isEmpty(connectionsPerHost)){
						options.connectionsPerHost(Integer.parseInt(connectionsPerHost));//连接池连接数
					}
					String connectTimeout = PropertyListener.getPropertyValue("${mongodb.connectTimeout}");
					if(!StringUtil.isEmpty(connectTimeout)){
						options.connectTimeout(Integer.parseInt(connectTimeout));//连接超时，推荐>3000毫秒
					}
					String socketKeepAlive = PropertyListener.getPropertyValue("${mongodb.socketKeepAlive}");
					if(!StringUtil.isEmpty(socketKeepAlive)){
						options.socketKeepAlive(Boolean.parseBoolean(socketKeepAlive));//是否保持长连接
					}
					String maxWaitTime = PropertyListener.getPropertyValue("${mongodb.maxWaitTime}");
					if(!StringUtil.isEmpty(maxWaitTime)){
						options.maxWaitTime(Integer.parseInt(maxWaitTime));//长连接的最大等待时间
					}
					String socketTimeout = PropertyListener.getPropertyValue("${mongodb.socketTimeout}");
					if(!StringUtil.isEmpty(socketTimeout)){
						options.socketTimeout(Integer.parseInt(socketTimeout));//套接字超时时间，0为无限
					}
					String tatbfcm = PropertyListener.getPropertyValue("${mongodb.threadsAllowedToBlockForConnectionMultiplier}");
					if(!StringUtil.isEmpty(tatbfcm)){
						options.threadsAllowedToBlockForConnectionMultiplier(Integer.parseInt(tatbfcm));//线程队列数，如果连接线程排满了队列就会抛出“out of semaphores to get db” 错误
					}
					
					//options.writeConcern(WriteConcern.SAFE);
					
					Boolean auth = Boolean.valueOf(PropertyListener.getPropertyValue("${mongodb.auth}"));
					String db = PropertyListener.getPropertyValue("${mongodb.db}");
					
					if(auth){
						String connectUrl = "mongodb://"
								          + PropertyListener.getPropertyValue("${mongodb.user}")+":"+PropertyListener.getPropertyValue("${mongodb.pwd}")
								          + "@"+PropertyListener.getPropertyValue("${mongodb.url}")
								          + "/?authSource="+ db;
						
						MongoClientURI connectionUri = new MongoClientURI(connectUrl,options);
						
					    mongoClient = new MongoClient(connectionUri);
						
					}else{
						mongoClient = new MongoClient(new ServerAddress(PropertyListener.getPropertyValue("${mongodb.url}")),options.build());
					}
					
					Const.MONGODB_AUTH = auth;
					Const.MONGODB_DEFAULT_DB  = db;
				}
			}
		}
		
		return mongoClient;
		
	}
	
	/**
	 * 获取数据库
	 * @param dbName
	 * @return
	 */
	public static MongoDatabase getDB(String dbName){
		MongoDatabase database = null;
		if(getMongoClient()!=null){
			database = mongoClient.getDatabase(dbName);
		}
		return database;
	}
	
	/**
	 * 获取指定db的指定的collection,如果db未空，则获取默认数据库的collection
	 * @param db
	 * @param collection
	 * @return
	 */
	public static MongoCollection getMongoCollection(String db,String collection){
		
		MongoCollection<Document> coll = null;
		
		if(getMongoClient()!=null){
			
			MongoDatabase database = null;
			//如果开启权限验证，则使用配置文件中的默认数据库
			if(Const.MONGODB_AUTH){
				database = getDB(Const.MONGODB_DEFAULT_DB);
			}else{
				
				if(StringUtil.isEmpty(db))
					database = getDB(Const.MONGODB_DEFAULT_DB);
				else
					database = getDB(db);
			}
			
			if(database!=null){
				try{
					database.listCollectionNames();
					coll = database.getCollection(collection);
				}catch(Exception e){
					e.printStackTrace();
					coll = null;
				}
			}
		}
		
		return coll;
	}
	
}
