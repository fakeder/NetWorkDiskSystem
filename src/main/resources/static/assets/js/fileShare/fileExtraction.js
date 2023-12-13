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
            $("#fileExtractionSizeByte").val(data.fileSizeByte);
            $("#fileExtractionTime").text(data.time);
            $("#fileExtractionFid").val(data.fid);
            $("#fileExtraction").modal('show');
        } else {
            $("#fileExtractionUserName").text("");
            $("#fileExtractionUserImage").attr("src","");
            $("#fileExtractionFileImage").attr("src","");
            $("#fileExtractionFileName").text("");
            $("#fileExtractionSize").text("");
            $("#fileExtractionSizeByte").val(0);
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

//保存到网盘当前目录下
function fileExtractionSave(){
    let fid=$("#fileExtractionFid").val();
    let shareCode=$("#shareCode").val();
    let username=$("#fileExtractionUserName").text();
    let fileName=$("#fileExtractionFileName").text();
    let fileSize=$("#fileExtractionSize").text();
    let fileSizeByte=$("#fileExtractionSizeByte").val();
    post(host+"/fileExtraction/updateNumberOfDownload",{
        fid:fid,
        shareCode:shareCode
    },function (data){
        if(data.code === 200){
            post(host+"/fileExtraction/saveToCurrentDirectory",{
                fid:fid,
                username:username,
                fileName:fileName,
                fileSize:fileSize,
                fileSizeByte:fileSizeByte
            },function (data){
                new Prompt(data.reason)
                if(data.code === 200){
                    new TimeOutReload(1000);
                }
            })
        } else {
            new Prompt(data.reason);
        }
    })
}