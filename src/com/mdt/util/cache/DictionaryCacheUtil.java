package com.mdt.util.cache;

import com.mdt.util.Const;

import java.util.HashMap;
import java.util.Map;

public class DictionaryCacheUtil {
	
	public static final String DICTIONARIE_MAP_POSTFIX = "Map";
	
	/**
	 * 从缓存获取字典值对应的Map
	 * @param pkey 上级字典key
	 * @return
	 */
	public static Map getDictionarieMapCache (String key){
		
		key = key+DICTIONARIE_MAP_POSTFIX;
		
		Map retMap = null;
		if(Const.DICTIONARIECACHE.isKeyInCache(key))
			retMap = (Map)Const.DICTIONARIECACHE.get(key).getObjectValue();
		else
			retMap = new HashMap();
		return retMap;
	}
	
	/**
	 * 从缓存中获取字典指定Map中的code对于的名称
	 * @param key 字典pkey
	 * @param code 字典code
	 * @return
	 */
	public static String getDictionarieMapCacheName(String key,String code){
		Map map = getDictionarieMapCache(key);
		if(map.containsKey(code))
			return map.get(code).toString();
		else
			return "";
	}
	
	/**
	 * 从数据字典cache中获取字典值
	 * @param cache
	 * @param key
	 * @return
	 */
	public static String getDictionarieStrCache(String key){
		String retStr = null;
		if(Const.DICTIONARIECACHE.isKeyInCache(key))
			retStr = (String)Const.DICTIONARIECACHE.get(key).getObjectValue();
		else
			retStr = "";
		return retStr;
	}
	
	
}
