package com.example.networkdisksystem.service.serviceImpl;

import com.example.networkdisksystem.entity.Users;
import com.example.networkdisksystem.mapper.UserMapper;
import com.example.networkdisksystem.service.UserService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    UserMapper mapper;

    @Resource
    HttpSession session;

    @Resource
    StringRedisTemplate template;


    @Override
    public int LoginCheck(String username, String password, boolean rememberMe,HttpServletResponse response){
        Users user = mapper.getUsersByUsername(username);
        if(!ObjectUtils.isEmpty(user)){
            String MD5Password = user.getPassword();
            boolean flag = new BCryptPasswordEncoder().matches(password, MD5Password);
            if(flag){
                //判断登录状态
                int condition = user.getCondition();
                if(condition == 0){
                    return 0;
                }else {
                    //将用户状态放入redis中
                    template.opsForValue().set("userCondition" + user.getUid(),""+1,10,TimeUnit.MINUTES);
                }
                //登录成功将用户信息放入session中
                session.setAttribute("user",user);
                session.setAttribute("mid",user.getMid());
                //remember-me将用户名密码放入cookie中
                if(rememberMe){
                    Cookie cookie_username=new Cookie("NetWorkDiskSystem_username",username);
                    cookie_username.setMaxAge(60*60*24*7);//cookie保存时间7天
                    cookie_username.setPath("/");//设置为根路径
                    cookie_username.setHttpOnly(true);//设置只有http请求可以访问cookie
                    Cookie cookie_password=new Cookie("NetWorkDiskSystem_password",user.getPassword());
                    cookie_password.setMaxAge(60*60*24*7);
                    cookie_password.setPath("/");
                    cookie_username.setHttpOnly(true);
                    response.addCookie(cookie_username);
                    response.addCookie(cookie_password);
                }
                return 2;
            } else {
                return 1;
            }
        }else {
            return 1;
        }
    }

    @Override
    public int to_loginCheck(String username, String password) {
        Users user = mapper.getUsersByUsername(username);
        if(!ObjectUtils.isEmpty(user)) {
            String MD5Password = user.getPassword();
            //boolean flag = new BCryptPasswordEncoder().matches(password, MD5Password);
            boolean flag=MD5Password.equals(password);
            if (flag) {
                //判断登录状态
                int condition = user.getCondition();
                if(condition == 0){
                    return 0;
                }else {
                    //将用户状态放入redis中
                    template.opsForValue().set("userCondition" + user.getUid(),""+1,10, TimeUnit.MINUTES);
                }
                //登录成功将用户信息放入session中
                session.setAttribute("user", user);
                session.setAttribute("mid",user.getMid());
                return 2;
            }else {
                return 1;
            }
        }
        return 1;
    }
}
