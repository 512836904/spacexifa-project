/**
 *
 */
var oldchanel = 0;
$(function () {
    //	rule();
    statusRadio();
    addWpslib();
    machineModel();
    getDictionary(10, "sxfselect");
    getDictionary(24, "sxfgas");
    getDictionary(23, "sxfdiameter");
    getDictionary(18, "sxfmaterial");
    getDictionary(19, "sxfcontroller");
    getDictionary(20, "sxfinitial");
    getDictionary(21, "sxfarc");

    $('#editSxDlg').dialog({
        onClose: function () {
            $('#sxfwpsnum').combobox('clear');
            $('#sxfselect').combobox('clear');
            $('#sxfgas').combobox('clear');
            $('#sxfdiameter').combobox('clear');
            $('#sxfmaterial').combobox('clear');
            $('#sxfinitial').combobox('clear');
            $('#sxfcontroller').combobox('clear');
            $("#sxfm").form("disableValidation");
        }
    })
    $('#smwdlg').dialog({
        onClose: function () {
            $('#mainWpsTable').datagrid('clearSelections');
        }
    })
    $('#smdlg').dialog({
        onClose: function () {
            $('#weldingmachineTable').datagrid('clearSelections');
        }
    })
    $('#sxSelectdlg').dialog({
        onClose: function () {
            $('#sxSelectWpsTab').datagrid('clearSelections');
        }
    })
    $('#sxMachinedlg').dialog({
        onClose: function () {
            $('#sxMachineTable').datagrid('clearSelections');
        }
    })
    $('#wltdlg').dialog({
        onClose: function () {
            $("#wltfm").form("disableValidation");
        }
    })
    $("#wltfm").form("disableValidation");
    $("#sxfm").form("disableValidation");
})

var url = "";
var flag = 1;

function addWpslib() {
    flag = 1;
    $('#wltfm').form('clear');
    $('#wltdlg').window({
        title: "新增工艺库",
        modal: true
    });
    $('#wltdlg').window('open');
    var statusId = document.getElementsByName("statusId");
    statusId[0].checked = 'checked';
    $('#model').combobox('enable');
    url = "wps/addWpslib";
}

function editWpslib() {
    flag = 2;
    $('#wltfm').form('clear');
    var row = $('#wpslibTable').datagrid('getSelected');
    if (row) {
        $('#wltdlg').window({
            title: "修改工艺库",
            modal: true
        });
        $('#wltdlg').window('open');
        $('#wltfm').form('load', row);
        $('#validwl').val(row.wpslibName);
        var statusId = document.getElementsByName("statusId");
        if (row.statusId == 61) {
            statusId[0].checked = 'checked';
        } else {
            statusId[1].checked = 'checked';
        }
        // $('#model').combobox('disable', true);//禁用单选按钮
        url = "wps/updateWpslib?fid=" + row.fid;
    }
}

function saveWpslib() {
    // var wpslibName = $('#wpslibName').val();
    var wpslibName = $("#wpslibName").textbox("getValue");
    var fstatus = $("input[name='statusId']:checked").val();
    var messager = "";
    var url2 = "";
    if (flag == 1) {
        var machineModel = $('#model').combobox('getValue');
        messager = "新增成功！";
        url2 = url + "?fstatus=" + fstatus + "&wpslibName=" + wpslibName + "&machineModel=" + encodeURI(machineModel);
    } else {
        messager = "修改成功！";
        url2 = url + "&fstatus=" + fstatus + "&wpslibName=" + wpslibName;
    }
    $('#wltfm').form('submit', {
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
                    $('#wltdlg').dialog('close');
                    $('#wpslibTable').datagrid('reload');
                    $("#validwl").val("");
                }
            }

        },
        error: function (errorMsg) {
            alert("数据请求失败，请联系系统管理员!11");
        }
    });
}

//工艺库状态
function statusRadio() {
    $.ajax({
        type: "post",
        async: false,
        url: "wps/getStatusAll",
        data: {},
        dataType: "json", //返回数据形式为json
        success: function (result) {
            if (result) {
                var str = "";
                for (var i = 0; i < result.ary.length; i++) {
                    str += "<input type='radio' class='radioStyle' name='statusId' id=\"" + "sId" + result.ary[i].id + "\" value=\"" + result.ary[i].id + "\" />"
                        + result.ary[i].name;
                }
                $("#radios").html(str);
                // $("input[name='statusId']").eq(0).attr("checked", true);     //这个会导致栈内存溢出
                var statusId = document.getElementsByName("statusId");
                statusId[0].checked = 'checked';
            }
        },
        error: function (errorMsg) {
            alert("数据请求失败，请联系系统管理员!22");
        }
    });
}

