/**
 *
 */
// var oldchanel = 0, wpsId = "", employeeId = "", stepId = "";
$(function () {
    $('#addOrUpdate').dialog({
        onClose: function () {
            $("#addOrUpdatefm").form("disableValidation");
        }
    })
    $("#addOrUpdatefm").form("disableValidation");
    $.extend($.fn.datagrid.methods, {
        editCell: function (jq, param) {
            return jq.each(function () {
                var opts = $(this).datagrid('options');
                var fields = $(this).datagrid('getColumnFields', true).concat($(this).datagrid('getColumnFields'));
                for (var i = 0; i < fields.length; i++) {
                    var col = $(this).datagrid('getColumnOption', fields[i]);
                    col.editor1 = col.editor;
                    if (fields[i] != param.field) {
                        col.editor = null;
                    }
                }
                $(this).datagrid('beginEdit', param.index);
                for (var i = 0; i < fields.length; i++) {
                    var col = $(this).datagrid('getColumnOption', fields[i]);
                    col.editor = col.editor1;
                }
            });
        }
    });
    // initTables();
//	getDictionary(10,"sxfselect");
})

// function initTables(){
// 	$("#femployeeTable").datagrid( {
// 		fitColumns : true,
// 		height : $("#addOrUpdate").height()*0.4,
// 		width : $("#addOrUpdate").width()*0.64*0.545,
// 		idField : 'fid',
// //		pageSize : 10,
// //		pageList : [ 10, 20, 30, 40, 50 ],
// 		url : employeeUrl,
// 		singleSelect : true,
// 		rownumbers : true,
// //		showPageList : false,
// 		nowrap : false,
// 		columns : [ [ {
// 			field : 'fid',
// 			title : '序号',
// //			width : 30,
// 			halign : "center",
// 			align : "left",
// 			hidden:true
// 		}, {
// 			field : 'femployee_id',
// 			title : '工序号',
// 			width : 100,
// //			halign : "center",
// 			align : "center",
// 			editor:'text'
// 		}, {
// 			field : 'femployee_version',
// 			title : '工序版本',
// 			width : 100,
// //			halign : "center",
// 			align : "center",
// 			editor:'text'
// 		}, {
// 			field : 'femployee_name',
// 			title : '工序名称',
// 			width : 100,
// //			halign : "center",
// 			align : "center",
// 			editor:'text'
// 		}] ],
// //		pagination : true,
// 		rowStyler: function(index,row){
//             if ((index % 2)!=0){
//             	//处理行代背景色后无法选中
//             	var color=new Object();
//                 return color;
//             }
//         },
// 		onLoadSuccess: function(data){
// 			if(data.rows.length!=0){
// 				$('#femployeeTable').datagrid('selectRow', 0);
// 			}
// 		},
// 		onSelect: function(rowIndex, rowData){
// 			if(rowData.fid){
// 				employeeId = rowData.fid;
// 			}else{
// 				employeeId = "";
// 			}
// 			stepUrl = "wps/getInfo?search=" + rowData.fid +"&valueFlag=1";
// 			$('#fstepTable').datagrid("options").url=stepUrl;
// 			$('#fstepTable').datagrid('reload');
// 		},
// 		onDblClickCell: function(index,field,value){
// 			if (endEditing(0)){
// //				$('#femployeeTable').datagrid('selectRow', index)
// //						.datagrid('editCell', {index:index,field:field});
// 				$(this).datagrid('beginEdit', index);
// 				var ed = $(this).datagrid('getEditor', {index:index,field:field});
// 				$(ed.target).focus();
// 				editIndex = index;
// 			}
// 		}
// //	    onAfterEdit: onAfterEdit
// 	});
//
// 	$("#fstepTable").datagrid( {
// 		height : $("#addOrUpdate").height()*0.4,
// 		width : $("#addOrUpdate").width()*0.64*0.58,
// 		idField : 'fid',
// //		pageSize : 10,
// //		pageList : [ 10, 20, 30, 40, 50 ],
// 		url : stepUrl,
// //		singleSelect : true,
// 		rownumbers : true,
// //		showPageList : false,
// 		fitColumns : true,
// 		columns : [ [ {
// 			field : 'fid',
// 			title : '序号',
// //			width : 30,
// 			halign : "center",
// 			align : "left",
// 			hidden:true
// 		}, {
// 			field : 'fstep_number',
// 			title : '工步号',
// 			width : 100,
// 			halign : "center",
// 			align : "left",
// 			editor:'text'
// 		}, {
// 			field : 'fstep_name',
// 			title : '工步名称',
// 			width : 100,
// 			halign : "center",
// 			align : "left",
// 			editor:'text'
// 		}, {
// 			field : 'fstep_version',
// 			title : '工步版本',
// 			width : 100,
// //			halign : "center",
// 			align : "center",
// 			editor:'text'
// 		}] ],
// //		pagination : true,
// 		rowStyler: function(index,row){
//             if ((index % 2)!=0){
//             	//处理行代背景色后无法选中
//             	var color=new Object();
//                 return color;
//             }
//         },
// 		onLoadSuccess: function(data){
// 			if(data.rows.length!=0){
// 				$('#fstepTable').datagrid('selectRow', 0);
// 			}
// 		},
// 		onSelect: function(rowIndex, rowData){
// 			if(rowData.fid){
// 				stepId = rowData.fid;
// 			}else{
// 				stepId = "";
// 			}
// 			junctionUrl = "wps/getInfo?search=" + rowData.fid +"&valueFlag=2";
// 			$('#fjunctionTable').datagrid("options").url=junctionUrl;
// 			$('#fjunctionTable').datagrid('reload');
// 			detailUrl = "wps/getInfo?search=" + rowData.fid +"&valueFlag=3";
// 			$('#wpsDetailTable').datagrid("options").url=detailUrl;
// 			$('#wpsDetailTable').datagrid('reload');
// 		},
// 		onDblClickCell: function(index,field,value){
// 			if (endEditing(1)){
// //				$('#femployeeTable').datagrid('selectRow', index)
// //						.datagrid('editCell', {index:index,field:field});
// 				$(this).datagrid('beginEdit', index);
// 				var ed = $(this).datagrid('getEditor', {index:index,field:field});
// 				$(ed.target).focus();
// 				editIndex = index;
// 			}
// 		}
// 	});
//
// 	$("#fjunctionTable").datagrid( {
// 		height : $("#addOrUpdate").height()*0.4,
// 		width : $("#addOrUpdate").width()*0.64*0.359,
// 		idField : 'fid',
// //		pageSize : 10,
// //		pageList : [ 10, 20, 30, 40, 50 ],
// 		url : "/",
// //		singleSelect : true,
// 		rownumbers : true,
// //		showPageList : false,
// 		fitColumns : true,
// 		selectOnCheck : false,
// 		checkOnSelect : false,
// 		columns : [ [ {
// 			field : 'fid',
// 			title : '序号',
// //			width : 30,
// 			halign : "center",
// 			align : "left",
// 			hidden:true
// 		}, {
// 		    field:'ck',
// 			checkbox:true
// 		}, {
// 			field : 'fjunction',
// 			title : '焊缝编号',
// 			width : 100,
// 			halign : "center",
// 			align : "left",
// 			editor:'text'
// 		}, {
// 			field : 'fwelding_area',
// 			title : '焊接部位',
// 			width : 100,
// 			halign : "center",
// 			align : "left",
// 			editor:'text'
// 		}, {
// 			field : 'fstatus',
// 			title : '标志',
// //			width : 30,
// 			halign : "center",
// 			align : "left",
// 			hidden:true
// 		}] ],
// //		pagination : true,
// 		rowStyler: function(index,row){
//             if ((index % 2)!=0){
//             	//处理行代背景色后无法选中
//             	var color=new Object();
//                 return color;
//             }
//         },
// 		onBeforeLoad:function(data){
// 			 $('#fjunctionTable').datagrid('clearChecked');
// 		},
// 		onLoadSuccess:function(data){
// 			 if(data){
// 				 $.each(data.rows, function(index, item){
// 					 if(item.fstatus==1){
// 				         $('#fjunctionTable').datagrid('checkRow', index);
// 					 }
// 				 })
// 			 }
// 		},
// 		onDblClickCell: function(index,field,value){
// 			if (endEditing(2)){
// //				$('#femployeeTable').datagrid('selectRow', index)
// //						.datagrid('editCell', {index:index,field:field});
// 				$(this).datagrid('beginEdit', index);
// 				var ed = $(this).datagrid('getEditor', {index:index,field:field});
// 				$(ed.target).focus();
// 				editIndex = index;
// 			}
// 		}
// 	});
//
// 	$("#wpsDetailTable").datagrid( {
// 		height : $("#addOrUpdate").height()*0.4,
// 		width : $("#addOrUpdate").width()*0.64*0.77,
// 		idField : 'fid',
// //		pageSize : 10,
// //		pageList : [ 10, 20, 30, 40, 50 ],
// 		url : "/",
// 		singleSelect : true,
// 		rownumbers : true,
// //		showPageList : false,
// 		fitColumns : true,
// 		columns : [ [ {
// 			field : 'fid',
// 			title : '序号',
// //			width : 30,
// 			halign : "center",
// 			align : "left",
// 			hidden:true
// 		}, {
// 			field : 'fquantitative_project',
// 			title : '量化项目',
// 			width : 100,
// 			halign : "center",
// 			align : "left",
// 			editor:'text'
// 		}, {
// 			field : 'frequired_value',
// 			title : '要求值',
// 			width : 100,
// 			halign : "center",
// 			align : "left",
// 			editor:'text'
// 		}, {
// 			field : 'fupper_deviation',
// 			title : '上偏差',
// 			width : 100,
// 			halign : "center",
// 			align : "left",
// 			editor:'text'
// 		}, {
// 			field : 'flower_deviation',
// 			title : '下偏差',
// 			width : 100,
// 			halign : "center",
// 			align : "left",
// 			editor:'text'
// 		}, {
// 			field : 'funit_of_measurement',
// 			title : '计量单位',
// 			width : 100,
// 			halign : "center",
// 			align : "left",
// 			editor:'text'
// 		}] ],
// //		pagination : true,
// 		rowStyler: function(index,row){
//             if ((index % 2)!=0){
//             	//处理行代背景色后无法选中
//             	var color=new Object();
//                 return color;
//             }
//         },
// 		onDblClickCell: function(index,field,value){
// 			if (endEditing(3)){
// //				$('#femployeeTable').datagrid('selectRow', index)
// //						.datagrid('editCell', {index:index,field:field});
// 				$(this).datagrid('beginEdit', index);
// 				var ed = $(this).datagrid('getEditor', {index:index,field:field});
// 				$(ed.target).focus();
// 				editIndex = index;
// 			}
// 		}
// 	});
// }

