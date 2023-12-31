package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.UserFollow;
import com.atguigu.entity.UserInfo;
import com.atguigu.result.Result;
import com.atguigu.service.UserFollowService;
import com.atguigu.vo.UserFollowVo;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/userFollow")
public class UserFollowController {

    @Reference
    private UserFollowService userFollowService;

    //    axios.get("/userFollow/auth/cancelFollow/"+id) 关注id
    @RequestMapping("/auth/cancelFollow/{id}")
    public Result cancelFollow(@PathVariable Long id){
        userFollowService.cancelFollow(id);
        return Result.ok();
    }



    // 查询我关注的所有房源
//    xios.get('/userFollow/auth/list/'+pageNum+'/'+this.page.pageSize)
    @RequestMapping("/auth/list/{pageNum}/{pageSize}")
    public Result findListPage(@PathVariable Integer pageNum,@PathVariable Integer pageSize,HttpServletRequest request){
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute("USER");
        Long userInfoId = userInfo.getId();
        PageInfo<UserFollowVo> pageInfo = userFollowService.findListPage(pageNum,pageSize,userInfoId);
        return Result.ok(pageInfo);
    }


    //    axios.get('/userFollow/auth/follow/'+this.id)
    @RequestMapping("/auth/follow/{houseId}")
    public Result follow(@PathVariable("houseId") Long houseId, HttpServletRequest request){
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute("USER");
        Long userInfoId = userInfo.getId();
        userFollowService.follow(userInfoId,houseId);
        return Result.ok();
    }
}
