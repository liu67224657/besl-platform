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
                <td class="list_table_header_td">新游戏标签列表</td>
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
                    <a href="/gameresource/newreleasetag/list"><input type="button" name="button" class="default_button" value="全部查询"/></a>
                    <a href="/gameresource/newreleasetag/list?ishot=true"><input type="button" name="button" class="default_button" value="热门标签查询"/></a>
                    <a href="/gameresource/newreleasetag/list?istop=true"><input type="button" name="button" class="default_button" value="顶部标签查询"/></a>
                    <a href="/gameresource/newreleasetag/createpage"><input type="button" name="button" class="default_button" value="添加标签"/></a>
                </td>
            </tr>
        </table>
        <form action="/gameresource/newreleasetag/list" method="post">
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td colspan="4" height="1" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="left" width="">标签名称</td>
                    <td nowrap align="left" width="">是否热门标签</td>
                    <td nowrap align="left" width="">是否顶部标签</td>
                    <td nowrap align="left" width="">操作</td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="tag" varStatus="st">
                            <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap>${tag.tagName}</td>
                                <td nowrap>
                                    <c:choose>
                                        <c:when test="${tag.isHot}">是</c:when>
                                        <c:otherwise>否</c:otherwise>
                                    </c:choose>
                                </td>
                                <td nowrap>
                                    <c:choose>
                                        <c:when test="${tag.isTop}">是</c:when>
                                        <c:otherwise>否</c:otherwise>
                                    </c:choose>
                                </td>
                                <td nowrap>
                                    <p:privilege name="/gameresource/newreleasetag/modifypage">
                                        <a href="/gameresource/newreleasetag/modifypage?tagid=${tag.newReleaseTagId}">编辑</a>
                                    </p:privilege>
                                    <p:privilege name="/gameresource/newreleasetag/delete">
                                        <a href="/gameresource/newreleasetag/delete?tagid=${tag.newReleaseTagId}">删除</a>
                                    </p:privilege>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="4" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="4" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="4" height="1" class="default_line_td"></td>
                </tr>
            </table>
        </form>
    </td>
</tr>
</table>
</body>
</html>