// function addEmployeeRow() {
//     if (endEditing(0)) {
//         $('#femployeeTable').datagrid('appendRow', {});
//         var index = $('#femployeeTable').datagrid("getRows").length - 1;
//         $('#femployeeTable').datagrid('beginEdit', index);
//         $('#femployeeTable').datagrid('selectRow', index);
//     }
//     editIndex = index;
// }

// function deleteEmployeeRow() {
//     if (endEditing(0)) {
//         var len = $('#femployeeTable').datagrid("getRows").length;
//         if (len == 0) {
//             return;
//         }
//         var rows = $('#femployeeTable').datagrid("getSelections");
//         if (rows.length != 0) {
//             var con = confirm("该操作将删除所选数据及其关联数据，并且无法撤销，是否继续？")
//             if (con == true) {
//                 var len = rows.length;
//                 for (var r = len - 1; r >= 0; r--) {
//                     var index = $('#femployeeTable').datagrid('getRowIndex', rows[r]);
//                     $('#femployeeTable').datagrid('deleteRow', index);
//                 }
//             }
//         } else {
//             return;
//         }
//     }
// }

// function addStepRow() {
//     if (endEditing(1)) {
//         $('#fstepTable').datagrid('appendRow', {});
//         var index = $('#fstepTable').datagrid("getRows").length - 1;
//         $('#fstepTable').datagrid('beginEdit', index);
//         $('#fstepTable').datagrid('selectRow', index);
//     }
//     editIndex = index;
// }

