package com.mdt.interceptor;

import com.mdt.entity.system.User;
import com.mdt.util.Const;
import com.mdt.util.Jurisdiction;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 类名称：LoginHandlerInterceptor.java 类描述：
 *
 * @author Hrq 作者单位： 联系方式： 创建时间：2017年1月1日
 * @version 1.6
 */
public class LoginHandlerInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getServletPath();
        if (path.matches(Const.NO_INTERCEPTOR_PATH)) {
            return true;
        } else {
            // shiro管理的session
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession();
            User user = (User) session.getAttribute(Const.SESSION_USER);
            if (user != null) {
                path = path.substring(1, path.length());
                boolean b = Jurisdiction.shiroJurisdiction(path);
                if (!b) {
                    response.sendRedirect(request.getContextPath() + Const.LOGIN);
                }
                return b;
            } else {
                // 登陆过滤
                response.sendRedirect(request.getContextPath() + Const.LOGIN);
                return false;
                // return true;
            }
        }
    }
}
