<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <meta content="width=device.width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>我的迷豆</title>

    <link href="${URL_LIB}/static/theme/default/css/wap_common.css?${version}" rel="stylesheet" type="text/css">
    <link href="${URL_LIB}/static/theme/default/css/wap_index.css?${version}" rel="stylesheet" type="text/css">
    <%--<script type="text/javascript" src="${URL_LIB}/static/js/common/bdhm-noseajs.js"></script>--%>
    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);
    </script>
</head>
<body>
<div id="wrapper">
    <header id="header-index">
        <div class="header-bg">
            <%--<a href="#" class="back"></a>--%>
            <div class="midou-wrap">
                <div class="midou">
                    <b>可用${wallname}</b>

                    <div class="midou-box">
                        <span id="midou">${point}</span>
                        <img src="${URL_LIB}/static/theme/default/images/my/midou.png" alt="">
                    </div>
                </div>
            </div>
            <p><img src="${URL_LIB}/static/theme/default/images/my/home-tit.jpg" alt=""></p>

        </div>
    </header>
    <div class="sign">
        <div class="sign-block yesterday">
            <div class="sign-block-t yd-top">昨天</div>
            <c:choose>
                <c:when test="${empty yesterday}">
                    <div class="sign-block-b">未签到</div>
                </c:when>
                <c:otherwise>
                    <div class="sign-block-b"> ${yesterday}${wallname}</div>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="today">
            <div class="sign-block-t now-top" id="sign">
                <c:choose>
                    <c:when test="${empty sign}">
                        签到
                    </c:when>
                    <c:otherwise>
                        已签到
                    </c:otherwise>
                </c:choose>
                <span class="pen"></span></div>
            <div class="sign-block-b">${today}${wallname}</div>
        </div>
        <div class="sign-block tomorrow">
            <div class="sign-block-t td-top">明天</div>
            <div class="sign-block-b">${tomorrow}${wallname}</div>
        </div>
        <p></p>
    </div>
    <div class="content">
        <ul>
            <%--<li>--%>
                <%--<a href="${URL_WWW}/my/totask?profileid=${profile.profileId}&appkey=${appkey}&clientid=${clientid}&platform=${platform}"><span--%>
                        <%--class="fl">今日任务</span><em class="fr"></em></a></li>--%>
            <%--<li>--%>
                <%--<a href="${URL_WWW}/my/giftlist?profileid=${profile.profileId}&appkey=${appkey}&type=1&clientid=${clientid}&platform=${platform}"><span--%>
                        <%--class="fl">热门礼包</span><em href="javascript:void(0);" class="fr"></em><c:if--%>
                        <%--test="${not empty giftname}"><b--%>
                        <%--class="fr">--%>
                    <%--<small class="cut_out">${giftname}</small>--%>
                    <%--<cite class="new fr">NEW</cite></b></c:if><em class="fr"></em></a></li>--%>
            <li>
                <a href="http://api.${DOMAIN}/my/giftlist?profileid=${profile.profileId}&appkey=${appkey}&type=2&clientid=${clientid}&platform=${platform}"><span
                        class="fl">${wallname}福利</span><em href="javascript:void(0);" class="fr"></em><c:if
                        test="${not empty coinname}"><b
                        class="fr">
                    <small class="cut_out">${coinname}</small>
                    <cite class="new fr">NEW</cite></b></c:if><em class="fr"></em></a></li>
            <li>
                <a href="http://api.${DOMAIN}/my/hotapp?appkey=${appkey}&uno=${uno}&clientid=${clientid}&platform=${platform}"><span
                        class="fl">精品应用</span><em href="javascript:void(0);" class="fr"></em></a></li>
        </ul>
    </div>
    <%--<div class="content">--%>
    <%--<ul>--%>
    <%--<li><a href="#"><span class="fl">今日任务</span><em class="fr"></em></a></li>--%>
    <%--<li><a href="#"><span class="fl">热门礼包</span><b class="fr">我叫MT光棍礼包<cite class="new fr">NEW</cite></b><em class="fr"></em></a></li>--%>
    <%--<li><a href="#"><span class="fl">迷豆福利</span><em href="#" class="fr"></em><b class="fr">移动手机充值卡大放送<cite class="new fr">NEW</cite></b></a></li>--%>
    <%--<li><a href="#"><span class="fl">精品应用</span><em href="#" class="fr"></em></a></li>--%>
    <%--</ul>--%>
    <%--</div>--%>
</div>
<div class="black-dialog " id="reserveSuccess"></div>
<script type="text/javascript">
    var _paq = _paq || [];
    _paq.push(['trackPageView']);
    _paq.push(['enableLinkTracking']);
    (function () {
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
<input type="hidden" value="${profile.profileId}" name="profileId"/>
<input type="hidden" value="${appkey}" name="appkey"/>
<input type="hidden" value="${point}" name="point"/>
<input type="hidden" value="${clientid}" name="clientid"/>
<input type="hidden" value="${platform}" name="platform"/>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/joymeappmy-init.js');
</script>
<script type="text/javascript">
    lz_main();
</script>

</body>
</html>
