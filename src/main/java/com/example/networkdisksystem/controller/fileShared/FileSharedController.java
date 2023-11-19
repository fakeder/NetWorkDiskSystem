package com.example.networkdisksystem.controller.fileShared;

import com.example.networkdisksystem.entity.FileShareEntity;
import com.example.networkdisksystem.entity.R;
import com.example.networkdisksystem.entity.Users;
import com.example.networkdisksystem.service.FileShareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  FileShareService service;


  /**
   * 文件分享（分享）
   * @param fileId 文件id
   * @param filename 文件名
   * @param fileSize 文件大小
   * @param expirationTime 结束时间flag
   * @param downloadType 下载次数flag
   * @param downloadNumber 下载次数
   */
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
    //检查是否已经在分享中
    boolean check = service.fileShareCheck(user.getUid(), fileId);
    if(check) {
      String shareCode =
        service.FileShared(user.getUid(), fileId, filename, fileSize, expirationTime, downloadType, downloadNumber);
      return new R(200, shareCode);
    }else {
      return new R(400,"此文件处于分享中！");
    }
  }
}
