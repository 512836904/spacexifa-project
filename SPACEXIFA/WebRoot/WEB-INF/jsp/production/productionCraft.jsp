<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">
    <title>生产工艺管理</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <link rel="stylesheet" type="text/css" href=""/>
    <link rel="stylesheet" type="text/css" href="resources/themes/icon.css"/>
    <link rel="stylesheet" type="text/css" href="resources/css/datagrid.css"/>
    <link rel="stylesheet" type="text/css" href="resources/themes/default/easyui.css"/>
    <link rel="stylesheet" type="text/css" href="resources/css/base.css"/>
    <script type="text/javascript" src="resources/js/jquery.min.js"></script>
    <script type="text/javascript" src="resources/js/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="resources/js/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="resources/js/easyui-extend-check.js"></script>
    <script type="text/javascript" src="resources/js/search/search.js"></script>
    <script type="text/javascript" src="resources/js/production/productionCraft.js"></script>
</head>
<body>
<div class="functiondiv">
    <div>
        <a href="javascript:addProduction();" class="easyui-linkbutton" iconCls="icon-newadd">新增</a>&nbsp;&nbsp;&nbsp;&nbsp;
    </div>
</div>
<div class="functiondiv">
    <div>
        <div style="float: left;">
            <label>工艺名：</label>
            <input class="easyui-textbox" name="name_search" id="name_search" data-options="prompt:'前缀关键词检索'"/>
        </div>
        <div style="float: left;">
            <a href="javascript:searchProduction();" class="easyui-linkbutton" iconCls="icon-select">查找</a>&nbsp;&nbsp;&nbsp;&nbsp;
        </div>
    </div>
</div>
<div id="body" style="width:1670px;height: 80%">
    <table id="productionTable" style="table-layout: fixed; width:100%;height: 600px"></table>
    <!-- 添加修改 -->
    <div id="dlg" class="easyui-dialog" style="width: 600px; height: 550px; padding:10px 20px" closed="true" buttons="#dlg-buttons">
        <form id="fm" class="easyui-form" method="post" data-options="novalidate:true"><br/>
            <div class="fitem">
                <input type="hidden" id="FID" name="FID">
                <lable><span class="required">*</span>工艺名：</lable>
                <input name="FNAME" id="FNAME" class="easyui-textbox" data-options="required:true">
            </div>
            <div class="fitem">
                <lable><span class="required">*</span>焊缝名称：</lable>
                <input name="FJUNCTION" id="FJUNCTION" type="hidden">
                <input name="JUNCTION_NAME" id="JUNCTION_NAME" title="焊缝名不可编辑" readonly="readonly" class="easyui-textbox">
            </div>
            <div class="fitem">
                <lable><span class="required">*</span>预热：</lable>
                <input name="PREHEAT" id="PREHEAT" class="easyui-textbox" data-options="required:true">
                <label>℃</label>
            </div>
            <div class="fitem">
                <lable><span class="required">*</span>层间：</lable>
                <input name="INTERLAMINATION" id="INTERLAMINATION" class="easyui-textbox" data-options="required:true">
                <label>℃</label>
            </div>
            <div class="fitem">
                <lable><span class="required">*</span>焊材：φ</lable>
                <input name="WELDING_MATERIAL" id="WELDING_MATERIAL" class="easyui-textbox" data-options="required:true">
                <label>mm</label>
            </div>
            <div class="fitem">
                <lable><span class="required">*</span>焊丝直径：</lable>
                <input name="WIRE_DIAMETER" id="WIRE_DIAMETER" class="easyui-textbox" data-options="required:true">
                <label>mm</label>
            </div>
            <div class="fitem">
                <lable>电流下限上限：</lable>
                <input name="ELECTRICITY_FLOOR" id="ELECTRICITY_FLOOR" class="easyui-textbox">
                <label>--</label>
                <input name="ELECTRICITY_UPPER" id="ELECTRICITY_UPPER" class="easyui-textbox">
                <label>A</label>
            </div>
            <div class="fitem">
                <lable>电压下限上限：</lable>
                <input name="VOLTAGE_FLOOR" id="VOLTAGE_FLOOR" class="easyui-textbox">
                <label>--</label>
                <input name="VOLTAGE_UPPER" id="VOLTAGE_UPPER" class="easyui-textbox">
                <label>V</label>
            </div>
            <div class="fitem">
                <lable>焊速下限上限：</lable>
                <input name="SOLDER_SPEED_FLOOR" id="SOLDER_SPEED_FLOOR" class="easyui-textbox">
                <label>--</label>
                <input name="SOLDER_SPEED_UPPER" id="SOLDER_SPEED_UPPER" class="easyui-textbox">
                <label>mm/min</label>
            </div>
            <div class="fitem">
                <lable><span class="required">*</span>摆宽：</lable>
                <input name="WIDE_SWING" id="WIDE_SWING" class="easyui-textbox" data-options="required:true">
                <label>mm</label>
            </div>
            <div class="fitem">
                <lable>其他：</lable>
                <input name="RESTS" id="RESTS" class="easyui-textbox">
            </div>
        </form>
    </div>
    <div id="dlg-buttons">
        <a href="javascript:saveProduction();" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
        <a href="javascript:close1();" class="easyui-linkbutton" iconCls="icon-cancel">取消</a>
    </div>
</div>
</body>
</html>
