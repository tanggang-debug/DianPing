package com.daren.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daren.dto.Result;
import com.daren.entity.RedisData;
import com.daren.entity.Shop;
import com.daren.mapper.ShopMapper;
import com.daren.service.IShopService;
import com.daren.utils.SystemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.domain.geo.Metrics;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {
    private final StringRedisTemplate stringRedisTemplate;
    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);
    private static final Logger log = LoggerFactory.getLogger(ShopServiceImpl.class);

    public ShopServiceImpl(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public Result queryShopById(Long id) {
//        String key = "cache:shop:" + id;
//        String shopJson = stringRedisTemplate.opsForValue().get(key);
//        if (shopJson != null && !shopJson.isEmpty()) {
//            Shop shop = JSONUtil.toBean(shopJson, Shop.class);
//            return Result.ok(shop);
//        }
//        if (Objects.equals(shopJson, "")) {
//            return Result.fail("店铺不存在");
//        }
//        Shop shop = getById(id);
//        if (shop == null) {
//            stringRedisTemplate.opsForValue().set(key, "", 60, TimeUnit.SECONDS);
//            return Result.fail("店铺不存在");
//        }
//        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(shop), 30, TimeUnit.MINUTES);
//        return Result.ok(getById(id));
        return queryWithLogicExpires(id);
    }

    public Result queryWithMutex(Long id) {
        String lockKey = "lock:shop:" + id;
        String key = "cache:shop:" + id;
        String shopJson = stringRedisTemplate.opsForValue().get(key);
        if (shopJson != null && !shopJson.isEmpty()) {
            Shop shop = JSONUtil.toBean(shopJson, Shop.class);
            return Result.ok(shop);
        }
        if (Objects.equals(shopJson, "")) {
            return Result.fail("店铺不存在");
        }
        try {
            boolean ans = tryLock(lockKey);
            if (ans) {
                Shop shop = getById(id);
                if (shop == null) {
                    stringRedisTemplate.opsForValue().set(key, "", 60, TimeUnit.SECONDS);
                    return Result.fail("店铺不存在");
                }
                stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(shop), 30, TimeUnit.MINUTES);
                return Result.ok(shop);
            } else {
                sleep(50);
                return queryWithMutex(id);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            unLock(lockKey);
        }
    }

    public Result queryWithLogicExpires(Long id) {
        String lockKey = "lock:shop:" + id;
        String key = "cache:shop:" + id;
        String jsonData = stringRedisTemplate.opsForValue().get(key);
        if (jsonData == null || jsonData.isEmpty()) {
            return Result.fail("店铺不存在");
        }
        RedisData redisData = JSONUtil.toBean(jsonData, RedisData.class);
        Shop shop = JSONUtil.toBean((JSONObject) redisData.getData(), Shop.class);
        if (redisData.getExpireTime() >= System.currentTimeMillis()) {
            return Result.ok(shop);
        }
        Boolean ans = tryLock(lockKey);
        if (Boolean.TRUE.equals(ans)) {
            try {
                CACHE_REBUILD_EXECUTOR.submit(() -> Result.ok(saveShopToRedis(id)));
//                Shop updatedShop = saveShopToRedis(id);
//                return Result.ok(updatedShop);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                unLock(lockKey);
            }
        }
        return Result.ok(shop);
    }

    public Shop saveShopToRedis(Long id) {
        String key = "cache:shop:" + id;
        Shop shop = getById(id);
        RedisData<Shop> redisData = new RedisData<>();
        if (shop == null) {
            stringRedisTemplate.opsForValue().set(key, "", 60, TimeUnit.SECONDS);
            return null;
        }
        redisData.setData(shop);
        redisData.setExpireTime(System.currentTimeMillis() + 60 * 1000);
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData));
        return shop;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(Shop shop) {
        if (shop.getId() == null) {
            return Result.fail("店铺id不能为空");
        }
        updateById(shop);
        String key = "cache:shop:" + shop.getId();
        stringRedisTemplate.delete(key);
        return Result.ok();
    }

    @Override
    public Result queryShopByType(Integer typeId, Integer current, Double x, Double y) {
        // 根据类型分页查询
        if (x == null || y == null) {
            Page<Shop> page = query()
                    .eq("type_id", typeId)
                    .page(new Page<>(current, SystemConstants.DEFAULT_PAGE_SIZE));
            // 返回数据
            return Result.ok(page.getRecords());
        }
        String key = "shop:geo:" + typeId;
        int begin = (current - 1) * 5;
        int end = current * 5;
        // 这是基于旧版 GEORADIUS 的写法
        Circle circle = new Circle(new Point(x, y), new Distance(5000, Metrics.METERS));
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                .includeDistance() // 包含距离
                .sortAscending()   // 排序
//                .sortDescending()
                .limit(5);         // 限制数量

        GeoResults<RedisGeoCommands.GeoLocation<String>> search = stringRedisTemplate.opsForGeo()
                .radius(key, circle, args);
        if (search == null) {
            return Result.ok(Collections.emptyList());
        }
        List<GeoResult<RedisGeoCommands.GeoLocation<String>>> list = search.getContent();
        if (list.isEmpty()) {
            return Result.ok(Collections.emptyList());
        }
        List<Long> ids = new ArrayList<>();
        for (GeoResult<RedisGeoCommands.GeoLocation<String>> item : list) {
//            log.debug("{}----{}", item.getContent().getName(), item.getDistance());
            ids.add(Long.valueOf(item.getContent().getName()));
        }
        String idStr = StrUtil.join(",", ids);
        List<Shop> shops = query().in("id", ids).last("ORDER BY FIELD(id," + idStr + ")").list();
        return Result.ok(shops);
    }

    public boolean tryLock(String key) {
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(flag);
    }

    public void unLock(String key) {
        stringRedisTemplate.delete(key);
    }
}
