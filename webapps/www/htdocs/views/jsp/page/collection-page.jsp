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
        <div class="page">
            <ul>
                <li name="li_page" data-page="1"><a href="javascript:void(0);">首页</a></li>
                <c:if test="${page.curPage>1}">
                    <li name="li_page" data-page="${page.curPage-1}"><a href="javascript:void(0);">上一页</a></li>
                </c:if>
                <c:choose>
                    <c:when test="${page.curPage<=4}">
                        <c:choose>
                            <c:when test="${page.maxPage<=4}">
                                <c:forEach var="curPage" begin="1" end="${page.maxPage}">
                                    <c:choose>
                                        <c:when test="${page.curPage==curPage}">
                                            <li name="li_page" data-page="${curPage}" class="thisclass">${curPage}</li>
                                        </c:when>
                                        <c:otherwise>
                                            <li name="li_page" data-page="${curPage}"><a
                                                    href="javascript:void(0);">${curPage}</a></li>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="curPage" begin="1" end="4">
                                    <c:choose>
                                        <c:when test="${page.curPage==curPage}">
                                            <li name="li_page" data-page="${curPage}" class="thisclass">${curPage}</li>
                                        </c:when>
                                        <c:otherwise>
                                            <li name="li_page" data-page="${curPage}"><a
                                                    href="javascript:void(0);">${curPage}</a></li>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                                <c:if test="${page.curPage>=3}">
                                    <c:if test="${page.curPage>3}">
                                        <li name="li_page" data-page="${page.curPage+1}"><a
                                                href="javascript:void(0);">${page.curPage+1}</a>
                                        </li>
                                    </c:if>
                                    <c:if test="${page.maxPage>=page.curPage+2}">
                                        <li name="li_page" data-page="${page.curPage+2}"><a
                                                href="javascript:void(0);">${page.curPage+2}</a>
                                        </li>
                                    </c:if>
                                </c:if>
                                <c:if test="${page.curPage<3||(page.curPage>=3 && page.maxPage>page.curPage+3)}">
                                    <li name="li_page" data-page="${page.maxPage}"><a
                                            href="javascript:void(0);">${page.maxPage}</a></li>
                                </c:if>
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:when test="${page.curPage>4}">
                        <li name="li_page" data-page="1"><a href="javascript:void(0);">1</a></li>
                        <c:choose>
                            <c:when test="${page.curPage<page.maxPage-2}">
                                <c:forEach var="curPage" begin="${page.curPage-2}" end="${page.curPage+2}">
                                    <c:choose>
                                        <c:when test="${page.curPage==curPage}">
                                            <li name="li_page" data-page="${curPage}" class="thisclass">${curPage}</li>
                                        </c:when>
                                        <c:otherwise>
                                            <li name="li_page" data-page="${curPage}"><a
                                                    href="javascript:void(0);">${curPage}</a></li>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                                <li name="li_page" data-page="${page.maxPage}"><a
                                        href="javascript:void(0);">${page.maxPage}</a></li>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="curPage" begin="${page.curPage-2}" end="${page.maxPage}">
                                    <c:choose>
                                        <c:when test="${page.curPage==curPage}">
                                            <li name="li_page" data-page="${curPage}" class="thisclass">${curPage}</li>
                                        </c:when>
                                        <c:otherwise>
                                            <li name="li_page" data-page="${curPage}"><a
                                                    href="javascript:void(0);">${curPage}</a></li>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                </c:choose>
                <c:if test="${page.curPage<page.maxPage}">
                    <li name="li_page" data-page="${page.curPage+1}"><a href="javascript:void(0);">下一页</a></li>
                </c:if>
                <li name="li_page" data-page="${page.maxPage}"><a href="javascript:void(0);">末页</a></li>
                <li><span class="pageinfo">共 <strong>${page.maxPage}</strong>页<strong>${page.totalRows}</strong>条</span>
                </li>
            </ul>
        </div>
    </div>
</c:if>