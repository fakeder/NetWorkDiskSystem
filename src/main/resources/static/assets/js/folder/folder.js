//创建文件夹
function mkdir(){
    let folderName=$("#folder").val().trim();
    if(folderName === ""){
        new Prompt("文件夹名称不能为空！")
        return;
    }
    post(host+'/folder/mkdir',{
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
        post(host+'/folder/folderRename',{
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
    new Warning("删除目录","确定删除该目录吗？",function (){
        post(host+'/folder/deleteFolder', {
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
    })
}

//移动目录——切换目录
function fileMoveGetFolder(mid,flag){
    console.log("mid:"+mid)

    let folderName=getFolderName(mid,flag);
    $("#folderName").text(folderName)
    //返回上一级
    if(flag === 1){
        get(host+"/folder/getLastMid?mid="+mid,function (data) {
            $("#fileMoveMid").val(mid);
            $("#fileMoveLastMid").val(data);
        })
    }else {//下一级目录
        //获取上目录一个mid
        let lastMid=$("#fileMoveMid").val();
        if(lastMid != mid){
            //将当前目录的id赋值
            $("#fileMoveMid").val(mid);
            //将上级目录的id赋值
            $("#fileMoveLastMid").val(lastMid);
        }
    }
    get(host+"/folder/folderShow?mid="+mid,function (list) {
        let modal = $("div#moveFile.modal.fade");
        // 清空动态列表
        modal.find('#move_folders').empty();
        //遍历传入的list，将list中的目录信息
        $.each(list, function (index, item) {
            //创建<tr>
            let listItem = $('<tr>').attr('onclick', 'fileMoveGetFolder(' + item.mid + ')')
            //创建<td>
            let td1 = $('<td>');
            //创建<a>,添加href,id属性
            let a = $('<a>').text(item.folderName).attr('href', 'javascript:fileMoveGetFolder(' + item.mid + ')').attr('id', item.mid)
            //将<a>标签添加到td1标签内部
            td1.append(a);
            //将td1放入<tr>标签内部
            listItem.append(td1);
            //创建<td>
            let td2 = $('<td>');
            let td3 = $('<td>');
            let td4 = $('<td>');
            let td5 = $('<td>');
            let td6 = $('<td>');
            //将td放入<tr>标签内部
            listItem.append(td2);
            listItem.append(td3);
            listItem.append(td4);
            listItem.append(td5);
            listItem.append(td6);
            modal.find('#move_folders').append(listItem);
        });
        $("#moveFile").modal('show');
    })
}

//移动目录--返回上一级目录
function returnLastFolder(){
    //获取上一级目录id
    let lastMid=$("#fileMoveLastMid").val();
    if( lastMid == ''){
        let mid=$("#fileMoveMid").val()
        get(host+"/folder/getLastMid?mid="+mid,function (data) {
            lastMid = data
        })
    }
    if(lastMid >0){
        fileMoveGetFolder(lastMid,1)
    }
}

//移动文件--获取目录名称
function getFolderName(mid,flag) {
    if(flag === 0){
       return  "/";
    }else {
        get(host+"/folder/getFolderName?mid="+mid, function (data) {
           return ""+data;
        })
    }
}