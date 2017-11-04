<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta name="applicable-device" content="mobile">
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta content="width=device-width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta name="Keywords" content="手机游戏礼包,激活码,兑换码">
    <meta name="description"
          content="着迷网礼包中心为手游玩家提供各种手游礼包,其中包括手机游戏激活码,特权码,测试礼包,新手礼包,兑换码等领取,还有多种手游的着迷专属礼包等你拿,多种手游礼包尽在着迷网手游礼包中心."/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>最新热门手机游戏礼包,激活码,兑换码_着迷网移动版</title>
    <link rel="stylesheet" type="text/css" href="http://static.joyme.com/mobile/cms/jmsy/css/common-beta1.css">
    <link rel="stylesheet" type="text/css"
          href="${URL_STATIC}/mobile/cms/jmsy/css/newlibao.css?v=${version}">

    <script type="text/javascript" src="${URL_STATIC}/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);
    </script>
</head>
<body>

<div class="container">
    <%@ include file="/views/jsp/passport/m/gift-header-m.jsp" %>

    <div id="wrapper">
        <div id="main">
            <div id="center">
                <div class="li-lin">
                    <h2>${gift.activitySubject}</h2>
                    <cite class="li-succer">
                        <span>淘号成功</span>
                    </cite>
                    <p>淘号库中的礼包是其他用户已领过的号码, 有可能已被使用，多试几个碰碰运气吧~</p>

                    <b>*请长按礼包号进行复制</b>
                    <c:if test="${itemList != null && itemList.size() > 0}">
                        <c:forEach items="${itemList}" var="item">
                            <div class="li-lin-block">
                                <span>${item.snValue1}</span>
                            </div>
                        </c:forEach>
                    </c:if>
                </div>
            </div>
            <script>
                document.write('<script src="http://passport.${DOMAIN}/auth/footer/m?v=' + Math.random() + '"><\/script>');
            </script>
        </div>
    </div>
    <%@ include file="/views/jsp/passport/m/nav.jsp" %>

</div>
<script type="text/javascript" src="${URL_STATIC}/mobile/cms/jmsy/js/common.js"></script>
<script type="text/javascript">
    var _paq = _paq || [];
    _paq.push(["setDocumentTitle", document.domain + "/" + document.title]);
    _paq.push(['trackPageView']);
    _paq.push(['enableLinkTracking']);
    (function () {
        var u = "//stat.joyme.com/";
        _paq.push(['setTrackerUrl', u + 'piwik.php']);
        _paq.push(['setSiteId', 198]);
        var d = document, g = d.createElement('script'), s = d.getElementsByTagName('script')[0];
        g.type = 'text/javascript';
        g.async = true;
        g.defer = true;
        g.src = u + 'piwik.js';
        s.parentNode.insertBefore(g, s);
    })();
    document.write('<noscript><p><img src="//stat.joyme.com/piwik.php?idsite=198" style="border:0;" alt="" /></p></noscript>');
</script>
<script>
    (function (G, D, s, c, p) {
        c = {//监测配置
            UA: "UA-joyme-000001", //客户项目编号,由系统生成
            NO_FLS: 0,
            WITH_REF: 1,
            URL: 'http://lib.joyme.com/static/js/iwt/iwt-min.js'
        };
        G._iwt ? G._iwt.track(c, p) : (G._iwtTQ = G._iwtTQ || []).push([c, p]), !G._iwtLoading && lo();
        function lo(t) {
            G._iwtLoading = 1;
            s = D.createElement("script");
            s.src = c.URL;
            t = D.getElementsByTagName("script");
            t = t[t.length - 1];
            t.parentNode.insertBefore(s, t);
        }
    })(this, document);
</script>
</body>
</html>
