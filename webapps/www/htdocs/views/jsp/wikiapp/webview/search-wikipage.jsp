<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <title>搜索</title>


<body>
<form action="http://api.${DOMAIN}/joymeapp/wikiapp/webview/search/wikipage">
    关键词：<input type="text" name="text" value="${text}">&nbsp;&nbsp;
    wikikey：
    <input type="text" name="wikikey" value="${wikikey}">&nbsp;&nbsp;
    <input type="submit" value="搜索">
</form>

<c:forEach varStatus="st" items="${result}" var="dto">
    <a href="http://www.joyme.com/mwiki/${dto.wikiKey}/${dto.wikiPageId}.shtml">${dto.title}</a><br/>
</c:forEach>
</body>
</html>