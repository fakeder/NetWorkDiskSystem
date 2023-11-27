//创建文件夹
function mkdir(){
    let folderName=$("#folder").val().trim();
    if(folderName === ""){
        new Prompt("文件夹名称不能为空！")
        return;
    }
    post('http://localhost:8080/folder/mkdir',{
        folder:folderName
    },function(data){
        if(data.code === 200){
            new Prompt(data.reason)
            new TimeOutReload(1000)
        }else{
            new Prompt(data.reason)
        }
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
            new Prompt("目录名称不能为空！")
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
                new Prompt(data.reason)
                new TimeOutReload(1000)
            }else {
                new Prompt(data.reason)
                $("#m"+mid).text(oldFolderName)
                $("#m"+mid).attr("type","text")
                $("#M"+mid).attr("type","hidden")
            }
        })
    })

}

//文件夹删除
function deleteFolder(mid){
    let flag=confirm("确定删除该目录？")
    if(flag) {
        post('http://localhost:8080/folder/deleteFolder', {
            mid: mid
        }, function (data) {
            if (data.code === 200) {
                new Prompt(data.reason)
                new TimeOutReload(1000)
            } else if (data.code === 400) {
                new Prompt(data.reason)
            } else {
                new Prompt(data.reason)
            }
        })
    }
}