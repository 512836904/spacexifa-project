// //WB-M350L
// function WBML() {}
// function WBMLGET(data) {}
// function WBMLINIT() {}
// function WBMLCHECK() {}
//
// //WB-P400
// function WBP() {}
// function WBPGET(data) {}
// function WBPINIT() {}
// function WBPCHECK() {}
//
// //WB-500L
// function WBL() {}
// function WBLGET(data) {}
// function WBLINIT() {}
// function WBLCHECK() {}
//
// //EP-400
// function EPS() {}
// function EPSGET(data) {}
// function EPSINIT() {}
// function EPSCHECK() {}
//
// //EP-500
// function EPW() {}
// function EPWGET(data) {}
// function EPWINIT() {}
// function EPWCHECK() {}

//CPVE-500,wpslibId:工艺库id
function CPVEW() {
	var wpslibrow = $('#wpslibTable').datagrid("getSelected");		//工艺库
	var wpslibId = wpslibrow.fid;

	var sochet_send_data = new Array();		// 字节码拼接
	var giveArray = new Array();
	var resultData = new Array();			//下发完成返回的数据
	var noReceiveGiveChanel = new Array();
	var realLength = 0;
	var selectMainWpsRows = $('#mainWpsTable').datagrid('getSelections');	//子工艺规范
	var selectMachine = $('#weldingmachineTable').datagrid('getSelections');	//焊机
	if (selectMachine.length == 0) {
		alert("请先选择焊机!!!");
		return false;
	}
	for (var m = 0; m < selectMachine.length; m++) {
		console.log("gatherId:"+selectMachine[m].gatherId);
		// if (!selectMachine[m].gatherId) {
		// 	alert(selectMachine[m].equipmentNo + "未绑定采集模块，请重新选择!!!");
		// 	return false;
		// }
	}
	var checkLength = selectMachine.length * selectMainWpsRows.length;
	for (var smindex = 0; smindex < selectMachine.length; smindex++) {
		noReceiveGiveChanel.length = 0;
		for (var mwindex = 0; mwindex < selectMainWpsRows.length; mwindex++) {

			var str = "";
			str = selectMachine[smindex].gatherId;

			console.log(str);

			//console.log(str.replace(/\s*/g,""));
			str   =   str.replace(/\s+/g,"");
			console.log(str);
			var math = parseInt(str).toString(16);

			console.log("math:==="+math);
			alert(str);
			alert("math:"+math);

			//console.log("------"+WBL(selectMainWpsRows[mwindex],selectMachine[smindex].gatherId));
			//sochet_send_data.push(WBL(selectMainWpsRows[mwindex],selectMachine[smindex].gatherId));
			//noReceiveGiveChanel.push(parseInt(selectMainWpsRows[mwindex].fchanel));
		}
		var jsonstr = {
			"machineNo" : selectMachine[smindex].equipmentNo,
			"gatherNo" : selectMachine[smindex].gatherId,
			"successNum" : 0,
			"failNum" : 0,
			"noNum" : noReceiveGiveChanel.join(",")
		};
		resultData.push(jsonstr);
		if (giveArray.length == 0) {
			giveArray.push(selectMachine[smindex].equipmentNo);
			giveArray.push(parseInt(selectMachine[smindex].gatherId));
			giveArray.push(0);
			giveArray.push(0);
			giveArray.push(0);
		} else {
			if ($.inArray(selectMachine[smindex].equipmentNo,giveArray) == (-1)) {
				giveArray.push(selectMachine[smindex].equipmentNo);
				giveArray.push(parseInt(selectMachine[smindex].gatherId));
				giveArray.push(0);
				giveArray.push(0);
				giveArray.push(0);
			}
		}
	}
	console.log("len:----------"+sochet_send_data.length);
	if (sochet_send_data.length != 0 && noReceiveGiveChanel.length != 0){
		putMQmessage(selectMainWpsRows,selectMachine,sochet_send_data,giveArray,noReceiveGiveChanel,resultData,realLength,checkLength,wpslibId);
	}
}

