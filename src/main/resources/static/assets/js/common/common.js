//ip地址
const host="http://192.168.0.112:8080"

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

/**
 * 警告框.
 * @param title 标题
 * @param reason 内容
 * @param func 点击确定后执行的函数方法
 * @constructor
 */
function Warning(title,reason,func){
    $("#warning-Message-title").text(title)
    $("#warning-Message-body").text(reason)
    $("#warning").modal('show')
    $("#warning-determine").on('click',func)
}

/**
 * 提示框.
 * @param reason 内容
 * @constructor
 */
function Prompt(reason){
    $("#prompt-Message-body").text(reason)
    $("#prompt").modal('show')

}

/**
 * 延时刷新界面.
 * @param time 延时时间（毫秒）
 * @constructor
 */
function TimeOutReload(time){
    setTimeout(function (){
        location.reload()
    },time)
}
//退出登录
function logout(){
    new Warning("退出警告！","确定退出？",function (){
            window.location=host+"/user/logout"
    })
}