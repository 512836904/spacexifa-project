/**
 * 
 */
var oldchanel = 0;
$(function() {
	//	rule();
	statusRadio();
	addWpslib();
	machineModel();
	getDictionary(10,"sxfselect");
	getDictionary(24,"sxfgas");
	getDictionary(23,"sxfdiameter");
	getDictionary(18,"sxfmaterial");
	getDictionary(19,"sxfcontroller");
	getDictionary(20,"sxfinitial");
	getDictionary(21,"sxfarc");
	
	$('#editSxDlg').dialog( {
		onClose : function() {
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
		onClose : function() {
			$('#mainWpsTable').datagrid('clearSelections');
		}
	})
	$('#smdlg').dialog({
		onClose : function() {
			$('#weldingmachineTable').datagrid('clearSelections');
		}
	})
	$('#sxSelectdlg').dialog( {
		onClose : function() {
			$('#sxSelectWpsTab').datagrid('clearSelections');
		}
	})
	$('#sxMachinedlg').dialog( {
		onClose : function() {
			$('#sxMachineTable').datagrid('clearSelections');
		}
	})
	$('#wltdlg').dialog({
		onClose : function() {
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
		title : "新增工艺库",
		modal : true
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
			title : "修改工艺库",
			modal : true
		});
		$('#wltdlg').window('open');
		$('#wltfm').form('load', row);
		$('#validwl').val(row.wpslibName);
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
		url : url2,
		onSubmit : function() {
			return $(this).form('enableValidation').form('validate');
		},
		success : function(result) {
			if (result) {
				var result = eval('(' + result + ')');
				if (!result.success) {
					$.messager.show({
						title : 'Error',
						msg : result.errorMsg
					});
				} else {
					$.messager.alert("提示", messager);
					$('#wltdlg').dialog('close');
					$('#wpslibTable').datagrid('reload');
					$("#validwl").val("");
				}
			}

		},
		error : function(errorMsg) {
			alert("数据请求失败，请联系系统管理员!11");
		}
	});
}

//工艺库状态
function statusRadio() {
	$.ajax({
		type : "post",
		async : false,
		url : "wps/getStatusAll",
		data : {},
		dataType : "json", //返回数据形式为json  
		success : function(result) {
			if (result) {
				var str = "";
				for (var i = 0; i < result.ary.length; i++) {
					str += "<input type='radio' class='radioStyle' name='statusId' id='sId' value=\"" + result.ary[i].id + "\" />"
					+ result.ary[i].name;
				}
				$("#radios").html(str);
				$("input[name='statusId']").eq(0).attr("checked", true);
			}
		},
		error : function(errorMsg) {
			alert("数据请求失败，请联系系统管理员!22");
		}
	});
}

var mflag = 1;
//新增工艺
function addMainWps(row) {
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
	$('#fmwpsCraft').form('reset');
	wpsLibRule(row.modelname);

	if (row.modelname == 'DP500/CPVM500'){
		height = 300;
	}

	//清空页面数据
	$("#modelname").val(row.model);
	$("#fid").val(row.fid);
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
		buttons:[
			{
				text:'索取规范',
				iconCls:'icon-getwps',
				handler:function(){
					selectMachineList(0);
				}
			},{
				text:'保存',
				iconCls:'icon-save',
				handler:function(){
					//获取iframe里面的内容
					// var childWin = document.getElementById('iframe1').contentWindow;
					// //调用子页面对象,子页面的方法
					// var rows = childWin.selectedDevListGrid.getSelectDevList();
					// if(rows){
					// 	if(rows.length==0){
					// 		$.messager.alert('提示','请选择需要选择的设备！');
					// 	}else{
					// 		console.log(rows);
					// 	}
					// }
					saveMainWps();
				}
			},{
				text:'关闭',
				iconCls:'icon-no',
				handler:function(){
					// $("#wpsCraft").dialog('destroy');
					$("#wpsCraft").dialog('close');
				}
		}],
		onClose : function() {
			// $(this).dialog('destroy');
			$("#wpsCraft").dialog('close');
		},
		onLoad:function(){
		}
	});

	//test
	// $('#mwfm').form('clear');

	// $('#mwdlg').window({
	// 	title : "新增工艺",
	// 	modal : true
	// });
	// var wlrow = $('#wpslibTable').datagrid('getSelected');
	//url = "wps/addMainWps?fid=" + wlrow.fid;
	// $('#mwdlg').window('open');
	// return;
	// if (wlrow.model == 174) {
	// 	EPWINIT();
	// 	$('#mwdlg').window('open');
	// 	return;
	// } else if (wlrow.model == 175) {
	// 	EPSINIT();
	// 	$('#mwdlg').window('open');
	// 	return;
	// } else if (wlrow.model == 176) {
	// 	WBMLINIT();
	// 	$('#mwdlg').window('open');
	// 	return;
	// } else if (wlrow.model == 177) {
	// 	WBPINIT();
	// 	$('#mwdlg').window('open');
	// 	return;
	// } else if (wlrow.model == 178) {
	// 	WBLINIT();
	// 	$('#mwdlg').window('open');
	// 	return;
	// } else if (wlrow.model == 171) {
	// 	CPVEWINIT();
	// 	comboboxCheck(wlrow.model);
	// 	$('#mwdlg').window('open');
	// 	return;
	// } else if (wlrow.model == 172) {
	// 	CPVESINIT();
	// 	$('#mwdlg').window('open');
	// 	return;
	// } else if (wlrow.model == 173) {
	// 	CPVETINIT();
	// 	$('#mwdlg').window('open');
	// 	return;
	// } else if (wlrow.manu == 149) {
	// 	$('#editSxDlg').window({
	// 		title : "新增工艺",
	// 		modal : true
	// 	});
	// 	$("#sxRemoveWpsBut").hide();
	// 	$("#sxgetWpsBut").show();
	// 	$("#sxSaveWpsBut").show();
	// 	$('#sxfm').form('clear');
	// 	sxDefault();
	// 	$('#editSxDlg').window('open');
	// 	$("input[name='sxfcharacter']").eq(0).prop("checked", true);
	// 	url = "wps/addSxWps?fwpslib_id=" + wlrow.fid+"&fcharacter="+$('input[name="sxfcharacter"]:checked').val();
	// 	return;
	// }
}

