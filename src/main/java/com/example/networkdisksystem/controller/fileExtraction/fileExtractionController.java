package com.example.networkdisksystem.controller.fileExtraction;

import com.example.networkdisksystem.entity.FileExtractionEntity;
import com.example.networkdisksystem.service.FileExtractionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;

@Controller
@RequestMapping("/fileExtraction")
@Slf4j
public class fileExtractionController {

    @Autowired
    FileExtractionService service;

    @RequestMapping(value = "/checkShareCode",method = RequestMethod.POST)
    @ResponseBody
    public FileExtractionEntity checkShareCode(String shareCode){
        log.info("文件提取码：{}",shareCode);
        FileExtractionEntity fileExtraction = service.checkShareCode(shareCode);
        return fileExtraction;
    }
}
