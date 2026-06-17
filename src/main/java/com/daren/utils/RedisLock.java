package com.daren.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RedisLock {
    private final StringRedisTemplate stringRedisTemplate;
    String PREFIX = "lock:";
    String value = UUID.randomUUID().toString();
    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT;

    static {
        UNLOCK_SCRIPT = new DefaultRedisScript<>();
        UNLOCK_SCRIPT.setLocation(new ClassPathResource("unlock.lua"));
        UNLOCK_SCRIPT.setResultType(Long.class);
    }

    public RedisLock(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public boolean tryLock() {
        Long id = UserHolder.getUser().getId();
        String key = PREFIX + id;
        Boolean b = stringRedisTemplate.opsForValue().setIfAbsent(key, value, 100, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(b);
    }

    public void unlock() {
        Long id = UserHolder.getUser().getId();
        String key = PREFIX + id;
        stringRedisTemplate.execute(UNLOCK_SCRIPT, Collections.singletonList(key), value);
    }
}
