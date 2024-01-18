package cn.com.xuxiaowei.bili.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Map;

import static cn.com.xuxiaowei.bili.configuration.AuthorizationServerConfiguration.*;

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

                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                httpHeaders.setBasicAuth(CLIENT_ID, CLIENT_SECRET);
                MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
                requestBody.put("code", Collections.singletonList(code));
                requestBody.put("grant_type", Collections.singletonList("authorization_code"));
                requestBody.put("redirect_uri", Collections.singletonList(REDIRECT_URI));
                HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(requestBody, httpHeaders);

                try {
                    map = restTemplate.postForObject(TOKEN_URI, httpEntity, Map.class);
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
