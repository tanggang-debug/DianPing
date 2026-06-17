package com.hmdp.entity;

import lombok.Data;

@Data
public class RedisData<T> {
    private T data;
    private Long expireTime;
}
