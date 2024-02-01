<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <title>Spring Authorization Server 设备授权服务 JDK 17 1.2.0</title>
    <link rel="shortcut icon" href="/images/icon-48x48.png">
</head>
<body>

<div>access_token：
    <textarea cols="200" rows="4" id="access_token">${access_token}</textarea>
    <hr>
</div>

<div>refresh_token：
    <textarea cols="200" rows="1" id="refresh_token">${refresh_token}</textarea>
    <hr>
</div>

<div>scope：
    <textarea cols="200" rows="1">${scope}</textarea>
    <hr>
</div>

<div>token_type：
    <textarea cols="200" rows="1">${token_type}</textarea>
    <hr>
</div>

<div>expires_in：
    <textarea cols="200" rows="1">${expires_in}</textarea>
    <hr>
</div>

<a href="/">首页</a>
<a href="/oauth2/authorize?client_id=client_id&redirect_uri=http://127.0.0.1:${serverPort}/code&response_type=code&scope=snsapi_base&state=beff3dfc-bad8-40db-b25f-e5459e3d6ad7">
    获取 OAuth 2.1 Token 链接
</a>

<hr>

<div>
    <button id="check-bearer-button" onclick="checkBearer()">测试 Token：Bearer</button>
    <div id="check-bearer-div"></div>
    <hr>
</div>

<div>
    <button id="check-url-param-button" onclick="checkUrlParam()">测试 Token：URL 参数</button>
    <div id="check-url-param-div"></div>
    <hr>
</div>

<div>
    <button id="refresh-button" onclick="refresh()">刷新 Token</button>
    <div id="refresh-div"></div>
    <hr>
</div>

<script type="application/javascript">
    function checkBearer() {

        // 创建 XMLHttpRequest 对象
        var xhr = new XMLHttpRequest();

        access_token = document.getElementById('access_token').value

        // 配置请求
        xhr.open('GET', '/token/check-bearer?token=' + access_token, true);

        // 设置回调函数，处理响应
        xhr.onreadystatechange = function () {
            console.log(xhr)
            if (xhr.readyState === 4 && xhr.status === 200) {
                // 请求成功，处理响应数据
                document.getElementById("check-bearer-div").innerText = xhr.responseText
            } else if (xhr.readyState === 4 && xhr.status !== 200) {
                // 请求失败，处理错误信息
                document.getElementById("check-bearer-div").innerText = '请求失败，HTTP 状态码：' + xhr.status + '，响应：' + xhr.responseText
            }
        };

        // 发送请求
        xhr.send();
    }

    function checkUrlParam() {

        // 创建 XMLHttpRequest 对象
        var xhr = new XMLHttpRequest();

        access_token = document.getElementById('access_token').value

        // 配置请求
        xhr.open('GET', '/token/check-url-param?token=' + access_token, true);

        // 设置回调函数，处理响应
        xhr.onreadystatechange = function () {
            console.log(xhr)
            if (xhr.readyState === 4 && xhr.status === 200) {
                // 请求成功，处理响应数据
                document.getElementById("check-url-param-div").innerText = xhr.responseText
            } else if (xhr.readyState === 4 && xhr.status !== 200) {
                // 请求失败，处理错误信息
                document.getElementById("check-url-param-div").innerText = '请求失败，HTTP 状态码：' + xhr.status + '，响应：' + xhr.responseText
            }
        };

        // 发送请求
        xhr.send();
    }

    function refresh() {

        // 创建 XMLHttpRequest 对象
        var xhr = new XMLHttpRequest();

        refresh_token = document.getElementById('refresh_token').value

        // 配置请求
        xhr.open('GET', '/token/refresh?refreshToken=' + refresh_token, true);

        // 设置回调函数，处理响应
        xhr.onreadystatechange = function () {
            console.log(xhr)
            if (xhr.readyState === 4 && xhr.status === 200) {
                // 请求成功，处理响应数据
                document.getElementById("refresh-div").innerText = xhr.responseText
            } else if (xhr.readyState === 4 && xhr.status !== 200) {
                // 请求失败，处理错误信息
                document.getElementById("refresh-div").innerText = '请求失败，HTTP 状态码：' + xhr.status + '，响应：' + xhr.responseText
            }
        };

        // 发送请求
        xhr.send();
    }
</script>

</body>
</html>