//文件重命名
function rename(fid){
    let filename=$("#F"+fid).text()
    $("#F"+fid).text('')
    $("#F"+fid).attr("type","hidden")

    $("#f"+fid).attr("type","text")
    $("#f"+fid).change(function (){
        let newFileName=$("#f"+fid).val().trim();
        if(newFileName === ""){
            new Prompt("文件名不能为空！")
            $("#F"+fid).text(filename)
            $("#F"+fid).attr("type","text")
            $("#f"+fid).attr("type","hidden")
            return;
        }
        post(host+'/file/rename',{
            filename:newFileName,
            Fid:fid
        },function (data){
            if (data.code === 200){
                new Prompt(data.reason)
                new TimeOutReload(1000)
            }else {
                new Prompt(data.reason)
                new TimeOutReload(1000)
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
        new Prompt("请选择要上传的文件！")
        return;
    }
    $.ajax({
        url: host+'/file/fileUpload', // 请求的URL
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
                new Prompt(R.reason);
                new TimeOutReload(1000)
            }else {
                new Prompt(R.reason);
                new TimeOutReload(1000)
            }
        }
    });

}
//文件下载
function download(fid){
   window.location=host+'/file/download?fid='+fid
}
//文件删除
function deleteFile(fid){
    Warning("删除文件","确定删除该文件？",function () {
        post(host+"/fileShare/fileShareCheck",{
            fid:fid
        },function (data){
            if(data.code === 200){
                get(host+'/file/delete?fid=' + fid, function (data) {
                    if (data.code === 200) {
                        new Prompt(data.reason)
                        new TimeOutReload(1000)
                    } else {
                        new Prompt("未知错误！删除失败！")
                        new TimeOutReload(1000)
                    }
                })
            } else {
                new Prompt(data.reason);
            }
        })

    })
}