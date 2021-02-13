package com.mdt.util;

import com.mdt.listener.WebAppContextListener;
import com.mdt.service.system.menu.MenuService;
import com.mdt.service.system.role.RoleService;
import com.mdt.service.system.user.UserService;


/**
 * @author Administrator
 *         获取Spring容器中的service bean
 */
public final class ServiceHelper {

    public static Object getService(String serviceName) {
        return WebAppContextListener.getBean(serviceName);
    }

    public static UserService getUserService() {
        return (UserService) getService("userService");
    }

    public static RoleService getRoleService() {
        return (RoleService) getService("roleService");
    }

    public static MenuService getMenuService() {
        return (MenuService) getService("menuService");
    }
}
