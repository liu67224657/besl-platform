<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <title>@提到我的 ${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/style.css?${version}" rel="stylesheet" type="text/css"/>
</head>
<body>
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>
<div class="wrapper clearfix">
    <div class="con">
        <div class="con_hd"></div>
        <div class="con_area con_blog clearfix">
            <div class="conleft_title">
                <h3>@提到我的</h3>
            </div>
            <div class="mycomment_tags">
                <a href="javascript:void(0)" class="up">全部</a>
                <a href="${ctx}/atme/focus">我关注的人</a>
            </div>
            <!-- 内容展示list -->
            <c:if test="${fn:length(atmeList)<1}">
                <div class="empty_text">
                    <fmt:message key="at.me.empty" bundle="${userProps}">
                    </fmt:message>
                </div>
            </c:if>
            <%@ include file="/views/jsp/atme/at-previewlist.jsp" %>
            <!-- 内容展示list -->
            <c:set var="pageurl" value="${ctx}/atme"/>
            <%@ include file="/views/jsp/page/goto.jsp" %>
            <!--左侧结束-->
        </div>
        <div class="con_ft"></div>
    </div>
    <!--右侧开始-->
    <%@ include file="/views/jsp/tiles/rightmenu.jsp" %>
    <!--右侧结束-->
</div>

<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/atme-init.js')
</script>
<script type="text/javascript" src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main(11);
</script>
</body>
</html>