// function deleteStepRow() {
//     if (endEditing(1)) {
//         var len = $('#fstepTable').datagrid("getRows").length;
//         if (len == 0) {
//             return;
//         }
//         var rows = $('#fstepTable').datagrid("getSelections");
//         if (rows.length != 0) {
//             var r = confirm("该操作将删除所选数据及其关联数据，并且无法撤销，是否继续？")
//             if (r == true) {
//                 var len = rows.length;
//                 for (var r = len - 1; r >= 0; r--) {
//                     var index = $('#fstepTable').datagrid('getRowIndex', rows[r]);
//                     $('#fstepTable').datagrid('deleteRow', index);
//                 }
//             }
//         } else {
//             return;
//         }
//     }
// }

// function addJunctionRow() {
//     if (endEditing(2)) {
//         $('#fjunctionTable').datagrid('appendRow', {});
//         var index = $('#fjunctionTable').datagrid("getRows").length - 1;
//         $('#fjunctionTable').datagrid('beginEdit', index);
//         $('#fjunctionTable').datagrid('selectRow', index);
//     }
//     editIndex = index;
// }
//
// var editIndex = undefined;
// function endEditing(value){
// 	if (editIndex == undefined){return true}
// 	//0工序；1工步；2焊缝；3参数
// 	if(value == 0){
// 		if ($('#femployeeTable').datagrid('validateRow', editIndex)){
// 			$('#femployeeTable').datagrid('endEdit', editIndex);
// 			editIndex = undefined;
// 			return true;
// 		} else {
// 			return false;
// 		}
// 	}
// 	if(value == 1){
// 		if ($('#fstepTable').datagrid('validateRow', editIndex)){
// 			$('#fstepTable').datagrid('endEdit', editIndex);
// 			editIndex = undefined;
// 			return true;
// 		} else {
// 			return false;
// 		}
// 	}
// 	if(value == 2){
// 		if ($('#fjunctionTable').datagrid('validateRow', editIndex)){
// 			$('#fjunctionTable').datagrid('endEdit', editIndex);
// 			editIndex = undefined;
// 			return true;
// 		} else {
// 			return false;
// 		}
// 	}
// 	if(value == 3){
// 		if ($('#wpsDetailTable').datagrid('validateRow', editIndex)){
// 			$('#wpsDetailTable').datagrid('endEdit', editIndex);
// 			editIndex = undefined;
// 			return true;
// 		} else {
// 			return false;
// 		}
// 	}
// }

