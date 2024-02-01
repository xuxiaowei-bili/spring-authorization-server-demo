<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <title>Activate</title>
</head>
<body>

<form action="/oauth2/device_verification" method="post">
    <h2>设备激活</h2>
    <p>输入激活码以授权设备。</p>
    <div>
        <label for="user_code">激活码</label>
        <input type="text" id="user_code" name="user_code" placeholder="激活码" autofocus>
    </div>
    <button type="submit">授权</button>
</form>

</body>
</html>