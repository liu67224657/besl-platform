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
    <title>今日任务</title>

    <link href="${URL_LIB}/static/theme/default/css/wap_common.css?${version}" rel="stylesheet" type="text/css">
    <link href="${URL_LIB}/static/theme/default/css/wap_index.css?${version}" rel="stylesheet" type="text/css">
    <%--<script type="text/javascript" src="${URL_LIB}/static/js/common/bdhm-noseajs.js"></script>--%>
    <%--<script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>--%>
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
    <!-- <header id="header">
         <a href="#" class="return"></a>
         <h1>精选礼包</h1>
         <a href="#" class="close">关闭</a>
     </header> -->
    <div class="background">
        <p><img src="${URL_LIB}/static/theme/default/images/my/bg-1.jpg" alt=""></p>

        <p><img src="${URL_LIB}/static/theme/default/images/my/bg-2.jpg" alt=""></p>

        <p><img src="${URL_LIB}/static/theme/default/images/my/bg-3.jpg" alt=""></p>

        <p><img src="${URL_LIB}/static/theme/default/images/my/bg-4.jpg" alt=""></p>

        <p><img src="${URL_LIB}/static/theme/default/images/my/bg-5.jpg" alt=""></p>

        <p><img src="${URL_LIB}/static/theme/default/images/my/bg-6.jpg" alt=""></p>

        <p><img src="${URL_LIB}/static/theme/default/images/my/bg-7.jpg" alt=""></p>

        <p><img src="${URL_LIB}/static/theme/default/images/my/bg-8.jpg" alt=""></p>

        <p><img src="${URL_LIB}/static/theme/default/images/my/bg-9.jpg" alt=""></p>

        <p><img src="${URL_LIB}/static/theme/default/images/my/bg-10.jpg" alt=""></p>

        <p><img src="${URL_LIB}/static/theme/default/images/my/bg-11.jpg" alt=""></p>

        <p><img src="${URL_LIB}/static/theme/default/images/my/bg-12.jpg" alt=""></p>
    </div>
    <div class="task-title">
        <h1>安装应用，轻松赚${wallname}</h1>

        <p>每安装一个应用即可获得对应的${wallname} </p>

        <p>只有首次安装时得${wallname}，不要重复安装哟！</p>
    </div>
    <span class="nail"><img src="${URL_LIB}/static/theme/default/images/my/dz.png" alt=""></span>

    <div class="muban">
        <a href="http://api.${DOMAIN}/my/hotapp"
           class="ban"><img src="${URL_LIB}/static/theme/default/images/my/muban.png" alt=""></a>
    </div>
    <div class="duihuan dh-position-a">
        <a href="javascript:void(0);"
           class="duihuan-t">兑换</a>
        <a href="javascript:void(0);"
           class="duihuan-b">热门礼包</a>
        <%--${URL_WWW}/my/giftlist?profileid=${profileid}&appkey=${appkey}&type=1&clientid=${clientid}&platform=${platform}--%>
    </div>
    <div class="duihuan dh-position-b">
        <a href="javascript:void(0);"
           class="duihuan-t">兑换</a>
        <a href="javascript:void(0);"
           class="duihuan-b">${wallname}福利</a>
        <%--${URL_WWW}/my/giftlist?profileid=${profileid}&appkey=${appkey}&type=2&clientid=${clientid}&platform=${platform}--%>
    </div>

</div>
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
<input type="hidden" value="${profileid}" name="profileId"/>
<input type="hidden" value="${appkey}" name="appkey"/>
<input type="hidden" value="${platform}" name="platform"/>
<input type="hidden" value="${clientid}" name="clientid"/>
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