/*function onClickCell(index, field){
	if (endEditing()){
//		$('#femployeeTable').datagrid('selectRow', index)
//				.datagrid('editCell', {index:index,field:field});
		$(this).datagrid('beginEdit', index);
		var ed = $(this).datagrid('getEditor', {index:index,field:field});
		$(ed.target).focus();
		editIndex = index;
	}
}

//单元格失去焦点执行的方法  
function onAfterEdit(index, row, changes) {  
	$('#femployeeTable').datagrid("endEdit", index);
    var updated = $('#femployeeTable').datagrid('getChanges');  
    if (updated.length < 1) {  
        editRow = undefined;  
        $('#femployeeTable').datagrid('unselectAll');  
        return;  
    } else {  
        // 传值  
		alert("xxx");
//      submitForm(index, row, changes);  
    }        
}  */

// var employeeUrl = "/", stepUrl = "/", junctionUrl = "/", detailUrl = "/", url = "";
var flag = "";

function addWps() {
    $("#tdd-buttons").hide();
    flag = "add";
    wpsId = "";
    $("#addOrUpdatefm").form("disableValidation");
    $('#addOrUpdate').window({
        title: "新增工艺",
        modal: true
    });
    $('#addOrUpdate').window('open');
    $('#addOrUpdatefm').form('clear');
}