var mflag = 1;

//新增工艺
function addMainWps(row) {
    if (row.statusId == 61) {
        mflag = 1;
        var height = 600;
        //var src = 'wps/goWB_P500L';
        //给子页面的文本框赋值
        // function assgVal(){
        // 	var fatherText = $("#fatherText").val();
        // 	$('#son').contents().find("#sonId").val(fatherText);
        // }
        /* 引用子页面index1.html */
        //var hrefs = "<iframe id='son' src='"+src+"' allowTransparency='true' style='border:0;width:99%;height:99%;padding-left:2px;' frameBorder='0'></iframe>";
        // $("#centers").html(hrefs);
        // $('#wpsCraft').window('open');
        $('#fmwpsCraft').form('clear');
        wpsLibRule(row.modelname);
        if (row.modelname == 'DP500/CPVM500') {
            height = 300;
        }
        var arr1 = [];
        arr1 = $('#fchanel').combobox('getData');
        //查询对应焊机型号下的所有规范
        var arr2 = [];
        arr2 = findSpecificationByFid(row.fid);
        // console.log(arr1);
        // console.log(arr2);
        //求两个对象数组的差集
        let diff = [...arr1];
        for (let i = 0, len = arr1.length; i < len; i++) {
            let flag = false
            for (let j = 0, length = arr2.length; j < length; j++) {
                if (arr1[i].value === arr2[j].id) {
                    flag = true
                }
            }
            if (flag) {
                diff.splice(diff.find(item => item.id === arr1[i].value), 1);
            }
        }
        // console.log(diff);
        $('#fchanel').combobox('clear');
        $('#fchanel').combobox('loadData', diff);
        $('#fchanel').combobox('select', diff[0].value);

        //清空页面数据
        $("#modelname").val(row.model);
        $("#fid").val(row.fid);
        $("#fchanel").combobox('readonly', false);
        $('#wpsCraft').dialog("open");
        $('#wpsCraft').dialog({
            title: '新增工艺',
            width: 900,
            height: height,
            closed: false,
            cache: false,
            // href: src,
            content: '',
            modal: true,
            buttons: [
                {
                    text: '索取规范',
                    iconCls: 'icon-getwps',
                    handler: function () {
                        selectMachineList(0);
                    }
                }, {
                    text: '保存',
                    iconCls: 'icon-save',
                    handler: function () {
                        saveMainWps(row.modelname);
                    }
                }, {
                    text: '关闭',
                    iconCls: 'icon-no',
                    handler: function () {
                        // $("#wpsCraft").dialog('destroy');
                        $("#wpsCraft").dialog('close');
                    }
                }]
        });
    } else {
        alert("该工艺库已被停用！");
    }
}

function editMainWps(indexrow, row) {
    mflag = 2;
    wpsLibRule(row.modelname);
    $('#specification_id').val(indexrow.fid);
    $('#fmwpsCraft').form('load', indexrow);
    $("#fchanel").combobox('readonly', true);
    $('#wpsCraft').dialog("open");
    $('#wpsCraft').dialog({
        title: '修改工艺',
        width: 900,
        height: 600,
        closed: false,
        cache: false,
        // href: src,
        content: '',
        modal: true,
        buttons: [
            {
                text: '索取规范',
                iconCls: 'icon-getwps',
                handler: function () {
                    selectMachineList(0);
                }
            }, {
                text: '保存',
                iconCls: 'icon-save',
                handler: function () {
                    saveMainWps(row.modelname);
                }
            }, {
                text: '关闭',
                iconCls: 'icon-no',
                handler: function () {
                    // $("#wpsCraft").dialog('destroy');
                    $("#wpsCraft").dialog('close');
                }
            }]
    });
}

