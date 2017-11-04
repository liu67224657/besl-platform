<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <meta http-equiv="Cache-Control" content="no-store">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Expires" content="0">
    <meta name="Keywords" content="游戏攻略,游戏下载，游戏资料大全">
    <meta name="description" content="着迷网提供最新和热门游戏的游戏攻略及下载、游戏资料、任何人都有机会参与创建和完善着迷的游戏条目,将有用的内容提供给所有有共同兴趣的玩家.。">
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon">
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css">
    <link href="${URL_LIB}/static/theme/default/css/lottery.css?${version}" rel="stylesheet" type="text/css">
    <title>抽奖</title>
    <script type="text/javascript" src="http://lib.joyme.com/static/js/common/google-statistics-noseajs.js"></script>
</head>
<body>

<div id="lottery-banner">
    <div>
        <a href="/lottery/extaction1?lid=${lid}&thirdCode=qq" class="lottery-join-qq">QQ参与抽奖</a>
        <a href="/lottery/extaction1?lid=${lid}&thirdCode=sinaweibo" class="lottery-join-weibo">微博参与抽奖</a>
    </div>
</div>

<div id="lottery-content">
    <h2>还可能<span>抽到</span>…</h2>

    <div id="gift-show">
        <ul>
            <%@ include file="/hotdeploy/views/jsp/lottery/lottery-page.jsp" %>
        </ul>
    </div>
    <h2>他们<span>成功了!</span></h2>
    <iframe src="/lottery/extaction3?lid=${lid}" scrolling="no" width="100%" frameborder=”no” border=”0″></iframe>
    <h2>如果你在玩<span>手游</span>，以下这些对你可能会有用<a href="http://html.joyme.com/mobile/gameguides.html">找手游攻略，就交给我们专业的来！</a></h2>

    <div id="recommend-wiki">
        <ul>
            <%@ include file="/hotdeploy/views/jsp/lottery/lottery-guide.jsp" %>
        </ul>
        <div class="getMore"><a href="http://www.joyme.com/game">更多攻略大全&nbsp;&gt;&gt;</a></div>
    </div>
    <div class="clear" style="height:40px;"></div>
</div>
<%@ include file="/views/jsp/tiles/footer.jsp" %>

<script type="text/javascript" src="${URL_LIB}/static/js/common/bdhm-noseajs.js"></script>
</body>
</html>