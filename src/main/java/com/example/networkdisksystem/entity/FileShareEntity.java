package com.example.networkdisksystem.entity;

import lombok.Data;

import java.util.Date;


@Data
public class FileShareEntity {
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
}
