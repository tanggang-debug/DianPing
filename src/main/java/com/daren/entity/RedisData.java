package com.daren.entity;

import lombok.Data;

@Data
public class RedisData<T> {
    private T data;
    private Long expireTime;
}
