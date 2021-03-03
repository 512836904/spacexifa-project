/**
 *
 */
$(function () {
    wpslibDatagrid();
    $.ajax({
        url: 'pmt/getParameterBySupergage',
        type: 'post',
        dataType: 'json',
        data: {},
        success: function (result) {
            $("#supergage_status").val(result.supergage_status);
            $("#paramterId").val(result.paramterId);
            if (result.supergage_status == 1){
                document.getElementById("supergageShow").checked = true;
            } else {
                document.getElementById("supergageHide").checked = true;
            }
        },
        error: function () {
            console.log("工作号展示异常");
        }
    });
})

var chartStr = "";

function setParam() {
    var dtoTime1 = $("#dtoTime1").datetimebox('getValue');
    var dtoTime2 = $("#dtoTime2").datetimebox('getValue');
    var machineNum = $("#machineNum").numberbox('getValue');
    var wpslibName = $("#theWpslibName").textbox('getValue');
    chartStr += "?machineNum=" + machineNum + "&wpslibName=" + encodeURI(wpslibName) + "&dtoTime1=" + dtoTime1 + "&dtoTime2=" + dtoTime2;
}

function wpslibDatagrid() {
    $("#wpslibTable").datagrid({
        fitColumns: false,
        height: $("#body").height(),
        width: $("#body").width(),
        idField: 'fid',
        pageSize: 10,
        pageList: [10, 20, 30, 40, 50],
        url: "wps/getWpsList",
        singleSelect: false,
        rownumbers: true,
        showPageList: false,
        columns: [[{
            field: 'ck',
            checkbox: true
        }, {
            field: 'fid',
            title: '序号',
//			width : 30,
            halign: "center",
            align: "left",
            hidden: true
        }, {
            field: 'workticket_number',
            title: '工票编号',
            width: 150,
            halign: "center",
            align: "center"
        }, {
            field: 'JOB_NUMBER',
            title: '工作号',
            width: 150,
            halign: "center",
            align: "center"
        }, {
            field: 'SET_NUMBER',
            title: '部套号',
            width: 150,
            halign: "center",
            align: "center"
        }, {
            field: 'PART_DRAWING_NUMBER',
            title: '零件图号',
            width: 150,
            halign: "center",
            align: "center"
        }, {
            field: 'PART_NAME',
            title: '零件名',
            width: 150,
            halign: "center",
            align: "center"
        }, {
            field: 'PRODUCTION_CRAFT',
            title: '生产工艺名称',
            width: 150,
            halign: "center",
            align: "center"
        }, {
            field: 'craft_param',
            title: '工艺参数',
            width: 300,
            halign: "center",
            align: "center"
        }, {
            field: 'status',
            title: '工作号是否展示',
            width: 150,
            halign: "center",
            align: "center",
            formatter: function (value, row, index) {
                var str = "";
                if (value === 0) {
                    str += "<span style='color: #CC2222'>否</span>";
                } else if (value === 1) {
                    str += "<span style='color: #00ee00'>是</span>";
                }
                return str;
            }
        }
            // , {
            // 	field : 'raw_materi',
            // 	title : '原料',
            // 	width : 150,
            // 	halign : "center",
            // 	align : "center"
            // }, {
            // 	field : 'process',
            // 	title : '工序',
            // 	width : 150,
            // 	halign : "center",
            // 	align : "center"
            // }
            , {
                field: 'FOPERATETYPE',
                title: '任务完成状态',
                width: 150,
                halign: "center",
                align: "center",
                formatter: function (value, row, index) {
                    var str = "";
                    if (value == null) {
                        str += "未开始";
                    } else if (value == 0) {
                        str += "进行中";
                    } else if (value == 1) {
                        str += "已完成";
                    } else if (value == 2) {
                        str += "返修";
                    }
                    return str;
                }
            }
        ]],
        pagination: true,
        rowStyler: function (index, row) {
            if ((index % 2) != 0) {
                //处理行代背景色后无法选中
                var color = new Object();
                return color;
            }
        },
        onLoadSuccess: function (data) {
            if ($("#wait").length != 0) {
                $("a[id='wait']").linkbutton({text: '待审核', plain: true, iconCls: 'icon-newcancel'});
            }
            if ($("#down").length != 0) {
                $("a[id='down']").linkbutton({text: '被驳回', plain: true, iconCls: 'icon-next'});
            }
            if ($("#finish").length != 0) {
                $("a[id='finish']").linkbutton({text: '已通过', plain: true, iconCls: 'icon-over'});
            }
        }
    });
}

function searchWps() {
    var search = "";
    var job_number_search = $("#job_number_search").textbox('getValue');
    var set_number_search = $("#set_number_search").textbox('getValue');
    var part_drawing_search = $("#part_drawing_search").textbox('getValue');
    var part_name_search = $("#part_name_search").textbox('getValue');
    if (job_number_search != "") {
        if (search == "") {
            search += " JOB_NUMBER LIKE " + "'%" + job_number_search + "%'";
        } else {
            search += " AND JOB_NUMBER LIKE " + "'%" + job_number_search + "%'";
        }
    }
    if (set_number_search != "") {
        if (search == "") {
            search += " SET_NUMBER LIKE " + "'%" + set_number_search + "%'";
        } else {
            search += " AND SET_NUMBER LIKE " + "'%" + set_number_search + "%'";
        }
    }
    if (part_drawing_search != "") {
        if (search == "") {
            search += " PART_DRAWING_NUMBER LIKE " + "'%" + part_drawing_search + "%'";
        } else {
            search += " AND PART_DRAWING_NUMBER LIKE " + "'%" + part_drawing_search + "%'";
        }
    }
    if (part_name_search != "") {
        if (search == "") {
            search += " PART_NAME LIKE " + "'%" + part_name_search + "%'";
        } else {
            search += " AND PART_NAME LIKE " + "'%" + part_name_search + "%'";
        }
    }
    $('#wpslibTable').datagrid('load', {
        "search": search
    });
}

