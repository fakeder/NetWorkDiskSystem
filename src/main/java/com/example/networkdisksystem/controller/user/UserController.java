package com.example.networkdisksystem.controller.user;

import com.example.networkdisksystem.entity.R;
import com.example.networkdisksystem.entity.Users;
import com.example.networkdisksystem.mapper.UserMapper;
import com.example.networkdisksystem.service.RegisterService;
import com.example.networkdisksystem.service.UserService;
import com.example.networkdisksystem.service.VerifyService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
    @Resource
    UserMapper userMapper;

    @Resource
    VerifyService verifyService;

    @Resource
    RegisterService registerService;

    @Resource
    UserService userService;


    @RequestMapping(value = "/verify-code",method = RequestMethod.GET)
    @ResponseBody
    public R verifyCode(@RequestParam("email") String email){//发送邮件

        try {
            verifyService.sendMailCode(email);
            return new R(200,"邮件发送成功！");
        }catch (Exception e){
            return new R(500,"邮件发送失败！");
        }
    }

    @RequestMapping(value = "/do-register",method = RequestMethod.POST)
    @ResponseBody
    public R do_register(@RequestParam("username") String username,
                         @RequestParam("password") String password,
                         @RequestParam("email") String email,
                         @RequestParam("verify") String verify){
        System.out.println("=======do-register========");
        System.out.println(username+":"+password+":"+email);
        int checkEmail = userMapper.checkEmail(email);
        if(checkEmail!=0){
            verifyService.deleteCode(email);
            return new R(403,"邮箱已被注册！");
        }
        int checkUserName = userMapper.checkUserName(username);
        if(checkUserName!=0)return new R(403,"用户名已存在！");
        int flag = verifyService.doVerify(email, verify);
        if(flag==0){
            return new R(403,"验证码失效！");
        }else if(flag ==1){
            return new R(403,"验证码错误！");
        }else if(flag == 2){
            registerService.register(username,password,email);
        }else {
            return new R(403,"未知错误！");
        }

        return new R(200,"注册成功！");
    }

    @RequestMapping(value = "/do-login",method = RequestMethod.POST)
    @ResponseBody
    public R do_login(String username,String password,boolean rememberMe,HttpServletResponse response) {
        int loginCheck = userService.LoginCheck(username, password, rememberMe, response);
        if (loginCheck == 2){
            return new R(200,"登录成功！");
        } else if (loginCheck == 1){
            return new R(400,"用户名或密码错误！");
        }else if (loginCheck == 0){
            return new R(403,"用户被禁用！");
        }else {
            return new R(500,"系统异常！");
        }
    }

    //退出登录
    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public String logout(HttpServletResponse response, HttpSession session){
        //删除session,cookie中的值
        session.removeAttribute("mid");
        session.removeAttribute("user");
        Cookie cookie_username=new Cookie("NetWorkDiskSystem_username","");
        cookie_username.setMaxAge(0);//cookie保存时间清空
        cookie_username.setPath("/");
        Cookie cookie_password=new Cookie("NetWorkDiskSystem_password","");
        cookie_password.setMaxAge(0);
        cookie_password.setPath("/");
        response.addCookie(cookie_username);
        response.addCookie(cookie_password);

        return "login";
    }

    //忘记密码
    @RequestMapping(value = "/forgetPassword",method = RequestMethod.POST)
    @ResponseBody
    public R forgetPassword(String email,String verify,String password){
        //1.判断验证码是否正确
        int flag = verifyService.doVerify(email, verify);
        if(flag==0){
            return new R(403,"验证码失效！");
        }else if(flag ==1) {
            return new R(403, "验证码错误！");
        }
        //2.判断邮箱是否存在
        Users user = userMapper.getUserByEmail(email);
        if(user == null){
            return new R(403,"该邮箱注册用户不存在！");
        }
        //3.重新设置密码
        int uid = user.getUid();
        password=new BCryptPasswordEncoder().encode(password);
        userMapper.updatePassword(uid,password);
        return new R(200,"密码设置成功！");
    }
}
