<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/youkutaglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>新手指南</title>


    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${URL_LIB}/static/js/common/touchbox.min.js"></script>
    <script type="text/javascript" src="http://static.joyme.com/app/youku/js/youkubtn.js"></script>

    <!--手机style-->
    <link rel="stylesheet" media="screen and (max-width:768px)" href="${URL_LIB}/static/theme/youku/css/club.css">
    <!--平板style-->
    <link rel="stylesheet" media="screen and (min-width:768px)" href="${URL_LIB}/static/theme/youku/css/ipad_common.css">
    <link rel="stylesheet" media="screen and (min-width:768px)" href="${URL_LIB}/static/theme/youku/css/ipad_club.css">

    <link href="${URL_LIB}/static/theme/youku/css/wap_guidance.css" rel="stylesheet" type="text/css"/>

    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);
    </script>
</head>
<body>
<!--加载-->
<div class="laoding">
    <div class="box">
        <h1 class="bs"><span>0</span>%</h1>

        <div class="line">
            <span><cite><img src="${URL_LIB}/static/theme/youku/images/new/sj-icon.png" alt=""></cite></span>
            <span><cite><img src="${URL_LIB}/static/theme/youku/images/new/lb-icon.png" alt=""></cite></span>
            <span><cite><img src="${URL_LIB}/static/theme/youku/images/new/yx-icon.png" alt=""></cite></span>
            <span><cite><img src="${URL_LIB}/static/theme/youku/images/new/re-icon.png" alt=""></cite></span>
        </div>
    </div>
</div>
<!--内容-->
<div id="touchBoxCt">
    <div class="box1">
        <div class="pic-box pic-box1">
            <cite><img src="${URL_LIB}/static/theme/youku/images/new/pic1.png" alt=""></cite>
            <span>
                <b></b>
                <b></b>
                <b></b>
            </span>
        </div>
        <div class="font-box"><img src="${URL_LIB}/static/theme/youku/images/new/font1.png" alt=""></div>
        <div class="up-btn"></div>
    </div>
    <div class="box2">
        <div class="pic-box pic-box2">
            <cite><img src="${URL_LIB}/static/theme/youku/images/new/pic2.png" alt=""></cite>
            <span>
                <em></em>
                <em></em>
                <em></em>
                <em></em>
                <img src="${URL_LIB}/static/theme/youku/images/new/huang.png" alt="">
            </span>
        </div>
        <div class="font-box"><img src="${URL_LIB}/static/theme/youku/images/new/font2.png" alt=""></div>
        <div class="up-btn"></div>
    </div>
    <div class="box3">
        <div class="pic-box pic-box3">
            <cite><img src="${URL_LIB}/static/theme/youku/images/new/pic3.png" alt=""></cite>
            <span>
                <p class="djs">
                    <b class="minute"><em>0</em><em>0</em></b>
                    <b class="second"><em>0</em><em>0</em></b>
                    <b class="ms"><em>0</em><em>0</em></b>
                </p>
            </span>
        </div>
        <div class="font-box"><img src="${URL_LIB}/static/theme/youku/images/new/font3.png" alt=""></div>
        <div class="up-btn"></div>
    </div>
    <div class="box4">
        <div class="pic-box pic-box4">
            <cite><img src="${URL_LIB}/static/theme/youku/images/new/pic4.png" alt=""></cite>
            <span class="cheng">
                <cite class="box1">
                    <b></b>
                </cite>
                <cite class="box2">
                    <b></b>
                </cite>
            </span>
        </div>
        <div class="font-box"><img src="${URL_LIB}/static/theme/youku/images/new/font4.png" alt=""></div>
        <a href="/youku/webview/giftmarket/list" class="btn">加入俱乐部<span></span></a>
    </div>
</div>
<script type="text/javascript">

    function loading() {
        var boxer = new TouchBox('#touchBoxCt', {
            loop: false,
            animation: 'slide',
        });
        var nIndex = 0;
        var times = null;
        var timer = null;
        boxer.on('beforeslide', function (toIndex, activeIndex) {
            if (toIndex === 2) {
                timeOut($('.djs'), new Date().getTime() + 600000);//中国北京时间
            } else {
                clearInterval(times);
            }
            qiehuan(toIndex);
        })
        qiehuan(nIndex);
        function qiehuan(nIndex) {
            $('#touchBoxCt>div').eq(nIndex).addClass('animate').siblings().removeClass('animate');
            $('#touchBoxCt>div').removeClass('animates');
            clearTimeout(timer);
            timer = setTimeout(function () {
                $('#touchBoxCt>div').eq(nIndex).addClass('animates');
            }, 800)
            if (nIndex === 0) {

            }
        }

        ;
        //定时器
        function timeOut(parents, future) {
            var parents = parents;
            var minutebox = parents.find('.minute'),
                    secondbox = parents.find('.second'),
                    msbox = parents.find('.ms');
            var future = future;

            function zero(num) {
                if (num < 10) {
                    return '0' + num;
                } else if (num >= 10) {
                    return '' + num;
                }
            }

            ;
            times = setInterval(function () {
                checkTime();
            }, 10);
            function checkTime() {
                var nowTime = new Date().getTime();
                var seconds = (future - nowTime) / 1000;
                var minutes = Math.floor(seconds / 60);
                var f = zero(minutes % 60);
                var m = zero(Math.floor(seconds % 60));
                var s = zero(Math.floor(seconds * 100 % 96));
                getTimes(f, m, s);
                if (future <= nowTime) {
                    clearInterval(times);
                    setTimes();
                    return false;
                }
                ;
            }

            ;
            function getTimes(f, m, s) {
                minutebox.children('em').eq(0).text(f.substr(0, 1));
                minutebox.children('em').eq(1).text(f.substr(1, 1));
                secondbox.children('em').eq(0).text(m.substr(0, 1));
                secondbox.children('em').eq(1).text(m.substr(1, 1));
                msbox.children('em').eq(0).text(s.substr(0, 1));
                msbox.children('em').eq(1).text(s.substr(1, 1));
            }

            ;
            function setTimes() {
                parents.find('em').text(0);
            }

            ;
        }

        ;
    }
    ;
    function run() {
        var num = 5;
        var bs = $('.bs').find('span');
        var bst = bs.text();
        bs.text('').text(parseInt(bst) + 1);
        if (parseInt(bst) >= 30 && parseInt(bst) <= 57 || parseInt(bst) >= 64 && parseInt(bst) <= 87) {
            num = 15;
        } else if (parseInt(bst) >= 57 && parseInt(bst) <= 63) {
            num = 20;
        } else if (parseInt(bst) == 99) {
            clearTimeout(timeout);
            $('.laoding').hide();
            loading();
            return;
        }
        var timeout = setTimeout("run()", num);
    }
    window.onload = function () {
        run();
    }

</script>
<%@ include file="/views/jsp/youku_pwiki.jsp" %>
</body>
</html>