//工作号展示
function allshow() {
    var rows = $('#wpslibTable').datagrid('getSelections');
    if (rows.length > 0) {
        var jobIds = [];
        for (var index in rows) {
            jobIds.push(rows[index].fid);
        }
        var jobArray = "" + jobIds;
        $.ajax({
            url: 'wps/jobNumberShowOrCancel',
            type: 'post',
            dataType: 'json',
            data: {
                jobIds: jobArray,
                type: "show"
            },
            success: function (result) {
                if (result && result.success) {
                    $('#wpslibTable').datagrid('reload');
                    alert("展示成功");
                } else {
                    alert("展示失败");
                }
            },
            error: function () {
                console.log("工作号展示异常");
            }
        });
    } else {
        alert("请至少选择一个工作号进行展示！");
    }
}

//工作号展示取消
function allcancel() {
    var rows = $('#wpslibTable').datagrid('getSelections');
    if (rows.length > 0) {
        var jobIds = new Array();
        for (var index in rows) {
            jobIds.push(rows[index].fid);
        }
        var jobArray = "" + jobIds;
        $.ajax({
            url: 'wps/jobNumberShowOrCancel',
            type: 'post',
            dataType: 'json',
            data: {
                jobIds: jobArray,
                type: "cancel"
            },
            success: function (result) {
                if (result && result.success) {
                    $('#wpslibTable').datagrid('reload');
                    alert("取消成功");
                } else {
                    alert("取消失败");
                }
            },
            error: function () {
                console.log("工作号取消展示异常");
            }
        });
    } else {
        alert("请至少选择一个工作号取消展示！");
    }
}

//超规范信息一键展示,隐藏
function supergageShowOrHide(status) {
    /**
     * 查询每个焊缝的电流电压上限，在实时信息展示中，
     * 判断电流电压如果超出上限，则不进行展示
     */
    if (status === 1){
        document.getElementById("supergageShow").checked = true;
    } else {
        document.getElementById("supergageHide").checked = true;
    }
    $.ajax({
        url: 'pmt/updateParameterBySupergage',
        type: 'post',
        dataType: 'json',
        data: {
            supergage_status: status,
            paramterId: $("#paramterId").val()
        },
        success: function (result) {
            if (result && result.success) {
                if (status === 1){
                    alert("展示成功！");
                }else {
                    alert("隐藏成功！");
                }
            } else {
                if (status === 1){
                    alert("展示失败！");
                }else {
                    alert("隐藏失败！");
                }
            }
        },
        error: function () {
            console.log("超规范信息展示异常");
        }
    });
}

function getContextPath() {
    var pathName = document.location.pathname;
    var index = pathName.substr(1).indexOf("/");
    var result = pathName.substr(0, index + 1);
    return result;
}

function wpsDetails() {
    var row = $('#wpslibTable').datagrid('getSelected');
    if (row) {
        window.location.href = encodeURI(getContextPath() + "/wps/goWpsdetails" + "?fid=" + row.fid + "&fproduct_name=" + row.fproduct_name + "&status=" + row.fstatus);
    } else {
        alert("请先选择一条数据。");
    }
}

function closeDlg() {
    if (!$("#addOrUpdate").parent().is(":hidden")) {
        $('#addOrUpdate').window('close');
    }
}

//打开文件导入dialog
function importclick() {
    $("#importdiv").dialog("open").dialog("setTitle", "从excel导入数据");
}

//确认导入
function importWpsExcel() {
    var file = $("#file").val();
    if (file == null || file == "") {
        $.messager.alert("提示", "请选择要上传的文件！");
        return false;
    } else {
        document.getElementById("load").style.display = "block";
        var sh = '<div id="show" style="align="center""><img src="resources/images/load.gif"/>正在加载，请稍等...</div>';
        $("#body").append(sh);
        document.getElementById("show").style.display = "block";
        $('#importfm').form('submit', {
            url: "import/importweldWps",
            success: function (result) {
                if (result) {
                    var result = eval('(' + result + ')');
                    document.getElementById("load").style.display = 'none';
                    document.getElementById("show").style.display = 'none';
                    if (!result.success) {
                        $.messager.show({
                            title: 'Error',
                            msg: result.msg
                        });
                    } else {
                        $('#importdiv').dialog('close');
                        $('#wpslibTable').datagrid('reload');
                        alert("导入成功");
                    }
                }

            },
            error: function (errorMsg) {
                alert("数据请求失败，请联系系统管理员!");
            }
        });

    }
}

//导出到excel
function exportclick() {
    $.messager.confirm("提示", "文件默认保存在浏览器的默认路径，<br/>如需更改路径请设置浏览器的<br/>“下载前询问每个文件的保存位置“属性！", function (result) {
        if (result) {
            var url = "export/exporWeldwps";
            var img = new Image();
            img.src = url;  // 设置相对路径给Image, 此时会发送出请求
            url = img.src;  // 此时相对路径已经变成绝对路径
            img.src = null; // 取消请求
            window.location.href = encodeURI(url);
        }
    });
}

//监听窗口大小变化
window.onresize = function () {
    setTimeout(domresize(), 500);
}

//改变表格高宽
function domresize() {
    $("#wpslibTable").datagrid('resize', {
        height: $("#body").height(),
        width: $("#body").width()
    });
}