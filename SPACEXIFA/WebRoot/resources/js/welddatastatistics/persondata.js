$(function () {
    //dgDatagrid();
    itemcombobox();
    wpslibDatagrid();
})

var chartStr = "";

function setParam() {
    var dtoTime1 = $("#dtoTime1").datetimebox('getValue');
    var dtoTime2 = $("#dtoTime2").datetimebox('getValue');
    chartStr += "?dtoTime1=" + dtoTime1 + "&dtoTime2=" + dtoTime2;
    // alert(chartStr);
}
function wpslibDatagrid(){
    parameterStr1();
    var url1 = encodeURI("datastatistics/getwelderweldtime?search="+searchStr);
    $("#dg").datagrid( {
        fitColumns : false,
        height: $("#wpsTableDiv").height(),
        width: $("#wpsTableDiv").width(),
        idField : 'fid',
        pageSize : 50,
        pageList : [ 10, 20, 30, 40, 50 ],
        url : url1,
        singleSelect : true,
        rownumbers : true,
        showPageList : false,
        columns : [ [ {
            field : 'fid',
            title : '序号',
//			width : 30,
            halign : "center",
            align : "left",
            hidden:true
        },{
            field : 'fprefix_number',
            title : '工作号',
            width : 200,
            halign : "center",
            align : "left"
        }, {
            field : 'fsuffix_number',
            title : '部套号',
            width : 200,
            halign : "center",
            align : "left"
        }, {
            field : 'fproduct_number',
            title : '工艺编号',
            width : 100,
            halign : "center",
            align : "left"
        },{
            field : 'junctionname',
            title : '焊缝名称',
            width : 200,
            halign : "center",
            align : "left"
        }, {
            field : 'fweldername',
            title : '焊工姓名',
            width : 100,
            halign : "center",
            align : "left"
        },{
            field : 'fweldernum',
            title : '焊工编号',
            width : 100,
            halign : "center",
            align : "left"
        },{
            field : 'fitem',
            title : '所属班组',
            width : 100,
            halign : "center",
            align : "left"
        },{
            field : 'worktime',
            title : '累计焊接时长',
            width : 100,
            halign : "center",
            align : "left"
        }, {
            field : 'nomeltime',
            title : '正常段时长',
            width : 100,
            halign : "center",
            align : "left"
        }, {
            field : 'alarmtime',
            title : '超规范时长',
            width : 100,
            halign : "center",
            align : "left"
        }, {
            field : 'temp',
            title : '规范符合率',
            width : 100,
            halign : "center",
            align : "left"
        }, {
            field : 'edit',
            title : '编辑',
            width : 130,
            halign : "center",
            align : "left",
            formatter:function(value,row,index){
                var str = "";
                var chart="";
                var dtoTime1 = $("#dtoTime1").datetimebox('getValue');
                var dtoTime2 = $("#dtoTime2").datetimebox('getValue');
                chart= "fid=" +row.welder_id + "&fjunction_id=" + row.fjunction_id + "&dtoTime1=" +'('+ dtoTime1 +')'+ "&dtoTime2=" +'('+ dtoTime2+')';
                str += '<a id="mc" class="easyui-linkbutton" href="weldedjunction/getNnstandardHistory?'+chart+'">';
                return str;
            }
        }] ],
        pagination : true,
        rowStyler: function(index,row){
            if ((index % 2)!=0){
                //处理行代背景色后无法选中
                var color=new Object();
                return color;
            }
        },
        onLoadSuccess:function(data){
            $("a[id='mc']").linkbutton({text:'任务信息',plain:true,iconCls:'icon-search'});
        }
    });
}

