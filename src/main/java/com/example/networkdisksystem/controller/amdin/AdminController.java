package com.example.networkdisksystem.controller.amdin;

import com.example.networkdisksystem.entity.R;
import com.example.networkdisksystem.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

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
}
