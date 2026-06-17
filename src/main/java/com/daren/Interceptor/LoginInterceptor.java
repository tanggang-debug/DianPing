package com.daren.Interceptor;

import com.daren.utils.UserHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (UserHolder.getUser() == null) {
            // 未登录，拦截
            response.setStatus(401);
            return false;
        }
        return true; // true 表示放行
    }


}