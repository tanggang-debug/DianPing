package com.daren.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import com.daren.dto.Result;
import com.daren.dto.UserDTO;
import com.daren.entity.Follow;
import com.daren.mapper.FollowMapper;
import com.daren.service.IFollowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daren.utils.UserHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
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
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements IFollowService {

    private final StringRedisTemplate stringRedisTemplate;
    private final UserServiceImpl userServiceImpl;

    public FollowServiceImpl(StringRedisTemplate stringRedisTemplate, UserServiceImpl userServiceImpl) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    public Result queryOrNotFollowBlogUser(Long blogUserId) {
        String userId = UserHolder.getUser().getId().toString();
        String key = "follow:user:" + blogUserId;
        Boolean isFollow = stringRedisTemplate.opsForSet().isMember(key, userId);
        if (BooleanUtil.isTrue(isFollow)) {
            return Result.ok(true);
        } else {
            return Result.ok(false);
        }
    }

    @Override
    public Result updateFollowStatus(Long followUserId, Boolean isFollow) {
        String key = "follow:user:" + followUserId;
        String userId = UserHolder.getUser().getId().toString();
        String focusKey = "focus:user:" + userId;
        if (isFollow) {
            Follow follow = new Follow();
            follow.setUserId(Long.valueOf(userId));
            follow.setFollowUserId(followUserId);
            boolean success = save(follow);
            if (!success) {
                return Result.fail("关注失败！");
            }
            stringRedisTemplate.opsForSet().add(key, userId);
            stringRedisTemplate.opsForSet().add(focusKey, followUserId.toString());
            return Result.ok("关注成功！");
        } else {
            boolean remove = update().eq("user_id", userId).eq("follow_user_id", followUserId).remove();
            if (!remove) {
                return Result.fail("取关失败！");
            }
            stringRedisTemplate.opsForSet().remove(key, userId);
            stringRedisTemplate.opsForSet().remove(focusKey, followUserId.toString());
            return Result.ok("取关成功！");
        }
    }

    @Override
    public Result queryCommonFollows(Long blogUserId) {
        String userId = UserHolder.getUser().getId().toString();
        String key1 = "focus:user:" + userId;
        String key2 = "focus:user:" + blogUserId.toString();
        Set<String> ans = stringRedisTemplate.opsForSet().intersect(key1, key2);
        if (ans == null || ans.isEmpty()) {
            return Result.ok();
        }
        List<Integer> ids = ans.stream().map(Integer::parseInt).collect(Collectors.toList());
        List<UserDTO> userDTOS = userServiceImpl.listByIds(ids).stream().map(user -> BeanUtil.copyProperties(user, UserDTO.class)).collect(Collectors.toList());
        return Result.ok(userDTOS);
    }
}
