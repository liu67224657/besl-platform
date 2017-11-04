<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>新游帐号列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script>
        $().ready(function() {

        });
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 条目管理 >> 新游帐号管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">新游帐号列表</td>
                </tr>
            </table>
            <table>
                <tr>
                    <td>
                        <form method="post" action="/developers/list">
                            <table>
                                <tr>
                                    <td>
                                        <select name="category">
                                            <option value="">请选择</option>
                                            <c:forEach items="${categoryCollection}" var="ca">
                                                <option value="${ca.code}"
                                                        <c:if test="${ca.code == category}">selected="selected"</c:if>>
                                                    <fmt:message key="profile.developer.category.${ca.code}"
                                                                 bundle="${def}"/>
                                                </option>
                                            </c:forEach>
                                        </select>
                                    <td>
                                    <td>
                                        <input type="submit" name="submit" value="查询"/>
                                    </td>
                                <tr>
                            </table>
                        </form>
                    </td>
                </tr>
            </table>
            <table>
                <tr>
                    <td>
                        <a href="/developers/list"><input type="button" name="button" class="default_button" value="全部查询"/></a>
                        <a href="/developers/list?status=audit"><input type="button" class="default_button" value="待审核查询"/></a>
                        <a href="/developers/list?status=access"><input type="button" class="default_button" value="已通过审核查询"/></a>
                        <a href="/developers/list?status=disapproved"><input type="button" class="default_button" value="未通过审核查询"/></a>
                    </td>
                </tr>
            </table>
            <form action="/developers/list" method="post">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="13" class="default_line_td"></td>
                    </tr>
                    <c:if test="${fn:length(errorMsg)>0}">
                        <tr>
                            <td height="1" colspan="13" class="error_msg_td">
                                <b><fmt:message key="${errorMsg}" bundle="${error}"/></b>
                            </td>
                        </tr>
                    </c:if>
                    <tr class="list_table_title_tr">
                        <td nowrap align="left">uno</td>
                        <td nowrap align="left">帐号</td>
                        <td nowrap align="left">认证说明</td>
                        <td nowrap align="left">状态</td>
                        <td nowrap align="left">申请日期</td>
                        <td nowrap align="left">操作</td>
                    </tr>
                    <tr>
                        <td height="1" colspan="13" class="default_line_td"></td>
                    </tr>
                    <c:choose>
                        <c:when test="${list.size() > 0}">
                            <c:forEach items="${list}" var="dto" varStatus="st">
                                <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                    <td nowrap align="left">${dto.developer.uno}</td>
                                    <td nowrap align="left">${dto.blog.screenName}</td>
                                    <td nowrap align="left">${dto.developer.verifyDesc}</td>
                                    <td nowrap align="left"
                                        <c:choose>
                                            <c:when test="${dto.developer.verifyStatus.code == 'access'}">
                                                style="color:#008000;"
                                            </c:when>
                                            <c:when test="${dto.developer.verifyStatus.code == 'remove'}">
                                                style="color:#808080;"
                                            </c:when>
                                            <c:when test="${dto.developer.verifyStatus.code == 'disapproved'}">
                                                style="color:#ff0000;"
                                            </c:when>
                                        </c:choose>>
                                        <fmt:message key="profile.developer.status.${dto.developer.verifyStatus.code}" bundle="${def}"/></td>
                                    <td nowrap align="left"><fmt:formatDate value="${dto.developer.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                    <td nowrap align="left"><a href="/developers/detail?uno=${dto.developer.uno}">查看</a>
                                        <%--<c:choose>--%>
                                            <%--<c:when test="${dto.developer.verifyStatus.code == 'remove'}">--%>
                                                <%--<a href="/developers/recover?uno=${dto.developer.uno}">解封</a>--%>
                                            <%--</c:when>--%>
                                            <%--<c:otherwise>--%>
                                                <%--<a href="/developers/delete?uno=${dto.developer.uno}">封停</a>--%>
                                            <%--</c:otherwise>--%>
                                        <%--</c:choose>--%>
                                    </td>
                                </tr>
                            </c:forEach>
                            <tr>
                                <td height="1" colspan="13" class="default_line_td"></td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="13" class="error_msg_td">暂无数据!</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    <tr>
                        <td colspan="13" height="1" class="default_line_td"></td>
                    </tr>
                    <c:if test="${page.maxPage > 1}">
                        <tr class="list_table_opp_tr">
                            <td colspan="13">
                                <pg:pager url="/developers/list"
                                          items="${page.totalRows}" isOffset="true"
                                          maxPageItems="${page.pageSize}"
                                          export="offset, currentPageNumber=pageNumber" scope="request">
                                    <pg:param name="category" value="${category}"/>
                                    <pg:param name="status" value="${status}"/>
                                    <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                    <pg:param name="items" value="${page.totalRows}"/>
                                    <%@ include file="/WEB-INF/jsp/toolspg.jsp" %>
                                </pg:pager>
                            </td>
                        </tr>
                    </c:if>
                </table>
            </form>
        </td>
    </tr>
</table>
</body>
</html>