$(function (){
    //延迟2秒初始化数据
    setTimeout(function(){
        loadLeft1(0);
        loadWorkRankData(0);
        loadWorkGas(0,0);
        loadWorkWelders(0,0);
        loadWorkMaterals(0,0);
        loadWorkRadios(0,0);
        loadJobSetNumber(0);
    }, 2000);
    //创建定时器每10分钟刷新页面
    setInterval(function(){
        loadLeft1(0);
        loadWorkRankData(0);
        loadWorkGas(0,0);
        loadWorkWelders(0,0);
        loadWorkMaterals(0,0);
        loadWorkRadios(0,0);
        loadJobSetNumber(0);
    }, 600000);
});

//当前时间
var nowDatetime = 0;
//当前组织机构
var nowOrganization = 0;

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

function loadLeft1(day){
    //查询焊工焊材消耗量排行
    if (day != null) {nowDatetime = day}
    var data = [];
    var dataList = [];
    $.ajax({
        type: "post",
        async: true,
        url: "frontEnd/findWelderMaterialConsume",
        data: {
            startTime: nowDatetime
        },
        dataType: "json", //返回数据形式为json
        success: function (result) {
            if (result) {
                var Left1 = echarts.init(document.querySelector("#Left1"));
                var option = Left1.getOption();
                for (var i in result.ary) {
                    dataList.push(result.ary[i].wirefeedrate);
                    data.push(result.ary[i].name);
                }
                option.yAxis[0].data = data;
                option.yAxis[1].data = dataList;
                option.series[0].data = dataList;
                Left1.setOption(option);
            }
        }
    });
}

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
            startTime: nowDatetime
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
    });
}

function loadWorkGas(day,organization){
    if (day != null) {nowDatetime = day};
    if (organization != null) {nowOrganization = organization};
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
                    teamgas.setOption(option);
                }
            }
        }
    });
}

function loadWorkWelders(day,organization){
    if (day != null) {nowDatetime = day};
    if (organization != null) {nowOrganization = organization};
    //异步加载人均工作时间排行
    var ins = [];
    var data = [];
    $.ajax({
        type: "post",
        async: true,
        url: "frontEnd/findAverageWorkingTime",
        data: {
            startTime: nowDatetime,
            organization: nowOrganization
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
                    // option.series[2].data = data;
                    workwelders.setOption(option);
                }else {
                    var teamwelder = echarts.init(document.querySelector("#teamwelder"));
                    var option = teamwelder.getOption();
                    option.xAxis[0].data = ins;
                    option.xAxis[0].axisLabel.formatter =
                    function(value) {
                        return value.split("").join("\n");
                    }
                    option.series[0].data = data;
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
                    // option.series[2].data = data;
                    teamwelder.setOption(option);
                }
            }
        }
    });
}

function loadWorkMaterals(day,organization){
    if (day != null) {nowDatetime = day};
    if (organization != null) {nowOrganization = organization};
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
    if (day != null) {nowDatetime = day};
    if (organization != null) {nowOrganization = organization};
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