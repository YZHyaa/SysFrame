package com.mdt.util;

import net.sf.ehcache.Cache;

/**
 * 项目名称：
 *
 * @author:luyun
 */
public class Const {
    public static final String SESSION_SECURITY_CODE = "sessionSecCode";

    public static final String SESSION_USER = "sessionUser";

    public static final String SESSION_ROLE_RIGHTS = "sessionRoleRights";

    public static final String SESSION_menuList = "menuList"; // 当前菜单

    public static final String SESSION_allmenuList = "allmenuList"; // 全部菜单

    public static final String SESSION_QX = "QX";

    public static final String SESSION_userpds = "userpds";

    public static final String SESSION_USERROL = "USERROL"; // 用户对象

    public static final String SESSION_USERNAME = "USERNAME"; // 用户名

    public static final String TRUE = "T";

    public static final String FALSE = "F";

    public static final String LOGIN = "/login_toLogin.do"; // 登录地址

    public static final String LOGOUT = "/login_toLogout.do"; // 登录地址

    public static final String SYSNAME = "admin/config/SYSNAME.txt"; // 系统名称路径

    public static final String PAGE = "admin/config/PAGE.txt"; // 分页条数配置路径

    public static final String EMAIL = "admin/config/EMAIL.txt"; // 邮箱服务器配置路径

    public static final String SMS1 = "admin/config/SMS1.txt"; // 短信账户配置路径1

    public static final String SMS2 = "admin/config/SMS2.txt"; // 短信账户配置路径2

    public static final String FWATERM = "admin/config/FWATERM.txt"; // 文字水印配置路径

    public static final String IWATERM = "admin/config/IWATERM.txt"; // 图片水印配置路径

    public static final String WEIXIN = "admin/config/WEIXIN.txt"; // 微信配置路径

    public static final String WEBSOCKET = "admin/config/WEBSOCKET.txt";// WEBSOCKET配置路径

    public static final String FILEPATHIMG = "uploadFiles/uploadImgs/"; // 图片上传路径

    public static final String FILEPATHFILE = "uploadFiles/file/"; // 文件上传路径

    public static final String FILEPATHQRCode = "uploadFiles/QRCode/"; // 二维码存放路径

    public static final String NO_INTERCEPTOR_PATH = ".*/((login)|(MP_verify_F7DkpSf8p5TQdkNC.txt)|(logout)|(articleshow)|(articleManage)|(articleFront)|(code)|(app)|(weixin)|(static)|(hplus)|(uploadFiles)|(plugins)|(main)|(.well-known)|(websocket)|(RESTFUL)).*"; // 不对匹配该值的访问路径拦截（正则）

    public static final String[] BJLX = {"dvs", "dts", "methane"};
    /**
     * APP Constants
     */
    // app注册接口_请求协议参数)
    public static final String[] APP_REGISTERED_PARAM_ARRAY = new String[]{"countries", "uname", "passwd", "title", "full_name", "company_name",
            "countries_code", "area_code", "telephone", "mobile"};
    public static final String[] APP_REGISTERED_VALUE_ARRAY = new String[]{"国籍", "邮箱帐号", "密码", "称谓", "名称", "公司名称", "国家编号", "区号", "电话", "手机号"};
    // app根据用户名获取会员信息接口_请求协议中的参数
    public static final String[] APP_GETAPPUSER_PARAM_ARRAY = new String[]{"USERNAME"};
    public static final String[] APP_GETAPPUSER_VALUE_ARRAY = new String[]{"用户名"};
    /**
     * 数据字典cache
     */
    public static Cache DICTIONARIECACHE;
    /**
     * 数据字典cache
     */
    public static Cache MENUCACHE;
    /**
     * 消息通道cache
     */
    public static Cache MSGTOPICCACHE;
    /**
     * mongodb是否开启授权访问
     */
    public static Boolean MONGODB_AUTH = true;
    /**
     * mongodb默认数据库
     */
    public static String MONGODB_DEFAULT_DB = "";
    // 统计待生成米间隔点数据总量
    public static double METER_INTERVAL_TOTAL = -1;

    // 统计当前生成米间隔点数量
    public static double METER_INTERVAL_CURR_COUNT = -1;

    // 统计待批量插入数据总量（管道标定）
    public static double Batch_Insert_TOTAL = -1;

    // 统计当前批量插入数据数量（管道标定）
    public static double Batch_Insert_CURR_COUNT = -1;

}
