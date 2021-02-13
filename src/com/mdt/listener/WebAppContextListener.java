package com.mdt.listener;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 系统注入
 *
 * @author "PangLin"
 * @ClassName: WebAppContextListener
 * @Description: TODO
 * @date 2015年12月3日 上午11:11:10
 */
@Component
public class WebAppContextListener implements InitializingBean, ApplicationContextAware {

    private static ApplicationContext applicationContext;


    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        WebAppContextListener.applicationContext = applicationContext;
    }

    public static <T> T getBean(String name) throws BeansException {
        return (T) applicationContext.getBean(name);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
