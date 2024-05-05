function adminLogin(){
    let username=$("#username").val();
    let password=$("#password").val();
    if(username === ""){
        new Prompt("请输入用户名");
        return;
    }
    if(password === ""){
        new Prompt("请输入密码")
        return;
    }
    post(host+'/admin/do-login',{
        username:username,
        password:password,
    },function (data) {
        if(data.code === 200){
            window.location=host+"/admin/mangerIndex"
        }else {
            new Prompt(data.reason)
            $("#username").val(username);
            $("#password").val('');
        }
    })
}