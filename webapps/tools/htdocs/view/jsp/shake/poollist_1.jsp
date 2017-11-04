<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>添加配置</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
        });
    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷手游画报管理 >> 玩霸摇一摇配置列表</td>
    </tr>
    <tr>
        <td valign="top"><br/>
            <%@ include file="shake_pool_header.jsp" %>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="13" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center">值</td>
                    <td nowrap align="center">操作</td>
                </tr>
                <c:forEach var="item" items="${list}" varStatus="st">
                    <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                        <td nowrap align="center"><c:forEach items="${gameDBList}" var="gameDB"><c:if test="${''+gameDB.gameDbId == item.directId}">${gameDB.gameName}</c:if></c:forEach>(${item.directId})</td>
                        <td nowrap align="center">
                            <a href="/shake/type/remove?configid=${config.configId}&type=${cfg.key}">删除</a>&nbsp;&nbsp;&nbsp;
                        </td>
                    </tr>
                </c:forEach>
                <tr>
                    <td height="1" colspan="13" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="10">
                            <LABEL>
                                <pg:pager url="/shake/pool/list"
                                          items="${page.totalRows}" isOffset="true"
                                          maxPageItems="${page.pageSize}"
                                          export="offset, currentPageNumber=pageNumber" scope="request">
                                    <pg:param name="type" value="${type}"/>
                                    <pg:param name="configid" value="${config.configId}"/>
                                    <pg:param name="currentPageNumber" value="${page.curPage}"/>
                                    <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                    <pg:param name="items" value="${page.totalRows}"/>
                                    <%@ include file="/WEB-INF/jsp/toolspgnoincludejquery.jsp" %>
                                </pg:pager>
                            </LABEL>
                        </td>
                    </tr>
                </c:if>
            </table>
        </td>
    </tr>

</table>

</body>
</html>