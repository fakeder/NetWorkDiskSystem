//下载次数变换
function downloadTypeChange(){
    let type=$("#downloadType option:selected").val();
    if(type === "0"){
        $("#downloadNumber").prop("disabled",true);
    }else {
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
    post("http://localhost:8080/fileShare/toFileShare",{
        fileId:$("#fileId").val(),
        filename:Filename,
        fileSize:FileSize,
        expirationTime:ExpirationTime,
        downloadType:DownloadType,
        downloadNumber:DownloadNumber
    },function (R){
        new Prompt("分享码:"+R.reason);
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
                new Prompt(data.reason)
            })
        }else {
            post("http://localhost:8080/fileShare/delShare",{
                shareId:shareId
            },function (data) {
                new Prompt(data.reason)
            })
        }
        new TimeOutReload(1000)
    }

}