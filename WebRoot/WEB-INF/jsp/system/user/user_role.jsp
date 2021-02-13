<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
<div class="modal-dialog">
	<div class="modal-content animated bounceInRight">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal">
				<span aria-hidden="true">&times;</span><span class="sr-only">关闭</span>
			</button>
			<h1 class="modal-title" style="text-align: -webkit-left;">用户角色管理</h1>
		</div>
		<div class="modal-body">
			<div class="row">
            <div class="col-sm-6">
                <div class="ibox">
                    <div class="ibox-content">
                        <h3>待选角色列表</h3>
                        <div class="input-group">
                            <input type="text" placeholder="查询角色" class="input input-sm form-control" id="queryRole">
                            <span class="input-group-btn">
                                        <button type="button" onclick="queryRole();" class="btn btn-sm btn-white"> <i class="fa fa-plus"></i> 查询</button>
                                </span>
                        </div>

                        <ul class="sortable-list connectList agile-list" id="list1">
                            <c:forEach items="${lRoleList }" var="item" varStatus="i">
                            	<li class="warning-element" id="${item.ROLE_ID }">
                                ${item.ROLE_NAME }<a href="javascript:move('${item.ROLE_ID }');" class="pull-right btn btn-xs btn-white">选择</a>
                            	</li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="col-sm-6">
                <div class="ibox">
                    <div class="ibox-content">
                        <h3>已选角色列表</h3>
                        <p class="small"><i class="fa fa-hand-o-up"></i> 在列表之间拖动面板</p>
                        <ul class="sortable-list connectList agile-list" id="list2">
                            <c:forEach items="${rRoleist }" var="item" varStatus="i">
                            	<li class="warning-element" id="${item.ROLE_ID }">
                                ${item.ROLE_NAME }<a href="javascript:move('${item.ROLE_ID }');" class="pull-right btn btn-xs btn-white">选择</a>
                            	</li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
		</div>
		<div class="modal-footer">
<!-- 			<button class="btn btn-primary" type="button" onclick="saveUser();">保存内容</button> -->
			<button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
		</div>
	</div>
</div>

<script type="text/javascript">
	var user='${userId}';
	
	$(window).load(function(){
		 $(".sortable-list").sortable({
             connectWith: ".connectList",
             receive:function(event,ui){
            }
         }).disableSelection();
	});
	
	function queryRole(){
		var queryStr = $("#queryRole").val();
		$.ajax({
			type: "POST",
			url: 'user/getSerachRole.do',
	    	data: {SERACHNAME:queryStr,USER_ID:user},
			dataType:'json',
			//beforeSend: validateData,
			cache: false,
			success: function(data){
				console.log(data);
				$('#list1').html('');
				for(var i=0;i<data.length;i++){
					var item = data[i];
					html='<li class=\"warning-element\" id=\"'+item.ROLE_ID+'\">'+item.ROLE_NAME
					+'<a href=\"javascript:move(\''+item.ROLE_ID+'\');\" class=\"pull-right btn btn-xs btn-white\">选择</a></li>'
					$('#list1').append(html); 
				}
				
			}
		});
	}
	
	
	function move(id){
		var list = $("#"+id).parent().attr("id");
		
     	if(list=='list1'){
     		$("#"+id).appendTo("#list2");
     	}else{
     		$("#"+id).appendTo("#list1");
     	}
     	
     	saveUser();
	}
	
	function saveUser(){
		var ids = "";
		$("#list2 li").each(function(index){
			if(ids==""){
				ids+=$(this).attr("id");
			}else{
				ids+=","+$(this).attr("id");
			}
		});
		
		$.ajax({
			type: "POST",
			url: 'user/addUserRole.do',
	    	data: {IDS:ids,USER_ID:user},
			dataType:'json',
			//beforeSend: validateData,
			cache: false,
			success: function(data){
				if(data.msg=='ok'){
// 					layer.msg('保存成功');
				}else{
					layer.msg('操作失败');
				}
				
			}
		});
	}
	

	
</script>

