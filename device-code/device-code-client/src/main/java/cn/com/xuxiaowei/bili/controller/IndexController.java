package cn.com.xuxiaowei.bili.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author xuxiaowei
 */
@Controller
public class IndexController {

    @RequestMapping
    public String index() {
        return "index";
    }

}
