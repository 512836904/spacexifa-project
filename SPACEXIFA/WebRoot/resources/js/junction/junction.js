$(function () {
    junctionDatagrid();
});
var vlogoflag = "";
var dialogDivWidth = "800";

function junctionDatagrid() {
    // var singleSelect = true;
    // if (null == document.getElementById("dialogDiv")){
    //     //焊缝管理列表
    //     dialogDivWidth = (document.getElementById("body").style.width).substring(0,4);
    // }else {
    //     //查找带回列表
    //     dialogDivWidth = (document.getElementById("dialogDiv").style.width).substring(0,4);
    //     singleSelect = false;
    // }
    $("#junctionTable").datagrid({
        height: $("#body").height(),
        width: $("#body").width(),
        // height: document.getElementById("junctionTable").style.height,
        // width: dialogDivWidth,
        idField: 'fid',
        pageSize: 50,
        pageList: [10, 20, 30, 40, 50],
        url: "junction/getJunctionList",
        // singleSelect: singleSelect,
        singleSelect: true,
        rownumbers: true,
        remoteSort: false,
        showPageList: false,
        columns: [[{
            field: 'fid',
            title: '序号',
            width: 50,
            halign: "center",
            align: "left",
            hidden: true
        }, {
            field: 'junction_name',
            title: '焊缝名称',
            // width: (dialogDivWidth*0.3),
            width: 200,
            halign: "center",
            align: "center"
        }, {
            field: 'current_limit',
            title: '电流上限',
            // width: (dialogDivWidth*0.2),
            width: 80,
            halign: "center",
            align: "center"
        }, {
            field: 'current_lower_limit',
            title: '电流下限',
            // width: (dialogDivWidth*0.2),
            width: 80,
            halign: "center",
            align: "center"
        }, {
            field: 'FMAXVOLTAGE',
            title: '电压上限',
            // width: (dialogDivWidth*0.2),
            width: 80,
            halign: "center",
            align: "center"
        }, {
            field: 'FMINVOLTAGE',
            title: '电压下限',
            // width: (dialogDivWidth*0.2),
            width: 80,
            halign: "center",
            align: "center"
        },{
            field: 'fjunction',
            title: '焊缝编号',
            // width: (dialogDivWidth*0.2),
            width: 80,
            halign: "center",
            align: "center",
            sortable: true
        }, {
            field: 'junction_length',
            title: '长度',
            // width: (dialogDivWidth*0.3),
            width: 60,
            halign: "center",
            align: "center"
        }, {
            field: 'junction_format',
            title: '规格',
            // width: (dialogDivWidth*0.3),
            width: 60,
            halign: "center",
            align: "center"
        }, {
            field: 'edit',
            title: '操作',
            // width: (dialogDivWidth*0.3),
            width: 150,
            halign: "center",
            align: "left",
            formatter: function (value, row, index) {
                var str = "";
                str += '<a id="edit" class="easyui-linkbutton" href="javascript:edit(' + row.fid + ')"/>';
                str += '<a id="delete" class="easyui-linkbutton" href="javascript:deleteJunction(' + row.fid + ')"/>';
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
function addJunction() {
    vlogoflag = "add";
    $('#fm').form('clear');
    $('#dlg').window({
        title: "新增焊缝",
        modal: true
    });
    $('#dlg').window('open');
    $('#fm').form('load', row);
}

//保存
function saveJunction() {
    var urls = "";
    var message = "";
    if (vlogoflag == "add") {
        message = "新增成功！";
        urls = "junction/addJunction";
    } else if (vlogoflag == "edit") {
        message = "修改成功！";
        urls = "junction/updateJunction";
    }
    if (urls != "") {
        $('#fm').form('submit', {
            url: urls,
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
                        $('#junctionTable').datagrid('reload');
                    }
                }

            },
            error: function (errorMsg) {
                alert("数据请求失败，请联系系统管理员!");
            }
        });
    }
    vlogoflag = "";
}

//编辑
function edit(fid) {
    vlogoflag = "edit";
    $('#fm').form('clear');
    var row = $('#junctionTable').datagrid('getSelected');
    if (row) {
        $('#dlg').window({
            title: "编辑",
            modal: true
        });
        $('#dlg').window('open');
        $('#fid').val(fid);
        $('#fjunction').textbox('setValue', row.fjunction);
        $('#junction_length').textbox('setValue', row.junction_length);
        $('#junction_format').textbox('setValue', row.junction_format);
        $('#current_limit').textbox('setValue', row.current_limit);
        $('#current_lower_limit').textbox('setValue', row.current_lower_limit);
        $('#junction_name').textbox('setValue', row.junction_name);
        $('#fm').form('load', row);
    }
}

//删除
function deleteJunction(fid) {
    $.messager.confirm('提示', '确认删除该焊缝信息?', function (flag) {
        if (flag) {
            $.ajax({
                type: "post",
                async: false,
                url: 'junction/deleteJunction',
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
                            $('#junctionTable').datagrid('reload');
                            $.messager.alert("提示", "删除成功！");
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
function searchJunction() {
    var junctionSearchs = $('#junction_search').textbox("getValue");          //焊缝编号
    var junction_name_searchs = $('#junction_name_search').textbox("getValue");    //焊缝名称

    var query = {junctionSearch: junctionSearchs, junction_name_search: junction_name_searchs}; //把查询条件拼接成JSON

    $("#junctionTable").datagrid('options').queryParams = query; //把查询条件赋值给datagrid内部变量
    $('#junctionTable').datagrid('reload');
}

//监听窗口大小变化
window.onresize = function () {
    setTimeout(domresize, 500);
}

//改变表格高宽
function domresize() {
    $("#junctionTable").datagrid('resize', {
        height: $("#body").height(),
        width: $("#body").width()
    });
}

