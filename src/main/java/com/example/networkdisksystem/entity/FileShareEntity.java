package com.example.networkdisksystem.entity;

import lombok.Data;

import java.util.Date;


public class FileShareEntity {

  /**
   * DB数据取得/录入
   */
  @Data
  public static class FileShareEntityInput{
    int shareId;
    int uid;
    int fid;
    String fileName;
    String fileSize;
    Date startTime;
    Date expirationTime;
    int downloadNumber;//下载次数
    int numberOfDownload;//被下载次数
    int condition;//状态
    String shareCode;//分享码
    int timeFlag;
    int downloadFlag;
  }

  /**
   * 页面展示
   */
  @Data
  public static class FileShareEntityOutput{
    int shareId;
    int uid;
    int fid;
    String fileName;
    String fileSize;
    String startTime;
    String expirationTime;
    int downloadNumber;//下载次数
    int numberOfDownload;//被下载次数
    int condition;//状态
    String shareCode;//分享码
    int timeFlag;
    int downloadFlag;
  }

}
