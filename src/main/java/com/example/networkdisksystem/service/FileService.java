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
     * @param FileTempPath 文件在服务器的临时路径
     * @param FileSavePath 文件保存路径
     * @param fileSize 文件大小 （B）
     * @param fileSizeByte 文件大小 （字节）
     * @param usedSize 用户已使用的容量（字节）
     * @param uid uid
     * @return
     */
    int pushFile(int mid,String filename,String FileTempPath,String FileSavePath,String fileSize,long fileSizeByte,long usedSize,int uid);


    /**
     * 获取目录下的所以文件
     * @param mid mid
     * @return
     */
    List<FileEntity.FileInputEntity> getFileNamesByMid(int mid);

    /**
     * 删除文件
     * @param fid fid
     * @param filePath 文件目录地址
     * @param user 用户信息
     * @return
     */
    int deleteFile(int fid, String filePath, Users user);

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
     * @param filePath 文件路径目录
     * @param fid 文件id
     */
    void downloadToClient(HttpServletRequest request, HttpServletResponse response,String filePath,int  fid);

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
