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
    <div class="paging page-pc web-hide">

        <c:if test="${page.curPage>1}">
            <a href="${pageurl}p=1" class="prepage">首页</a>
            <a href="${pageurl}p=${page.curPage-1}" class="prepage">上一页</a>
        </c:if>
        <c:choose>
            <c:when test="${page.maxPage<7}">
                <c:forEach var="curPage" begin="1" end="${page.maxPage}">
                    <c:choose>
                        <c:when test="${page.curPage!=curPage}">

                            <a href="${pageurl}p=${curPage}">${curPage}</a>

                        </c:when>
                        <c:otherwise>
                            <a href="javascript:;" class="on">${curPage}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <c:choose>
                    <c:when test="${page.maxPage-page.curPage<3}">
                        <c:if test="${page.maxPage-page.curPage==2}">
                            <c:forEach var="curPage" begin="${page.curPage-4}"
                                       end="${page.curPage+2}">
                                <c:choose>
                                    <c:when test="${page.curPage!=curPage}">

                                        <a href="${pageurl}p=${curPage}">${curPage}</a>

                                    </c:when>
                                    <c:otherwise>
                                        <a href="javascript:;" class="on">${curPage}</a>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </c:if>
                        <c:if test="${page.maxPage-page.curPage==1}">
                            <c:forEach var="curPage" begin="${page.curPage-5}"
                                       end="${page.curPage+1}">
                                <c:choose>
                                    <c:when test="${page.curPage!=curPage}">

                                        <a href="${pageurl}p=${curPage}">${curPage}</a>

                                    </c:when>
                                    <c:otherwise>
                                        <a href="javascript:;" class="on">${curPage}</a>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </c:if>
                        <c:if test="${page.maxPage==page.curPage}">
                            <c:forEach var="curPage" begin="${page.curPage-6}"
                                       end="${page.maxPage}">
                                <c:choose>
                                    <c:when test="${page.curPage!=curPage}">

                                        <a href="${pageurl}p=${curPage}">${curPage}</a>

                                    </c:when>
                                    <c:otherwise>
                                        <a href="javascript:;" class="on">${curPage}</a>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </c:if>

                    </c:when>
                    <c:otherwise>
                        <c:choose>
                            <c:when test="${page.curPage<7}">
                                <c:forEach var="curPage" begin="1" end="7">
                                    <c:choose>
                                        <c:when test="${page.curPage!=curPage}">

                                            <a href="${pageurl}p=${curPage}">${curPage}</a>

                                        </c:when>
                                        <c:otherwise>
                                            <a href="javascript:;" class="on">${curPage}</a>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="curPage" begin="${page.curPage-3}" end="${page.curPage+3}">
                                    <c:choose>
                                        <c:when test="${page.curPage!=curPage}">

                                            <a href="${pageurl}p=${curPage}">${curPage}</a>

                                        </c:when>
                                        <c:otherwise>
                                            <a href="javascript:;" class="on">${curPage}</a>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </c:otherwise>
                </c:choose>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${page.curPage<page.maxPage}">
                <a href="${pageurl}p=${page.curPage+1}" class="next">下一页</a>

                <a href="${pageurl}p=${page.maxPage}" class="last">尾页</a>

            </c:when>
            <c:otherwise>

            </c:otherwise>
        </c:choose>
        <a href="javascript:;" class="count-num">共<b> ${page.maxPage}</b>页<b>${page.totalRows}</b>条</a>


    </div>
</c:if>