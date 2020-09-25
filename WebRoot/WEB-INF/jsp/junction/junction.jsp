<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<base href="<%=basePath%>">

	<title>焊缝管理</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">

	<link rel="stylesheet" type="text/css" href="" />
	<link rel="stylesheet" type="text/css" href="resources/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="resources/css/datagrid.css" />
	<link rel="stylesheet" type="text/css" href="resources/themes/default/easyui.css" />
	<link rel="stylesheet" type="text/css" href="resources/css/base.css" />

	<script type="text/javascript" src="resources/js/jquery.min.js"></script>
	<script type="text/javascript" src="resources/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="resources/js/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="resources/js/welder/allWelder.js"></script>
	<script type="text/javascript" src="resources/js/easyui-extend-check.js"></script>
	<script type="text/javascript" src="resources/js/search/search.js"></script>
<%--	<script type="text/javascript" src="resources/js/welder/addWelder.js"></script>--%>
	<script type="text/javascript" src="resources/js/junction/junction.js"></script>
<%--	<script type="text/javascript" src="resources/js/welder/destroyWelder.js"></script>--%>

</head>
<body>
<div class="functiondiv">
	<div>
		<div style="float: left;">
			<label>焊缝编号：</label>
			<input class="easyui-textbox" name="fjunction_search" id="junction_search" />
		</div>
		<div style="float: left;">
			<label>焊缝名称：</label>
			<input class="easyui-textbox" name="junction_name_search" id="junction_name_search" />
		</div>
		<div  style="float: left;">
			<a href="javascript:searchJunction();" class="easyui-linkbutton" iconCls="icon-select">查找</a>&nbsp;&nbsp;&nbsp;&nbsp;
		</div>
	</div>
</div>
<div id="body"  style="height: 78%">
	<div id="importdiv" class="easyui-dialog" style="width:300px; height:200px;" closed="true">
		<form id="importfm" method="post" class="easyui-form" data-options="novalidate:true" enctype="multipart/form-data">
			<div>
				<span><input type="file" name="file" id="file"></span>
				<input type="button" value="上传" onclick="importWeldingMachine()" class="upButton"/>
			</div>
		</form>
	</div>

	<table id="junctionTable" style="table-layout: fixed; width:100%;"></table>

	<!-- 自定义多条件查询 -->
	<div id="searchdiv" class="easyui-dialog" style="width:800px; height:400px;" closed="true" buttons="#searchButton" title="自定义条件查询">
		<div id="div0">
			<select class="fields" id="fields"></select>
			<select class="condition" id="condition"></select>
			<input class="content" id="content"/>
			<select class="joint" id="joint"></select>
			<a href="javascript:newSearchWelder();" class="easyui-linkbutton" iconCls="icon-add"></a>
			<a href="javascript:removeSerach();" class="easyui-linkbutton" iconCls="icon-remove"></a>
		</div>
	</div>
	<div id="searchButton">
		<a href="javascript:searchWelder();" class="easyui-linkbutton" iconCls="icon-ok">查询</a>
		<a href="javascript:close();" class="easyui-linkbutton" iconCls="icon-cancel">取消</a>
	</div>
	<!-- 添加修改 -->
	<div id="dlg" class="easyui-dialog" style="width: 400px; height: 500px; padding:10px 20px" closed="true" buttons="#dlg-buttons">
		<form id="fm" class="easyui-form" method="post" data-options="novalidate:true"><br/>
			<div class="fitem">
				<input type="hidden" id="fid" name="fid">
				<lable><span class="required">*</span>焊缝编号</lable>
				<input name="fjunction" id="fjunction" class="easyui-textbox" data-options="required:true">
			</div>
			<div class="fitem">
				<lable><span class="required">*</span>长度</lable>
				<input name="junction_length" id="junction_length" class="easyui-textbox" data-options="required:true">
			</div>
			<div class="fitem">
				<lable><span class="required"></span>规格</lable>
				<input name="junction_format" id="junction_format" type="easyui-textbox" class="easyui-textbox">
			</div>
			<div class="fitem">
				<lable><span class="required">*</span>电流上限</lable>
				<input name="current_limit" id="current_limit" class="easyui-textbox" data-options="required:true">
			</div>
			<div class="fitem">
				<lable><span class="required">*</span>电流下限</lable>
				<input class="easyui-textbox" name="current_lower_limit" id="current_lower_limit">
			</div>
			<div class="fitem">
				<lable><span class="required">*</span>焊缝名称</lable>
				<input class="easyui-textbox" name="junction_name" id="junction_name">
			</div>
		</form>
	</div>
	<div id="dlg-buttons">
		<a href="javascript:saveJunction();" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
		<a href="javascript:close1();" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
	</div>

	<div class="functiondiv">
		<div>
			<a href="javascript:addJunction();" class="easyui-linkbutton" iconCls="icon-newadd">新增</a>&nbsp;&nbsp;&nbsp;&nbsp;
		</div>
	</div>
</div>
</body>
</html>
