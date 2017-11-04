<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台数据管理、标签页面</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script language="JavaScript" type="text/JavaScript">
        function add(menuid) {
            window.location.href = "/joymeapp/menu/tag/createpage?menuid=" + menuid;
        }
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="100%" valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr class="toolbar_tr">
                    <td>
                        <%--<p:privilege name="/joymeapp/menu/tag/createpage">--%>
                            <input name="Submit" type="submit" class="default_button" value="添加新标签"
                                   onClick="add('${menuid}');">
                        <%--</p:privilege>--%>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="11" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="left" width="5%">标签ID</td>
                    <td nowrap align="left" width="10%">标签名称</td>
                    <td nowrap align="left" width="10%">状态</td>
                    <td nowrap align="center" width="10%">创建人</td>
                    <td nowrap align="center" width="10%">创建IP</td>
                    <td nowrap align="center" width="10%">创建时间</td>
                    <td nowrap align="center" width="10%">排序</td>
                    <td nowrap align="center" width="10%">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="11" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${taglist.size() > 0}">
                        <input type="hidden" name="categoryId" value="">
                        <c:forEach items="${taglist}" var="tag" varStatus="st">
                            <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr </c:when><c:otherwise>list_table_even_tr </c:otherwise></c:choose>">
                                <td nowrap>${tag.tagId}</td>
                                <td nowrap>${tag.tagName}</td>
                                <td nowrap><fmt:message key="joymeapp.menu.status.${tag.removeStatus.getCode()}"
                                                        bundle="${def}"/></td>
                                <td nowrap>${tag.createId}</td>
                                <td nowrap>${tag.createIp}</td>
                                <td nowrap>${tag.createDate}</td>
                                <td nowrap>
                                    <a href="/joymeapp/menu/tag/sort/up?tagid=${tag.tagId}"><img
                                            src="/static/images/icon/up.gif"></a>

                                    <a href="/joymeapp/menu/tag/sort/down?tagid=${tag.tagId}"><img
                                            src="/static/images/icon/down.gif"></a>
                                </td>
                                <td nowrap>
                                    <a href="/joymeapp/menu/tag/modifypage?tagid=${tag.tagId}">编辑</a>
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
            </table>
        </td>
    </tr>
</table>
</body>
</html>