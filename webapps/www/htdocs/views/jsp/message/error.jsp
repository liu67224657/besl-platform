<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>收件箱  ${jmh_title}</title>
    <link href="${URL_LIB}/static/defult/css/core.css" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/defult/css/home.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${URL_LIB}/static/js/dialog.js"></script>
    <script type="text/javascript" src="${URL_LIB}/static/js/jquery-1.5.2.js"></script>
    <script type="text/javascript" src="${URL_LIB}/static/js/common/google-statistics.js"></script>

</head>

<body>
<div id="wraper">
    <c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>
    <div id="main">

        <div class="follow_warp">

            <div class="search_error">
                <ul>
                    <li><fmt:message key="${error}" bundle="${userProps}" >
                        <fmt:param value="${name}"/>
                        </fmt:message></li>                                                                       <li><a href="${ctx}/message/private/sendbox">返回上一页</a></li>
                </ul>
            </div>
        </div>

        <%@ include file="/views/jsp/tiles/rightmenu.jsp" %>
    </div>
</div>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main(16);
</script>
</body>
</html>
