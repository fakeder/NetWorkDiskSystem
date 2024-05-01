package com.example.networkdisksystem.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserService {
    int LoginCheck(String username, String password, boolean rememberMe,  HttpServletResponse response);

    int to_loginCheck(String username,String password);
}
