<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html style="height: 100%">
  <head>
    <base href="<%=basePath%>">
    
    <title>工作号管理</title>
<%--    <title>工艺规程管理</title>--%>
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
	
	<script type="text/javascript" src="resources/js/weldingtask/json2.js"></script>
	<script type="text/javascript" src="resources/js/jquery.min.js"></script>
	<script type="text/javascript" src="resources/js/jquery.easyui.min.js"></script>
<!-- 	<script type="text/javascript" src="resources/js/datagrid-detailview.js" charset="utf-8"></script> -->
	<script type="text/javascript" src="resources/js/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="resources/js/easyui-extend-check.js"></script>
	<script type="text/javascript" src="resources/js/search/search.js"></script>
	<script type="text/javascript" src="resources/js/wpslib/allWps.js"></script>
 	<script type="text/javascript" src="resources/js/wpslib/addeditWps.js"></script>
	<script type="text/javascript" src="resources/js/wpslib/removeWps.js"></script>
<%--	<script type="text/javascript" src="resources/js/junction/junction.js"></script>--%>
<!--	<script type="text/javascript" src="resources/js/wpslib/giveWpslib.js"></script>
	<script type="text/javascript" src="resources/js/wpslib/differentMachine.js"></script>
	<script type="text/javascript" src="resources/js/wpslib/control.js"></script>
	<script type="text/javascript" src="resources/js/wpslib/comboboxCheck.js"></script> -->
	<script type="text/javascript" src="resources/js/getTimeToHours.js"></script>
	<script type="text/javascript" src="resources/js/swfobject.js"></script>
	<script type="text/javascript" src="resources/js/web_socket.js"></script>

	<style type="text/css">
		table tr td{
			font-size:12px;
		}
		.leftTd{
			text-align: right;
		}
		.textbox-text{
			width:85px;
		}
	</style>
  </head>
  
  <body style="height: 100%">
  	  	<div class="functiondiv">
			<div>
				<div style="float: left;">
					<label>工作号：</label>
					<input class="easyui-textbox" id="job_number_search" />
				</div>
				<div  style="float: left;">
					<label>部套号：</label>
					<input class="easyui-textbox" id="set_number_search" />
				</div>
				<div  style="float: left;">
					<label>零件图号：</label>
					<input class="easyui-textbox" id="part_drawing_search" />
				</div>
				<div>
					<label>零件名：</label>
					<input class="easyui-textbox" id="part_name_search" />
				</div>
				<div>
					<a href="javascript:searchWps();" class="easyui-linkbutton" iconCls="icon-select">查找</a>&nbsp;&nbsp;&nbsp;&nbsp;
					<a href="javascript:allshow();" class="easyui-linkbutton" iconCls="icon-redo">工作号一键展示</a>&nbsp;&nbsp;&nbsp;&nbsp;
					<a href="javascript:allcancel();" class="easyui-linkbutton" iconCls="icon-undo">工作号一键取消展示</a>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
			</div>
		</div>
  	<div id="body" style="height: 78%">
  		<!-- 工艺台账列表 -->
  		<div id="wpsTableDiv" style="height: auto;">
	   		<table id="wpslibTable" style="table-layout: fixed; width:100%;"></table>
  		</div>

		<!-- 焊缝信息列表 -->
		<div id="dialogDiv" class="easyui-dialog" style="width: 1150px; height: 660px;" closed="true" buttons="#tdd-buttons">
			<div class="functiondiv">
				<div>
					<div style="float: left;">
						<label>焊缝编号：</label>
						<input class="easyui-textbox" name="fjunction_search" id="junction_search"/>
					</div>
					<div style="float: left;">
						<label>焊缝名称：</label>
						<input class="easyui-textbox" name="junction_name_search" id="junction_name_search"/>
					</div>
					<div style="float: left;">
						<a href="javascript:searchJunction();" class="easyui-linkbutton" iconCls="icon-select">查找</a>&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
				</div>
			</div>
			<table id="junctionTable" style="width:100%;height: 480px"></table>
			<div align="center">
				<a href="javascript:determine();" style="width: 13%;height: 40px" class="easyui-linkbutton" iconCls="icon-ok">确定</a>
			</div>
		</div>

	     <!-- 添加修改工艺台账 -->
