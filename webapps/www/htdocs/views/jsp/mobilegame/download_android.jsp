<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<%@ taglib prefix="for" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta content="width=device.width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <meta name="Keywords" content="手机游戏排行榜,热门手机游戏,好玩的手机游戏" />
    <meta name="description" content="着迷手机游戏排行榜,众多玩家玩家一起推荐的手机游戏." />
    <title>下载</title>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/mobildgamestyle.css">
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);
    </script>
</head>
<body style="background:#fff;">
<div class="share-title">应用下载<span class="close-window-btn" ontouchstart="javascript:history.go(-1)"></span></div>

<div class="d-p">Android版下载</div>

<div class="d-p-l">
    <c:choose>
        <c:when test="${not empty androidList}">
            <c:forEach items="${androidList}" var="cs">
                       <a href="${cs.downloadAddress}">${cs.channelName}</a>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <a href="javascript:void(0);">暂无下载地址</a>
        </c:otherwise>
    </c:choose>

</div>
</body>
</html>