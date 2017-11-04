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
    <script src="${URL_LIB}/static/js/common/seajs.js"></script>
    <script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
    <script>
        seajs.use("${URL_LIB}/static/js/init/common-init.js")
    </script>
    <title>抽奖</title>
    <script type="text/javascript" src="http://lib.joyme.com/static/js/common/google-statistics-noseajs.js"></script>
</head>
<body>
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>
<div id="lottery-content">
    <div id="lottery-get-type1">
        <h1>没有获得实物奖品，<span>您获得了100积分</span>，可以兑换以下奖品</h1>

        <div id="recommend-wiki">
            <%@ include file="/hotdeploy/views/jsp/lottery/lottery-point.jsp" %>
        </div>
    </div>
</div>
<%@ include file="/views/jsp/tiles/footer.jsp" %>


<script type="text/javascript" src="${URL_LIB}/static/js/common/bdhm-noseajs.js"></script>
</body>
</html>