function editMainWps(indexrow,row) {
	mflag = 2;
	wpsLibRule(row.modelname);
	$('#specification_id').val(indexrow.fid);
	// $('#fmwpsCraft').form('clean');
	$('#fmwpsCraft').form('load', indexrow);
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
		buttons:[
			{
				text:'索取规范',
				iconCls:'icon-getwps',
				handler:function(){
					alert("索取规范");
				}
			},{
			text:'保存',
			iconCls:'icon-save',
			handler:function(){
				//获取iframe里面的内容
				// var childWin = document.getElementById('iframe1').contentWindow;
				// //调用子页面对象,子页面的方法
				// var rows = childWin.selectedDevListGrid.getSelectDevList();
				// if(rows){
				// 	if(rows.length==0){
				// 		$.messager.alert('提示','请选择需要选择的设备！');
				// 	}else{
				// 		console.log(rows);
				// 	}
				// }
				saveMainWps();
			}
		},{
			text:'关闭',
			iconCls:'icon-no',
			handler:function(){
				// $("#wpsCraft").dialog('destroy');
				$("#wpsCraft").dialog('close');
			}
		}],
		onClose : function() {
			// $(this).dialog('destroy');
			$(this).dialog('close');
		},
		onLoad:function(){
		}
	});

	// url = "wps/updateMainWps?fid=" + indexrow.fid;
	// // return;
	// if (row) {
	// 	if (row.model == 174) {
	// 		EPWINIT();
	// 	} else if (row.model == 175) {
	// 		EPSINIT();
	// 	} else if (row.model == 176) {
	// 		WBMLINIT();
	// 	} else if (row.model == 177) {
	// 		WBPINIT();
	// 	} else if (row.model == 178) {
	// 		WBLINIT();
	// 	} else if (row.model == 172) {
	// 		CPVESINIT();
	// 	} else if (row.model == 173) {
	// 		CPVETINIT();
	// 	} else if (row.model == 171) {
	// 		CPVEWINIT();
	// 		comboboxCheck(row.model);
	// 	} else if (row.manu == 149){
	// 		mflag = 2;
	// 		$('#sxfm').form('clear');
	// 		if (row) {
	// 			$('#editSxDlg').window( {
	// 				title : "修改工艺",
	// 				modal : true
	// 			});
	// 			$("#sxRemoveWpsBut").hide();
	// 			$("#sxgetWpsBut").show();
	// 			$("#sxSaveWpsBut").show();
	// 			$('#editSxDlg').window('open');
	// 			$('#sxfm').form('load', indexrow);
	// 			$('#sxchanel').val(indexrow.fwpsnum);
	// 			$("input[name='sxfcharacter']").eq(indexrow.sxfcharacter).prop("checked", true);
	// 			url = "wps/editSxWps?fid="+indexrow.fid+"&fcharacter="+$('input[name="sxfcharacter"]:checked').val();
	// 		}
	// 		return;
	// 	}
	// 	$('#mwdlg').window({
	// 		title : "修改工艺",
	// 		modal : true
	// 	});
	// 	$('#mwdlg').window('open');
	// 	$('#mwfm').form('load', indexrow);
	// 	if (encodeURI(indexrow.initial) == "1") {
	// 		$("#finitial").prop("checked", true);
	// 	}
	// 	if (encodeURI(indexrow.mode) == "1") {
	// 		$("#fmode").prop("checked", true);
	// 	}
	// 	if (encodeURI(indexrow.controller) == "1") {
	// 		$("#fcontroller").prop("checked", true);
	// 	}
	// 	if (encodeURI(indexrow.torch) == "1") {
	// 		$("#ftorch").prop("checked", true);
	// 	}
	// 	url = "wps/updateMainWps?fid=" + indexrow.fid;
	// 	oldchanel = indexrow.fchanel;
	// }
}

