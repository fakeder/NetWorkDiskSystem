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
    String fileSize;//文件大小（B）
    long fileSizeByte;//文件大小（字节）
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
    String shareCode;//分享码
    String fileName;
    String fileSize;//文件大小（B）
    long fileSizeByte;//文件大小（字节）
    String startTime;//开始时间
    String expirationTime;//结束时间
    String downloadNumber;//下载次数
    int numberOfDownload;//被下载次数
    String condition;//状态
    String conditionClass;//状态class
    String buttonClass;//按钮class
    String buttonText;//按钮显示文件
  }

}
