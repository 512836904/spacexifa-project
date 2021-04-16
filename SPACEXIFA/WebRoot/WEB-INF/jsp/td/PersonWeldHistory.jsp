<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>焊机历史曲线-任务信息</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<link rel="stylesheet" type="text/css" href="resources/css/main.css">
	<link rel="stylesheet" type="text/css" href="resources/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="resources/css/datagrid.css" />
	<link rel="stylesheet" type="text/css" href="resources/themes/default/easyui.css" />
	<link rel="stylesheet" type="text/css" href="resources/css/base.css" />
	<link rel="stylesheet" type="text/css" href="resources/css/iconfont.css">
	
	<script type="text/javascript" src="resources/js/load.js"></script>
	<script type="text/javascript" src="resources/js/jquery.min.js"></script>
	<script type="text/javascript" src="resources/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="resources/js/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="resources/js/easyui-extend-check.js"></script>
	<script type="text/javascript" src="resources/js/highcharts.js"></script>
	<script type="text/javascript" src="resources/js/echarts.js"></script>
	<script type="text/javascript" src="resources/js/getTimeADay.js"></script>
	<script type="text/javascript" src="resources/js/exporting.js"></script>
	<script type="text/javascript" src="resources/js/map.js"></script>
<!-- 	<script type="text/javascript" src="resources/js/td/HistoryCurve.js"></script> -->
	<script type="text/javascript" src="resources/js/td/PersonWeldHistory.js"></script>

  </head>
  
<body>
	<div id="bodys" >
		 <div class="functionleftdiv">历史曲线 >> 任务信息</div>
	   	 <div id="companyOverproof_btn">
			 <a href="javascript:goback()" class="easyui-linkbutton" iconCls="icon-select" id="full" >返回上一级</a>
<%--				<input  name="parent" id="parent" type="hidden" value="${parent }"/>--%>
				<input  name="fjunction_id" id="fjunction_id" type="hidden" value="${fjunction_id }"/>
			    <input  name="type" id="type" type="hidden" value="${type }"/>
				<input  name="fid" id="fid" type="hidden" value="${fid }"/>
	            <input  name="machin_id" id="machin_id" type="hidden" value="${machin_id }"/>
				<input  style="width:0px" name="dto1" id="dto1" type="hidden" value="${dto1 }"/>
				<input  style="width:0px" name="dto2" id="dto2" type="hidden" value="${dto2 }"/>
				 <input  name="product_drawing_no" id="product_drawing_no" type="hidden" value="${product_drawing_no }"/>
				 <input  name="product_name" id="product_name" type="hidden" value="${product_name }"/>
				 <input  name="taskno" id="taskno" type="hidden" value="${taskno }"/>
				<input  name="fwelded_junction_no" id="fwelded_junction_no" type="hidden" value="${fwelded_junction_no }"/>
				 <input  name="weldername" id="weldername" type="hidden" value="${weldername }"/>
				<input  name="weldernum" id="weldernum" type="hidden" value="${weldernum }"/>
			 	<input  name="machinenum" id="machinenum" type="hidden" value="${machinenum }"/>
				 <input  name="zitem" id="zitem" type="hidden" value="${zitem }"/>
				 <input  name="bitem" id="bitem" type="hidden" value="${bitem }"/>
			     <input  name="part_number" id="part_number" type="hidden" value="${part_number }"/>
				 <input  name="part_name" id="part_name" type="hidden" value="${part_name }"/>
				 <input  name="type" id="type" type="hidden" value="${type }"/>
			     <input  name="fstatus" id="fstatus" type="hidden" value="${fstatus }"/>
			 <input class="easyui-datetimebox" style="width:0px" name="dtoTime1" id="dtoTime1" type="hidden" value="${time1 }">
			 <input class="easyui-datetimebox" style="width:0px" name="dtoTime2" id="dtoTime2" type="hidden" value="${time2 }">
<%--				<input  name="machineid" id="machineid" type="hidden" value="${machineid }"/>--%>
<%--				时间：--%>
<%--				<input class="easyui-datetimebox" name="dtoTime1" id="dtoTime1">----%>
<%--				<input class="easyui-datetimebox" name="dtoTime2" id="dtoTime2">--%>
<%--				<a href="javascript:serachCompanyOverproof();" class="easyui-linkbutton" iconCls="icon-select" >搜索</a>--%>
		</div>
		<div id="dgtb" style="width:100%;height:100%;">
			<table id="dg" style="table-layout:fixed;width:100%;"></table>
<!-- 			<a href="javascript:fullScreen()" class="easyui-linkbutton" iconCls="icon-select" id="full" >全屏显示</a> -->
		</div>
		<div id="load" style="width:100%;height:42%;"></div>
		<div id="elebody" style="position:absolute;top:60%;width:100%;height:25%;z-index:999;background:#fff;">
			<a href="javascript:fullScreen()" class="easyui-linkbutton" iconCls="icon-select" id="full">全屏显示</a>
			<a href="javascript:theSmallScreen()" class="easyui-linkbutton" iconCls="icon-select" id="little">还原</a>
			<div id="body1" style="position:absolute;top:23px;width:98%;z-index:999;"></div>
		</div>
		<div id="body2" style="position:absolute;top:82%;width:98%;height:20%;z-index:999;"></div>
		
		<div id="swdetail" class="easyui-dialog" style="width:500px; height:375px;" closed="true">
			<input name="taskno" id="taskno" type="hidden" >
			<input name="machid" id="machid" type="hidden" >
			<label>板长(cm)</label>
			<input name="boardlength" id="boardlength" style="width:70px;" class="easyui-numberbox" value="0" data-options="required:true,precision:1">
			<a href="javascript:loadChart('','');" class="easyui-linkbutton" iconCls="icon-search">任务曲线总览</a>
			<a href="javascript:exportExcel('','');" class="easyui-linkbutton" iconCls="icon-export">总览导出Excel</a>
			<table id="swdetailtable" style="table-layout:fixed;width:100%;"></table>
		</div>
	</div>
	<style type="text/css">
    #load{ display: none; position: absolute; left:0; top:60%;width: 100%; height: 40%; background-color: #555753; z-index:1001; -moz-opacity: 0.4; opacity:0.4; filter: alpha(opacity=70);}
	#show{display: none; position: absolute; top: 80%; left: 45%; width: 10%; height: 5%; padding: 8px; border: 8px solid #E8E9F7; background-color: white; z-index:1002; overflow: auto;}
	</style>
</body>
</html>
 
 