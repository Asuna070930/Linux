package com.atguigu.config;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.entity.Admin;
import com.atguigu.service.AdminService;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Reference
    private AdminService adminService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名,查询用户的对象
        Admin admin = adminService.getUserByUsername(username);
        //判断当前用户是否有值
        if (admin == null){
            return null;
        }
        String password = admin.getPassword();
        return new User(username,password, AuthorityUtils.commaSeparatedStringToAuthorityList(""));
    }
}
