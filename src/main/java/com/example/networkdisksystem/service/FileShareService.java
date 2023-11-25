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
   * 文件分享 查询用用户的所以分享记录.
   * @param uid 传入的uid
   * @return 转换返回FileShareEntityOutput
   */
  List<FileShareEntity.FileShareEntityOutput> showFileShare(int uid);

  /**
   * 模糊匹配查询用户传入的文件名.
   * @param uid uid
   * @param find 用户传入的文件名
   * @return FileShareEntityOutput
   */
  List<FileShareEntity.FileShareEntityOutput> findFileShare(int uid,String find);

  /**
   * 停止分享
   * @param shareId id
   * @param shareCode 分享码
   * @return
   */
  int stopSharing(int shareId,String shareCode);

  /**
   * 删除分享记录
   * @param shareId id
   * @return
   */
  int delShare(int shareId);
}
