package com.example.networkdisksystem.service;

import com.example.networkdisksystem.entity.FileEntity;

import java.util.List;

public interface FileService {
    /**
     * @param fileSize 文件大小 （B）
     * @param usedSize 用户已使用的容量（字节）
     */
    int pushFile(int mid,String filename,String windowsFilePath,String HDFSFilePath,String fileSize,long usedSize,int uid);

    int pullFile(String HDFSFilePath,int fid);

    List<FileEntity> getFileNamesByMid(int mid);

    int deleteFile(int fid,String HDFSFilePath);

    int rename(String filename,int fid,int mid);
}
