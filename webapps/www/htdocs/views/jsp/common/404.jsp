<%@ page import="com.enjoyf.platform.util.http.AppUtil" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<%
    if (!AppUtil.checkIsAndroid(request) && !AppUtil.checkIsIOS(request)) {
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta name="applicable-device" content="pc"/>
    <meta name="mobile-agent" content="format=xhtml;url=http://m.joyme.com/"/>
    <meta name="mobile-agent" content="format=html5;url=http://m.joyme.com"/>
    <script type="text/javascript">
        var _speedMark = new Date();

    </script>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta name="shenma-site-verification" content="362ee286130d388f82f998599b8c30dc_1443432989"/>
    <title>着迷网_中国领先的泛娱乐门户网站_电脑游戏_电视游戏_手机游戏</title>
    <meta name="Keywords" content="着迷网,泛娱乐,电脑游戏,电视游戏,手机游戏"/>
    <meta name="description" content="着迷网是中国领先的泛娱乐门户网站，为玩家提供电脑游戏、电视游戏、手机游戏等娱乐领域深度服务，及WIKI开放型中文维基百科。只有着迷，才有乐趣！"/>
    <meta name="360-site-verification" content="1de1c482f5c029917d9e65306dcb78d4"/>
    <meta name="sogou_site_verification" content="VEFkoCjIwA"/>
    <meta property="qc:admins" content="21404323606271556375"/>
    <link href="http://static.joyme.com/pc/cms/jmsy/css/header-index_201607.css?${version}" rel="stylesheet"
          type="text/css"/>
    <link href="http://static.joyme.com/pc/cms/jmsy/css/index_201607.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="http://static.joyme.com/pc/cms/jmsy/css/index.css?${version}" rel="stylesheet" type="text/css"/>
    <script type="text/javascript">
        var require = {
            urlArgs: '20160719000'
        }
    </script>
    <script src="http://static.joyme.com/js/jquery-1.9.1.min.js" language="javascript"></script>
    <script>
        var bs = {
            versions: function () {
                var u = navigator.userAgent, app = navigator.appVersion;
                return {//移动终端浏览器版本信息
                    trident: u.indexOf('Trident') > -1, //IE内核
                    presto: u.indexOf('Presto') > -1, //opera内核
                    webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
                    gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
                    mobile: !!u.match(/AppleWebKit.*Mobile.*/) || !!u.match(/AppleWebKit/), //是否为移动终端
                    ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
                    android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或者uc浏览器
                    iPhone: u.indexOf('iPhone') > -1, //是否为iPhone或者QQHD浏览器
                    WPhone: u.indexOf('Windows Phone') > -1,//windows phone
                    iPad: u.indexOf('iPad') > -1, //是否iPad
                    webApp: u.indexOf('Safari') == -1 //是否web应该程序，没有头部与底部
                };
            }(),
            language: (navigator.browserLanguage || navigator.language).toLowerCase()
        }

        var jumpUrl = 'http://m.joyme.com/index.html';
        var domain = window.location.hostname;
        var env = domain.substring(domain.lastIndexOf('.'), domain.length);

        if (bs.versions.mobile) {
            if (bs.versions.android || bs.versions.iPhone || bs.versions.iPad || bs.versions.ios || bs.versions.WPhone) {
                if (window.location.hash.indexOf('wappc') <= 0) {
                    var flag = getCookie('jumpflag');
                    if (flag == '' || flag == null || flag == undefined) {
                        window.location.href = jumpUrl;
                    }
                } else {
                    var timeOutDate = new Date();
                    timeOutDate.setTime(timeOutDate.getTime() + (24 * 60 * 60 * 1000));
                    setCookie('jumpflag', 'wappc', timeOutDate, env);
                }
            }
        }

        function getCookie(objName) {
            var arrStr = document.cookie.split("; ");
            for (var i = 0; i < arrStr.length; i++) {
                var temp = arrStr[i].split("=");
                if (temp[0] == objName && temp[1] != '\'\'' && temp[1] != "\"\"") {
                    return unescape(temp[1]);
                }
            }
            return null;
        }

        function setCookie(key, value, exDate, env) {
            var cookie = "";
            if (!!key)
                cookie += key + "=" + escape(value) + ";path=/;domain=.joyme" + env + ";expires=" + exDate.toUTCString();
            document.cookie = cookie;
        }

    </script>
</head>

<body>
<div id="joyme-wrapper">
    <!--头部开始-->
    <c:import url="/views/jsp/passport/new-header.jsp"/>
    <div id="cont-404">
        <cite><img src="http://static.joyme.com/pc/cms/jmsy/images/404.png" alt=""/></cite>
        <a id="back-btn" href="http://www.joyme.com" target="_blank">前往首页 &gt;</a>
    </div>
    <%@include file="/views/jsp/tiles/new-footer.jsp" %>
</div>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<!--页尾结束-->
</body>
<script>
    $(function () {
        /*5秒后跳转*/
        (function () {
            var num = 5;
            var showtime = $("<p>您访问的页面不存在，页面将在<em>" + num + "</em>s之后跳转至<a id='jm-back' href='http://www.joyme.com'>首页</a><br />如果没有跳转，请点击下面的链接</p>");
            var showtimeNum = showtime.find("em");
            showtime.insertBefore("#back-btn");
            num -= 1;
            var countDown = setInterval(function () {
                if (num) {
                    showtimeNum.html(num);
                    num -= 1;
                } else {
                    countDown && clearInterval(countDown);
                    document.location.href = document.getElementById("back-btn").getAttribute("href");
                }
            }, 1000);
        })();
    });
</script>


</html>
<% } else {%>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <title>404</title>
    <link href="${URL_LIB}/static/theme/wap/css/errorpage.css" rel="stylesheet" type="text/css">
<body>
<div id="wrapper">
    <div class="no-net">
        <b><img src="${URL_LIB}/static/theme/wap/images/no-net.jpg" alt=""></b>

        <p>页面加载失败</p>

        <p>你访问的页面走丢了</p>
    </div>
</div>
<script>
    function getMoreInfo() {
        return {"show": "no"};
    }

   window.setTimeout(function(){
       window.location.href="http://m.joyme.com";
   },3000);
</script>
</body>
</html>
<%}%>
