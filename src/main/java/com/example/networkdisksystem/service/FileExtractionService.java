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
}
