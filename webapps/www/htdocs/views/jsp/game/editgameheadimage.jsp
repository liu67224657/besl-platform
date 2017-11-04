<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta name="KeyWords"
          content="${game.resourceName}、${game.resourceName}下载、${game.resourceName}攻略、${game.resourceName}论坛">
    <meta name="Description" content="${game.resourceName}着迷游戏专区、${game.resourceName}游戏下载、攻略秘籍、问答、小组"/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>${game.resourceName}游戏百科及下载、攻略_${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
</head>
<body>
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>
<div class="sqcontent">
	<div class="sqt"></div>
    <div class="sqc minheight-600">

        <h2 class="editor-baike-title">
            <a class="returnback" href="${URL_WWW}/game/${game.gameCode}">返回${game.resourceName}条目页&gt;&gt;</a>
            ${game.resourceName}--头图管理</h2>

        <div class="editor-baike-bar">
            <a href="javascript:void(0);" class="current">头图管理</a>
            <%--<a href="${URL_WWW}/game/${game.gameCode}/edit/descpage" >编辑游戏介绍</a>--%>
            <%--<a href="${URL_WWW}/game/${game.gameCode}/edit/recentpage" >编辑最近更新</a>--%>
            <%--<a href="${URL_WWW}/game/${game.gameCode}/edit/menupage" >导航设置</a>--%>
            <%--<a href="${URL_WWW}/game/${game.gameCode}/edit/addmodulepage" >添加新栏目</a>--%>
        </div>
        <div class="clear"></div>
        <div class="editor-baike-box noborder clearfix">
        <div class="item-manager">
             <div class="editor-baike-addNew" style="padding-bottom:10px">
                <a href="javascript:void(0);" id="add_image">添加头图</a>
            </div>
            <div class="inportantNews noborder clearfix">
                <ul id="news_list" class="sqlist">
                    <c:forEach var="image" items="${imageList}" varStatus="st">
                    <li>
                        <div class="changeItem">
                           <c:choose>
                               <c:when test="${st.first}"><a class="derection derection-top-disable" href="javascript:void(0)"></a></c:when>
                               <c:otherwise><a class="derection derection-top" href="${URL_WWW}/game/${game.gameCode}/edit/sortheadimage/up?imageid=${image.itemId}"></a></c:otherwise>
                           </c:choose>
                            <c:choose>
                               <c:when test="${st.last}"><a class="derection derection-bottom-disable" href="javascript:void(0)"></a></c:when>
                               <c:otherwise><a class="derection derection-bottom" href="${URL_WWW}/game/${game.gameCode}/edit/sortheadimage/down?imageid=${image.itemId}"></a></c:otherwise>
                           </c:choose>

                        </div>
                        <div class="sqpic">
                            <a target="_blank" href="<c:choose><c:when test="${fn:length(image.displayInfo.linkUrl)>0}">${image.displayInfo.linkUrl}</c:when><c:otherwise>javascript:void(0);</c:otherwise></c:choose>">
                                <img width="195" height="70" src="${uf:parseOrgImg(image.displayInfo.iconUrl)}"></a>
                        </div>
                        <div class="sq_area">
                            <p>头图名称：${image.displayInfo.subject}</p>
                            <br>

                            <p>连接地址：<c:if test="${fn:length(image.displayInfo.linkUrl)>0}"><a href="${image.displayInfo.linkUrl}">${image.displayInfo.linkUrl}</a></c:if></p>
                        </div>
                        <div class="sq_editor">
                            <a href="javascript:void(0);" class="editor-v-btn2" data-itemid="${image.itemId}" >删除</a><a href="javascript:void(0);" data-itemid="${image.itemId}" class="editor-v-btn1">编辑</a>
                        </div>
                    </li>
                    </c:forEach>
                </ul>
            </div>
        </div>
        </div>
    </div>
    <div class="sqb"></div>
</div>
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    var gameCode = '${gameCode}';
    seajs.use('${URL_LIB}/static/js/init/game-editimage-init.js');
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</body>
</html>