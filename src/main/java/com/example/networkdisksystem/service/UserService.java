package com.example.networkdisksystem.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserService {
    boolean LoginCheck(String username, String password, boolean rememberMe,  HttpServletResponse response);

    boolean to_loginCheck(String username,String password);
}
