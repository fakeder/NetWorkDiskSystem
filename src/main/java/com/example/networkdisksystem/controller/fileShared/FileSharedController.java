package com.example.networkdisksystem.controller.fileShared;

import com.example.networkdisksystem.entity.FileShareEntity;
import com.example.networkdisksystem.entity.R;
import com.example.networkdisksystem.entity.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
@Slf4j
public class FileSharedController {


  @RequestMapping(value = "/toFileShare",method = RequestMethod.POST)
  @ResponseBody
  public R fileShared(HttpSession session,
                      @RequestParam("fileId") int fileId,
                      @RequestParam("filename") String filename,
                      @RequestParam("fileSize") String fileSize,
                      @RequestParam("expirationTime") int expirationTime,
                      @RequestParam("downloadType") int downloadType,
                      @RequestParam("downloadNumber") int downloadNumber) {
    Users user = (Users) session.getAttribute("user");
    FileShareEntity fileShareEntity=new FileShareEntity();
    fileShareEntity.setFid(fileId);
    fileShareEntity.setFileName(filename);
    fileShareEntity.setFileSize(fileSize);
    fileShareEntity.setDownloadNumber(downloadNumber);
    fileShareEntity.setUid(user.getUid());
    fileShareEntity.setCondition(0);
    if(expirationTime == 0){
      fileShareEntity.setExpirationTime(new Date());
    }
    System.out.println("以成功返回！");
    return new R(200,"aaa");
  }
}
