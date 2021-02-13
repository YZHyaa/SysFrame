<script src="hplus/js/jquery-2.1.1.js" ></script>
<script src="hplus/js/jquery.cookie.js" ></script>
<script src="hplus/js/bootstrap.min.js" ></script>
<script src="hplus/js/plugins/bootstrap-table/bootstrap-table_pegasus.js" ></script>
<script src="hplus/js/plugins/bootstrap-table/locale/bootstrap-table-zh-CN.js"></script>
<script src="hplus/js/plugins/bootstrap-table/extensions/mobile/bootstrap-table-mobile.js"></script>
<script src="hplus/js/plugins/bootstrap-table/extensions/export/tableExport.js"></script>
<script src="hplus/js/plugins/layer/layer.min.js"></script>
<script src="hplus/js/plugins/validate/jquery.validate.min.js"></script>
<script src="hplus/js/plugins/validate/messages_zh.min.js"></script>
<script src="hplus/js/plugins/jqtips/jquery.tips.js"></script>
<script src="hplus/js/plugins/webuploader/webuploader.js"></script>
<script src="plugins/utils/createOption.js"></script>
<script src="hplus/js/plugins/metisMenu/jquery.metisMenu.js"></script>
<script src="hplus/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
<script src="hplus/js/inspinia.js"></script>
<script src="hplus/js/plugins/pace/pace.min.js"></script>
<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
<!--jquery datetimepicker -->
<script src="hplus/js/plugins/datetimepicker/jquery.datetimepicker.full.js"></script>
<script src="plugins/fileinput/js/plugins/canvas-to-blob.min.js"></script>
<script src="plugins/fileinput/js/plugins/sortable.min.js"></script>
<script src="plugins/fileinput/js/plugins/purify.min.js"></script>
<script src="plugins/fileinput/js/fileinput.js"></script>
<script src="plugins/fileinput/js/locales/zh.js"></script>
<script src="plugins/fileinput/js/themes/fa/theme.js"></script>
<script type="text/javascript">
	var loadingCssHtml= 
		  "<div class=\"sk-spinner sk-spinner-fading-circle\">"
	    + "<div class=\"sk-circle1 sk-circle\"></div>"
	    + "<div class=\"sk-circle2 sk-circle\"></div>"
	    + "<div class=\"sk-circle3 sk-circle\"></div>"
	    + "<div class=\"sk-circle4 sk-circle\"></div>"
	    + "<div class=\"sk-circle5 sk-circle\"></div>"
	    + "<div class=\"sk-circle6 sk-circle\"></div>"
	    + "<div class=\"sk-circle7 sk-circle\"></div>"
	    + "<div class=\"sk-circle8 sk-circle\"></div>"
	    + "<div class=\"sk-circle9 sk-circle\"></div>"
	    + "<div class=\"sk-circle10 sk-circle\"></div>"
	    + "<div class=\"sk-circle11 sk-circle\"></div>"
	    + "<div class=\"sk-circle12 sk-circle\"></div>"
	    + "</div>";
	
	$(document).ready(function () {
		var msg = '${msg}';
		if(msg!=''){
			if(msg=='ok'||msg=='success'){
				layer.msg('\u64cd\u4f5c\u6210\u529f');
			}else{
				layer.msg('\u64cd\u4f5c\u5931\u8d25');
			}
		}
		
		$.datetimepicker.setLocale('zh');
		
		$('.datetimepicker').datetimepicker({
			format:'Y-m-d H:i',
			dayOfWeekStart : 1,
			step:5
		});
		
		$('.datepicker').datetimepicker({
			timepicker:false,
			format:'Y-m-d',
			dayOfWeekStart : 1,
			step:5
		});
		
		$('.timepicker').datetimepicker({
			datepicker:false,
			format:'H:i',
			dayOfWeekStart : 1,
			step:5
		});
	});
	
	function showProgress(progress){
		$(".hplusProgress").show();
		var progressHtml = $(".hplusProgress").html();
		if(progress>100){
			progress=100;
		}
		if(progress<0){
			progress=0;
		}
		if(progressHtml==''){
			var html = "<div class=\"background\"></div>"
			         + "<div class=\"progress progress-striped active progressRow\">"
			         + "<div id=\"progressBar\" class=\"progress-bar progress-bar-success\" style=\"width:"+progress+"%\"></div>"
			         + "</div>";
			$(".hplusProgress").html(html)         
		}else{
			$("#progressBar").css("width",progress+"%");
		}
	}
	
	function closeProgress(){
		$(".hplusProgress").hide();
		$(".hplusProgress").html('')
	}
	
</script>