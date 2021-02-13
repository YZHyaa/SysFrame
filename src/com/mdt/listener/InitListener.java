package com.mdt.listener;

import com.mdt.entity.system.Menu;
import com.mdt.service.system.dictionaries.DictionariesService;
import com.mdt.service.system.menu.MenuService;
import com.mdt.service.system.message.MessageTopicService;
import com.mdt.util.Const;
import com.mdt.util.cache.CacheUtil;
import com.mdt.util.cache.DictionaryCacheUtil;
import com.mdt.util.mongodb.MessageUtil;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 本类专门用于在spring容器加载完成后进行各项初始化工作
 *
 * @author "PangLin"
 * @ClassName: InitListener
 * @Description: TODO
 * @date 2016年1月13日 下午3:08:13
 */
@Component
public class InitListener implements ApplicationListener<ContextRefreshedEvent> {
    Logger log = Logger.getLogger(InitListener.class);


    @Resource(name = "dictionariesService")
    private DictionariesService dictionariesService;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            //初始化数据字典
            initDic();
            //初始化菜单
            initMenu();
//            initMsgTopic();
            //initTest();
        }
    }

    /**
     * 初始化数据字典
     */
    private void initDic() {
        try {
            Const.DICTIONARIECACHE = (Cache) WebAppContextListener.getApplicationContext().getBean("dicCache");
            DictionariesService dictionariesService = (DictionariesService) WebAppContextListener.getApplicationContext().getBean("dictionariesService");
            List dicDataList = dictionariesService.listDic(null);
            CacheUtil.initCache(CacheUtil.makeSelectDataCache(dicDataList, "ID", "PID", "NAME", "CODE", "0", "root", true, DictionaryCacheUtil.DICTIONARIE_MAP_POSTFIX), Const.DICTIONARIECACHE);
        } catch (Exception e) {
            log.error("字典初始化出错:" + e.toString());
            e.printStackTrace();
        }
    }

    private void initMenu() {
        String s_menuUrl;
        try {
            Const.MENUCACHE = (Cache) WebAppContextListener.getApplicationContext().getBean("menuCache");
            MenuService menuService = (MenuService) WebAppContextListener.getApplicationContext().getBean("menuService");
            List<Menu> menusList = menuService.listAllMenus();
            List<Element> elist = new ArrayList<Element>();
            for (Menu menu : menusList) {
                s_menuUrl = menu.getMENU_URL();
                if (s_menuUrl != null && !"".equals(s_menuUrl)) {
                    String url = menu.getMENU_URL().split(".do")[0];
                    if (url.startsWith("/") || url.startsWith("\\"))
                        url = url.substring(1);
                    Element e = new Element(url, 1);
                    elist.add(e);
                }
            }
            CacheUtil.initCache(elist, Const.MENUCACHE);
        } catch (Exception e) {
            log.error("菜单缓存初始化出错:" + e.toString());
            e.printStackTrace();
        }
    }

    /**
     * 初始化消息通道
     */
    private void initMsgTopic() {
        try {
            Const.MSGTOPICCACHE = (Cache) WebAppContextListener.getApplicationContext().getBean("msgTopicCache");
            MessageTopicService messageTopicService = (MessageTopicService) WebAppContextListener.getApplicationContext().getBean("messageTopicService");
            CacheUtil.initCache(CacheUtil.makeSimpleDataCache(messageTopicService.findUserGroupByTopic(), "TOPICCODE", "TOPICUSER"), Const.MSGTOPICCACHE);
        } catch (Exception e) {
            log.error("消息通道初始化出错:" + e.toString());
            e.printStackTrace();
        }
    }

    private void initTest() {
        MessageUtil.publicMsg("topic_test", "warning", "广播消息", "测试消息广播");
        System.out.println("====消息已经广播");
    }
}
