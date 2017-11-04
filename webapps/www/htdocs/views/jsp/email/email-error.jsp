<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <title>邮箱修改 ${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/style.css?${version}" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${URL_LIB}/static/js/common/google-statistics.js"></script>


</head>
<body>
<c:import url="/views/jsp/passport/header.jsp"/>
<div class="icontent clearfix">
    <div id="content" class="set_all">
        <div class="set_title">
            <h3>邮箱认证失败！</h3>
        </div>

        <div class="set_domain_text">
            <li><fmt:message key="${errorInfo}" bundle="${userProps}"/></li>
            <li><a id="a_send" href="${ctx}/home" class="submitbtn"><span>返回首页</span></a></li>
        </div>
    </div>
</div>
</body>
</html>
