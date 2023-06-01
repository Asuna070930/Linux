package com.atguigu.config;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Admin;
import com.atguigu.service.AdminService;
import com.atguigu.service.PermissionService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Reference
    private AdminService adminService;

    @Reference
    private PermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户的名字，查询用户的对象
        Admin admin = adminService.getUserByUsername(username);
        // 判断当前登录的用户是否有值
        if (admin == null){
            return null;
        }
        String password = admin.getPassword();

        // 根据当前登录的用户，查询用户的权限
        List<String> codeList =  permissionService.findCodeListByAdminId(admin.getId());
        List<GrantedAuthority> list = new ArrayList<>();
        for (String code : codeList) {
            if (StringUtils.isEmpty(code)) continue;
            list.add(new SimpleGrantedAuthority(code));

        }

        return new User(username,password,list );
    }
}
