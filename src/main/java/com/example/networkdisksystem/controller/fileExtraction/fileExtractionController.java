package com.example.networkdisksystem.controller.fileExtraction;

import com.example.networkdisksystem.config.FileConfig;
import com.example.networkdisksystem.entity.FileEntity;
import com.example.networkdisksystem.entity.FileExtractionEntity;
import com.example.networkdisksystem.entity.R;
import com.example.networkdisksystem.entity.Users;
import com.example.networkdisksystem.service.FileExtractionService;
import com.example.networkdisksystem.service.FileService;
import com.example.networkdisksystem.util.Naming;
import com.example.networkdisksystem.util.SizeChange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/fileExtraction")
@Slf4j
public class fileExtractionController {

    @Autowired
    FileConfig fileConfig;

    @Autowired
    FileExtractionService service;

    @Autowired
    FileService fileService;

    /**
     * 文件提取码有效check
     * @param shareCode 提取码
     * @return FileExtractionEntity
     */
    @RequestMapping(value = "/checkShareCode",method = RequestMethod.POST)
    @ResponseBody
    public FileExtractionEntity checkShareCode(String shareCode){
        log.info("文件提取码：{}",shareCode);
        FileExtractionEntity fileExtraction = service.checkShareCode(shareCode);
        return fileExtraction;
    }

    /**
     * 提取文件更新被下载次数.
     * @param shareCode 提取码
     * @param fid fid
     * @return
     */
    @RequestMapping(value = "/updateNumberOfDownload",method = RequestMethod.POST)
    @ResponseBody
    public R updateNumberOfDownload(@RequestParam("shareCode") String shareCode,
                                    @RequestParam("fid") int fid){
        boolean flag = service.updateShareNumberOfDownload(shareCode, fid);
        if(flag) return new R(200,"被下载次数更新成功！");
        else return new R(400,"提取码已失效！");
    }


    /**
     * 将文件保存到当前目录下.
     * @param username 分享文件的用户名
     * @param fid 文件id
     * @param fileName 文件名
     * @param fileSize 文件大小
     * @param session session
     * @return
     */
    @RequestMapping(value = "/saveToCurrentDirectory",method = RequestMethod.POST)
    @ResponseBody
    public R saveToCurrentDirectory(@RequestParam("username") String username,
                                    @RequestParam("fid") int fid,
                                    @RequestParam("fileName") String fileName,
                                    @RequestParam("fileSize") String fileSize,
                                    HttpSession session){
        //判断用户网盘容量
        long fileSizeByte = SizeChange.formatFileSizeReverse(fileSize);
        log.info("提取文件大小：{} | 转换成字节大小：{}",fileSize,fileSizeByte);
        Users user = (Users) session.getAttribute("user");
        long usedSize = SizeChange.formatFileSizeReverse(user.getUsedSize());
        log.info("用户已使用：{} | 转换成字节大小：{}",user.getUsedSize(),usedSize);
        long totalSize = SizeChange.formatFileSizeReverse(user.getTotalSize());
        log.info("用户总容量：{} | 转换成字节大小：{}",user.getTotalSize(),totalSize);
        usedSize=usedSize+fileSizeByte;
        if(usedSize>totalSize) return new R(400,"网盘容量已满，请购买网盘容量或充值VIP");
        //判断文件是否重名
        int mid = (int) session.getAttribute("mid");
        List<FileEntity.FileInputEntity> fileNames = fileService.getFileNamesByMid(mid);
        fileName= Naming.fileNaming(fileNames,fileName);
        //复制文件并向文件表中写入数据
        //提取文件用户地址
        String HDFSFilePath1=fileConfig.getHdfsUploadPath()+user.getUsername()+"/";
        //分享文件用户地址
        String HDFSFilePath2=fileConfig.getHdfsUploadPath()+username+"/"+fid;
        //下载到服务器临时地址
        String tempPath=fileConfig.getWindowsUploadPath() +fileName;
        try{
           service.saveFile(fileName, mid, user.getUid(), fileSize, usedSize, HDFSFilePath1, HDFSFilePath2, tempPath);
            return new R(200,"文件已成功保存到当前目录下");
        }catch (Exception e){
            return new R(500,"文件已保存到当前目录下发生未知错误,保存失败!");
        }
    }
}
