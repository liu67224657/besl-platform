<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <title>博文编辑 ${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>

    <%--<script type="text/javascript" src="${URL_LIB}/static/js/common/google-statistics.js"></script>--%>
</head>
<body>
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>
<div class="content homebg clearfix" id="content">
    <!--左侧开始-->
    <div id="cont_left" class="conleft">
        <!--发布开始-->
        <div id="post_area" class="conmentbd clearfix">
            <div class="conmenthd">有什么好玩的游戏想告诉大家？</div>
            <div class="talk "></div>
            <div class="edit ">
                <%@ include file="/views/jsp/edit/edit-frame.jsp" %>
            </div>
            <!--发布结束-->
        </div>
    </div>
    <!--右侧开始-->
    <%@ include file="/views/jsp/tiles/rightmenu.jsp" %>
    <!--右侧结束-->
</div>
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/edit-init.js')
</script>
</body>
</html>