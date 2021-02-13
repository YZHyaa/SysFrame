<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
basePath = basePath +"";
%>
<base href="<%=basePath%>">
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">
<meta http-equiv="Cache-Control" content="no-siteapp" />
<title>${pd.SYSNAME}</title>
 <!--[if lt IE 8]>
 <meta http-equiv="refresh" content="0;ie.html" />
 <![endif]-->
<link href="hplus/css/bootstrap.css"  rel="stylesheet">
<link href="hplus/css/plugins/bootstrap-table/bootstrap-table.css"  rel="stylesheet">
<link href="hplus/fonts/font-awesome.css" rel="stylesheet">
<link href="hplus/css/animate.css" rel="stylesheet">
<link href="hplus/css/style.css" rel="stylesheet">
<link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet">
<link href="hplus/css/frame.css" rel="stylesheet">
<link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet">
<!-- select2 -->
<link href="hplus/css/plugins/select2/select2.min.css?v=20170904" rel="stylesheet">
<!-- jquery datetimepicker -->
<link href="hplus/css/plugins/datetimepicker/jquery.datetimepicker.css?v=20170904" rel="stylesheet">
<link href="plugins/fileinput/css/fileinput.min.css?v=20170904"rel="stylesheet">
