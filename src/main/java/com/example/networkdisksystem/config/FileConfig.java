package com.example.networkdisksystem.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(value = "app")
public class FileConfig {
  /**
   * windows服务器路径
   */
  private String windowsUploadPath = "";

  /**
   * HDFS路径
   */
  private String hdfsUploadPath = "";

  /**
   * 文件类型
   */
  private String[] fileTypeArray;

  /**
   * 文件大小
   */
  private int maxFileSize;

  /**
   * 用户文件目录地址
   */
  private String userFilePath = "";
}
