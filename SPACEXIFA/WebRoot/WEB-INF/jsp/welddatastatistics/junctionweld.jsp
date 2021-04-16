<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html style="height: 100%">
<head>
	<base href="<%=basePath%>">

	<title>工件焊接数据</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">

	<link rel="stylesheet" type="text/css" href="resources/css/base.css" />
	<link rel="stylesheet" type="text/css" href="resources/css/datagrid.css" />
	<link rel="stylesheet" type="text/css" href="resources/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="resources/themes/default/easyui.css" />

	<!-- <script type="text/javascript" src="resources/js/load.js"></script> -->
	<script type="text/javascript" src="resources/js/jquery.min.js"></script>
	<script type="text/javascript" src="resources/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="resources/js/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="resources/js/easyui-extend-check.js"></script>
	<script type="text/javascript" src="resources/js/getTimeToHours.js"></script>
	<script type="text/javascript" src="resources/js//welddatastatistics/junctionweld.js"></script>
	<style type="text/css">
		.textbox-text {
			width:100px;
		}
	</style>
</head>

<body style="height: 100%">
<div id="body">
	<div id="chartLoading" style="width:100%;height:100%;">
		<div id="chartShow" style="width:160px;" align="center"><img src="resources/images/load1.gif"/>数据加载中，请稍候...</div>
	</div>
	<div class="functiondiv">
		<div>
			<div  style="float: left;">
				<label>工作号：</label>
				<input class="easyui-textbox" style="width:120px;" name="product_drawing_no" id="product_drawing_no" value="${product_drawing_no}"/>
			</div>
			<div  style="float: left;">
				<label>部套号：</label>
				<input class="easyui-textbox" style="width:120px;" name="product_name" id="product_name" value="${product_name}"/>
			</div>
			<div  style="float: left;">
				<label>零件图号：</label>
				<input class="easyui-textbox" style="width:120px;" name="part_number" id="part_number" value="${part_number}"/>
			</div>
			<div  style="float: left;">
				<label>零件名：</label>
				<input class="easyui-textbox" style="width:120px;" name="part_name" id="part_name" value="${part_name}"/>
			</div>
			<div  style="float: left;">
				<label>工艺编号：</label>
				<input class="easyui-textbox" style="width:120px;" name="taskno" id="taskno" value="${taskno}"/>
			</div>
			<div>
				<label>焊缝名称：</label>
				<input class="easyui-textbox" style="width:120px;" name="fwelded_junction_no" id="fwelded_junction_no" value="${fwelded_junction_no}"/>
			</div>
		</div>
		<div>
			<div style="float: left;">
				<label>正常/返修：</label>
				<select class="easyui-combobox" name="type" id="ftype" data-options="editable:false">
					<option value="0">正常</option>
					<option value="1">返修</option>
				</select>
			</div>
			<div style="float: left;">
				<label>焊工姓名：</label>
				<input class="easyui-textbox" style="width:120px;" name="weldername" id="weldername" value="${weldername}"/>
			</div>
			<div style="float: left;">
				<label>时间：</label>
				<input class="easyui-datetimebox" style="width:150px;" name="dtoTime1" id="dtoTime1">--
				<input class="easyui-datetimebox" style="width:150px;" name="dtoTime2" id="dtoTime2">&emsp;
			</div>
			<input  name="Time1" id="Time1" type="hidden" value="${Time1}">
			<input  name="Time2" id="Time2" type="hidden" value="${Time2}">
			<input  name="fstatus" id="fstatus" type="hidden" value="${fstatus}">
			<div  style="float: left;">
				<a href="javascript:serach();" class="easyui-linkbutton" iconCls="icon-select" >搜索</a>&emsp;
				<%--					<a href="javascript:exportExcel();" class="easyui-linkbutton" iconCls="icon-export">导出</a>--%>
			</div>
		</div>
	</div>
	<div id="wpsTableDiv" style="height:90%;">
		<table id="taskviewtable" style="table-layout: fixed; width:100%;"></table>
	</div>
</div>
</body>
</html>
