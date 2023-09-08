package com.example.networkdisksystem.service.serviceImpl;

import com.example.networkdisksystem.entity.Users;
import com.example.networkdisksystem.mapper.UserMapper;
import com.example.networkdisksystem.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    UserMapper mapper;

    @Resource
    HttpSession session;

    @Override
    public boolean LoginCheck(String username, String password, boolean rememberMe,HttpServletResponse response){
        Users user = mapper.getUsersByUsername(username);
        if(!ObjectUtils.isEmpty(user)){
            String MD5Password = user.getPassword();
            boolean flag = new BCryptPasswordEncoder().matches(password, MD5Password);
            if(flag){
                //登录成功将用户信息放入session中
                session.setAttribute("user",user);
                session.setAttribute("mid",user.getMid());
                //remember-me将用户名密码放入cookie中
                if(rememberMe){
                    Cookie cookie_username=new Cookie("username",username);
                    cookie_username.setMaxAge(60*60*24*7);//cookie保存时间7天
                    cookie_username.setPath("/");//设置为根路径
                    Cookie cookie_password=new Cookie("password",password);
                    cookie_password.setMaxAge(60*60*24*7);
                    cookie_password.setPath("/");
                    response.addCookie(cookie_username);
                    response.addCookie(cookie_password);
                }
                return true;
            } else {
                return false;
            }
        }else {
            return false;
        }
    }

    @Override
    public boolean to_loginCheck(String username, String password) {
        Users user = mapper.getUsersByUsername(username);
        if(!ObjectUtils.isEmpty(user)) {
            String MD5Password = user.getPassword();
            boolean flag = new BCryptPasswordEncoder().matches(password, MD5Password);
            if (flag) {
                session.setAttribute("user", user);
                session.setAttribute("mid",user.getMid());
                return true;
            }else {
                return false;
            }
        }
        return false;
    }
}
