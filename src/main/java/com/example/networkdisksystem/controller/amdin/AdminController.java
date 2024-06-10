package com.example.networkdisksystem.controller.amdin;

import com.example.networkdisksystem.config.HadoopConfig;
import com.example.networkdisksystem.entity.AdminEntity;
import com.example.networkdisksystem.entity.R;
import com.example.networkdisksystem.entity.ServerEntity;
import com.example.networkdisksystem.service.AdminService;
import com.example.networkdisksystem.util.ShellUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.concurrent.ExecutionException;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ShellUtils shellUtils;
    
    @Autowired
    private HadoopConfig hadoopConfig;

    /**
     * 登录.
     */
    @RequestMapping("/do-login")
    @ResponseBody
    public R doLogin(String username,String password){
        boolean flag = adminService.adminLogin(username, password);
        if (flag){
            return new R(200,"登录成功！");
        }else {
            return new R(400,"用户名密码错误!");
        }
    }

  /**
   * 退出登录.
   */
  @RequestMapping("/logout")
    public String adminLogout(HttpSession session){
      session.removeAttribute("admin");
      return "adminLogin";
    }
    
    /**
     * 更新用户状态.
    * @param uid uid
    * @param condition 状态
    */
    @RequestMapping(value = "/changeCondition",method = RequestMethod.GET)
    @ResponseBody
    public R changeCondition(@RequestParam("uid") int uid,
                             @RequestParam("condition") int condition){
      
      adminService.changeCondition(uid,condition);
      if(condition == 1){
        log.info("禁用用户---用户id={}",uid);
        return new R(200,"禁用成功");
      }else {
        log.info("启用用户---用户id={}",uid);
        return new R(200,"启用成功");
      }
    }

    /**
     * 删除用户.
     */
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    @ResponseBody
    public R deleteUser(@RequestParam("uid") int uid){
      log.info("删除用户:{}---用户id={}",uid);
      int flag = adminService.deleteUser(uid);
      if(flag == 1) {
        return new R(200, "删除成功");
      }else {
        return new R(500,"操作失败！");
      }
    }

    /**
     * 查看用户详细信息.
     */
    @RequestMapping(value = "/catUserDetail",method = RequestMethod.POST)
    @ResponseBody
    public AdminEntity.UserDetail catUserDetail(@RequestParam("uid") int uid){
      AdminEntity.UserDetail userDetail = adminService.catUserDetail(uid);
      return userDetail;
    }

  /**
   * 更改用户会员等级
   * @param uid uid
   * @param level 会员等级
   */
  @RequestMapping(value = "/userLevelChange",method = RequestMethod.POST)
  @ResponseBody
  public R userLevelChange(@RequestParam("uid") int uid,
                             @RequestParam("level") String level){
      adminService.userLevelChange(uid,level);
      return new R(200,"修改成功！");
  }

  
  /**
   * 监视集群.
   */
  @RequestMapping(value = "/monitor",method = RequestMethod.GET)
  @ResponseBody
  public ServerEntity monitor() throws ExecutionException, InterruptedException {
    return shellUtils.serverDetail(
      hadoopConfig.getUsername(),
      hadoopConfig.getPassword(),
      hadoopConfig.getServer());
  }
}
