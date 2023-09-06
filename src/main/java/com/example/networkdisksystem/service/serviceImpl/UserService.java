package com.example.networkdisksystem.service.serviceImpl;

import com.example.networkdisksystem.entity.Users;
import com.example.networkdisksystem.mapper.UserMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Service
public class UserService implements UserDetailsService {
    @Resource
    UserMapper mapper;

    @Resource
    HttpSession session;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users=mapper.getUsersByUsername(username);
        if (users==null) throw new UsernameNotFoundException("用户 "+username+" 登录失败，用户名不存在！");
        //用户信息,mid放入session
        session.setAttribute("user",users);
        session.setAttribute("mid",users.getMid());
        return User
                .withUsername(users.getUsername())
                .password(users.getPassword())
                .roles("user")
                .build();
    }
}
