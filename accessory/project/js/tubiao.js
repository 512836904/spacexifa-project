var name;
var userid;
var qualification;
var lockReconnect = false;//避免重复连接
var eledata = new Array(),voldata = new Array(),gasdata = new Array();
var valuedata;

$(function(){
	mqttTest();
	//websocket();
	localdata();
})

//保存上个页面的值
function localdata(){
	valuedata = $.query.get("valuedata");
    var textdata = $.query.get("textdata");
    var data = JSON.parse(textdata);
    $("#productDrawNo").val(data.productDrawNo);
    $("#cardnumber").val(data.cardId);
    $("#employee").val(data.employeeId);
    $("#machineNumber").val(data.machId);
    $("#productNumber").val(data.productId);
    $("#junction").val(data.junctionId);
    $("#step").val(data.stepId);
    $("#welder").val(data.welderId);
}

//点击任务信息按钮
function mission_onclick(){
    window.location.href="tongji.html?name="+name+"&userid="+userid+"&qualification="+qualification+"";
}

//点击实时监控按钮
function indata_onclick(){
    window.location.href="tubiao.html?name="+name+"&userid="+userid+"&qualification="+qualification+"";
}

//点击退出按钮
function out_onclick(){
    window.location.href="login.html";
}

//websocket通知
function websocket() {
  if (typeof (WebSocket) == "undefined") {
      WEB_SOCKET_SWF_LOCATION = "resources/js/WebSocketMain.swf";
      WEB_SOCKET_DEBUG = true;
  }
  if (typeof (WebSocket) == "undefined") {
		WEB_SOCKET_SWF_LOCATION = "resources/js/WebSocketMain.swf";
		WEB_SOCKET_DEBUG = true;
	}
	createWebSocket();
}

function createWebSocket() {
  try {
  	socket = new WebSocket("ws://10.137.14.2:5550");
  	webclient();
  } catch(e) {
  	console.log('catch');
  	reconnect();
  }
}

function webclient() {
	socket.onopen = function() {
		lockReconnect = false;
	};
	socket.onmessage = function(msg) {
		redata = msg.data;
		dataView();
	};
	//关闭事件
	socket.onclose = function(e) {
		if(lockReconnect == true){
			return;
		};
		reconnect();
	};
	//发生了错误事件
	socket.onerror = function(e) {
		if(lockReconnect == true){
			return;
		};
		reconnect();
	}
}

function reconnect(){
	if(lockReconnect == true){
		return;
	};
	lockReconnect = true;
  var tt = window.setInterval(function () {
  	if(lockReconnect == false){
  		window.clearInterval(tt);
  	}
  	try {
  		createWebSocket();
		} catch (e) {
			console.log(e.message);
		}
  }, 10000);
}

