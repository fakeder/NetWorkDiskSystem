package com.example.networkdisksystem.mapper;

import com.example.networkdisksystem.entity.FileShareEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface FileShareMapper {

  /**
   * 第一次录入表格信息.
   * @param uid 用户id
   * @param fid 文件id
   * @param fileName 文件名
   * @param fileSize 文件大小（B）
   * @param fileSizeByte 文件大小 (字节)
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
                           @Param("fileSizeByte") long fileSizeByte,
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


  /**
   * 获取用户的文件分享信息.
   * @param uid 用户id
   */
  List<FileShareEntity.FileShareEntityInput> getFileShareByUid(int uid);

  /**
   * 查询用户搜索的文件名的文件.
   * @param uid uid
   * @param find 文件名
   */
  List<FileShareEntity.FileShareEntityInput> findFileShare(int uid,String find);

  /**
   * 更改状态
   * @param shareId id
   * @param condition 状态
   * @return
   */
  int changeCondition(@Param("shareId") int shareId,@Param("condition") int condition);

  /**
   * 删除分享记录
   * @param shareId id
   * @return
   */
  int delShareById(int shareId);

    /**
     * 通过fid,分享码获取文件分享类
     * @param shareCode 分享码
     * @param fid fid
     * @return 文件分享类
     */
  FileShareEntity.FileShareEntityInput getFileShareEntityInputByShareCodeAndFid(@Param("shareCode") String shareCode,
                                                                                @Param("fid") Integer fid);

    /**
     * 更新文件分享下载次数
     * @param numberOfDownload 下载次数
     * @param shareId id
     * @return
     */
  int updateNumberOfDownload(@Param("numberOfDownload") int numberOfDownload,
                             @Param("shareId") int shareId);


    /**
     * 根据fid_list获取正在分享中的shareCode_list
     * @param fid_list fid_list
     * @return shareCode_list
     */
  List<String> getShareCodeListByFidList(List<Integer> fid_list);


  /**
   * 获取用户正在分享中的分享文件list
   * @param uid uid
   * @retur fileShare_list
   */
  List<FileShareEntity.FileShareEntityInput> getFileShareListByUid(int uid);

  /**
   * 删除用户的文件分享记录
   * @param uid uid
   */
  int deleteFileShareByUid(int uid);
}
