package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.service.HelloService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/demo")
public class HelloController {

    /**
     * @Reference : 表示远程调用,两个服务器不在一个机房,不在一个局域网
     * @Autowired : 表示本地调用
     */
    //在服务消费者一方配置负载均衡策略
    @Reference
    private HelloService helloService;

    @RequestMapping("/hello")
    @ResponseBody
    public String getName(String name){

        //远程调用
        String result = helloService.sayHello(name);
        System.out.println(result);
        return result;
    }
}
