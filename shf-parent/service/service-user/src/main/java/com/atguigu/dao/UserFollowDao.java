package com.atguigu.dao;

import com.atguigu.base.BaseDao;
import com.atguigu.entity.UserFollow;
import com.atguigu.vo.UserFollowVo;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

public interface UserFollowDao extends BaseDao<UserFollow> {

    UserFollow isFollowed(@Param("id") Long id,@Param("userInfoId") Long userInfoId);

    Page<UserFollowVo> findListPage(Long userInfoId);

}
