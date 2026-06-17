package com.daren.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daren.dto.Result;
import com.daren.entity.ShopType;
import com.daren.mapper.ShopTypeMapper;
import com.daren.service.IShopTypeService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {

    private final StringRedisTemplate stringRedisTemplate;

    public ShopTypeServiceImpl(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }
    @Override
    public Result queryTypeList() {
        String key = "cache:shopType:list";
        List<String> jsonShopTypesList = stringRedisTemplate.opsForList().range(key, 0, -1);
        if (jsonShopTypesList != null && !jsonShopTypesList.isEmpty()) {
            List<ShopType> shopTypeList = jsonShopTypesList.stream().map(json -> JSONUtil.toBean(json, ShopType.class)).collect(Collectors.toList());
            return Result.ok(shopTypeList);
        }
        List<ShopType> typeList = query().orderByAsc("sort").list();
        for (ShopType shopType : typeList) {
            stringRedisTemplate.opsForList().rightPush(key, JSONUtil.toJsonStr(shopType));
        }
        return Result.ok(typeList);
    }
}
