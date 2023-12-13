package com.example.networkdisksystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileExtractionEntity {
    int code;
    String username;//用户名
    String userImage;//用户头像
    String fileName;//文件名
    String fileImage;//文件图片
    String fileSize;//文件大小（B）
    long fileSizeByte;//文件大小(字节)
    String time;//提取码到期时间
    Integer fid;
    String reason;
}
