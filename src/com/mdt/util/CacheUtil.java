package com.mdt.util;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import java.util.*;

/**
 * 缓存工具类
 *
 * @author "PangLin"
 * @ClassName: CacheUtil
 * @Description: TODO
 * @date 2015年12月3日 上午11:05:54
 */
public class CacheUtil {

    private static final String DICTIONARIE_MAP_POSTFIX = "Map";

    /**
     * 使用指定数据初始化一个ehcache.xml中的一个cache
     *
     * @param list
     * @param cache
     */
    public static void initCache(List<Element> list, Cache cache) {
        synchronized (cache) {
            cache.removeAll();
            cache.putAll(list);
        }
    }

    /**
     * 从数据字典cache中获取字典值
     *
     * @param cache
     * @param key
     * @return
     */
    public static String getDictionarieStrCache(String key) {
        String retStr = null;
        if (Const.DICTIONARIECACHE.isKeyInCache(key))
            retStr = (String) Const.DICTIONARIECACHE.get(key).getObjectValue();
        else
            retStr = "";
        return retStr;
    }

    /**
     * 从缓存获取字典值对应的Map
     *
     * @param pkey  上级字典key
     * @param value 当前值
     * @return
     */
    public static Map getDictionarieMapCache(String key) {

        key = key + DICTIONARIE_MAP_POSTFIX;

        Map retMap = null;
        if (Const.DICTIONARIECACHE.isKeyInCache(key))
            retMap = (Map) Const.DICTIONARIECACHE.get(key).getObjectValue();
        else
            retMap = new HashMap();
        return retMap;
    }

    public static String getDictionarieMapCacheName(String key, String code) {
        Map map = getDictionarieMapCache(key);
        if (map.containsKey(code))
            return map.get(code).toString();
        else
            return "";
    }

    /**
     * 为配合下拉框组件
     * 使用具有上下级结构的数据
     * 生成下拉框所需的格式数据,如value:name,value:name,value:name
     * 生成的结果可以直接放入ehcache中
     * 注意：可以根据需要生成为快速转化value-name所需的map，这个map的key为字典字段+“Map”
     *
     * @param infoList    源数据
     * @param idCol       源数据中的id字段
     * @param pIdCol      源数据中的父id字段，父节点的nameCol做为缓存的key
     * @param nameCol     元数据中用作option属性NAME的字段
     * @param valueCol    元数据中用作option属性VALUE的字段
     * @param rootColVal  根节点的id值，通常为0
     * @param rootName    根节点对应的NAME的值，通常为root
     * @param makeMapCahe 是否同时生成map
     * @return
     */
    public static List<Element> makeSelectDataCache(List<PageData> infoList, String idCol, String pIdCol, String nameCol, String valueCol, String rootColVal, String rootName, Boolean makeMapCahe) {
        List<Element> retList = new ArrayList<Element>();

        Map<String, PageData> tempmap = new HashMap<String, PageData>();
        Map<String, Map<String, String>> tempMapMap = new HashMap<String, Map<String, String>>();
        Map<String, String> tempStrMap = new HashMap();
        for (PageData pd : infoList) {
            String id = pd.get(idCol).toString();
            tempmap.put(id, pd);
        }
        PageData root = new PageData();
        root.put(idCol, rootColVal);
        root.put(valueCol, rootName);
        tempmap.put(rootColVal, root);


        for (PageData pd : infoList) {
            String pid = pd.get(pIdCol).toString();
            String name = pd.get(nameCol).toString();
            String value = pd.get(valueCol).toString();

            PageData pNode = tempmap.get(pid);
            String code = pNode.getString(valueCol);
            String optionStr = value + ":" + name;

            if (tempStrMap.containsKey(code)) {
                String selectStr = tempStrMap.get(code);
                if (selectStr == null || "".equals(selectStr)) {
                    tempStrMap.put(code, optionStr);
                } else {
                    tempStrMap.put(code, selectStr + "," + optionStr);
                }
            } else {
                tempStrMap.put(code, optionStr);
            }

            if (makeMapCahe) {
                if (tempMapMap.containsKey(code)) {
                    Map<String, String> node = tempMapMap.get(code);
                    node.put(value, name);
                } else {
                    Map<String, String> node = new HashMap<String, String>();
                    node.put(value, name);
                    tempMapMap.put(code, node);
                }
            }


        }

        Iterator<String> it = tempStrMap.keySet().iterator();

        while (it.hasNext()) {
            String key = it.next();
            String optionStr = tempStrMap.get(key);
            Element e = new Element(key, "{" + optionStr + "}");
            retList.add(e);
        }

        if (makeMapCahe) {
            Iterator<String> mapIt = tempMapMap.keySet().iterator();
            while (mapIt.hasNext()) {
                String key = mapIt.next();
                Map mapValue = tempMapMap.get(key);
                Element e = new Element(key + DICTIONARIE_MAP_POSTFIX, mapValue);
                retList.add(e);
            }
        }

        return retList;
    }

}
