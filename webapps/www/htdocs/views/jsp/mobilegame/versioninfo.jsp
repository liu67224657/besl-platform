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
    <title>${gameDB.gameName}_版本更新</title>
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
        <p class="t">版本更新</p>
        <img src="${URL_LIB}/static/theme/default/images/mobilegame/topbar.png">
    </div>

    <div class="corp-dis game-dis">
        ${gameDB.versionProfile}
    </div>

    <!-- footer -->
    <%@ include file="mobilegamefooter.jsp" %>

    <!-- 返回顶部 -->
    <div id="returntop" onclick="returnTop();">返回顶部</div>
    <script type="text/javascript" src="${URL_LIB}/static/js/page/touch-0.2.14.min.js"></script>
    <script type="text/javascript" src="${URL_LIB}/static/js/page/action.js"></script>
</body>
</html>