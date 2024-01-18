# 默认授权服务 JDK 17

## 注意

1. 1.x 之后的版本，从 `org.springframework.security:spring-security-oauth2-authorization-server:1.2.1` 开始，
   Token 接口的 `Content-Type` 只能使用 `application/x-www-form-urlencoded`，
   源码提交记录为 https://github.com/spring-projects/spring-authorization-server/commit/4bc0df5e ，
   议题为 https://github.com/spring-projects/spring-authorization-server/issues/1451

## 查看 Spring Authorization Server 默认提供的端点

http://127.0.0.1:8003/.well-known/oauth-authorization-server

## 使用方式一

### 第一步：登录

1. 地址：http://127.0.0.1:8003/login
2. 用户名：user
3. 密码：password

### 第二步：点击链接获取 OAuth 2.1 Token

1. 获取 OAuth 2.1 Token 地址：
   http://127.0.0.1:8003/oauth2/authorize?client_id=client_id&redirect_uri=http://127.0.0.1:8003/code&response_type=code&scope=snsapi_base&state=beff3dfc-bad8-40db-b25f-e5459e3d6ad7

## 使用方式二

### 第一步：点击链接获取 OAuth 2.1 Token

1. 未登录的情况下会自动跳转到登录页面
2. 已登录的状态下会获取到 Token，与【使用方式一】相同

### 第二步：登录

1. 地址：http://127.0.0.1:8003/login
2. 用户名：user
3. 密码：password
4. 登录完成后，自动跳转到 上一次需要登录成功才能访问的链接，即：获取 OAuth 2.1 Token 的链接
