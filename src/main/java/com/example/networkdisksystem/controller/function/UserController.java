package com.example.networkdisksystem.controller.function;

import com.example.networkdisksystem.entity.R;
import com.example.networkdisksystem.mapper.UserMapper;
import com.example.networkdisksystem.service.RegisterService;
import com.example.networkdisksystem.service.VerifyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class UserController {
    @Resource
    UserMapper userMapper;

    @Resource
    VerifyService verifyService;

    @Resource
    RegisterService registerService;

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
        if(checkEmail!=0)return new R(403,"邮箱已被注册！");
        int checkUserName = userMapper.checkUserName(username);
        if(checkUserName!=0)return new R(403,"用户名已存在！");
        if(!verifyService.doVerify(email,verify))return new R(403,"邮箱或验证码错误！");
        registerService.register(username,password,email);
        return new R(200,"注册成功！");
    }
}
