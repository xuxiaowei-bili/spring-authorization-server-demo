<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <title>Authorize</title>
    <script src="/jquery-3.5.1.min.js"></script>
    <script src="/jquery.qrcode.min.js"></script>
</head>
<body>

<h2>设备激活</h2>
<div>请在另一台设备上访问网址或扫描二维码：</div>
<div>
    <a href="http://127.0.0.1:8006/activate">http://127.0.0.1:8006/activate</a>
    <div id="code"></div>
</div>

<p>激活码</p>
<div>
    <span >${userCode}</span>
    <form action="/authorize" method="post">
        <input type="hidden" id="device_code" name="device_code" value="${deviceCode}"/>
    </form>
</div>

<script type="text/javascript">
    let verificationUriComplete = '${verificationUriComplete}';
    $("#code").qrcode({width: 150, height: 150, text: verificationUriComplete});

    function authorize() {
        let deviceCode = $('#device_code').val();
        if (deviceCode) {
            $.ajax({
                url: '/authorize',
                method: 'POST',
                data: {
                    device_code: deviceCode
                },
                timeout: 0
            }).fail((err) => {
                console.error("报错了", err)
            }).done((response) => {
                console.log(response)
                if (response.error === 'authorization_pending') {
                    console.log('授权待定，继续轮询...');
                } else if (response.error === 'slow_down') {
                    console.log('放慢速度...');
                    slowDown();
                } else if (response.error === 'token_expired') {
                    console.log('令牌已过期，正在停止...');
                    clear();
                    location.href = '/';
                } else if (response.error === 'access_denied') {
                    console.log('访问被拒绝，正在停止...');
                    clear();
                    location.href = '/';
                } else {
                    window.location.href = '/authorized'
                }
            });
        }
    }

    function schedule() {
        authorize.handler = window.setInterval(authorize, authorize.interval * 1000);
    }

    function clear() {
        if (authorize.handler !== null) {
            window.clearInterval(authorize.handler);
        }
    }

    function slowDown() {
        authorize.interval += 3;
        clear();
        schedule();
    }

    authorize.interval = 3;
    authorize.handler = null;

    window.addEventListener('load', schedule);

</script>

</body>
</html>