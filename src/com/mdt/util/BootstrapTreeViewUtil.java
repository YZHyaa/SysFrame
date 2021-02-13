package com.mdt.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author PangLin
 */
public class BootstrapTreeViewUtil {

    /**
     * 将数据列表封装成bootstrap-treeview所需要的结构
     *
     * @param dataList  数据列表
     * @param rootId    根节点的id的值，一般为“0”
     * @param rootText  根节点显示的值
     * @param idCol     id对应的数据库列
     * @param textCol   text对应的数据库列
     * @param pidCol    pid对应的数据库列
     * @param otherCols 其他需要放到树节点的属性及其数据库对应的字段，例如name,USERNAME,age,USERAGE形式，必须成对出现
     * @return
     */
    public static List<PageData> getTree(List<PageData> dataList,
                                         String rootId, String rootText, String idCol, String textCol,
                                         String pidCol, String... otherCols) {
        Map root = new HashMap();
        root.put("id", rootId);
        root.put("text", rootText);
        root.put("nodes", new ArrayList());

        Map<String, Map> temp = new HashMap<String, Map>();
        int paramLength = 0;

        if (otherCols.length % 2 != 0) {
            paramLength = (otherCols.length - 1) / 2;
        } else {
            paramLength = otherCols.length / 2;
        }

        for (PageData pd : dataList) {
            Map node = new HashMap();
            node.put("id", pd.get(idCol).toString());
            node.put("text", pd.get(textCol).toString());
            node.put("nodes", new ArrayList());
            for (int i = 0; i < paramLength; i++) {
                if (pd.containsKey(otherCols[i * 2 + 1])) {
                    node.put(otherCols[i * 2], pd.get(otherCols[i * 2 + 1])
                            .toString());
                } else {
                    continue;
                }

            }

            temp.put(pd.get(idCol).toString(), node);
        }

        for (PageData pd : dataList) {
            String id = pd.get(idCol).toString();
            String pid = pd.get(pidCol).toString();

            if (rootId.equals(pid)) {
                ((ArrayList) root.get("nodes")).add(temp.get(id));
            } else {
                ((ArrayList) temp.get(pid).get("nodes")).add(temp.get(id));
            }

        }

        List retList = new ArrayList();
        retList.add(root);

        return retList;
    }

    /**
     * 将数据列表封装成bootstrap-treeview复选模式所需要的结构
     *
     * @param dataList  数据列表
     * @param dataList  关系表中存在关系的列表
     * @param rootId    根节点的id的值，一般为“0”
     * @param rootText  根节点显示的值
     * @param idCol     id对应的数据库列
     * @param textCol   text对应的数据库列
     * @param pidCol    pid对应的数据库列
     * @param otherCols 其他需要放到树节点的属性及其数据库对应的字段，例如name,USERNAME,age,USERAGE形式，必须成对出现
     * @return
     */
    public static List<PageData> getCheckTree(List<PageData> dataList,
                                              List<PageData> checkedList, String rootId, String rootText,
                                              String idCol, String textCol, String pidCol, String... otherCols) {
        Map root = new HashMap();
        root.put("id", rootId);
        root.put("text", rootText);
//		Map state=new HashMap();
//		state.put("checked", true);
//		root.put("state", state);
        root.put("showCheckbox", false);
        root.put("nodes", new ArrayList());

        Map<String, Map> temp = new HashMap<String, Map>();
        int paramLength = 0;

        if (otherCols.length % 2 != 0) {
            paramLength = (otherCols.length - 1) / 2;
        } else {
            paramLength = otherCols.length / 2;
        }

        for (PageData pd : dataList) {
            Map node = new HashMap();
            boolean flag = false;//在关系表中是否找到该menu
            for (PageData pData : checkedList) {
                if (pData.containsKey(idCol) && pd.containsKey(idCol)) {
                    if (pData.get(idCol).toString()
                            .equals(pd.get(idCol).toString())) {
                        Map state1 = new HashMap();
                        state1.put("checked", true);
                        node.put("state", state1);
                        flag = true;
                        break;
                    }
                }
            }
            node.put("id", pd.get(idCol).toString());
            node.put("text", pd.get(textCol).toString());
            node.put("nodes", new ArrayList());
            for (int i = 0; i < paramLength; i++) {
                if (pd.containsKey(otherCols[i * 2 + 1])) {
                    node.put(otherCols[i * 2], pd.get(otherCols[i * 2 + 1]).toString());
                } else {
                    continue;
                }
            }
            temp.put(pd.get(idCol).toString(), node);
        }

        for (PageData pd : dataList) {
            String id = pd.get(idCol).toString();
            String pid = pd.get(pidCol).toString();

            if (rootId.equals(pid)) {
                ((ArrayList) root.get("nodes")).add(temp.get(id));
            } else {
                ((ArrayList) temp.get(pid).get("nodes")).add(temp.get(id));
            }

        }

        List retList = new ArrayList();
        retList.add(root);

        return retList;
    }

    /**
     * 将数据列表封装成bootstrap-treeview所需要的结构
     * 只有最后一级才显示复选框
     *
     * @param dataList  数据列表
     * @param rootId    根节点的id的值，一般为“0”
     * @param rootText  根节点显示的值
     * @param idCol     id对应的数据库列
     * @param textCol   text对应的数据库列
     * @param pidCol    pid对应的数据库列
     * @param otherCols 其他需要放到树节点的属性及其数据库对应的字段，例如name,USERNAME,age,USERAGE形式，必须成对出现
     * @return
     */
    public static List<PageData> getChartsTree(List<PageData> dataList,
                                               String rootId, String rootText, String idCol, String textCol,
                                               String pidCol, String... otherCols) {
        Map root = new HashMap();
        root.put("id", rootId);
        root.put("text", rootText);
        root.put("showCheckbox", false);
        root.put("nodes", new ArrayList());

        Map<String, Map> temp = new HashMap<String, Map>();
        int paramLength = 0;

        if (otherCols.length % 2 != 0) {
            paramLength = (otherCols.length - 1) / 2;
        } else {
            paramLength = otherCols.length / 2;
        }

        for (PageData pd : dataList) {
            Map node = new HashMap();
            node.put("id", pd.get(idCol).toString());
            node.put("text", pd.get(textCol).toString());
            node.put("showCheckbox", false);
            node.put("nodes", new ArrayList());
            for (int i = 0; i < paramLength; i++) {
                if (pd.containsKey(otherCols[i * 2 + 1])) {
                    node.put(otherCols[i * 2], pd.get(otherCols[i * 2 + 1]).toString());
                } else {
                    continue;
                }
            }
            temp.put(pd.get(idCol).toString(), node);
        }

        for (PageData pd : dataList) {
            String id = pd.get(idCol).toString();
            String pid = pd.get(pidCol).toString();

            if (rootId.equals(pid)) {
                ((ArrayList) root.get("nodes")).add(temp.get(id));
            } else {
                temp.get(id).put("showCheckbox", true);
                ((ArrayList) temp.get(pid).get("nodes")).add(temp.get(id));
            }
        }

        List retList = new ArrayList();
        retList.add(root);

        return retList;
    }
}