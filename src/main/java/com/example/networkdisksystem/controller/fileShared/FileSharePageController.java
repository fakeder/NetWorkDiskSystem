package com.example.networkdisksystem.controller.fileShared;

import com.example.networkdisksystem.entity.FileEntity;
import com.example.networkdisksystem.entity.FileShareEntity;
import com.example.networkdisksystem.entity.Users;
import com.example.networkdisksystem.service.FileShareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
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


  //文件分享
  @RequestMapping(value = "/fileShare",method = RequestMethod.GET)
  public String fileShare(@RequestParam(value = "Fid",defaultValue = "-1" ) int Fid,
                          @RequestParam(value = "FileName",defaultValue = "-1") String FileName,
                          @RequestParam(value = "FileSize",defaultValue = "-1") String FileSize,
                          Model model, HttpSession session){
    log.info("======文件分享start======");
    log.info("fid={}",Fid);
    log.info("FileName={}",FileName);
    log.info("fileSize={}",FileSize);

    //文件分享页面提交bug，修复（原因未知，）
    if(Fid==-1&&FileName.equals("-1")&&FileSize.equals("-1")){
      return "redirect:/common/cancel";
    }

    FileEntity file=new FileEntity();
    file.setFileSize(FileSize);
    file.setFileName(FileName);
    file.setFid(Fid);
    Users user = (Users) session.getAttribute("user");
    model.addAttribute("file",file);
    model.addAttribute("user",user);
    log.info("=======文件分享end=======");
    return "fileShare";
  }

  /**
   * 文件分享页面展示.
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
