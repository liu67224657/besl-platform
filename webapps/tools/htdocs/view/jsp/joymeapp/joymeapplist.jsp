<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>着迷APP管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷APP >> APP管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">着迷APP管理</td>
                </tr>
            </table>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">名称</td>
                    <td>
                        <form method="post" action="/joymeapp/app/list">
                            <input type="text" name="appname" value=""/>
                            <input type="submit" name="button" value="查询"/>
                        </form>
                    </td>
                    <td class="list_table_header_td">
                        <form method="post" action="/joymeapp/app/createpage">
                            <input type="submit" name="button" value="添加APP"/>
                        </form>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="9" class="error_msg_td">
                            <fmt:message key="${errorMsg}" bundle="${error}"/>
                        </td>
                    </tr>
                </c:if>

                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>

            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="9" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="left" width="200">APPKEY</td>
                    <td nowrap align="left" width="200" style="display: none;">APP密钥</td>
                    <td nowrap width="60">APP名称</td>
                    <td nowrap width="60">APP平台</td>
                    <td nowrap align="center" width="60">APP类型</td>
                    <td nowrap align="center" width="100">APP描述</td>
                    <td nowrap align="center">我的模块</td>
                    <td nowrap align="center" width="60">可用状态</td>
                    <td nowrap align="center" width="120">创建时间</td>
                    <td nowrap align="center" width="80">创建IP</td>
                    <td nowrap align="center" width="80">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="9" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="app" varStatus="st">
                            <tr class="<c:choose><c:when test="
                            ${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap align="left" name="app.appname">
                                        ${app.appId}
                                </td>
                                <td nowrap align="left"  style="display: none;">
                                        ${app.appKey}
                                </td>
                                <td nowrap>
                                        ${app.appName}
                                </td>
                                <td nowrap>
                                    <fmt:message key="joymeapp.platform.${app.platform.code}" bundle="${def}"/>
                                </td>
                                <td nowrap>
                                    <fmt:message key="def.app.type.${app.appType.code}" bundle="${def}"/>
                                </td>
                                <td nowrap>
                                        ${app.appDetail}
                                </td>
                                <td nowrap>
                                    <c:choose>
                                        <c:when test="${app.displayMy == 0}">
                                            不显示
                                        </c:when>
                                        <c:otherwise>
                                            显示
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td nowrap>
                                    <fmt:message key="def.app.validstatus.${app.validStatus.code}" bundle="${def}"/>
                                </td>
                                <td nowrap>
                                    <fmt:formatDate value="${app.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </td>
                                <td nowrap>
                                        ${app.createIp}
                                </td>
                                <td nowrap>
                                    <a href="/joymeapp/app/modifypage?appid=${app.appId}">编辑</a>
                                    <a href="/joymeapp/app/delete?appid=${app.appId}">删除</a>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="9" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="9" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="9" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="9">
                            <pg:pager url="/joymeapp/app/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="resourceName" value="${resourceName}"/>
                                <pg:param name="removeStatusCode" value="${removeStatusCode}"/>
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