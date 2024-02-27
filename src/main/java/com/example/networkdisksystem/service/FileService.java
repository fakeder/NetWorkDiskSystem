package com.example.networkdisksystem.service;

import com.example.networkdisksystem.entity.FileEntity;
import com.example.networkdisksystem.entity.Users;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface FileService {

    /**
     * 上传文件.
     * @param mid 所属目录id
     * @param filename 文件名
     * @param windowsFilePath 服务器路径
     * @param HDFSFilePath hdfs路径
     * @param fileSize 文件大小 （B）
     * @param fileSizeByte 文件大小 （字节）
     * @param usedSize 用户已使用的容量（字节）
     * @param uid uid
     * @return
     */
    int pushFile(int mid,String filename,String windowsFilePath,String HDFSFilePath,String fileSize,long fileSizeByte,long usedSize,int uid);

    /**
     * 将文件下载到服务器端
     * @param HDFSFilePath hdfs路径
     * @param fid fid
     * @return 文件名
     */
    String pullFile(String HDFSFilePath, int fid);

    /**
     * 获取目录下的所以文件
     * @param mid mid
     * @return
     */
    List<FileEntity.FileInputEntity> getFileNamesByMid(int mid);

    /**
     * 删除文件
     * @param fid fid
     * @param HDFSFilePath hdfs地址
     * @param user 用户信息
     * @return
     */
    int deleteFile(int fid, String HDFSFilePath, Users user);

    /**
     * 文件重命名
     * @param filename 新文件名
     * @param fid fid
     * @param mid 目录id
     * @return
     */
    int rename(String filename,int fid,int mid);

    /**
     * 将文件从服务器端下载到客户端
     * @param request
     * @param response
     * @param fileName
     */
    void downloadToClient(HttpServletRequest request, HttpServletResponse response,String fileName);

    /**
     * DB录入/取得 -> 页面展示
     * @param list
     * @return
     */
    List<FileEntity.FileOutputEntity> fileInputChangeToOutput(List<FileEntity.FileInputEntity> list);

    /**
     * 根据fid修改mid
     * @param fid fid
     * @param mid mid
     * @return 0:失败 1:成功
     */
    int removeMidByFid(int fid,int mid);
}
