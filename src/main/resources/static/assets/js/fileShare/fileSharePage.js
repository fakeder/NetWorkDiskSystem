//文件分享页面跳转
function share(fid,filename,fileSize){
    post(host+"/fileShare/fileShareCheck",{
        fid:fid
    },function (data){
        if(data.code === 200){
            $("#fileName").val(filename);
            $("#fileId").val(fid);
            $("#fileSize").text(fileSize)
            $("#fileSharePage").modal('show')
            $("#shareBtn").on('click',function (){
                fileSharedComment();
            })
        } else {
          new Prompt(data.reason);
        }
    })
}
//下载次数变换
function downloadTypeChange(){
    let type=$("#downloadType option:selected").val();
    if(type === "0"){
        $("#downloadNumber").val('无限制')
        $("#downloadNumber").prop("disabled",true);
    }else {
        $("#downloadNumber").val('')
        $("#downloadNumber").prop("disabled",false);
    }
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
    post(host+"/fileShare/toFileShare",{
        fileId:$("#fileId").val(),
        filename:Filename,
        fileSize:FileSize,
        expirationTime:ExpirationTime,
        downloadType:DownloadType,
        downloadNumber:DownloadNumber
    },function (data){
        if(data.code === 200){
            new Prompt("分享码:"+data.reason);
        }else {
            new Prompt(data.reason);
        }
    })
}