function dgDatagrid() {
    setParam();
    var column = new Array();
    $.ajax({
        type: "post",
        async: false,
        url: "datastatistics/getWeldPersonData" + chartStr,
        data: {},
        dataType: "json", //返回数据形式为json
        success: function (result) {
            if (result) {
                var str = ["焊工编号", "焊工姓名", "累计焊接时间", "正常段时长", "超规范时长", "规范符合率(%)"];
                for (var i = 0; i < str.length; i++) {
                    column.push({
                        field: "t" + i,
                        title: str[i],
                        width: 100,
                        halign: "center",
                        align: "left",
                        sortable: true,
                        sorter: function (a, b) {
                            return (a > b ? 1 : -1);
                        }
                    });
                }
                var grid = {
                    fitColumns: true,
                    height: $("#body").height(),
                    width: $("#body").width(),
                    url: "datastatistics/getWeldPersonData" + chartStr,
                    pageSize: 10,
                    pageList: [10, 20, 30, 40, 50],
                    singleSelect: true,
                    rownumbers: true,
                    showPageList: false,
                    pagination: true,
                    remoteSort: false,
                    nowrap: false,
                    columns: [column],
                    rowStyler: function (index, row) {
                        if ((index % 2) != 0) {
                            //处理行代背景色后无法选中
                            var color = new Object();
                            return color;
                        }
                    },
                    onBeforeLoad: function (param) {
                        $("#chartLoading").hide();
                    }
                };
                $('#dg').datagrid(grid);
            }
        },
        error: function (errorMsg) {
            alert("数据请求失败，请联系系统管理员!");
        }
    });
}
function itemcombobox() {
    /*	$.ajax({
          type : "post",
          async : false,
          url : "datastatistics/getAllInsframework",
          data : {},
          dataType : "json", //返回数据形式为json
          success : function(result) {
              if (result) {
                  var optionStr = '';
                  for (var i = 0; i < result.ary.length; i++) {
                      optionStr += "<option value=\"" + result.ary[i].id + "\" >"
                              + result.ary[i].name + "</option>";
                  }
                  $("#item").html(optionStr);
              }
          },
          error : function(errorMsg) {
              alert("数据请求失败，请联系系统管理员!");
          }
        });
        $("#item").combobox();*/

    $.ajax({
        type: "post",
        async: false,
        url: "weldtask/getOperateArea",
        data: {},
        dataType: "json", //返回数据形式为json
        success: function (result) {
            if (result) {
                if (result.type == 23) {
                    var zoptionStr = "";
                    var boptionStr = "";
                    for (var i = 0; i < result.ary.length; i++) {
                        zoptionStr += "<option value=\"" + result.ary[i].id + "\" >"
                            + result.ary[i].name + "</option>";
                    }
                    for (var j = 0; j < result.banzu.length; j++) {
                        boptionStr += "<option value=\"" + result.banzu[j].id + "\" >"
                            + result.banzu[j].name + "</option>";
                    }
                    $("#zitem").html(zoptionStr);
                    $("#bitem").html(boptionStr);
                    $("#zitem").combobox();
                    $("#zitem").combobox('select', result.ary[0].id);
                    $("#bitem").combobox();
                    $("#bitem").combobox('select', result.banzu[0].id);
//		        	$("#zitem").combobox({disabled: true});
//		        	$("#bitem").combobox({disabled: true});
                } else if (result.type == 22) {
                    var zoptionStr = "";
                    var boptionStr = '<option value="0">请选择</option>';
                    for (var i = 0; i < result.ary.length; i++) {
                        zoptionStr += "<option value=\"" + result.ary[i].id + "\" >"
                            + result.ary[i].name + "</option>";
                    }
                    for (var j = 0; j < result.banzu.length; j++) {
                        boptionStr += "<option value=\"" + result.banzu[j].id + "\" >"
                            + result.banzu[j].name + "</option>";
                    }
                    $("#zitem").html(zoptionStr);
                    $("#bitem").html(boptionStr);
                    $("#zitem").combobox();
                    $("#zitem").combobox('select', result.ary[0].id);
                    $("#bitem").combobox();
                    $("#bitem").combobox('select', 0);
//		        	$("#zitem").combobox({disabled: true});
                } else {
                    $("#bitem").combobox({disabled: true});
                    var zoptionStr = '<option value="0">请选择</option>';
                    for (var i = 0; i < result.ary.length; i++) {
                        zoptionStr += "<option value=\"" + result.ary[i].id + "\" >"
                            + result.ary[i].name + "</option>";
                    }
                    $("#zitem").html(zoptionStr);
                    $("#zitem").combobox();
                    $("#zitem").combobox('select', 0);
                }

            }
        },
        error: function (errorMsg) {
            alert("数据请求失败，请联系系统管理员!");
        }
    });

    $("#zitem").combobox({
        onChange: function (newValue, oldValue) {
            if (oldValue != "") {
                $.ajax({
                    type: "post",
                    async: false,
                    url: "weldtask/getTeam?searchStr=" + " and i.fparent=" + newValue,
                    data: {},
                    dataType: "json", //返回数据形式为json
                    success: function (result) {
                        if (result) {
                            var boptionStr = '<option value="0">请选择</option>';
                            for (var i = 0; i < result.ary.length; i++) {
                                boptionStr += "<option value=\"" + result.ary[i].id + "\" >"
                                    + result.ary[i].name + "</option>";
                            }
                            $("#bitem").html(boptionStr);
                            $("#bitem").combobox();
                            $("#bitem").combobox('select', 0);
                            $("#bitem").combobox({disabled: false});
                        }
                    },
                    error: function (errorMsg) {
                        alert("数据请求失败，请联系系统管理员!");
                    }
                });
            }
        }
    })

    $("#zitem").combobox('select', 0);
}

function serach() {
    // $("#chartLoading").show();
    // chartStr = "";
    // setTimeout(function () {
    //     dgDatagrid();
    // }, 500);
    wpslibDatagrid();
}

