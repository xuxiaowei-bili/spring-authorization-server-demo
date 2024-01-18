package cn.com.xuxiaowei.bili.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
