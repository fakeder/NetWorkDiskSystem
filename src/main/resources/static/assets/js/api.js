function login(){
    let username=$("#username").val();
    let password=$("#password").val();
    let rememberMe=$('input[name=rememberMe]').is(':checked');
    if(username === ""){
        alert("请输入用户名")
        return;
    }
    if(password === ""){
        alert("请输入密码")
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
            alert("用户名或密码错误！");
            location.reload();
            $("#username").val(username);
        }
    })
}

function logout(){
    if(confirm("确定退出？")){
        window.location="http://localhost:8080/user/logout"
    }
}

//文件重命名
function rename(fid){
    $("#F"+fid).prop("disabled",false)
    $("#F"+fid).change(function (){
        let newFileName=$("#F"+fid).val().trim();
        if(newFileName === ""){
            alert("文件名不能为空！")
            location.reload()
            return;
        }
        post('http://localhost:8080/file/rename',{
            filename:newFileName,
            Fid:fid
        },function (data){
            if (data.code === 200){
                alert(data.reason)
                location.reload()
            }else {
                alert(data.reason)
                location.reload()
            }
        })
    })
}
//文件夹重命名
function folderRename(mid){
    let oldFolderName=$("#m"+mid).text()
    $("#M"+mid).attr("type","text")
    $("#M"+mid).val(oldFolderName)
    $("#m"+mid).text('')
    $("#m"+mid).attr("type","hidden")
    $("#M"+mid).change(function (){
        if($("#M"+mid).val() === ""){
            alert("目录名称不能为空！")
            $("#m"+mid).text(oldFolderName)
            $("#m"+mid).attr("type","text")
            $("#M"+mid).attr("type","hidden")
            return;
        }
        post('http://localhost:8080/folder/folderRename',{
            mid:mid,
            folderName:$("#M"+mid).val()
        },function (data){
            if(data.code === 200){
                alert(data.reason)
                location.reload()
            }else {
                alert(data.reason)
                $("#m"+mid).text(oldFolderName)
                $("#m"+mid).attr("type","text")
                $("#M"+mid).attr("type","hidden")
            }
        })
    })

}
//文件上传
function fileUpload(event){
    event.preventDefault();  // 阻止表单的默认提交行为
    let file = $("#file")[0].files[0];
    let formData = new FormData();
    formData.append("file", file);
    if($("#file").val() === ''){
        alert("请选择要上传的文件！")
        return;
    }
    $.ajax({
        url: 'http://localhost:8080/file/fileUpload', // 请求的URL
        type: 'POST',
        data: formData, // 发送的数据
        processData: false, // 不要对FormData进行处理
        contentType: false, // 不要设置Content-Type请求头
        success: function(R) {
            // 请求成功后，在弹窗中显示返回的数据
            //$('#result').text(data.reason);
            if(R.reason === undefined){
                R.reason="文件上传失败！"
            }
            if(R.code === 200){
                alert(R.reason);
                location.reload()
            }else {
                alert(R.reason);
                location.reload()
            }
        }
    });

}
//文件下载
function download(fid){
    get('http://localhost:8080/file/download?fid='+fid,function (data){
        if(data.code === 200){
            alert(data.reason)
        }else {
            alert("未知错误！下载失败！")
        }
    })
}
//文件删除
function deleteFile(fid){
    let flag=confirm("确定删除该文件？")
    if(flag) {
        get('http://localhost:8080/file/delete?fid=' + fid, function (data) {
            if (data.code === 200) {
                alert(data.reason)
                location.reload()
            } else {
                alert("未知错误！删除失败！")
                location.reload()
            }
        })
    }
}
//文件夹删除
function deleteFolder(mid){
    let flag=confirm("确定删除该目录？")
    if(flag) {
        post('http://localhost:8080/folder/deleteFolder', {
            mid: mid
        }, function (data) {
            if (data.code === 200) {
                alert(data.reason)
                location.reload()
            } else if (data.code === 400) {
                alert(data.reason)
            } else {
                alert(data.reason)
            }
        })
    }
}
//查询
function find(){
    let name=$("#find").val();
    if(name === ""){
        return;
    }
    alert(name)
}
//发送验证码
function askVerifyCode(){
    let email=$("#input-email").val();
    if (email === ''){
        alert("请输入邮箱！")
        return;
    }
    Get('http://localhost:8080/user/verify-code',{
        email:email
    }, function (data){
        alert(data.reason)
    })
}
//注册
function register(){
    let username=$("#username").val();
    let password=$("#password").val();
    let email=$("#input-email").val();
    let verify=$("#verify").val();
    if(username === ''){
        alert("请输入用户名！")
        return;
    }
    if (password === ''){
        alert("请输入密码！")
        return;
    }
    if(email === ''){
        alert("请输入邮箱！")
        return;
    }
    if(verify === ''){
        alert("请输入验证码！")
        return;
    }
    post('http://localhost:8080/user/do-register',{
        username:username,
        password:password,
        email:email,
        verify:verify
    },function (data){
        if(data.code===200){
            alert(data.reason)
            window.location='login.html'
        }else {
            alert(data.reason)
        }
    })
}
//创建文件夹
function mkdir(){
    let folderName=$("#folder").val().trim();
    if(folderName === ""){
        alert("文件夹名称不能为空！")
        return;
    }
    post('http://localhost:8080/folder/mkdir',{
        folder:folderName
    },function(data){
        if(data.code === 200){
            window.alert(data.reason)
            location.reload()
        }else{
            alert(data.reason)
        }
    })
}
//忘记密码
function forgetPassword(){
    let password=$("#password").val();
    let email=$("#input-email").val();
    let verify=$("#verify").val();
    if(email === ''){
        alert("请输入邮箱！")
        return;
    }
    if(verify === ''){
        alert("请输入验证码！")
        return;
    }
    if (password === ''){
        alert("请输入密码！")
        return;
    }
    post("http://localhost:8080/user/forgetPassword",{
        email:email,
        verify:verify,
        password:password
    },function (data){
        if(data.code === 200){
            alert(data.reason)
            window.location="login.html "
        }else {//失败
            alert(data.reason)
        }
    })
}

