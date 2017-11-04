<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:wml="http://www.wapforum.org/2001/wml">
<head>
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
    <meta name="Keywords" content="手机游戏礼包,手游礼包领取,手游礼包中心,兑换码,激活码,手机游戏礼包领取">
    <meta name="description" content="着迷网礼包中心为手游玩家提供各种手游礼包,其中包括手机游戏激活码,特权码,测试礼包,新手礼包,兑换码等领取,还有多种手游的着迷专属礼包等你拿,多种手游礼包尽在着迷网手游礼包中心."/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>登录</title>
    <link href="${URL_STATIC}/mobile/cms/jmsy/css/common.css?20160115" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${URL_STATIC}/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function (){
            document.addEventListener('touchstart', function (){return false}, true)
        }, true);
    </script>
    <script type="text/javascript">
        $(document).ready(function(){
            if(is_weixn()){
                $('.login-wb').remove();
                $('.login-qq').remove();
                $('.login-wx')
            }else{
                $('.login-wx').remove();
            }
        });

        function is_weixn() {
            var ua = navigator.userAgent.toLowerCase();
            if (ua.match(/MicroMessenger/i) == "micromessenger") {
                return true;
            } else {
                return false;
            }
        }
    </script>
</head>
<body>
<div class="login-box" style="display: block;">
    <div class="login-logo border-b">
        <cite></cite>
        <b></b>
        <b></b>
    </div>
    <div class="login-cont">
        <h2>请选择登录方式</h2>
        <p>
            <a href="http://passport.${DOMAIN}/auth/loginwappage?channel=wx&reurl=${reurl}" class="login-wx">
                <cite></cite>
                <span>微信</span>
            </a>
            <a href="http://passport.${DOMAIN}/auth/thirdapi/sinaweibo/bind?reurl=${reurl}" class="login-wb">
                <cite></cite>
                <span>新浪微博</span>
            </a>
            <a href="http://passport.${DOMAIN}/auth/thirdapi/qq/bind?reurl=${reurl}" class="login-qq">
                <cite></cite>
                <span>QQ</span>
            </a>
        </p>
    </div>
</div>
</body>
</html>