function dataView(){
var machid;
 machid =  JSON.parse(valuedata).machId;
	if(machid.length!=4){
	var lenth=4-machid.length;
	for(var i1=0;i1<lenth;i1++){var
		machid="0"+machid;
			}
	}
if(redata.substring(4, 8)== machid ){
	for (var i = 0; i < redata.length; i += 135) {
        	if(eledata.length>10){
        		eledata.shift();
        	}
        	if(voldata.length>10){
        		voldata.shift();
        	}
        	if(gasdata.length>10){
        		gasdata.shift();
        	}
	        //data.push(Math.random()*1000);
        gasdata.push({
		        	value: [
		        		redata.substring(54+i, 73+i),
		                parseFloat(redata.substring(20+i, 24+i))/10
		            ]
        		}
        		);
	    eledata.push(
	    		{
		        	value: [
		        		redata.substring(54+i, 73+i),
		        		parseFloat(redata.substring(38+i, 42+i))
		            ]
        		}
	    	);
	    voldata.push(
	    		{
		        	value: [
		        		redata.substring(54+i, 73+i),
		        		parseFloat(redata.substring(42+i, 46+i))/10
		            ]
        		}
	    		);
	    $("#weldele").val(parseFloat(redata.substring(38+i, 42+i)));
	    $("#weldvol").val(parseFloat(redata.substring(42+i, 46+i))/10);
	    $("#weldgas").val(parseFloat(redata.substring(20+i, 24+i))/10);
	    myChartele.setOption({
	    	xAxis: {
	            type: 'time'
	        },
	        yAxis: {
	            type: 'value'
	        },
	        series: [{
	            data: eledata,
                type: 'line',
                areaStyle: {
                        color: 'green'    //折线区域的背景颜色
                    }
	        }]
	    });
	    myChartvol.setOption({
	    	xAxis: {
	            type: 'time'
	        },
	        yAxis: {
	            type: 'value'
	        },
	        series: [{
	            data: voldata,
                type: 'line',
                areaStyle: {
                        color: 'blue'    //折线区域的背景颜色
                    }
	        }]
	    });
	    myChartflow.setOption({
	    	xAxis: {
	            type: 'time'
	        },
	        yAxis: {
	            type: 'value'
	        },
	        series: [{
	            data: gasdata,
                type: 'line',
                areaStyle: {
                        color: 'purple'    //折线区域的背景颜色
                    }
	        }]
	    });

	    var preele = $("#preele").val();
	    var prevol = $("#prevol").val();
	    var pregas = $("#pregas").val();
	    if(preele!=""){
	    	mainelerow.setOption({
		        series: [{
		            data: [{
		            	name: "电流偏移度",
	        			value: ((parseFloat(redata.substring(38+i, 42+i))-preele)/preele*100).toFixed(1)
		            }],
		        }]
		    });
	    }
	    if(prevol!=""){
	    	mainvolrow.setOption({
		        series: [{
		            data: [{
		            	name: "电压偏移度",
	        			value: ((parseFloat(redata.substring(42+i, 46+i))/10-prevol)/prevol*100).toFixed(1)
		            }],
		        }]
		    });
	    }
	    if(pregas!=""){
	    	 mainflowrow.setOption({
	 	        series: [{
	 	            data: [{
	 	            	name: "气流量偏移度",
	         			value: ((parseFloat(redata.substring(20+i, 24+i))-pregas)/pregas*100).toFixed(1)
	 	            }],
	 	        }]
	 	    });
	    }
	    }
}
} 

var client,clientId;
function mqttTest(){
	clientId = Math.random().toString().substr(3,8) + Date.now().toString(36);
	client = new Paho.MQTT.Client("10.137.14.2:8083".split(":")[0], parseInt("10.137.14.2:8083".split(":")[1]), clientId);
	var options = {
        timeout: 5,  
        keepAliveInterval: 60,  
        cleanSession: false,  
        useSSL: false,  
        onSuccess: onConnect,  
        onFailure: function(e){  
            console.log(e);  
        },
        reconnect : true
	}
	
	//set callback handlers
	client.onConnectionLost = onConnectionLost;
	client.onMessageArrived = onMessageArrived;

	//connect the client
	client.connect(options);
}

//called when the client connects
function onConnect() {
	// Once a connection has been made, make a subscription and send a message.
	console.log("onConnect");
//	client.publish('/public/TEST/SHTH', 'SHTHCS', 0, false);
	client.subscribe("weldmesrealdata"/*, {
			qos: 0,
			onSuccess:function(e){  
	            console.log("订阅成功");  
				var loadingMask = document.getElementById('loadingDiv');
				loadingMask.parentNode.removeChild(loadingMask);
	        },
	        onFailure: function(e){  
	            console.log(e);  
				var loadingMask = document.getElementById('loadingDiv');
				loadingMask.parentNode.removeChild(loadingMask);
	        }
		}*/)
}

//called when the client loses its connection
function onConnectionLost(responseObject) {
	if (responseObject.errorCode !== 0) {
		console.log("onConnectionLost:"+responseObject.errorMessage);
	}
}

//called when a message arrives
function onMessageArrived(message) {
//	console.log("onMessageArrived:"+message.payloadString);
	redata = message.payloadString;
	dataView();
/*	message = new Paho.MQTT.Message("1");
	message.destinationName = "api2";
	client.send(message);*/
}

function gotaskdata(){
	window.location.href="tongji.html";
	sessionStorage.setItem("valuedata", valuedata);
}