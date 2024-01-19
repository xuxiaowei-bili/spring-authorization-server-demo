# 默认授权服务 JDK 8 0.3.1

## 注意

1. 1.x 之前的版本，使用依赖 `org.springframework.security:spring-security-oauth2-authorization-server:0.3.1` 之前的版本（包含），
   授权服务器的配置使用 `org.springframework.security.oauth2.server.authorization.config.ProviderSettings`
2. 1.x 之前的版本，使用依赖 `org.springframework.security:spring-security-oauth2-authorization-server:0.3.1` 之后的版本（不包含），
   授权服务器的配置使用 `org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings`
   （改名了），源码提交记录为：https://github.com/spring-projects/spring-authorization-server/commit/c60ae4532f1d745bff6eb793113731aba0493b70
3. 1.x 之前的版本，使用依赖 `org.springframework.security:spring-security-oauth2-authorization-server:0.3.1` 之前的版本（包含），
   `ClientSettings` 的包名是 `org.springframework.security.oauth2.server.authorization.config`
4. 1.x 之前的版本，使用依赖 `org.springframework.security:spring-security-oauth2-authorization-server:0.3.1` 之后的版本（不包含），
   `ClientSettings` 的包名是 `org.springframework.security.oauth2.server.authorization.settings`，
   （改包名了），源码提交记录为：https://github.com/spring-projects/spring-authorization-server/commit/3877999a

## 查看 Spring Authorization Server 默认提供的端点

http://127.0.0.1:8005/.well-known/oauth-authorization-server

## 使用方式一

### 第一步：登录

1. 地址：http://127.0.0.1:8005/login
2. 用户名：user
3. 密码：password

### 第二步：点击链接获取 OAuth 2.1 Token

1. 获取 OAuth 2.1 Token 地址：
   http://127.0.0.1:8005/oauth2/authorize?client_id=client_id&redirect_uri=http://127.0.0.1:8005/code&response_type=code&scope=snsapi_base&state=beff3dfc-bad8-40db-b25f-e5459e3d6ad7

## 使用方式二

### 第一步：点击链接获取 OAuth 2.1 Token

1. 未登录的情况下会自动跳转到登录页面
2. 已登录的状态下会获取到 Token，与【使用方式一】相同

### 第二步：登录

1. 地址：http://127.0.0.1:8005/login
2. 用户名：user
3. 密码：password
4. 登录完成后，自动跳转到 上一次需要登录成功才能访问的链接，即：获取 OAuth 2.1 Token 的链接
