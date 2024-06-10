package com.example.networkdisksystem.controller.amdin;


import com.example.networkdisksystem.entity.AdminEntity;
import com.example.networkdisksystem.service.serviceImpl.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminPageController {
  
    @Autowired
    AdminServiceImpl adminService;

    @RequestMapping("/login")
    public String adminLogin(){
        return "adminLogin";
    }

  /**
   * 管理员管理用户页面.
   */
    @RequestMapping(value = {"/mangerIndex","/mangerIndex.html"})
    public String adminIndex(Model model, HttpSession session){
        adminService.updateSession();
        AdminEntity admin = (AdminEntity) session.getAttribute("admin");
        model.addAttribute("admin",admin);
        return "mangerIndex";
    }

  /**
   * 系统监控.
   */
  @RequestMapping(value = "/systemLister",method = RequestMethod.GET)
    public String systemLister(){
      return "monitor";
    }
}
