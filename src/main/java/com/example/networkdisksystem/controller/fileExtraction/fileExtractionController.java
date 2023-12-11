package com.example.networkdisksystem.controller.fileExtraction;

import com.example.networkdisksystem.entity.FileExtractionEntity;
import com.example.networkdisksystem.entity.R;
import com.example.networkdisksystem.service.FileExtractionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;

@Controller
@RequestMapping("/fileExtraction")
@Slf4j
public class fileExtractionController {

    @Autowired
    FileExtractionService service;

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
}
