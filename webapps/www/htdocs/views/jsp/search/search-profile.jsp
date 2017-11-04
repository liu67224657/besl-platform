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
<c:set var="holdstype" value="profile"/>
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
            <!-- wsearch -->
            <%@ include file="/views/jsp/search/hold-search.jsp"%>
            <!--wsearch-->


                <c:choose>
                  <c:when test="${fn:length(list)>0}">
                        <div class="result-report">“${key}”相关的用户（${page.totalRows}）</div>
                      <div class="result-user">
                      <%@include file="/views/jsp/focus/profile-list.jsp" %>
                      </div>
                      <c:set var="pageurl" value="${URL_WWW}/search/profile/${key}/"/>
                      <%@ include file="/views/jsp/page/page.jsp" %>
                  </c:when>
                  <c:otherwise>
                      <div class="noresult">
                          <div class="no1"><span class="tanh"></span>抱歉没有找到含有“<span><c:out value="${key}"/></span>”的人</div>
                          <p>• 请尝试用准确的关键词搜索，例如游戏名称等</p>
                          <p>• 用空格将多个关键词分开</p>
                          <p>• 看看输入的文字是否有误</p>
                      </div>
                  </c:otherwise>
                </c:choose>

            <!--wsearch_friend-->
        </div>
        <!--searchl-->
        <%@ include file="/views/jsp/search/search-profile-right.jsp" %>
        <!--searchl-->
    </div><!--searchc-->
    <div class="wsearchb"></div>
</div>
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/search-profile-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>

</body>
</html>