package com.example.networkdisksystem.controller.page;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

@ControllerAdvice
public class ErrorPage {

    @ExceptionHandler(Exception.class)
    @RequestMapping("/exception")
    public String ExceptionPage(){
        return "error";
    }
}
