<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<c:choose>
    <c:when test="${key!=null}">
        <c:set var="slt" value="${key}"/>
    </c:when>
    <c:otherwise>
        <c:set var="slt" value="无搜索内容!"/>
    </c:otherwise>
</c:choose>
<c:set var="holdstype" value="game"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <title>搜索${slt} ${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
</head>
<body>
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>
<div class="sqcontent clearfix">
    <div class="wsearcht"></div>
    <div class="wsearchcon clearfix">
        <div class="searchl">
<%@ include file="/views/jsp/search/hold-search.jsp"%>
<!-- 搜索小组 -->

            <c:choose>
                <c:when test="${fn:length(gameList)>0}">
                    <div class="result-report">“${key}”相关的游戏（${page.totalRows}）</div>
                    <div class="result-game <c:if test="${fn:length(gameList)==1}">result-game-one"</c:if>">
                        <ul>
                            <c:forEach var="game" items="${gameList}" varStatus="st">
                                <li <c:if test="${st.last}">class="noborder"</c:if>>
                                    <div class="li-div1">
                                        <c:choose>
                                            <c:when test="${game.logoSize=='3:3'}">
                                                <a class="game-type-app" title="${game.resourceName}" target="_blank" href="${URL_WWW}/game/${game.gameCode}">
                                                    <p>
                                                        <c:if test="${fn:length(game.icon.images)>0}">
                                                            <c:forEach items="${game.icon.images}" var="icon">
                                                                <img src="${uf:parseBFace(icon.ll)}" width="100" height="100"/><em></em>
                                                            </c:forEach>
                                                        </c:if>
                                                    </p>
                                                </a>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="${URL_WWW}/game/${game.gameCode}"target="_blank" >
                                                    <p>
                                                        <c:if test="${fn:length(game.icon.images)>0}">
                                                            <c:forEach items="${game.icon.images}" var="icon"><img
                                                                    src="${uf:parseBFace(icon.ll)}"
                                                                    width="100"/></c:forEach>
                                                        </c:if>
                                                    </p>
                                                </a>
                                                <span class="img-bottom-shadow"></span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <div class="li-div2">
                                        <h2><a href="${URL_WWW}/game/${game.gameCode}" target="_blank">${game.resourceName}</a></h2>

                                        <div>
                                            <c:forEach items="${game.deviceSet.deviceSet}" var="device" varStatus="st">${device.code}&nbsp;/&nbsp;</c:forEach>
                                            <c:forEach items="${game.categorySet.categorySet}" var="category" varStatus="st">${category.code}&nbsp;/&nbsp;</c:forEach>
                                            <c:if test="${game.resourceStatus.showDevelop() && fn:length(game.develop)>0}">${game.develop}&nbsp;/&nbsp;</c:if>
                                            ${game.publishDate}</div>
                                        <p>
                                        <c:choose><c:when test="${fn:length(game.resourceDesc)>130}">${fn:substring(game.resourceDesc,0,130)}…</c:when><c:otherwise>${game.resourceDesc}</c:otherwise></c:choose>
                                        </p>
                                        <p class="gotogame"><a href="${URL_WWW}/game/${game.gameCode}" target="_blank">进入游戏条目&gt;&gt;</a></p>
                                    </div>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                    <c:set var="pageurl" value="${URL_WWW}/search/game/${key}/"/>
                    <%@ include file="/views/jsp/page/page.jsp" %>
                </c:when>
                <c:otherwise>
                    <!-- 没有搜索结果(也是引用现成的) -->
                    <div class="noresult">
                        <div class="no1"><span class="tanh"></span>抱歉没有找到含有“<span><c:out value="${key}"/></span>”的游戏，<a href="http://www.joyme.com/note/2BIoVTYU176Wz7cHDjLmHI" target="_blank">创建游戏条目</a></div>
                        <p>• 请尝试用准确的关键词搜索，例如游戏名称等</p>
                        <p>• 用空格将多个关键词分开</p>
                        <p>• 看看输入的文字是否有误</p>
                    </div>
                </c:otherwise>
            </c:choose>
<!--wsearch-->
</div>
<!--search right-->
  <%@ include file="/views/jsp/search/search-game-right.jsp" %>

        <!--search right-->
    </div>
    <!--searchc-->
    <div class="wsearchb"></div>
</div>
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/search-board-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>

</body>
</html>