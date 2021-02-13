<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<system:header />
</head>

<body class="gray-bg top-navigation">
    <div id="wrapper">
        <div id="page-wrapper" class="gray-bg">
            <div class="row border-bottom white-bg">
                <nav class="navbar navbar-static-top" role="navigation">
                    <div class="navbar-header" style="background-color:honeydew;">
                        <button id="navButton" aria-controls="navbar" aria-expanded="false" data-target="#navbar" data-toggle="collapse" class="navbar-toggle collapsed" type="button">
                            <i class="fa fa-reorder"></i>
                        </button>
                        <div class="navbar-brand" style="color:black;padding:0px 25px;"><img src="${pd.LOGO }" height="50px"></div>
                        <font class="navbar-brand" style="color:black;">${pd.SYSNAME }</font>
                        
                    </div>
                    <div class="navbar-collapse collapse" id="navbar">
                        <ul class="nav navbar-nav">
                            <li class="dropdown" class="active">
                                <a aria-expanded="false" role="button" href="javascript:void(0);" onclick="openMenu('${pd.HOMEPAGE}',this)" class="dropdown-toggle" data-toggle="dropdown"><i class="fa fa-desktop"></i>&nbsp;首页&nbsp;</a>
                            </li>
                            <c:forEach items="${menuList}" var="item" varStatus="i">
							<li class="dropdown">
                                <a aria-expanded="false" role="button" href="javascript:void(0);" onclick="openMenu('${item.MENU_URL}',this)" class="dropdown-toggle" data-toggle="dropdown"><i class="fa ${item.MENU_ICON}"></i>${item.MENU_NAME} <span class="caret"></span></a>
                                <ul role="menu" class="dropdown-menu">
                                    <c:forEach items="${item.subMenu}" var="subItem" varStatus="si">
										<li><a href="javascript:void(0);" onclick="openMenu('${subItem.MENU_URL}',this)"><i class="fa ${subItem.MENU_ICON}"></i>&nbsp;${subItem.MENU_NAME}</a>
										</li>
									</c:forEach>
                                </ul>
                            </li>
							</c:forEach>
                        </ul>
                        <ul class="nav navbar-nav" style="float:right;">
							<li class="dropdown">
                                <a aria-expanded="false" role="button" href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown"><i class="fa fa-user"></i>${user.NAME} <span class="caret"></span></a>
                                <ul role="menu" class="dropdown-menu">
									<li><a href="javascript:void(0);" onclick="changePwd()">修改密码</a>
									</li>
                                </ul>
                            </li>
                            <li class="dropdown" id="msgDD">
                            <a class="dropdown-toggle count-info" data-toggle="dropdown" href="#"> 
                            <i class="fa fa-envelope"></i> <span class="label label-warning" id="msgNumTag">0</span>
							</a>
							<ul class="dropdown-menu dropdown-messages" style="overflow-y:scroll;" id="msgul">
								<li>
									<div class="text-center link-block">
										<a href="javascript:void(0);" onclick="openAllMsg()"> <i
											class="fa fa-envelope"></i> <strong> 查看所有消息</strong>
										</a>
									</div>
								</li>
								<li class="divider" id="msgList"></li>
							</ul></li>
							
							<li class="dropdown" id="alarmDD">
                            <a class="dropdown-toggle count-info" data-toggle="dropdown" href="#"> 
                            <i class="fa fa-envelope"></i> <span class="label label-warning" id="alarmNumTag">0</span>
							</a>
							<ul class="dropdown-menu dropdown-messages" style="overflow-y:scroll;" id="alarmul">
								<li>
									<div class="text-center link-block">
										<a href="javascript:void(0);" onclick="openAllMsg()"> <i
											class="fa fa-envelope"></i> <strong> 查看所有消息</strong>
										</a>
									</div>
								</li>
								<li class="divider" id="alarmList"></li>
							</ul></li>
                            <li>
                                <a href="<%=basePath%>logout" >
                                    <i class="fa fa-sign-out"></i> 退出
                                </a>
                            </li>
                        </ul>
                        
                    </div>
                </nav>
            </div>
            <div class="wrapper wrapper-content">
                <iframe id="iframepage" src="articleManage/listArticleManages" frameborder="0"  scrolling="no" marginheight="0" marginwidth="0" width="100%" height="100%"></iframe>
            </div>
            
            <div class="footer" style="position: fixed;bottom: 0;width: 100%;">
                <div class="pull-right">
                    By：<a href="javascript:window.open('${pd.COPYRIGHT_GROUP_LINK}','_blank')" >${pd.COPYRIGHT_GROUP}</a>
                </div>
                <div>
                    <strong>Copyright</strong> ${pd.COPYRIGHT}
                </div>
            </div>
        </div>
    </div>
	<div id="indexModal" class="modal inmodal fade" tabindex="-1"
		role="dialog" aria-hidden="true"></div>
	<div class="hplusProgress"></div>
	<system:jsFooter/>
    <%--<script src="hplus/js/plugins/toastr/toastr.min.js?v=20170904"></script>
    --%>
    <script src="hplus/js/plugins/toastr/toastr.notmin.js?v=20170904"></script>
    <script>
    var array_markers = new Array();
    getMarkers();
    	var alarmCount=0; 
    	var alarmMap = new Object();
    	var alarmIdList = new Array();
        $(document).ready(function(){
        	var ifm= document.getElementById("iframepage");   
	    	ifm.height = $(window).height()-140;
	    	$("#msgul").height($(window).height()-300);
        });
    
        function openMenu(url,obj){
        	if(url=='#')
        		return false;
        	
        	if($("#navbar").attr("aria-expanded")=='true'){
        		$("#navButton").click();
        	}
        	
        	$("#iframepage").attr("src",url);
        	
        	if($(obj).parent().hasClass("dropdown")){
        		$(".active").removeClass("active");
        		$(obj).parent().parent().addClass("active");
        	}else if($(obj).parent().parent().parent().hasClass("dropdown")){
        		$(".active").removeClass("active");
        		$(obj).parent().parent().parent().addClass("active");
        	}
        	
        }
        
        function changePwd(){
    		$("#indexModal").load("toChangePwd",function(){
    			$("#indexModal").modal();
    		});
    	}
        function getAlarmTruck(){
        	return "${dvs_level_truck}";
        }
        function getAlarmTrain(){
        	return "${dvs_level_train}";
        }
        function getAlarmTraficTimeout(){
        	return "${dvs_trafic_timeout}";
        }
        function getAlarmDvsTraficLasttime(){
        	return "${alarm_dvs_trafic_lasttime}";
        }
      //获取服务器设置，被message.js调用
        function getMessageServer(){
        	var server = '${msgServer}';
        	return server;
        }
        //获取用户信息，被message.js调用
        function getMessageUser(){
        	var user='${user.USERNAME}';
        	return user;
        }
      	//获取是否提醒，被message.js调用
        function getMessageEnabled(){
        	var msgEnabled=${msgEnabled};
        	return msgEnabled;
        }
      	
      	
      //获取服务器设置，被message.js调用
        function getAlarmServer(){
        	var server = '${alarmServer}';
        	return server;
        }
        //获取用户信息，被message.js调用
        function getAlarmUser(){
        	var user='${user.USERNAME}';
        	return user;
        }
      	//获取是否提醒，被message.js调用
        function getAlarmEnabled(){
        	var msgEnabled=${alarmEnabled};
        	return msgEnabled;
        }
        
        
    	/**
    	 * 消息提醒
    	 * @param type success info warning error
    	 * @param msg 消息提醒内容
    	 * @param title 标题
    	 * @param clickmethod 点击回调的方法名-可以写在调用页面
    	 */
    	function doToast(type,msg,title,clickmethod,timeout,extendedTimeOut,closeBut,needClickHide){
    		toastr.options = {
    				  "closeButton": closeBut==null?true:closeBut,
    				  "debug": false,
    				  "progressBar": true,
    				  "positionClass": "toast-top-right",
    				  "onclick": clickmethod,
    				  "showDuration": "400",
    				  "hideDuration": "1000",
    				  "timeOut": timeout!=null?timeout:"10000",
    				  "extendedTimeOut": extendedTimeOut!=null?extendedTimeOut:"1000",
    				  "showEasing": "swing",
    				  "hideEasing": "linear",
    				  "showMethod": "fadeIn",
    				  "hideMethod": "fadeOut",
    				  "needClickHide":needClickHide== null?true:needClickHide
    				};
    		var toastrElement;
    		if(type!=null && "successinfowarningerrordtsdvsdxjwndjwdl".indexOf(type)!=-1){
    			toastrElement = toastr[type](msg,title)
    		}else{
    			toastrElement = toastr['warning'](msg,title)
    		}
    		return toastrElement;
    	}
    	
    	 var msgNum = 0;
    	 var alarmNum = 0;
    	 Date.prototype.format = function(fmt)   
    	 {
    	   var o = {   
    	     "M+" : this.getMonth()+1,                 //月份   
    	     "d+" : this.getDate(),                    //日   
    	     "h+" : this.getHours(),                   //小时   
    	     "m+" : this.getMinutes(),                 //分   
    	     "s+" : this.getSeconds(),                 //秒   
    	     "q+" : Math.floor((this.getMonth()+3)/3), //季度   
    	     "S"  : this.getMilliseconds()             //毫秒   
    	   };   
    	   if(/(y+)/.test(fmt))   
    	     fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
    	   for(var k in o)   
    	     if(new RegExp("("+ k +")").test(fmt))   
    	   fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
    	   return fmt;   
    	 }
    	  
    	function showMessage(data){
    		  doToast(data.msgtype,data.message,data.title,null);
    		  
    		  var sendtime = "&nbsp;";
    		  
    		  if(data.sendtime!='undefined'){
    			  sendtime=new Date(data.sendtime).format("yyyy-MM-dd hh:mm:ss");
    		  }
    		  
    		  var msgHtml = '<li id=\"li'+data.messageid+'\">'
    			  		  + '<div class=\"dropdown-messages-box\">'
    			  		  + '<div class=\"media-body\">'
    			  		  + '<strong><i class=\"fa fa-envelope fa-fw\"></i></strong>'+data.message	
    			  		  + '<br>'
    			  		  + '<small class=\"text-muted\">'+sendtime+'</small><small class=\"text-muted\"><a href=\"javascript:void(0);\" onclick=\"readMsg(\''+data.messageid+'\');\"><i class=\"fa fa-envelope\"></i> <strong>接收</strong></a></small>'
    			  		  + '</div>'
    	                  + '</div>'
    	                  + '</li>'
    	                  + '<li class=\"divider\" id=\"lid'+data.messageid+'\"></li>';
    		  
    		  
    		  $("#msgList").after(msgHtml);
    		  msgNum++;
    		  $("#msgNumTag").html(msgNum);
    	}
    	
    	function readMsg (messageid){
    		$("#li"+messageid).remove();
    		$("#lid"+messageid).remove();
    		msgNum--;
    		$("#msgNumTag").html(msgNum);
    		socket.emit('recive message', messageid);
    	}
    	
    	function openAllMsg(){
    		$("#indexModal").load("message/listAllMsg",function(){
    			$("#indexModal").modal();
    		});
    	}
    	var tempdata;
    	function clickAlarm(data){
    		tempdata = data;
    		//alert($("#iframepage").attr("src"));
    		/*if($("#iframepage").attr("src")!="gis/toGisNew"){
    			$("#iframepage").attr("src","gis/toGisNew");
    			setTimeout(function (){clickAlarm_timeout(data);},1000);
    		
    		}else{
    			document.getElementById('iframepage').contentWindow.showAlarmPointMutli(data,true,true)
    		}*/
    		var coor1=[data.lng,data.lat];
    		var nearby = getNearby(coor1)
    		showAlarmWin(data.aid,data.ppname,data.alarmType,data.bjwz,nearby);
    		closeToastrById(data.aid);
    		readAlarm(data.aid);
    	}
    	function isEmpty(obj)
    	{
    	    for (var name in obj)
    	    {
    	        return false;
    	    }
    	    return true;
    	};
    	
    	function getMarkers(){
    		$.ajax({
    			type: "POST",
    			url: "gis/getMarkerBy?isAlarm=1",
    			dataType: "JSON",
    			async: true,
    			success: function(jsonsData_marker){
    				if(jsonsData_marker != null && jsonsData_marker.length > 0){
    					array_markers = jsonsData_marker;
    					
    				}
    			}
    		});
    	}
    	function getIns(data){
    		if(data.lng==null || data.lat==null){
    			return "";
    		}
    		var wgs84Sphere = new ol.Sphere(6378137);
    		var coor1=[data.lng,data.lat];
    		
    		
    		var obj = getNearby(coor1);
    		if(obj.featureName!=""){
    			return "距 "+obj.featureName +" "+obj.length+"米";
    			//return "距 "+obj.featureName +" "+obj.length+"米</br>方向 "+obj.slope;
    		}
    		return "";
    	}
    	function getNearby(coor1){
    		var wgs84Sphere = new ol.Sphere(6378137);
    		var distance=9999999999999999999999;
    		var finalCoor;
    		var featureObj = new Object();
    		featureObj.featureName="";
    		featureObj.featureid="";
    		featureObj.length=0;
    	
			for ( var j = 0; j < array_markers.length; j++) {
				var feature = array_markers[j];
				if(array_markers[j].M_MAPALARM=="011001"){
					var coor2 = [array_markers[j].M_LON,array_markers[j].M_LAT];
					var length = wgs84Sphere.haversineDistance(coor1,coor2);
					if(length<distance){
						distance = length;
						finalCoor=coor2;
						featureObj.length = Math.ceil(distance);
						featureObj.featureName = array_markers[j].M_NAME;
						featureObj.featureid = array_markers[j].M_ID;
					}
				}
			}
    		if(featureObj.featureName!=""){
    			featureObj.slope = getFx(getSlope(transCoor(coor1),transCoor(finalCoor)));
    		}	
    		return featureObj;
    		 
    	}
    	function getSlope(coor1,coor2){
    		 var firstPoint = coor1;
    		    var secondPoint = coor2;
    		    var slope = ((secondPoint[1] - firstPoint[1]) / (secondPoint[0] - firstPoint[0]));
    		    var angle = Math.atan(slope);
    		    var jd = 180/Math.PI*angle;
    		    var thridPoint = new Array();
    		    thridPoint[0] =secondPoint[0] - firstPoint[0];
    		    thridPoint[1] =secondPoint[1] - firstPoint[1];
    		    thridPoint[2] = jd;
    		    return thridPoint;
    	}
    	function transCoor(coor){
    	    var trans = ol.proj.transform(coor, 'EPSG:4326', 'EPSG:3857');
    	    var point = new ol.geom.Point(trans);
    	    return point.getCoordinates();
    	}
    	function getFx(secondPoint){
    		var rotation;
    		if (secondPoint[0] > 0 && secondPoint[1] < 0) {
    			if(secondPoint[2] >=-5){
    				rotation="西"
    			}else if(secondPoint[2] >=-30){
    				rotation="西北偏西"
    			}else if(secondPoint[2] <=-85 ){
    				rotation="北"
    			}else if(secondPoint[2] <=-60){
    				rotation="西北偏北"
    			}
    			else{
   					rotation = "西北";
    			}
   			}
   			//Second quadrant
   			else if (secondPoint[0] < 0 && secondPoint[1] > 0) {
   				if(secondPoint[2] >=-5){
    				rotation="东"
    			}else if(secondPoint[2] >=-30){
    				rotation="东南偏东"
    			}else if(secondPoint[2] <=-85 ){
    				rotation="南"
    			}else if(secondPoint[2] <=-60){
    				rotation="东南偏南"
    			}
    			else{
   					rotation = "东南";
    			}
   			}
   			//Third quadrant
   			else if (secondPoint[0] < 0 && secondPoint[1] < 0) { 
   				if(secondPoint[2] <=5){
    				rotation="东"
    			}else if(secondPoint[2] <=30){
    				rotation="东北偏东"
    			}else if(secondPoint[2] >=85 ){
    				rotation="北"
    			}else if(secondPoint[2] >=60){
    				rotation="东北偏北"
    			}
    			else{
   					rotation = "东北";
    			}
   			} 
   			//First quadrant 
   			else if (secondPoint[0] > 0 && secondPoint[1] > 0) {
   				//rotation = "西南";
   				if(secondPoint[2] <= 5){
    				rotation="西"
    			}else if(secondPoint[2] <=30){
    				rotation="西南偏西"
    			}else if(secondPoint[2] >=85 ){
    				rotation="南"
    			}else if(secondPoint[2] >=60){
    				rotation="西南偏南"
    			}else{
   					rotation = "西南";
    			}
   			}
    		return rotation
    	}
    	var global_bjwz;
    	//报警弹窗集合 通过id查询 index
    	var alarmIndexMap= new Object();
    	//通过index查询id
    	var alarmIdMap= new Object();
    	//通过id查询layer对象
    	var alarmWinObjMap= new Object();
    	function showAlarmWin(id,title,alarmType,bjwz,nearby){
    		if(alarmIndexMap[id] != null){
    			layer.setTopByCall(alarmWinObjMap[id]);
    			return;
    		}
    		global_bjwz =  bjwz.replace("</br>",";");
    		if(alarmType=="jwdl" || alarmType=="jwnd" || alarmType=="jwwd" || alarmType=="jwyw"){
    			alarmType ="methane";
    		}
    		var url = encodeURI('gis/alarm/queryAlarmDetail?id='+id+"&PP_BJLX="+alarmType+"&bjwz="+bjwz+"&nearbyid="+nearby.featureid);
    		var width = screen.width>=400?"400px":"100%";
    		var height = screen.height>=1000?"500px":"100%";
    		var index = layer.open({
    		    type: 2,
    		    title:title,
    		    skin: 'layui-layer-rim', //加上边框
    		    area: [width, height], //宽高
    		    scrolling:'yes',
    		    scrollbar:'yes',
    		    closeBtn: 1,
    		    
    		    maxmin: false,
    		    //content: 'html内容',
    		    content: [url, 'yes'],
    		    shade: 0,
    		    success: function(layero){
    	            layer.setTop(layero); //重点2
    	            alarmWinObjMap[id] = layero;
    		    },
    		    cancel:function(index){
//    		    	console.info(index);
    		    	var id = alarmIdMap[index];
    		    	delete alarmIndexMap[id];
    		    	delete alarmWinObjMap[id];
    		    	delete alarmIdMap[index];
    		    }
    		});
    		alarmIndexMap[id] = index;
    		alarmIdMap[index] = id;
    	}
    	var tempdata ;
    	function locationMap(data)
		{
    		tempdata = data;
	  		if($("#iframepage").attr("src")!="gis/toGisNew"){
				$("#iframepage").attr("src","gis/toGisNew");
				
				setTimeout("location_timeout()",5000);
			}else{
				location_timeout();
			}
		}
    	function location_timeout(){
    		document.getElementById('iframepage').contentWindow.showAlarmPointMutli(tempdata,true,false);
    	}
    	function closeWinByAlarmId(id){
    		var index = alarmIndexMap[id];
    		delete alarmIndexMap[id];
    		delete alarmWinObjMap[id];
    		layer.close(index);
    		//removeAlarm(id);
    		//if(tempIcon){
    		//	alarmVector_temp.removeFeature(tempIcon);
    			//tempIcon = null;
    		//}
    		//ol.Observable.unByKey(listenerMp[id]);
    		closeToastrById(id);
    	}
    	function clickAlarm_timeout(){
    		document.getElementById('iframepage').contentWindow.showAlarmPointMutli(tempdata,true,true);
    	}
    	function showBatchOpt(){
    		var title = "批量处理";
    		var type="success";
    		var content = '<button class="btn btn-primary" type="button" onclick="batchOpt();">确认</button>&nbsp;&nbsp;&nbsp;'
    					+'<button class="btn btn-primary" type="button"	onclick="clearAllAlarm();">隐藏</button>'
			return doToast(type,content,title, null,"-1","-1",false,false);			
    	}
    	function  batchOpt(){
    		alarmIdList[alarmIdList.length]=" ";
    		$.ajax({
				type : "POST",
				url : "gis/alarm/batchUpdateAlarm",
				dataType : "JSON",
				data : {"alarmIdList":alarmIdList},
				traditional: true,  
				async : false,
				success : function(data) {
					
						layer.msg('处理完成');
						clearAllAlarm();
						if(alarmIdList[alarmIdList.length-1]==" "){
							alarmIdList.pop();
						}
						for(var i =0;i<alarmIdList.lenght;i++){
							alarmIdList[i] =alarmIdList[i].split("::")[0]; 
						}
						socket.emit('processAlarmArray',alarmIdList);
						alarmIdList = new Array();
						if($("#iframepage").attr("src")=="gis/toGisNew"){
							document.getElementById('iframepage').contentWindow.clearAlarm();
						}
				},
				error:function (data){
					//alert("asdfsad");
				}
			});
    	}
    	//关闭所有toastr
    	function clearAllAlarm(){
    		toastr.clear();
    		batchToastr.remove();
    	}
    	function closeToastrById(id){
    		if(alarmMap[id]!=null){
    			alarmMap[id].remove();
    			delete alarmMap[id];
    			//如果所有toastr都关了，干掉batchToastr
    			if(isEmpty(alarmMap)){
    				batchToastr.remove();
    				socket.emit('cleanAlarm',"");
    			}
    		}
    	}
    	var batchToastr;
    	
    	function getTimeDifference(alarmtime){
    		alarmtime = alarmtime.replace("T"," ").replace("Z","").replace(".000","");
    		var begintime_ms = Date.parse(new Date(alarmtime.replace(/-/g, "/")));  
    		//var endtime="2016-04-20 05:16:18";
    		var d=  new Date()
    		var endtime_ms = d.getTime();// Date.parse(new Date(endtime.replace(/-/g, "/")));  
    		return endtime_ms-begintime_ms;
    	}
    	var aa=0;
    	function showAlarm(data,alarmType){
    	//	console.info((++aa)+" alarmType:"+alarmType)
    		if(alarmType=="dvs" && (data.alevel==getAlarmTruck() || data.alevel ==getAlarmTrain())){
    			if(getTimeDifference(data.adate)<getAlarmTraficTimeout()){
	    			if($("#iframepage").attr("src")=="gis/toGisNew"){
	    				document.getElementById('iframepage').contentWindow.showTruckOrTrain(data);
	    			}
    			}
    			return;
    		}
    		if(alarmMap[data.aid]!=null){
    			alarmMap[data.aid].remove();
    			if($("#iframepage").attr("src")=="gis/toGisNew"){
    				document.getElementById('iframepage').contentWindow.removeAlarm(data.aid);
    			}
    		}
    		data.alarmType=alarmType;
    		
    		alarmIdList[alarmIdList.length]=data.aid+"::"+data.alarmType;
			var title = alarmType=="dts"?"温度报警":"震动报警";
			
			var type = data.alevelname=="断纤"?"dx":alarmType;
			if(alarmType == "methane"){
				title = data.atype == "010604" ? "甲烷浓度报警" : (data.atype == "010603" ? "甲烷电量报警" : (data.atype == "010605" ? "甲烷环境温度报警" : "甲烷液位报警"));
				type = data.atype == "010604" ? "jwnd" : (data.atype == "010603" ? "jwdl" : (data.atype == "010605" ? "jwwd" : "jwyw"));
				data.alarmType = data.atype == "010604" ? "jwnd" : (data.atype == "010603" ? "jwdl" : (data.atype == "010605" ? "jwwd" : "jwyw"));
				//type = data.atype == "010604" ? "jwnd" : "jwdl";
				//data.alarmType = data.atype == "010604"?"jwnd":"jwdl";
			}
			if(type == "dx"){
				title+="-断纤 ";
			}
			//var pploc_str = data.pploc == null?"":"("+data.pploc+")"
			var bjwz1 = getIns(data);
			var bjwz;
			var pploc_str = data.pploc == null ? "" : "(" + data.pploc + ")米。";
			if(alarmType == 'methane'){
				bjwz = bjwz1;
			}else if(alarmType == 'dts' || alarmType == "dvs"){
				bjwz = data.fibloc + pploc_str + bjwz1;
			}
			
			data.bjwz = bjwz;
			/*if(bjwz==""){
				bjwz=data.fibloc+pploc_str;
			}*/
			var ppname_str =  data.ppname==null?"对应管道不存在": data.ppname;	
			
			var content =ppname_str+"</br>报警位置:"+bjwz+"</br>报警等级:"+data.alevelname;
  		  	
			var toastrObj = doToast(type,content,title, function(){
				clickAlarm(data);
			},"-1","-1",false);
			alarmMap[data.aid] = toastrObj;
			if(batchToastr != null){
				//toastr.remove(batchToastr);
				batchToastr.remove();
			}
    		batchToastr = showBatchOpt();
    		alarmCount++;
  		  var sendtime = "&nbsp;";
  		  
  		  if(data.adate!='undefined'){
  			  sendtime=new Date(data.adate).format("yyyy-MM-dd hh:mm:ss");
  		  }
  		  
  		  var msgHtml = '<li id=\"li'+data.aid+'\">'
  			  		  + '<div class=\"dropdown-messages-box\">'
  			  		  + '<div class=\"media-body\">'
  			  		  + '<strong><i class=\"fa fa-envelope fa-fw\"></i></strong>'+title	
  			  		  + '<br>'
  			  		  + '<small class=\"text-muted\">'+sendtime+'</small><br><small class=\"text-muted\"><a id=\"mail_'+data.aid+'\" href=\"javascript:void(0);\" ><i class=\"fa fa-envelope\"></i> <strong>接收</strong></a></small>'
  			  		  + '</div>'
  	                  + '</div>'
  	                  + '</li>'
  	                  + '<li class=\"divider\" id=\"lid'+data.aid+'\"></li>';
  		  
  		  $("#alarmList").after(msgHtml);
  		  alarmNum++;
  		  $("#alarmNumTag").html(alarmNum);
  		  var alarmMsg = document.getElementById("mail_"+data.aid);
  	      addEventHandler(alarmMsg,"click",clickAlarm,data);
	  		if($("#iframepage").attr("src")!="gis/toGisNew"){
				//$("#iframepage").attr("src","gis/toGisNew");
				
				//setTimeout("document.getElementById('iframepage').contentWindow.showAlarmPoint('asdf')",1000);
			}else{
				document.getElementById('iframepage').contentWindow.showAlarmPointMutli(data,false,false);
			}
	  	}
    
    	function addEventHandler(obj,eventName,fun,param){
    	    var fn = fun;
    	    if(param)
    	    {
    	        fn = function(e)
    	        {
    	            fun.call(this, param);  
    	        }
    	    }
    	    if(obj.attachEvent){
    	        obj.attachEvent('on'+eventName,fn);
    	    }else if(obj.addEventListener){
    	        obj.addEventListener(eventName,fn,false);
    	    }else{
    	        obj["on" + eventName] = fn;
    	    }
    	}
    	function showAlarmOnGis(){
    		
    	}
    	
        function fullScreen(element) {
		    var el = document.getElementById("iframepage"),
		        rfs = el.requestFullScreen || el.webkitRequestFullScreen || el.mozRequestFullScreen || el.msRequestFullScreen,
		        wscript;
		 
		    if(typeof rfs != "undefined" && rfs) {
		        rfs.call(el);
		        return;
		    }
		 
		    if(typeof window.ActiveXObject != "undefined") {
		        wscript = new ActiveXObject("WScript.Shell");
		        if(wscript) {
		            wscript.SendKeys("{F11}");
		        }
		    }
		}
        function exitFullScreen() {
       	  if(document.exitFullscreen) {
       	    document.exitFullscreen();
       	  } else if(document.mozCancelFullScreen) {
       	    document.mozCancelFullScreen();
       	  } else if(document.webkitExitFullscreen) {
       	    document.webkitExitFullscreen();
       	  }
       	}
        function readAlarm (aid){
    		$("#li"+aid).remove();
    		$("#lid"+aid).remove();
    		alarmNum--;
    		$("#alarmNumTag").html(alarmNum);
    	}
    </script>
</body>

</html>