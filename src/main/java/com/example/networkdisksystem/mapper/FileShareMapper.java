package com.example.networkdisksystem.mapper;

import com.example.networkdisksystem.entity.FileShareEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

@Mapper
public interface FileShareMapper {

  /**
   * 第一次录入表格信息.
   * @param uid 用户id
   * @param fid 文件id
   * @param fileName 文件名
   * @param fileSize 文件大小
   * @param startTime 开始时间
   * @param expirationTime 结束时间
   * @param downloadNumber 限定下载次数
   * @param timeFlag 限定时间flag
   * @param downloadFlag 限定下载flag
   */
  int insertFileShareTable(@Param("uid")int uid,
                           @Param("fid") int fid,
                           @Param("fileName") String fileName,
                           @Param("fileSize") String fileSize,
                           @Param("startTime")Date startTime,
                           @Param("expirationTime") Date expirationTime,
                           @Param("downloadNumber") int downloadNumber,
                           @Param("timeFlag") int timeFlag,
                           @Param("downloadFlag") int downloadFlag);

  /**
   * 根据Uid fid 查询文件分享表
   * @param uid 用户id
   * @param fid 文件id
   * @return
   */
  FileShareEntity.FileShareEntityInput getSharedByUIdAndFid(@Param("uid") int uid,
                                                            @Param("fid") int fid);

  /**
   * 设置shareCode.
   * @param shareId 分享id
   * @param shareCode 分享code
   */
  int setShareCodeById(@Param("shareId") int shareId,
                       @Param("shareCode") String shareCode);
}
