package cn.com.xuxiaowei.bili.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
public class InfoRestController {

    @RequestMapping("/info")
    public Map<String, Object> info(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();

        map.put("msg", "Token 有效");
        map.put("time", System.currentTimeMillis());

        return map;
    }

}
