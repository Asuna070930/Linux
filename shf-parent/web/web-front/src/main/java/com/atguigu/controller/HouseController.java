package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.*;
import com.atguigu.result.Result;
import com.atguigu.service.*;
import com.atguigu.vo.HouseQueryVo;
import com.atguigu.vo.HouseVo;
import com.github.pagehelper.PageInfo;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/house")
public class HouseController {

    @Reference
    private HouseService houseService;

    @Reference
    private CommunityService communityService;

    @Reference
    private HouseBrokerService houseBrokerService;

    @Reference
    private HouseImageService houseImageService;

    @Reference
    private UserFollowService userFollowService;


    /*
     房屋详情介绍
     axios.get('/house/info/'+this.id)
     */
    @RequestMapping("/info/{id}")
    public Result info(@PathVariable Long id, ModelMap modelMap, HttpServletRequest request) {
        // 获取房屋对象
        House house = houseService.getById(id);
        // 获取小区对象
        Community community = communityService.getById(house.getCommunityId());
        // 获取中介对象
        List<HouseBroker> houseBroker = houseBrokerService.findListByHouseId(id);
        // 获取图片对象
        List<HouseImage> houseImage1List = houseImageService.findList(id, 1);
        // 是否关注房源
        Boolean isFollow = false;
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute("USER");
        // 判断当前用户是否已经登录
        if (userInfo != null) {
            Long userInfoId = userInfo.getId();
            // 当前登录用户是否关注当前的房源
            isFollow = userFollowService.isFollowed(id, userInfoId);
        }
        // 根据房屋id，查询关注表
        modelMap.addAttribute("house", house);
        modelMap.addAttribute("community", community);
        modelMap.addAttribute("houseBrokerList", houseBroker);
        modelMap.addAttribute("houseImage1List", houseImage1List);
        modelMap.addAttribute("isFollow", isFollow);
        return Result.ok(modelMap);

    }


//     axios.get("/house/list/"+pageNum+"/"+this.page.pageSize,this.houseQueryVo)


    @RequestMapping("/list/{pageNum}/{pageSize}")
    public Result findListPage(@PathVariable Integer pageNum,
                               @PathVariable Integer pageSize,
                               @RequestBody HouseQueryVo houseQueryVo) {
        PageInfo<HouseVo> pageInfo = houseService.findListPage(pageNum, pageSize, houseQueryVo);
        return Result.ok(pageInfo);

    }
}
