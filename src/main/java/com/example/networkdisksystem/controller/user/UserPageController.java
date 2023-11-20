package com.example.networkdisksystem.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Slf4j
@RequestMapping("/user")
public class UserPageController {
  //登录
  @RequestMapping(value = {"/login","/login.html"},method = RequestMethod.GET)
  public String login(){
    System.out.println("======login=====");
    return "login";
  }
  //注册
  @RequestMapping(value = {"/register","/register.html"},method = RequestMethod.GET)
  public String register(){
    return "register";
  }

  //重置密码
  @RequestMapping(value = {"/forgot-pws","/forgot-pws.html"},method = RequestMethod.GET)
  public String forgot(){
    return "forgot-pws";
  }
}
