<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <title>游戏资料 ${jmh_title}</title>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/core.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/global.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/style.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/common.css?${version}"/>
</head>
<body>
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>
<div class="content set_content clearfix">
    <!--设置导航-->
    <%@ include file="leftmenu.jsp" %>
    <!--设置内容-->
    <div id="set_right">
        <div class="set_title">
            <h3>游戏资料</h3>
        </div>
        <!--设置-游戏资料-->
        <div class="set_domain_text">
            <p>填写你在所玩网络游戏中的角色信息，使朋友们更方便的了解你并找到你。</p>
        </div>
        <c:choose>
            <c:when test="${fn:length(playedGameList)>0}">
                <c:forEach var="game" items="${playedGameList}" varStatus="st">
                    <div class="addgameshowlist" id="getDate_${game.playedGameId}">
                        <div id="gameText_${game.playedGameId}">
                            <h3>${game.gameName}</h3><span><a href="javascript:void(0)" name="updateGame"
                                                              id="${game.playedGameId}" class="schcool_change">修改</a></span>
                            <ul>
                                <c:if test="${fn:length(game.gameRoleSet.gameRoles)>0}">
                                    <c:forEach var="role" items="${game.gameRoleSet.gameRoles}" varStatus="status">
                                        <li>
                                            <p>${role.serverArea}</p>

                                            <p class="pcenter">${role.roleName}</p>

                                            <p class="pcenter">${role.guild}</p>
                                        </li>
                                    </c:forEach>
                                </c:if>
                            </ul>
                        </div>
                    </div>
                </c:forEach>
                <div class="addgame" id="addGame"
                     <c:if test="${fn:length(mapMessage.playedGameList)>=50}">style="display:none"</c:if>><a
                        href="javascript:void(0)">+添加游戏中的角色信息</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="addgame" id="addGame"><a href="javascript:void(0)">+添加游戏中的角色信息</a>
                </div>
            </c:otherwise>
        </c:choose>
        <!--设置-游戏资料 end-->
    </div>
    <!--设置内容结束-->
</div>
<!--content结束-->
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/customize-playedgame-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</body>
</html>
