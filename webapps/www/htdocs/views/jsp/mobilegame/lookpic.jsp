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
    <title>画面截图</title>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/mobildgamestyle.css">
    <script type="text/javascript">
    window.addEventListener('DOMContentLoaded', function () {
        document.addEventListener('touchstart', function () {return false}, true)
    }, true);
    </script>
</head>
<body>
    <!-- topbar -->
    <div class="topbar imgbox">
        <a class="return-btn" href="javascript:history.go(-1)"></a>
        <p class="t">画面截图</p>
        <img src="${URL_LIB}/static/theme/default/images/mobilegame/topbar.png">
    </div>

    <div class="d-game">
        <img src="${gameDB.gameIcon}">
        <div>
            <p>${gameDB.gameName}</p>
            <span class="score-1"><i style="width:${forignContent.average_score/10*100}%"></i></span><em>${forignContent.average_score}</em>
        </div>
        <a href="${URL_WWW}/mobilegame/download_info?gameid=${gameDB.gameDbId}" class="d">下载</a>
    </div>

    <!-- 图片轮播 -->
    <div id="pic-loop">
        <div id="pic-loop-box" style="-webkit-transform:translate3d(-100px, 0, 0)" class="swiper-wrapper">
                <c:if test="${not empty gamePicList}">
                    <c:forEach items="${gamePicList}" var="pic" varStatus="picindex">
                        <div class="swiper-slide"><img src="${pic}"></div>
                    </c:forEach>
                </c:if>
        </div>
    </div>

    <!-- footer -->
    <%@ include file="mobilegamefooter.jsp" %>

    <script src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${URL_LIB}/static/js/page/touch-0.2.14.min.js"></script>
    <script type="text/javascript" src="${URL_LIB}/static/js/page/action.js"></script>
    <script type="text/javascript" src="${URL_LIB}/static/js/page/swiper.js"></script>
    <script>
        var mySwiper = new Swiper('#pic-loop',{
            //pagination: '.pagination1',
            paginationClickable: true,
            mode: 'horizontal',  //水平
            cssWidthAndHeight: false
        })
    </script>
</body>
</html>