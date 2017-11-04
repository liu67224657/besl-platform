<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <link href="http://static.joyme.com/app/wanba/20150915/css/style.css" rel="stylesheet"/>
    <title>玩霸金秋大答谢</title>

</head>
<body>
<div class="wrapper">
    <div class="cont">
        <img src="${URL_LIB}/static/theme/wap/images/wanba/zq2015/title1.jpg" alt="">

        <div class="active-cont">
            <div>
                <img src="${URL_LIB}/static/theme/wap/images/wanba/zq2015/active-cont.png" alt="">
            </div>
            <a href="http://www.joyme.com/appclick/spd5p5yj" class="load-btn"><img
                    src="${URL_LIB}/static/theme/wap/images/wanba/zq2015/si-btn.png" alt=""></a>
        </div>
        <p class="hint share"><span>PS：迷豆是一种神奇的东东，可以在玩霸里换奖品。<br/>（本次活动最终解释权归着迷网所有）</span></p>
    </div>
</div>
<div class="shadow-box">
    <cite><img src="${URL_LIB}/static/theme/wap/images/wanba/zq2015/popup.jpg" alt=""></cite>
</div>
<!--统计-->
<script type="text/javascript">
    var _paq = _paq || [];
    _paq.push(['trackPageView']);
    _paq.push(['enableLinkTracking']);
    (function() {
        var u = "//stat.joyme.com/";
        _paq.push(['setTrackerUrl', u + 'piwik.php']);
        _paq.push(['setSiteId', 900]);
        var d = document, g = d.createElement('script'), s = d.getElementsByTagName('script')[0];
        g.type = 'text/javascript';
        g.async = true;
        g.defer = true;
        g.src = u + 'piwik.js';
        s.parentNode.insertBefore(g, s);
    })();
</script>
</body>
<script src="http://static.joyme.com/js/jquery-1.9.1.min.js"></script>
<script>
    $(document).ready(function() {
        var setting = {
            int:function() {
                function is_weixn() {
                    var ua = navigator.userAgent.toLowerCase();
                    if (ua.match(/MicroMessenger/i) == "micromessenger") {
                        return true;
                    } else {
                        return false;
                    }
                }

                ;
                $('.load-btn').on('click', function() {
                    if (is_weixn()) {
                        $('body,html').scrollTop(0);
                        $('.shadow-box').show();
                        $('.shadow-box').on('touchmove', function(e) {
                            e.preventDefault();
                        })
                    } else {
                        window.location.href = "http://www.joyme.com/appclick/rli4niip";
                    }
                    ;
                });
                $('.shadow-box').on('click', function() {
                    $(this).hide();
                })
            }
        };
        setting.int();
    });
</script>
</html>
