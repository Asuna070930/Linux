package com.atguigu.base;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class BaseController {
    /**
     * 获取页面传递过来的所有参数
     * @param request
     * @return
     */
    public Map<String, Object> getFilters(HttpServletRequest request) {
//        String pageNum = request.getParameter("pageNum");
//        String pageSize = request.getParameter("pageSize");
        Enumeration parameterNames = request.getParameterNames();
        Map<String, Object> map = new HashMap<>();
        //判断页面是否传递过来参数
        while (parameterNames != null && parameterNames.hasMoreElements()) {
            //如果页面传递过来的参数补位null,并且有下一个参数,进入迭代
            //获取下一个元素
            String parameterName = (String) parameterNames.nextElement();
            //根据name获取value
            String[] values = request.getParameterValues(parameterName);
            //判断是否有值
            if (values != null && values.length != 0) {
                map.put(parameterName, values[0]);
            }
        }
        //玩意pageNum和pageSize 没有传值过来,给他们两个一个默认值
        if (!map.containsKey("pageNum")) {
            map.put("pageNum", 1);
        }
        if (!map.containsKey("pageSize")) {
            map.put("pageSize", 2);
        }
        return map;
    }
}
