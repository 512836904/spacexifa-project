$(function (){
    //延迟2秒初始化数据
    setTimeout(function(){
        loadWorkRankData(0);
        loadWorkGas(0,0);
        loadWorkAreas(0,0);
        loadWorkWelders(0,0);
        loadWorkMaterals(0,0);
        loadWorkRadios(0,0);
        loadJobSetNumber(0);
    }, 2000);
    //创建定时器每10分钟刷新页面
    setInterval(function(){
        var one_day = $('#one_day').css('display');
        if(one_day=='none'){
            loadWorkRankData(1);
        }else{
            loadWorkRankData(0);
        }
        var one_day4 = $('#one_day4').css('display');
        var vchart1 = $('#vchart1').css('display');
        if(one_day4=='none' && vchart1=='none'){
            loadWorkGas(1, 1);
        }else if(one_day4=='none' && vchart1=='block'){
            loadWorkGas(1, 0);
        }else if(one_day4=='block' && vchart1=='none'){
            loadWorkGas(0, 1);
        }else{
            loadWorkGas(0, 0);
        }
        var pchart1 = $('#pchart1').css('display');
        var one_day2 = $('#one_day2').css('display');
        if(one_day2=='none' && pchart1=='none'){
            loadWorkAreas(1, 1);
        }else if(one_day2=='none' && pchart1=='block'){
            loadWorkAreas(1, 0);
        }else if(one_day2=='block' && pchart1=='none'){
            loadWorkAreas(0, 1);
        }else{
            loadWorkAreas(0, 0);
        }
        var one_day6 = $('#one_day6').css('display');
        var vchart3 = $('#vchart3').css('display');
        if(one_day6=='none' && vchart3=='none'){
            loadWorkWelders(1, 1);
        }else if(one_day6=='none' && vchart3=='block'){
            loadWorkWelders(1, 0);
        }else if(one_day6=='block' && vchart3=='none'){
            loadWorkWelders(0, 1);
        }else{
            loadWorkWelders(0, 0);
        }
        var one_day8 = $('#one_day8').css('display');
        var vchart5 = $('#vchart5').css('display');
        if(one_day8=='none' && vchart5=='none'){
            loadWorkMaterals(1, 1);
        }else if(one_day8=='none' && vchart5=='block'){
            loadWorkMaterals(1, 0);
        }else if(one_day8=='block' && vchart5=='none'){
            loadWorkMaterals(0, 1);
        }else{
            loadWorkMaterals(0, 0);
        }
        var one_day10 = $('#one_day10').css('display');
        var vchart7 = $('#vchart7').css('display');
        if(one_day10=='none' && vchart7=='none'){
            loadWorkRadios(1, 1);
        }else if(one_day10=='none' && vchart7=='block'){
            loadWorkRadios(1, 0);
        }else if(one_day10=='block' && vchart7=='none'){
            loadWorkRadios(0, 1);
        }else{
            loadWorkRadios(0, 0);
        }
        var one_day12 = $('#one_day12').css('display');
        if(one_day12=='none'){
            loadJobSetNumber(1);
        }else{
            loadJobSetNumber(0);
        }
    }, 600000);
});
//当前时间
var nowDatetime = 0;
//当前组织机构
var nowOrganization = 0;

function loadWorkRankData(day){
    if (day != null) {nowDatetime = day};
    //异步加载焊工工作时间排行
    var dataList = [];
    var data = [];
    $.ajax({
        type: "post",
        async: true,
        url: "frontEnd/getWorkRank",
        data: {
            startTime: nowDatetime,
            standbyTime: $("#standbyTime").val()
        },
        dataType: "json", //返回数据形式为json
        success: function (result) {
            if (result) {
                var Left2 = echarts.init(document.querySelector("#Left2"));
                var option = Left2.getOption();
                for (var i in result.rows) {
                    dataList.push(result.rows[i].hour);
                    data.push(result.rows[i].name);
                }
                option.yAxis[0].data = data;
                option.yAxis[1].data = dataList;
                option.series[0].data = dataList;
                Left2.setOption(option);
            }
        }
        ,error: function (e) {
            alert("加载失败！");
        }
    });
}