//导出到Excel
function exportExcel() {
    chartStr = "";
    setParam();
    $.messager.confirm("提示", "文件默认保存在浏览器的默认路径，<br/>如需更改路径请设置浏览器的<br/>“下载前询问每个文件的保存位置“属性！", function (result) {
        if (result) {
            var url = "export/exportPersonWelddata";
            var img = new Image();
            img.src = url;  // 设置相对路径给Image, 此时会发送出请求
            url = img.src;  // 此时相对路径已经变成绝对路径
            img.src = null; // 取消请求
            window.location.href = encodeURI(url + chartStr);
        }
    });
}
function parameterStr1(){
    searchStr = "";
    var dt1 = $("#dtoTime1").datetimebox('getValue');
    var dt2 = $("#dtoTime2").datetimebox('getValue');
    //var item = $("#item").combobox('getValue');
    var product_drawing_no = $("#product_drawing_no").val();
    var product_name = $("#product_name").val();
    var taskno = $("#taskno").val();
    var fwps_lib_num = $("#fwps_lib_num").val();
    var fwelded_junction_no = $("#fwelded_junction_no").val();
    var product_number = $("#product_number").val();
    var junction_name = $("#fwelded_junction_no").val();
    var weldername = $("#weldername").val();
    var weldernum = $("#weldernum").val();
    var zitem = $("#zitem").combobox('getValue');
    var bitem = $("#bitem").combobox('getValue');
    var item = "";
    if (zitem != 0) {
        item = zitem;
    }
    if (bitem != 0) {
        item = bitem;
    }
    if(item != ""){
        if(searchStr == ""){
            searchStr += " (i.fid =" + item + "  or ins.fid =" + item + " or insf.fid =" + item + " or insf.fparent =" + item + ")";
        }else{
            searchStr += " AND (i.fid =" + item + "  or ins.fid =" + item + " or insf.fid =" + item + " or insf.fparent =" + item + ")";
        }
    }
    if(weldername != ""){
        if(searchStr == ""){
            searchStr += " e.fname LIKE "+"'%" + weldername + "%'";
        }else{
            searchStr += " AND e.fname LIKE "+"'%" + weldername + "%'";
        }
    }
    if(weldernum != ""){
        if(searchStr == ""){
            searchStr += " e.fwelder_no LIKE "+"'%" + weldernum + "%'";
        }else{
            searchStr += " AND e.fwelder_no LIKE "+"'%" + weldernum + "%'";
        }
    }
    if(junction_name != ""){
        if(searchStr == ""){
            searchStr += " u.junction_name LIKE "+"'%" + junction_name + "%'";
        }else {
            searchStr += " AND u.junction_name LIKE "+"'%" + junction_name + "%'";
        }
    }
    if(product_drawing_no != ""){
        if(searchStr == ""){
            searchStr += " j.JOB_NUMBER LIKE "+"'%" + product_drawing_no + "%'";
        }else{
            searchStr += " AND j.JOB_NUMBER LIKE "+"'%" + product_drawing_no + "%'";
        }
    }
    if(product_name != ""){
        if(searchStr == ""){
            searchStr += " j.SET_NUMBER LIKE "+"'%" + product_name + "%'";
        }else {
            searchStr += " AND j.SET_NUMBER LIKE "+"'%" + product_name + "%'";
        }
    }
    if(taskno != ""){
        if(searchStr == ""){
            searchStr += " c.fname LIKE "+"'%" + taskno + "%'";
        }else{
            searchStr += " AND c.fname LIKE "+"'%" + taskno + "%'";
        }
    }
    if(fwelded_junction_no != ""){
        if(searchStr == ""){
            searchStr += " u.junction_name LIKE "+"'%" + fwelded_junction_no + "%'";
        }else{
            searchStr += " AND u.junction_name LIKE "+"'%" + fwelded_junction_no + "%'";
        }
    }

    if(dt1 != ""){
        if(searchStr == ""){
            searchStr += " fstarttime >to_date('" +dt1+"', 'yyyy-mm-dd hh24:mi:ss')";
        }else{
            searchStr += " AND fstarttime >to_date('"+dt1+"', 'yyyy-mm-dd hh24:mi:ss')";
        }
    }
    if(dt2 != ""){
        if(searchStr == ""){
            searchStr += " fendtime < to_date('"+dt2+"', 'yyyy-mm-dd hh24:mi:ss')";
        }else{
            searchStr += " AND fendtime <to_date('"+dt2+"', 'yyyy-mm-dd hh24:mi:ss')";
        }
    }
}
//监听窗口大小变化
window.onresize = function () {
    setTimeout(domresize, 500);
}

//改变表格高宽
function domresize() {
    $("#dg").datagrid('resize', {
        height: $("#body").height(),
        width: $("#body").width()
    });
}