function junctionButton() {
    var dialogDiv = (document.getElementById("dialogDiv").style.width).substring(0, 4);
    var dialogDivheight = (document.getElementById("dialogDiv").style.height).substring(0, 3);
    $('#junctionTable').datagrid('clearChecked');
    $('#dialogDiv').dialog({
        title: '查找带回',
        width: dialogDiv,
        height: dialogDivheight,
        closed: false,
        cache: false,
        //url: 'junction/goJunction?dialogDiv='+dialogDiv,
        // href: 'junction/goJunction',
        modal: true,
        maximizable: true,
        resizable: true
    }).dialog("open");
    $('#dialogDiv').show();
};

function determine() {
    $('#fids').val('');
    var junctionName = [];
    var ids = [];
    var rows = $('#junctionTable').datagrid('getSelections');
    if (rows.length > 0) {
        for (var i = 0; i < rows.length; i++) {
            if (i == (rows.length - 1)) {
                junctionName.push(rows[i].fjunction);
            } else {
                junctionName.push(rows[i].fjunction + "、");
            }
            ids.push(rows[i].fid);
        }
    }
    // alert(ids.join());
    $('#junctionName').textbox('setValue', junctionName);
    $('#fids').val(ids);
    $('#dialogDiv').dialog("close");
}

function editWps(addVersionRow) {
    flag = "edit";
    wpsId = "";
    $('#addOrUpdatefm').form('clear');
    //var row = $('#wpslibTable').datagrid('getSelected');
    var row = $('#wpslibTable').datagrid('getSelections');
    if (addVersionRow != "") {
        row = addVersionRow;
    }
    if (row.length == 1) {
        $('#addOrUpdate').window({
            title: "修改工艺",
            modal: true
        });
        $('#addOrUpdate').window('open');
        $('#junctionName').textbox('setValue', row[0].JUNCTION);
        $('#fids').val(row[0].junctionIds);
        $('#addOrUpdatefm').form('load', row[0]);
    } else {
        alert("请先选择一条数据。");
    }
}

function saveWps() {
    //var wpsFlag = $('#flag').combobox('getValue');//焊缝名称
    var url2 = "";
    if (flag == "add") {
        url2 = "wps/addWpsLibrary";
    } else if (flag == "edit"){
        url2 = "wps/updateWpsLibrary";
    }
    $('#addOrUpdatefm').form('submit', {
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
                    alert("保存成功");
                    $('#addOrUpdate').window('close');
                    $('#wpslibTable').datagrid('reload');
                }
            }

        },
        error: function (errorMsg) {
            alert("数据请求失败，请联系系统管理员!");
        }
    });
}

function deleteWps(){
    var deleteRows = $("#wpslibTable").datagrid('getSelections'); //获取删除的数据
    if(deleteRows != null && deleteRows != ''){
        var c = confirm("该操作将删除所选数据及其关联数据，并且无法撤销，是否继续？")
        if (c) {
            $.ajax({
                type : "post",
                async : false,
                url : "wps/deleteWps",
                data : {deleteRows:JSON.stringify(deleteRows)},
                dataType : "json", //返回数据形式为json
                success : function(result) {
                    if (result) {
                        if (!result.success) {
                            $.messager.show( {
                                title : 'Error',
                                msg : result.msg
                            });
                        } else {
                            alert("删除成功！");
                            $('#wpslibTable').datagrid('reload');
                        }
                    }
                },
                error : function(errorMsg) {
                    alert("数据请求失败，请联系系统管理员!");
                }
            });
        }
    }else{
        alert("请先选择一条数据。");
    }
}
// function saveReview() {
//     $.ajax({
//         type: "post",
//         async: false,
//         url: "wps/passReview?fid=" + wpsId + "&value=0",
//         dataType: "json",
//         data: {},
//         success: function (result) {
//             if (result) {
//                 if (!result.success) {
//                     $.messager.show({
//                         title: 'Error',
//                         msg: result.errorMsg
//                     });
//                 } else {
//                     alert("保存成功");
//                     $('#addOrUpdate').window('close');
//                     $('#wpslibTable').datagrid('reload');
//                 }
//             }
//         },
//         error: function () {
//             alert('error');
//         }
//     });
// }