function loadWorkGas(day,organization){
    var nowzation = 0;
    if (day != null) {nowDatetime = day};
    if (organization != null) {
        nowOrganization = organization
    }else{
        nowOrganization = nowzation;
    }
    //异步加载电能气体消耗量
    var ins = [];
    var data1 = [];
    var data2 = [];
    $.ajax({
        type: "post",
        async: true,
        url: "frontEnd/findElectricityAndGas",
        data: {
            startTime: nowDatetime,
            organization: nowOrganization
        },
        dataType: "json", //返回数据形式为json
        success: function (result) {
            if (result) {
                for (var i in result.rows) {
                    ins.push(result.rows[i].t0);
                    data1.push(result.rows[i].t10);
                    data2.push(result.rows[i].t11);
                }
                if (nowOrganization === 0){
                    var workgas = echarts.init(document.querySelector("#workgas"));
                    var option = workgas.getOption();
                    option.xAxis[0].data = ins;
                    option.series[0].data = data1;
                    option.series[1].data = data2;
                    if(data1.length>0){
                        option.yAxis[0].max=Math.ceil(Math.max(...data1));
                        option.yAxis[0].min=0;
                        option.yAxis[0].interval=Math.ceil(Math.max(...data1)/5);
                    }if(data2.length>0){
                        option.yAxis[1].max=Math.ceil(Math.max(...data2));
                        option.yAxis[1].min=0;
                        option.yAxis[1].interval=Math.ceil(Math.max(...data2)/5);
                    }
                    workgas.setOption(option);
                }else {
                    var teamgas = echarts.init(document.querySelector("#teamgas"));
                    var option = teamgas.getOption();
                    option.xAxis[0].data = ins;
                    option.xAxis[0].axisLabel.formatter =
                    function(value) {
                        return value.split("").join("\n");
                    }
                    option.series[0].data = data1;
                    option.series[1].data = data2;
                    if(data1.length>0){
                        option.yAxis[0].max=Math.ceil(Math.max(...data1));
                        option.yAxis[0].min=0;
                        option.yAxis[0].interval=Math.ceil(Math.max(...data1)/5);
                    }if(data2.length>0){
                        option.yAxis[1].max=Math.ceil(Math.max(...data2));
                        option.yAxis[1].min=0;
                        option.yAxis[1].interval=Math.ceil(Math.max(...data2)/5);
                    }
                    teamgas.setOption(option);
                }
            }
        }
    });
}

