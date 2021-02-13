package com.mdt.util;

import com.mdt.entity.system.Menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 将数据封装成BootStrap TreeGrid所需要的结构（封装成String串填充treeGrid）
 *
 * @param allMenuList          所有的菜单列表
 * @param roleMenuList         某一角色的所有菜单列表
 * @param allbuttonList        所有的按钮列表
 * @param roleButtonListGlobal 某一角色的拥有的全局按钮（对应sys_auth_button_all数据表）
 * @param roleButtonList       某一角色的拥有菜单的的按钮（对应sys_auth_button数据表）
 * @return string串填充前台页面中的table标签
 */
public class BootstrapTreeGridUtil {

    static int index = 1;   //每一列的索引

    //构造前台table所需内容，返回String字符串
    public static String getTreeGrid(List<Menu> allMenuList,
                                     List<Menu> roleMenuList, List<PageData> allbuttonList,
                                     List<PageData> roleButtonListGlobal, List<PageData> roleButtonList) {
        // 构造列表树结构
        List<Menu> orderList = new ArrayList<Menu>();
        Map<String, Menu> allMap = new HashMap<String, Menu>();
        Map<String, Menu> tempMap = new HashMap<String, Menu>();

        for (Menu menu : allMenuList) {
            allMap.put(menu.getMENU_ID(), menu);
        }

        for (Menu menu : roleMenuList) {
            if (tempMap.containsKey(menu.getMENU_ID()))
                continue;

            if (StringUtil.isEmpty(menu.getPARENT_ID())
                    || "0".equals(menu.getPARENT_ID())) {
                if (allMap.containsKey(menu.getMENU_ID()))
                    orderList.add(allMap.get(menu.getMENU_ID()));
                tempMap.put(menu.getMENU_ID(), menu);
            } else {
                Menu parentMenu = allMap.get(menu.getPARENT_ID());
                Menu tempMenu = menu;
                do {
                    if (parentMenu.getSubMenu() == null) {
                        parentMenu.setSubMenu(new ArrayList<Menu>());
                        parentMenu.getSubMenu().add(tempMenu);
                    } else {
                        parentMenu.getSubMenu().add(tempMenu);
                    }

                    if (StringUtil.isEmpty(parentMenu.getPARENT_ID())
                            || "0".equals(parentMenu.getPARENT_ID())) {
                        if (!tempMap.containsKey(parentMenu.getMENU_ID())) {
                            if (allMap.containsKey(parentMenu.getMENU_ID()))
                                orderList.add(allMap.get(parentMenu.getMENU_ID()));
                            tempMap.put(parentMenu.getMENU_ID(), parentMenu);
                        }
                        break;
                    }
                    tempMenu = parentMenu;
                    parentMenu = allMap.get(parentMenu.getPARENT_ID());
                } while (true);
            }

        }
        // 以下是构造treeGrid全局button行
        StringBuilder sBuilder = new StringBuilder();
        index = 1;  //初始化index变量
        //currMenuIndex = index;
        sBuilder.append("<tr class=\"treegrid-" + (index++) + "\"><td>");
        sBuilder.append("全部");
        sBuilder.append("</td>");
        for (PageData pd : allbuttonList) {
            boolean isChecked = false;
            for (PageData pdData : roleButtonListGlobal) {
                if (pdData.getString("BUTTON_ID").equals(
                        pd.getString("BUTTON_ID"))) {
                    isChecked = true;
                    break;
                }
            }
            if (isChecked) {
                sBuilder.append("<td>" + "<input type=\"checkbox\" id=\""
                        + pd.getString("BUTTON_ID") + "\" checked>"
                        + pd.getString("BUTTON_NAME") + "</td>");

            } else {
                sBuilder.append("<td>" + "<input type=\"checkbox\" id=\""
                        + pd.getString("BUTTON_ID") + "\">"
                        + pd.getString("BUTTON_NAME") + "</td>");
            }

        }
        sBuilder.append("</tr>");
        // 以下是构造根菜单的代码（包含勾选）
        for (Menu menu : orderList) {

            sBuilder.append("<tr class=\"treegrid-" + index
                    + " treegrid-parent-1\"><td>");
            sBuilder.append(menu.getMENU_NAME());
            sBuilder.append("</td>");
            // 构造复选框
            for (PageData pd : allbuttonList) {
                boolean isChecked = false;
                for (PageData pdData : roleButtonList) {
                    if (pdData.get("MENU_ID").toString()
                            .equals(menu.getMENU_ID())) {
                        if (pdData.getString("BUTTON_ID").equals(
                                pd.getString("BUTTON_ID"))) {
                            isChecked = true;
                            break;
                        }
                    }
                }
                // 如果该角色存在此按钮则勾选复选框，否则不勾选
                if (isChecked) {
                    sBuilder.append("<td>" + "<input type=\"checkbox\" id=\""
                            + menu.getMENU_ID() + ","
                            + pd.getString("BUTTON_ID") + "\" checked>"
                            + pd.getString("BUTTON_NAME") + "</td>");

                } else {
                    sBuilder.append("<td>" + "<input type=\"checkbox\" id=\""
                            + menu.getMENU_ID() + ","
                            + pd.getString("BUTTON_ID") + "\">"
                            + pd.getString("BUTTON_NAME") + "</td>");
                }

            }
            sBuilder.append("</tr>");
            // 以下是构造叶子节点菜单的代码(递归调用 )
            buildChildNode(menu, sBuilder, roleButtonList, allbuttonList);
            index++;

        }
        return sBuilder.toString();
    }


    //递归方法逐级构造节点
    public static void buildChildNode(Menu tempMenu, StringBuilder sBuilder,
                                      List<PageData> roleButtonList, List<PageData> allbuttonList) {
        if (tempMenu.getSubMenu() != null) {
            int currMenuIndex = index++;
            for (Menu childMenu : tempMenu.getSubMenu()) {
                sBuilder.append("<tr class=\"treegrid-" + index
                        + " treegrid-parent-" + currMenuIndex + "\"><td>");
                sBuilder.append(childMenu.getMENU_NAME());
                sBuilder.append("</td>");
                for (PageData pd : allbuttonList) {
                    boolean isChecked = false;
                    for (PageData pdData : roleButtonList) {
                        if (pdData.get("MENU_ID").toString()
                                .equals(childMenu.getMENU_ID())) {
                            if (pdData.getString("BUTTON_ID").equals(
                                    pd.getString("BUTTON_ID"))) {
                                isChecked = true;
                                break;
                            }
                        }
                    }
                    if (isChecked) {
                        sBuilder.append("<td>"
                                + "<input type=\"checkbox\" id=\""
                                + childMenu.getMENU_ID() + ","
                                + pd.getString("BUTTON_ID") + "\" checked>"
                                + pd.getString("BUTTON_NAME") + "</td>");

                    } else {
                        sBuilder.append("<td>"
                                + "<input type=\"checkbox\" id=\""
                                + childMenu.getMENU_ID() + ","
                                + pd.getString("BUTTON_ID") + "\">"
                                + pd.getString("BUTTON_NAME") + "</td>");
                    }

                }

                sBuilder.append("</tr>");
                buildChildNode(childMenu, sBuilder, roleButtonList,
                        allbuttonList);
                index++;
            }
        }
    }

}
