package com.atguigu.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.UserInfo;
import com.atguigu.result.Result;
import com.atguigu.result.ResultCodeEnum;
import com.atguigu.service.UserInfoService;
import com.atguigu.util.MD5;
import com.atguigu.vo.LoginVo;
import com.atguigu.vo.RegisterVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/userInfo")
@SuppressWarnings({"unchecked", "rawtypes"})
public class UserInfoController {
    @Reference
    private UserInfoService userInfoService;

    @GetMapping("/logout")
    public Result logout(HttpServletRequest request) {
        request.getSession().removeAttribute("USER");
        return Result.ok();
    }


    /**
     * 会员登录
     *
     * @param loginVo
     * @param request
     * @return
     */
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo, HttpServletRequest request) {
        String phone = loginVo.getPhone();
        String password = loginVo.getPassword();

        //校验参数
        if (StringUtils.isEmpty(phone) ||
                StringUtils.isEmpty(password)) {
            return Result.build(null, ResultCodeEnum.PARAM_ERROR);
        }

        UserInfo userInfo = userInfoService.getByPhone(phone);
        if (null == userInfo) {
            return Result.build(null, ResultCodeEnum.ACCOUNT_ERROR);
        }

        //校验密码
        if (!MD5.encrypt(password).equals(userInfo.getPassword())) {
            return Result.build(null, ResultCodeEnum.PASSWORD_ERROR);
        }

        //校验是否被禁用
        if (userInfo.getStatus() == 0) {
            return Result.build(null, ResultCodeEnum.ACCOUNT_LOCK_ERROR);
        }
        request.getSession().setAttribute("USER", userInfo);

        Map<String, Object> map = new HashMap<>();
        map.put("phone", userInfo.getPhone());
        map.put("nickName", userInfo.getNickName());
        return Result.ok(map);
    }


    /**
     * 注册
     * axios.post('/userInfo/register', this.registerVo)
     *
     * @return
     */
    @RequestMapping("/register")
    public Result register(@RequestBody RegisterVo registerVo, HttpServletRequest request) {
        String phone = registerVo.getPhone();
        String code = registerVo.getCode();
        String password = registerVo.getPassword();
        String nickName = registerVo.getNickName();
        // 校验数据
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)
                || StringUtils.isEmpty(password) || StringUtils.isEmpty(nickName)) {
            return Result.build(null, ResultCodeEnum.PARAM_ERROR);
        }
        // 获取session里面的验证码
        String sessionCode = (String) request.getSession().getAttribute("CODE");
        // 真正的验证码，需要跟前端用户输入的验证码进行比较
        if (!code.equals(sessionCode)) {
            return Result.build(null, ResultCodeEnum.CODE_ERROR);
        }
        // 校验手机号码
        UserInfo userInfo = userInfoService.getByPhone(phone);
        // 判断userinfo是否为null
        if (userInfo != null) {
            return Result.build(null, ResultCodeEnum.PHONE_REGISTER_ERROR);
        }
        userInfo = new UserInfo();
        userInfo.setPhone(phone);
        userInfo.setPassword(MD5.encrypt(password));
        userInfo.setNickName(nickName);
        //状态（0：锁定 1：正常）
        userInfo.setStatus(1);
        userInfoService.insert(userInfo);
        return Result.ok();
    }


    @RequestMapping("/sendCode/{phone}")
    public Result sendCode(@PathVariable String phone, HttpServletRequest request) {
        // 生成验证码 4位
        int code = new Random().nextInt(9000);
        code = code + 1000;
        System.out.println("code------------" + code);
        request.getSession().setAttribute("CODE", code + "");
        // 发送短信验证码
//        try {
//            SMSUtils.sendShortMessage(phone,code+"");
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
        return Result.ok(code);
    }
}
