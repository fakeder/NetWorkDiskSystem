package com.example.networkdisksystem.service;

import com.example.networkdisksystem.entity.FileEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface FileService {
    /**
     * @param fileSize 文件大小 （B）
     * @param usedSize 用户已使用的容量（字节）
     */
    int pushFile(int mid,String filename,String windowsFilePath,String HDFSFilePath,String fileSize,long usedSize,int uid);

    /**
     * 将文件下载到服务器端
     * @param HDFSFilePath hdfs路径
     * @param fid fid
     * @return 文件名
     */
    String pullFile(String HDFSFilePath, int fid);

    /**
     * 获取路面下的所以文件
     * @param mid mid
     * @return
     */
    List<FileEntity> getFileNamesByMid(int mid);

    int deleteFile(int fid,String HDFSFilePath);

    int rename(String filename,int fid,int mid);

    /**
     * 将文件从服务器端下载到客户端
     * @param request
     * @param response
     * @param fileName
     */
    void downloadToClient(HttpServletRequest request, HttpServletResponse response,String fileName);
}
