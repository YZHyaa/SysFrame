<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<system:header />
</head>
<!-- md-skin  skin-1  skin-2  skin-3-->
<body class="fixed-nav md-skin"   style="overflow:hidden;">
	<div id="wrapper">
		<!-- 左边菜单栏 -->
		<nav class="navbar-default navbar-static-side" role="navigation">
			<div class="sidebar-collapse">
				<ul class="nav metismenu" id="side-menu">
					<li class="nav-header">
						<div class="dropdown profile-element">
<!-- 							 <span class="clear"> <span class="block m-t-xs"> <strong -->
<%-- 									class="font-bold">${pd.SYSNAME }</strong> --%>
<!-- 							</span> <span class="text-muted text-xs block">JAVA快速开发 <b -->
<!-- 									class="caret"></b></span> -->
<!-- 							</span> -->
						</div>
						<div class="logo-element">JAVA</div>
					</li>
					<c:forEach items="${menuList}" var="item" varStatus="i">
						<li <c:if test="${i.index==0}">class="active"</c:if> onclick=""><a href="index.html"><i class="fa ${item.MENU_ICON}"></i> 
							<span class="nav-label">${item.MENU_NAME}</span>
							<span class="fa arrow"></span></a>
							<ul class="nav nav-second-level">
								<c:forEach items="${item.subMenu}" var="subItem" varStatus="si">
									<li><a href="javascript:void(0);" onclick="goto('${subItem.MENU_URL}')"><i class="fa ${subItem.MENU_ICON}"></i> ${subItem.MENU_NAME}</a></li>
								</c:forEach>
							</ul>
						</li>
					</c:forEach>
				</ul>
			</div>
		</nav>
		<div id="page-wrapper" class="gray-bg dashbard-1">
			<!-- 顶部栏 -->
			<div class="row border-bottom">
				<nav class="navbar navbar-fixed-top" role="navigation"
					style="margin-bottom: 0">
					<div class="navbar-header">
						<a class="navbar-minimalize minimalize-styl-2 btn btn-primary "
							href="#"><i class="fa fa-bars"></i> </a>
						<h2 style="margin:14px 5px 5px 15px;float:left;color:white;">JavaWeb快速开发权限框架——${pd.SYSNAME }</h2>
						
						<form role="search" class="navbar-form-custom"
							action="search_results.html">
					
							<div class="form-group">
								<input type="text" placeholder="搜索"
									class="form-control" name="top-search" id="top-search">
							</div>
						</form>
					</div>
					<ul class="nav navbar-top-links navbar-right">
						<li><span class="m-r-sm text-muted welcome-message">${user.NAME}</span></li>
						<li><a href="<%=basePath%>logout"> <i class="fa fa-sign-out"></i>
								退出登录
						</a>
						</li>
						<li><a href="javascript:void(0);" onclick="changePwd()"> <i class="fa fa-gear"></i>
								修改密码
						</a>
						</li>
						<li><a class="right-sidebar-toggle"> <i
								class="fa fa-tasks"></i>
						</a></li>
					</ul>

				</nav>
			</div>
			<!--内容 -->
			<div  style="margin:15px 0px 10px 0px;">
				<iframe class="J_iframe" id="iframepage" name="iframe0" width="100%" scrolling="yes" onload="changeFrameHeight()" 
					src="" frameborder="0"></iframe>
			</div>
			<!-- 内容结束 -->
			<!-- 尾栏 -->
			<div class="footer">
            	<div class="pull-right">
              	 	 10GB of <strong>250GB</strong> Free.
            	</div>
            	<div>
                	<strong>Copyright</strong> Example Company &copy; 2014-2017
            	</div>
        	</div>
		</div>
	</div>
	
	<!--设置下拉栏 -->
	<div id="right-sidebar" class="animated">
		<div class="sidebar-container">
			<div class="tab-content">

				<div class="sidebar-title">
					<h3>
						<i class="fa fa-gears"></i> 设置
					</h3>
				</div>
				<div class="setings-item">
					<span> Show notifications </span>
					<div class="switch">
						<div class="onoffswitch">
							<input type="checkbox" name="collapsemenu"
								class="onoffswitch-checkbox" id="example"> <label
								class="onoffswitch-label" for="example"> <span
								class="onoffswitch-inner"></span> <span
								class="onoffswitch-switch"></span>
							</label>
						</div>
					</div>
				</div>
				<div class="setings-item">
					<span> Disable Chat </span>
					<div class="switch">
						<div class="onoffswitch">
							<input type="checkbox" name="collapsemenu" checked
								class="onoffswitch-checkbox" id="example2"> <label
								class="onoffswitch-label" for="example2"> <span
								class="onoffswitch-inner"></span> <span
								class="onoffswitch-switch"></span>
							</label>
						</div>
					</div>
				</div>
				<div class="setings-item">
					<span> Enable history </span>
					<div class="switch">
						<div class="onoffswitch">
							<input type="checkbox" name="collapsemenu"
								class="onoffswitch-checkbox" id="example3"> <label
								class="onoffswitch-label" for="example3"> <span
								class="onoffswitch-inner"></span> <span
								class="onoffswitch-switch"></span>
							</label>
						</div>
					</div>
				</div>
				<div class="setings-item">
					<span> Offline users </span>
					<div class="switch">
						<div class="onoffswitch">
							<input type="checkbox" checked name="collapsemenu"
								class="onoffswitch-checkbox" id="example5"> <label
								class="onoffswitch-label" for="example5"> <span
								class="onoffswitch-inner"></span> <span
								class="onoffswitch-switch"></span>
							</label>
						</div>
					</div>
				</div>
				<div class="setings-item">
					<span> Global search </span>
					<div class="switch">
						<div class="onoffswitch">
							<input type="checkbox" checked name="collapsemenu"
								class="onoffswitch-checkbox" id="example6"> <label
								class="onoffswitch-label" for="example6"> <span
								class="onoffswitch-inner"></span> <span
								class="onoffswitch-switch"></span>
							</label>
						</div>
					</div>
				</div>
				<div class="setings-item">
					<span> Update everyday </span>
					<div class="switch">
						<div class="onoffswitch">
							<input type="checkbox" name="collapsemenu"
								class="onoffswitch-checkbox" id="example7"> <label
								class="onoffswitch-label" for="example7"> <span
								class="onoffswitch-inner"></span> <span
								class="onoffswitch-switch"></span>
							</label>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="indexModal" class="modal inmodal fade" tabindex="-1"
		role="dialog" aria-hidden="true"></div>
	<system:jsFooter/>
	<script>
	
	function changeFrameHeight(){
	    var ifm= document.getElementById("iframepage"); 
	    ifm.height=document.documentElement.clientHeight-30;
	}
	window.onresize=function(){  
	     changeFrameHeight();  
	}
	
	function goto(url) {
	    if(url === "#")return;
		$("#iframepage").attr("src",url);
        return false;
    }
    function changePwd(){
        $("#indexModal").load("toChangePwd",function(){
            $("#indexModal").modal();
        });
    }
	</script>
</body>
</html>
