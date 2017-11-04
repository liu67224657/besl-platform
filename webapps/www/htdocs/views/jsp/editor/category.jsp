<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <title>${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
</head>
<body>
<div id="wraper">
    <c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>
    <div class="content homebg clearfix" id="content">
        <!--左侧开始-->
        <div id="cont_left" class="conleft">
            <div style="margin:200px auto;text-align:center">
                <c:forEach var="category" items="${viewCategoryList}" varStatus="st">
                <span style="padding:10px;font-size:18px;">
                <a href="${URL_WWW}/editor/list?cgid=${category.categoryId}">${category.categoryName}</a>
                </span>
                    <c:if test="${(st.index+1)/6!=0 && (st.index+1)%6==0}">
                        <br/>
                    </c:if>
                </c:forEach>
            </div>
        </div>
        <!--右侧开始-->
        <%@ include file="/views/jsp/tiles/rightmenu.jsp" %>
        <!--右侧结束-->
    </div>
    <%@ include file="/views/jsp/tiles/footer.jsp" %>


</body>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</html>
