package com.daren.Interceptor;

import cn.hutool.core.bean.BeanUtil;
import com.daren.dto.UserDTO;
import com.daren.utils.UserHolder;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Component
@AllArgsConstructor
public class RefreshTokenInterceptor implements HandlerInterceptor {
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("authorization");
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries("user:login:" + token);
        if (userMap.isEmpty()) {
            return true;
        }
        UserDTO user = BeanUtil.fillBeanWithMap(userMap, new UserDTO(), false);

        // 这里可以做登录校验
        UserHolder.saveUser(user);
//        stringRedisTemplate.expire("user:login:" + token, 240, TimeUnit.MINUTES);
        return true; // true 表示放行
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 移除用户
        UserHolder.removeUser();
    }
}