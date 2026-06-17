package com.hmdp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.dto.Result;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.Blog;
import com.hmdp.entity.Follow;
import com.hmdp.entity.ScrollResult;
import com.hmdp.entity.User;
import com.hmdp.mapper.BlogMapper;
import com.hmdp.service.IBlogService;
import com.hmdp.utils.SystemConstants;
import com.hmdp.utils.UserHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {

    private final UserServiceImpl userServiceImpl;
    private final StringRedisTemplate stringRedisTemplate;
    private final FollowServiceImpl followServiceImpl;

    public BlogServiceImpl(UserServiceImpl userServiceImpl, StringRedisTemplate stringRedisTemplate, FollowServiceImpl followServiceImpl) {
        this.userServiceImpl = userServiceImpl;
        this.stringRedisTemplate = stringRedisTemplate;
        this.followServiceImpl = followServiceImpl;
    }

    @Override
    public Result queryBlogById(Long id) {
        Blog blog = getById(id);
        if (blog == null) {
            return Result.fail("笔记不存在！");
        }
        User user = userServiceImpl.getById(blog.getUserId());
        blog.setName(user.getNickName());
        blog.setIcon(user.getIcon());
        isBlogLiked(blog);
        return Result.ok(blog);
    }

    private void isBlogLiked(Blog blog) {
        Long id = blog.getId();
        String key = "blog:likes:" + id;
        Double score = stringRedisTemplate.opsForZSet().score(key, UserHolder.getUser().getId().toString());
        Boolean isLiked = score != null;
        blog.setIsLike(isLiked);
    }

    @Override
    public Result queryHotBlog(Integer current) {
        // 根据用户查询
        Page<Blog> page = query()
                .orderByDesc("liked")
                .page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE));
        // 获取当前页数据
        List<Blog> records = page.getRecords();
        // 查询用户
        records.forEach(this::addUserInfoToBlog);
        return Result.ok(records);
    }

    @Override
    public Result likeBlog(Long id) {
        String key = "blog:likes:" + id;
        Double score = stringRedisTemplate.opsForZSet().score(key, UserHolder.getUser().getId().toString());
        Boolean isLiked = score != null;
        if (BooleanUtil.isTrue(isLiked)) {
            boolean success = update().setSql("liked=liked-1").eq("id", id).update();
            if (!success) {
                return Result.fail("取消点赞失败！");
            }
            stringRedisTemplate.opsForZSet().remove(key, UserHolder.getUser().getId().toString());
            return Result.ok("取消点赞成功！");
        } else {
            boolean success = update().setSql("liked=liked+1").eq("id", id).update();
            if (!success) {
                return Result.fail("点赞失败！");
            }
            stringRedisTemplate.opsForZSet().add(key, String.valueOf(UserHolder.getUser().getId()), System.currentTimeMillis());
            return Result.ok("点赞成功！");
        }
    }

    @Override
    public Result queryBlogLikesById(Long id) {
        String key = "blog:likes:" + id;
        Set<String> range = stringRedisTemplate.opsForZSet().range(key, 0, 4);
        if (range == null || range.isEmpty()) {
            return Result.ok();
        }
        List<Long> ids = range.stream().map(Long::valueOf).collect(Collectors.toList());
        ArrayList<UserDTO> users = new ArrayList<>();
        for (Long ID : ids) {
            users.add(BeanUtil.copyProperties(userServiceImpl.getById(ID), UserDTO.class));
        }
        return Result.ok(users);
    }

    @Override
    public Result saveBlog(Blog blog) {
        // 获取登录用户
        UserDTO user = UserHolder.getUser();
        blog.setUserId(user.getId());
        // 保存探店博文
        save(blog);
        // 返回id
        String keyPrefix = "follow:blog:";
        List<Follow> follows = followServiceImpl.query().select("user_id").eq("follow_user_id", user.getId()).list();
        if (follows == null || follows.isEmpty()) {
            return Result.ok();
        }
        for (Follow follow : follows) {
            stringRedisTemplate.opsForZSet().add(keyPrefix + follow.getUserId(), blog.getId().toString(), System.currentTimeMillis());
        }
        return Result.ok(blog.getId());
    }

    @Override
    public Result queryBlogOfFollow(Long timestamp, Integer offset) {
        String key = "follow:blog:" + UserHolder.getUser().getId();
        Set<ZSetOperations.TypedTuple<String>> typedTuples = stringRedisTemplate.opsForZSet().rangeByScoreWithScores(key, 0, timestamp, 0, 3);
        long minStamp = Long.MAX_VALUE;
        if (typedTuples == null || typedTuples.isEmpty()) {
            return Result.ok();
        }
        List<Long> blogIds = new ArrayList<>();
        for (ZSetOperations.TypedTuple<String> typedTuple : typedTuples) {
            minStamp = (long) Math.min(minStamp, typedTuple.getScore());
            blogIds.add(Long.valueOf(typedTuple.getValue()));
        }
        List<Blog> blogs = listByIds(blogIds);
        for (Blog blog : blogs) {
            // 5.1.查询blog有关的用户
//            queryBlogUser(blog);
            // 5.2.查询blog是否被点赞
            isBlogLiked(blog);
        }
        ScrollResult scrollResult = new ScrollResult();
        scrollResult.setList(blogs);
        scrollResult.setMinTime(minStamp);
        scrollResult.setOffset(1);
        return Result.ok(scrollResult);
    }


    public void addUserInfoToBlog(Blog blog) {
        String key = "blog:likes:" + blog.getId();
        Long userId = blog.getUserId();
        User user = userServiceImpl.getById(userId);
        blog.setName(user.getNickName());
        blog.setIcon(user.getIcon());
        Double score = stringRedisTemplate.opsForZSet().score(key, UserHolder.getUser().getId().toString());
        Boolean isLiked = score != null;
        blog.setIsLike(isLiked);
    }
}
