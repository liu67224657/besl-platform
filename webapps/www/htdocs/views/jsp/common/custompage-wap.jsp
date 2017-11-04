<!doctype html>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <title><c:if test="${message != null}"><fmt:message key="${message}"
                                                        bundle="${userProps}"></fmt:message></c:if></title>
    <link href="${URL_LIB}/static/theme/wap/css/errorpage.css" rel="stylesheet" type="text/css">
<body>
<div id="wrapper">
    <div class="no-net">
        <%--<b><img src="${URL_LIB}/static/theme/wap/images/no-net.jpg" alt=""></b>--%>
        <b></b>

        <p><c:if test="${message == null}">页面加载失败</c:if></p>

        <p><c:if test="${message != null}">
            <fmt:message key="${message}" bundle="${userProps}"></fmt:message>
        </c:if></p>
    </div>
</div>
</body>
<script>
    window.setTimeout(function(){
        window.location.href="http://m.joyme.com";
    },3000);
</script>
</html>