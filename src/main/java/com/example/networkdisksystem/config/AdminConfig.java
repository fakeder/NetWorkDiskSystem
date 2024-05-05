package com.example.networkdisksystem.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(value = "admin")
public class AdminConfig {

    /**
     * 用户名.
     */
    private String username;

    /**
     * 密码.
     */
    private String password;
}