<%--		<div id="addOrUpdate" class="easyui-dialog" style="width: 400px; height: 500px; padding:10px 20px" closed="true" buttons="#tdd-buttons">--%>
<%--			<form id="addOrUpdatefm" class="easyui-form" method="post" data-options="novalidate:true">--%>
<%--				<div style="width: 100%">--%>
<%--					<div class="fitem">--%>
<%--						<input id="fid" name="fid" type="hidden">--%>
<%--						<lable><span class="required">*</span>工作号</lable>--%>
<%--						<input class="easyui-textbox" name="JOB_NUMBER" id="JOB_NUMBER"  data-options="required:true"/>--%>
<%--					</div>--%>
<%--					<div class="fitem">--%>
<%--						<lable><span class="required">*</span>部套号</lable>--%>
<%--						<input class="easyui-textbox" name="SET_NUMBER" id="SET_NUMBER"  data-options="required:true"/>--%>
<%--					</div>--%>
<%--					<div class="fitem">--%>
<%--						<lable><span class="required">*</span>零级图号</lable>--%>
<%--						<input class="easyui-textbox" name="PART_DRAWING_NUMBER" id="PART_DRAWING_NUMBER"  data-options="required:true"/>--%>
<%--					</div>--%>
<%--					<div class="fitem">--%>
<%--						<lable><span class="required">*</span>零件名</lable>--%>
<%--						<input class="easyui-textbox" name="PART_NAME" id="PART_NAME"  data-options="required:true"/>--%>
<%--					</div>--%>
<%--					<div class="fitem">--%>
<%--						<lable><a href="javascript:junctionButton();" class="easyui-linkbutton">焊缝查找带回</a></lable>--%>
<%--						<input class="easyui-textbox" name="junctionName" id="junctionName" readonly/>--%>
<%--						<input type="hidden" id="fids" name="fids">--%>
<%--					</div>--%>
<%--					<div class="fitem">--%>
<%--						<lable><span class="required">*</span>工票编号</lable>--%>
<%--						<input class="easyui-textbox" name="workticket_number" id="workticket_number"  data-options="required:true"/>--%>
<%--					</div>--%>
<%--					<div class="fitem">--%>
<%--						<lable><span class="required">*</span>工艺参数</lable>--%>
<%--						<input class="easyui-textbox" name="raw_material" id="raw_material"  data-options="required:true"/>--%>
<%--					</div>--%>
<%--					<div class="fitem">--%>
<%--						<lable><span class="required">*</span>原料</lable>--%>
<%--						<input class="easyui-textbox" name="craft_param" id="craft_param"  data-options="required:true"/>--%>
<%--					</div>--%>
<%--					<div class="fitem">--%>
<%--						<lable><span class="required">*</span>工序</lable>--%>
<%--						<input class="easyui-textbox" name="process" id="process"  data-options="required:true"/>--%>
<%--					</div>--%>
<%--					<div align="center">--%>
<%--						<a href="javascript:saveWps();" class="easyui-linkbutton" iconCls="icon-ok">保存</a>--%>
<%--						<!-- <a href="javascript:closeDialog('wltdlg');" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a> -->--%>
<%--					</div>--%>
<%--				</div>--%>
<%--			</form>--%>
<%--		</div>--%>

		<!-- 新增电子跟踪卡 -->
		<div id="addTarckingCard" class="easyui-dialog" style="width: 1100px; height: 600px; padding:10px 20px" closed="true" buttons="#tdd-buttons">
			<form id="addTarckingCardfm" class="easyui-form" method="post" data-options="novalidate:true">
				<div style="width: 100%">
					<div class="fitem">
						<input id="fid" name="fid" type="hidden">
						<input id="productionCraftId" name="productionCraftId" type="hidden">
						<lable><span class="required">*</span>工作号：</lable>
						<input class="easyui-textbox" name="JOB_NUMBER" id="JOB_NUMBER"  data-options="required:true"/>
						<lable><span class="required">*</span>部套号：</lable>
						<input class="easyui-textbox" name="SET_NUMBER" id="SET_NUMBER"  data-options="required:true"/>
						<lable><span class="required">*</span>工票编号：</lable>
						<input class="easyui-textbox" name="workticket_number" id="workticket_number"  data-options="required:true"/>
					</div>
					<div class="fitem">
						<lable><span class="required">*</span>零级图号：</lable>
						<input class="easyui-textbox" name="PART_DRAWING_NUMBER" id="PART_DRAWING_NUMBER"  data-options="required:true"/>
						<lable><span class="required">*</span>零件名：</lable>
						<input class="easyui-textbox" name="PART_NAME" id="PART_NAME"  data-options="required:true"/>
						<lable><span class="required">*</span>工艺参数：</lable>
						<input class="easyui-textbox" name="craft_param" id="craft_param"  data-options="required:true"/>

					</div>
