package cn.com.xuxiaowei.bili.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class IndexController {

    @RequestMapping
    public String index(HttpServletRequest request, HttpServletResponse response, Model model) {

        int serverPort = request.getServerPort();
        model.addAttribute("serverPort", serverPort + "");

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String name = authentication.getName();
        model.addAttribute("name", name);

        return "index";
    }

}
