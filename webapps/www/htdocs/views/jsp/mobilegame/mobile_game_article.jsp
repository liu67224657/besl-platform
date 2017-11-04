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
    <title>长评</title>
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
        <a class="return-btn" href="javascript:history.go(-1)">${gameName}</a>
        <a class="normal-btn" id="tg-btn">投稿</a>
        <img src="${URL_LIB}/static/theme/default/images/mobilegame/topbar.png">
    </div>
    <div style="height:15px;"></div>

    <!-- 长评列表 -->
    <div class="pl-long">
        <c:choose>
            <c:when test="${not empty mobileArticle}">
                <c:forEach items="${mobileArticle}" var="article">
                    <a href="${article.articleUrl}">
                        <span class="sp-1">${article.title}</span>
                        <span class="sp-2">${article.desc}</span>
                        <span class="sp-3">${article.authorName}&nbsp;&nbsp;${article.createTime}</span>
                    </a>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <span class="sp-1">暂时没有长评哦～</span>
            </c:otherwise>
        </c:choose>

    </div>

    <!-- footer -->
    <%@ include file="mobilegamefooter.jsp" %>


    <!-- 返回顶部 -->
    <div id="returntop" onclick="returnTop();">返回顶部</div>
    
    <!-- 投稿弹层  -->
    <div class="tc-layer" id="tg-layer">
        <div class="contribution">
            <div>欢迎投稿</div>
            <p>欢迎您将您的个性评论发送到：</p>
            <a href="mailto:zhenggao@staff.joyme.com">zhenggao@staff.joyme.com</a>
            <p>PS: 请将邮件标题写明为：</p>
            <p>游戏名称+征稿，并留下您的联系方式，</p>
            <p>以便我们能即时联系到您并进行致谢！</p>
            <span id="tg-close-btn">知道了</span>
        </div>
    </div>

    <script type="text/javascript" src="${URL_LIB}/static/js/page/touch-0.2.14.min.js"></script>
    <script type="text/javascript" src="${URL_LIB}/static/js/page/action.js"></script>
    <script>
        contribution() // 投稿
    </script>
</body>
</html>