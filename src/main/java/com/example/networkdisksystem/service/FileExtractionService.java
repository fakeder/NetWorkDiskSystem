package com.example.networkdisksystem.service;

import com.example.networkdisksystem.entity.FileExtractionEntity;

public interface FileExtractionService {

    /**
     * 判断提取码是否有效
     * @param shareCode 提取码
     * @return
     */
    FileExtractionEntity checkShareCode(String shareCode);

    /**
     * 更新文件被下载次数
     * @param shareCode 提取码
     * @param fid 文件id
     * @return true:更新成功   false:更新失败（到达限定下载次数）
     */
    boolean updateShareNumberOfDownload(String shareCode,int fid);

    /**
     * 将分享保存到网盘当前目录下.
     * @param fileName 文件名
     * @param mid 目录id
     * @param uid 用户id
     * @param fileSize 文件大小
     * @param usedSize 已使用大小
     * @param filePath1 提取文件用户地址
     * @param filePath2 分享文件用户地址
     * @return
     */
    int saveFile(String fileName,int mid,int uid,String fileSize, long fileSizeByte ,long usedSize,String filePath1,String filePath2);
}
