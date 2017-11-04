<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
    <title>着迷 cj2013</title>
    <link href="${URL_LIB}/static/theme/default/css/cj2013.css?${version}" rel="stylesheet" type="text/css">

    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
</head>

<body>
<div id="cj2013-topline"></div>
<div id="cj2013-content">
    <div id="cj2013-banner"></div>
    <div class="cj2013-box">
        <h1 class="cj2013-title">注册着迷拿大奖</h1>

        <p class="text-8" style="padding-top:20px;">点击以下任意一个按钮，用你的社交网络账号直接注册着迷，可100%获得着迷展台送出的精美礼品！</p>

        <div class="platform-login-btn">
            <p><a href="${URL_WWW}/huodong/chinajoy2013conference/sinaweibo/bind?lid=${lottery.lotteryId}" style="background:#56b6cf">新浪微博注册</a></p>

            <p><a href="${URL_WWW}/huodong/chinajoy2013conference/qq/bind?lid=${lottery.lotteryId}" style="background:#56b6cf">QQ号码注册</a></p>

            <%--<p><a href="${URL_WWW}/huodong/chinajoy2013conference/renren/bind?lid=${lottery.lotteryId}" style="background:#56b6cf">人人网注册</a></p>--%>
        </div>
    </div>
</div>
<div class="blank10"></div>
<script src="${URL_LIB}/static/js/common/bdhm-noseajs.js" type="text/javascript"></script>
<script src="${URL_LIB}/static/js/common/google-statistics-noseajs.js" type="text/javascript"></script>
</body>
</html>