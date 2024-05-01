package com.example.networkdisksystem.filter;

import com.example.networkdisksystem.entity.Users;
import com.example.networkdisksystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ObjectUtils;
import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter("/*")
public class LoginCheckFilter implements Filter {

    @Resource
    UserService userService;

    @Resource
    StringRedisTemplate template;


    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();
    public static String[] URL={
            "/static/**",
            "/user/**"
    };

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request= (HttpServletRequest) servletRequest;
        HttpServletResponse response= (HttpServletResponse) servletResponse;
        String url=request.getRequestURI();
        log.info("拦截到的请求URL：{}",url);

        if(check(url)){
            log.info("{}不需要验证,放行！",url);
            chain.doFilter(request,response);
            return;
        }

        //获取session中的用户信息判断是否登录
        if(request.getSession().getAttribute("user")!= null){
            //判断用户状态
            Users users= (Users) request.getSession().getAttribute("user");
            String condition = template.opsForValue().get("userCondition" + users.getUid());
            //状态过期
            if(ObjectUtils.isEmpty(condition)){
                //重新获取状态
                int loginCheck = userService.to_loginCheck(users.getUsername(), users.getPassword());
                if (loginCheck == 2) {
                    log.info("用户：{} -- 免密登录", users.getUsername());
                    chain.doFilter(request, response);
                    return;
                }else if (loginCheck == 1){
                    log.info("用户：{} -- 用户名密码错误", users.getUsername());
                }else if (loginCheck == 0){
                    log.info("用户：{} -- 禁用中！", users.getUsername());
                }else {
                    log.info("系统异常");
                }
            }else {
                if(condition.equals("0")){
                    log.info("用户被禁用！");
                }else {
                    long id = Thread.currentThread().getId();
                    log.info("用户已登录，放行！");
                    log.info("{}的线程ID为：{}",url,id);
                    chain.doFilter(request,response);
                    return;
                }
            }
            response.sendRedirect("/user/login");
            return;
        }

        //检查cookie中是否存了用户名密码
        Cookie[] cookies = request.getCookies();
        if(!ObjectUtils.isEmpty(cookies)) {
          String username = null;
          String password = null;
          for (Cookie cookie : cookies) {
            if (cookie.getName().equals("NetWorkDiskSystem_username")) {
              username = cookie.getValue();
            }
            if (cookie.getName().equals("NetWorkDiskSystem_password")) {
              password = cookie.getValue();
            }
          }
          if (username != null && password != null) {
            int loginCheck = userService.to_loginCheck(username, password);
            if (loginCheck == 2) {
              log.info("用户：{} -- 免密登录", username);
              chain.doFilter(request, response);
              return;
            }else if (loginCheck == 1){
                log.info("用户：{} -- 用户名密码错误", username);
            }else if (loginCheck == 0){
                log.info("用户：{} -- 禁用中！", username);
            }
          }
        }
        log.info("用户未登录");
        response.sendRedirect("/user/login");
    }

    //判断传入的url是否在URL中
    public static boolean check(String url){
        for (String s : URL) {
            if (PATH_MATCHER.match(s, url))
                return true;
        }
        return false;
    }
}
