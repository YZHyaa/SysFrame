package com.mdt.util.cache;

import com.mdt.util.Const;

/**
 * 消息通道工具类
 * @ClassName: MsgTopicCacheUtil 
 * @Description: TODO
 * @author "PangLin"
 * @date 2016年1月29日 上午11:20:31 
 *
 */
public class MsgTopicCacheUtil {

	/**
	 * 根据通道名称查询通道对应的用户，如：根据topic查询得到用户列表user1,user2,user3，主要用于从系统向这些用户进行广播
	 * @param topic
	 * @return
	 */
	public static String getUserNameByTopic(String topic){
		String retStr = null;
		
		if(Const.MSGTOPICCACHE.isKeyInCache(topic))
			retStr = Const.MSGTOPICCACHE.get(topic).getObjectValue().toString();
		else
			retStr = "";
		
		return retStr;
	}
	
}