function saveMainWps(modelname) {
    //参数检测
    if (modelname == 'CPVE500') {
        if (CPVEWCHECK() == false) {
            return;
        }
    } else if (modelname == 'WB-P500L') {
        if (WBLCHECK() == false) {
            return;
        }
    } else if (modelname == 'DP500/CPVM500') {

    } else {
        return;
    }
    var wpsLibRow = $('#wpslibTable').datagrid('getSelected');
    var index = $('#wpslibTable').datagrid('getRowIndex', wpsLibRow);
    var messager = "";
    if (mflag == 1) {
        messager = "新增成功！";
        $("#addORupdate").val("add");
    } else {
        messager = "修改成功！";
        $("#addORupdate").val("update");
    }
    if ($('#fchanel').combobox("getValue") == null || $('#fchanel').combobox("getValue") == '') {
        alert("请选择通道号");
        return;
    }
    $('#fmwpsCraft').form('submit', {
        url: 'wps/apSpe',
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
                    oldchanel = 0;
                } else {
                    $.messager.alert("提示", messager);
                    $('#wpsCraft').dialog('close');
                    $('#ddv-' + index).datagrid('reload');
                    oldchanel = 0;
                }
            }
        },
        error: function (errorMsg) {
            alert("数据请求失败，请联系系统管理员!");
        }
    });
}

//根据工艺库id查询对应的通道号规范（一个焊机型号只能有一个通道号）
function findSpecificationByFid(wpslibid) {
    var array = [];
    $.ajax({
        type: "post",
        async: false,
        url: "wps/findSpecificationByFid?",
        data: {
            'wpslibid': wpslibid
        },
        dataType: "json", //返回数据形式为json
        success: function (result) {
            var data = result.ary;
            if (data.length > 0) {
                for (var index in data) {
                    let modelItem = {};
                    modelItem["id"] = data[index].fspe_num.toString();
                    array[index] = modelItem;
                }
            }
        },
        error: function (errorMsg) {
            alert("数据请求失败，请联系系统管理员!");
        }
    });
    return array;
}

//根据焊机型号判断页面显示元素
function wpsLibRule(modelname) {
    //焊机型号：Cpve500
    if (modelname === 'CPVE500') {
        $("#dtorch").hide();
        $("#itorch").hide();
        $("#dfrequency").hide();
        $("#ifrequency").hide();
        $("#cwivo").hide();
        $("#cwtivo").hide();
        $("#tcontroller").show();
        $("#rcontroller").show();
        $("#dfweldprocess").show();
        $("#rfweldprocess").show();
        $("#dgas").show();
        $("#rgas").show();
        $("#dmaterial").show();
        $("#rmaterial").show();
        $("#cwwvo").hide();
        $("#cwtwvo").hide();
        $("#cwwvto").hide();
        $("#cwtwvto").hide();
        $("#dmodel").show();
        $("#imodel").show();
        $("#hide1").show();
        $("#hide2").show();
        $("#hide3").show();
        $("#hide4").show();
        $("#hide5").show();
        $("#hide6").show();
        $("#hide7").show();
        $("#individual_2").show();
        $("#cwwv").show();
        $("#cwtwv").show();
        $("#cwwvt").show();
        $("#cwtwvt").show();
        $("#cwiv").show();
        $("#cwtiv").show();
        $("#cwav").show();
        $("#cwtav").show();
        $("#cwavo").hide();
        $("#cwtavo").hide();
        $("#cwavto").hide();
        $("#cwtavto").hide();
        CPVEWINITwps();		//参数初始化
        CPVEWRULE();		//选择框参数规则
    } else if (modelname == 'DP500/CPVM500') {
        $("#tcontroller").hide();
        $("#rcontroller").hide();
        $("#dfweldprocess").hide();
        $("#rfweldprocess").hide();
        $("#dtorch").hide();
        $("#itorch").hide();
        $("#dgas").hide();
        $("#rgas").hide();
        $("#dmaterial").hide();
        $("#rmaterial").hide();
        $("#hide1").hide();
        $("#cwwvo").hide();
        $("#cwtwvo").hide();
        $("#cwwvto").hide();
        $("#cwtwvto").hide();
        $("#hide2").hide();
        $("#hide3").hide();
        $("#hide4").hide();
        $("#hide5").hide();
        $("#hide6").hide();
        $("#hide7").hide();
        $("#cwavo").hide();
        $("#cwtavo").hide();
        $("#cwavto").hide();
        $("#cwtavto").hide();
        $("#individual_2").hide();
        $('#fchanel').combobox('clear');
        // var str = "";
        // for (var i = 1; i < 101; i++) {
        //     str += '<option value="' + i + '">通道号' + i + '</option>';
        // }
        var fchanels = [];
        for (var i = 0; i < 100; i++) {
            let fchanel = {};
            fchanel["value"] = "" + (i + 1);
            fchanel["text"] = "通道号" + (i + 1);
            fchanels[i] = fchanel;
        }
        $('#fchanel').combobox('loadData', fchanels);
        // $('#fchanel').append(str);
        // $('#fchanel').combobox();
        // $('#fchanel').combobox('select', "1");
    } else if (modelname == "WB-P500L") {
        $("#tcontroller").show();
        $("#rcontroller").show();
        $("#dfweldprocess").show();
        $("#rfweldprocess").show();
        $("#dtorch").show();
        $("#itorch").show();
        $("#dgas").show();
        $("#rgas").show();
        $("#dmaterial").show();
        $("#rmaterial").show();
        $("#hide1").show();
        $("#hide2").show();
        $("#hide3").show();
        $("#hide4").show();
        $("#hide5").show();
        $("#hide6").show();
        $("#hide7").show();
        $("#dfrequency").show();
        $("#ifrequency").show();
        $("#cwwvo").hide();
        $("#cwtwvo").hide();
        $("#cwwvto").hide();
        $("#cwtwvto").hide();
        $("#cwivo").hide();
        $("#cwtivo").hide();
        $("#dmodel").hide();
        $("#imodel").hide();
        $("#cwavo").hide();
        $("#cwtavo").hide();
        $("#cwavto").hide();
        $("#cwtavto").hide();
        $("#individual_2").show();
        WBLINITwps();	//参数初始化
        WBLRULEwps();	//选择框参数规则
    }
}

