package cn.com.xuxiaowei.bili.controller;

import cn.com.xuxiaowei.bili.properties.AuthorizationServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
public class CodeController {

    private AuthorizationServerProperties authorizationServerProperties;

    @Autowired
    public void setAuthorizationServerProperties(AuthorizationServerProperties authorizationServerProperties) {
        this.authorizationServerProperties = authorizationServerProperties;
    }

    @RequestMapping(value = "/code")
    @SuppressWarnings("unchecked")
    public String code(HttpServletRequest request, HttpServletResponse response, String code, String state, Model model) {

        String clientId = authorizationServerProperties.getClientId();
        String clientSecret = authorizationServerProperties.getClientSecret();

        int serverPort = request.getServerPort();
        model.addAttribute("serverPort", serverPort + "");

        if (StringUtils.hasText(code)) {
            HttpSession session = request.getSession();
            Object token = session.getAttribute(code);

            Map<String, Object> map;
            if (token instanceof Map) {
                map = (Map<String, Object>) token;
            } else {

                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                httpHeaders.setBasicAuth(clientId, clientSecret);
                Map<String, String> param = new HashMap<>();
                param.put("code", code);
                param.put("grant_type", "authorization_code");
                param.put("redirect_uri", String.format("http://127.0.0.1:%d/code", serverPort));
                HttpEntity<HttpHeaders> httpEntity = new HttpEntity<>(httpHeaders);

                String url = String.format("http://127.0.0.1:%d/oauth2/token?grant_type=authorization_code&code={code}&redirect_uri={redirect_uri}", serverPort);

                try {
                    map = restTemplate.postForObject(url, httpEntity, Map.class, param);
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
