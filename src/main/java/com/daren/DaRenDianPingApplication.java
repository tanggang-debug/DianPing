package com.daren;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.daren.mapper")
@SpringBootApplication
public class DaRenDianPingApplication {
    public static void main(String[] args) {
        SpringApplication.run(DaRenDianPingApplication.class, args);
    }
}
