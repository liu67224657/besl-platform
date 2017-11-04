<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>新游戏预告管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
    <td height="22" class="page_navigation_td">>> 运营管理 >> 条目管理 >> 新游戏预告管理</td>
</tr>
<tr>
    <td height="100%" valign="top"><br>
        <table border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td class="list_table_header_td">新游戏城市列表</td>
            </tr>
        </table>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <c:if test="${fn:length(errorMsg)>0}">
                <tr>
                    <td height="1" colspan="3" class="error_msg_td">
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
                    <a href="/gameresource/city/list"><input type="button" name="button" class="default_button" value="全部查询"/></a>
                    <a href="/gameresource/city/list?ispreset=true"><input type="button" name="button" class="default_button" value="预置城市查询"/></a>
                    <a href="/gameresource/city/createpage"><input type="button" name="button" class="default_button" value="添加城市"/></a>
                </td>
            </tr>
        </table>
        <form action="/gameresource/city/list" method="post">
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td colspan="3" height="1" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="left" width="">城市名称</td>
                    <td nowrap align="left" width="">是否预置</td>
                    <td nowrap align="left" width="">操作</td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="city" varStatus="st">
                            <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap>${city.cityName}</td>
                                <td nowrap>
                                    <c:choose>
                                        <c:when test="${city.isPreset}">是</c:when>
                                        <c:otherwise>否</c:otherwise>
                                    </c:choose>
                                </td>
                                <td nowrap>
                                    <p:privilege name="/gameresource/city/modifypage">
                                        <a href="/gameresource/city/modifypage?cityid=${city.cityId}">编辑</a>
                                    </p:privilege>
                                    <p:privilege name="/gameresource/city/delete">
                                        <a href="/gameresource/city/delete?cityid=${city.cityId}">删除</a>
                                    </p:privilege>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="3" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="3" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="3" height="1" class="default_line_td"></td>
                </tr>
            </table>
        </form>
    </td>
</tr>
</table>
</body>
</html>