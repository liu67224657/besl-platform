<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <title>后台数据管理-专题元素列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script src="/static/include/js/jquery.js" type="text/javascript"></script>
    <script language="JavaScript" type="text/JavaScript">
        function add(specialId) {
            window.location.href = "/viewspecial/preaddspecialitem?specialId=" + specialId;
        }

        function remove(specialId, templateLocationCode, itemId) {
            if (window.confirm("你确定要删除该信息吗？")) {
                window.location.href = '/viewspecial/removespecialitem?specialId=' + specialId + "&templateLocationCode=" + templateLocationCode + "&itemId=" + itemId;
            }
        }
    </script>
</head>

<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="100%" valign="top">
            <c:if test="${templateLocations.size() > 0}">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr class="toolbar_tr">
                        <td>
                            <input name="Submit" type="submit" class="default_button" value="添加专题元素" onClick="add('${special.specialId}');">
                        </td>
                    </tr>
                </table>
            </c:if>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="4" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td width="100">模板位置</td>
                    <td>元素名称</td>
                    <td align="center" width="100">元素类型</td>
                    <td align="center" width="60">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="4" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${specialItems.size() > 0}">
                        <c:forEach items="${specialItems}" var="item" varStatus="st">
                            <tr class="<c:choose>
                                <c:when test="${st.index % 2 == 0}">
                                   list_table_opp_tr
                                </c:when>
                                <c:otherwise>
                                    list_table_even_tr
                                </c:otherwise>
                                </c:choose>">
                                <td>
                                        ${item.templateLocation.locationCode}-${item.templateLocation.locationName}
                                </td>
                                <td>
                                    <a href="/view${item.itemType.code}/detail${item.itemType.code}?${item.itemType.code}Id=${item.itemId}" target="_parent">
                                            ${item.itemName}
                                    </a>
                                </td>

                                <td align="center">
                                    <fmt:message key="def.viewspecial.itemtype.${item.itemType.code}.name" bundle="${def}"/>
                                </td>
                                <td align="center">
                                    <a href="#" onclick="remove('${special.specialId}','${item.templateLocation.locationCode}','${item.itemId}');">删除</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="4" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td height="1" colspan="4" class="default_line_td"></td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</form>
</body>
</html>