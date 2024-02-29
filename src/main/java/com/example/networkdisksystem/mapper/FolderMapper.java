package com.example.networkdisksystem.mapper;

import com.example.networkdisksystem.entity.FolderEntity;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface FolderMapper {

    /**
     * 目录新增
     * @param uid uid
     * @param folderName 目录名称
     * @param upFolderId 上级目录id
     * @param startTime 创建时间
     * @return 1:成功执行  0:执行失败
     */
    int insertMkdir(@Param("uid") int uid,
                    @Param("FolderName") String folderName,
                    @Param("UpFolderId") int upFolderId,
                    @Param("startTime")Date startTime);



    /**
     * 获取文件夹id  仅在注册时使用UpFolderId=-1.
     * @param uid uid
     * @param UpFolderId 上级目录
     * @return
     */
    int getMidByUidAndUpMkdirId(@Param("uid") int uid, @Param("UpFolderId") int UpFolderId);

    /**
     * 根据上一级目录id获取目录
     * @param UpFolderId 目录的上一级目录id
     * @return
     */
    List<FolderEntity.FolderEntityInput> getFolderByUpMkdirId(int UpFolderId);

    /**
     * 根据mid删除目录
     * @param mid mid
     * @return
     */
    int delete(int mid);

    /**
     * 根据mid更新目录名称
     * @param mid mid
     * @param FolderName 目录名称
     * @return
     */
    int rename(@Param("mid") int mid,@Param("FolderName") String FolderName);

    /**
     * 根据uid对目录id进行模糊匹配获取FolderEntity
     * @param uid uid
     * @param FolderName 目录名称
     * @return 目录
     */
    List<FolderEntity.FolderEntityInput> getFolderByIdAndFolderName(@Param("uid") int uid ,@Param("FolderName") String FolderName);

    /**
     * 根据mid获取目录名称
     * @param mid mid
     * @return 目录名称
     */
    FolderEntity.FolderEntityInput getFolderNameAndUpFolderIdByMid(@Param("mid")int mid);
}
