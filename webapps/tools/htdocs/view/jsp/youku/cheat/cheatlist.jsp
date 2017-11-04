<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title></title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 优酷游戏中心 >> 优酷修改点赞</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="20%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">文章点赞修改</td>

                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="8" class="error_msg_td">
                            ${errorMsg}
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td height="1" colspan="8" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap width="50" align="center">文章ID</td>
                    <td nowrap width="">标题</td>
                    <td nowrap width="" align="center">阅读数</td>
                    <td nowrap width="" align="center">点赞数</td>
                    <td nowrap width="" align="center">真实阅读数</td>
                    <td nowrap width="" align="center">真实点赞数</td>

                    <td nowrap width="" >创建时间</td>
                    <td nowrap width="" align="center">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="8" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="msg" varStatus="st">
                            <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                <td nowrap align="center">${msg.dede_archives_id}</td>
                <td nowrap>
                    <c:forEach items="${archiveMap}" var="cheat">
                        <c:if test="${cheat.key==msg.dede_archives_id}">
                            <c:out value="${cheat.value.title}" />
                        </c:if>
                    </c:forEach>

                </td>
                <td nowrap align="center">${msg.read_num}</td>
                <td nowrap align="center">${msg.agree_num}</td>
                <td nowrap align="center">${msg.real_read_num}</td>
                <td nowrap align="center">${msg.real_agree_num}</td>
                <td nowrap>
                    <fmt:formatDate value="${msg.cheating_time}" pattern="yyyy-MM-dd HH:mm:ss"/>
                </td>
                <td nowrap align="center">
                  <a href="/youku/cheat/modifypage?dede_archives_id=${msg.dede_archives_id}&offset=${page.startRowIdx}">编辑</a>
                </td>
                </tr>
                </c:forEach>
                <tr>
                    <td height="1" colspan="8" class="default_line_td"></td>
                </tr>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="8" class="error_msg_td">暂无数据!</td>
                    </tr>
                </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="8" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="8">
                            <pg:pager url="/youku/cheat/list"
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