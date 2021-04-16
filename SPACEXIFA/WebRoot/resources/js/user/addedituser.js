$(function () {
    statusRadio();
    inscombobox();
    $('#dlg').dialog({
        onClose: function () {
            $('#insid').combobox('clear');
            $("#fm").form("disableValidation");
        }
    })
    $("#fm").form("disableValidation");
    $('#ISSUEWPS').combobox('clear');
    $('#ISSUEWPS').combobox('loadData', [{
        "text": "无",
        "value": "0"
    }, {
        "text": "有",
        "value": "1"
    }]);
    $('#RECEIVEALARM').combobox('clear');
    $('#RECEIVEALARM').combobox('loadData', [{
        "text": "无",
        "value": "0"
    }, {
        "text": "有",
        "value": "1"
    }]);
});

function inscombobox() {
    $.ajax({
        type: "post",
        url: "user/getIns",
        dataType: "json",
        data: {},
        success: function (result) {
            if (result) {
                var optionstring = "", soptionstring = '';
                soptionstring += "<option value=''>请选择</option>";
                //循环遍历 下拉框绑定
                for (var k = 0; k < result.rows.length; k++) {
                    optionstring += "<option value=\"" + result.rows[k].insid + "\" >" + result.rows[k].insname + "</option>";
                    soptionstring += "<option value=\"" + result.rows[k].insid + "\" >" + result.rows[k].insname + "</option>";
                }
                $("#insid").html(optionstring);
                $("#insid").combobox();
                $("#sinsid").html(soptionstring);
                $("#sinsid").combobox();
            }
        },
        error: function () {
            alert('error');
        }
    });
    // $('#ISSUEWPS').combobox({
    //     valueField: 'id',
    //     textField: 'text',
    //     multiple: false,  //多选
    //     editable: false,  //是否可编辑
    //     panelHeight: 'auto',
    //     data: [{id: 0, text: '无'}, {id: 1, text: '有'}]//设置默认值
    // });
    // $('#RECEIVEALARM').combobox({
    //     valueField: 'id',
    //     textField: 'text',
    //     multiple: false,  //多选
    //     editable: false,  //是否可编辑
    //     panelHeight: 'auto',
    //     data: [{id: 0, text: '无'}, {id: 1, text: '有'}]//设置默认值
    // });
}

function UserDatagrid() {
    var urls = "";
    var row = $('#dg').datagrid('getSelected');
    if (flag == 1) {
        urls = "user/getAllRole";
    } else {
        urls = "user/getAllRole1?id=" + row.id;
    }
    $("#tt").datagrid({
        fitColumns: true,
        height: '280px',
        width: '80%',
        idField: 'roles_name',
        url: urls,
        rownumbers: false,
        showPageList: false,
        checkOnSelect: true,
        selectOnCheck: true,
        columns: [[{
            field: 'ck',
            checkbox: true
        }, {
            field: 'id',
            title: 'id',
            width: 100,
            halign: "center",
            align: "left",
            hidden: true
        }, {
            field: 'roles_name',
            title: '角色名',
            width: 100,
            halign: "center",
            align: "left"
        }]],
        rowStyler: function (index, row) {
            if ((index % 2) != 0) {
                //处理行代背景色后无法选中
                var color = new Object();
                return color;
            }
        },
        onBeforeLoad: function (data) {
            $('#tt').datagrid('clearChecked');
        },
        onLoadSuccess: function (data) {
            if (data) {
                $.each(data.rows, function (index, item) {
                    if (item.symbol == 1) {
                        $('#tt').datagrid('checkRow', index);
                    }
                })
            }
        }
    });
}

var url = "";
var flag = 1;

function saveUser() {
    flag = 1;
    $("#fm").form("disableValidation");
    $('#dlg').window({
        title: "新增用户",
        modal: true
    });
    $('#dlg').window('open');
    $('#fm').form('clear');
    $('#ISSUEWPS').combobox('select',0);
    $('#RECEIVEALARM').combobox('select',0);
    UserDatagrid();
    var statusId = document.getElementsByName("statusid");
    statusId[0].checked = 'checked';
    url = "user/addAllUser";
}

function editUser() {
    flag = 2;
    $('#fm').form('clear');
    var row = $('#dg').datagrid('getSelected');
    if (row) {
        $('#dlg').window({
            title: "修改用户",
            modal: true
        });
        $('#dlg').window('open');
        UserDatagrid();
        $('#fm').form('load', row);
        $('#validName').val(row.userLoginName);
        url = "user/updateUser?uid=" + row.id;
    }
}

//提交
function save() {
    var insframework = $("#insid").combobox("getValue");
    var sid = $("input[name='statusid']:checked").val();
    var rows = $("#tt").datagrid("getSelections");
    var receivealarm = $("#RECEIVEALARM").combobox("getValue");
    var issuewps = $("#ISSUEWPS").combobox("getValue");
    var pssword = $("input[name='userPassword']").val();
    var name = $("input[name='userName']").val();
    var validName = $("input[name='userLoginName']").val();
    var userPosition = $("input[name='userPosition']").val();
    var userEmail = $("input[name='userEmail']").val();
    var userPhone = $("input[name='userPhone']").val();
    var str = "";
    for (var i = 0; i < rows.length; i++) {
        str += rows[i].id + ",";
    }
    var url2 = "";
    if (flag == 1) {
        messager = "新增成功！";
        url2 = url + "?userInsframework=" + insframework + "&status=" + sid + "&rid=" + str+"&receivealarm=" + receivealarm+"&issuewps=" + issuewps+"&pssword=" + pssword+"&name=" + name+
            "&validName=" + validName+"&userPosition=" + userPosition+"&userEmail=" + userEmail+"&userPhone=" + userPhone;
    } else {
        messager = "修改成功！";
        url2 = url + "&userInsframework=" + insframework + "&status=" + sid + "&rid=" + str;
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
                    $('#dg').datagrid('reload');
                    $("#dg").datagrid('clearSelections');
                }
            }

        },
        error: function (errorMsg) {
            alert("数据请求失败，请联系系统管理员!");
        }
    });
}

function statusRadio() {
    $.ajax({
        type: "post",
        async: false,
        url: "user/getStatusAll",
        data: {},
        dataType: "json", //返回数据形式为json
        success: function (result) {
            if (result) {
                var str = "", sstr = "";
                for (var i = 0; i < result.ary.length; i++) {
                    str += "<input type='radio' class='radioStyle' name='statusid' id='sId' value=\"" + result.ary[i].id + "\" />"
                        + result.ary[i].name + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                    sstr += "<input type='radio' class='radioStyle' name='sstatusid' id='ssId' value=\"" + result.ary[i].id + "\" />"
                        + result.ary[i].name + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                }
                $("#radios").html(str);
                $("#sradios").html(sstr);
            }
        },
        error: function (errorMsg) {
            alert("数据请求失败，请联系系统管理员!");
        }
    });
}