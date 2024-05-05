package com.example.networkdisksystem.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(value = "hadoop")
public class HadoopConfig {

    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 脚本路径
     */
    private String[] scriptPath;
    /**
     * 服务器地址
     */
    private String[] server;
}
