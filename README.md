# Spring Authorization Server 示例

## 目录

### [默认授权服务 JDK 8](default-authorization-server-jdk8)

1. 1.x 之前的版本，从 `org.springframework.security:spring-security-oauth2-authorization-server:0.4.5` 开始，
   Token 接口的 `Content-Type` 只能使用 `application/x-www-form-urlencoded`，
   源码提交记录为 https://github.com/spring-projects/spring-authorization-server/commit/4bc0df5e ，
   议题为 https://github.com/spring-projects/spring-authorization-server/issues/1451

### [默认授权服务 JDK 8 0.4.4](default-authorization-server-jdk8-0.4.4)

1. 使用依赖 `org.springframework.security:spring-security-oauth2-authorization-server:0.4.4` 之前的版本（包含），
   Token 接口的 `Content-Type` 可使用 `application/json`

### [默认授权服务 JDK 8 0.3.1](default-authorization-server-jdk8-0.3.1)

1. 1.x 之前的版本，使用依赖 `org.springframework.security:spring-security-oauth2-authorization-server:0.3.1` 之前的版本（包含），
   授权服务器的配置使用 `org.springframework.security.oauth2.server.authorization.config.ProviderSettings`
2. 1.x 之前的版本，使用依赖 `org.springframework.security:spring-security-oauth2-authorization-server:0.3.1` 之后的版本（不包含），
   授权服务器的配置使用 `org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings`
   （改名了），源码提交记录为：https://github.com/spring-projects/spring-authorization-server/commit/c60ae4532f1d745bff6eb793113731aba0493b70
3. 1.x 之前的版本，使用依赖 `org.springframework.security:spring-security-oauth2-authorization-server:0.3.1` 之前的版本（包含），
   `ClientSettings` 的包名是 `org.springframework.security.oauth2.server.authorization.config`
4. 1.x 之前的版本，使用依赖 `org.springframework.security:spring-security-oauth2-authorization-server:0.3.1` 之后的版本（不包含），
   `ClientSettings` 的包名是 `org.springframework.security.oauth2.server.authorization.settings`

### [默认授权服务 JDK 17](default-authorization-server-jdk17)

1. 1.x 之后的版本，从 `org.springframework.security:spring-security-oauth2-authorization-server:1.2.1` 开始，
   Token 接口的 `Content-Type` 只能使用 `application/x-www-form-urlencoded`，
   源码提交记录为 https://github.com/spring-projects/spring-authorization-server/commit/4bc0df5e ，
   议题为 https://github.com/spring-projects/spring-authorization-server/issues/1451

### [默认授权服务 JDK 17 1.2.0](default-authorization-server-jdk17-1.2.0)

1. 使用依赖 `org.springframework.security:spring-security-oauth2-authorization-server:1.2.0`，
   Token 接口的 `Content-Type` 可使用 `application/json`
