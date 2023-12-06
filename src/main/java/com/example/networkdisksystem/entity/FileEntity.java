package com.example.networkdisksystem.entity;

import lombok.Data;

import java.util.Date;

@Data
public class FileEntity {

    /**
     * DB录入/取得
     */
    @Data
    public static class FileInputEntity{
        int fid;
        int mid;
        String fileName;
        String fileSize;
        int uid;
        Date startTime;//创建时间
        Date lastModifiedTime;//修改时间
    }


    /**
     * 页面展示
     */
    @Data
    public static class FileOutputEntity{
        int fid;
        int mid;
        String fileName;
        String fileSize;
        int uid;
        String startTime;//创建时间
        String lastModifiedTime;//修改时间
        String type;//文件类型
        String fileImage;//文件图片
    }
}
