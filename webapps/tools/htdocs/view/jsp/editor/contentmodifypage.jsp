<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <title>编辑录入页面</title>
    <style type="text/css">
        td, td div {
            overflow: hidden;
            text-overflow: ellipsis; /* for IE */
            -moz-text-overflow: ellipsis; /* for Firefox,mozilla */
            white-space: nowrap;

        }
    </style>
</head>

<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="100%" valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="22" class="page_navigation_td">>> 编辑管理 >>> 文章管理 >>> 文章编辑</td>
                </tr>
            </table>
            <br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="">
                        <form action="/editor/content/page" method="post">
                            <input type="submit" value="返回" class="default_button"/>
                        </form>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">文章录入</td>
                </tr>
            </table>

            <c:if test="${fn:length(errorMsg)>0}">
                <b style="color:red;"><fmt:message key="${errorMsg}" bundle="${error}"/></b>
            </c:if>

            <form action="/editor/content/modify" method="post">
                <table width="40%" border="0" cellspacing="1" cellpadding="0">
                    <input value="${editorItem.itemNo}" type="hidden" name="itemno"/>
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td width="50" align="left" class="edit_table_defaulttitle_td">文章ID</td>
                        <td class="edit_table_value_td" width="230">${editorItem.sourceId}</td>
                        <td nowrap class="edit_table_value_td"></td>
                    </tr>
                    <tr>
                        <td width="50" align="left" class="edit_table_defaulttitle_td">文章地址</td>
                        <td class="edit_table_value_td" width="230">${editorItem.itemSrcNo}</td>
                        <td nowrap class="edit_table_value_td"></td>
                    </tr>
                    <tr>
                        <td width="50" align="left" class="edit_table_defaulttitle_td">文章类型</td>
                        <td class="edit_table_value_td">
                            <select name="subtype">
                                <option value="">请选择</option>
                                <c:forEach var="subType" items="${subTypeList}">
                                    <option value="${subType.code}" <c:if test="${editorItem.itemSubType eq subType.code}">selected</c:if>><fmt:message key="def.editor.${subType.code}.name" bundle="${def}"/></option>
                                </c:forEach>
                            </select>
                            <%--<input class="default_input_singleline" value="${editorItem.itemSubType}" name="subtype" size="40"/>--%>
                        </td>
                        <td nowrap class="edit_table_value_td"></td>
                    </tr>
                    <tr>
                        <td height="1" colspan="3" class="default_line_td" align="center">
                            <input type="submit" value="修改"/>
                        </td>
                    </tr>
                    </td>
                    </tr>
                </table>
            </form>
        </td>
    </tr>
</table>
</body>
</html>