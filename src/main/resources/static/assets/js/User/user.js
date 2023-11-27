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
    post('http://localhost:8080/user/do-login',{
        username:username,
        password:password,
        rememberMe:rememberMe
    },function (data) {
        if(data.code === 200){
            window.location="http://localhost:8080/common/index"
        }else {
            new Prompt("用户名或密码错误！")
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
    post("http://localhost:8080/user/forgetPassword",{
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
    Get('http://localhost:8080/user/verify-code',{
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
    post('http://localhost:8080/user/do-register',{
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