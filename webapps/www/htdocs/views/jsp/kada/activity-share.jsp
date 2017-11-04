<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
    <link href="${URL_LIB}/static/theme/default/css/kada.css?${version}" rel="stylesheet" type="text/css"/>
    <script src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>
    <title>着迷 </title>
</head>
<body style="background:#242429">
<article class="item1">
    <section>
        <img src="images/img-1.jpg">
    </section>
    <section class="title">
        <div class="t1">
            <h2>${activity.title}</h2>
            <span>参与人数：<strong>${joinNumber}</strong></span>
        </div>
        <div class="t2">${activity.description}</div>
    </section>
</article>

<article class="item2">
    <h2>活动照片</h2>
    <div class="piclist2">
        <c:forEach var="dto" items="${list}">
            <a href="#">
                <div class="box">
                    <div><img src="${dto.profile.icon}">${dto.profile.username}</div>
                    <span>${dto.profile.fanssum}</span>
                    <img class="t" src="${dto.content.pic.pic_s}">
                </div>
            </a>
        </c:forEach>
    </div>
</article>
<div id="bottomLayer" class="bottomLayer">
    <div><span>Kada</span>有声有影，随心随性！</div>
    <a href="#">免费下载</a>
</div>
</body>
</html>