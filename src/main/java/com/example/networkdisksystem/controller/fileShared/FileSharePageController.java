package com.example.networkdisksystem.controller.fileShared;

import com.example.networkdisksystem.entity.FileShareEntity;
import com.example.networkdisksystem.entity.Users;
import com.example.networkdisksystem.service.FileShareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@Slf4j
public class FileSharePageController {


  @Autowired
  FileShareService service;

  /**
   * 文件分享页面展示.
   */
  @RequestMapping(value = {"/fileSharePage","/fileShare.html"},method = RequestMethod.GET)
  public String fileSharePage(HttpSession session, Model model){
    Users user = (Users) session.getAttribute("user");
    model.addAttribute("user",user);
    List<FileShareEntity.FileShareEntityOutput> list = service.showFileShare(user.getUid());
    model.addAttribute("list",list);
    return "FileSharingHistory";
  }
}
