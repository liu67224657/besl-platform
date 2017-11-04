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
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);

        $(document).ready(function () {
            $("#phone").focus();
            $("#phonesubmit").click(function () {
                var reg = /^[1][3-9][0-9]{9}$/;
                var phone = $("#phone").val().trim();
                if (phone == '' || !reg.test(phone)) {
                    alert("请填写正确的手机号");
                    return;
                }
                $.post("/json/giftmarket/wap/checkphoneisbind", {phone: phone, profilekey: 'www'}, function (data) {
                    if (data.msg == '-10') {
                        alert("该手机号已经被绑定过");
                        return;
                    } else {
                        var aid = $("#aid").val();
                        var reurl = $('#reurl').val();
                        window.location.href = "/gift/bindphone?aid=" + aid + "&phone=" + phone+"&reurl="+reurl;
                    }
                }, "json");
            });
        });
    </script>

</head>
<body>
<div class="bind_phone">
    <div><input type="text" placeholder="请输入手机号" id="phone"></div>
    <a href="javascript:;" id="phonesubmit">提交</a>
    <input type="hidden" value="${aid}" name="aid" id="aid"/>
    <input type="hidden" value="${reurl}" name="reurl" id="reurl"/>
</div>
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
