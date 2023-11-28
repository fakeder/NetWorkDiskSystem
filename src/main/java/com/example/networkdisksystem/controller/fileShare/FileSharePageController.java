package com.example.networkdisksystem.controller.fileShare;

import com.example.networkdisksystem.entity.FileEntity;
import com.example.networkdisksystem.entity.FileShareEntity;
import com.example.networkdisksystem.entity.Users;
import com.example.networkdisksystem.service.FileShareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

@Controller
@Slf4j
@RequestMapping("/fileShare")
public class  FileSharePageController {


  @Autowired
  FileShareService service;

  /**
   * 文件分享页面展示.
   * @param find 要模糊查找的文件名
   * @return
   */
  @RequestMapping(value = {"/fileSharePage","/fileShare.html"},method = RequestMethod.GET)
  public String fileSharePage(HttpSession session, Model model, @RequestParam(value = "find",required = false) String find){
    Users user = (Users) session.getAttribute("user");
    model.addAttribute("user",user);
    List<FileShareEntity.FileShareEntityOutput> list;
    if(Objects.isNull(find)||find.equals("")) {
      list = service.showFileShare(user.getUid());
    }else {
      list= service.findFileShare(user.getUid(), find);
    }
    model.addAttribute("list",list);
    return "FileSharingHistory";
  }
}
