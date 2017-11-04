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
    <div class="cj2013-box cj2013-box-success">
        <img src="${userLotteryLog.lotteryAwardPic}"  width="80" height="100" class="img-1">
        <span class="text-1"><fmt:message key="lottery.award.level.${userLotteryLog.lotteryAwardLevel}" bundle="${userProps}"/></span>
		<span class="text-2">${userLotteryLog.lotteryAwardName}</span>
        <span class="text-3">(此页面是您的领奖凭证，<span style="color:#ff0000">请立即截图或保持本页开启。</span>)</span>
        <span class="text-4">请前往着迷展台领取，我们的展台位置在:W4馆，WM629着迷网展台<br/>欢迎你继续支持着迷！支持我们的游戏攻略WIKI！</span>
        <span class="text-6">领奖号:</span>
        <span class="text-7">${userLotteryLog.value1}</span>
    </div>
</div>
<div class="blank10"></div>


</body>
<script src="${URL_LIB}/static/js/common/bdhm-noseajs.js" type="text/javascript"></script>
<script src="${URL_LIB}/static/js/common/google-statistics-noseajs.js" type="text/javascript"></script>
</html>