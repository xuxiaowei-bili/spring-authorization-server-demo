# 默认授权服务 JDK 8 0.4.4

## 版本说明

1. 使用依赖：`org.springframework.security:spring-security-oauth2-authorization-server:0.4.4`
2. [版本差异](../README.md)

## 查看 Spring Authorization Server 默认提供的端点

http://127.0.0.1:8002/.well-known/oauth-authorization-server

## 使用方式一

### 第一步：登录

1. 地址：http://127.0.0.1:8002/login
2. 用户名：user
3. 密码：password

### 第二步：点击链接获取 OAuth 2.1 Token

1. 获取 OAuth 2.1 Token 地址：
   http://127.0.0.1:8002/oauth2/authorize?client_id=client_id&redirect_uri=http://127.0.0.1:8002/code&response_type=code&scope=snsapi_base&state=beff3dfc-bad8-40db-b25f-e5459e3d6ad7

## 使用方式二

### 第一步：点击链接获取 OAuth 2.1 Token

1. 未登录的情况下会自动跳转到登录页面
2. 已登录的状态下会获取到 Token，与【使用方式一】相同

### 第二步：登录

1. 地址：http://127.0.0.1:8002/login
2. 用户名：user
3. 密码：password
4. 登录完成后，自动跳转到 上一次需要登录成功才能访问的链接，即：获取 OAuth 2.1 Token 的链接
