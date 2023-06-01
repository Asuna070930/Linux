package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Admin;
import com.atguigu.entity.Permission;
import com.atguigu.service.AdminService;
import com.atguigu.service.PermissionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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
    private final static String PAGE_AUTH = "frame/auth";

    @Reference
    private AdminService adminService;

    @Reference
    private PermissionService permissionService;

    @RequestMapping("/auth")
    public String auth(){
        return PAGE_AUTH;
    }

    @RequestMapping("/login")
    public String login() {
        return "frame/login";
    }


    /**
     * 框架首页
     *
     * @return
     */
    @GetMapping("/")
    public String index(ModelMap modelMap) {
        // 需要从security 的上下文容器获取对象
        //SecurityContextHolder.getContext() 获取security容器
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //我们的用户名 ,密码 权限 都存储在user对象里面
        User user = (User) authentication.getPrincipal();


        // 根据用户id，查询用户对象
        Admin admin = adminService.getUserByUsername(user.getUsername());
        // 动态展示左侧菜单
        List<Permission> permissionList = permissionService.findMenuPermissionByAdminId(admin.getId());
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