//下载次数变换
function downloadTypeChange(){
    let type=$("#downloadType option:selected").val();
    if(type === "0"){
        $("#downloadNumber").prop("disabled",true);
    }else {
        $("#downloadNumber").prop("disabled",false);
    }
}

//文件分享
function share(fid,filename,fileSize){
    //alert("fid="+fid+"filename="+filename+"fileSize="+fileSize)
    location.href="http://localhost:8080/fileShare/fileShare?Fid="+fid+"&FileName="+filename+"&FileSize="+fileSize;
}

//文件分享表单提交
function fileSharedComment(){
    let Filename=$("#fileName").val();
    let FileSize=$("#fileSize").text();
    let ExpirationTime=$("#expirationTime").val();
    let DownloadType=$("#downloadType").val();
    let DownloadNumber;
    if(DownloadType === '0'){
        DownloadNumber=99999;
    }else {
        DownloadNumber=$("#downloadNumber").val();
    }
    post("http://localhost:8080/fileShare/toFileShare",{
        fileId:$("#fileId").val(),
        filename:Filename,
        fileSize:FileSize,
        expirationTime:ExpirationTime,
        downloadType:DownloadType,
        downloadNumber:DownloadNumber
    },function (R){
        alert("分享码:"+R.reason);
    })
}

//文件分享记录更新
function fileShareChange(shareId,Condition,shareCode) {
    let text;
    if(Condition === '分享中'){
        text='确定停止分享?'
    }else {
        text='确定删除此条记录?'
    }
    let flag=confirm(text)
    if(flag){
        if(Condition === '分享中') {
            post("http://localhost:8080/fileShare/stopSharing",{
                shareId:shareId,
                shareCode:shareCode
            },function (data) {
                alert(data.reason)
            })
        }else {
            post("http://localhost:8080/fileShare/delShare",{
                shareId:shareId
            },function (data) {
                alert(data.reason)
            })
        }
        location.reload()
    }

}



function get(url, success){
    $.ajax({
        type: "get",
        url: url,
        async: true,
        dataType: 'json',
        xhrFields: {
            withCredentials: true
        },
        success: success
    });
}

function post(url, data, success){
    $.ajax({
        type: "post",
        url: url,
        async: false,
        data: data,
        dataType: 'json',
        xhrFields: {
            withCredentials: true
        },
        success: success
    });
}

function Get(url, data, success){
    $.ajax({
        type: "get",
        url: url,
        async: true,
        data: data,
        dataType: 'json',
        xhrFields: {
            withCredentials: true
        },
        success: success
    });
}

function POST(url,data,success){
    $.ajax({
        url: url, // 请求的URL
        type: 'POST',
        data: data, // 发送的数据
        processData: false, // 不要对FormData进行处理
        contentType: false, // 不要设置Content-Type请求头
        dataType: 'json',
        xhrFields: {
            withCredentials: true
        },
        success: success,
    });
}