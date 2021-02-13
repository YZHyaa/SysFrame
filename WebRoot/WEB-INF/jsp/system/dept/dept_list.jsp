<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
	<head>
	 <system:header/>
	<link href="hplus/css/plugins/treeview/bootstrap-treeview.css?v=20170904" rel="stylesheet">
	<!-- jsp文件头和头部 -->
	</head> 
<body class="gray-bg">
	<div class="animated fadeInRight">
	   <div class="row">
            <div class="col-sm-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>组织管理</h5>
                        <div class="ibox-tools">
                            <shiro:hasPermission name="dept:add">
                            <a href="javascript:toAdd()" class="btn btn-primary" id="addFun">新增</a>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="dept:edit">
                            <a href="javascript:toEdit()" class="btn btn-primary" id="editFun">修改</a>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="dept:delete">
                            <a href="javascript:toDel()" class="btn btn-danger" id="deleteFun">删除</a>
                        	</shiro:hasPermission>
                        </div>
                        <div class="ibox-content">
							<div class="col-sm-5">
		                        <div id="treeview" class="test"></div>
		                    </div>
		                    <div class="col-sm-7">
		                           <div class="row" id="loadSta">
		                        </div>
		                        <div class="row" >
		                        	<div class="col-sm-10 col-sm-offset-1">
		                        	<div class="ibox float-e-margins">
					                    <div class="ibox-content">
					                        <table class="table" style="table-layout:fixed;word-wrap: break-word;">
					                            <thead>
					                                <tr>
					                                	<th width="30%">单位编号</th>
					                                    <th width="60%">单位名称</th>
					                                    <th width="10%">排序</th>
					                                </tr>
					                            </thead>
					                            <tbody id="dept">
					                            </tbody>
					                        </table>
					                    </div>
                					</div>
                					</div>
		                        </div>
		                    </div>
		                    <div class="clearfix"></div>
	                    </div>
                    </div>
                </div>
            </div>
        </div>	
	</div>	
	
	<div id="myModal" class="modal inmodal fade" tabindex="-1" role="dialog"  aria-hidden="true">
		                        	 
	</div>
	
		<!-- 全局js -->
	 <system:jsFooter/>
    <!-- treeview -->
    <script src="hplus/js/plugins/treeview/bootstrap-treeview.js?v=20170904"></script>
    <!-- 自定义js -->
    <script src="hplus/js/content.min.js?v=20170904"></script>
		<script type="text/javascript">
		var selectNodeid = 0;
		$(document).ready(function () {
			makeTree();
			$("#myModal").on('hidden.bs.modal',function(){
				reloadTree();
			})
        });
		
		
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
				        onNodeSelected:function(event,node){
				        	selectNodeid = node.id;
				        	$("#loadSta").html(loadingCssHtml);
				        	$("#dept").html('');
			        		$.ajax({
			        			type: "POST",
			    				url: 'dept/getSubTree',
			    				dataType:'json',
			    				data:{pid:node.id},
			    				cache: false,
			    				success:function(menulist){
			    					$("#loadSta").html('');
			    					var deptHtml = '';
			    					$(menulist).each(function(i,val){
			    						deptHtml += '<tr><td>'+val.DEPT_CODE+'</td><td>'+val.DEPT_NAME+'</td><td>'+val.DEPT_ORDER+'</td><tr>';
			    						
			    					});
			    					$("#dept").html(deptHtml);
			    				}
			        		});
				        	
				        }
				    });
				}
			});
		}
		
		function reloadTree(){
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
				        onNodeSelected:function(event,node){
				        	selectNodeid = node.id;
				        	$("#loadSta").html(loadingCssHtml);
				        	$("#dept").html('');
			        		$.ajax({
			        			type: "POST",
			    				url: 'dept/getSubTree',
			    				dataType:'json',
			    				data:{pid:node.id},
			    				cache: false,
			    				success:function(menulist){
			    					$("#loadSta").html('');
			    					var deptHtml = '';
			    					$(menulist).each(function(i,val){
			    						deptHtml += '<tr><td>'+val.DEPT_CODE+'</td><td>'+val.DEPT_NAME+'</td><td>'+val.DEPT_ORDER+'</td><tr>';
			    						
			    					});
			    					$("#dept").html(deptHtml);
			    				}
			        		});
				        	
				        }
				    });
				}
			});
		}
		
		
		function toAdd(){
			$("#myModal").load("dept/toAdd?pid="+selectNodeid,function(){
				$("#myModal").modal();
			})
		}
		
		function toEdit(){
			if(selectNodeid==undefined||selectNodeid==0){
				layer.msg('请先选择一个单位');
				return false;
			}
			
			$("#myModal").load("dept/toEdit?id="+selectNodeid,function(){
				$("#myModal").modal();
			})
		}
		
		function toDel(){
			if(selectNodeid==undefined||selectNodeid==0){
				layer.msg('请先选择一个单位');
				return false;
			}
			
			layer.confirm('操作将会删除当前单位，确认删除吗？',{
				btn:['确认','取消'],
				shade:false
			},function(){
				$.ajax({
					type: "POST",
					url: 'dept/delete.do',
			    	data: {'id':selectNodeid},
					dataType:'json',
					//beforeSend: validateData,
					cache: false,
					success: function(data){
						
						if(data.msg=='ok'){
							layer.msg('删除信息成功');
							reloadTree();
						}else{
							layer.msg('删除信息失败');
						}
					}
				});
				
			},function(){
				
			}
			);
			
		}
		
		</script>
		
	</body>
</html>

