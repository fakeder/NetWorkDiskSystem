package com.example.networkdisksystem.controller.amdin;


import com.example.networkdisksystem.entity.AdminEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminPageController {

    @RequestMapping("/login")
    public String adminLogin(){
        return "adminLogin";
    }

    @RequestMapping(value = {"/mangerIndex","/mangerIndex.html"})
    public String adminIndex(Model model, HttpSession session){
        AdminEntity admin = (AdminEntity) session.getAttribute("admin");
        model.addAttribute("admin",admin);
        return "mangerIndex";
    }
}
