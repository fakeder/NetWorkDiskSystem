package com.example.networkdisksystem.controller.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorPage {

    @ExceptionHandler(Exception.class)
    @RequestMapping("/exception")
    public String exceptionPage(Exception e){
        log.error("错误信息");
        e.printStackTrace();
        return "error";
    }
}
