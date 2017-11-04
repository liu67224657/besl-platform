<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>活动管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script>
        $(document).ready(function () {
            $('#form_submit').bind('submit', function () {


            });
        });
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="1" valign="top">
            <table>
                <tr>
                    <td height="1">
                        <form action="/gamedb/archives/list" method="post">
                            <table>
                                <tr>
                                    <td>
                                        状态:
                                    </td>
                                    <td>
                                        <select name="status">
                                            <option value="valid"
                                                    <c:if test="${status=='valid'}">selected</c:if> >可用
                                            </option>
                                            <option value="removed"
                                                    <c:if test="${status=='removed'}">selected</c:if>>已删除
                                            </option>
                                        </select>
                                    </td>
                                    <td>
                                        分类:
                                    </td>
                                    <td>
                                        <select name="contenttype">
                                            <c:forEach items="${contentTypeSet}" var="content">
                                                <option value="${content.code}"
                                                        <c:if test="${content.code== contentType}">selected</c:if> >${content.name}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td>
                                        <input type="hidden" name="gameid" value="${gameId}"/>
                                        <input type="submit" class="default_button" value="查询"/>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="13" class="error_msg_td">
                            <b><fmt:message key="${errorMsg}" bundle="${error}"/></b>
                        </td>
                    </tr>
                </c:if>
                <tr class="list_table_title_tr">
                    <td nowrap align="left">序号</td>
                    <td nowrap align="left">文章ID</td>
                    <td nowrap align="left">标题</td>
                    <td nowrap align="left">头图</td>
                    <td nowrap align="left">简介</td>
                    <td nowrap align="left">状态</td>
                    <td nowrap align="left">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="13" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="archive" varStatus="st">
                            <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap align="left">${st.index + 1}</td>
                                <td nowrap align="left">${archive.dede_archives_id}</td>
                                <td nowrap align="left">${archive.dede_archives_title}</td>
                                <td nowrap align="left"><img src="${archive.dede_archives_litpic}"/></td>
                                <td nowrap align="left">
                                    <textarea cols="20" rows="3">${archive.dede_archives_description}</textarea>
                                </td>
                                <td nowrap align="left"
                                        <c:choose>
                                            <c:when test="${archive.remove_status.code == 'valid'}">style="color:
                                                #008000;" </c:when>
                                            <c:otherwise>style="color: #ff0000;"</c:otherwise>
                                        </c:choose>>
                                    <c:choose>
                                        <c:when test="${archive.remove_status.code == 'valid'}">可用</c:when>
                                        <c:otherwise>删除</c:otherwise>
                                    </c:choose>
                                </td>
                                <td nowrap align="left">
                                    <c:choose>
                                        <c:when test="${archive.remove_status.code == 'valid'}">
                                            <a href="/gamedb/archives/delete?gameid=${gameId}&archiveid=${archive.dede_archives_id}&contenttype=${contentType}&status=${status}">删除</a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="/gamedb/archives/recover?gameid=${gameId}&archiveid=${archive.dede_archives_id}&contenttype=${contentType}&status=${status}">恢复</a>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="13" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="13" height="1" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="13" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="13">
                            <pg:pager url="/gamedb/archives/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="gameid" value="${gameId}"/>
                                <pg:param name="contenttype" value="${contentType}"/>
                                <pg:param name="status" value="${status}"/>
                                <pg:param name="currentPageNumber" value="${page.curPage}"/>
                                <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                <pg:param name="items" value="${page.totalRows}"/>
                                <%@ include file="/WEB-INF/jsp/toolspg.jsp" %>
                            </pg:pager>
                        </td>
                    </tr>
                </c:if>
            </table>
        </td>
    </tr>
</table>
</body>
</html>