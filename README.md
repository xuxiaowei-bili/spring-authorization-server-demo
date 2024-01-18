# Spring Authorization Server 示例

## 目录

### [默认授权服务 JDK 8](default-authorization-server-jdk8)

1. 1.x 之前的版本，从 `org.springframework.security:spring-security-oauth2-authorization-server:0.4.5` 开始，
   Token 接口的 `Content-Type` 只能使用 `application/x-www-form-urlencoded`，
   源码提交记录为 https://github.com/spring-projects/spring-authorization-server/commit/4bc0df5e ，
   议题为 https://github.com/spring-projects/spring-authorization-server/issues/1451

### [默认授权服务 JDK 8 0.4.4](default-authorization-server-jdk8-0.4.4)

1. 使用依赖 `org.springframework.security:spring-security-oauth2-authorization-server:0.4.4`，
   Token 接口的 `Content-Type` 可使用 `application/json`
