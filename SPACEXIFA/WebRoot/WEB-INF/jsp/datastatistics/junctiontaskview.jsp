<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>生产任务明细</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<link rel="stylesheet" type="text/css" href="resources/css/base.css" />
	<link rel="stylesheet" type="text/css" href="resources/css/datagrid.css" />
	<link rel="stylesheet" type="text/css" href="resources/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="resources/themes/default/easyui.css" />

<%--	<script type="text/javascript" src="resources/js/weldingtask/json2.js"></script>--%>
	<script type="text/javascript" src="resources/js/echarts.js"></script>
	<!-- <script type="text/javascript" src="resources/js/load.js"></script> -->
	<script type="text/javascript" src="resources/js/jquery.min.js"></script>
	<script type="text/javascript" src="resources/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="resources/js/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="resources/js/easyui-extend-check.js"></script>
	<script type="text/javascript" src="resources/js/getTimeToHours.js"></script>
	<script type="text/javascript" src="resources/js//datastatistics/junctiontaskview.js"></script>
  </head>
  
  <body>
    	<div id="body" style="height: 46%">
	    	<div class="functiondiv">
				<div>
					<div style="float: left;">
							<label>工作号：</label>
		<!-- 				</div>
						<div> -->
							<input class="easyui-textbox" name="job_number_search" id="job_number_search" />&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
					<div style="float: left;">
							<label>部套号：</label>
		<!-- 				</div>
						<div> -->
							<input class="easyui-textbox" name="set_number_search" id="set_number_search" style="width: 90px"/>&nbsp;&nbsp;&nbsp;&nbsp;
<%--							<input class="easyui-textbox" name="fproduct_id" id="fproduct_id" style="width: 45px"/> ----%>
<%--							<input class="easyui-textbox" name="fsuffix_number" id="fsuffix_number" style="width: 90px"/>--%>
					</div>
					<div style="float: left;">
							<label>零件图号：</label>
		<!-- 				</div>
						<div> -->
							<input class="easyui-textbox" name="part_drawing_search" id="part_drawing_search" />&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
					<div style="float: left;">
							<label>零件名：</label>
		<!-- 				</div>
						<div> -->
							<input class="easyui-textbox" name="part_name_search" id="part_name_search" />&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
					<div>
							<label>焊缝名称：</label>
		<!-- 				</div>
						<div> -->
							<input class="easyui-textbox" name="fjunction_id" id="fjunction_id" />&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
				</div>
				<div>
<%--					<div style="float: left;">--%>
<%--							<label>&nbsp;&nbsp;&nbsp;&nbsp;工&nbsp;&nbsp;&nbsp;&nbsp;步&nbsp;&nbsp;&nbsp;&nbsp;号&nbsp;：</label>--%>
<%--							<input class="easyui-textbox" name="fstep_id" id="fstep_id" />--%>
<%--					</div>--%>
					<div style="float: left;">
							<label>正常/返修：</label>&nbsp;&nbsp;&nbsp;&nbsp;
		<!-- 				</div>
						<div> -->
						<select class="easyui-combobox" name="ftype" id="ftype" data-options="editable:false"></select>
					</div>
					<div style="float: left;">
							<label>时间：</label>
		<!-- 				</div>
						<div> -->
							<input class="easyui-datetimebox" name="dtoTime1" id="dtoTime1"> -- 
							<input class="easyui-datetimebox" name="dtoTime2" id="dtoTime2">
					</div>
					<div style="float: left;">
						<a href="javascript:searchHistory();" class="easyui-linkbutton" iconCls="icon-select">查找</a>&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
				</div>
			</div>
<%--			<div id="load" style="width:100%;height:42%;"></div>--%>
<%--			<div id="elebody" style="position:absolute;top:60%;width:100%;height:25%;z-index:999;background:#fff;">--%>
<%--				<a href="javascript:fullScreen()" class="easyui-linkbutton" iconCls="icon-select" id="full">全屏显示</a>--%>
<%--				<a href="javascript:theSmallScreen()" class="easyui-linkbutton" iconCls="icon-select" id="little">还原</a>--%>
<%--				<div id="body1" style="position:absolute;top:23px;width:100%;z-index:999;"></div>--%>
<%--			</div>--%>
			<div id="body2" style="position:absolute;top:82%;width:100%;height:20%;z-index:999;"></div>
			<div id="tableDiv" style="height: 100%">
				<div id="chartLoading" style="width:100%;height:100%;">
					<div id="chartShow" style="width:160px;" align="center"><img src="resources/images/load1.gif"/>数据加载中，请稍候...</div>
				</div>
			    <table id="dg" style="table-layout: fixed; width:100%;"></table>
			</div>
    	</div>
<%--	    <div  id="historyCurveDiv" style="height: 46%;overflow: auto;"></div>--%>
<%--		<style type="text/css">--%>
<%--			#load{ display: none; position: absolute; left:0; top:60%;width: 100%; height: 40%; background-color: #555753; z-index:1001; -moz-opacity: 0.4; opacity:0.4; filter: alpha(opacity=70);}--%>
<%--			#show{display: none; position: absolute; top: 80%; left: 45%; width: 10%; height: 5%; padding: 8px; border: 8px solid #E8E9F7; background-color: white; z-index:1002; overflow: auto;}--%>
<%--		</style>--%>
<%--  </body>--%>
</html>
