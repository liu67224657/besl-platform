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
    <div class="pagecon">
        <div class="page" style="${pagestyle}">
            <ul>
                <c:if test="${page.curPage>1}">
                    <li><a href="${pageurl}p=1${anchorPoint}" class="prepage">首页</a></li>
                    <li><a href="${pageurl}p=${page.curPage-1}${anchorPoint}" class="prepage">上一页</a></li>
                </c:if>
                <c:choose>
                    <c:when test="${page.maxPage<7}">
                        <c:forEach var="curPage" begin="1" end="${page.maxPage}">
                            <c:choose>
                                <c:when test="${page.curPage!=curPage}">
                                    <li>
                                        <a href="${pageurl}p=${curPage}${anchorPoint}">${curPage}</a>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <li class="thisclass">${curPage}</li>
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
                                                <li>
                                                    <a href="${pageurl}p=${curPage}${anchorPoint}">${curPage}</a>
                                                </li>
                                            </c:when>
                                            <c:otherwise>
                                                <li class="thisclass">${curPage}</li>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </c:if>
                                <c:if test="${page.maxPage-page.curPage==1}">
                                    <c:forEach var="curPage" begin="${page.curPage-5}"
                                               end="${page.curPage+1}">
                                        <c:choose>
                                            <c:when test="${page.curPage!=curPage}">
                                                <li>
                                                    <a href="${pageurl}p=${curPage}${anchorPoint}">${curPage}</a>
                                                </li>
                                            </c:when>
                                            <c:otherwise>
                                                <li class="thisclass">${curPage}</li>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </c:if>
                                <c:if test="${page.maxPage==page.curPage}">
                                    <c:forEach var="curPage" begin="${page.curPage-6}"
                                               end="${page.maxPage}">
                                        <c:choose>
                                            <c:when test="${page.curPage!=curPage}">
                                                <li>
                                                    <a href="${pageurl}p=${curPage}${anchorPoint}">${curPage}</a>
                                                </li>
                                            </c:when>
                                            <c:otherwise>
                                                <li class="thisclass">${curPage}</li>
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
                                                    <li>
                                                        <a href="${pageurl}p=${curPage}${anchorPoint}">${curPage}</a>
                                                    </li>
                                                </c:when>
                                                <c:otherwise>
                                                    <li class="thisclass">${curPage}</li>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="curPage" begin="${page.curPage-3}" end="${page.curPage+3}">
                                            <c:choose>
                                                <c:when test="${page.curPage!=curPage}">
                                                    <li>
                                                        <a href="${pageurl}p=${curPage}${anchorPoint}">${curPage}</a>
                                                    </li>
                                                </c:when>
                                                <c:otherwise>
                                                    <li class="thisclass">${curPage}</li>
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
                        <li><a href="${pageurl}p=${page.curPage+1}${anchorPoint}">下一页</a>
                        </li>
                        <li><a href="${pageurl}p=${page.maxPage}${anchorPoint}">尾页</a>
                        </li>
                    </c:when>
                    <c:otherwise>

                    </c:otherwise>
                </c:choose>
                <li><span class="pageinfo">共 <strong>${page.maxPage}</strong>页<strong>${page.totalRows}</strong>条</span>
                </li>

            </ul>
        </div>
    </div>
</c:if>