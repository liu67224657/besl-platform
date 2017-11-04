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
    <meta name="Keywords" content="手机游戏礼包领取,手游兑换码,手游激活码">
    <meta name="description"
          content="着迷网礼包中心为手游玩家提供各种手游礼包,其中包括手机游戏激活码,特权码,测试礼包,新手礼包,兑换码，节假日礼包等,还有多种着迷专属礼包等你拿,多种手游礼包尽在着迷网"/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>手机游戏礼包领取,手游兑换码,手游激活码_着迷网</title>
    <link rel="stylesheet" type="text/css" href="http://static.joyme.com/mobile/cms/jmsy/css/common-beta1.css">
    <link rel="stylesheet" type="text/css"
          href="${URL_STATIC}/mobile/cms/jmsy/css/newlibao.css?v=${version}">
    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>

</head>
<body>
<div class="container">
    <%@ include file="/views/jsp/passport/m/gift-header-m.jsp" %>
    <div id="wrapper">
        <div id="main">
            <div id="center">
                <p class="expire-gift">
                    <fmt:message key="${message}" bundle="${userProps}"></fmt:message>
                </p>
            </div>
        </div>
        <script>
            document.write('<script src="http://passport.${DOMAIN}/auth/footer/m?v=' + Math.random() + '"><\/script>');
        </script>
    </div>

    <%@ include file="/views/jsp/passport/m/nav.jsp" %>
</div>
<script type="text/javascript" src="${URL_STATIC}/mobile/cms/jmsy/js/common.js"></script>

</body>
</html>
