$(function (){
    //延迟2秒初始化数据
    setTimeout(function(){
        loadWorkAreas(0,0);
        loadJobSetNormRate(0);
        loadSupergageCumulativeNumber(0,0);
        loadSupergage(0);
    }, 2000);
    //创建定时器每10分钟刷新页面
    setInterval(function(){
        loadWorkAreas(0,0);
        loadJobSetNormRate(0);
        loadSupergageCumulativeNumber(0,0);
        loadSupergage(0);
    }, 600000);
});

//进入全屏
function requestFullScreen() {
    var de =  document.documentElement;
    if (de.requestFullscreen) {
        de.requestFullscreen();
    } else if (de.mozRequestFullScreen) {
        de.mozRequestFullScreen();
    } else if (de.webkitRequestFullScreen) {
        de.webkitRequestFullScreen();
    }
    //隐藏全屏按钮
    var quanping = document.getElementById("quanping");
    quanping.style.display="none";
}

function loadWorkAreas(day,organization){
    if (day != null) {nowDatetime = day};
    if (organization != null) {nowOrganization = organization};
    //异步加载工区焊接工艺规范符合率
    var passRate = [];
    var data = [];
    $.ajax({
        type: "post",
        async: true,
        url: "frontEnd/getLoadRate",
        data: {
            startTime: nowDatetime,
            organization: nowOrganization
        },
        dataType: "json", //返回数据形式为json
        success: function (result) {
            if (result) {
                for (var i in result.ary) {
                    passRate.push(result.ary[i].hour);
                    data.push(result.ary[i].itemname);
                }
                if (nowOrganization === 0){
                    var workareas = echarts.init(document.querySelector("#Left1"));
                    var option = workareas.getOption();
                    option.series[0].data = passRate;
                    option.xAxis[0].data = data;
                    workareas.setOption(option);
                }else {
                    var teamworks = echarts.init(document.querySelector("#Left2"));
                    var option = teamworks.getOption();
                    option.xAxis[0].data = data;
                    option.xAxis[0].axisLabel.formatter =
                    function(value) {
                        return value.split("").join("\n");
                    }
                    option.series[0].data = passRate;
                    teamworks.setOption(option);
                }
            }
        }
    });
}

function loadJobSetNormRate(day){
    if (day != null) {nowDatetime = day};
    //异步加载工作号/布套号焊接工艺规范符合率
    var data = [];
    var dataList = [];
    $.ajax({
        type: "post",
        async: true,
        url: "frontEnd/findJobSetNormRate",
        data: {
            startTime: nowDatetime
        },
        dataType: "json", //返回数据形式为json
        success: function (result) {
            if (result) {
                for (var i in result.ary) {
                    if(result.ary[i].job_number!=null){
                        //var name = result.ary[i].job_number + " " + result.ary[i].set_number + " " + result.ary[i].part_name;
                        var name = result.ary[i].job_number + " " + result.ary[i].set_number ;
                        data.push(name);
                        dataList.push(result.ary[i].normRate);
                    }
                }
                var Right1 = echarts.init(document.querySelector("#Right1"));
                var option = Right1.getOption();
                option.xAxis[0].data = data;
                option.series[0].data = dataList;
                Right1.setOption(option);
            }
        }
    });
}

function loadSupergageCumulativeNumber(day,organization){
    if (day != null) {nowDatetime = day};
    if (organization != null) {nowOrganization = organization};
    //异步加载工区超规范累计次数
    var ins = [];
    var data1 = [];
    $.ajax({
        type: "post",
        async: true,
        url: "frontEnd/findSupergageCumulativeNumber",
        data: {
            startTime: nowDatetime,
            organization: nowOrganization
        },
        dataType: "json", //返回数据形式为json
        success: function (result) {
            if (result) {
                for (var i in result.ary) {
                    ins.push(result.ary[i].name);
                    data1.push(result.ary[i].num);
                }
                if (nowOrganization === 0){
                    var workmaterals = echarts.init(document.querySelector("#Right2_1"));
                    var option = workmaterals.getOption();
                    option.xAxis[0].data = ins;
                    option.series[0].data = data1;
                    workmaterals.setOption(option);
                }else {
                    var Right2_0 = echarts.init(document.querySelector("#Right2_0"));
                    var option = Right2_0.getOption();
                    option.xAxis[0].data = ins;
                    option.xAxis[0].axisLabel.formatter =
                    function(value) {
                        return value.split("").join("\n");
                    }
                    option.series[0].data = data1;
                    Right2_0.setOption(option);
                }
            }
        }
    });
}