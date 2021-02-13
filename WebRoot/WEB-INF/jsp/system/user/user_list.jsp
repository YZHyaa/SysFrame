<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<system:header />
<link
	href="hplus/css/plugins/treeview/bootstrap-treeview.css?v=20170904"
	rel="stylesheet">
<!-- jsp文件头和头部 -->
</head>
<body class="gray-bg">
	<div class="animated fadeInRight">
		<div class="row">
			<div class="col-sm-12">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>用户管理</h5>
					</div>
					<div class="ibox-content">
						<div class="row">
							<div class="col-sm-4">
								<div style="margin-top:10px;" id="treeview" class="test"></div>
							</div>
							<div class="col-sm-8">
								<div id="toolbar" class="btn-group">
									<div class="pull-left form-inline form-group">
										<input type="text" id="USERNAME" name="USERNAME" style="width:130px;"
											class="form-control" placeholder="用户名称">
										<button type="button" class="btn  btn-primary"
											onclick="bstQuery();">
											<i class="fa fa-search" aria-hidden="true"></i>&nbsp;查询
										</button>
										<shiro:hasPermission name="user:add">
											<a href="javascript:toAdd()"
												class="btn btn-primary" id="addFun">新增</a>
										</shiro:hasPermission>
										<shiro:hasPermission name="user:edit">
											<a href="javascript:toEdit()"
												class="btn  btn-primary" id="editFun">修改</a>
										</shiro:hasPermission>
										<shiro:hasPermission name="user:exp">
											<a href="javascript:toExport()"
												class="btn  btn-primary" id="expFun">导出</a>
										</shiro:hasPermission>
										<shiro:hasPermission name="user:delete">
											<a href="javascript:toDel()"
												class="btn  btn-danger" id="deleteFun">删除</a>
										</shiro:hasPermission>
										<shiro:hasPermission name="user:config">
											<button type="button" class="btn  btn-primary"
												onclick="toAddRole();">添加角色</button>
										</shiro:hasPermission>
										<shiro:hasPermission name="user:config">
											<button type="button" class="btn  btn-primary"
												onclick="resetPwd();">重置密码</button>
										</shiro:hasPermission>
										<shiro:hasPermission name="user:view">
											<button type="button" class="btn  btn-primary"
												onclick="showRP();">查看授权</button>
										</shiro:hasPermission>
									</div>
								</div>

								<table id="queryTable" data-mobile-responsive="true"></table>

							</div>
						</div>

					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="myModal" class="modal inmodal fade" tabindex="-1"
		role="dialog" aria-hidden="true"></div>
	<!-- 全局js -->
	<system:jsFooter />
	<script
		src="hplus/js/plugins/treeview/bootstrap-treeview.js?v=20170904"></script>
	<!-- 自定义js -->
	<script type="text/javascript">
var  deptId=0;
$(document).ready(function () {
	makeTree();
    table= $('#queryTable').bootstrapTable({
	    url: '',
	    toolbar:'#toolbar' ,
		showRefresh : false,
		showToggle : false,
		showColumns: false,
		striped:false,//取消隔行变色
	    columns: [
	    {
	        field: 'USER_ID',
	        visible:false,
	        halign: 'center',
	    }, {
	        field: 'SNUMBER',
	        title: '编号',
	        align: 'center',
	        halign: 'center',
	    }, {
	        field: 'USERNAME',
	        title: '用户名',
	        align: 'center',
	        halign: 'center',
	    }, {
	        field: 'NAME',
	        title: '姓名',
	        align: 'center',
	        halign: 'center',
	    },{
	    	field: 'LAST_LOGIN',
	        title: '最近登录',
	        align: 'center',
	        halign: 'center',
	    }
	    ] 
	});
});

//构造组织数
function makeTree(){
	$.ajax({
		type: "POST",
		url: 'dept/getTree',
		dataType:'json',
		//beforeSend: validateData,
		cache: false,
		success: function(treeData){
			$('#treeview').treeview({
		        data:treeData,
		        levels:3,
		        showBorder:false,
		        onNodeSelected:function(event,node){
		        	deptId=node.id;
		        	getUserByDept(node.id);
		        }
		    });
		}
	});
}


