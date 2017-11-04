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
    <title>礼包详情</title>

    <link href="${URL_LIB}/static/theme/default/css/wap_common.css?${version}" rel="stylesheet" type="text/css">
    <%--<script type="text/javascript" src="${URL_LIB}/static/js/common/bdhm-noseajs.js"></script>--%>
    <%--<script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>--%>
    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>
    <c:if test="${empty sdkversion}">
        <script src='${URL_LIB}/static/js/page/WebViewJavascriptBridge.js?${version}'></script>
    </c:if>

    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);
        var browser = {
            versions: function () {
                var u = navigator.userAgent, app = navigator.appVersion;
                return {//移动终端浏览器版本信息
                    trident: u.indexOf('Trident') > -1, //IE内核
                    presto: u.indexOf('Presto') > -1, //opera内核
                    webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
                    gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
                    mobile: !!u.match(/AppleWebKit.*Mobile.*/), //是否为移动终端
                    ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
                    android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或uc浏览器
                    iPhone: u.indexOf('iPhone') > -1, //是否为iPhone或者QQHD浏览器
                    iPad: u.indexOf('iPad') > -1, //是否iPad
                    webApp: u.indexOf('Safari') == -1 //是否web应该程序，没有头部与底部
                };
            }(),
            language: (navigator.browserLanguage || navigator.language).toLowerCase()
        }
        <c:choose>
        <c:when test="${empty sdkversion}">
        if (browser.versions.ios) {
            var _jclient;
        }
        function copy(value) {
            var stringval = 'type=text&content=' + value;
            if (browser.versions.ios) {
                _jclient.callHandler('copy', stringval, function (response) {
                    if (response) {
                        $("#reserveSuccess").text("复制成功");
                        $("#reserveSuccess").addClass("close");
                        var t = setInterval(function () {
                            $("#reserveSuccess").removeClass("close");
                            clearTimeout(t);
                        }, 3000);
                    }
                })
            }
            if (browser.versions.android) {
                _jclient.copy(stringval);
            }
        }
        </c:when>
        <c:otherwise>
        function copy(value) {
            var stringval = 'type=text&content=' + value;
            if (browser.versions.android) {
                _jclient.copy(stringval);
            } else {
                _jclient.copy(stringval, function (response) {
                    if (response) {
                        $("#reserveSuccess").text("复制成功");
                        $("#reserveSuccess").addClass("close");
                        var t = setInterval(function () {
                            $("#reserveSuccess").removeClass("close");
                            clearTimeout(t);
                        }, 3000);
                    }
                })
            }
        }
        </c:otherwise>
        </c:choose>
    </script>
</head>
<body>
<div id="wrapper">
    <!-- <header id="header">
         <a href="#" class="return"></a>
         <h1>礼包详情</h1>
         <a href="#" class="close">关闭</a>
     </header> -->

    <div class="details">
        <%--<cite class="reserveing-icon"><img src="images/my/reserveing.png" alt=""></cite>--%>
        <div class="details-mark"></div>
        <p><img src="${dto.bgPic}" alt=""></p>

        <div class="details-box">
            <div class="details-tit fl"><img src="${dto.gipic}" alt=""></div>
            <div class="details-text fl"><h1>${dto.title}</h1>

                <h2>需${dto.point}${wallname}</h2>

                <h3>剩余<span>${dto.sn}/${dto.cn}</span></h3></div>
        </div>
    </div>
    <div class="details-message">
        <p class="fl"><span style="color:#f00;">*</span>&nbsp;兑换后请尽快使用，以免过期失效</p>

        <p class="fr">有效期:<span>${dto.endTime}</span></p>
    </div>

    <%--<div class="code">--%>
    <%--<c:forEach items="${goodsItem}" var="dto" varStatus="index">--%>
    <%--<div class="codebox share_code">--%>

    <%--<div onclick="copy('${dto.snValue1}');"><span>${dto.snValue1}</span></div>--%>


    <%--</div>--%>
    <%--</c:forEach>--%>
    <%--</div>--%>
    <%--<div class="done dn">--%>
    <%--<p style="text-align:center">兑换后请尽快使用，以免过期无效</p>--%>
    <%--</div>--%>
    <c:forEach items="${goodsItem}" var="dto">
        <div class="code">
            <div class="codebox">
                <div><span class="fl cut_out5" id="codevalue">${dto.snValue1}</span><a
                        href="javascript:copy('${dto.snValue1}');" class="copyBtn fr">复制</a></div>
            </div>

        </div>
    </c:forEach>
    <div class="done dn">
        <p style="text-align:center">兑换后请尽快使用，以免过期无效</p>
    </div>

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

    function changeBox() {
        $("#exchange").css("display", "none");
        $("#wrapper").css("display", "block");
    }
</script>
<noscript><p><img src="//stat.joyme.com/piwik.php?idsite=106" style="border:0;" alt=""/></p></noscript>

<input type="hidden" value="${profileid}" name="profileId"/>
<input type="hidden" value="${appkey}" name="appkey"/>
<input type="hidden" value="${activityDetailDTO.aid}" name="aid"/>
<input type="hidden" value="${activityDetailDTO.gid}" name="gid"/>
<input type="hidden" value="1" name="type"/>
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
