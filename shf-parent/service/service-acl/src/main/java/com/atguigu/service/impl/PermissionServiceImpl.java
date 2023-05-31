package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.PermissionDao;
import com.atguigu.dao.RolePermissionDao;
import com.atguigu.entity.Permission;
import com.atguigu.entity.RolePermission;
//import com.atguigu.helper.PermissionHelper;
import com.atguigu.helper.PermissionHelper;
import com.atguigu.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = PermissionService.class)
public class PermissionServiceImpl extends BaseServiceImpl<Permission> implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private RolePermissionDao rolePermissionDao;

    @Override
    protected BaseDao<Permission> getEntityDao() {
        return permissionDao;
    }

    @Override
    public List<Map<String, Object>> findPermissionByRoleId(Long roleId) {
        // 首先查询当前角色原有的权限
        List<Long> permissionList = rolePermissionDao.findPermissionListByRoleId(roleId);
        // 查询所有的权限，进行展示
        List<Permission> permissionAllList = permissionDao.findAll();
        //参考文档：http://www.treejs.cn/v3/demo.php#_201
        // { id:2, pId:0, name:"随意勾选 2", checked:true, open:true},
        // 迭代所有的权限
        List<Map<String, Object>> zNodes = new ArrayList<>();
        for (Permission permission : permissionAllList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", permission.getId());
            map.put("pId", permission.getParentId());
            map.put("name", permission.getName());
            // 需要勾选当前角色的权限
            if (permissionList.contains(permission.getId())) {
                map.put("checked", true);
            }
            zNodes.add(map);
        }
        return zNodes;
    }

    @Override
    public void saveRoleIdAndPermissionIds(Long roleId, Long[] permissionIds) {
        rolePermissionDao.deleteByRoleId(roleId);
        for (Long permissionId : permissionIds) {
            if (StringUtils.isEmpty(permissionId)) continue;
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            rolePermissionDao.insert(rolePermission);
        }
    }

    @Override
    public List<Permission> findMenuPermissionByAdminId(Long adminId) {
        List<Permission> permissionList = null;
        // 判断adminId，如果是超级管理员 1，具备所有的权限
        if (adminId.longValue() == 1) {
            // 说明是超级管理员
            permissionList = permissionDao.findAll();
        } else {
            // 具体是什么权限，就返回什么权限
            permissionList = permissionDao.findListByAdminId(adminId);
        }
        // 需要把查询出来的数据，转换成菜单
        List<Permission> result = PermissionHelper.bulid(permissionList);
        return result;
    }
}
