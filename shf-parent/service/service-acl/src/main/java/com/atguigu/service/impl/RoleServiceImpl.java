package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseService;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.AdminRoleDao;
import com.atguigu.dao.RoleDao;
import com.atguigu.entity.AdminRole;
import com.atguigu.entity.Role;
import com.atguigu.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = RoleService.class)
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {

    @Resource
    private RoleDao roleDao;

    @Autowired
    private AdminRoleDao adminRoleDao;

    @Override
    protected BaseDao<Role> getEntityDao() {
        return roleDao;
    }

    /**
     * noAssginRoleList : 没有分配的角色
     * assginRoleList ： 已经分配的角色
     *
     * @return
     */
    @Override
    public Map<String, Object> findRoleByAdminId(Long adminId) {
        // 根据用户id，查询用户当前已经拥有的角色
        List<Long> exitRoleIdList = adminRoleDao.findRoleIdByAdminId(adminId);
        // 先查询所有的角色
        List<Role> allRoleList = roleDao.findAll();

        // 没有分配的角色
        List<Role> noAssginRoleList = new ArrayList<>();
        // 已经分配的角色
        List<Role> assginRoleList = new ArrayList<>();
        // 所有的角色集合里面排除当前用户已经拥有的角色
        for (Role role : allRoleList) {
            // 判断当前用户已经拥有的角色，是否包含在集合里面
            if (exitRoleIdList.contains(role.getId())) {
                // 已经分配的角色
                assginRoleList.add(role);
            } else {
                noAssginRoleList.add(role);
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("noAssginRoleList", noAssginRoleList);
        map.put("assginRoleList", assginRoleList);

        return map;
    }

    @Override
    public void saveAdminAndRole(Long[] roleIds, Long adminId) {
        // 先清空数据
        adminRoleDao.deleteByAdminId(adminId);
//        adminRoleDao.save()
        for (Long roleId : roleIds) {
            // [1,2,3,,4,5]
            // 判断数组是否有空值
            // continue ,break,return
            // for循环和增强for循环有啥区别
            // if eles Switch
            if (StringUtils.isEmpty(roleId))
                continue;

            AdminRole adminRole = new AdminRole();
            adminRole.setAdminId(adminId);
            adminRole.setRoleId(roleId);
            adminRoleDao.insert(adminRole);
        }
    }
}
