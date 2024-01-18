<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <title>Spring Authorization Server 默认授权服务 JDK 8 0.4.4</title>
    <link rel="shortcut icon" href="/images/icon-48x48.png">
</head>
<body>

用户名：${name}

<br>

<a href="/oauth2/authorize?client_id=client_id&redirect_uri=http://127.0.0.1:8002/code&response_type=code&scope=snsapi_base&state=beff3dfc-bad8-40db-b25f-e5459e3d6ad7">
    获取 OAuth 2.1 Token 链接
</a>

</body>
</html>