function saveMainWps() {
	// var wpsLibRow = $('#wpslibTable').datagrid('getSelected');
	// var index = $('#wpslibTable').datagrid('getRowIndex',wpsLibRow);
	// var messager = "";
	var addORupdate = "";
	if (mflag == 1) {
		messager = "新增成功！";
		$("#addORupdate").val("add");
	} else {
		messager = "修改成功！";
		$("#addORupdate").val("update");
	}
	// var url2 = "";
	// url2 = url + "&fwpsnum="+$('#fwpsnum').combobox('getValue') + "&fprocessid=" + $('#fprocessid').combobox('getValue')
	// + "&fmaterial=" + $('#fmaterial').combobox('getValue') + "&fdiameter=" + $('#fdiameter').combobox('getValue') + "&fwpslib_id=" + wpsLibRow.fid;
	if ($('#fchanel').combobox("getValue") == null || $('#fchanel').combobox("getValue") == ''){
		alert("请选择通道号");
		return ;
	}
	$('#fmwpsCraft').form('submit', {
		url : 'wps/apSpe',
		onSubmit : function() {
			return $(this).form('enableValidation').form('validate');
		},
		success : function(result) {
			if(result){
				var result = eval('(' + result + ')');
				if (!result.success) {
					$.messager.show({
						title : 'Error',
						msg : result.errorMsg
					});
					oldchanel = 0;
				} else {
					$.messager.alert("提示", messager);
					$('#wpsCraft').dialog('close');
					$('#ddv-'+index).datagrid('reload');
					oldchanel = 0;
				}
			}
		},  
	    error : function(errorMsg) {  
	        alert("数据请求失败，请联系系统管理员!");  
	    } 
	});
}

//根据焊机型号判断页面显示元素
function wpsLibRule(modelname){
	//焊机型号：Cpve500
	// alert(modelname);
	if (modelname === 'CPVE500'){
		$("#dtorch").hide();
		$("#itorch").hide();
		$("#dfrequency").hide();
		$("#ifrequency").hide();
		$("#tcontroller").show();
		$("#rcontroller").show();
		$("#dfweldprocess").show();
		$("#rfweldprocess").show();
		$("#dgas").show();
		$("#rgas").show();
		$("#dmaterial").show();
		$("#rmaterial").show();
		$("#cwwvo").show();
		$("#cwtwvo").show();
		$("#cwwvto").show();
		$("#cwtwvto").show();
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
		CPVEWINITwps();
		CPVEWRULE();
	}else if (modelname == 'DP500/CPVM500'){
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
		// $('#fadvance').validatebox({required:false});
		// $('#fhysteresis').validatebox({required:false});
		// $('#fini_ele').validatebox({required:false});
		// $('#farc_ele').validatebox({required:false});
		// $('#farc_tuny_ele').validatebox({required:false});
		// $('#ftime').validatebox({required:false});
		// $('#farc_tuny_vol').validatebox({required:false});
		// $('#fini_vol1').validatebox({required:false});
		// $('#fcharacter').validatebox({required:false});
		// $('#frequency').validatebox({required:false});
		// $('#fini_vol').validatebox({required:false});
		// $('#farc_vol').validatebox({required:false});
		// $('#fweld_vol1').validatebox({required:false});
		// $('#fweld_tuny_vol1').validatebox({required:false});
		// $('#farc_vol1').validatebox({required:false});
		// $('#farc_tuny_vol1').validatebox({required:false});
		$('#fchanel').combobox('clear');
		var str = "";
		for (var i = 1; i < 101; i++) {
			str += '<option value="' + i + '">通道号' + i + '</option>';
		}
		$('#fchanel').append(str);
		$('#fchanel').combobox();
		$('#fchanel').combobox('select', "1");
	}else if (modelname == "WB-P500L"){
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
		WBLINITwps();
		WBLRULEwps();
	}
}

