package com.mdt.filter;

import com.mdt.controller.base.BaseController;
import com.mdt.plugin.websocketInstantMsg.ChatServer;
import com.mdt.plugin.websocketOnline.OnlineChatServer;
import com.mdt.util.Const;
import com.mdt.util.Tools;
import org.java_websocket.WebSocketImpl;

import javax.servlet.*;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 创建人：luyun
 * 创建时间：2014年2月17日
 */
public class startFilter extends BaseController implements Filter {
    /**
     * 初始化
     */
    public void init(FilterConfig fc) throws ServletException {
        this.startWebsocketInstantMsg();
        this.startWebsocketOnline();
    }

    /**
     * 启动即时聊天服务
     */
    public void startWebsocketInstantMsg() {
        WebSocketImpl.DEBUG = false;
        ChatServer s;
        try {
            String strWEBSOCKET = Tools.readTxtFile(Const.WEBSOCKET);//读取WEBSOCKET配置,获取端口配置
            if (null != strWEBSOCKET && !"".equals(strWEBSOCKET)) {
                String strIW[] = strWEBSOCKET.split(",luyun,");
                if (strIW.length == 4) {
                    s = new ChatServer(Integer.parseInt(strIW[1]));
                    s.start();
                }
            }
            //System.out.println( "websocket服务器启动,端口" + s.getPort() );
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动在线管理服务
     */
    public void startWebsocketOnline() {
        WebSocketImpl.DEBUG = false;
        OnlineChatServer s;
        try {
            String strWEBSOCKET = Tools.readTxtFile(Const.WEBSOCKET);//读取WEBSOCKET配置,获取端口配置
            if (null != strWEBSOCKET && !"".equals(strWEBSOCKET)) {
                String strIW[] = strWEBSOCKET.split(",luyun,");
                if (strIW.length == 4) {
                    s = new OnlineChatServer(Integer.parseInt(strIW[3]));
                    s.start();
                }
            }
            //System.out.println( "websocket服务器启动,端口" + s.getPort() );
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


    //计时器
    public void timer() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9); // 控制时
        calendar.set(Calendar.MINUTE, 0);        // 控制分
        calendar.set(Calendar.SECOND, 0);        // 控制秒

        Date time = calendar.getTime();        // 得出执行任务的时间

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                //PersonService personService = (PersonService)ApplicationContext.getBean("personService");
                //System.out.println("-------设定要指定任务--------");
            }
        }, time, 1000 * 60 * 60 * 24);// 这里设定将延时每天固定执行
    }

    public void destroy() {
    }


    public void doFilter(ServletRequest arg0, ServletResponse arg1,
                         FilterChain arg2) throws IOException, ServletException {
    }
}