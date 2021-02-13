<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8"/>
<title>后台登录</title>
<meta name="author" content="DeathGhost" />
<link rel="stylesheet" type="text/css" href="plugins/loginShink/css/style.css" tppabs="css/style.css" />
<style>
body{height:100%;background:#16a085;overflow:hidden;}
canvas{z-index:-1;position:absolute;}
</style>
<script src="hplus/js/jquery.min.js"></script>
<script src="plugins/loginShink/js/verificationNumbers.js" tppabs="js/verificationNumbers.js"></script>
<script src="plugins/loginShink/js/Particleground.js" tppabs="js/Particleground.js"></script>
<script>
$(document).ready(function() {
  //粒子背景特效
  $('body').particleground({
    dotColor: '#5cbdaa',
    lineColor: '#5cbdaa'
  });
});
</script>
</head>
<body>
<dl class="admin_login">
 <dt>
  <strong>JavaWeb快速开发框架</strong>
  <em>管理系统</em>
 </dt>
 <dd class="user_icon">
  <input id="loginname" type="text" placeholder="账号" class="login_txtbx"/>
 </dd>
 <dd class="pwd_icon">
  <input id="password" type="password" placeholder="密码" class="login_txtbx"/>
 </dd>

 <dd>
  <button id="loginBtn"  type="button" onclick="severCheck();"  class="submit_btn">登&nbsp;&nbsp;陆</button>
  <input id="code"  name="code" value="123"  type="hidden">
 </dd>
 <dd>
  <p></p>
 </dd>
</dl>
</body>
<script src="hplus/js/plugins/jqtips/jquery.tips.js"></script>
<script type="text/javascript" src="static/js/jquery.cookie.js?v=20170904"></script>
<script type="text/javascript">
		//服务器校验
		function severCheck(){
			if(check()){
				var loginname = $("#loginname").val();
				var password = $("#password").val();
				var code = "PKSPKSluyun"+loginname+",luyun,"+password+"PKSPKSluyun"+",luyun,"+$("#code").val();
				$.ajax({
					type: "POST",
					url: 'login_login',
			    	data: {KEYDATA:code,tm:new Date().getTime()},
					dataType:'json',
					cache: false,
					success: function(data){
						if("success" == data.result){
							saveCookie();
							window.location.href="<%=basePath%>main/index";
						}else if("usererror" == data.result){
							$("#loginBtn").tips({
								side : 3,
								msg : "用户名或密码有误",
								bg : '#FF5080',
								time : 1
							});
							$("#loginname").focus();
						}else if("codeerror" == data.result){
							$("#code").tips({
								side : 1,
								msg : "验证码输入有误",
								bg : '#FF5080',
								time : 1
							});
							$("#code").focus();
						}else{
							$("#loginname").tips({
								side : 1,
								msg : "缺少参数",
								bg : '#FF5080',
								time : 15
							});
							$("#loginname").focus();
						}
					}
				});
			}
		}
	
		$(document).ready(function() {
			changeCode();
			$("#codeImg").bind("click", changeCode);
		});

		$(document).keyup(function(event) {
			if (event.keyCode == 13) {
				$("#loginBtn").trigger("click");
			}
		});

		function genTimestamp() {
			var time = new Date();
			return time.getTime();
		}

		function changeCode() {
			$("#codeImg").attr("src", "code.do?t=" + genTimestamp());
		}

		//客户端校验
		function check() {

			if ($("#loginname").val() == "") {

				$("#loginname").tips({
					side : 3,
					msg : '用户名不得为空',
					bg : '#AE81FF',
					time : 1
				});

				$("#loginname").focus();
				return false;
			} else {
				$("#loginname").val(jQuery.trim($('#loginname').val()));
			}

			if ($("#password").val() == "") {

				$("#password").tips({
					side : 3,
					msg : '密码不得为空',
					bg : '#AE81FF',
					time : 1
				});

				$("#password").focus();
				return false;
			}
			return true;
		}

	

		function saveCookie() {
			if ($("#saveid").attr("checked")) {
				$.cookie('loginname', $("#loginname").val(), {
					expires : 7
				});
				$.cookie('password', $("#password").val(), {
					expires : 7
				});
			}
		}
		
		
		jQuery(function() {
			var loginname = $.cookie('loginname');
			var password = $.cookie('password');
			if (typeof(loginname) != "undefined"
					&& typeof(password) != "undefined") {
				$("#loginname").val(loginname);
				$("#password").val(password);
				$("#saveid").attr("checked", true);
				$("#code").focus();
			}
		});
	</script>
</html>

</html>
