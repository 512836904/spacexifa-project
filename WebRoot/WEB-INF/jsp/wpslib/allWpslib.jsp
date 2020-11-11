<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>焊机工艺管理</title>
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

    <script type="text/javascript" src="resources/js/weldingtask/json2.js"></script>
    <script type="text/javascript" src="resources/js/jquery.min.js"></script>
    <script type="text/javascript" src="resources/js/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="resources/js/datagrid-detailview.js" charset="utf-8"></script>
    <script type="text/javascript" src="resources/js/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="resources/js/easyui-extend-check.js"></script>
    <script type="text/javascript" src="resources/js/search/search.js"></script>

    <script type="text/javascript" src="resources/js/wpslib/allWpslib.js"></script>
    <script type="text/javascript" src="resources/js/wpslib/addeditWpslib.js"></script>
    <script type="text/javascript" src="resources/js/wpslib/removeWpslib.js"></script>
    <script type="text/javascript" src="resources/js/wpslib/giveWpslib.js"></script>
    <script type="text/javascript" src="resources/js/wpslib/differentMachine.js"></script>
    <script type="text/javascript" src="resources/js/wpslib/control.js"></script>
    <script type="text/javascript" src="resources/js/wpslib/comboboxCheck.js"></script>
    <script type="text/javascript" src="resources/js/getTimeToHours.js"></script>
    <script type="text/javascript" src="resources/js/swfobject.js"></script>
    <script type="text/javascript" src="resources/js/web_socket.js"></script>

    <script type="text/javascript" src="resources/js/wpslib/craft/WB-P500L.js"></script>
    <script type="text/javascript" src="resources/js/paho-mqtt.js"></script>
    <script type="text/javascript" src="resources/js/paho-mqtt-min.js"></script>
    <script type="text/javascript" src="resources/js/first.js"></script>
    <style type="text/css">
        table tr td {
            font-size: 14px;
            height: 30px;
        }

        .leftTd {
            text-align: right;
            width: 160px;
        }

        .rightTd {
            text-align: left;
            width: 200px;
        }
    </style>
    <script type="text/javascript" src="resources/js/datagrid-filter.js"></script>
    <%--    <script type="text/javascript" src="resources/js/paho-mqtt.js"></script>--%>
    <%--    <script type="text/javascript" src="resources/js/paho-mqtt-min.js"></script>--%>
    <%--    <script type="text/javascript" src="resources/js/specification/MqttConnect.js"></script>--%>
    <%--    <script type="text/javascript" src="resources/js/specification/addSpe.js"></script>--%>
</head>

<body>
<div class="functiondiv">
    <div>
        <a href="javascript:addWpslib();" class="easyui-linkbutton" iconCls="icon-newadd">新增工艺库</a>&nbsp;&nbsp;&nbsp;&nbsp;
        <a href="javascript:openCondlg();" class="easyui-linkbutton" iconCls="icon-newadd">控制命令下发</a>&nbsp;&nbsp;&nbsp;&nbsp;
        <!--<a href="javascript:openHistorydlg();" class="easyui-linkbutton" iconCls="icon-newadd"> 下发历史查询</a>&nbsp;&nbsp;&nbsp;&nbsp; -->
        <%--<a href="javascript:importclick();" class="easyui-linkbutton" iconCls="icon-newadd"> 工艺导入</a>--%>&nbsp;&nbsp;&nbsp;&nbsp;
    </div>
