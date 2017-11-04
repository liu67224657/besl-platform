<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:wml="http://www.wapforum.org/2001/wml">
<head>
    <meta name="applicable-device"content="mobile">
    <meta charset="utf-8">
    <meta content="width=device.width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <meta name="Keywords" content="手机游戏礼包,激活码,兑换码">
    <meta name="description" content="着迷网礼包中心为手游玩家提供各种手游礼包,其中包括手机游戏激活码,特权码,测试礼包,新手礼包,兑换码等领取,还有多种手游的着迷专属礼包等你拿,多种手游礼包尽在着迷网手游礼包中心."/>

    <title>最新热门手机游戏礼包,激活码,兑换码_着迷网移动版</title>
    <link href="${URL_LIB}/static/theme/default/css/wapstyle.css?${version}" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${URL_STATIC}/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);
        function onBridgeReady() {
            WeixinJSBridge.call('hideOptionMenu');
        }

        if (typeof WeixinJSBridge == "undefined") {
            if (document.addEventListener) {
                document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
            } else if (document.attachEvent) {
                document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
                document.attachEvent('sonWeixinJSBridgeReady', onBridgeReady);
            }
        } else {
            onBridgeReady();
        }

    </script>

</head>
<body>
<div class="bind_code">
    <p>绑定手机: <span>${phone}</span></p>

    <div><input type="text" name="verifycode" id="verifycode" placeholder="请输入验证码"></div>
    <p class="bind_btn"><a href="javascript:void(0);" id="bind">绑定</a></p>

    <p class="resend_code">30秒内没有收到验证码，请点击
        <input type="button" value="重新发送(${intravel})" id="send"/>
        <input type="hidden" disabled id="sendinput"/>
    </p>
</div>
<input type="hidden" value="${aid}" id="aid"/>
<input type="hidden" value="${phone}" id="phone"/>
<input type="hidden" value="${reurl}" id="reurl"/>
<script type="text/javascript">
    var interval;
    $(document).ready(function(){
        if ($('#sendinput').attr('disabled')) {
            interval = setInterval(calSendTime, 1000);
        }

        $("#bind").on('touchstart', function () {
            var phone = $("#phone").val();
            if(phone.length <= 0){
                alert("请输入手机号");
                return;
            }
            var verifycode = $("#verifycode").val();
            if(verifycode.length <= 0){
                alert("请输入验证码");
                return;
            }
            $.ajax({
                url: "http://api."+joyconfig.DOMAIN + "/json/gift/verifyphone",
                data: {phone: phone, verifycode: verifycode},
                timeout: 5000,
                dataType: "jsonp",
                type: "POST",
                jsonpCallback:"verifyphonecallback",
                success: function (req) {
                    var result = req[0];
                    if (result.rs == '1') {
                        var aid = $("#aid").val();
                        var reurl = $('#reurl').val();
                        if(reurl.length > 0){
                            window.location.href = reurl;
                        }else{
                            window.location.href = joyconfig.URL_M + "/gift/" + aid;
                        }
                    }
                    if (result.rs == '-10204') {
                        alert("请输入验证码");
                        return;
                    }
                    if (result.rs == '-10205') {
                        alert("请输入手机号");
                        return;
                    }
                    if (result.rs == '-10206') {
                        alert("验证码错误或验证码已过期，请重新发送");
                        return;
                    }
                    if (result.rs == '-10203') {
                        alert("该号码已被绑定");
                        return;
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    alert(XMLHttpRequest.status + ","+XMLHttpRequest.readyState+","+textStatus);
                    return;
                }
            });
        });
    });
    function calSendTime() {
        var prefix = '重新发送(';
        var suffix = ')'
        var sendTime = $('#send').val();
        sendTime = sendTime.replace(prefix, '');
        sendTime = sendTime.replace(suffix, '');
        sendTime = parseInt(sendTime);
        sendTime--;
        if (sendTime >= 0) {
            $('#send').val('重新发送(' + sendTime + ')');
        } else {
            var phone = $("#phone").val();
            var aid = $("#aid").val();
            $('#send').val('重新发送');
            $("#send").click(function () {
                $.ajax({ type: "POST",
                    url: joyconfig.URL_M + "/giftmarket/wap/jsonbindphone",
                    data: {phone: phone, aid: "1"},
                    success: function (req) {
                        var result = eval('(' + req + ')');
                        if (result.result.rs == "1") {
                            $('#send').val('重新发送(30)');
                            $('#send').unbind("click");
                            interval = setInterval(calSendTime, 1000);
                            return;
                        }
                        if (result.result.rs == "-2") {
                            alert("今日已发送五次短信");
                            return;
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        alert(XMLHttpRequest.status);
                        alert(XMLHttpRequest.readyState);
                        alert(textStatus);
                    }
                });
            });
            $('#sendinput').attr('disabled', false);
            clearInterval(interval);
        }
    }
</script>
<script type="text/javascript">
    var _paq = _paq || [];
    _paq.push(["setDocumentTitle", document.domain + "/" + document.title]);
    _paq.push(['trackPageView']);
    _paq.push(['enableLinkTracking']);
    (function() {
        var u="//stat.joyme.com/";
        _paq.push(['setTrackerUrl', u+'piwik.php']);
        _paq.push(['setSiteId', 198]);
        var d=document, g=d.createElement('script'), s=d.getElementsByTagName('script')[0];
        g.type='text/javascript'; g.async=true; g.defer=true; g.src=u+'piwik.js'; s.parentNode.insertBefore(g,s);
    })();
    document.write('<noscript><p><img src="//stat.joyme.com/piwik.php?idsite=198" style="border:0;" alt="" /></p></noscript>');
</script>
<script>
    (function (G,D,s,c,p) {
        c={//监测配置
            UA:"UA-joyme-000001", //客户项目编号,由系统生成
            NO_FLS:0,
            WITH_REF:1,
            URL:'http://lib.joyme.com/static/js/iwt/iwt-min.js'
        };
        G._iwt?G._iwt.track(c,p):(G._iwtTQ=G._iwtTQ || []).push([c,p]),!G._iwtLoading && lo();
        function lo(t) {
            G._iwtLoading=1;s=D.createElement("script");s.src=c.URL;
            t=D.getElementsByTagName("script");t=t[t.length-1];
            t.parentNode.insertBefore(s,t);
        }
    })(this,document);
</script>
</body>
</html>
