package com.example.networkdisksystem.service;

import com.example.networkdisksystem.entity.FileShareEntity;

import java.util.List;

/**
 * 文件分享..
 */
public interface FileShareService {

  /**
   * 文件分享列表中是否存在.
   * @param uid 用户id
   * @param fid 文件id
   */
  boolean fileShareCheck(int uid,int fid);

  /**
   * 文件分享表单提交写入文件文件分享表
   * @param uid 用户id
   * @param fileId 文件id
   * @param filename 文件名
   * @param fileSize 文件大小
   * @param expirationTime 分享时间flag
   * @param downloadType  下载次数flag
   * @param downloadNumber 下载次数
   */
  String FileShared(int uid,int fileId,String filename,String fileSize,int expirationTime,int downloadType,int downloadNumber);

  /**
   * 文件分享 FileShareEntityInput->FileShareEntityOutput.
   * @param list 传入的FileShareEntityInput
   * @return 转换返回FileShareEntityOutput
   */
  List<FileShareEntity.FileShareEntityOutput> showFileShare(int uid);
}