</div>
<div id="body">
    <table id="wpslibTable" style="table-layout: fixed; width:100%;"></table>

    <!-- 添加修改工艺库 -->
    <div id="wltdlg" class="easyui-dialog" style="width: 400px; height: 225px; padding:10px 20px" closed="true"
         buttons="#wltdlg-buttons">
        <form id="wltfm" class="easyui-form" method="post" data-options="novalidate:true">
            <div class="fitem">
                <lable><span class="required">*</span>工艺库名称</lable>
                <input type="hidden" id="validwl">
                <input class="easyui-textbox" name="wpslibName" id="wpslibName"
                       data-options="validType:['wpslibValidate'],required:true"/>
            </div>
            <div class="fitem">
                <lable><span class="required">*</span>焊机型号</lable>
                <select class="easyui-combobox" name="model" id="model"
                        data-options="required:true,editable:false"></select>
            </div>
            <div class="fitem">
                <lable>状态</lable>
                <span id="radios"></span>
            </div>
        </form>
    </div>
    <div id="wltdlg-buttons">
        <a href="javascript:saveWpslib();" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
        <a href="javascript:closeDialog('wltdlg');" class="easyui-linkbutton" iconCls="icon-cancel">取消</a>
    </div>

    <!-- 添加修改工艺 -->
    <div id="wpsCraft" class="easyui-dialog" style="width: 900px; height: 600px; padding:10px 20px" closed="true" data-options="iconCls:'icon-navigation',modal:true">
        <form id="fmwpsCraft" class="easyui-form" method="post" data-options="novalidate:true">
            <input type="hidden" id="modelname" name="modelname">
            <input type="hidden" id="fid" name="fid">
            <input type="hidden" id="addORupdate" name="addORupdate">
            <input type="hidden" id="specification_id" name="specification_id">
            <div region="left">
                <table>
                    <tr>
                        <td class="leftTd">
                            <lable>通道号：</lable>
                        </td>
                        <td class="rightTd">
                            <select class="easyui-combobox" name="fchanel" id="fchanel" data-options="editable:false"></select>
                        </td>
                        <td id="tcontroller" class="leftTd">
                            <lable>熔深控制：</lable>
                        </td>
                        <td id="rcontroller" class="rightTd">
                            <input name="fcontroller" id="fcontroller" type="checkbox" value="1"/>
                        </td>
                    </tr>
                </table>
            </div>
            <div>
                <table>
                    <tr>
                        <td id="dfweldprocess" class="leftTd">
                            <lable><span class="required">*</span>焊接过程：</lable>
                        </td>
                        <td id="rfweldprocess" class="rightTd">
                            <select class="easyui-combobox" name="fweldprocess" id="fweldprocess" data-options="editable:false"></select>
                        </td>
                        <td id="dtorch" class="leftTd">
                            <lable>水冷焊枪：</lable>
                        </td>
                        <td id="itorch" class="rightTd"><input name="ftorch" id="ftorch" type="checkbox" value="0"></td>
                    </tr>
                </table>
            </div>
            <div>
                <table>
                    <tr>
                        <td id="dgas" class="leftTd">
                            <lable><span class="required">*</span>气体：</lable>
                        </td>
                        <td id="rgas" class="rightTd">
                            <select class="easyui-combobox" name="fgas" id="fgas" data-options="editable:false">
                                <option value="121">CO2</option>
                                <option value="122">MAG</option>
                                <option value="123">MIG</option>
                            </select>
                        </td>
                        <td id="dmaterial" class="leftTd">
                            <lable><span class="required">*</span>焊丝材质：</lable>
                        </td>
                        <td id="rmaterial" class="rightTd">
                            <select class="easyui-combobox" name="fmaterial" id="fmaterial" data-options="editable:false">
                                <option value="91">低碳钢实心</option>
                                <option value="92">不锈钢实心</option>
                                <option value="93">低碳钢药芯</option>
                                <option value="94">不锈钢药芯</option>
                            </select>
                        </td>
                    </tr>
                </table>
            </div>
            <div id="hide1">
                <table>
                    <tr>
                        <td class="leftTd">
                            <lable><span class="required">*</span>焊丝直径：</lable>
                        </td>
                        <td class="rightTd">
                            <select class="easyui-combobox" name="fdiameter" id="fdiameter" data-options="editable:false">
                                <option value="131">Φ1.0</option>
                                <option value="132">Φ1.2</option>
                                <option value="133">Φ1.4</option>
                                <option value="134">Φ1.6</option>
                            </select>
                        </td>
                        <td class="leftTd">
                            <lable><span class="required">*</span>一元/个别：</lable>
                        </td>
                        <td class="rightTd">
                            <select class="easyui-combobox" name="fselect" id="fselect" data-options="editable:false">
                                <option value="102">个别</option>
                                <option value="101">一元</option>
                            </select>
                        </td>
                    </tr>
                </table>
            </div>
            <div>
                <table>
                    <tr>
                        <td class="leftTd">
                            <lable><span class="required">*</span>焊接电流：</lable>
                        </td>
                        <td class="rightTd">
                            <input name="fweld_ele" id="fweld_ele" value="" class="easyui-numberbox" data-options="required:true">(A)
                        </td>
                        <td class="leftTd">
                            <lable><span class="required">*</span>焊接电流微调：</lable>
                        </td>
                        <td class="rightTd">
                            <input name="fweld_tuny_ele" id="fweld_tuny_ele" class="easyui-numberbox" data-options="required:true">(A)
                        </td>

                    </tr>
                </table>
            </div>
            <div id="hide2">
                <table>
                    <tr>
                        <td class="leftTd">
                            <lable><span class="required">*</span>提前送气：</lable>
                        </td>
                        <td class="rightTd">
                            <input name="fadvance" id="fadvance" class="easyui-numberbox" data-options="precision:1">(s)
                        </td>
                        <td class="leftTd">
                            <lable><span class="required">*</span>滞后送气：</lable>
                        </td>
                        <td class="rightTd">
                            <input name="fhysteresis" id="fhysteresis" class="easyui-numberbox" data-options="precision:1">(s)
                        </td>
                    </tr>
                </table>
            </div>
            <div id="hide3">
                <table>
                    <tr>
                        <td id="tinitial" class="leftTd">
                            <lable>初期条件：</lable>
                        </td>
                        <td id="rinitial" class="rightTd">
                            <input name="finitial" id="finitial" type="checkbox" value="1"/>
                        </td>
                        <td id="dmodel" class="leftTd">
                            <lable>柔软电弧模式：</lable>
                        </td>
                        <td id="imodel" class="rightTd"><input name="fmode" id="fmode" type="checkbox" value="1"></td>
                    </tr>
                </table>
            </div>
            <div id="hide4">
                <table>
                    <tr>
                        <td class="leftTd">
                            <lable><span class="required">*</span>初期电流：</lable>
                        </td>
                        <td class="rightTd">
                            <input name="fini_ele" id="fini_ele" class="easyui-numberbox" value="100.0" data-options="precision:1">(A)
                        </td>
                        <td id="dfarc" class="leftTd">
                            <lable><span class="required">*</span>收弧：</lable>
                        </td>
                        <td id="rfarc" class="rightTd">
                            <select class="easyui-combobox" name="farc" id="farc" data-options="editable:false"></select>
                        </td>
                    </tr>
                </table>
            </div>
            <div id="hide5">
                <table>
                    <tr>
                        <td class="leftTd">
                            <lable><span class="required">*</span>收弧电流：</lable>
                        </td>
                        <td class="rightTd">
                            <input name="farc_ele" id="farc_ele" class="easyui-numberbox" value="100.0" data-options="precision:1">(A)
                        </td>
                        <td class="leftTd">
                            <lable><span class="required">*</span>收弧电流微调：</lable>
                        </td>
                        <td class="rightTd">
                            <input name="farc_tuny_ele" id="farc_tuny_ele" class="easyui-numberbox" value="0.0" data-options="precision:1">(A)
                        </td>
                    </tr>
                </table>
            </div>
            <div id="hide6">
                <table>
                    <tr>
                        <td id="dftime" class="leftTd">
                            <lable><span class="required">*</span>点焊时间：</lable>
                        </td>
                        <td id="rftime" class="rightTd">
                            <input name="ftime" id="ftime" value="30.0" class="easyui-numberbox" data-options="precision:1">(s)
                        </td>
                        <td id="cwavt" class="leftTd">
                            <lable><span class="required">*</span>收弧电压微调：</lable>
                        </td>
                        <td id="cwtavt" class="rightTd">
                            <input name="farc_tuny_vol" id="farc_tuny_vol" class="easyui-numberbox" value="0.0" data-options="precision:1">(V)
                        </td>
                        <td id="cwivo" class="leftTd">
                            <lable><span class="required">*</span>初期电压(一元)：</lable>
                        </td>
                        <td id="cwtivo" class="rightTd">
                            <input name="fini_vol1" id="fini_vol1" class="easyui-numberbox" value="0">(±25)
                        </td>
                    </tr>
                </table>
            </div>
            <div id="hide7">
                <table>
                    <tr>
                        <td class="leftTd">
                            <lable><span class="required">*</span>电弧特性：</lable>
                        </td>
                        <td class="rightTd">
                            <input id="fcharacter" name="fcharacter" class="easyui-numberbox" value="0">(±10)
                        </td>
                        <td id="dfrequency" class="leftTd">
                            <lable><span class="required">*</span>双脉冲频率：</lable>
                        </td>
                        <td id="ifrequency" class="rightTd">
                            <input name="frequency" id="frequency" class="easyui-numberbox" value="3.0" data-options="precision:1">(Hz)
                        </td>
                    </tr>
                </table>
            </div>
            <div id="individual_1">
                <table>
                    <tr>
                        <td id="cwwv" class="leftTd">
                            <lable><span class="required">*</span>焊接电压：</lable>
                        </td>
                        <td id="cwtwv" class="rightTd">
                            <input name="fweld_vol" id="fweld_vol" class="easyui-numberbox" data-options="required:true,precision:1">(V)
                        </td>
                        <td id="cwwvt" class="leftTd">
                            <lable><span class="required">*</span>焊接电压微调：</lable>
                        </td>
                        <td id="cwtwvt" class="rightTd">
                            <input name="fweld_tuny_vol" id="fweld_tuny_vol" class="easyui-numberbox" data-options="required:true,precision:1">(V)
                        </td>
                    </tr>
                </table>
            </div>
            <div id="individual_2">
                <table>
                    <tr>
                        <td id="cwiv" class="leftTd">
                            <lable><span class="required">*</span>初期电压：</lable>
                        </td>
                        <td id="cwtiv" class="rightTd">
                            <input name="fini_vol" id="fini_vol" class="easyui-numberbox" value="19.0" data-options="precision:1">(V)
                        </td>
                        <td id="cwav" class="leftTd">
                            <lable><span class="required">*</span>收弧电压：</lable>
                        </td>
                        <td id="cwtav" class="rightTd">
                            <input name="farc_vol" id="farc_vol" class="easyui-numberbox" value="19.0" data-options="precision:1">(V)
                        </td>
                    </tr>
                </table>
            </div>
            <div id="unitary_1">
                <table>
                    <tr>
                        <td id="cwwvo" class="leftTd">
                            <lable><span class="required">*</span>焊接电压(一元)：</lable>
                        </td>
                        <td id="cwtwvo" class="rightTd">
                            <input name="fweld_vol1" id="fweld_vol1" class="easyui-numberbox">(±25)
                        </td>
                        <td id="cwwvto" class="leftTd">
                            <lable><span class="required">*</span>焊接电压微调(一元)：</lable>
                        </td>
                        <td id="cwtwvto" class="rightTd">
                            <input name="fweld_tuny_vol1" id="fweld_tuny_vol1" class="easyui-numberbox">(%)
                        </td>
                    </tr>
                </table>
            </div>
            <div id="unitary_2">
                <table>
                    <tr>
                        <td id="cwavo" class="leftTd">
                            <lable><span class="required">*</span>收弧电压(一元)：</lable>
                        </td>
                        <td id="cwtavo" class="rightTd">
                            <input name="farc_vol1" id="farc_vol1" class="easyui-numberbox" value="0">(±25)
                        </td>
                        <td id="cwavto" class="leftTd">
                            <lable><span class="required">*</span>收弧电压微调(一元)：</lable>
                        </td>
                        <td id="cwtavto" class="rightTd">
                            <input name="farc_tuny_vol1" id="farc_tuny_vol1" class="easyui-numberbox" value="0">(%)
                        </td>
                    </tr>
                </table>
            </div>
        </form>
    </div>

    <!-- 选择工艺(工艺库下发) -->
    <div id="smwdlg" class="easyui-dialog" style="width: 900px; height: 500px; padding:10px 20px" closed="true"
         buttons="#smwdlg-buttons">
        <form id="smwfm" class="easyui-form" method="post" data-options="novalidate:true">
            <table id="mainWpsTable" style="table-layout: fixed; width:100%;"></table>
        </form>
    </div>
    <div id="smwdlg-buttons">
        <a href="javascript:selectMachineList(1);" class="easyui-linkbutton" iconCls="icon-ok">下一步</a>
        <a href="javascript:closedlg();" class="easyui-linkbutton" iconCls="icon-cancel">取消</a>
    </div>

    <!-- 选择焊机 -->
    <div id="smdlg" class="easyui-dialog" style="width: 600px; height: 400px; padding:10px 20px" closed="true"
         buttons="#smdlg-buttons">
        <form id="smfm" class="easyui-form" method="post" data-options="novalidate:true">
            <table id="weldingmachineWpsTable" style="table-layout: fixed; width:100%;"></table>
        </form>
    </div>
    <div id="smdlg-buttons">
        <a href="javascript:selectModelSure();" class="easyui-linkbutton" iconCls="icon-ok">确认</a>
        <a href="javascript:closedlg();" class="easyui-linkbutton" iconCls="icon-cancel">取消</a>
    </div>

    <!-- 下发结果表格 -->
    <div id="resultdlg" class="easyui-dialog" style="width: 1120px; height: 600px; padding:10px 20px" closed="true"
         buttons="#resultdlg-buttons">
        <form id="resultfm" class="easyui-form" method="post" data-options="novalidate:true">
            <table id="giveResultTable" style="table-layout: fixed; width:100%;"></table>
        </form>
    </div>
    <div id="resultdlg-buttons">
        <a href="javascript:closedlg();" class="easyui-linkbutton" iconCls="icon-ok">确认</a>
        <a href="javascript:closedlg();" class="easyui-linkbutton" iconCls="icon-cancel">取消</a>
    </div>

    <!-- 控制命令下发 -->
    <div id="condlg" class="easyui-dialog" style="width: 700px; height: 300px; padding:10px 20px" closed="true"
         buttons="#condlg-buttons">
        <form id="confm" class="easyui-form" method="post" data-options="novalidate:true">
            <table width="100%" height="94%" border="1" style="text-align: center;">
                <tr height="30px">
                    <td colspan="2" align="center">
                        <font face="黑体" size="5">控制命令</font>
                    </td>
                </tr>
                <tr height="30px">
                    <td align="center" bgcolor="#FFFAF0">工作：</td>
                    <td>
                        <input id="free" name="free" type="radio" value="1" checked="checked"/>工作不可自由调节
                        <input name="free" type="radio" value="0"/>工作自由调节
                    </td>
                </tr>
                <tr height="30px">
                    <td colspan="2" align="center">
                        <a href="javascript:selectMachineList(3);" class="easyui-linkbutton"
                           iconCls="icon-ok">下发控制命令</a>
                        <a href="javascript:openPassDlg();" class="easyui-linkbutton" iconCls="icon-ok">密码下发</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div id="condlg-buttons">
        <a href="javascript:closedlg();" class="easyui-linkbutton" iconCls="icon-ok">确认</a>
        <a href="javascript:closedlg();" class="easyui-linkbutton" iconCls="icon-cancel">取消</a>
    </div>

    <!-- 密码框 -->
    <div id="pwd" class="easyui-dialog" style="text-align:center;width:400px;height:200px" closed="true"
         buttons="#dlg-pwd"
         algin="center">
        <br><br>
        <lable><span class="required">*</span>密码：</lable>
        <input name="passwd" id="passwd" type="password" class="easyui-numberbox"><br/>
        <lable style="color:red;">（注：密码范围是1~999）</lable>
    </div>
    <div id="dlg-pwd">
        <a href="javascript:selectMachineList(2);" class="easyui-linkbutton" iconCls="icon-ok">下一步</a>
        <a href="javascript:closedlg();" class="easyui-linkbutton" iconCls="icon-cancel">取消</a>
    </div>

    <!-- 选择松下工艺 -->
    <%--		<div id="sxSelectdlg" class="easyui-dialog" style="width: 600px; height: 400px; padding:10px 20px" closed="true" buttons="#sxSelectdlg-buttons">--%>
    <%--			<form id="smwfm" class="easyui-form" method="post" data-options="novalidate:true">--%>
    <%--				<table id="sxSelectWpsTab" style="table-layout: fixed; width:100%;"></table>--%>
    <%--			</form>--%>
    <%--		</div>--%>
    <%--		<div id="sxSelectdlg-buttons">--%>
    <%--			<a href="javascript:selectSxMachineList(1);" class="easyui-linkbutton" iconCls="icon-ok">下一步</a>--%>
    <%--			<a href="javascript:closedlg();" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>--%>
    <%--		</div>--%>
    <%--		--%>
    <%--		<!-- 选择松下焊机 -->--%>
    <%--		<div id="sxMachinedlg" class="easyui-dialog" style="width: 600px; height: 400px; padding:10px 20px" closed="true" buttons="#sxmachinedlg-buttons">--%>
    <%--			<form id="sxmachinefm" class="easyui-form" method="post" data-options="novalidate:true">--%>
    <%--				<table id="sxMachineTable" style="table-layout: fixed; width:100%;"></table>--%>
    <%--			</form>--%>
    <%--		</div> --%>
    <%--		<div id="sxmachinedlg-buttons">--%>
    <%--			<a href="javascript:selectSxModel();" class="easyui-linkbutton" iconCls="icon-ok">确认</a>--%>
    <%--			<a href="javascript:closedlg();" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>--%>
    <%--		</div>--%>

    <!-- 下发历史查询 -->
    <div id="wmhistorydlg" class="easyui-dialog" style="width: 950px; height: 520px; padding:10px 20px" closed="true">
        <form id="wmhistoryfm" class="easyui-form" method="post" data-options="novalidate:true">
            <div id="dg_btn">
                <div style="margin-bottom: 5px;">
                    焊机编号：
                    <input class="easyui-numberbox" name="machineNum" id="machineNum">
                    工艺库名称：
                    <input class="easyui-textbox" name="theWpslibName" id="theWpslibName">
                    时间：
                    <input class="easyui-datetimebox" name="dtoTime1" id="dtoTime1">--
                    <input class="easyui-datetimebox" name="dtoTime2" id="dtoTime2">
                    <a href="javascript:searchHistory();" class="easyui-linkbutton" iconCls="icon-select">搜索</a>
                </div>
            </div>
            <table id="historyTable" style="table-layout: fixed; width:100%;"></table>
        </form>
    </div>

    <div id="importdiv" class="easyui-dialog" style="width:300px; height:200px;" closed="true">
        <form id="importfm" method="post" class="easyui-form" data-options="novalidate:true"
              enctype="multipart/form-data">
            <div>
                <span><input type="file" name="file" id="file"></span>
                <input type="button" value="上传" onclick="importWpsExcel()" class="upButton"/>
            </div>
        </form>
    </div>
    <div id="load" style="width:100%;height:100%;"></div>
</div>
<style type="text/css">
    #load {
        display: none;
        position: absolute;
        left: 0;
        top: 0;
        width: 100%;
        height: 40%;
        background-color: #555753;
        z-index: 10001;
        -moz-opacity: 0.4;
        opacity: 0.5;
        filter: alpha(opacity=70);
    }

    #show {
        display: none;
        position: absolute;
        top: 45%;
        left: 45%;
        width: 180px;
        height: 5%;
        padding: 8px;
        border: 8px solid #E8E9F7;
        background-color: white;
        z-index: 10002;
        overflow: auto;
    }
</style>
</body>
</html>