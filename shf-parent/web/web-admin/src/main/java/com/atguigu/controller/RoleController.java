package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Role;
import com.atguigu.service.PermissionService;
import com.atguigu.service.RoleService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {
    private final static String PAGE_INDEX = "role/index";
    private final static String PAGE_CREATE = "role/create";
    private final static String PAGE_EDIT = "role/edit";

    private final static String LIST_ACTION = "redirect:/role";

    private final static String PAGE_ASSGIN_SHOW = "role/assignShow";
    private static final String PAGE_SUCCESS = "common/successPage";

    @Reference
    private RoleService roleService;

    @Reference
    private PermissionService permissionService;

    /**
     * 保存权限
     * th:action="@{/role/assignPermission}"
     */
    @PreAuthorize("hasAuthority('role.assgin')")
    @RequestMapping("/assignPermission")
    public String assignPermission(Long roleId, Long[] permissionIds) {
        permissionService.saveRoleIdAndPermissionIds(roleId, permissionIds);
        return PAGE_SUCCESS;
    }


    /**
     * 给角色分配权限，展示页面
     * opt.openWin("/role/assignShow/"+id,
     *
     * @return
     */
    @PreAuthorize("hasAuthority('role.assgin')")
    @RequestMapping("/assignShow/{roleId}")
    public String assignShow(@PathVariable Long roleId, ModelMap modelMap) {
        // 需要查询所有的节点 根据角色id查询所有的权限
        List<Map<String, Object>> zNodes = permissionService.findPermissionByRoleId(roleId);
        modelMap.addAttribute("zNodes", zNodes);
        modelMap.addAttribute("roleId", roleId);
        return PAGE_ASSGIN_SHOW;
    }


    /**
     * 删除数据
     */
    @PreAuthorize("hasAuthority('role.delete')")
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        roleService.delete(id);
        //删除成功,刷新页面
        return LIST_ACTION;
    }


    /**
     * 更新数据
     */
    @PreAuthorize("hasAuthority('role.edit')")
    @RequestMapping("/update")
    public String update(Role role) {
        roleService.update(role);
        return PAGE_SUCCESS;
    }

    /**
     * 回显数据
     */
    @PreAuthorize("hasAuthority('role.edit')")
    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable Long id, ModelMap modelMap) {
        Role role = roleService.getById(id);
        modelMap.addAttribute("role", role);
        return PAGE_EDIT;
    }

    /**
     * 保存数据
     *
     * @PostMapping : 只能接收post请求,有一个限制
     * @RequextMapping : 可以接收所有请求,没有限制
     */
    @PreAuthorize("hasAuthority('role.create2')")
    @PostMapping("/save")
    public String save(Role role) {
        roleService.insert(role);
        return PAGE_SUCCESS;
    }

    /**
     * 展示页面
     *
     * @return
     */
    @PreAuthorize("hasAuthority('role.create')")
    @RequestMapping("/create")
    public String create() {
        return PAGE_CREATE;
    }

    @PreAuthorize("hasAuthority('role.show')")
    @RequestMapping
    public String Index(ModelMap modelMap, HttpServletRequest request) {
        Map<String, Object> filters = getFilters(request);
        PageInfo<Role> pageInfo = roleService.findPage(filters);

//        List<Role> list = roleService.findAll();
        modelMap.addAttribute("filters", filters);
        modelMap.addAttribute("page", pageInfo);
        return PAGE_INDEX;
    }

}
