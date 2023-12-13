package com.example.networkdisksystem.controller.fileShare;

import com.example.networkdisksystem.entity.R;
import com.example.networkdisksystem.entity.Users;
import com.example.networkdisksystem.service.FileShareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Objects;

@Controller
@Slf4j
@RequestMapping("/fileShare")
public class FileSharedController {

  @Autowired
  FileShareService service;


    /**
     * 文件分享存在check，判断文件是否在分享中
     * @param fid fid
     * @param session session
     */
  @RequestMapping(value = "/fileShareCheck",method = RequestMethod.POST)
  @ResponseBody
  public R fileShareCheck(@RequestParam("fid") int fid,
                          HttpSession session){
      Users user = (Users) session.getAttribute("user");
      String shareCode = service.fileShareCheck(user.getUid(), fid);
      if(Objects.isNull(shareCode)) return new R(200,"文件不在分享中");
      else return new R(400,"该文件处于分享中,分享码:"+shareCode);
  }
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
                      @RequestParam("fileSizeByte") long fileSizeByte,
                      @RequestParam("expirationTime") int expirationTime,
                      @RequestParam("downloadType") int downloadType,
                      @RequestParam("downloadNumber") int downloadNumber) {
    Users user = (Users) session.getAttribute("user");
    String shareCode =
        service.FileShared(user.getUid(), fileId, filename, fileSize, fileSizeByte, expirationTime, downloadType, downloadNumber);
    return new R(200, shareCode);
  }

  /**
   * 分享记录查询（根据文件名）
   * @param find 文件名
   * @return
   */
  @RequestMapping("/find")
  public String find(@RequestParam(value = "find",required = false) String find){
    if(Objects.isNull(find)||find.equals("")){
      return "redirect:fileSharePage";
    }
    return "redirect:fileSharePage?find="+find;
  }

  /**
   * 停止分享
   * @param shareId id
   * @param shareCode 分享码
   * @return
   */
  @RequestMapping(value = "/stopSharing",method = RequestMethod.POST)
  @ResponseBody
  public R stopSharing(@RequestParam("shareId") int shareId,
                       @RequestParam("shareCode") String shareCode){
    service.stopSharing(shareId,shareCode);
    return new R(200,"该文件已停止分享！");
  }

  /**
   * 删除分享记录
   * @param shareId id
   * @return
   */
  @RequestMapping(value = "/delShare",method = RequestMethod.POST)
  @ResponseBody
  public R delShare(int shareId){
    service.delShare(shareId);
    return new R(200,"删除成功！");
  }
}
