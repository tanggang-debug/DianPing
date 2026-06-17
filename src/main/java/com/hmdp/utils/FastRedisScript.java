package com.hmdp.utils;

import org.springframework.data.redis.core.script.RedisScript;

public class FastRedisScript<T> implements RedisScript<T> {
    private final String sha1;
    private final String scriptText;
    private final Class<T> resultType;

    public FastRedisScript(String sha1, String scriptText, Class<T> resultType) {
        this.sha1 = sha1;
        this.scriptText = scriptText;
        this.resultType = resultType;
    }

    @Override public String getSha1() { return sha1; } // 没有锁！直接返回
    @Override public Class<T> getResultType() { return resultType; }
    @Override public String getScriptAsString() { return scriptText; }
}