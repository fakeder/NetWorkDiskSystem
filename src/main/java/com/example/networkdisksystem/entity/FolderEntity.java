package com.example.networkdisksystem.entity;

import lombok.Data;

import java.util.Date;

@Data
public class FolderEntity {

    /**
     * DB录入/取得
     */
    @Data
    public static class FolderEntityInput{
        int mid;
        int uid;
        String folderName;
        int upFolderId;
        Date startTime;
        Date lastModifiedTime;
    }

    /**
     * 页面展示
     */
    @Data
    public static class FolderEntityOutput{
        int mid;
        int uid;
        String folderName;
        int upFolderId;
        String startTime;
        String lastModifiedTime;
    }
}
