package cn.com.xuxiaowei.bili.controller;

import cn.com.xuxiaowei.bili.properties.AuthorizationServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/token")
public class TokenRestController {

    private AuthorizationServerProperties authorizationServerProperties;

    @Autowired
    public void setAuthorizationServerProperties(AuthorizationServerProperties authorizationServerProperties) {
        this.authorizationServerProperties = authorizationServerProperties;
    }

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

        String clientId = authorizationServerProperties.getClientId();
        String clientSecret = authorizationServerProperties.getClientSecret();

        Map<String, Object> map = new HashMap<>();

        // 设置请求头
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBasicAuth(clientId, clientSecret);

        // 设置请求参数
        Map<String, String> param = new HashMap<>();
        param.put("grant_type", "refresh_token");
        param.put("refresh_token", refreshToken);

        // 将请求体和请求头组装成 HttpEntity
        HttpEntity<HttpHeaders> httpEntity = new HttpEntity<>(httpHeaders);

        String url = String.format("http://127.0.0.1:%d/oauth2/token?grant_type=refresh_token&refresh_token={refresh_token}", request.getServerPort());

        try {
            map = restTemplate.postForObject(url, httpEntity, Map.class, param);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "无法刷新 Token");
            map.put("time", System.currentTimeMillis());
        }

        return map;
    }

}
