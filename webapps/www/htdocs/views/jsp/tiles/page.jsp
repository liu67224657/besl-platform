<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<c:choose>
   <c:when test="${fn:length(pageparam)>0}">
        <c:set var="pageurl" value="${pageurl}?${pageparam}&"></c:set>
   </c:when>
   <c:otherwise>
        <c:set var="pageurl" value="${pageurl}?"></c:set>
   </c:otherwise>
</c:choose>

<c:if test="${page.maxPage>1}">
    <div class="t_page" style="${pagestyle}">
        <c:if test="${page.curPage>1}">
            <a href="${pageurl}p=${page.curPage-1}" class="prev"><em>上一页</em></a>
        </c:if>
        <c:choose>
            <c:when test="${page.curPage<=3}">
                <c:choose>
                    <c:when test="${page.maxPage<=3}">
                        <c:choose>
                            <c:when test="${page.curPage!=1}">
                                <a href="${pageurl}p=1"><em>1</em></a>
                            </c:when>
                            <c:otherwise><b>1</b></c:otherwise>
                        </c:choose>
                        <c:if test="${page.maxPage>=2}">
                            <c:choose>
                                <c:when test="${page.curPage!=2}">
                                    <a href="${pageurl}p=2"><em>2</em></a>
                                </c:when>
                                <c:otherwise><b>2</b></c:otherwise>
                            </c:choose>
                        </c:if>
                        <c:if test="${page.maxPage==3}">
                            <c:choose>
                                <c:when test="${page.curPage!=3}">
                                    <a href="${pageurl}p=3"><em>3</em></a>
                                </c:when>
                                <c:otherwise><b>3</b>
                                </c:otherwise>
                            </c:choose>
                        </c:if>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="curPage" begin="1" end="3">
                            <c:choose>
                                <c:when test="${page.curPage!=curPage}">
                                    <a href="${pageurl}p=${curPage}"><em>${curPage}</em></a>
                                </c:when>
                                <c:otherwise><b>${curPage}</b></c:otherwise>
                            </c:choose>
                        </c:forEach>
                        <c:if test="${page.curPage==3}">
                            <a href="${pageurl}p=${page.curPage+1}"><em>${page.curPage+1}</em></a>
                        </c:if>
                        <c:if test="${page.curPage<3||(page.curPage==3 && page.maxPage>3+1)}">
                            <b>...</b>
                        </c:if>
                    </c:otherwise>
                </c:choose>
            </c:when>
            <c:when test="${page.curPage>3}">
                <a href="${pageurl}p=1"><em>1</em></a>
                <c:choose>
                    <c:when test="${page.curPage<page.maxPage-1}">
                        <b>...</b>
                        <c:forEach var="curPage" begin="${page.curPage-1}" end="${page.curPage+1}">
                            <c:choose>
                                <c:when test="${page.curPage!=curPage}">
                                    <a href="${pageurl}p=${curPage}"><em>${curPage}</em></a>
                                </c:when>
                                <c:otherwise><b>${curPage}</b></c:otherwise>
                            </c:choose>
                        </c:forEach>
                        <b>...</b>
                    </c:when>
                    <c:otherwise>
                        <c:if test="${page.curPage>4}">
                            <b>...</b>
                        </c:if>
                        <c:forEach var="curPage" begin="${page.maxPage-2}" end="${page.maxPage}">
                            <c:choose>
                                <c:when test="${page.curPage!=curPage}">
                                    <a href="${pageurl}p=${curPage}"><em>${curPage}</em></a>
                                </c:when>
                                <c:otherwise><b>${curPage}</b></c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>

            </c:when>
        </c:choose>
        <c:if test="${page.curPage<page.maxPage}">
            <a href="${pageurl}p=${page.curPage+1}" class="next"><em>下一页</em></a>
        </c:if>
    </div>
</c:if>
