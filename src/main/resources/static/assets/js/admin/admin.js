function adminLogin(){
    let username=$("#username").val();
    let password=$("#password").val();
    if(username === ""){
        new Prompt("请输入用户名");
        return;
    }
    if(password === ""){
        new Prompt("请输入密码")
        return;
    }
    post(host+'/admin/do-login',{
        username:username,
        password:password
    },function (data) {
        if(data.code === 200){
            window.location=host+"/admin/mangerIndex"
        }else {
            new Prompt(data.reason)
            $("#username").val(username);
            $("#password").val('');
        }
    })
}

//禁用/启用用户
function changeCondition(uid,condition){
  let conditionName;
  if(condition == 0) {
    conditionName = "启用"
  } else {
    conditionName = "禁用"
  }
  new Warning(conditionName+"用户",
      "确定"+conditionName+"该用户吗?",
      function (){
    get(host+"/admin/changeCondition?uid="+uid+"&condition="+condition,
        function (data){
          if(data.code === 200){
            new Prompt(data.reason)
            new TimeOutReload(1000)
          }else {
            new Prompt("操作失败")
            new TimeOutReload(1000)
          }
        })
  })
  
}
//删除用户
function deleteUser(uid){
  new Warning("删除用户",
      "确定删除该用户吗，这个操作不可逆的哦？",
      function (){get(host+"/admin/delete?uid="+uid,
      function (data){
        if(data.code === 200){
          new Prompt(data.reason)
          new TimeOutReload(1000)
        }else {
          new Prompt("操作失败")
          new TimeOutReload(1000)
        }
      })
  })
  
}

//查看用户详细信息
function catUserDetail(userId){
  post(host+"/admin/catUserDetail",
      {
        uid:userId
      },function (data){
        console.log(data)
        $("#username").text(data.username);
        $("#email").text(data.email);
        $("#totalSize").text(data.totalSize);
        $("#usedSize").text(data.usedSize);
        $("#fileCount").text(data.fileCount);
        $("#folderCount").text(data.folderCount);
        $("#fileShareCount").text(data.fileShareCount);
        $("#VIPLevel").val(data.vipflag)
        $("#VIPLevelOld").val(data.vipflag);
        $("#userId").val(data.uid)
        if(data.vipflag !== 0){
          $("#VIPFlag").val(1)
        } else {
          $("#VIPFlag").val(0)
        }
        VIPChange();
        if(data.condition === 0) {
          $("#VIPFlag").prop("disabled",true);
          $("#VIPLevel").prop("disabled",true);
        }
        $("#userDetailPage").modal('show');
      })
}

//会员变换
function VIPChange(){
  let type=$("#VIPFlag option:selected").val();
  if(type === "0"){
    $("#VIPLevel").val(0);
    $("#VIPLevel").prop("disabled",true);
    netWorkSizeChange();
  }else {
    $("#VIPLevel").prop("disabled",false);
    netWorkSizeChange();
  }
}

//网盘容量变换
function netWorkSizeChange(){
  let type=$("#VIPLevel option:selected").val();
  $("#netWorkSize").val(type);
}

//确定修改
function userLevelChange(){
  let oldLevel = $("#VIPLevelOld").val();
  let level = $("#VIPLevel").val();
  if(oldLevel === level){
    return;
  }
  let uid = $("#userId").val()
  post(host+"/admin/userLevelChange",
      {
        uid:uid,
        level:level
      },function (data){
        if(data.code === 200){
          new Prompt(data.reason)
          new TimeOutReload(1000)
        }else {
          new Prompt("操作失败！！！");
          new TimeOutReload(1000)
        }
      })
}

function adminLogout(){
  new Warning("退出警告！","确定退出？",function (){
    window.location=host+"/admin/logout"
  })
}

