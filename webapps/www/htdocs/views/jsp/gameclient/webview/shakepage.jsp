<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<%@ include file="/views/jsp/common/jsconfig.jsp" %>
<html lang="en">
<meta charset="utf-8">
<meta content="width=device-width, initial-scale=1.0, user-scalable=no" name="viewport">
<meta content="yes" name="apple-mobile-web-app-capable">
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<meta content="telephone=no" name="format-detection">
<meta name="description" content="${gameDB.recommendReason}" />
<title>${gameDB.gameName}</title>
<link href="${URL_LIB}/static/theme/wap/css/yyy.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>
<script type="text/javascript">
    window.addEventListener('DOMContentLoaded', function (){
        document.addEventListener('touchstart', function (){return false}, true)
    }, true);
</script>
<body>
<div class="loading-box">
    <div class="loading"></div>
</div>
<div class="hand-result">
    <div class="hand-result-block">
        <div class="hand-result-line"></div>
        <div class="hand-result-box">
            <p class="hand-result-title">刀塔传奇</p>
            <div class="hand-result-cont huo">
                <a href="#">
                    <cite><img src="${URL_LIB}/static/theme/wap/images/title.png" alt=""></cite>
                    <div class="hand-result-text">
                        <p>卡牌</p>
                        <p><span></span><span></span><span></span></p>
                        <p>今日免费下载</p>
                    </div>
                    <b></b>
                </a>
            </div>
            <p class="hand-result-end">友情： 不满意？！再摇一次</p>
        </div>
    </div>
    <div class="hand-btn clearfix">
        <div class="hand-btn-jx now">
            <div>
                <cite></cite>
            </div>
            <span>继续摇</span>
        </div>
        <div class="hand-btn-fx">
            <div>
                <cite></cite>
            </div>
            <span>分享</span>
        </div>
    </div>
</div>
<div id="wrapper">
    <div class="close"></div>
    <header class="header">摇一摇</header>
    <section class="hand-box">
        <div class="hand-circle">
            <div class="hand-icon"></div>
        </div>
        <div class="mark"></div>
        <p>看看今天能找到什么游戏吧！</p>
        <div class="hand-btn clearfix">
            <div class="hand-btn-yx now">
                <div>
                    <cite></cite>
                </div>
                <span>游戏</span>
            </div>
            <div class="hand-btn-md dot">
                <div>
                    <cite></cite>
                </div>
                <span>天天迷豆</span>
            </div>
            <!-- <div class="hand-btn-qqb">
                <div>
                    <cite></cite>
                </div>
                <span>抢Q币</span>
            </div> -->
        </div>
    </section>
</div>
<script type="text/javascript">
    $('.hand-icon').addClass('handCartoon');
    var i = 0;
    if(window.DeviceMotionEvent) {
        var speed = 25;
        var x = y = z = lastX = lastY = lastZ = 0;
        var timer=null;
        window.addEventListener('devicemotion', function(){
            var acceleration =event.accelerationIncludingGravity;
            x = acceleration.x;
            y = acceleration.y;
            if(Math.abs(x-lastX) > speed || Math.abs(y-lastY) > speed) {
                $('.loading-box').show();
                $('.hand-icon').removeClass('handCartoon');
                timer=setTimeout(function(){
                    $('.loading-box').hide();
                    $('.hand-result').show();
                },1000)
                return false;
            }
            lastX = x;
            lastY = y;
        }, false);
    }
</script>
</body>
</html>
