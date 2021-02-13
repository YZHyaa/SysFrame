package com.mdt.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListUtil {


    /**
     * 将有树结构关系的list转换为有层级结构的map
     * 树结构为{id:{"entity":object,"nodes":[{..},{..},{..}]}}
     *
     * @param dataList 数据list
     * @param idCol    id字段名称
     * @param pidCol   pid字段名称
     * @return
     */
    public static Map<String, Map> List2TreeMap(List<PageData> dataList, String idCol, String pidCol) {
        Map<String, Map> retMap = new HashMap<String, Map>();

        for (PageData pd : dataList) {
            Map temp = new HashMap();
            temp.put("entity", pd);
            temp.put("nodes", new ArrayList());

            retMap.put(pd.get(idCol).toString(), temp);
        }

        for (PageData pd : dataList) {
            String id = pd.get(idCol).toString();
            String pid = pd.get(pidCol).toString();

            if (retMap.containsKey(pid))
                ((ArrayList) retMap.get(pid).get("nodes")).add(retMap.get(id));
        }

        return retMap;
    }

    /**
     * 获取子树的id,形式为"id1,id2,id3",可用于删除语句
     *
     * @param tree  整棵树
     * @param idCol id列名称
     * @param subid 子树id
     * @return
     */
    public static String getSubtreeIds(Map<String, Map> tree, String idCol, String subid) {
        if (subid == null || "".equals(subid))
            return null;
        Map subTree = tree.get(subid);
        return getStr(subTree, idCol);
    }

    /**
     * 获取带单引号的子树的id 数组,可用于删除语句
     *
     * @param tree  整棵树
     * @param idCol id列名称
     * @param subid 子树id
     * @return
     */
    public static String[] getSubtreeStrIdsArr(Map<String, Map> tree, String idCol, String subid) {
        String str = getSubtreeIds(tree, idCol, subid);
        if (str == null || "".equals(str))
            return new String[0];
        else {
            str = "'" + str.replaceAll(",", "','") + "'";
            return str.split(",");
        }

    }

    /**
     * 获取子树的id 数组,可用于删除语句
     *
     * @param tree  整棵树
     * @param idCol id列名称
     * @param subid 子树id
     * @return
     */
    public static String[] getSubtreeIdsArr(Map<String, Map> tree, String idCol, String subid) {
        String str = getSubtreeIds(tree, idCol, subid);
        if (str == null || "".equals(str))
            return new String[0];
        else
            return str.split(",");
    }

    private static String getStr(Map tree, String idCol) {

        PageData pd = (PageData) tree.get("entity");

        String retStr = pd.get(idCol).toString();

        List<Map> nodes = (List<Map>) tree.get("nodes");

        for (Map pd2 : nodes) {
            retStr += "," + getStr(pd2, idCol);
        }


        return retStr;
    }

}
