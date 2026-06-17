package com.hmdp.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

@Component
public class SnowflakeIdWorker {

    private Snowflake snowflake;

    @PostConstruct
    public void init() {
        // 参数1：终端ID；参数2：数据中心ID
        // 在分布式环境下，这两个参数可以从配置文件或环境变量读取，保证每台机器不同
        this.snowflake = IdUtil.getSnowflake(1, 1);
    }

    public long nextId() {
        return snowflake.nextId();
    }
}