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