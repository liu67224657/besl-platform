<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
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
    <title>下载-ios</title>
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

<div id="d-p-tab" class="d-p-tab">
    <span class="sel">iPhone版下载</span>
    <span>iPad版下载</span>
</div>

<!-- iphone下载链接 -->

<div id="iphone">
    <div class="d-p">Appstore下载</div>
    <c:choose>
        <c:when test="${not empty appStoreIphoneList}">

            <c:forEach items="${appStoreIphoneList}" var="al">
                <div class="d-p-l">
                    <a href="${al.downloadAddress}" class="appstore">${al.channelName}</a>
                </div>
            </c:forEach> <%--<a href="#" class="appstore">appstore下载</a>--%>
        </c:when>
        <c:otherwise>
            <div class="d-p-l">
                <a href="javascript:void(0);"> 暂无下载地址</a>
            </div>
        </c:otherwise>
    </c:choose>
    <div class="d-p">越狱版下载</div>
    <c:choose>
        <c:when test="${not empty iphoneList}">
            <div class="d-p-l">
                <c:forEach items="${iphoneList}" var="il">
                    <a href="${il.downloadAddress}">${il.channelName}</a>
                </c:forEach> <%--<a href="#" class="appstore">appstore下载</a>--%>
            </div>
        </c:when>
        <c:otherwise>
            <div class="d-p-l">
                <a href="javascript:void(0);"> 暂无下载地址</a>
            </div>
        </c:otherwise>
    </c:choose>
</div>
<!-- ipad下载链接 -->


<div id="ipad" style="display:none">
    <div class="d-p">Appstore下载</div>

    <c:choose>
        <c:when test="${not empty appStoreIpadList}">

            <div class="d-p-l">
                <c:forEach items="${appStoreIpadList}" var="cs">
                    <a href="${cs.downloadAddress}" class="appstore-ipad">${cs.channelName}</a>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <div class="d-p-l">
                <a href="javascript:void(0);"> 暂无下载地址</a>
            </div>
        </c:otherwise>
    </c:choose>
    <div class="d-p">越狱版下载</div>
    <c:choose>
        <c:when test="${not empty ipadList}">


            <div class="d-p-l">
                <c:forEach items="${ipadList}" var="cs">
                    <a href="${cs.downloadAddress}">${cs.channelName}</a>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <div class="d-p-l">
                <a href="javascript:void(0);"> 暂无下载地址</a>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<script type="text/javascript" src="${URL_LIB}/static/js/page/action.js"></script>
<script>
    d_p_tab(); // 切换选项卡
</script>
</body>
</html>