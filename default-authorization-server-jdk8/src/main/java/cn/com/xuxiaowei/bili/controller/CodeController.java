package cn.com.xuxiaowei.bili.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

import static cn.com.xuxiaowei.bili.config.AuthorizationServerConfiguration.CLIENT_SECRET;

@Controller
public class CodeController {

    @RequestMapping(value = "/code")
    @SuppressWarnings("unchecked")
    public String code(HttpServletRequest request, HttpServletResponse response, String code, String state, Model model) {

        if (StringUtils.hasText(code)) {
            HttpSession session = request.getSession();
            Object token = session.getAttribute(code);

            Map<String, Object> map;
            if (token instanceof Map) {
                map = (Map<String, Object>) token;
            } else {

                String accessTokenUri = "http://127.0.0.1:8001/oauth2/token?grant_type=authorization_code&code={code}&redirect_uri={redirect_uri}";

                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                httpHeaders.setBasicAuth("client_id", CLIENT_SECRET);
                HttpEntity<HttpHeaders> httpEntity = new HttpEntity<>(httpHeaders);
                Map<String, String> param = new HashMap<>(8);
                param.put("code", code);
                param.put("redirect_uri", "http://127.0.0.1:8001/code");

                try {
                    map = restTemplate.postForObject(accessTokenUri, httpEntity, Map.class, param);
                    session.setAttribute(code, map);
                } catch (Exception e) {
                    e.printStackTrace();
                    session.setAttribute("msg", "code 无效，重复使用");
                    return "invalid-code";
                }
            }

            model.addAttribute("access_token", map.get("access_token"));
            model.addAttribute("refresh_token", map.get("refresh_token"));
            model.addAttribute("scope", map.get("scope"));
            model.addAttribute("token_type", map.get("token_type"));
            model.addAttribute("expires_in", map.get("expires_in"));
            return "code";
        } else {
            return "no-code";
        }
    }

}
