//文件分享记录更新
function fileShareChange(shareId,Condition,shareCode) {
    let title,text;
    if(Condition === '分享中'){
        title='停止分享'
        text='确定停止分享?'
    }else {
        title='删除记录'
        text='确定删除此条记录?'
    }
    Warning(title,text,function (){
        if(Condition === '分享中') {
            post(host+"/fileShare/stopSharing",{
                shareId:shareId,
                shareCode:shareCode
            },function (data) {
                new Prompt(data.reason)
            })
        }else {
            post(host+"/fileShare/delShare",{
                shareId:shareId
            },function (data) {
                new Prompt(data.reason)
            })
        }
        new TimeOutReload(1000)
    })
}