$(function () {
    insframworkCombobox();
    leveCombobox();
    quaidCombobox();
    loadWelderStatus();
    $('#dlg').dialog({
        onClose: function () {
            $('#leveid').combobox('clear');
            $('#quali').combobox('clear');
            $('#owner').combobox('clear');
            $('#fstatus').combobox('clear');
            $('#DIMISSIONSTATUS').combobox('clear');
            $("#fm").form("disableValidation");
        }
    })
    $("#fm").form("disableValidation");
    $("#welderno").textbox('textbox').blur(function () {
        var welderno = $("#welderno").val();
        var len = welderno.length;
        if (welderno != "" && len < 8) {
            for (var i = 0; i < 8 - len; i++) {
                welderno = "0" + welderno;
            }
            $("#welderno").textbox('setValue', welderno);
        }
    });
})

function insframworkCombobox() {
    $.ajax({
        type: "post",
        async: false,
        url: "weldingMachine/getInsframeworkAll",
        dataType: "json",
        data: {},
        success: function (result) {
            if (result) {
                var optionstring = "", soptionstring = '';
                soptionstring += "<option value=''>请选择</option>";
                //循环遍历 下拉框绑定
                for (var i = 0; i < result.ary.length; i++) {
                    optionstring += "<option value=\"" + result.ary[i].id + "\" >" + result.ary[i].name + "</option>";
                    soptionstring += "<option value=\"" + result.ary[i].id + "\" >" + result.ary[i].name + "</option>";
                }
                $("#owner").html(optionstring);
                $("#sowner").html(soptionstring);
            } else {
                alert('部门加载失败');
            }
            $("#owner").combobox();
            $("#sowner").combobox();
        },
        error: function () {
            alert('error');
        }
    });

}

function leveCombobox() {
    var we = 8;
    $.ajax({
        type: "post",
        async: false,
        url: "welders/getLeve?we=" + we,
        dataType: "json",
        data: {},
        success: function (result) {
            if (result) {
                var optionstring = "", soptionstring = '';
                soptionstring += "<option value=''>请选择</option>";
                //循环遍历 下拉框绑定
                for (var k = 0; k < result.rows.length; k++) {
                    optionstring += "<option value=\"" + result.rows[k].leveid + "\" >" + result.rows[k].levename + "</option>";
                    soptionstring += "<option value=\"" + result.rows[k].leveid + "\" >" + result.rows[k].levename + "</option>";
                }
                $("#leveid").html(optionstring);
                $("#sleveid").html(soptionstring);
            } else {
                alert('数据加载失败');
            }
            $("#leveid").combobox();
            $("#sleveid").combobox();
        },
        error: function () {
            alert('error');
        }
    });
}

function quaidCombobox() {
    var wee = 7;
    $.ajax({
        type: "post",
        async: false,
        url: "welders/getLeve?we=" + wee,
        dataType: "json",
        data: {},
        success: function (result) {
            if (result) {
                var optionstring = "", soptionstring = '';
                soptionstring += "<option value=''>请选择</option>";
                //循环遍历 下拉框绑定
                for (var k = 0; k < result.rows.length; k++) {
                    optionstring += "<option value=\"" + result.rows[k].quaid + "\" >" + result.rows[k].quaname + "</option>";
                    soptionstring += "<option value=\"" + result.rows[k].quaid + "\" >" + result.rows[k].quaname + "</option>";
                }
                $("#quali").html(optionstring);
                $("#squali").html(soptionstring);
            } else {
                alert('数据加载失败');
            }
            $("#quali").combobox();
            $("#squali").combobox();
        },
        error: function () {
            alert('error');
        }
    });
}

//查询焊工状态
function loadWelderStatus(){
    $.ajax({
        type: "post",
        async: false,
        url: "welders/loadWelderStatus",
        dataType: "json",
        data: {},
        success: function (result) {
            if (result) {
                var data = result.rows;
                var welderstatus = "";
                //循环遍历 下拉框绑定
                for (var index in data) {
                    welderstatus += "<option value=\"" + data[index].value + "\" >" + data[index].valueName + "</option>";
                }
                $("#fstatus").html(welderstatus);
            }
            $("#fstatus").combobox();
        },
        error: function () {
            alert('error');
        }
    });
    //焊工离职状态
    $("#DIMISSIONSTATUS").combobox({
        valueField: 'id',
        textField: 'text',
        multiple: false,  //多选
        editable: false,  //是否可编辑
        panelHeight: 'auto',
        data: [{id: 1, text: '在职',selected:true}, {id: 0, text: '已离职'}]//设置默认值
    });

}

var url = "";
var flag = 1;

function saveWelder() {
    flag = 1;
    $('#dlg').window({
        title: "新增焊工",
        modal: true
    });
    $('#dlg').window('open');
    $('#fm').form('clear');
    url = "welders/addWelder";
}

function editWelder() {
    flag = 2;
    $('#fm').form('clear');
    var row = $('#welderTable').datagrid('getSelected');
    if (row) {
        $('#dlg').window({
            title: "修改焊工",
            modal: true
        });
        $('#dlg').window('open');
        $('#fm').form('load', row);
        $('#validName').val(row.welderno);
        url = "welders/updateWelder?FID=" + row.id;
    } else {
        alert("请先选择一条数据。");
        return;
    }
}

//提交
function save() {
    var welderno = $("#welderno").val();
    var len = welderno.length;
    if (len > 9) {
        alert("焊工编号超出指定的9位长度!!!");
        return;
    }
    if (welderno == "000000000") {
        alert("焊工编号不能全为0!!!");
        return;
    }
    var insframework = $('#owner').combobox('getValue');
    var leve = $('#leveid').combobox('getValue');
    var qua = $('#quali').combobox('getValue');
    var url2 = "";
    if (flag == 1) {
        messager = "新增成功！";
        url2 = url + "?ins=" + insframework + "&leve=" + leve + "&qua=" + qua;
    } else {
        messager = "修改成功！";
        url2 = url + "&ins=" + insframework + "&leve=" + leve + "&qua=" + qua;
    }
    $('#fm').form('submit', {
        url: url2,
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
                    $.messager.alert("提示", messager);
                    $('#dlg').dialog('close');
                    $('#welderTable').datagrid('reload');
                    $("#welderTable").datagrid('clearSelections');
                }
            } else {
                $('#fm').submit();
            }
            numversion();
        },
        error: function (errorMsg) {
            alert("数据请求失败，请联系系统管理员!");
        }
    });
}

        