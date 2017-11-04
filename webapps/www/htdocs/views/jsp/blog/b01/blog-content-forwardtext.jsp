<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<h3><a href="${URL_WWW}/note/${blogContent.rootContent.contentId}"
       target="_blank"><c:out value="${blogContent.rootContent.subject}"/></a></h3>

<p>${blogContent.rootContent.content}</p>

<div class="area_ft">
    <span class="time">${dateutil:parseDate(blogContent.rootContent.publishDate)}</span>
        <c:if test="${blogContent.rootBoard!=null}">
            <span class="from">
            来自：<a href="${URL_WWW}/group/${blogContent.rootBoard.gameCode}/talk" target="_blank">${blogContent.rootBoard.gameName}小组</a>
            </span>
        </c:if>
    <div class="operate">
        <ul>
            <li>
                <a href="${URL_WWW}/note/${blogContent.rootContent.contentId}"
                   class="share" title="评论">评论(${blogContent.rootContent.replyTimes})</a></li>
            <li>
        </ul>
    </div>
</div>
