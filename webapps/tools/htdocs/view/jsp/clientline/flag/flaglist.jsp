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
        $().ready(function() {
            $('#form_submit').bind('submit', function() {


            });
        });
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 手游画报 >> 着迷大端flag标志管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">大端flag标志列表</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="13" class="error_msg_td">
                            <fmt:message key="${errorMsg}" bundle="${error}"/>
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table>
                <tr>
                    <td>
                        <form method="post" action="/clientline/flag/list">
                            <table>
                                <tr>
                                    <td height="1" class="default_line_td">
                                        选择分类:
                                    </td>
                                    <td height="1">
                                        <select name="flagtype" id="select_type">
                                            <option value="">请选择</option>
                                            <c:forEach items="${flagTypes}" var="type">
                                                <option value="${type.code}"
                                                        <c:if test="${type.code == flagType}">selected="selected"</c:if>>
                                                    <fmt:message key="client.flag.type.${type.code}" bundle="${def}"/></option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td>
                                        <p:privilege name="/clientline/flag/list">
                                        <input type="submit" name="button" class="default_button" value="查询"/>
                                        </p:privilege>
                                    <td>
                                <tr>
                            </table>
                        </form>
                    </td>
                    <c:if test="${flagType != null}">
                    <td>
                        <form method="post" action="/clientline/flag/createpage">
                            <table>
                                <tr>
                                    <td>
                                        <input type="hidden" name="flagtype" id="input_hidden_flagtype" value="${flagType}"/>
                                        <p:privilege name="/clientline/flag/createpage">
                                        <input type="submit" name="button" class="default_button" value="添加一个标志"/>
                                        </p:privilege>
                                    <td>
                                <tr>
                            </table>
                        </form>
                    </td>
                    </c:if>
                </tr>
            </table>
            <form action="/clientline/flag/list" method="post">
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
                        <td nowrap align="left">描述</td>
                        <td nowrap align="left">lineID或platform</td>
                        <td nowrap align="left">LineCode</td>
                        <td nowrap align="left">子元素ID最大值</td>
                        <td nowrap align="left">类型</td>
                        <td nowrap align="left">状态</td>
                        <td nowrap align="left">创建信息</td>
                        <td nowrap align="left">修改信息</td>
                        <td nowrap align="left">操作</td>
                    </tr>
                    <tr>
                        <td height="1" colspan="13" class="default_line_td"></td>
                    </tr>
                    <c:choose>
                        <c:when test="${list.size() > 0}">
                            <c:forEach items="${list}" var="flag" varStatus="st">
                                <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                    <td nowrap align="left">${flag.flagId}</td>
                                    <td nowrap align="left">${flag.flagDesc}</td>
                                    <td nowrap align="left">${flag.lineId}</td>
                                    <td nowrap align="left">${flag.lineCode}</td>
                                    <td nowrap align="left">${flag.maxItemId}</td>
                                    <td nowrap align="left"><fmt:message key="client.flag.type.${flag.clientLineFlagType.code}" bundle="${def}"/></td>
                                    <td nowrap align="left" <c:choose>
                                            <c:when test="${flag.validStatus.code == 'valid'}">
                                                  style="color: #008000;"
                                            </c:when>
                                            <c:otherwise>
                                                style="color: #ff0000;"
                                            </c:otherwise>
                                        </c:choose>><fmt:message key="client.flag.status.${flag.validStatus.code}" bundle="${def}"/></td>
                                    <td nowrap align="left">
                                        <fmt:formatDate value="${flag.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/><br/>
                                        ${flag.createUserId}<br/>
                                        ${flag.createIp}
                                    </td>
                                    <td nowrap align="left">
                                        <fmt:formatDate value="${flag.modifyDate}" pattern="yyyy-MM-dd HH:mm:ss"/><br/>
                                        ${flag.modifyUserId}<br/>
                                        ${flag.modifyIp}
                                    </td>
                                    <td nowrap align="left">
                                        <a href="/clientline/flag/modifypage?flagid=${flag.flagId}">编辑</a>
                                        <%--<c:choose>
                                            <c:when test="${flag.validStatus.code == 'valid'}">
                                                <a href="/clientline/flag/delete?flagid=${flag.flagId}">删除</a>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="/clientline/flag/recover?flagid=${flag.flagId}">激活</a>
                                            </c:otherwise>
                                        </c:choose>--%>
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
                                <pg:pager url="/clientline/flag/list"
                                          items="${page.totalRows}" isOffset="true"
                                          maxPageItems="${page.pageSize}"
                                          export="offset, currentPageNumber=pageNumber" scope="request">
                                    <pg:param name="flagtype" value="${flagType}"/>
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