<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div class="article-time">${dateutil:parseDate(blogContent.content.publishDate)}</div>


<!-- 纯图片 -->
<c:if test="${fn:length(blogContent.content.images.images)>0}">
    <div class="img-and-video">
    <c:forEach var="img" items="${blogContent.content.images.images}">
        <div class="blogarticle_img">
            <a href="<c:out value="${uf:parseBFace(img.m)}"/>" target="_blank">
                <img src="<c:out value="${uf:parseMFace(img.m)}"/>">
            </a>
        </div>
    </c:forEach>
    </div>
</c:if>
<c:if test="${fn:length(blogContent.content.videos.videos)>0}">
    <div class="img-and-video">
        <c:forEach var="video" items="${blogContent.content.videos.videos}">
            <a href="${video.orgUrl}" class="video">
                <span></span>
                <img src="<c:choose><c:when test="${fn:length(video.url)==0}">${URL_LIB}/static/images/default.jpg</c:when><c:otherwise>${video.url}</c:otherwise></c:choose>"/>
            </a>
        </c:forEach>
    </div>
</c:if>
<div class="article-content">
${blogContent.content.content}
</div>

