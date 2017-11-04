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
    <div class="sqc">
        <h2 class="editor-baike-title">
        <a class="returnback" href="${URL_WWW}/game/${game.gameCode}">返回${game.resourceName}条目页&gt;&gt;</a>
        ${layout.layoutName}--编辑栏目</h2>

        <div class="editor-baike-bar">
            <a href="${URL_WWW}/game/${game.gameCode}/edit/editmodulepage?mid=${moduleLine.lineId}" >栏目设置</a>
            <a href="javascript:void(0);" class="current">文章管理</a>
        </div>
        <div class="clear"></div>
        <div class="editor-baike-box clearfix">
            <div class="editor-baike-addNew">
                <a href="javascript:void(0);" id="add_content">添加新文章&gt;&gt;</a>
            </div>
            <div class="editor-baike-video clearfix">
                                <c:choose>
                    <c:when test="${fn:length(contentList)>0}">
                <ul class="clearfix">
                    <c:forEach var="content" items="${contentList}" varStatus="st">
                        <li <c:if test="${st.index>3}">class="fixpos"</c:if>>
                            <div>
                                <a href="${URL_WWW}/note/${content.elementId}" class="v-box">
                                    <p><img height="90" width="130" src="${uf:parseOrgImg(content.thumbimg)}"></p>
                                    <c:choose>
                                        <c:when test="${fn:length(content.title)>6}">${fn:substring(content.title, 0,6)}…</c:when>
                                        <c:otherwise>${content.title}</c:otherwise>
                                    </c:choose>
                                </a>
                                <c:choose>
                                    <c:when test="${!page.firstPage || !st.first}">
                                        <a href="${URL_WWW}/game/${game.gameCode}/edit/upcontent?cid=${content.elementId}&mid=${moduleLine.lineId}&p=${page.curPage}<c:if test="${st.first}">&isp=true</c:if>"
                                           class="derection derection-left"></a>
                                    </c:when>
                                    <c:otherwise>
                                         <a href="javascript:void(0)" class="derection derection-left-disable"></a>
                                    </c:otherwise>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${!page.lastPage || !st.last}">
                                        <a href="${URL_WWW}/game/${game.gameCode}/edit/downcontent?cid=${content.elementId}&mid=${moduleLine.lineId}&p=${page.curPage}<c:if test="${st.last}">&isp=true</c:if>"
                                           class="derection derection-right"></a>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="javascript:void(0)" class="derection derection-right-disable"></a>
                                    </c:otherwise>
                                </c:choose>
                                <a class="editor-btn-remove" href="javascript:void(0);" data-cid="${content.elementId}">删除</a>
                                <a class="editor-btn-edit" href="javascript:void(0);" data-cid="${content.elementId}" data-domain="${content.domain}" data-title="${content.title}">编辑</a>
                            </div>
                        </li>
                    </c:forEach>
                </ul>
                                    </c:when>
                    <c:otherwise>
                        <div class="minheight-600"></div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
                <c:if test="${page!=null && page.maxPage>1}">
            <div class="pagecon clearfix">
            <c:set var="pageparam" value="mid=${moduleLine.lineId}"/>
            <c:set var="pageurl" value="${URL_WWW}/game/${game.gameCode}/edit/contentpage"/>
            <%@include file="/views/jsp/page/goto.jsp" %>
            </div>
        </c:if>
    </div>
    <div class="sqb"></div>
</div>
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    var gameCode = '${gameCode}';
    var moduleId =${moduleLine.lineId};
    var curPage =${page.curPage};
    seajs.use('${URL_LIB}/static/js/init/game-editcontent-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</body>
</html>