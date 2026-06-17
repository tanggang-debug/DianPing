//package com.daren.config;
//
//import io.lettuce.core.resource.ClientResources;
//import io.lettuce.core.resource.DefaultClientResources;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class NettyConfig {
//    @Bean
//    public ClientResources clientResources() {
//        return DefaultClientResources.builder()
//                .ioThreadPoolSize(16) // 默认通常只有 4 个，调大它来处理并发 IO
//                .computationThreadPoolSize(16)
//                .build();
//    }
//}
