package com.daren.controller;


import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@RestController
@RequestMapping("/redis")
public class BlogCommentsController {
    private final StringRedisTemplate redisTemplate;

    public BlogCommentsController(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/test")
    public String r() {
        redisTemplate.opsForValue().set("k", "v");
        return "ok";
    }
}
