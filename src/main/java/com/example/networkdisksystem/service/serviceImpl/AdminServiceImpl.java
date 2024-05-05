package com.example.networkdisksystem.service.serviceImpl;

import com.example.networkdisksystem.config.AdminConfig;
import com.example.networkdisksystem.entity.AdminEntity;
import com.example.networkdisksystem.entity.Users;
import com.example.networkdisksystem.mapper.AdminMapper;
import com.example.networkdisksystem.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminConfig adminConfig;

    @Autowired
    private HttpSession session;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private StringRedisTemplate template;

    @Override
    public boolean adminLogin(String username, String password) {
        if(username.equals(adminConfig.getUsername())&&password.equals(adminConfig.getPassword())){
            AdminEntity adminEntity=new AdminEntity();
            List<Users> usersList = adminMapper.getAllUsers(100);
            adminEntity.setUsersList(usersList);
            adminEntity.setCountUser(adminMapper.countUser());
            adminEntity.setUsedUser(adminMapper.countUsedUser());
            adminEntity.setForbiddenUser(adminMapper.countForbiddenUser());
            adminEntity.setVIPCount(adminMapper.countVIP());
            adminEntity.setOnlineUsers(template.keys("userCondition*").size());
            session.setAttribute("admin",adminEntity);
            return true;
        }
        return false;
    }
}
