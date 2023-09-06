package com.example.networkdisksystem.entity;

import lombok.Data;

@Data
public class FileEntity {
    int fid;
    int mid;
    String fileName;
    String fileSize;
    int uid;
}
