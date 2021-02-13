package com.mdt.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JQueryJsTreeUtil {
    /**
     * 将数据列表封装成jQury-jsTree所需要的结构
     *
     * @param dataList 数据列表
     * @param dataList 已勾选列表
     * @param id       id对应的数据库列
     * @param text     text对应的数据库列
     * @param parent   pid对应的数据库列
     * @return
     */
    public static List<PageData> getTree(List<PageData> dataList, List<PageData> checkedList, String id,
                                         String parent, String text) {

        List retList = new ArrayList();
        Map nodeMap = new HashMap();
        nodeMap.put("id", 0);
        nodeMap.put("parent", "#");
        nodeMap.put("text", "系统菜单");
        Map stateMap = new HashMap<String, Boolean>();
        stateMap.put("opened", true);
        nodeMap.put("state", stateMap);

        retList.add(nodeMap);
        for (PageData pd : dataList) {
            Map node = new HashMap();
            for (PageData pData : checkedList) {
                //System.out.println("pData.menu_id:"+pData.get(id));
                if (pData.containsKey(id) && pd.containsKey(id)) {
                    if (pData.get(id).toString().equals(pd.get(id).toString())) {
                        Map stateMap2 = new HashMap<String, Boolean>();
                        stateMap2.put("selected", true);
                        node.put("state", stateMap2);
                        break;
                    }
                }
            }
            node.put("id", pd.get(id).toString());
            node.put("parent", pd.get(parent).toString());
            node.put("text", pd.get(text).toString());
            retList.add(node);
        }

        return retList;
    }
}
