package cn.com.xuxiaowei.bili.controller;

import cn.com.xuxiaowei.bili.properties.DeviceCodeClientProperties;
import cn.hutool.core.codec.Base64;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class AuthorizeController {

    private DeviceCodeClientProperties deviceCodeClientProperties;

    @Autowired
    public void setDeviceCodeClientProperties(DeviceCodeClientProperties deviceCodeClientProperties) {
        this.deviceCodeClientProperties = deviceCodeClientProperties;
    }

    @GetMapping("/authorize")
    public String authorize(Model model) {

        String baseUrl = deviceCodeClientProperties.getBaseUrl();
        String clientId = deviceCodeClientProperties.getClientId();
        String clientSecret = deviceCodeClientProperties.getClientSecret();
        String scope = deviceCodeClientProperties.getScope();

        String accessTokenUri = baseUrl + "/oauth2/device_authorization?client_id={client_id}&scope={scope}";

        Map<String, String> param = new HashMap<>(4);
        param.put("client_id", clientId);
        param.put("scope", scope);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBasicAuth(clientId, clientSecret);

        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);

        Map<String, Object> responseParameters = restTemplate.postForObject(accessTokenUri, httpEntity, Map.class, param);

        Instant issuedAt = Instant.now();
        Integer expiresIn = (Integer) responseParameters.get("expires_in");
        Instant expiresAt = issuedAt.plusSeconds(expiresIn);
        Object tokenValue = responseParameters.get("device_code");
        Object userCode = responseParameters.get("user_code");
        Object verificationUri = responseParameters.get("verification_uri");
        Object verificationUriComplete = responseParameters.get("verification_uri_complete");

        model.addAttribute("deviceCode", tokenValue);
        model.addAttribute("expiresAt", expiresAt);
        model.addAttribute("userCode", userCode);
        model.addAttribute("verificationUri", verificationUri);
        // 注意：您可以使用二维码显示此网址
        model.addAttribute("verificationUriComplete", verificationUriComplete);

        return "authorize";
    }

    @GetMapping("/authorized")
    public String authorized(HttpSession session, Model model) {

        Object accessToken = session.getAttribute("accessToken");
        Object refreshToken = session.getAttribute("refreshToken");
        Object scope = session.getAttribute("scope");
        Object tokenType = session.getAttribute("tokenType");
        Object expiresIn = session.getAttribute("expiresIn");

        Object payloadDecode = session.getAttribute("payloadDecode");

        model.addAttribute("accessToken", accessToken);
        model.addAttribute("refreshToken", refreshToken);
        model.addAttribute("scope", scope);
        model.addAttribute("tokenType", tokenType);
        model.addAttribute("expiresIn", expiresIn);

        model.addAttribute("payloadDecode", payloadDecode);

        return "authorized";
    }

    @SneakyThrows
    @ResponseBody
    @PostMapping("/authorize")
    public Map<String, Object> authorize(HttpSession session, @RequestParam("device_code") String deviceCode) {

        String baseUrl = deviceCodeClientProperties.getBaseUrl();
        String clientId = deviceCodeClientProperties.getClientId();
        String clientSecret = deviceCodeClientProperties.getClientSecret();

        String accessTokenUri = baseUrl + "/oauth2/token";

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.put("device_code", Collections.singletonList(deviceCode));
        requestBody.put("grant_type", Collections.singletonList("urn:ietf:params:oauth:grant-type:device_code"));
        requestBody.put("client_id", Collections.singletonList(clientId));

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.setBasicAuth(clientId, clientSecret);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(requestBody, httpHeaders);

        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> responseParameters = new HashMap<>();
        try {
            responseParameters = restTemplate.postForObject(accessTokenUri, httpEntity, Map.class);

            String accessToken = responseParameters.get("access_token").toString();
            Object refreshToken = responseParameters.get("refresh_token");
            Object scope = responseParameters.get("scope");
            Object tokenType = responseParameters.get("token_type");
            Object expiresIn = responseParameters.get("expires_in");

            String[] split = accessToken.split("\\.");
            String payload = split[1];

            String payloadDecode = Base64.decodeStr(payload);

            session.setAttribute("accessToken", accessToken);
            session.setAttribute("refreshToken", refreshToken);
            session.setAttribute("scope", scope);
            session.setAttribute("tokenType", tokenType);
            session.setAttribute("expiresIn", expiresIn);

            session.setAttribute("payloadDecode", payloadDecode);

        } catch (Exception e) {
            log.info("轮训授权结果：", e);
            if (e instanceof HttpClientErrorException.BadRequest badRequest) {
                String responseBodyAsString = badRequest.getResponseBodyAsString();

                responseParameters = objectMapper.readValue(responseBodyAsString, new TypeReference<Map<String, Object>>() {
                });
            }
        }

        // 响应数据可能出现的结果：
        // 授权待定，继续轮询...
        // map.put("errorCode", "authorization_pending");
        // 放慢速度...
        // map.put("errorCode", "slow_down");
        // 令牌已过期，正在停止...
        // map.put("errorCode", "token_expired");
        // 访问被拒绝，正在停止...
        // map.put("errorCode", "access_denied");

        return responseParameters;
    }

}
