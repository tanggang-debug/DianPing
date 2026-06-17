package com.daren.config;

import com.daren.utils.FastRedisScript;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;

@Configuration
public class RedisConfig {

    @Bean
    public FastRedisScript<Long> seckillScript() {
        DefaultRedisScript<Long> temp = new DefaultRedisScript<>();
        temp.setLocation(new ClassPathResource("seckill.lua"));
        temp.setResultType(Long.class);
        
        // 返回单例实例
        return new FastRedisScript<>(
            temp.getSha1(), 
            temp.getScriptAsString(), 
            Long.class
        );
    }
}