<%--					<div class="fitem">--%>
<%--						<lable><span class="required">*</span>原料：</lable>--%>
<%--						<input class="easyui-textbox" name="raw_materi" id="raw_materi"  data-options="required:true"/>--%>
<%--						<lable><span class="required">*</span>工序：</lable>--%>
<%--						<input class="easyui-textbox" name="process" id="process"  data-options="required:true"/>--%>
<%--						<lable><a href="javascript:junctionButton();" class="easyui-linkbutton">焊缝查找带回</a></lable>--%>
<%--						<input class="easyui-textbox" id="junctionName" name="junctionName" readonly data-options="required:true"/>--%>
<%--						<input type="hidden" id="junctionId" name="junctionId">--%>
<%--					</div>--%>
				</div>
			</form>
			<table id="productionTable" style="table-layout: fixed; width:100%;height: 450px"></table>
		</div>

	    <div class="functiondiv">
			<div>
				<a href="javascript:addWpsTrackCard();" class="easyui-linkbutton" iconCls="icon-newadd">新增电子跟踪卡</a>&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="javascript:editWpsTrackCard();" class="easyui-linkbutton" iconCls="icon-update">修改电子跟踪卡</a>&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="javascript:deleteWpsTrackCard();" class="easyui-linkbutton" iconCls="icon-delete">删除电子跟踪卡</a>
<%--				<a href="javascript:wpsDetails();" class="easyui-linkbutton" iconCls="icon-navigation">工艺规程详情</a>&nbsp;&nbsp;&nbsp;&nbsp;--%>
<%--				<a href="javascript:addVersion();" class="easyui-linkbutton" iconCls="icon-navigation">新建版本</a>&nbsp;&nbsp;&nbsp;&nbsp;--%>
<%--				<a href="javascript:importclick();" class="easyui-linkbutton" iconCls="icon-newadd"> 工艺导入</a>&nbsp;&nbsp;&nbsp;&nbsp;--%>
<%--				<a href="javascript:exportclick();" class="easyui-linkbutton" iconCls="icon-newadd"> 工艺导出</a>&nbsp;&nbsp;&nbsp;&nbsp;--%>
			</div>
		</div>
		<div id="importdiv" class="easyui-dialog" style="width:300px; height:200px;" closed="true">
			<form id="importfm" method="post" class="easyui-form" data-options="novalidate:true" enctype="multipart/form-data"> 
				<div>
					<span><input type="file" name="file" id="file"></span>
					<input type="button" value="上传" onclick="importWpsExcel()" class="upButton"/>
				</div>
			</form>
		</div>
		<div id="load" style="width:100%;height:100%;"></div>
	</div>
	<style type="text/css">
	    #load{ display: none; position: absolute; left:0; top:0;width: 100%; height: 40%; background-color: #555753; z-index:10001; -moz-opacity: 0.4; opacity:0.5; filter: alpha(opacity=70);}
		#show{display: none; position: absolute; top: 45%; left: 45%; width: 180px; height: 5%; padding: 8px; border: 8px solid #E8E9F7; background-color: white; z-index:10002; overflow: auto;}
	</style>
  </body>
</html>