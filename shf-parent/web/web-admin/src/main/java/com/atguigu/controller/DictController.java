package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Dict;
import com.atguigu.result.Result;
import com.atguigu.service.DictService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dict")
public class DictController {

    @Reference
    private DictService dictService;


    /**
     * 需求：需要查询数据库，找到数据，解析成json数据，塞到下拉列表
     * 根据父节点找到子节点
     * ① http://localhost:8000/dict/findListByParentId?password=123456
     * ② http://localhost:8000/dict/findListByParentId/123456
     * http://localhost:8000/dict/findListByParentId/1
     * <p>
     * ① 第一种传输方式，key-->value ，会把key和value暴露在外面
     * ② 第二种传输方式，使用的是斜线，看不出来value
     *
     * @PathVariable ： rest风格，提取斜线后面的参数
     * @RequestParam ：获取请求参数
     * ① 可以取别名，理论上来讲，前端传输过来的数据，必须跟后端的数据保持一致，不然获取不了数据
     * ② 用了这个注解，就必须要传参数
     * ③ 可以设置默认值
     */
    @RequestMapping("/findListByParentId/{parentId}")
    @ResponseBody
    public Result<List<Dict>> findListByParentId(@PathVariable Long parentId) {
        List<Dict> list = dictService.findListByParentId(parentId);
        return Result.ok(list);
    }


    /**
     * 根据省，找到市，根据市，找到区，在根据区，找到街道（板块）
     * http://localhost:8000/dict/findListByDictCode/beijing
     */
    @RequestMapping("/findListByDictCode/{dictCode}")
    @ResponseBody
    public Result<List<Dict>> findListByDictCode(@PathVariable String dictCode){
        List<Dict> list =  dictService.findListByDictCode(dictCode);
        return Result.ok(list);
    }


//    url:"/dict/findZnodes",

    /**
     * 数据库里面父节点的值，默认是0
     *
     * @param id
     * @return
     * @ResponseBody ：表示把数据转换成json数据
     */
    @RequestMapping("/findZnodes")
    @ResponseBody
    public Result findZnodes(@RequestParam(value = "id", defaultValue = "0") Long id) {
        // 传入0，查询数据库，返回所有的1
        List<Map<String, Object>> list = dictService.findZnodes(id);
        return Result.ok(list);
    }

    @RequestMapping
    public String index() {
        return "dict/index";
    }
}
