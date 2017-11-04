<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>新手游画报--热门轮播图和自定义榜管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.min.js"></script>
    <script>
        $().ready(function () {
        });
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷手游画报管理 >> 轮播图自定义管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">新手游画报--热门轮播图和自定义榜</td>
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
                    <td nowrap align="left">名称</td>
                    <td nowrap align="left">编码</td>
                    <td nowrap align="left">类型</td>
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
                                <td nowrap align="left">${line.lineId}</td>
                                <td nowrap align="left"><a
                                        href="/gameclient/clientline/custom/itemlist?lineId=${line.lineId}&lineName=${line.lineName}&lineCode=${line.code}">${line.lineName}</a>
                                </td>
                                <td nowrap align="left">${line.code}</td>
                                <td nowrap align="left"><fmt:message key="client.item.type.${line.itemType.code}"
                                                                     bundle="${def}"/></td>
                                <td nowrap align="left" <c:choose>
                                    <c:when test="${line.validStatus.code == 'valid'}">
                                        style="color: #008000;"
                                    </c:when>
                                    <c:otherwise>
                                        style="color: #ff0000;"
                                    </c:otherwise>
                                </c:choose>><fmt:message key="client.line.status.${line.validStatus.code}"
                                                         bundle="${def}"/></td>
                                <td nowrap align="left"><fmt:formatDate value="${line.createDate}"
                                                                        pattern="yyyy-MM-dd HH:mm:ss"/>/${line.createUserid}</td>
                                <td nowrap align="left">
                                        <%--<a href="/gameclient/clientline/modifypage?lineId=${line.lineId}">编辑</a>--%>
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
                            <pg:pager url="/gameclient/clientline/custom/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                <pg:param name="items" value="${page.totalRows}"/>
                                <%@ include file="/WEB-INF/jsp/toolspgwithnewversionjquery.jsp" %>
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