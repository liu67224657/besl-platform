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
    <div class="page" style="${pagestyle}">
        <c:if test="${page.curPage>1}">
            <a href="${pageurl}p=${page.curPage-1}${anchorPoint}" class="prepage"><span>上一页</span></a>
        </c:if>
        <c:choose>
            <c:when test="${page.curPage<=4}">
                <c:choose>
                    <c:when test="${page.maxPage<=4}">
                        <c:forEach var="curPage" begin="1" end="${page.maxPage}">
                            <c:choose>
                                <c:when test="${page.curPage!=curPage}">
                                    <a href="${pageurl}p=${curPage}${anchorPoint}"><span>${curPage}</span></a>
                                </c:when>
                                <c:otherwise>
                                    <b>${curPage}</b>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="curPage" begin="1" end="4">
                            <c:choose>
                                <c:when test="${page.curPage!=curPage}">
                                    <a href="${pageurl}p=${curPage}${anchorPoint}"><span>${curPage}</span></a>
                                </c:when>
                                <c:otherwise>
                                    <b>${curPage}</b>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                        <c:if test="${page.curPage>=3}">
                            <c:if test="${page.curPage>3}">
                                <a href="${pageurl}p=${page.curPage+1}${anchorPoint}"><span>${page.curPage+1}</span></a>
                            </c:if>
                            <c:if test="${page.maxPage>=page.curPage+2}">
                                <a href="${pageurl}p=${page.curPage+2}${anchorPoint}"><span>${page.curPage+2}</span></a>
                            </c:if>
                        </c:if>
                        <c:if test="${page.maxPage>=page.curPage+3}"><!--'4'->'3' '>'->'>='-->
                            <em>...</em>
                        </c:if>
                        <%--<c:if test="${page.curPage<3||(page.curPage>=3 && page.maxPage>page.curPage+3)}">--%>
                            <%--<a href="${pageurl}p=${page.maxPage}"><span>${page.maxPage}</span></a>--%>
                        <%--</c:if>--%>
                    </c:otherwise>
                </c:choose>
            </c:when>
            <c:when test="${page.curPage>4}">
                <a href="${pageurl}p=1${anchorPoint}"><span>1</span></a>
                <em>...</em>
                <c:choose>
                    <c:when test="${page.curPage<page.maxPage-2}">
                        <c:forEach var="curPage" begin="${page.curPage-2}" end="${page.curPage+2}">
                            <c:choose>
                                <c:when test="${page.curPage!=curPage}">
                                    <a href="${pageurl}p=${curPage}${anchorPoint}"><span>${curPage}</span></a>
                                </c:when>
                                <c:otherwise><b>${curPage}</b></c:otherwise>
                            </c:choose>
                        </c:forEach>
                        <em>...</em>
                        <%--<a href="${pageurl}p=${page.maxPage}"><span>${page.maxPage}</span></a>--%>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="curPage" begin="${page.curPage-2}" end="${page.maxPage}">
                            <c:choose>
                                <c:when test="${page.curPage!=curPage}">
                                    <a href="${pageurl}p=${curPage}${anchorPoint}"><span>${curPage}</span></a>
                                </c:when>
                                <c:otherwise><b>${curPage}</b></c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </c:when>
        </c:choose>
        <c:choose>
            <c:when test="${page.curPage<page.maxPage}">
                <a href="${pageurl}p=${page.curPage+1}${anchorPoint}" class="nextpage"><span>下一页</span></a>
            </c:when>
        </c:choose>
    </div>
</c:if>