package cn.com.xuxiaowei.bili.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static cn.com.xuxiaowei.bili.configuration.AuthorizationServerConfiguration.*;

@RestController
@RequestMapping("/token")
public class TokenRestController {

    @RequestMapping(value = "/check-bearer", params = {"token"})
    @SuppressWarnings("unchecked")
    public Map<String, Object> checkBearer(HttpServletRequest request, HttpServletResponse response, String token) {
        Map<String, Object> map = new HashMap<>();

        RestTemplate restTemplate = new RestTemplate();

        // 设置请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(token);

        // 设置请求体
        Map<String, Object> requestBody = new HashMap<>();

        // 将请求体和请求头组装成 HttpEntity
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestBody, httpHeaders);

        try {
            map = restTemplate.postForObject("http://127.0.0.1:8002/info", httpEntity, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "Token Bearer 无效");
            map.put("time", System.currentTimeMillis());
        }

        return map;
    }

    @RequestMapping(value = "/check-url-param", params = {"token"})
    @SuppressWarnings("unchecked")
    public Map<String, Object> checkUrlParam(HttpServletRequest request, HttpServletResponse response, String token) {
        Map<String, Object> map = new HashMap<>();

        RestTemplate restTemplate = new RestTemplate();

        String accessToken = "http://127.0.0.1:8002/info?access_token=" + token;

        try {
            map = restTemplate.getForObject(accessToken, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "Token URL 参数 无效");
            map.put("time", System.currentTimeMillis());
        }

        return map;
    }

    @RequestMapping(value = "/refresh", params = {"refreshToken"})
    @SuppressWarnings("unchecked")
    public Map<String, Object> refresh(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        Map<String, Object> map = new HashMap<>();

        // 设置请求头
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBasicAuth(CLIENT_ID, CLIENT_SECRET);

        // 设置请求参数
        Map<String, String> param = new HashMap<>();
        param.put("grant_type", "refresh_token");
        param.put("refresh_token", refreshToken);

        // 将请求体和请求头组装成 HttpEntity
        HttpEntity<HttpHeaders> httpEntity = new HttpEntity<>(httpHeaders);

        try {
            map = restTemplate.postForObject(REFRESH_TOKEN_URI, httpEntity, Map.class, param);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "无法刷新 Token");
            map.put("time", System.currentTimeMillis());
        }

        return map;
    }

}
