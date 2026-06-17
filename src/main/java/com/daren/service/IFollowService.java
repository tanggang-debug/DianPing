package com.daren.service;

import com.daren.dto.Result;
import com.daren.entity.Follow;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
public interface IFollowService extends IService<Follow> {

    Result queryOrNotFollowBlogUser(Long blogUserId);

    Result updateFollowStatus(Long followUserId, Boolean isFollow);

    Result queryCommonFollows(Long blogUserId);
}
