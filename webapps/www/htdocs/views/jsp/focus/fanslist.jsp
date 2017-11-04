<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <title>我的粉丝 ${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <script src="${URL_LIB}/static/js/common/seajs.js"></script>
    <script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
    <script>
        seajs.use('${URL_LIB}/static/js/init/fans_init.js')
    </script>

</head>

<body>
<!--头部开始-->
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>
<!--头部结束-->
<div class="wrapper clearfix">
    <div class="con">
        <div class="con_hd"></div>
        <div class="con_area con_blog clearfix">
            <div class="friend-title">已有<c:out value="${userSession.countdata.fansSum}"/>人关注你</div>
            <c:choose>
                <c:when test="${fn:length(list)>0}">
                    <%@ include file="/views/jsp/focus/profile-list.jsp" %>
                </c:when>
                <c:otherwise>
                    <div class="uncontent clearfix"><fmt:message key="fans.my.empty" bundle="${userProps}"/></div>
                </c:otherwise>
            </c:choose>
            <c:set var="pageurl" value="${ctx}/social/fans/list"/>
            <%@ include file="/views/jsp/page/page.jsp" %>
        </div>
        <div class="con_ft"></div>
    </div>
    <%@ include file="/views/jsp/tiles/rightmenu.jsp" %>
    <!--conright结束-->
</div>

<!--content结束-->
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</body>
</html>