//根据单位id获取用户
function getUserByDept(id){
	 $('#queryTable').bootstrapTable('destroy');
	 $('#queryTable').bootstrapTable({
		    url: 'user/pageSerach?DEPT_ID='+id,
		    toolbar:'#toolbar' ,
			showRefresh : false,
			showToggle : false,
			showColumns: false,
			striped:false,//取消隔行变色
		    columns: [
		    {
		        field: 'USER_ID',
		        visible:false,
		        halign: 'center',
		    }, {
		        field: 'SNUMBER',
		        title: '编号',
		        align: 'center',
		        halign: 'center',
		    }, {
		        field: 'USERNAME',
		        title: '用户名',
		        align: 'center',
		        halign: 'center',
		    }, {
		        field: 'NAME',
		        title: '姓名',
		        align: 'center',
		        halign: 'center',
		    },{
		    	field: 'LAST_LOGIN',
		        title: '最近登录',
		        align: 'center',
		        halign: 'center',
		    }
		    ] 
		});
}
		
		function toAdd(){
			if(deptId==0){
				layer.msg("请先选择单位");
				return false;
			}
			$("#myModal").load("<%=basePath%>user/toAdd?DEPT_ID="+deptId,function(){
				$("#myModal").modal();
			})
<%-- 			window.location.href="<%=basePath%>user/toAdd?DEPT_ID="+deptId; --%>
		}
		
		function toEdit(){
			var ids = getBstCheckedId('USER_ID');
			if(!(ids.length==1)){
				layer.msg('请只选中一条信息再进行编辑。');
				return false;
			}
			$("#myModal").load("<%=basePath%>user/toEdit?USER_ID=" + ids[0],function(){
				$("#myModal").modal();
			})
<%-- 			window.location.href="<%=basePath%>user/toEdit?USER_ID=" + ids[0]; --%>
		}

		function toExport() {
			window.open('user/export');
		}
		
		//添加角色
		function toAddRole(){
			var ids = getBstCheckedId('USER_ID');
			if (!(ids.length == 1)) {
				layer.msg('请只选中一条信息再进行编辑。');
				return false;
			}
			$("#myModal").load("<%=basePath%>user/toAddRole?USER_ID=" + ids[0],function(){
				$("#myModal").modal();
			})
		}
		
		//重置密码
		function resetPwd() {
			var ids = getBstCheckedId('USER_ID');
			if (!(ids.length == 1)) {
				layer.msg('请只选中一条信息再进行编辑。');
				return false;
			}
			layer.confirm('确认将该用户密码重置为默认密码？', {
				btn : [ '确认', '取消' ],
				shade : false
			}, function() {
				$.ajax({
					type : "POST",
					url : 'user/resetPwd',
					data : {
						IDS : ids[0]
					},
					dataType : 'json',
					//beforeSend: validateData,
					cache : false,
					success : function(data) {
						if (data.msg == 'ok') {
							layer.msg('重置密码成功');
						} else {
							layer.msg('重置密码失败');
						}
					}
				});

			}, function() {

			});

		}

		function toDel() {
			var ids = getBstCheckedId('USER_ID');
			if ((ids.length < 1)) {
				layer.msg('请选中信息再进行删除。');
				return false;
			}
			var idsStr = ids.toString();
			layer.confirm('确认删除这些信息吗？', {
				btn : [ '确认', '取消' ],
				shade : false
			}, function() {
				$.ajax({
					type : "POST",
					url : 'user/delete.do?tm=' + new Date().getTime(),
					data : {
						IDS : idsStr
					},
					dataType : 'json',
					//beforeSend: validateData,
					cache : false,
					success : function(data) {
						bstQuery();
						if (data.msg == 'ok') {
							layer.msg('删除信息成功');
						} else {
							layer.msg('删除信息失败');
						}

					}
				});

			}, function() {

			});

		}

		function showRP() {
			var ids = getBstCheckedId('USER_ID');
			if (!(ids.length == 1)) {
				layer.msg('请只选中一条信息再进行编辑。');
				return false;
			}
			$("#myModal").html('');
			$("#myModal").load("user/showRP?USER_ID=" + ids[0], function() {
				$("#myModal").modal();
			})
		}
	</script>

</body>
</html>