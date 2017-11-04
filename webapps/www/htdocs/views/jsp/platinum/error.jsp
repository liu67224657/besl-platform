<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <meta content="width=device.width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link href="${URL_LIB}/static/theme/default/css/platinum.css?${version}" rel="stylesheet" type="text/css"/>
    <title>精品手游</title>
</head>
<body>
<div class="topbar">
    <div><a class="current" href="${URL_WWW}/platinum/${appsCode}">精品手游</a></div>
    <div><a href="${URL_WWW}/platinum/${eventsCode}">热门活动</a></div>
</div>
<div class="listbox">
    <div align="center" style="height: 50px;line-height: 50px"><c:if test="${fn:length(message)>0}"><fmt:message key="${message}" bundle="${userProps}"></fmt:message></c:if></div>
</div>
</body>
</html>