package cn.com.xuxiaowei.bili.configuration;

import cn.com.xuxiaowei.bili.properties.AuthorizationServerProperties;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.web.NimbusJwkSetEndpointFilter;
import org.springframework.security.oauth2.server.authorization.web.authentication.*;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfiguration {

    private AuthorizationServerProperties authorizationServerProperties;

    @Autowired
    public void setAuthorizationServerProperties(AuthorizationServerProperties authorizationServerProperties) {
        this.authorizationServerProperties = authorizationServerProperties;
    }

    private int serverPort;

    @Value("${server.port}")
    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    /**
     * @see org.springframework.security.web.server.ui.LoginPageGeneratingWebFilter
     * @see org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter
     */
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {

        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        // 未从授权端点进行身份验证时重定向到登录页面
        http.exceptionHandling((exceptions) -> exceptions.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")));

        return http.build();
    }

    /**
     * @see NimbusJwkSetEndpointFilter
     * @see OAuth2AuthorizationServerConfigurer#configure(HttpSecurity)
     */
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http, KeyPair keyPair) throws Exception {

        // 表单登录处理从授权服务器过滤器链到登录页面的重定向
        http.authorizeHttpRequests((authorize) -> {
            authorize
                    //
                    .mvcMatchers("/favicon.ico").permitAll()
                    //
                    .mvcMatchers("/code").permitAll()
                    //
                    .mvcMatchers("/token/check-bearer").permitAll()
                    //
                    .mvcMatchers("/token/check-url-param").permitAll()
                    //
                    .mvcMatchers("/token/refresh").permitAll()
                    //
                    .anyRequest().authenticated();
        }).formLogin(Customizer.withDefaults());

        // 凭证验证
        http.oauth2ResourceServer().jwt(jwtCustomizer -> {
            // 方案1：
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            NimbusJwtDecoder.PublicKeyJwtDecoderBuilder publicKeyJwtDecoderBuilder = NimbusJwtDecoder.withPublicKey(publicKey);
            NimbusJwtDecoder nimbusJwtDecoder = publicKeyJwtDecoderBuilder.build();
            jwtCustomizer.decoder(nimbusJwtDecoder);

            // 方案2：
            // jwtCustomizer.jwkSetUri(String.format("http://127.0.0.1:%d/oauth2/jwks", serverPort));
        });

        return http.build();
    }

    /**
     * @see OAuth2ResourceServerConfigurer#configure(HttpSecurityBuilder)
     */
    @Bean
    public BearerTokenResolver bearerTokenResolver() {
        DefaultBearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();
        // 设置是否支持使用 URI 参数传输访问令牌。默认为 {@code false}。
        bearerTokenResolver.setAllowUriQueryParameter(true);
        return bearerTokenResolver;
    }

    @Bean
    @SuppressWarnings("deprecation")
    public UserDetailsService userDetailsService() {

        String username = authorizationServerProperties.getUsername();
        String password = authorizationServerProperties.getPassword();

        UserDetails userDetails = User
                //
                .withDefaultPasswordEncoder()
                //
                .username(username)
                //
                .password(password)
                //
                .roles("USER")
                //
                .build();
        return new InMemoryUserDetailsManager(userDetails);
    }

    /**
     * {@link ClientAuthenticationMethod#BASIC}：
     * {@link ClientAuthenticationMethod#CLIENT_SECRET_BASIC}：{@link ClientSecretBasicAuthenticationConverter#convert(HttpServletRequest)}
     * {@link ClientAuthenticationMethod#POST}：
     * {@link ClientAuthenticationMethod#CLIENT_SECRET_POST}：{@link ClientSecretPostAuthenticationConverter#convert(HttpServletRequest)}
     * {@link ClientAuthenticationMethod#CLIENT_SECRET_JWT}：
     * {@link ClientAuthenticationMethod#PRIVATE_KEY_JWT}：
     * {@link ClientAuthenticationMethod#NONE}：
     * {@link AuthorizationGrantType#AUTHORIZATION_CODE}：{@link OAuth2AuthorizationCodeAuthenticationConverter#convert(HttpServletRequest)}
     * {@link AuthorizationGrantType#REFRESH_TOKEN}：{@link OAuth2RefreshTokenAuthenticationConverter#convert(HttpServletRequest)}
     * {@link AuthorizationGrantType#CLIENT_CREDENTIALS}：{@link OAuth2ClientCredentialsAuthenticationConverter#convert(HttpServletRequest)}
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository() {

        String clientId = authorizationServerProperties.getClientId();
        String clientSecret = authorizationServerProperties.getClientSecret();
        String scope = authorizationServerProperties.getScope();

        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                // 客户ID
                .clientId(clientId)
                // 客户凭证：密码使用明文
                .clientSecret("{noop}" + clientSecret)
                // 客户凭证验证方式：
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                // 客户凭证验证方式：
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                // 授权类型：
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                // 授权类型：
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                // 授权类型：
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                // 授权类型：
                .authorizationGrantType(AuthorizationGrantType.IMPLICIT)
                // 授权成功后重定向地址
                .redirectUri(String.format("http://127.0.0.1:%d/code", serverPort))
                // 授权范围
                .scope(scope)
                //
                .clientSettings(ClientSettings.builder().build())
                //
                .build();

        return new InMemoryRegisteredClientRepository(registeredClient);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource(KeyPair keyPair) {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey).privateKey(privateKey).keyID(UUID.randomUUID().toString()).build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    @Bean
    public KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

}
