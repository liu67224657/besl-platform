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
    <title><c:if test="${type eq '1'}">厂商信息</c:if><c:if test="${type eq '2'}">开发商信息</c:if></title>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/mobildgamestyle.css">
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);
    </script>
</head>
<body>

<!-- topbar -->
<div class="topbar imgbox">
    <a class="return-btn" href="javascript:history.go(-1)"></a>

    <p class="t"><c:if test="${type eq '1'}">${publishers}</c:if><c:if test="${type eq '2'}">${publishers}</c:if></p>
    <img src="${URL_LIB}/static/theme/default/images/mobilegame/topbar.png">
</div>

<%--<div class="corp-dis game-dis">--%>
<%--<h2>厂商简介</h2>--%>
<%--<p>保卫萝卜是一款由开发商“凯罗天下”开发的可爱风格塔防小游戏，容易上手、老少皆宜，内置新手引导。游戏含有丰富的关卡和主题包，拥有各自风格特色的多种防御塔，有趣的音效设定和搞怪的怪物造型及名字大大地增加了游戏的趣味性。</p>--%>
<%--</div>--%>

<div id="listbox" class="listbox2">
    <!-- 列表 -->
    <c:forEach items="${dto.gamedtolist}" var="dtov">


        <div class="list">
            <!-- 游戏 -->
            <a class="game-info"
               href="${URL_WWW}/mobilegame/comment/${dtov.gamedbid}?lineid=${dtov.lineid}&contentid=${dtov.contentid}">
                <img src="${dtov.gameicon}">

                <div>
                    <div class="h2">${dtov.gamename}</div>
                    <p>
                        <code>${dtov.replynum}评</code>
                        <span class="score-1"><i style="width:${dtov.average_score/10*100}%"></i></span>
                        <em>${dtov.average_score}</em>
                    </p>
                </div>
            </a>

            <c:forEach items="${dtov.shortcommentlist}" var="dtov2">
                <div class="user-dp">
                    <img src="${dtov2.header}">

                    <div>${dtov2.msg}</div>
                </div>
            </c:forEach>
            <!-- 用户的点评 -->
                <%--<div class="user-dp">--%>
                <%--<img src="${URL_LIB}/static/theme/default/images/mobilegame/user.jpg">--%>
                <%--<div>这游戏的任务很坑爹，冲刺一次不是只能用一个吗？还有道具全用过了那任务还是没满格〜现在分数663W 感觉已经瓶颈了。</div>--%>
                <%--</div>--%>
        </div>
    </c:forEach>
</div>

<!-- footer -->
<%@ include file="mobilegamefooter.jsp" %>

<!-- 返回顶部 -->
<div id="returntop" onclick="returnTop();">返回顶部</div>
<script type="text/javascript" src="${URL_LIB}/static/js/page/touch-0.2.14.min.js"></script>
<script type="text/javascript" src="${URL_LIB}/static/js/page/action.js"></script>
</body>
</html>