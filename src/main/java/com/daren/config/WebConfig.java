//package com.hmdp.config;
//
//import com.hmdp.Interceptor.LoginInterceptor;
//import com.hmdp.Interceptor.RefreshTokenInterceptor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Autowired
//    private LoginInterceptor loginInterceptor;
//    @Autowired
//    private RefreshTokenInterceptor refreshTokenInterceptor;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//
//        registry.addInterceptor(loginInterceptor)
//                .addPathPatterns("/**")   // 拦截所有请求
//                .excludePathPatterns(
//                        "/shop/**",
//                        "/voucher/**",
//                        "/shop-type/**",
//                        "/upload/**",
//                        "/blog/hot",
//                        "/user/code",
//                        "/user/login",
//                        "/actuator/startup"
//                ).order(1); // 排除登录接口
//        registry.addInterceptor(refreshTokenInterceptor)
//                .addPathPatterns("/**").order(0);
//    }
//}