package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Admin;
import com.atguigu.entity.Permission;
import com.atguigu.service.AdminService;
import com.atguigu.service.PermissionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@SuppressWarnings({"unchecked", "rawtypes"})
public class IndexController {

    private final static String PAGE_INDEX = "frame/index";
    private final static String PAGE_MAIN = "frame/main";

    @Reference
    private AdminService adminService;

    @Reference
    private PermissionService permissionService;

    @RequestMapping("/login")
    public String login(){
        return "frame/login";
    }


    /**
     * 框架首页
     *
     * @return
     */
    @GetMapping("/")
    public String index(ModelMap modelMap) {
        // 假设当前登录的用户是admin
        Long adminId = 1L;
        // 根据用户id，查询用户对象
        Admin admin = adminService.getById(adminId);
        // 动态展示左侧菜单
        List<Permission> permissionList = permissionService.findMenuPermissionByAdminId(adminId);
        modelMap.addAttribute("permissionList", permissionList);
        modelMap.addAttribute("admin", admin);
        return PAGE_INDEX;
    }


    /**
     * 框架主页
     *
     * @return
     */
    @GetMapping("/main")
    public String main() {

        return PAGE_MAIN;
    }
}