function loadWorkAreas(day,organization){
    var nowzation =0;
    if (day != null) {nowDatetime = day};
    if (organization != null) {nowOrganization = organization}
    else{
        nowOrganization = nowzation;
    };
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
                    var workareas = echarts.init(document.querySelector("#workareas"));
                    var option = workareas.getOption();
                    option.series[0].data = passRate;
                    option.xAxis[0].data = data;
                    workareas.setOption(option);
                }else {
                    var teamworks = echarts.init(document.querySelector("#teamworks"));
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

function loadWorkWelders(day,organization){
    var nowzation = 0;
    if (day != null) {nowDatetime = day};
    if (organization != null) {
        nowOrganization = organization
    } else{
        nowOrganization=nowzation;
    };
    //异步加载工区人均工作时间
    var ins = [];
    var data = [];
    $.ajax({
        type: "post",
        async: true,
        url: "frontEnd/findAverageWorkingTime",
        data: {
            startTime: nowDatetime,
            organization: nowOrganization,
            standbyTime: $("#standbyTime").val()
        },
        dataType: "json", //返回数据形式为json
        success: function (result) {
            if (result) {
                for (var i in result.ary) {
                    data.push(result.ary[i].rate);
                    ins.push(result.ary[i].name);
                }
                if (nowOrganization === 0){
                    var workwelders = echarts.init(document.querySelector("#workwelders"));
                    var option = workwelders.getOption();
                    option.xAxis[0].data = ins;
                    option.series[0].data = data;
                    // option.series[2].data = data;
                    // //var rangeY = workwelders.getModel().getComponent('yAxis').axis.scale._extent;//y轴刻度最大最小值
                    // var datamap = data.map(function (x){
                    //     if (x == 0 || x == '' || x == null){
                    //         //如果柱状图数据为0，清除顶部长度
                    //         option.series[2].itemStyle.borderWidth = 0;
                    //         return 0;
                    //     }else {
                    //         x = Number(x + 0.23);
                    //         let arr = x.toString().split(".");
                    //         if (arr[1] && arr[1].length > 2){
                    //             x = x.toFixed(2); //截取小数点后两位
                    //         }
                    //         return Number(x);
                    //     }
                    // });
                    // option.series[1].data = datamap;
                    workwelders.setOption(option);
                }else {
                    var teamwelder = echarts.init(document.querySelector("#teamwelder"));
                    var option = teamwelder.getOption();
                    option.xAxis[0].data = ins;
                    option.xAxis[0].axisLabel.formatter =
                    function(value) {
                        return value.split("").join("\n"); //班组竖直方向展示
                    }
                    option.series[0].data = data;
                    // option.series[2].data = data;
                    // var datamap = data.map(function (x){
                    //     if (x == 0 || x == '' || x == null){
                    //         //如果柱状图数据为0，清除顶部长度
                    //         option.series[2].itemStyle.borderWidth = 0;
                    //         return 0;
                    //     }else {
                    //         x = Number(x + 0.23);
                    //         let arr = x.toString().split(".");
                    //         if (arr[1] && arr[1].length > 2){
                    //             x = x.toFixed(2); //截取小数点后两位
                    //         }
                    //         return Number(x);
                    //     }
                    // });
                    // option.series[1].data = datamap;
                    teamwelder.setOption(option);
                }
            }
        }
    });
}

function loadWorkMaterals(day,organization){
    var nowzation=0;
    if (day != null) {nowDatetime = day};
    if (organization != null) {nowOrganization = organization
    }else{
        nowOrganization=nowzation;
    };
    //异步加载工区耗材消耗量
    var ins = [];
    var data1 = [];
    $.ajax({
        type: "post",
        async: true,
        url: "frontEnd/findWireConsumption",
        data: {
            startTime: nowDatetime,
            organization: nowOrganization
        },
        dataType: "json", //返回数据形式为json
        success: function (result) {
            if (result) {
                for (var i in result.rows) {
                    ins.push(result.rows[i].t0);
                    data1.push(result.rows[i].t9);
                }
                if (nowOrganization === 0){
                    var workmaterals = echarts.init(document.querySelector("#workmaterals"));
                    var option = workmaterals.getOption();
                    option.xAxis[0].data = ins;
                    option.series[0].data = data1;
                    workmaterals.setOption(option);
                }else {
                    var teammaterals = echarts.init(document.querySelector("#teammaterals"));
                    var option = teammaterals.getOption();
                    option.xAxis[0].data = ins;
                    option.xAxis[0].axisLabel.formatter =
                    function(value) {
                        return value.split("").join("\n");
                    }
                    option.series[0].data = data1;
                    teammaterals.setOption(option);
                }
            }
        }
    });
}
function loadWorkRadios(day,organization){
    var nowzation=0;
    if (day != null) {nowDatetime = day};
    if (organization != null) {nowOrganization = organization
    }else{
        nowOrganization=nowzation;
    };
    //异步加载工区设备使用率
    var ins = [];
    var usageRate = [];
    $.ajax({
        type: "post",
        async: true,
        url: "frontEnd/findEquipmentUseRatio",
        data: {
            startTime: nowDatetime,
            organization: nowOrganization
        },
        dataType: "json", //返回数据形式为json
        success: function (result) {
            if (result) {
                for (var i in result.rows) {
                    ins.push(result.rows[i].t0);
                    usageRate.push(result.rows[i].t4);
                }
                if (nowOrganization === 0){
                    var workradios = echarts.init(document.querySelector("#workradios"));
                    var option = workradios.getOption();
                    option.xAxis[0].data = ins;
                    option.series[0].data = usageRate;
                    workradios.setOption(option);
                }else {
                    var teamradios = echarts.init(document.querySelector("#teamradios"));
                    var option = teamradios.getOption();
                    option.xAxis[0].data = ins;
                    option.xAxis[0].axisLabel.formatter =
                    function(value) {
                        return value.split("").join("\n");
                    }
                    option.series[0].data = usageRate;
                    teamradios.setOption(option);
                }
            }
        }
    });
}


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


