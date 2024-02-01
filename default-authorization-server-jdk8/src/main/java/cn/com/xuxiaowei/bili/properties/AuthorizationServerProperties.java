package cn.com.xuxiaowei.bili.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 授权服务配置
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Data
@Component
@ConfigurationProperties("authorization-server")
public class AuthorizationServerProperties {

    private String username;

    private String password;

    private String clientId;

    private String clientSecret;

    private String scope;

}
