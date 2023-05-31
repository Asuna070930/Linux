package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.UserFollow;
import com.atguigu.vo.UserFollowVo;
import com.github.pagehelper.PageInfo;

public interface UserFollowService extends BaseService<UserFollow> {
    void follow(Long userInfoId, Long houseId);

    Boolean isFollowed(Long id, Long userInfoId);

    PageInfo<UserFollowVo> findListPage(Integer pageNum, Integer pageSize, Long userInfoId);


    void cancelFollow(Long id);
}
