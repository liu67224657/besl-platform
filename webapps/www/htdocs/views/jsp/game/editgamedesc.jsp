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
    <div class="sqc">

        <h2 class="editor-baike-title">
            <a class="returnback" href="${URL_WWW}/game/${game.gameCode}">返回${game.resourceName}条目页&gt;&gt;</a>
            ${game.resourceName}--编辑游戏介绍</h2>

        <div class="editor-baike-bar">
            <%--<a href="${URL_WWW}/game/${game.gameCode}/edit/headimagepage" >头图管理</a>--%>
            <a href="javascript:void(0);" class="current">编辑游戏介绍</a>
            <%--<a href="${URL_WWW}/game/${game.gameCode}/edit/recentpage" >编辑最近更新</a>--%>
            <%--<a href="${URL_WWW}/game/${game.gameCode}/edit/menupage" >导航设置</a>--%>
            <%--<a href="${URL_WWW}/game/${game.gameCode}/edit/addmodulepage" >添加新栏目</a>--%>
        </div>
        <div class="clear"></div>
        <div class="content-manager">
            <form action="${URL_WWW}/game/${game.gameCode}/edit/modifydesc" method="post" id="form_modifydesc">
                <textarea name="desc" id="txtarea_desc" style="height:300px;">${game.resourceDesc}</textarea>
                <div class="tipstext" id="desc_error" style="display:none;clear: both;"></div>
                <div class="editor-baike-btn"><input type="submit" class="save_btn" value=""/></div>
            </form>
        </div>
    </div>
    <div class="sqb"></div>
</div>
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    var gameCode = '${gameCode}';
    seajs.use('${URL_LIB}/static/js/init/game-editdesc-init.js');
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</body>
</html>