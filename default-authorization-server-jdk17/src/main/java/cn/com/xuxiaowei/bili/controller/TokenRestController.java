package cn.com.xuxiaowei.bili.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static cn.com.xuxiaowei.bili.configuration.AuthorizationServerConfiguration.CLIENT_ID;
import static cn.com.xuxiaowei.bili.configuration.AuthorizationServerConfiguration.CLIENT_SECRET;

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
            map = restTemplate.postForObject(String.format("http://127.0.0.1:%d/info", request.getServerPort()), httpEntity, Map.class);
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

        String accessToken = String.format("http://127.0.0.1:%d/info?access_token=%s", request.getServerPort(), token);

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
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.setBasicAuth(CLIENT_ID, CLIENT_SECRET);

        // 设置请求参数
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.put("grant_type", Collections.singletonList("refresh_token"));
        requestBody.put("refresh_token", Collections.singletonList(refreshToken));

        // 将请求体和请求头组装成 HttpEntity
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(requestBody, httpHeaders);

        try {
            map = restTemplate.postForObject(String.format("http://127.0.0.1:%d/oauth2/token", request.getServerPort()), httpEntity, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "无法刷新 Token");
            map.put("time", System.currentTimeMillis());
        }

        return map;
    }

}
