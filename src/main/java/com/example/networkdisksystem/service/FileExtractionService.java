package com.example.networkdisksystem.service;

import com.example.networkdisksystem.entity.FileExtractionEntity;

public interface FileExtractionService {

    /**
     * 判断提取码是否有效
     * @param shareCode 提取码
     * @return
     */
    FileExtractionEntity checkShareCode(String shareCode);
}
