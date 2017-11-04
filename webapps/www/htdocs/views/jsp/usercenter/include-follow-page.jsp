<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<c:if test="${page!=null && page.maxPage>1}">
    <div class="paging web-hide">
        <a href="/usercenter/follow/mylist">首页</a>
        <c:forEach var="displayPage" items="${page.getDisplayingPages()}">
            <c:choose>
                <c:when test="${displayPage!=page.curPage}">
                    <a href="${pageurl}pnum=${displayPage}">${displayPage}</a>
                </c:when>
                <c:otherwise>
                    <a href="javascript:;" class="on">${displayPage}</a>
                </c:otherwise>
            </c:choose>
        </c:forEach>
        <a href="${pageurl}pnum=${page.curPage+1}" class="next">下一页</a>
        <a href="${pageurl}pnum=${page.maxPage}" class="last">末页</a>
        <a href="javascript:;" class="count-num">共<b>${page.maxPage}</b>页<b>${page.totalRows}</b>条</a>
    </div>
</c:if>