//通过mq发送到服务器
function putMQmessage(selectMainWpsRows,selectMachine,sochet_send_data,giveArray,noReceiveGiveChanel,resultData,realLength,checkLength,wpslibId){
	var oneMinuteTimer = window.setTimeout(function() {
		alert("部分焊机下发超时");
		$('#smdlg').window('close');
		$('#smwdlg').window('close');
		$('#weldingmachineTable').datagrid('clearSelections');
		$('#mainWpsTable').datagrid('clearSelections');
		selectMainWpsRows.length = 0;
		selectMachine.length = 0;
		sochet_send_data.length = 0;
		giveArray.length = 0;
		noReceiveGiveChanel.length = 0;
		resultData.length = 0;
		realLength = 0;
	}, 30000);
	showResult();
	$("#giveResultTable").datagrid('loadData', resultData);	//下发结果table
	var timer = window.setInterval(function() {
		if (sochet_send_data.length != 0) {
			var popdata = sochet_send_data.pop();
			var message = new Paho.MQTT.Message(popdata);
			message.destinationName = "weldmes/downparams";
			client.send(message);
		} else {
			window.clearInterval(timer);
		}
	}, 300)
	client.subscribe("weldmes/upparams", {
		qos: 0,
		onSuccess:function(e){
			console.log("订阅成功");
		},
		onFailure: function(e){
			console.log(e);
		}
	});
	client.onMessageArrived = function(e){
		var fan = e.payloadString;
		if (fan.substring(0, 2) == "7E" && fan.substring(10, 12) == "52") {
			var rows = $('#giveResultTable').datagrid("getRows");
			if (parseInt(fan.substring(18, 20), 16) == 1) {
				realLength++;
				var frchanel = parseInt(fan.substring(16, 18), 16);
				var indexNum = $.inArray(parseInt(fan.substring(12, 16), 16),giveArray);
				//var indexNum = giveArray.indexOf(parseInt(fan.substring(12, 16), 16));
				if (indexNum != -1) {
					giveArray[indexNum + 2] = frchanel;
					/*					giveArray[indexNum+3].splice(giveArray[indexNum+3].indexOf(frchanel), 1);*/
					if (rows[(indexNum - 1) / 5].noNum != "0") {
						var onNumArr = rows[(indexNum - 1) / 5].noNum.split(",");
						//if(onNumArr.indexOf(frchanel)!=-1){
						onNumArr.splice($.inArray(frchanel,onNumArr), 1);
						var nowNoArr = onNumArr;
						if (nowNoArr.length != 0) {
							rows[(indexNum - 1) / 5].noNum = nowNoArr.join(",");
						} else {
							rows[(indexNum - 1) / 5].noNum = 0;
						}
					} else {
						rows[(indexNum - 1) / 5].noNum = 0;
					}
					if (parseInt(rows[(indexNum - 1) / 5].failNum) != 0) {
						rows[(indexNum - 1) / 5].failNum = rows[(indexNum - 1) / 5].failNum + "," + giveArray[indexNum + 2];
					} else {
						rows[(indexNum - 1) / 5].failNum = giveArray[indexNum + 2];
					}
					$('#giveResultTable').datagrid('refreshRow', (indexNum - 1) / 5);
				}
				if (realLength == checkLength) {
					client.unsubscribe("weldmes/upparams", {
						onSuccess : function(e) {
							console.log("取消订阅成功");
						},
						onFailure : function(e) {
							console.log(e);
						}
					})
					window.clearTimeout(oneMinuteTimer);
					$.ajax({
						type : "post",
						async : false,
						url : "wps/saveGiveWpsHistory",
						data : {mainwps:JSON.stringify(selectMainWpsRows),machine:JSON.stringify(selectMachine),wpslib:wpslibId,flag:0},
						dataType : "json", //返回数据形式为json
						success : function(result) {
							if (result.success) {
								alert("下发完成");
							} else{
								alert("下发完成，存储下发记录失败")
							}
							$('#smdlg').window('close');
							$('#smwdlg').window('close');
							$('#weldingmachineTable').datagrid('clearSelections');
							$('#mainWpsTable').datagrid('clearSelections');
							selectMainWpsRows.length = 0;
							selectMachine.length = 0;
							sochet_send_data.length = 0;
							giveArray.length = 0;
							resultData.length = 0;
							noReceiveGiveChanel.length = 0;
							realLength = 0;
						},
						error : function(errorMsg) {
							alert("数据请求失败，请联系系统管理员!");
						}
					});
				}
			} else {
				realLength++;
				var frchanel = parseInt(fan.substring(16, 18), 16);
				var indexNum = $.inArray(parseInt(fan.substring(12, 16), 16),giveArray);
				//var indexNum = giveArray.indexOf(parseInt(fan.substring(12, 16), 16));
				if (indexNum != -1) {
					giveArray[indexNum + 1] = frchanel;
					/*					giveArray[indexNum+3].splice(giveArray[indexNum+3].indexOf(frchanel), 1);*/
					if (rows[(indexNum - 1) / 5].noNum != "0") {
						var onNumArr = rows[(indexNum - 1) / 5].noNum.split(",");
						//if(onNumArr.indexOf(frchanel)!=-1){
						onNumArr.splice($.inArray(frchanel,onNumArr), 1);
						var nowNoArr = onNumArr;
						if (nowNoArr.length != 0) {
							rows[(indexNum - 1) / 5].noNum = nowNoArr.join(",");
						} else {
							rows[(indexNum - 1) / 5].noNum = 0;
						}
					} else {
						rows[(indexNum - 1) / 5].noNum = 0;
					}
					if (parseInt(rows[(indexNum - 1) / 5].successNum) != 0) {
						rows[(indexNum - 1) / 5].successNum = rows[(indexNum - 1) / 5].successNum + "," + giveArray[indexNum + 1];
					} else {
						rows[(indexNum - 1) / 5].successNum = giveArray[indexNum + 1];
					}
					$('#giveResultTable').datagrid('refreshRow', (indexNum - 1) / 5);
				}
				if (realLength == checkLength) {
					client.unsubscribe("weldmes/upparams", {
						onSuccess : function(e) {
							console.log("取消订阅成功");
						},
						onFailure : function(e) {
							console.log(e);
						}
					})
					window.clearTimeout(oneMinuteTimer);
					$.ajax({
						type : "post",
						async : false,
						url : "wps/saveGiveWpsHistory",
						data : {mainwps:JSON.stringify(selectMainWpsRows),machine:JSON.stringify(selectMachine),wpslib:wpslibId,flag:0},
						dataType : "json", //返回数据形式为json
						success : function(result) {
							if (result.success) {
								alert("下发完成");
							} else{
								alert("下发完成，存储下发记录失败")
							}
							$('#smdlg').window('close');
							$('#smwdlg').window('close');
							$('#weldingmachineTable').datagrid('clearSelections');
							$('#mainWpsTable').datagrid('clearSelections');
							selectMainWpsRows.length = 0;
							selectMachine.length = 0;
							sochet_send_data.length = 0;
							giveArray.length = 0;
							resultData.length = 0;
							noReceiveGiveChanel.length = 0;
							realLength = 0;
						},
						error : function(errorMsg) {
							alert("数据请求失败，请联系系统管理员!");
						}
					});
				}
			}
		}
	}
}

