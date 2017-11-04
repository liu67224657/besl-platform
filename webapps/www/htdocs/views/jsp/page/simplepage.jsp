<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<c:choose>
   <c:when test="${fn:length(pageparam)>0}">
        <c:set var="pageurl" value="${pageurl}?${pageparam}&"></c:set>
   </c:when>
   <c:otherwise>
        <c:set var="pageurl" value="${pageurl}?"></c:set>
   </c:otherwise>
</c:choose>
<c:if test="${fn:length(anchorPoint)>0}">
       <c:set var="anchorPoint" value="#${anchorPoint}"></c:set>
</c:if>
<c:if test="${page.maxPage>1}">
    <div class="pages-box" style="${pagestyle}">
        <c:if test="${page.curPage>1}">
            <a href="${pageurl}p=${page.curPage-1}${anchorPoint}" class="prev-page">上一页</a>
        </c:if>
        <c:if test="${page.curPage<page.maxPage}">
            <a href="${pageurl}p=${page.curPage+1}${anchorPoint}" class="next-page">下一页</a>
        </c:if>
    </div>
</c:if>