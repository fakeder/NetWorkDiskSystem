package com.example.networkdisksystem.filter;

import com.example.networkdisksystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
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
            long id = Thread.currentThread().getId();
            log.info("用户已登录，放行！");
            log.info("{}的线程ID为：{}",url,id);
            chain.doFilter(request,response);
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
                boolean loginCheck = userService.to_loginCheck(username, password);
                if (loginCheck) {
                    log.info("用户：{} -- 免密登录", username);
                    chain.doFilter(request, response);
                    return;
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