function rule() {
    $("#farc").combobox({
        onSelect: function (record) {
            if (record.value == 111) {
                $('#farc_ele').numberbox("disable", true);
                $('#farc_vol').numberbox("disable", true);
                $('#farc_tuny_ele').numberbox("disable", true);
                $('#farc_tuny_vol').numberbox("disable", true);
                $('#farc_tuny_vol1').numberbox("disable", true);
                $('#farc_vol1').numberbox("disable", true);
                $('#ftime').numberbox("disable", true);
                $('#fini_ele').numberbox("disable", true);
                $('#fini_vol').numberbox("disable", true);
                $('#fini_vol1').numberbox("disable", true);
            } else if (record.value == 112) {
                $('#farc_ele').numberbox("enable", true);
                $('#farc_vol').numberbox("enable", true);
                $('#farc_tuny_ele').numberbox("enable", true);
                $('#farc_tuny_vol').numberbox("enable", true);
                $('#farc_tuny_vol1').numberbox("enable", true);
                $('#farc_vol1').numberbox("enable", true);
                $('#ftime').numberbox("disable", true);
                if ($("#finitial").is(":checked")) {
                    $('#fini_ele').numberbox("enable", true);
                    $('#fini_vol').numberbox("enable", true);
                    $('#fini_vol1').numberbox("enable", true);
                } else {
                    $('#fini_ele').numberbox("disable", true);
                    $('#fini_vol').numberbox("disable", true);
                    $('#fini_vol1').numberbox("disable", true);
                }
            } else if (record.value == 113) {
                $('#farc_ele').numberbox("enable", true);
                $('#farc_vol').numberbox("enable", true);
                $('#farc_tuny_ele').numberbox("enable", true);
                $('#farc_tuny_vol').numberbox("enable", true);
                $('#farc_tuny_vol1').numberbox("enable", true);
                $('#farc_vol1').numberbox("enable", true);
                $('#ftime').numberbox("disable", true);
                if ($("#finitial").is(":checked")) {
                    $('#fini_ele').numberbox("enable", true);
                    $('#fini_vol').numberbox("enable", true);
                    $('#fini_vol1').numberbox("enable", true);
                } else {
                    $('#fini_ele').numberbox("disable", true);
                    $('#fini_vol').numberbox("disable", true);
                    $('#fini_vol1').numberbox("disable", true);
                }
            } else {
                $('#farc_ele').numberbox("disable", true);
                $('#farc_vol').numberbox("disable", true);
                $('#farc_tuny_ele').numberbox("disable", true);
                $('#farc_tuny_vol').numberbox("disable", true);
                $('#farc_tuny_vol1').numberbox("disable", true);
                $('#farc_vol1').numberbox("disable", true);
                $('#fini_ele').numberbox("disable", true);
                $('#fini_vol').numberbox("disable", true);
                $('#fini_vol1').numberbox("disable", true);
                $('#ftime').numberbox("enable", true);
                $('#ftime').numberbox("enable", true);
            }
        }
    });

    $("#finitial").click(function () {
        if ($("#finitial").is(":checked")) {
            if ($('#farc').combobox('getValue') == 112 || $('#farc').combobox('getValue') == 113) {
                $('#fini_ele').numberbox("enable", true);
                $('#fini_vol').numberbox("enable", true);
                $('#fini_vol1').numberbox("enable", true);
            } else {
                $('#fini_ele').numberbox("disable", true);
                $('#fini_vol').numberbox("disable", true);
                $('#fini_vol1').numberbox("disable", true);
            }
        } else {
            $('#fini_ele').numberbox("disable", true);
            $('#fini_vol').numberbox("disable", true);
            $('#fini_vol1').numberbox("disable", true);
        }
    });

    $("#fmaterial").combobox({
        onSelect: function (record) {
            if (record.value == 91) {
                $('#fgas').combobox('clear');
                $('#fgas').combobox('loadData', [{
                    "text": "CO2",
                    "value": "121"
                }, {
                    "text": "MAG",
                    "value": "122"
                }]);
                $('#fdiameter').combobox('clear');
                $('#fdiameter').combobox('loadData', [{
                    "text": "Φ1.0",
                    "value": "131"
                }, {
                    "text": "Φ1.2",
                    "value": "132"
                }, {
                    "text": "Φ1.4",
                    "value": "133"
                }, {
                    "text": "Φ1.6",
                    "value": "134"
                }]);
            } else if (record.value == 92) {
                $('#fgas').combobox('clear');
                $('#fgas').combobox('loadData', [{
                    "text": "MIG",
                    "value": "123"
                }]);
                $('#fdiameter').combobox('clear');
                $('#fdiameter').combobox('loadData', [{
                    "text": "Φ1.2",
                    "value": "132"
                }, {
                    "text": "Φ1.6",
                    "value": "134"
                }]);
            } else if (record.value == 93) {
                $('#fgas').combobox('clear');
                $('#fgas').combobox('loadData', [{
                    "text": "CO2",
                    "value": "121"
                }]);
                $('#fdiameter').combobox('clear');
                $('#fdiameter').combobox('loadData', [{
                    "text": "Φ1.2",
                    "value": "132"
                }, {
                    "text": "Φ1.4",
                    "value": "133"
                }, {
                    "text": "Φ1.6",
                    "value": "134"
                }]);
            } else {
                $('#fgas').combobox('clear');
                $('#fgas').combobox('loadData', [{
                    "text": "CO2",
                    "value": "121"
                }]);
                $('#fdiameter').combobox('clear');
                $('#fdiameter').combobox('loadData', [{
                    "text": "Φ1.2",
                    "value": "132"
                }, {
                    "text": "Φ1.6",
                    "value": "134"
                }]);
            }
            var fgas = $('#fgas').combobox('getData');
            var fdiameter = $('#fdiameter').combobox('getData');
            $('#fgas').combobox('select', fgas[0].value);
            $('#fdiameter').combobox('select', fdiameter[0].value);
        }
    });
}

