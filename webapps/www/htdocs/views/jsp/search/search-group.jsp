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
<c:set var="holdstype" value="group"/>
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
                <c:when test="${fn:length(groupList)>0}">
                    <div class="result-report">“${key}”相关的小组（${page.totalRows}）</div>
                    <div class="result-group">
                        <ul>
                             <c:forEach var="group" items="${groupList}" varStatus="st">
                            <li <c:if test='${st.last}'>class="noborder"</c:if>>
                                <div class="li-div1">
                                    <a target="_blank" href="${URL_WWW}/group/${group.gameResource.gameCode}">
                                        <c:if test="${fn:length(group.gameResource.icon.images)>0}">
                                            <c:forEach items="${group.gameResource.icon.images}" var="icon"><img src="${uf:parseBFace(icon.ll)}" width="55"/></c:forEach>
                                        </c:if>
                                    </a>
                                </div>
                                <div class="li-div2">
                                    <h2> <a href="${URL_WWW}/group/${group.gameResource.gameCode}" target="_blank">${group.gameResource.resourceName}</a></h2>
                                    <p>${group.totalNum}帖子&nbsp;&nbsp;&nbsp;&nbsp;今日：${group.todayNum}</p>
                                    <span><c:choose><c:when test="${fn:length(group.gameResource.resourceDesc)>130}">${fn:substring(group.gameResource.resourceDesc,0,80)}…</c:when><c:otherwise>${group.gameResource.resourceDesc}</c:otherwise></c:choose></span>
                                </div>
                                <div class="li-div3">
                                    <c:choose>
                                        <c:when test="${group.socialRelation!=null && group.socialRelation.srcStatus.code=='y'}">
                                            <a href="javascript:void(0);" data-gid="${group.gameResource.resourceId}" name="unfollowGroup" class="joinbtn"><span>已加入</span></a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="javascript:void(0);" data-gid="${group.gameResource.resourceId}" name="followGroup" class="joinbtn"><span>+加入</span></a>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </li>
                                   </c:forEach>
                        </ul>
                    </div>
                    <c:set var="pageurl" value="${URL_WWW}/search/group/${key}/"/>
                    <%@ include file="/views/jsp/page/page.jsp" %>
                </c:when>
                <c:otherwise>
                    <!-- 没有搜索结果(也是引用现成的) -->
                    <div class="noresult">
                        <div class="no1"><span class="tanh"></span>抱歉没有找到含有“<span><c:out value="${key}"/></span>”的小组</div>
                        <p>• 请尝试用准确的关键词搜索，例如游戏名称等</p>
                        <p>• 用空格将多个关键词分开</p>
                        <p>• 看看输入的文字是否有误</p>
                    </div>
                </c:otherwise>
            </c:choose>
<!--wsearch-->
</div>
<!--search right-->
  <%@ include file="/views/jsp/search/search-group-right.jsp" %>

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