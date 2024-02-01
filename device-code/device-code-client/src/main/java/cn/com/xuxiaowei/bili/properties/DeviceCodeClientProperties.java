package cn.com.xuxiaowei.bili.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 客户配置
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Data
@Component
@ConfigurationProperties("device-code-client")
public class DeviceCodeClientProperties {

    private String clientId;

    private String clientSecret;

    private String scope;

    private String baseUrl;

}
