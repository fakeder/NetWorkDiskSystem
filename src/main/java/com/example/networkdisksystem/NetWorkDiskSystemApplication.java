package com.example.networkdisksystem;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan //filter
@SpringBootApplication
@Slf4j
public class NetWorkDiskSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(NetWorkDiskSystemApplication.class, args);
        log.info("【项目启动...】");
    }

}
