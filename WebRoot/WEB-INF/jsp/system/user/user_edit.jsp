<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
<div class="modal-dialog">
	<div class="modal-content animated bounceInRight">
		<form id="userForm">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal">
				<span aria-hidden="true">&times;</span><span class="sr-only">关闭</span>
			</button>
			<h2 class="modal-title" style="text-align: -webkit-left;">用户管理-<small>新增</small></h2>
		</div>
		<div class="modal-body">
			<input type="hidden" name="USER_ID" id="user_id"
								value="${pd.USER_ID }" />
			<div style="margin-bottom：8px;">
				<label>编号</label><input type="text" class="form-control" name="SNUMBER"
							 value="${pd.SNUMBER }"	id="SNUMBER" onblur="hasN('${pd.USERNAME }')">
			</div>
			<div  style="margin-bottom：8px;">
				<label>用户名</label><input type="text" class="form-control" value="${pd.USERNAME }"  name="USERNAME" id="loginname">
			</div>
		
			<div style="margin-bottom：8px;">
				<label>姓名</label> 	<input type="text" class="form-control" name="NAME" id="name" value="${pd.NAME }">
			</div>
			<div style="margin-bottom：10px;">
				<label>备注</label><input type="text" class="form-control" name="BZ" id="BZ" value="${pd.BZ }">
			</div>
			
		</div>
		<div class="modal-footer">
			<button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
			<shiro:hasPermission name="user:save">
			<button type="submit" class="btn btn-primary">保存</button>
			</shiro:hasPermission>
		</div>
		</form>
	</div>
</div>
<script type="text/javascript">
	$().ready(function(){
		$('#userForm').validate({
			submitHandler:function(form){
				
				if($("#SNUMBER").val()==""){
					
					$("#SNUMBER").tips({
						side:3,
			            msg:'输入编号',
			            bg:'#AE81FF',
			            time:3
			        });
					$("#SNUMBER").focus();
					return false;
				}else{
					$("#SNUMBER").val($.trim($("#SNUMBER").val()));
				}
				if($("#loginname").val()=="" || $("#loginname").val()=="此用户名已存在!"){
					
					$("#loginname").tips({
						side:3,
			            msg:'输入用户名',
			            bg:'#AE81FF',
			            time:2
			        });
					return false;
					
				}
				if($("#name").val()=="" || $("#name").val()=="此用户名已存在!"){
					
					$("#name").tips({
						side:3,
			            msg:'输入姓名',
			            bg:'#AE81FF',
			            time:2
			        });
					return false;
					
				}

				
				if($("#user_id").val()==""){
					hasU();
					return false;
				}
				$.ajax({
					type:'post',
					url:'user/saveEdit',
					data:$('#userForm').serialize(),
					success:function(data){
						layer.msg('新增成功');
						getUserByDept(deptId);
						$("#myModal").modal('hide');
					}
				});
			}
		});
	});
	
	
	function ismail(mail){
		return(new RegExp(/^(?:[a-zA-Z0-9]+[_\-\+\.]?)*[a-zA-Z0-9]+@(?:([a-zA-Z0-9]+[_\-]?)*[a-zA-Z0-9]+\.)+([a-zA-Z]{2,})+$/).test(mail));
		}
	
	//判断用户名是否存在
	function hasU(){
		var USERNAME = $.trim($("#loginname").val());
		$.ajax({
			type: "POST",
			url: 'user/hasU.do',
	    	data: {USERNAME:USERNAME,tm:new Date().getTime()},
			dataType:'json',
			cache: false,
			success: function(data){
				 if("success" == data.result){
					$("form").submit();
				 }else{
					$("#loginname").css("background-color","#D16E6C");
					setTimeout("$('#loginname').val('此用户名已存在!')",500);
				 }
			}
		});
	}
	
	//判断邮箱是否存在
	function hasE(USERNAME){
		var EMAIL = $.trim($("#EMAIL").val());
		$.ajax({
			type: "POST",
			url: 'user/hasE.do',
	    	data: {EMAIL:EMAIL,USERNAME:USERNAME,tm:new Date().getTime()},
			dataType:'json',
			cache: false,
			success: function(data){
				 if("success" != data.result){
					 $("#EMAIL").tips({
							side:3,
				            msg:'邮箱已存在',
				            bg:'#AE81FF',
				            time:3
				        });
					setTimeout("$('#EMAIL').val('')",2000);
				 }
			}
		});
	}
	
	//判断编码是否存在
	function hasN(USERNAME){
		var NUMBER = $.trim($("#SNUMBER").val());
		$.ajax({
			type: "POST",
			url: 'user/hasN.do',
	    	data: {SNUMBER:NUMBER,USERNAME:USERNAME,tm:new Date().getTime()},
			dataType:'json',
			cache: false,
			success: function(data){
				 if("success" != data.result){
					 $("#SNUMBER").tips({
							side:3,
				            msg:'编号已存在',
				            bg:'#AE81FF',
				            time:3
				        });
					setTimeout("$('#SNUMBER').val('')",2000);
				 }
			}
		});
	}
	
	
	function openDeptSelect(){
		$("#myModal").load("user/toDeptSelect?DEPT_ID="+$("#DEPT_ID").val(),function(){
			$("#myModal").modal();
		});
	}
	
</script>

