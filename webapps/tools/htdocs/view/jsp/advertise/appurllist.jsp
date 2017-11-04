<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>跳转链接管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 广告管理 >> APP跳转链接管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr width="100%">
                <td class="list_table_header_td">跳转链接</td>
                </tr>
            </table>
            <table>
                <tr>
                    <form action="/advertise/appurl/list" mothed="post">
                        <td class="list_table_header_td" width="600px">
                            APP跳转链接平成:<input type="text" name="searchname" value="${searchname}"/>
                            <input type="submit" value="查询"/> （模糊搜索）
                        </td>
                    </form>
                    <form method="post" action="/advertise/appurl/createpage">
                        <td class="list_table_header_td">
                            <input type="submit" name="button" value="添加链接"/>
                        </td>
                    </form>
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
                    <td nowrap align="center" width="100">名称</td>
                    <td nowrap align="center" width="100">访问链接</td>
                    <td nowrap align="center" width="200">IOS链接</td>
                    <td nowrap align="center" width="200">ANDROID链接</td>
                    <td nowrap align="center" width="120">创建时间</td>
                    <td nowrap align="center" width="80">创建IP</td>
                    <td nowrap align="center" width="60">可用状态</td>
                    <td nowrap align="center" width="80">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="9" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="advertise" varStatus="st">
                            <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap align="center" width="100">${advertise.codeName}</td>
                                <td nowrap align="center" width="100">${URL_WWW}/appclick/${advertise.code}</td>
                                <td nowrap align="center" width="200">${advertise.iosUrl}</td>
                                <td nowrap align="center" width="200">${advertise.androidUrl}</td>

                                <td nowrap align="center" width="120">${advertise.createTime}</td>
                                <td nowrap align="center" width="80">${advertise.createIp}</td>
                                <td nowrap align="center" width="60"><fmt:message
                                        key="joymeapp.menu.status.${advertise.removeStatus.code}"
                                        bundle="${def}"/>
                                </td>
                                <td nowrap align="center" width="80">
                                    <a href="/advertise/appurl/modifypage?clientId=${advertise.clientUrlId}&code=${advertise.code}">编辑</a>
                                    <c:choose>
                                        <c:when test="${advertise.removeStatus.code!='n'}">
                                            <a href="/advertise/appurl/recover?clientId=${advertise.clientUrlId}&code=${advertise.code}">恢复</a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="/advertise/appurl/delete?clientId=${advertise.clientUrlId}&code=${advertise.code}">删除</a>
                                        </c:otherwise>
                                    </c:choose>

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
                            <pg:pager url="/advertise/appurl/list"
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
        </td>
    </tr>
</table>
</body>
</html>