function machineModel() {
    $.ajax({
        type: "post",
        async: false,
        url: "Dictionary/getValueByTypeid?type=" + 17,
        data: {},
        dataType: "json", //返回数据形式为json
        success: function (result) {
            if (result) {
                if (result.ary.length != 0) {
                    var boptionStr = '';
                    for (var i = 0; i < result.ary.length; i++) {
                        boptionStr += "<option value=\"" + result.ary[i].value + "\" >"
                            + result.ary[i].name + "</option>";
                    }
                    $("#model").html(boptionStr);
                    $("#model").combobox();
                    $("#model").combobox('select', result.ary[0].value);
                }
            }
        },
        error: function (errorMsg) {
            alert("数据请求失败，请联系系统管理员!");
        }
    });
}

function saveSxWps() {
    if (checkSxWps() == false) {
        return;
    }
    ;
    var wpsLibRow = $('#wpslibTable').datagrid('getSelected');
    var index = $('#wpslibTable').datagrid('getRowIndex', wpsLibRow);
    var messager = "";
    var url2 = "";
    if (mflag == 1) {
        messager = "新增成功！";
        url2 = url;
    } else {
        messager = "修改成功！";
        url2 = url;
    }
    $('#sxfm').form('submit', {
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
                    $('#ddv-' + index).datagrid('reload');
                    $.messager.alert("提示", messager);
                    $('#editSxDlg').dialog('close');
//					$('#wpslibTable').datagrid('reload');
                }
            }

        },
        error: function (errorMsg) {
            alert("数据请求失败，请联系系统管理员!");
        }
    });
}

