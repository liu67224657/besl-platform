<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
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

        <h2 class="editor-baike-title"> <a href="${URL_WWW}/game/${game.gameCode}" class="returnback">返回${game.resourceName}游戏页&gt;&gt;</a>${game.resourceName}--导航设置</h2>

        <div class="editor-baike-bar">
             <%--<a href="${URL_WWW}/game/${game.gameCode}/edit/headimagepage" >头图管理</a>--%>
            <%--<a href="${URL_WWW}/game/${game.gameCode}/edit/descpage">编辑游戏介绍</a>--%>
            <%--<a href="${URL_WWW}/game/${game.gameCode}/edit/recentpage" >编辑最近更新</a>--%>
            <a href="javascript:void(0);" class="current">导航设置</a>
            <%--<a href="${URL_WWW}/game/${game.gameCode}/edit/addmodulepage">添加新栏目</a>--%>
        </div>
        `

        <div class="clear"></div>

        <div class="editor-baike-box noborder clearfix">
            <div class="editor-baike-addNew" style="padding-bottom:10px">
                <a href="javascript:void(0);" id="add_menu">添加导航</a>
            </div>
            <div class="add-navigate">
                <table width="100%">
                    <tbody>
                    <tr>
                        <th width="165">栏目名称</th>
                        <th width="484">链接地址</th>
                        <th>操作</th>
                    </tr>
                    <c:forEach var="menu" items="${menuList}" varStatus="st">
                        <tr id="tr_menu_${st.index}">
                            <td>${menu.menuName}</td>
                            <td class="t-align-left">
                                <c:choose>
                                    <c:when test="${fn:length(menu.archPoint)>0}">栏目：${menu.archPointName}</c:when>
                                    <c:otherwise><a href="${menu.linkUrl}">${menu.linkUrl}</a></c:otherwise>
                                </c:choose>
                            </td>
                            <td class="optional">
                                <a href="javascript:void(0);" name="link_delmenu" data-mid="${menu.itemId}">删除</a>
                                <a href="javascript:void(0);" name="link_modifymenu" data-mid="${menu.itemId}">编辑</a>
                                <c:choose>
                                    <c:when test="${st.first}"><span>上移</span></c:when>
                                    <c:otherwise><a
                                            href="${URL_WWW}/game/${game.gameCode}/edit/sortmenu/up?menuid=${menu.itemId}">上移</a></c:otherwise>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${st.last}"><span>下移</span></c:when>
                                    <c:otherwise><a
                                            href="${URL_WWW}/game/${game.gameCode}/edit/sortmenu/down?menuid=${menu.itemId}">下移</a></c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
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
    seajs.use('${URL_LIB}/static/js/init/game-editmenu-init.js');
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</body>
</html>