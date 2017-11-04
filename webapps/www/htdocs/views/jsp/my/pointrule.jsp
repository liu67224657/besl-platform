<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ include file="/views/jsp/common/taglibs.jsp" %>

<head>
    <meta charset="utf-8">
    <meta content="width=device.width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <title>${wallname}规则</title>
    <link href="${URL_LIB}/static/theme/default/css/wap_common.css?${version}" rel="stylesheet" type="text/css">

    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);
    </script>
<body>
<div id="wrapper">
    <!-- <header id="header">
         <a href="#" class="return"></a>
         <h1>精选礼包</h1>
         <a href="#" class="close">关闭</a>
     </header> -->
    <div class="rule-text">
        <h1>一、什么是${wallname}（福利）商城？</h1>

        <p>${wallname}（福利）商城是着迷网为广大用户提供真情回馈的增值服务平台，每一名使用着迷移动应用的用户，只要打开应用那一刻，恭喜你，你已成为${wallname}（福利）商城尊享会员！
            这里，是着迷网这一权威手游媒体，为广大玩家谋取最大福利的会所！
            任何会员都可以用“${wallname}”兑换礼品，包括热门手游礼包、手游周边商品、电影票、Q币，手机充值卡等丰富的礼品，这些品种丰富，实惠好用的礼品都是免费向广大用户开放的。</p>
    </div>
    <div class="rule-text mt">
        <h1>二、怎样快速攒${wallname}？</h1>

        <p>着迷网每天为您更新N多好玩、好用的移动应用，用户只要随时进入【精品应用】，挑选并下载就可以轻松攒${wallname}，快速致富哦！</p>
    </div>
    <div class="rule-text mt">
        <h1>三、${wallname}（福利）商城里可以兑换什么？</h1>

        <p class="color">在${wallname}（福利）商城，用户可以用${wallname}进行以下两种形式的兑换，免费获得好礼</p>

        <p>
            一、${wallname}兑换。用户无需支付任何费用，就可用${wallname}超值兑换丰富的商品，从手机充值卡、Q币等、电影票到手伴、数码产品，以各种游戏周边、着迷福利、showgirl签名照等纪念品，各种惊喜，创造用户最大福利。不过，并不是所有奖品都无限供应哦，要想兑换到心仪的奖品，那就赶紧的努力攒金币吧!</p>

        <p>二、限时抢兑。着迷不定期发放给力的商品，用户参与限时抢兑，就能用${wallname}兑换获得。请注意，因限时抢的商品价值高，所以并不是无限供应哦，要想抢到超值好礼，请注意着迷移动应用的通知，第一时间进行兑换！</p>
    </div>
</div>
<script type="text/javascript">
    var _paq = _paq || [];
    _paq.push(['trackPageView']);
    _paq.push(['enableLinkTracking']);
    (function() {
        var u = "//stat.joyme.com/";
        _paq.push(['setTrackerUrl', u + 'piwik.php']);
        _paq.push(['setSiteId', 106]);
        var d = document, g = d.createElement('script'), s = d.getElementsByTagName('script')[0];
        g.type = 'text/javascript';
        g.async = true;
        g.defer = true;
        g.src = u + 'piwik.js';
        s.parentNode.insertBefore(g, s);
    })();
</script>
<noscript><p><img src="//stat.joyme.com/piwik.php?idsite=106" style="border:0;" alt=""/></p></noscript>
</body>
</html>