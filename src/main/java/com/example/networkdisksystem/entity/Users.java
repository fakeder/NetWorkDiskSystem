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
    String usedSize;
    int VIPFlag;
}