function rule() {
	$("#farc").combobox({
		onSelect : function(record) {
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

	$("#finitial").click(function() {
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
		onSelect : function(record) {
			if (record.value == 91) {
				$('#fgas').combobox('clear');
				$('#fgas').combobox('loadData', [ {
					"text" : "CO2",
					"value" : "121"
				}, {
					"text" : "MAG",
					"value" : "122"
				} ]);
				$('#fdiameter').combobox('clear');
				$('#fdiameter').combobox('loadData', [ {
					"text" : "Φ1.0",
					"value" : "131"
				}, {
					"text" : "Φ1.2",
					"value" : "132"
				}, {
					"text" : "Φ1.4",
					"value" : "133"
				}, {
					"text" : "Φ1.6",
					"value" : "134"
				} ]);
			} else if (record.value == 92) {
				$('#fgas').combobox('clear');
				$('#fgas').combobox('loadData', [ {
					"text" : "MIG",
					"value" : "123"
				} ]);
				$('#fdiameter').combobox('clear');
				$('#fdiameter').combobox('loadData', [ {
					"text" : "Φ1.2",
					"value" : "132"
				}, {
					"text" : "Φ1.6",
					"value" : "134"
				} ]);
			} else if (record.value == 93) {
				$('#fgas').combobox('clear');
				$('#fgas').combobox('loadData', [ {
					"text" : "CO2",
					"value" : "121"
				} ]);
				$('#fdiameter').combobox('clear');
				$('#fdiameter').combobox('loadData', [ {
					"text" : "Φ1.2",
					"value" : "132"
				}, {
					"text" : "Φ1.4",
					"value" : "133"
				}, {
					"text" : "Φ1.6",
					"value" : "134"
				} ]);
			} else {
				$('#fgas').combobox('clear');
				$('#fgas').combobox('loadData', [ {
					"text" : "CO2",
					"value" : "121"
				} ]);
				$('#fdiameter').combobox('clear');
				$('#fdiameter').combobox('loadData', [ {
					"text" : "Φ1.2",
					"value" : "132"
				}, {
					"text" : "Φ1.6",
					"value" : "134"
				} ]);
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
		type : "post",
		async : false,
		url : "Dictionary/getValueByTypeid?type=" + 17,
		data : {},
		dataType : "json", //返回数据形式为json  
		success : function(result) {
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
		error : function(errorMsg) {
			alert("数据请求失败，请联系系统管理员!");
		}
	});
}

function saveSxWps(){
	if(checkSxWps()==false){
		return;
	};
	var wpsLibRow = $('#wpslibTable').datagrid('getSelected');
	var index = $('#wpslibTable').datagrid('getRowIndex',wpsLibRow);
	var messager = "";
	var url2 = "";
	if(mflag==1){
		messager = "新增成功！";
		url2 = url;
	}else{
		messager = "修改成功！";
		url2 = url;
	}
	$('#sxfm').form('submit', {
		url : url2,
		onSubmit : function() {
			return $(this).form('enableValidation').form('validate');
		},
		success : function(result) {
			if(result){
				var result = eval('(' + result + ')');
				if (!result.success) {
					$.messager.show( {
						title : 'Error',
						msg : result.errorMsg
					});
				}else{
					$('#ddv-'+index).datagrid('reload');
					$.messager.alert("提示", messager);
					$('#editSxDlg').dialog('close');
//					$('#wpslibTable').datagrid('reload');
				}
			}
			
		},  
	    error : function(errorMsg) {  
	        alert("数据请求失败，请联系系统管理员!");  
	    } 
	});
}

function getDictionary(typeid,id) {
	$.ajax({
		type : "post",
		async : false,
		url : "wps/getDictionary?typeid=" + typeid,
		data : {},
		dataType : "json", //返回数据形式为json  
		success : function(result) {
			if (result) {
				var optionStr = '';
				for (var i = 0; i < result.ary.length; i++) {
					optionStr += "<option value=\"" + result.ary[i].id + "\" >"
						+ result.ary[i].name + "</option>";
				}
				$("#"+id).html(optionStr);
			}
		},
		error : function(errorMsg) {
			alert("数据请求失败，请联系系统管理员!");
		}
	});
	$("#" + id).combobox();
}

function CPVEWRULE(){
	$("#fselect").combobox({
		onChange : function(record) {
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

// 	$("#fchanel").combobox({
// 		onSelect : function(record) {
// 			CPVEWINIT(0);
// 			$.ajax({
// 				type : "post",
// 				async : false,
// 				url : "wps/getAllSpe?machine=" + node11.id + "&chanel=" + record.value,
// 				data : {},
// 				dataType : "json", //返回数据形式为json
// 				success : function(result) {
// 					if (result) {
// 						yshu = eval(result.rows);
// 						if (yshu.length != 0) {
// 							$('#fchanel').combobox('select', yshu[0].FWPSNum);
// 							$('#fselect').combobox('select', yshu[0].fselect);
// 							$("#ftime").numberbox('setValue', yshu[0].ftime);
// 							$("#fadvance").numberbox('setValue', yshu[0].fadvance);
// 							$("#fini_ele").numberbox('setValue', yshu[0].fini_ele);
// 							$("#fini_vol").numberbox('setValue', yshu[0].fini_vol);
// 							$("#fini_vol1").numberbox('setValue', yshu[0].fini_vol1);
// 							$("#fweld_vol").numberbox('setValue', yshu[0].fweld_vol);
// 							$("#fweld_vol1").numberbox('setValue', yshu[0].fweld_vol1);
// 							$("#farc_vol").numberbox('setValue', yshu[0].farc_vol);
// 							$("#farc_vol1").numberbox('setValue', yshu[0].farc_vol1);
// 							$("#fweld_ele").numberbox('setValue', yshu[0].fweld_ele);
// 							$("#farc_ele").numberbox('setValue', yshu[0].farc_ele);
// 							$("#fhysteresis").numberbox('setValue', yshu[0].fhysteresis);
// 							$("#fcharacter").numberbox('setValue', yshu[0].fcharacter);
// 							$('#fweldprocess').combobox('select', yshu[0].fprocessid);
// 							$('#fgas').combobox('select', yshu[0].fgas);
// 							$('#fmaterial').combobox('select', yshu[0].fmaterial);
// 							$('#fdiameter').combobox('select', yshu[0].fdiameter);
// 							$("#fweld_tuny_ele").numberbox('setValue', yshu[0].fweld_tuny_ele);
// 							$("#fweld_tuny_vol").numberbox('setValue', yshu[0].fweld_tuny_vol);
// 							$("#farc_tuny_ele").numberbox('setValue', yshu[0].farc_tuny_ele);
// 							$("#farc_tuny_vol").numberbox('setValue', yshu[0].Fdiameter);
// 							$("#farc_tuny_vol1").numberbox('setValue', yshu[0].Fdiameter);
// 							$("#fweld_tuny_vol1").numberbox('setValue', yshu[0].fweld_tuny_vol);
// 							$("#frequency").numberbox('setValue', yshu[0].frequency);
// 							$("#gasflow").numberbox('setValue', yshu[0].gasflow);
// 							$("#weldingratio").numberbox('setValue', yshu[0].weldingratio);
// 							//上海通用
// 							$("#farc_time").numberbox('setValue', yshu[0].farc_time);
// 							$("#Rush").numberbox('setValue', yshu[0].Rush);
// 							$("#handarc_ele").numberbox('setValue', yshu[0].handarc_ele);//热引弧电流
// 							$("#handarc_time").numberbox('setValue', yshu[0].handarc_time);//热引弧时间
// 							$("#hand_ele").numberbox('setValue', yshu[0].hand_ele);//手工焊电流
// 							$("#Base_ele").numberbox('setValue', yshu[0].Base_ele);//基值电流
// 							$("#Base_vol").numberbox('setValue', yshu[0].Base_vol);//基值电压
// 							$("#Base_vol1").numberbox('setValue', yshu[0].Base_vol1);//基值电压一元
// 							$("#fargon").combobox('select', yshu[0].fargon);//氩弧焊模式选择
// 							$("#manual_weld").combobox('select', yshu[0].manual_weld);//手/气焊选择
// //								$("#pulse").combobox('select',yshu[0].pulse);//双脉冲
// 							$("#rise_time").numberbox('setValue', yshu[0].rise_time);//缓升时间
// 							$("#firsttime").numberbox('setValue',yshu[0].firsttime);//初期时间
// 							$("#decline_time").numberbox('setValue', yshu[0].decline_time);//缓降时间
// 							$("#thrust_ele").numberbox('setValue', yshu[0].thrust_ele);//推力电流
// 							$("#pulse_ratio").numberbox('setValue', yshu[0].pulse_ratio);//双脉冲占空比
// 							$("#point_speed").numberbox('setValue', yshu[0].point_speed);//点动送丝速度
// 							$('#fweldparameters').combobox('select', yshu[0].fweldparameters);
// 							if (yshu[0].arc_length == "1") {
// 								$("#arc_length").prop("checked", true);
// 							}
// 							if (yshu[0].finitial == "1") {
// 								$("#finitial").prop("checked", true);
// 							}
// 							$('#farc').combobox('select', yshu[0].farc);
// 							if (yshu[0].fcontroller == "1") {
// 								$("#fcontroller").prop("checked", true);
// 							}
// 							if (yshu[0].fmode == "1") {
// 								$("#fmode").prop("checked", true);
// 							}
// 							if (yshu[0].ftorch == "1") {
// 								$("#ftorch").prop("checked", true);
// 							}
// 							if (yshu[0].pulse == "1") {
// 								$("#pulse").prop("checked", true);
// 								$("#frequency").numberbox('enable',true);
// 								$("#pulse_ratio").numberbox('enable',true);
// 								$("#Base_ele").numberbox('enable',true);
// 								$("#Base_vol").numberbox('enable',true);
// 								$("#Base_vol1").numberbox('enable',true);
// 							}
// 						} else {
// 							alert("未查询到相关数据，已初始化，也可尝试索取。");
// 						}
// 					}
// 				},
// 				error : function(errorMsg) {
// 					alert("数据请求失败，请联系系统管理员!");
// 				}
// 			});
// 		}
// 	});

	$("#fmaterial").combobox({//焊丝种类
		onChange : function() {
			var fmaterial = $("#fmaterial").combobox('getValue');
			if(fmaterial == 91){
				fgas_1();
			}else if(fmaterial == 92){
				$('#fgas').combobox('clear');
				$('#fgas').combobox('loadData', [ {
					"text" : "MIG_2O2",
					"value" : "124"
				} ]);
			}else if(fmaterial == 93){
				$('#fgas').combobox('clear');
				$('#fgas').combobox('loadData', [ {
					"text" : "CO2",
					"value" : "121"
				} ]);
			}else if(fmaterial == 94){
				$('#fgas').combobox('clear');
				$('#fgas').combobox('loadData', [ {
					"text" : "CO2",
					"value" : "121"
				} ]);
			}
			var data = $('#fgas').combobox('getData');
			$('#fgas').combobox('select',data[0].value);
		}
	})
	$("#fgas").combobox({//气体
		onChange : function() {
			var fmaterial = $("#fmaterial").combobox('getValue');
			var fgas = $("#fgas").combobox('getValue');
			if(fmaterial == 91){//低碳钢实芯
				fdiameter_5();
			}else if(fmaterial == 93){
				fdiameter_3();
			}else if(fmaterial == 92 || fmaterial == 94){
				fdiameter_4();
			}
			var data = $('#fdiameter').combobox('getData');
			$('#fdiameter').combobox('select',data[0].value);
		}
	})
	/*	$('#fweldprocess').combobox('select', 1);
        fmaterial_1();
        var data = $('#fmaterial').combobox('getData');
        $('#fmaterial').combobox('select',data[0].value);*/
}

