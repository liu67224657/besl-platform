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
    <title>热门活动</title>
</head>
<body>
<div class="topbar">
    <div><a href="${URL_WWW}/platinum/${appsCode}">精品手游</a></div>
    <div><a class="current" href="${URL_WWW}/platinum/${eventsCode}">热门活动</a></div>
</div>

<div class="listbox2">
    <c:if test="${hotList.size() > 0}">
        <h2 class="tit-hot">热门活动</h2>
        <c:forEach items="${hotList}" var="hotDto">
            <div class="list">
                <img src="${hotDto.pic}">

                <div>
                    <h2>${hotDto.title}</h2>

                    <p>${hotDto.description}</p>

                    <p>时间:<fmt:formatDate value="${hotDto.startDate}" pattern="yyyy-MM-dd"/>--<fmt:formatDate value="${hotDto.endDate}" pattern="yyyy-MM-dd"/></p>
                </div>
                <a class="down" href="${hotDto.url}">参加</a>
            </div>
        </c:forEach>
    </c:if>
    <c:choose>
        <c:when test="${dateList.size() > 0}">
            <c:forEach items="${dateList}" var="date">
                <c:choose>
                    <c:when test="${dtoMap[date].size() > 0}">
                        <h2 class="tit-time">${date}</h2>
                        <c:forEach items="${dtoMap[date]}" var="dto">
                            <div class="list">
                                <img src="${dto.pic}">

                                <div>
                                    <h2>${dto.title}</h2>

                                    <p>${dto.description}</p>

                                    <p>时间:<fmt:formatDate value="${dto.startDate}" pattern="yyyy-MM-dd"/>--<fmt:formatDate value="${dto.endDate}" pattern="yyyy-MM-dd"/></p>
                                </div>
                                <a class="down" href="${dto.url}">参加</a>
                            </div>
                        </c:forEach>
                    </c:when>
                </c:choose>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <div align="center" style="height: 50px;line-height: 50px">暂时没有内容~</div>
        </c:otherwise>
    </c:choose>
</div>

</body>
</html>
