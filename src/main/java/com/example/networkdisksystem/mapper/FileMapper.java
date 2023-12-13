package com.example.networkdisksystem.mapper;

import com.example.networkdisksystem.entity.FileEntity;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface FileMapper {

    /**
     * 文件信息初始化录入
     * @param mid mid
     * @param fileName 文件名
     * @param fileSize 文件大小
     * @param fileSizeByte 文件大小（字节大小）
     * @param uid uid
     * @param startTime 开始时间
     * @return 1:成功执行  0:执行失败
     */
    int addFile(@Param("mid") int mid,
                @Param("fileName") String fileName,
                @Param("FileSize")String fileSize,
                @Param("FileSizeByte") long fileSizeByte,
                @Param("uid") int uid,
                @Param("startTime")Date startTime);

    /**
     * 获取文件夹下所有文件信息
     * @param mid mid
     * @return
     */
    List<FileEntity.FileInputEntity> getFileByMid(int mid);

    /**
     * 根据mid和文件名获取文件id
     * @param mid mid
     * @param fileName 文件名
     * @return fid
     */
    int getFileIdByMidAndFileName(@Param("mid") int mid,@Param("fileName") String fileName);

    /**
     * 根据fid删除文件
     * @param fid fid
     * @return 1:成功执行  0:执行失败
     */
    int deleteFile(int fid);

    /**
     * 根据fid获取文件名
     * @param fid fid
     * @return 文件名
     */
    String getFileNameByFid(int fid);

    /**
     * 根据fid修改文件名
     * @param filename 文件名
     * @param fid fid
     * @return 1:成功执行  0:执行失败
     */
    int rename(@Param("filename") String filename,@Param("fid") int fid);

    /**
     * 根据uid对filename进行模糊匹配查询文件
     * @param uid uid
     * @param fileName 文件名
     * @return
     */
    List<FileEntity.FileInputEntity> getFileByIdAndFileName(@Param("uid") int uid,@Param("fileName") String fileName);

    /**
     * 根据fid获取文件信息
     * @param fid fid
     * @return 文件信息
     */
    FileEntity.FileInputEntity getFileById(int fid);
}
