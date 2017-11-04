<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>直播权限管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script>
        $().ready(function () {
            $('#form_submit').bind('submit', function () {


            });
        });
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 直播权限>> 权限列表 </td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">权限列表</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="13" class="error_msg_td">
                            ${errorMsg}
                        </td>
                    </tr>
                </c:if>
            </table>
            <table>
                <tr>
                    <td>
                    </td>
                    <td>
                        <form method="post" action="/comment/manage/createpage">
                            <table>
                                <tr>
                                    <td>
                                        <input type="submit" name="button" class="default_button" value="增加直播权限"/>
                                    <td>
                                <tr>
                            </table>
                        </form>
                    </td>
                </tr>
            </table>
            <form action="/comment/manage/list" method="post">
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
                        <td nowrap align="left" width="80">ID</td>
                        <td nowrap align="left">昵称</td>
                        <td nowrap align="left">状态</td>
                        <td nowrap align="left">创建人信息</td>
                        <td nowrap align="left">操作</td>
                    </tr>
                    <tr>
                        <td height="1" colspan="13" class="default_line_td"></td>
                    </tr>
                    <c:choose>
                        <c:when test="${list.size() > 0}">
                            <c:forEach items="${list}" var="line" varStatus="st">
                                <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                    <td nowrap align="left">${line.permissionId}</td>
                                    <td nowrap align="left">${line.nick}</td>
                                    <td nowrap align="left" <c:choose>
                                        <c:when test="${line.status.code == 'valid'}">
                                            style="color: #008000;"
                                        </c:when>
                                        <c:otherwise>
                                            style="color: #ff0000;"
                                        </c:otherwise>
                                    </c:choose>><fmt:message key="client.line.status.${line.status.code}"
                                                             bundle="${def}"/></td>
                                    <td nowrap align="left"><fmt:formatDate value="${line.createTime}"
                                                                            pattern="yyyy-MM-dd HH:mm:ss"/>/${line.createUserId}
                                    </td>
                                    <td nowrap align="left">
                                        <c:choose>
                                            <c:when test="${line.status.code == 'valid'}">
                                                <a href="/comment/manage/delete?id=${line.permissionId}">删除</a>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="/comment/manage/create?nick=${line.nick}">恢复</a>
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
                                <pg:pager url="/comment/manage/list"
                                          items="${page.totalRows}" isOffset="true"
                                          maxPageItems="${page.pageSize}"
                                          export="offset, currentPageNumber=pageNumber" scope="request">
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