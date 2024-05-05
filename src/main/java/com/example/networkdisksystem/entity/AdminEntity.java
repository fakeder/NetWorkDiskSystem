package com.example.networkdisksystem.entity;

import lombok.Data;

import java.util.List;

@Data
public class AdminEntity {
    /**
     * 用户总量.
     */
    int countUser;
    /**
     * 在线用户数量.
     */
    int onlineUsers;
    /**
     * 启用用户数量.
     */
    int usedUser;
    /**
     * 禁用用户数量
     */
    int forbiddenUser;
    /**
     * VIP用户数量.
     */
    int VIPCount;
    /**
     * 用户列表.
     */
    List<Users> usersList;
}