function CPVEWGET(data) {
	$('#fchanel').combobox('select', parseInt(data.substring(18, 20), 16));
	$("#ftime").numberbox('setValue', (parseInt(data.substring(20, 24), 16) / 10).toFixed(1));
	$("#fadvance").numberbox('setValue', (parseInt(data.substring(24, 28), 16) / 10).toFixed(1));
	$("#fini_ele").numberbox('setValue', parseInt(data.substring(28, 32), 16));
	$("#fini_vol").numberbox('setValue', (parseInt(data.substring(32, 36), 16) / 10).toFixed(1));
	$("#fini_vol1").numberbox('setValue', parseInt(data.substring(36, 40), 16).toFixed(1));
	$("#fweld_ele").numberbox('setValue', parseInt(data.substring(40, 44), 16));
	$("#fweld_vol").numberbox('setValue', (parseInt(data.substring(44, 48), 16) / 10).toFixed(1));
	$("#fweld_vol1").numberbox('setValue', parseInt(data.substring(48, 52), 16).toFixed(1));
	$("#farc_ele").numberbox('setValue', parseInt(data.substring(52, 56), 16));
	$("#farc_vol").numberbox('setValue', (parseInt(data.substring(56, 60), 16) / 10).toFixed(1));
	$("#farc_vol1").numberbox('setValue', parseInt(data.substring(60, 64), 16).toFixed(1));
	$("#fhysteresis").numberbox('setValue', (parseInt(data.substring(64, 68), 16) / 10).toFixed(1));
	$("#fcharacter").numberbox('setValue', parseInt(data.substring(68, 72), 16));
	if (parseInt(data.substring(72, 74), 16) == 0) {
		$('#fgas').combobox('select', 121);
	} else if (parseInt(data.substring(72, 74), 16) == 1) {
		$('#fgas').combobox('select', 122);
	} else {
		$('#fgas').combobox('select', 124);
	}
	if (parseInt(data.substring(74, 76), 16) == 10) {
		$('#fdiameter').combobox('select', 131);
	} else if (parseInt(data.substring(74, 76), 16) == 12) {
		$('#fdiameter').combobox('select', 132);
	} else if (parseInt(data.substring(74, 76), 16) == 14) {
		$('#fdiameter').combobox('select', 133);
	} else {
		$('#fdiameter').combobox('select', 134);
	}
	if (parseInt(data.substring(76, 78), 16) == 0) {
		$('#fmaterial').combobox('select', 91);
	} else if (parseInt(data.substring(76, 78), 16) == 1) {
		$('#fmaterial').combobox('select', 92);
	} else if (parseInt(data.substring(76, 78), 16) == 4) {
		$('#fmaterial').combobox('select', 93);
	} else {
		$('#fmaterial').combobox('select', 94);
	}
	var sconx = parseInt(data.substring(82, 84), 16);
	sconx = sconx.toString(2);
	if (sconx.length < 8) {
		var length = 8 - sconx.length;
		for (var i = 0; i < length; i++) {
			sconx = "0" + sconx;
		}
	}
	if (sconx.substring(7, 8) == "1") {
		$("#finitial").prop("checked", true);
	} else {
		$("#finitial").prop("checked", false);
	}
	if (sconx.substring(6, 7) == "0") {
		$('#farc').combobox('select', 111);
	} else {
		$('#farc').combobox('select', 112);
	}
	if (sconx.substring(5, 6) == "1") {
		$('#farc').combobox('select', 113);
	}
	if (sconx.substring(4, 5) == "1") {
		$('#farc').combobox('select', 114);
	}
	if (sconx.substring(2, 3) == "0") {
		$('#fselect').combobox('select', 102);
	} else {
		$('#fselect').combobox('select', 101);
	}
	if (sconx.substring(1, 2) == "1") {
		$("#fcontroller").prop("checked", true);
	} else {
		$("#fcontroller").prop("checked", false);
	}
	if (sconx.substring(0, 1) == "1") {
		$("#fmode").prop("checked", true);
	} else {
		$("#fmode").prop("checked", false);
	}
	$("#fweld_tuny_ele").numberbox('setValue', parseInt(data.substring(84, 86), 16));
	$("#farc_tuny_ele").numberbox('setValue', parseInt(data.substring(88, 90), 16));
	$("#fwarn_ele_up").numberbox('setValue', parseInt(data.substring(92, 96), 16));
	$("#fwarn_ele_down").numberbox('setValue', parseInt(data.substring(100, 104), 16));
	$("#fwarn_vol_up").numberbox('setValue', (parseInt(data.substring(96, 100), 16) / 10).toFixed(1));
	$("#fwarn_vol_down").numberbox('setValue', (parseInt(data.substring(104, 108), 16) / 10).toFixed(1));
	if (sconx.substring(2, 3) == "0") {
		$("#fweld_tuny_vol").numberbox('setValue', (parseInt(data.substring(86, 88), 16) / 10).toFixed(1));
		$("#farc_tuny_vol").numberbox('setValue', (parseInt(data.substring(90, 92), 16) / 10).toFixed(1));
	} else {
		$("#fweld_tuny_vol").numberbox('setValue', parseInt(data.substring(86, 88), 16));
		$("#farc_tuny_vol").numberbox('setValue', parseInt(data.substring(90, 92), 16));
	}
}
function CPVEWINITwps() {
	$('#fchanel').combobox('clear');
	var str = "";
	for (var i = 1; i < 31; i++) {
		str += '<option value="' + i + '">通道号' + i + '</option>';
	}
	$('#fchanel').append(str);
	$('#fchanel').combobox();
	$('#fchanel').combobox('select', "1");
	$('#farc').combobox('clear');
	$('#farc').combobox('loadData', [ {
		"text" : "无填弧坑",
		"value" : "111"
	}, {
		"text" : "直流填弧坑",
		"value" : "112"
	}, {
		"text" : "脉冲填弧坑",
		"value" : "113"
	}, {
		"text" : "电弧点焊",
		"value" : "114"
	} ]);
	$('#fgas').combobox('clear');
	$('#fgas').combobox('loadData', [ {
		"text" : "CO2",
		"value" : "121"
	}, {
		"text" : "MAG",
		"value" : "122"
	}, {
		"text" : "MIG",
		"value" : "123"
	}, {
		"text" : "MIG_2O2",
		"value" : "124"
	} ]);
	$('#fdiameter').combobox('clear');
	$('#fdiameter').combobox('loadData', [ {
		"text" : "Φ0.8",
		"value" : "135"
	}, {
		"text" : "Φ0.9",
		"value" : "136"
	}, {
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
	$('#fmaterial').combobox('clear');
	$('#fmaterial').combobox('loadData', [ {
		"text" : "低碳钢实芯",
		"value" : "91"
	}, {
		"text" : "不锈钢实芯",
		"value" : "92"
	}, {
		"text" : "低碳钢药芯",
		"value" : "93"
	}, {
		"text" : "不锈钢药芯",
		"value" : "94"
	}, {
		"text" : "铁氧体不锈钢实芯",
		"value" : "95"
	}, {
		"text" : "纯铝",
		"value" : "96"
	}, {
		"text" : "铝镁合金",
		"value" : "97"
	} ]);
	$('#fweldprocess').combobox('clear');
	$('#fweldprocess').combobox('loadData', [ {
		"text" : "直流脉冲",
		"value" : "0"
	}, {
		"text" : "直流",
		"value" : "1"
	}, {
		"text" : "直流低飞溅",
		"value" : "2"
	}, {
		"text" : "直流双脉冲",
		"value" : "3"
	} ]);
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
	$("#fmode").prop("checked", false);
	$("#finitial").prop("checked", false);
	$("#fcontroller").prop("checked", false);
	$("#ftorch").prop("checked", false);
	$('#fselect').combobox('select', 102);
	$("#ftime").numberbox('setValue', 3.0);
	$("#fadvance").numberbox('setValue', 0.1);
	$("#fini_ele").numberbox('setValue', 100);
	$("#fweld_ele").numberbox('setValue', 150);
	$("#farc_ele").numberbox('setValue', 100);
	$("#fhysteresis").numberbox('setValue', 0.4);
	$("#fcharacter").numberbox('setValue', 0);
	$('#fweldprocess').combobox('select', 0);
	$('#fgas').combobox('select', 122);
	$('#fmaterial').combobox('select', 91);
	$('#fdiameter').combobox('select', 132);
	$("#fweld_tuny_ele").numberbox('setValue', 0);
	$("#farc_tuny_ele").numberbox('setValue', 0);
	$("#fini_vol").numberbox('setValue', 21.5);
	$("#fweld_vol").numberbox('setValue', 23.5);
	$("#farc_vol").numberbox('setValue', 21.5);
	$("#fweld_tuny_vol").numberbox('setValue', 0.0);
	$("#farc_tuny_vol").numberbox('setValue', 0.0);
	$("#fini_vol1").numberbox('setValue', 0);
	$("#fweld_vol1").numberbox('setValue', 0);
	$("#farc_vol1").numberbox('setValue', 0);
	$("#fweld_tuny_vol1").numberbox('setValue', 0);
	$("#farc_tuny_vol1").numberbox('setValue', 0);
	$('#farc').combobox('select', 111);
	$("#frequency").numberbox('setValue', 3);
}
function CPVEWCHECK() {
	if ($('#ftime').numberbox('getValue') < 0.1 || $('#ftime').numberbox('getValue') > 10) {
		alert("点焊时间：0.1~10");
		return false;
	}
	if ($('#fadvance').numberbox('getValue') < 0 || $('#fadvance').numberbox('getValue') > 10) {
		alert("提前送气范围：0~10");
		return false;
	}
	if ($('#fini_ele').numberbox('getValue') < 30 || $('#fini_ele').numberbox('getValue') > 550) {
		alert("初期电流范围：30~550");
		return false;
	}
	if ($('#fini_vol').numberbox('getValue') < 12 || $('#fini_vol').numberbox('getValue') > 50) {
		alert("初期电压范围：12~50");
		return false;
	}
	if ($('#fini_vol1').numberbox('getValue') < (-30) || $('#fini_vol1').numberbox('getValue') > (30)) {
		alert("初期电压一元范围：-30~30");
		return false;
	}
	if ($('#fweld_ele').numberbox('getValue') < 30 || $('#fweld_ele').numberbox('getValue') > 550) {
		alert("焊接电流范围：30~550");
		return false;
	}
	if ($('#fweld_vol').numberbox('getValue') < 12 || $('#fweld_vol').numberbox('getValue') > 50) {
		alert("焊接电压范围：12~50");
		return false;
	}
	if ($('#fweld_vol1').numberbox('getValue') < (-30) || $('#fweld_vol1').numberbox('getValue') > (30)) {
		alert("焊接电压一元范围：-30~30");
		return false;
	}
	if ($('#farc_ele').numberbox('getValue') < 30 || $('#farc_ele').numberbox('getValue') > 550) {
		alert("收弧电流范围：30~550");
		return false;
	}
	if ($('#farc_vol').numberbox('getValue') < 12 || $('#farc_vol').numberbox('getValue') > 50) {
		alert("收弧电压范围：12~50");
		return false;
	}
	if ($('#farc_vol1').numberbox('getValue') < (-30) || $('#farc_vol1').numberbox('getValue') > (30)) {
		alert("收弧电压一元范围：-30~30");
		return false;
	}
	if ($('#fhysteresis').numberbox('getValue') < 0.1 || $('#fhysteresis').numberbox('getValue') > 10) {
		alert("滞后送气范围：0.1~10");
		return false;
	}
	if ($('#fcharacter').numberbox('getValue') < (-99) || $('#fcharacter').numberbox('getValue') > (99)) {
		alert("电弧特性范围：-99~99");
		return false;
	}
	/*if ($('#frequency').numberbox('getValue') < (0.5) || $('#frequency').numberbox('getValue') > (32)) {
		alert("双脉冲频率范围：0.5~32");
		return false;
	}
	if ($('#gasflow').numberbox('getValue') < (5) || $('#gasflow').numberbox('getValue') > (50)) {
		alert("气体流量范围：5~50");
		return;
	}*/
	if ($('#fweld_tuny_ele').numberbox('getValue') < (0) || $('#fweld_tuny_ele').numberbox('getValue') > (50)) {
		alert("焊接电流微调范围：0~50");
		return false;
	}
	if ($('#farc_tuny_ele').numberbox('getValue') < (0) || $('#farc_tuny_ele').numberbox('getValue') > (50)) {
		alert("收弧电流微调范围：0~50");
		return false;
	}
/*	if ($('#fwarn_tuny_ele').numberbox('getValue') < (0) || $('#fwarn_tuny_ele').numberbox('getValue') > (250)) {
		alert("报警电流微调范围：0~250");
		return false;
	}
	if ($('#fwarn_tuny_vol').numberbox('getValue') < (0) || $('#fwarn_tuny_vol').numberbox('getValue') > (25)) {
		alert("报警电压微调范围：0~25");
		return false;
	}*/
	if ($('#fselect').combobox('getValue') == 102) {
		if ($('#fweld_tuny_vol').numberbox('getValue') < (0) || $('#fweld_tuny_vol').numberbox('getValue') > (5)) {
			alert("焊接电压微调范围：0~5");
			return false;
		}
		if ($('#farc_tuny_vol').numberbox('getValue') < (0) || $('#farc_tuny_vol').numberbox('getValue') > (5)) {
			alert("收弧电压微调范围：0~5");
			return false;
		}
	} else {
		if ($('#fweld_tuny_vol').numberbox('getValue') < (0) || $('#fweld_tuny_vol').numberbox('getValue') > (20)) {
			alert("焊接电压一元微调范围：0~20");
			return false;
		}
		if ($('#farc_tuny_vol').numberbox('getValue') < (0) || $('#farc_tuny_vol').numberbox('getValue') > (20)) {
			alert("收弧电压一元微调范围：0~20");
			return false;
		}
	}
}

// //CPVE-400
// function CPVES() {}
// function CPVESGET(data) {}
// function CPVESINIT() {}
// function CPVESCHECK() {}
//
// //CPVE-250
// function CPVET() {}
// function CPVETGET(data) {}
// function CPVETINIT() {}
// function CPVETCHECK() {}

//YD-500GR3 验证
function checkSxWps() {
	var wpsLibRow = $('#wpslibTable').datagrid('getSelected');
	if ($('#sxchanel').val() != $('#sxfwpsnum').combobox('getValue')) {
		var num;
		$.ajax({
			type : "post",
			async : false,
			url : "wps/getCountByWpslibidChanel?wpslibid=" + wpsLibRow.fid + "&chanel=" + $('#sxfwpsnum').combobox('getValue'),
			data : {},
			dataType : "json", //返回数据形式为json  
			success : function(result) {
				if (result) {
					num = eval(result.count);
				}
			},
			error : function(errorMsg) {
				alert("数据请求失败，请联系系统管理员!");
			}
		});
		if (num > 0) {
			alert("该通道规范已经存在!!!");
			return false;
		}
	}
	if ($("#sxfflow_top").numberbox('getValue') < 0.1 || $("#sxfflow_top").numberbox('getValue') > 25) {
		alert("流量上限范围：0.1~25.0");
		return false;
	}
	if ($("#sxfflow_bottom").numberbox('getValue') < 0.1 || $("#sxfflow_bottom").numberbox('getValue') > 25) {
		alert("流量下限范围：0.1~25.0");
		return false;
	}
	if ($("#sxfdelay_time").numberbox('getValue') < 0.1 || $("#sxfdelay_time").numberbox('getValue') > 25) {
		alert("延时时间范围：0.1~25.0");
		return false;
	}
	if ($("#sxfover_time").numberbox('getValue') < 0.1 || $("#sxfover_time").numberbox('getValue') > 25) {
		alert("超限时间范围：0.1~25.0");
		return false;
	}
	if ($("#sxffixed_cycle").numberbox('getValue') < 0.1 || $("#sxffixed_cycle").numberbox('getValue') > 10) {
		alert("修正周期范围：0.1~10.0");
		return false;
	}
	if ($("#sxfarc_delay_time").numberbox('getValue') < 0.1 || $("#sxfarc_delay_time").numberbox('getValue') > 3) {
		alert("起弧延时时间范围：0.1~3.0");
		return false;
	}
	if ($("#sxfwarn_delay_time").numberbox('getValue') < 0.1 || $("#sxfwarn_delay_time").numberbox('getValue') > 25) {
		alert("报警延时时间范围：0.1~25.0");
		return false;
	}
	if ($("#sxfwarn_stop_time").numberbox('getValue') < 0.1 || $("#sxfwarn_stop_time").numberbox('getValue') > 25) {
		alert("报警停机时间范围：0.1~25.0");
		return false;
	}
}