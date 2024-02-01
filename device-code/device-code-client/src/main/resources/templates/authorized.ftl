<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <title>Authorized</title>
</head>
<body>
<a href="/authorize">请求设备授权登录</a>
<div>
    <label for="payloadDecode">name</label>
    <textarea id="payloadDecode" disabled rows="3" style="width: 500px">${payloadDecode}</textarea>
</div>

<div>
    <label for="accessToken">accessToken</label>
    <textarea id="accessToken" disabled rows="3" style="width: 500px">${accessToken}</textarea>
</div>

<div>
    <label for="refreshToken">refreshToken</label>
    <textarea id="refreshToken" disabled rows="3" style="width: 500px">${refreshToken}</textarea>
</div>

<div>
    <label for="scope">scope</label>
    <textarea id="scope" disabled rows="3" style="width: 500px">${scope}</textarea>
</div>

<div>
    <label for="tokenType">tokenType</label>
    <textarea id="tokenType" disabled rows="3" style="width: 500px">${tokenType}</textarea>
</div>

<div>
    <label for="expiresIn">expiresIn</label>
    <textarea id="expiresIn" disabled rows="3" style="width: 500px">${expiresIn}</textarea>
</div>

</body>
</html>