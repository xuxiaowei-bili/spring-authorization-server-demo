# 设备授权客户服务

## 使用方式

1. 启动 [device-code-authorization-server-jdk17-1.2.0](../device-code-authorization-server-jdk17-1.2.0)
2. 启动 [device-code-client](../device-code-client)
3. 访问 http://127.0.0.1:8007/authorize 申请授权
    1. 申请授权页面会出现 `授权二维码`
    2. 申请授权页面会出现 `激活码/授权码/用户码`
4. `使用另一个浏览器` 访问 http://127.0.0.1:8006
    1. 输入用户名：user
    2. 输入密码：password
    3. 登录
    4. 访问 http://127.0.0.1:8006/activate
    5. 输入 `激活码/授权码/用户码`
    6. 点击 `授权`
    7. 成功授权后，http://127.0.0.1:8007/authorize 会自动跳转到 http://127.0.0.1:8007/authorized ，并显示授权后的信息