function getDictionary(typeid, id) {
    $.ajax({
        type: "post",
        async: false,
        url: "wps/getDictionary?typeid=" + typeid,
        data: {},
        dataType: "json", //返回数据形式为json
        success: function (result) {
            if (result) {
                var optionStr = '';
                for (var i = 0; i < result.ary.length; i++) {
                    optionStr += "<option value=\"" + result.ary[i].id + "\" >"
                        + result.ary[i].name + "</option>";
                }
                $("#" + id).html(optionStr);
            }
        },
        error: function (errorMsg) {
            alert("数据请求失败，请联系系统管理员!");
        }
    });
    $("#" + id).combobox();
}

function CPVEWRULE() {
    //一元/个别
    $("#fselect").combobox({
        onChange: function (record) {
            if (record == 102) {
                $("#cwwvo").hide();
                $("#cwtwvo").hide();
                $("#cwwvto").hide();
                $("#cwtwvto").hide();
                $("#cwivo").hide();
                $("#cwtivo").hide();
                $("#cwavo").hide();
                $("#cwtavo").hide();
                $("#cwavto").hide();
                $("#cwtavto").hide();
                $("#cwwv").show();
                $("#cwtwv").show();
                $("#cwwvt").show();
                $("#cwtwvt").show();
                $("#cwiv").show();
                $("#cwtiv").show();
                $("#cwav").show();
                $("#cwtav").show();
                $("#cwavt").show();
                $("#cwtavt").show();
            } else {
                $("#cwwvo").show();
                $("#cwtwvo").show();
                $("#cwwvto").show();
                $("#cwtwvto").show();
                $("#cwivo").show();
                $("#cwtivo").show();
                $("#cwavo").show();
                $("#cwtavo").show();
                $("#cwavto").show();
                $("#cwtavto").show();
                $("#cwwv").hide();
                $("#cwtwv").hide();
                $("#cwwvt").hide();
                $("#cwtwvt").hide();
                $("#cwiv").hide();
                $("#cwtiv").hide();
                $("#cwav").hide();
                $("#cwtav").hide();
                $("#cwavt").hide();
                $("#cwtavt").hide();
            }
        }
    });
    //气体
    $("#fgas").combobox({
        onChange: function () {
            var fmaterial = $("#fmaterial").combobox('getValue');
            var fgas = $("#fgas").combobox('getValue');
            if (fmaterial == 91) {//低碳钢实芯
                fdiameter_5();
            } else if (fmaterial == 93) {
                fdiameter_3();
            } else if (fmaterial == 92 || fmaterial == 94) {
                fdiameter_4();
            }
            var data = $('#fdiameter').combobox('getData');
            $('#fdiameter').combobox('select', data[0].value);
        }
    });
    //焊丝材质
    $('#fmaterial').combobox({
        onChange: function () {
            var fmaterial = $('#fmaterial').combobox('getValue');
            if (fmaterial == 91) {
                fgas_1();
            } else if (fmaterial == 92) {
                $('#fgas').combobox('clear');
                $('#fgas').combobox('loadData', [{
                    "text": "MIG_2O2",
                    "value": "124"
                }]);
            } else if (fmaterial == 93) {
                $('#fgas').combobox('clear');
                $('#fgas').combobox('loadData', [{
                    "text": "CO2",
                    "value": "121"
                }]);
            } else if (fmaterial == 94) {
                $('#fgas').combobox('clear');
                $('#fgas').combobox('loadData', [{
                    "text": "CO2",
                    "value": "121"
                }]);
            }
            var data = $('#fgas').combobox('getData');
            $('#fgas').combobox('select', data[0].value);
        }
    });
}