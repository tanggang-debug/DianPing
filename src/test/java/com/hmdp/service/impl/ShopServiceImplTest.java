package com.hmdp.service.impl;

import com.hmdp.entity.Shop;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
class ShopServiceImplTest {
    @Autowired
    private ShopServiceImpl shopService;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Test
    void saveShopToRedis() {
        shopService.saveShopToRedis(1L);
    }

    @Test
    void loadDataXYToRedis() {
        String keyPrefix = "shop:geo:";
        List<Shop> shopList = shopService.list();
        Map<Long, List<Shop>> list = shopList.stream().collect(Collectors.groupingBy(Shop::getTypeId));
        for (Long key : list.keySet()) {
            List<Shop> shops = list.get(key);
            for (Shop shop : shops) {
                stringRedisTemplate.opsForGeo().add(keyPrefix + key, new Point(shop.getX(), shop.getY()), shop.getId().toString());
            }
        }
    }

    @Test
    void testHyperLogLog() {
        String key = "hyperloglog:sign:user:";
        for (int i = 0; i < 100000; i++) {
            stringRedisTemplate.opsForHyperLogLog().add(key, "user" + i);
        }
        System.out.println(stringRedisTemplate.opsForHyperLogLog().size( key));
    }
}