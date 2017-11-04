<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>着迷彩票</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>

</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 着迷彩票 >> 彩票管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">着迷彩票管理</td>
                    <td class="list_table_header_td">
                        <form method="post" action="/ticket/menu/createpage">
                            <input type="submit" name="button" value="添加彩票信息"/>
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
                    <td height="1" colspan="11" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="left">编号</td>
                    <td nowrap align="left">彩票名称</td>
                    <td nowrap>中奖几率</td>
                    <td nowrap align="center">奖品最大等级</td>
                    <td nowrap align="center">彩票类别</td>
                    <td nowrap align="center">开始时间</td>
                    <td nowrap align="center">结束时间</td>
                    <td nowrap align="center">创建ID/创建IP</td>
                    <td nowrap align="center">最后修改ID/最后修改IP</td>
                    <td nowrap align="center">状态</td>
                    <td nowrap align="center">编辑</td>
                </tr>
                <tr>
                    <td height="1" colspan="11" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="app" varStatus="st">
                            <tr align="center"
                                class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap align="left" name="">
                                        ${app.ticketId}
                                </td>

                                <td nowrap align="left">
                                        ${app.ticketName}
                                </td>
                                <td nowrap align="left">
                                        ${app.base_rate}
                                </td>
                                <td nowrap>
                                        ${app.awardLevelCount}
                                </td>
                                <td nowrap>
                                    <fmt:message key="ticket.wintype.status.${app.win_type}"
                                                 bundle="${def}"/>
                                </td>
                                <td nowrap>
                                        ${app.start_time}
                                </td>
                                <td nowrap>
                                        ${app.end_time}
                                </td>
                                <td nowrap>
                                        ${app.createUserid}/${app.createIp}
                                </td>
                                <td nowrap>
                                        ${app.lastModifyUserid}/${app.lastModifyIp}
                                </td>
                                <td nowrap>
                                    <fmt:message key="ticket.valid.status.${app.validStatus.code}"
                                                 bundle="${def}"/>
                                </td>
                                <td nowrap>
                                    <a href="/ticket/menu/modifypage?ticketId=${app.ticketId}">编辑</a>
                                    <a href="/ticket/menu/activate?ticketid=${app.ticketId}&validstatus=${app.validStatus.code}">激活</a>
                                    <a href="/ticket/menu/delete?ticketid=${app.ticketId}&validstatus=${app.validStatus.code}">删除</a>
                                </td>

                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="11" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="11" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="11" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="11">
                            <pg:pager url="/ticket/menu/list"
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