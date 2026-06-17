package com.hmdp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hmdp.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class UserServiceImplTest {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void createUserToken() {
        IPage<User> p = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(1, 500);
        IPage<User> page = userService.page(p);
        List<User> users = page.getRecords();
        List<String> tokens = new ArrayList<>();
        for (User user : users) {
            String token = RandomUtil.randomString(10);
            tokens.add(token);
            Map<String, Object> userMap =
                    BeanUtil.beanToMap(user, new HashMap<>(), CopyOptions.create()
                            .setIgnoreNullValue(true)
                            .setFieldValueEditor((fieldName, fieldValue) -> fieldValue == null ? "" : fieldValue.toString()));
            stringRedisTemplate.opsForHash().putAll("user:login:" + token, userMap);
        }
        for (String token : tokens) {
            System.out.println(token);
        }
    }
}