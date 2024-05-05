package com.example.networkdisksystem.service;

public interface AdminService {
    /**
     * 验证管理员账号密码
     * @param username 用户名
     * @param password 密码
     */
    boolean adminLogin(String username,String password);
}
