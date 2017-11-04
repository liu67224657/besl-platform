<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1x.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/jstllibs.jsp" %>
<%@ include file="/views/jsp/common/jsconfig.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <meta content="width=device.width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/wap/css/lottery/styles_wap.css">
    <title>下载着迷玩霸</title>
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);
    </script>
</head>
</head>
<body>
<div id="wrapper">
    <!--topbar-->
    <div id="topBar">
        <div class="ad_box">
            <span class="logo"><img src="${URL_LIB}/static/theme/wap/images/lottery/wap-logo.png" alt="玩霸" title="玩霸"/></span>

            <div class="tit">
                <em>着迷玩霸</em>
                <span>只要你着迷&nbsp;陪你玩到底</span>
            </div>
            <div class="dw-btn" id="dw-btn"><a id="downLoadUrl" class="icon-phone"
                                               href="http://www.joyme.com/appclick/sbd9em69" title="玩家称霸 要啥有啥">下载</a>
            </div>
        </div>
        <div class="popup_box">
            <img src="${URL_LIB}/static/theme/wap/images/lottery/popup.jpg" alt="">
        </div>
    </div>
    <!--topbar==end-->
    <!--content-->
    <div id="content">
        <div class="con_bg">
            <!-- <img class="backgroundImg" src="images/bg-1.jpg" alt="" title="" /> -->
            <img class="backgroundImg" src="${URL_LIB}/static/theme/wap/images/lottery/bg-2.jpg" alt="" title=""/>
            <img class="backgroundImg" src="${URL_LIB}/static/theme/wap/images/lottery/bg-3.jpg" alt="" title=""/>
            <img class="backgroundImg" src="${URL_LIB}/static/theme/wap/images/lottery/bg-4.jpg" alt="" title=""/>
            <img class="backgroundImg" src="${URL_LIB}/static/theme/wap/images/lottery/bg-5.jpg" alt="" title=""/>
            <img class="backgroundImg" src="${URL_LIB}/static/theme/wap/images/lottery/bg-6.jpg" alt="" title=""/>
            <img class="backgroundImg" src="${URL_LIB}/static/theme/wap/images/lottery/bg-7.jpg" alt="" title=""/>
            <img class="backgroundImg" src="${URL_LIB}/static/theme/wap/images/lottery/bg-foot.jpg" alt="" title=""/>
        </div>
    </div>
    <!--content==end-->
    <div id="footer">
        <div>&copy;2011－2015 joyme.com, all rights reserved</div>
        <div>京ICP备11029291号 京公网安备110108001706号</div>
    </div>
</div>
<div class="mark_box" id="mark_box"></div>
<div id="loadTips">
    <div class='loadTips txt-c'>
        <div class='load-bar1'></div>
        <div class='load-bar2'></div>
        <div class='load-bar1'></div>
    </div>
</div>
</body>
<script type="text/javascript">

    function is_weixn() {
        var ua = navigator.userAgent.toLowerCase();
        if (ua.match(/MicroMessenger/i) == "micromessenger") {
            return true;
        } else {
            return false;
        }
    }
    function clickLoadBtn() {
        var btn = document.getElementById('dw-btn');
        var topBar = document.getElementById('topBar');
        var mark = document.getElementById('mark_box');
        btn.addEventListener('touchstart', function (e) {
            if (is_weixn()) {
                topBar.className = 'popup_show';
                mark.style.display = 'block';
            } else {
                window.location.href = $("#downLoadUrl").attr("href");
            }
        }, false);
        mark.addEventListener('touchstart', function (e) {
            topBar.className = '';
            mark.style.display = 'none';
        })
    }
    clickLoadBtn();
    function loading() {
        var imgs = document.getElementsByClassName('backgroundImg');
        var len = imgs.length;
        var loading = document.getElementById('loadTips');
        var timer = null;
        for (var i = 0; i < len; i++) {
            timer = setInterval(function () {
                imgs[i].onload = function () {
                    loading.style.display = 'block';
                }
            }, 500)
            clearInterval(timer)
        }
    }

    //loading()
    <!-- Piwik -->
    var _paq = _paq || [];
    _paq.push(["setDocumentTitle", document.domain + "/" + document.title]);
    _paq.push(['trackPageView']);
    _paq.push(['enableLinkTracking']);
    (function() {
        var u="//stat.joyme.com/";
        _paq.push(['setTrackerUrl', u+'piwik.php']);
        _paq.push(['setSiteId', 11]);
        var d=document, g=d.createElement('script'), s=d.getElementsByTagName('script')[0];
        g.type='text/javascript'; g.async=true; g.defer=true; g.src=u+'piwik.js'; s.parentNode.insertBefore(g,s);
    })();
    <!-- End Piwik Code -->
</script>
</html>