// function addVersion() {
//     flag = 1;
//     wpsId = "";
//     $('#addVersionfm').form('clear');
//     var row = $('#wpslibTable').datagrid('getSelected');
//     if (row) {
//         $("#addVersionfm").form("disableValidation");
//         $('#addVersionDiv').window({
//             title: "新建版本",
//             modal: true
//         });
//         $('#addVersionDiv').window('open');
//         $("#hide_id").val(row.fid);
//         $("#fproduct_drawing_no_v").textbox('setValue', row.fproduct_drawing_no);
//         $("#fproduct_name_v").textbox('setValue', row.fproduct_name);
//         $("#fproduct_version_v").textbox('setValue', row.fproduct_version);
//         url = "wps/addVersion?fid=" + row.fid;
//     }
// }

// function saveVersion() {
//     var fwps_lib_version_v = $("#fwps_lib_version_v").textbox('getValue');
//     var fwps_lib_name_v = $("#fwps_lib_name_v").textbox('getValue');
//     var fproduct_drawing_no_v = $("#fproduct_drawing_no_v").textbox('getValue');
//     var fproduct_name_v = $("#fproduct_name_v").textbox('getValue');
//     var fproduct_version_v = $("#fproduct_version_v").textbox('getValue');
//     $.ajax({
//         type: "post",
//         async: false,
//         url: "wps/wpsversionvalidate?wpsversion=" + fwps_lib_version_v + "&wln=" + fwps_lib_name_v + "&pdn=" + fproduct_drawing_no_v + "&pv=" + fproduct_name_v,
//         dataType: "json",
//         data: {},
//         success: function (result) {
//             if (result) {
//                 if (result.success) {
//                     alert("该图号版本下工艺规程的版本已经存在");
//                     return;
//                 } else {
//                     var messager = "";
//                     $('#addVersionfm').form('submit', {
//                         url: url + "&fwps_lib_version_v=" + fwps_lib_version_v + "&fwps_lib_name_v=" + fwps_lib_name_v + "&fproduct_drawing_no_v=" + fproduct_drawing_no_v + "&fproduct_name_v=" + fproduct_name_v + "&fproduct_version_v=" + fproduct_version_v,
//                         onSubmit: function () {
//                             return $(this).form('enableValidation').form('validate');
//                         },
//                         success: function (result) {
//                             if (result) {
//                                 var result = eval('(' + result + ')');
//                                 if (!result.success) {
//                                     $.messager.show({
//                                         title: 'Error',
//                                         msg: result.errorMsg
//                                     });
//                                 } else {
//                                     if (result.wpsId != "") {
//                                         wpsId = result.wpsId;
//                                     }
//                                     $('#wpslibTable').datagrid('reload');
//                                     $('#addVersionDiv').window('close');
//                                     var addVersionRow = result.objRow;
//                                     var con = confirm("保存成功,是否前往修改相关参数？")
//                                     if (con == true) {
//                                         editWps(addVersionRow);
//                                     }
//                                 }
//                             }
//
//                         },
//                         error: function (errorMsg) {
//                             alert("数据请求失败，请联系系统管理员!");
//                         }
//                     });
//                 }
//             }
//         },
//         error: function () {
//             alert('error');
//         }
//     });
// }