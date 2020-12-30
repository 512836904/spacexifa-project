$(function () {
    junctionDatagrid();
});
var vlogoflag = "";
function junctionDatagrid() {
    $("#productionTable").datagrid({
        height: $("#body").height(),
        width: $("#body").width(),
        idField: 'FID',
        pageSize: 10,
        pageList: [10, 20, 30, 40, 50],
        url: "production/getProductionCraftList",
        singleSelect: true,
        rownumbers: true,
        remoteSort: false,
        showPageList: false,
        columns: [[{
            field: 'FID',
            title: '序号',
            width: 50,
            halign: "center",
            align: "left",
            hidden: true
        }, {
            field: 'FNAME',
            title: '工艺名',
            width: 80,
            halign: "center",
            align: "center"
        }, {
            field: 'FJUNCTION',
            title: '焊缝名称',
            width: 250,
            halign: "center",
            align: "center"
        }, {
            field: 'PREHEAT',
            title: '预热℃',
            width: 80,
            halign: "center",
            align: "center"
        }, {
            field: 'INTERLAMINATION',
            title: '层间℃',
            width: 80,
            halign: "center",
            align: "center"
        }, {
            field: 'WELDING_MATERIAL',
            title: '焊材mm',
            width: 80,
            halign: "center",
            align: "center"
        }, {
            field: 'WIRE_DIAMETER',
            title: '焊丝直径',
            width: 80,
            halign: "center",
            align: "center"
        }, {
            field: 'ELECTRICITY_FLOOR',
            title: '电流下限A',
            width: 80,
            halign: "center",
            align: "center"
        }, {
            field: 'ELECTRICITY_UPPER',
            title: '电流上限A',
            width: 80,
            halign: "center",
            align: "center"
        }, {
            field: 'VOLTAGE_FLOOR',
            title: '电压下限V',
            width: 80,
            halign: "center",
            align: "center"
        }, {
            field: 'VOLTAGE_UPPER',
            title: '电压上限V',
            width: 80,
            halign: "center",
            align: "center"
        }, {
            field: 'SOLDER_SPEED_FLOOR',
            title: '焊速下限mm/min',
            width: 80,
            halign: "center",
            align: "center"
        }, {
            field: 'SOLDER_SPEED_UPPER',
            title: '焊速上限mm/min',
            width: 80,
            halign: "center",
            align: "center"
        }, {
            field: 'WIDE_SWING',
            title: '摆宽mm',
            width: 80,
            halign: "center",
            align: "center"
        }, {
            field: 'RESTS',
            title: '其他',
            width: 80,
            halign: "center",
            align: "center"
        }, {
            field: 'DATA_SOURCES',
            title: '数据来源',
            width: 80,
            halign: "center",
            align: "center",
            formatter: function (value, row, index) {
                var str = "";
                if (value == 1){
                    str += "系统录入";
                }else if (value == 2){
                    str += "终端扫码录入";
                }
                return str;
            }
        }, {
            field: 'edit',
            title: '操作',
            width: 200,
            halign: "center",
            align: "left",
            formatter: function (value, row, index) {
                var str = "";
                str += '<a id="edit" class="easyui-linkbutton" href="javascript:editProduction(' + row.FID + ')"/>';
                str += '<a id="delete" class="easyui-linkbutton" href="javascript:deleteProduction(' + row.FID + ')"/>';
                return str;
            }
        }
        ]],
        pagination: true,
        fitColumns: true,
        rowStyler: function (index, row) {
            if ((index % 2) != 0) {
                //处理行代背景色后无法选中
                var color = new Object();
                return color;
            }
        },
        onLoadSuccess: function (data) {
            $("a[id='edit']").linkbutton({text: '编辑', plain: true, iconCls: 'icon-update'});
            $("a[id='delete']").linkbutton({text: '删除', plain: true, iconCls: 'icon-delete'});
        }
    });
}

//新增
function addProduction() {
    vlogoflag = "add";
    $('#fm').form('clear');
    $('#dlg').window({
        title: "新增生产工艺库",
        modal: true
    });
    $('#dlg').window('open');
    // $('#fm').form('load', row);
}

//保存
function saveProduction() {
    var urls = "";
    var message = "";
    if (vlogoflag == "add") {
        message = "新增成功！";
        urls = "production/addProduction";
    } else if (vlogoflag == "edit") {
        message = "修改成功！";
        urls = "production/updateProduction";
    }
    if (urls != "") {
        $('#fm').form('submit', {
            url: urls,
            type: 'POST',
            dataType: "text",
            onSubmit: function () {
                return $(this).form('enableValidation').form('validate');
            },
            success: function (result) {
                if (result) {
                    var result = eval('(' + result + ')');
                    if (!result.success) {
                        $.messager.show({
                            title: 'Error',
                            msg: result.errorMsg
                        });
                    } else {
                        $.messager.alert("提示", message);
                        $('#dlg').dialog('close');
                        $('#productionTable').datagrid('reload');
                    }
                }
            },
            error: function (errorMsg) {
                alert("数据请求失败，请联系系统管理员!");
            }
        });
    }
}

//编辑
function editProduction(fid) {
    vlogoflag = "edit";
    $('#fm').form('clear');
    var row = $('#productionTable').datagrid('getSelected');
    if (row) {
        $('#dlg').window({
            title: "编辑",
            modal: true
        });
        $('#dlg').window('open');
        $('#FID').val(fid);
        $('#fm').form('load', row);
    }
}

//删除
function deleteProduction(fid) {
    $.messager.confirm('提示', '确认删除该工艺信息?', function (flag) {
        if (flag) {
            $.ajax({
                type: "post",
                async: false,
                url: 'production/deleteProduction',
                data: {
                    'fid': fid
                },
                dataType: "json", //返回数据形式为json
                success: function (result) {
                    if (result) {
                        if (!result.success) {
                            $.messager.show({
                                title: 'Error',
                                msg: result.msg
                            });
                        } else {
                            $.messager.alert("提示", "删除成功！");
                            $('#productionTable').datagrid('reload');
                        }
                    }
                },
                error: function (errorMsg) {
                    alert("数据请求失败，请联系系统管理员!");
                }
            });
        }
    });
}

//查询
function searchProduction() {
    var name_search = $('#name_search').textbox("getValue");          //焊缝编号
    var query = {name_search:name_search}; //把查询条件拼接成JSON
    $("#productionTable").datagrid('options').queryParams = query; //把查询条件赋值给datagrid内部变量
    $('#productionTable').datagrid('reload');
}

//监听窗口大小变化
window.onresize = function () {
    setTimeout(domresize, 500);
}

//改变表格高宽
function domresize() {
    $("#productionTable").datagrid('resize', {
        height: $("#body").height(),
        width: $("#body").width()
    });
}