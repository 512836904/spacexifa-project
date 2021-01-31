(function(){

})

//点击登录按钮 模拟请求服务器
function login_onclick(){
    //json登陆协议
    var serreq = {
        type: "loginrequest",
        data: {
            username: document.getElementById("user").value,
            password: document.getElementById("password").value
        }
    }
    //将JSON转为字符串存到变量里
    var serreqstr = JSON.stringify(serreq);

    $.ajax({
        type:"GET",
        async:true,
        dataType:'jsonp',
        jsonp: 'jsonpCallback',
        data:{json:serreqstr},
        url:"http://localhost:8080/SPACEXIFA/terminal/login",
        success:function (loginrespon) {
            //模拟请求服务器返回数据
            //var loginrespon = loginreq(serreqstr);

            //获取返回类型
            var respondtype = loginrespon.respondtype;
            if(respondtype == "default"){
                alert("用户名密码错误");
            }else if(respondtype == "succeed"){
                var name = loginrespon.data.name;
                var userid = loginrespon.data.userid;
                var qualification = loginrespon.data.qualification;
                window.location.href="tongji.html?name="+name+"&userid="+userid+"&qualification="+qualification+"";
                alert("登陆成功");
            }
        },
        error:function(e){
            console.log(e);
        }
    });

    
}

//模拟服务器验证登陆
// function loginreq(serreqstr){
//     var serreqstrdata =  JSON.parse(serreqstr) //转为JSON

//     //处理登陆验证
//     var type = serreqstrdata.type;
//     if(type == "loginrequest"){
//         var username = serreqstrdata.data.username;
//         var password = serreqstrdata.data.password;

//         if(username == "root" && password == "123456"){
//             //返回有此用户
//             var serrespon = {
//                 type: "loginrespond",
//                 respondtype: "succeed",
//                 data:{
//                     "datalength":"3",
//                     "name": "张三",
//                     "userid": "1234",
//                     "qualification": "1"
//                 }
//             }
//         }else{
//             //返回无此用户
//             var serrespon = {
//                 type: "loginrespond",
//                 respondtype: "default",
//                 data:{
//                     "datalength":"0"
//                 }
//             }
//         }
//         return(serrespon);
//     }
// }