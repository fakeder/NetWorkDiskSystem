package com.example.networkdisksystem.entity;

import lombok.Data;

@Data
public class Users {
    int uid;
    String username;
    String password;
    String email;
    int mid;
    String totalSize;
    long totalSizeByte;
    String usedSize;
    long usedSizeByte;
    int VIPFlag;
}
