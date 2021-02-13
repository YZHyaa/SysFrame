<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
	<head>
	 <system:header/>
	<!-- jsp文件头和头部 -->
	</head> 
<body class="gray-bg">
	<div class="animated fadeInRight">
	   <div class="row">
            <div class="col-sm-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>角色信息管理 <small>修改</small></h5>
                        <div class="ibox-tools">
                            
                        </div>
                    </div>
                    <div class="ibox-content">
                        <form id="roleForm" name="roleForm" action="role/saveEdit"  method="post" class="form-horizontal">
                        	<input type="hidden" name="ROLE_ID" id="role_id" value="${pd.ROLE_ID }"/>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">角色编号</label>
                                <div class="col-sm-8">
                                    <input type="text" class="form-control" name="ROLE_CODE" id="role_code" value="${pd.ROLE_CODE }">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">角色名称</label>
                                <div class="col-sm-8">
                                    <input type="text" class="form-control required" name="ROLE_NAME" id="role_name" value="${pd.ROLE_NAME }">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">排序</label>
                                <div class="col-sm-8">
                                    <input type="text" class="form-control required digits" name="ROLE_ORDER" id="role_order" value="${pd.ROLE_ORDER }">
                                </div>
                            </div>
                            <div class="hr-line-dashed"></div>
                            <div class="form-group">
                                <div class="col-sm-4 col-sm-offset-2">
                                 	<shiro:hasPermission name="role:save">
                                    	<button class="btn btn-primary" type="submit">保存内容</button>
                                 	</shiro:hasPermission>
                                    <button class="btn btn-white" type="button" onclick="goBack();">取消</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>	
	</div>	
	<!-- 全局js -->
    <system:jsFooter/>
    <!-- 附件上传 -->
	<script src="hplus/js/plugins/webuploader/webuploader_pegasus.js?v=20170904"></script>
    <!-- 自定义js -->
    <script src="hplus/js/content.min.js?v=20170904"></script>
    
    <script type="text/javascript">
	
    $().ready(function(){
		$("#roleForm").validate();
		
	});
    
	function goBack(){
		this.location.href="<%=basePath%>role/listRoles";
	}
	
</script>
	</body>
</html>

