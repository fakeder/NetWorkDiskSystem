function login(){
    let username=$("#username").val();
    let password=$("#password").val();
    let rememberMe=$('input[name=rememberMe]').is(':checked');
    if(username === ""){
        new Prompt("请输入用户名");
        return;
    }
    if(password === ""){
        new Prompt("请输入密码")
        return;
    }
    post(host+'/user/do-login',{
        username:username,
        password:password,
        rememberMe:rememberMe
    },function (data) {
        if(data.code === 200){
            window.location=host+"/common/index"
        }else {
            new Prompt(data.reason)
            $("#username").val(username);
            $("#password").val('');
        }
    })
}

//忘记密码
function forgetPassword(){
    let password=$("#password").val();
    let email=$("#input-email").val();
    let verify=$("#verify").val();
    if(email === ''){
        new Prompt("请输入邮箱！")
        return;
    }
    if(verify === ''){
        new Prompt("请输入验证码！")
        return;
    }
    if (password === ''){
        new Prompt("请输入密码！")
        return;
    }
    post(host+"/user/forgetPassword",{
        email:email,
        verify:verify,
        password:password
    },function (data){
        if(data.code === 200){
            new Prompt(data.reason)
            window.location="login.html "
        }else {//失败
            new Prompt(data.reason)
        }
    })
}
//发送验证码
function askVerifyCode(){
    let email=$("#input-email").val();
    if (email === ''){
        new Prompt("请输入邮箱！")
        return;
    }
    sendMessage("message")
    Get(host+'/user/verify-code',{
        email:email
    }, function (data){
        new Prompt(data.reason)
    })
}
//注册
function register(){
    let username=$("#username").val();
    let password=$("#password").val();
    let email=$("#input-email").val();
    let verify=$("#verify").val();
    if(username === ''){
        new Prompt("请输入用户名!")
        return;
    }
    if (password === ''){
       new Prompt("请输入密码!")
        return;
    }
    if(email === ''){
        new Prompt("请输入邮箱！")
        return;
    }
    if(verify === ''){
        new Prompt("请输入验证码！")
        return;
    }
    post(host+'/user/do-register',{
        username:username,
        password:password,
        email:email,
        verify:verify
    },function (data){
        if(data.code===200){
            new Prompt(data.reason)
            setTimeout(function (){window.location='login.html'},1000)
        }else {
            new Prompt(data.reason)
        }
    })
}

//点击按钮后60秒倒计时
function sendMessage(btn) {
    const count = 60; // 间隔函数，1秒执行
    let InterVal; // timer变量，控制时间
    // 按钮点击倒计时，限制点击
    let curCount = count;
    //设置button效果，开始计时
    $("#"+btn).attr("disabled",true);
    $("#"+btn).text(curCount + "秒再获取");
    InterVal = window.setInterval(function (){
        if (curCount == 0) {
            window.clearInterval(InterVal); //停止计时器
            $("#"+btn).attr("disabled",false); //启用按钮
            $("#message").text("重新发送");
        } else {
            curCount--;
            $("#"+btn).text(curCount + "秒再获取");
        }
    }, 1000); //启动计时器，1秒执行一次
}