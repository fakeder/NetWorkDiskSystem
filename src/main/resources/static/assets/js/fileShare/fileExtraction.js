//文件提取
function fileExtraction(){
    let shareCode=$("#shareCode").val().trim();
    if(shareCode === ''){
        $("#shareCode").val('');
        new Prompt("提取码为空");
        return;
    }
    post(host+"/fileExtraction/checkShareCode",{
        shareCode:shareCode
    },function (data){
        if(data.code === 200){
            $("#fileExtractionUserName").text(data.username);
            $("#fileExtractionUserImage").attr("src",data.userImage);
            $("#fileExtractionFileImage").attr("src",data.fileImage);
            $("#fileExtractionFileName").text(data.fileName);
            $("#fileExtractionSize").text(data.fileSize);
            $("#fileExtractionTime").text(data.time);
            $("#fileExtractionFid").val(data.fid);
            $("#fileExtraction").modal('show');
        } else {
            $("#fileExtractionUserName").text("");
            $("#fileExtractionUserImage").attr("src","");
            $("#fileExtractionFileImage").attr("src","");
            $("#fileExtractionFileName").text("");
            $("#fileExtractionSize").text("");
            $("#fileExtractionTime").text("");
            new Prompt(data.reason)
        }
    })
}
//直接下载到本地
function fileExtractionDownload(){
    let fid=$("#fileExtractionFid").val();
    let shareCode=$("#shareCode").val();
    let username=$("#fileExtractionUserName").text();
    post(host+"/fileExtraction/updateNumberOfDownload",{
        fid:fid,
        shareCode:shareCode
    },function (data){
        if(data.code === 200){
            window.location=host+'/file/fileExtractionDownload?fid='+fid+"&username="+username;
        } else {
          new Prompt(data.reason);
        }
    })
}