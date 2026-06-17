package com.daren.controller;

import com.daren.dto.Result;
import com.daren.utils.FastRedisScript;
import com.daren.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/hello")
public class HelloController {
    @Autowired
    FastRedisScript<Long> fastRedisScript;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @PostMapping()
    public String hello() {
        return "Hello World";
    }

    @GetMapping()
    public String test() {
        String stockKey = "seckill:voucher:" + 13;
        String orderKey = "seckill:order:" + 13;
        Long userId = UserHolder.getUser().getId();
        Long ans = stringRedisTemplate.execute(fastRedisScript, Arrays.asList(stockKey, orderKey), userId.toString());
        return ans.toString();
    }
    @PostMapping("seckill/{id}")
    public Result seckillVoucher(@PathVariable("id") Long voucherId) {
        Long userId = UserHolder.getUser().getId();
        String stockKey = "seckill:voucher:" + voucherId;
        String orderKey = "seckill:order:" + voucherId;
        Long ans = stringRedisTemplate.execute(fastRedisScript, Arrays.asList(stockKey, orderKey), userId.toString());
        if (ans != null && ans != 0) {
            return Result.fail(ans == 1 ? "库存不足" : "请勿重复下单");
        }
//        return voucherOrderServiceImpl.seckillVoucher(voucherId);
//        return Result.fail("功能未完成");
        return Result.ok("fail");
//        return voucherOrderServiceImpl.seckillVoucher(voucherId);
    }
}
