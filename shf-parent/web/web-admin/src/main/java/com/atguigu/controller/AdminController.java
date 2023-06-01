package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Admin;
import com.atguigu.service.AdminService;
import com.atguigu.service.RoleService;
import com.atguigu.util.QiniuUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping(value = "/admin")
@SuppressWarnings({"unchecked", "rawtypes"})
public class AdminController extends BaseController {

    @Reference
    private AdminService adminService;

    @Reference
    private RoleService roleService;

    private final static String LIST_ACTION = "redirect:/admin";
    private final static String PAGE_INDEX = "admin/index";
    private final static String PAGE_CREATE = "admin/create";
    private final static String PAGE_EDIT = "admin/edit";
    private final static String PAGE_SUCCESS = "common/successPage";
    private final static String PAGE_UPLOED_SHOW = "admin/upload";
    private final static String PAGE_ASSIGN_SHOW = "admin/assignShow";


    /**
     * 保存用户角色
     * th:action="@{/admin/assignRole}"
     * 前端：前端工程师
     * 后端：JAVA,PHP,.net,python，go
     * android端：Android工程师
     * ios端：ios工程师
     */
    @RequestMapping("/assignRole")
    public String assignRole(Long[] roleIds, Long adminId) {
        roleService.saveAdminAndRole(roleIds, adminId);
        return PAGE_SUCCESS;
    }


    /**
     * 给公司员工设置一个工作岗位
     * opt.openWin('/admin/assignShow/'+id,'分配角色',550,450)
     */
    @RequestMapping("/assignShow/{id}")
    public String assignShow(@PathVariable Long id, ModelMap modelMap) {
        // 根据用户查询用户的角色
        Map<String, Object> roleMap = roleService.findRoleByAdminId(id);
        // 注意：当前的数据是map，所以需要使用all的方法，之前只是单纯传输单个对象
        modelMap.addAllAttributes(roleMap);
        modelMap.addAttribute("adminId", id);
        // 给员工分配角色
        return PAGE_ASSIGN_SHOW;
    }


    //th:action="@{/admin/upload/{id}(id=${id})}"
    @RequestMapping("/upload/{id}")
    public String upload(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws Exception {
        // 获取图片的原始名字 abc.jpg
        String originalFilename = file.getOriginalFilename();
        // 随机生成图片的名字
        String newFileName = UUID.randomUUID().toString();
        // 一张张的图片上传 abc.jpg abc.jpg
        // 上传图片
        QiniuUtils.upload2Qiniu(file.getBytes(), newFileName);
        // 图片的完整地址 = 域名 + 图片的名字
//                http://rv5fwefeb.hb-bkt.clouddn.com/FoMAocn5A-LmDoDv6TqrVY2iPfUK
        // 拼接图片的地址 ,注意：每个人的域名都不一样，必须要拷贝自己分配域名
        String url = "http://rv5fwefeb.hb-bkt.clouddn.com/" + newFileName;
        Admin admin = new Admin();
        admin.setId(id);
        admin.setHeadUrl(url);
        adminService.update(admin);
        return PAGE_SUCCESS;
    }


    /**
     * 上传头像页面
     *
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/uploadShow/{id}")
    public String uploadShow(ModelMap modelMap, @PathVariable Long id) {
        modelMap.addAttribute("id", id);
        return PAGE_UPLOED_SHOW;
    }

    @PostMapping("/upload/{id}")
    public String upload(@PathVariable Long id, @RequestParam(value = "file") MultipartFile file, HttpServletRequest request) throws IOException {
        try {
            String newFileName = UUID.randomUUID().toString();
            // 上传图片
            QiniuUtils.upload2Qiniu(file.getBytes(), newFileName);
            String url = "http://rv7d1v8uo.hb-bkt.clouddn.com/" + newFileName;
            Admin admin = new Admin();
            admin.setId(id);
            admin.setHeadUrl(url);
            adminService.update(admin);
            return PAGE_SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return PAGE_SUCCESS;
    }


    /**
     * 列表
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping
    public String index(ModelMap model, HttpServletRequest request) {
        Map<String, Object> filters = getFilters(request);
        PageInfo<Admin> page = adminService.findPage(filters);
        model.addAttribute("page", page);
        model.addAttribute("filters", filters);
        return PAGE_INDEX;
    }

    /**
     * 进入新增页面
     *
     * @param model
     * @param admin
     * @return
     */
    @GetMapping("/create")
    public String create() {
        return PAGE_CREATE;
    }

    /**
     * 保存新增
     *
     * @param admin
     * @param request
     * @return
     */
    @PostMapping("/save")
    public String save(Admin admin) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //使用加密框架进行加密
        String encode = passwordEncoder.encode(admin.getPassword());
        //设置密文
        admin.setPassword(encode);
        //设置默认头像
        admin.setHeadUrl("http://47.93.148.192:8080/group1/M00/03/F0/rBHu8mHqbpSAU0jVAAAgiJmKg0o148.jpg");
        adminService.insert(admin);
        return PAGE_SUCCESS;
    }

    /**
     * 进入编辑页面
     *
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/edit/{id}")
    public String edit(ModelMap model, @PathVariable Long id) {
        Admin admin = adminService.getById(id);
        model.addAttribute("admin", admin);
        return PAGE_EDIT;
    }

    /**
     * 保存更新
     *
     * @param id
     * @param admin
     * @param request
     * @return
     */
    @PostMapping(value = "/update")
    public String update(Admin admin) {

        adminService.update(admin);

        return PAGE_SUCCESS;
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        adminService.delete(id);
        return LIST_ACTION;
    }
}