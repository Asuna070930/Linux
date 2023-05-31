package com.atguigu.dao;

import com.atguigu.base.BaseDao;
import com.atguigu.entity.HouseImage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HouseImageDao extends BaseDao<HouseImage> {
    //如果是多个参数,必须要添加注解
    //可以使用hashmap
    List<HouseImage> findList(@Param("houseId") Long id, @Param("type") Integer i);

}