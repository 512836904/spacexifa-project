<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>工件生产数据</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">

    <link rel="stylesheet" type="text/css" href="resources/css/base.css"/>
    <link rel="stylesheet" type="text/css" href="resources/css/datagrid.css"/>
    <link rel="stylesheet" type="text/css" href="resources/themes/icon.css"/>
    <link rel="stylesheet" type="text/css" href="resources/themes/default/easyui.css"/>

    <script type="text/javascript" src="resources/js/load.js"></script>
    <script type="text/javascript" src="resources/js/jquery.min.js"></script>
    <script type="text/javascript" src="resources/js/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="resources/js/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="resources/js/easyui-extend-check.js"></script>
    <script type="text/javascript" src="resources/js/getTimeToHours.js"></script>
    <script type="text/javascript" src="resources/js/js/echarts.js"></script>
    <script type="text/javascript" src="resources/js/js/echarts.min.js"></script>
    <script type="text/javascript" src="resources/js//datastatistics/workpiecedata.js"></script>
</head>

<body>
<div id="body">
    <div id="chartLoading" style="width:100%;height:100%;">
        <div id="chartShow" style="width:160px;" align="center"><img src="resources/images/load1.gif"/>数据加载中，请稍候...
        </div>
    </div>
    <div id="dg_btn">
        <div style="margin-bottom: 5px;">
            焊缝名称：
            <input class="easyui-textbox" name="junctionname" id="junctionname"/>
            正常/返修：
            <select class="easyui-combobox" name="type" id="ftype" data-options="editable:false">
                <option value="0">正常</option>
                <option value="1">返修</option>
            </select>
            时间：
            <input class="easyui-datetimebox" name="dtoTime1" id="dtoTime1">--
            <input class="easyui-datetimebox" name="dtoTime2" id="dtoTime2">
            <a href="javascript:serach();" class="easyui-linkbutton" iconCls="icon-select">搜索</a>
            <a href="javascript:exportExcel();" class="easyui-linkbutton" iconCls="icon-export">导出</a>
        </div>
    </div>
    <table id="dg" style="table-layout: fixed; width:100%;height: 100%;">
        <div style="float:right;width: 35%;height: 95%">
            <h2 class="h2-side"><span>工件工作时间、焊接时间（h）</span></h2>
            <div class="panel02">
                <div id="workgas" style="height:90%;width:88%;overflow:auto;padding-left:5px;float:left;background-color: #e1e1fd"></div>
            </div>
        </div>
    </table>
</div>
</body>
</html>
