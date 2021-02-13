package com.mdt.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConstDic {
    public static final String PIPELINE_PIPELINE_TYPE = "gdlx";                            //管道类型
    public static final String PIPELINE_CONSTRUCTION_STATE = "gdsgzt";                    //管道施工状态
    public static final String PIPELINE_CONSTRUCTION_TYPE = "010906";                        //管道施工类型
    public static final String PIPELINE_STANDARDIZATION_STATE = "bjsfqy";                    //管道标注状态
    public static final String PIPELINE_MARKER_TYPE = "bswlx";                                //标识物类型
    public static final String MAP_ALARM = "bjsfqy";                                            //报警是否启用
    public static final String MAP_ALARM_YES = "011101";                                    //报警启用
    public static final String MAP_SHOW = "dtsfxs";                                            //地图是否显示
    public static final String MAP_SHOW_YES = "011101";                                        //地图显示
    public static final String MAP_SHOW_NO = "011102";                                        //地图不显示
    public static final String EQUIPMENT_TYPE = "sblx";                                        //设备类型
    public static final String EQUIPMENT_TYPE_LINE = "010801";                                //设备类型_光纤型
    public static final String EQUIPMENT_TYPE_SINGLE = "010802";                            //设备类型_单点型
    public static final String EQUIPMENT_PART_TYPE = "sbfqlx";                                //设备分区类型
    public static final String EQUIPMENT_PART_STATUS = "sbfqzt";                            //设备分区状态
    public static final String FIBER_INVALID_TYPE = "gxwxjllx";                                //光纤无效距离类型
    public static final String ALARM_TYPE = "bjlx";                                            //报警类型
    public static final String ALARM_STATUS = "bjzt";                                        //报警状态
    public static final String ALARM_STATUS_FINISH = "bjzt01";                                //报警状态-已处理
    public static final String ALARM_STATUS_NO_FINISH = "bjzt02";                            //报警状态-未处理
    public static final String ALARM_INFO_REMIND_TYPE = "bjtxfs";                            //报警提醒方式
    public static final String EMERGENCY_PLAN_LEVEL = "yajb";                                //预案级别
    public static final String EMERGENCY_EXPERT_EVENT = "sysj";                                //适用事件
    public static final String EQUIPMENT_DET_DIRECTION = "sbjcfx";                            //设备检测方向
    public static final String EQUIPMENT_DET_DIRECTION_FORWARD = "010401";                    //设备检测方向-正向
    public static final String EQUIPMENT_DET_DIRECTION_REVERSE = "010402";                    //设备检测方向-反向
    public static final String LINE_TYPE = "linear";                                        //线条形状
    public static final String LINE_WIDTH = "lineWidth";                                        //线条宽度
    public static final String EQUIPMENT_TYPE_LINE_DTS = "01080101";                            //DTS设备
    public static final String EQUIPMENT_TYPE_LINE_DVS = "01080102";                            //DVS设备
    public static final String EQUIPMENT_TYPE_SINGLE_METHANE = "01080201";                    //甲烷设备
    //	public static final String DTS_CODE="lineWidth";                      					//
    public static final String ALARM_TYPE_DVS = "010602";                                    //报警类型-震动
    public static final String ALARM_TYPE_DTS = "010601";                                    //报警类型-温度
    public static final String ALARM_TYPE_METHANE_CHARGE = "010603";                        //报警类型-甲烷-电量
    public static final String ALARM_TYPE_METHANE_THICKNEWSS = "010604";                    //报警类型-甲烷-浓度
    public static final String ALARM_TYPE_METHANE_TEMPERATURE = "010605";                    //报警类型-甲烷-环境温度
    public static final String ALARM_TYPE_METHANE_WATERLEVEL = "010606";                    //报警类型-甲烷-液位
    public static final String GIS_INIT_EXTENT = "gis_init_extent";                            //GIS地图初始化位置坐标
    public static final String GIS_INIT_BASELAYER = "gis_base_layer";                        //GIS地图初始化默认基础图层
    public static final String GIS_PIPELINE_ISOTHERMY_AREA = "010907";                        //GIS地图管道等温区域
    public static final String DTS_FIB_CUT = "1060104";                                        //DTS断纤
    public static final String DVS_FIB_CUT = "1060204";                                        //DVS断纤
    public static final String EQUIPMENT_ERROR_TYPE = "sbcwlx";                                //设备错误类型
    public static final String EQUIPMENT_ERROR_SOLVE_STATUS = "cwjjzt";                        //设备错误解决状态
    public static final String METER_INTERVAL_PARAM = "dInstance";                            //米间隔参数
    public static final String DVS_MERGE_SETTING = "dvsMergeSetting";                        //dvs粘连设置
    public static final String DTS_MERGE_SETTING = "dtsMergeSetting";                        //dts粘连设置
    public static final String METHANE_MERGE_SETTING = "methaneMergeSetting";                //甲烷粘连设置
    public static final String MERGE_TIME = "timeItv";                                        //粘连时间
    public static final String MERGE_SPACE = "spaceItv";                                        //粘连距离
    public static final String PIPE_ISOTHERMY_AREA = "pipeIsothermyArea";                    //管道等温区域
    public static final String ISO_ISSTART = "isStart";                                        //管道等温区域是够启用
    public static final String ISO_TIME = "timeInterval";                                    //管道等温区域间隔
    public static final String ALARM_PUSHED = "alarmPushed";                                //报警推送
    public static final String ALARM_PUSHED_ISPUSHED = "isPushed";                            //报警是否推送
    public static final String ALARM_PUSHED_TIME = "timeInterval";                            //报警推送时间间隔
    public static final String ALARM_PUSHED_ALARMDATAINTERVAL = "alarmDateInterval";            //报警时间范围
    public static final String SYNC_FRAME_DATA = "syncCloudFrameData";                        //同步平台数据
    public static final String SYNC_FRAME_DATA_ISSYNC = "isSync";                            //平台数据是否同步
    public static final String SYNC_FRAME_DATA_TIME = "timeInterval";                        //平台数据同步时间间隔
    public static final String ALARM_REASON = "bjyy";                                        //报警原因
    public static final String METHANE_TEMP = "010605";                                        //甲烷温度
    public static final String METHANE_CHARGE = "010603";                                    //甲烷电量
    public static final String METHANE_THICKNEWSS = "010604";                                //甲烷浓度
    public static final String LOGIN_TITLES = "loginTitle";                                    //登录界面标题
    public static final String LOGIN_BIG_TITLE = "bigTitle";                                    //登录界面大标题
    public static final String LOGIN_TITLE = "title";                                        //登陆界面标题
    public static final String LOGIN_SUB_TITLE = "subTitle";                                    //登陆界面小标题
    public static final String MAIN_SOFT_WORDS = "mainSoftWord";                                //软件主界面文字
    public static final String SOFT_COPYRIGHT = "copyright";                                    //软件版权
    public static final String SOFT_COPYRIGHT_GROUP = "copyrightGroup";                        //软件版权所有组织
    public static final String SOFT_COPYRIGHT_GROUP_LINK = "copyrightGroupLink";                //软件版权所有组织链接
    public static final String PIPELINE_CONST = "pipeline";   //管道前缀pipeline_
    public static final String MARKER_CONST = "marker";       //标识物前缀marker_
    public static Map<String, String> bjlxMap = new HashMap<String, String>();
    public static List<PageData> tclxMapList = new ArrayList<PageData>();

    static {
        bjlxMap.put(ALARM_TYPE_DVS, "dvs");
        bjlxMap.put(ALARM_TYPE_DTS, "dts");
        bjlxMap.put(ALARM_TYPE_METHANE_CHARGE, "methane");
        bjlxMap.put(ALARM_TYPE_METHANE_TEMPERATURE, "methane");
        bjlxMap.put(ALARM_TYPE_METHANE_THICKNEWSS, "methane");
        bjlxMap.put(ALARM_TYPE_METHANE_WATERLEVEL, "methane");
    }

    static {
        PageData p1 = new PageData();
        p1.put("ID", PIPELINE_MARKER_TYPE);
        p1.put("TEXT", "标识物类型");
        PageData p2 = new PageData();
        p2.put("ID", PIPELINE_PIPELINE_TYPE);
        p2.put("TEXT", "管道类型");
        tclxMapList.add(p1);
        tclxMapList.add(p2);
    }
}
