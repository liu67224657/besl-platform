<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
 <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <title>后台数据管理、修改编辑人员</title>

    <script language="JavaScript" type="text/JavaScript">

    </script>
</head>

<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <c:if test="${errorMsgMap.containsKey('system')}">
        <fmt:message key="${errorMsgMap['system']}" bundle="${error}"></fmt:message>
    </c:if>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">修改编辑人员</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <form action="/viewline/editprivacy" method="POST">
                    <input name="categoryId" type="hidden" value="${categoryId}">
                    <input name="uno" type="hidden" value="${uno}">
                    <%--<input name="lineId" type="hidden" value="${lineId}">--%>
                    <tr>
                        <td height="1" colspan="4" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">用户昵称：</td>
                        <td class="edit_table_value_td">
                            <input name="editor" type="text" class="default_input_singleline"
                                   size="24" maxlength="50" value="${privacy.profileBlog.screenName}" disabled="disabled">
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">请选择隐私种类：</td>
                        <td nowrap class="edit_table_value_td">
                            <select name="privacy">
                                <c:forEach items="${privacyLevels}" var="privacyLevel">
                                    <option value="${privacyLevel.code}" <c:if test="${privacy.categoryPrivacy.privacyLevel.code eq privacyLevel.code}">selected="selected"</c:if>>
                                        <fmt:message key="def.viewline.category.privacy.${privacyLevel.code}.name" bundle="${def}"/>
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" colspan="4" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td colspan="4">&nbsp;</td>
                    </tr>
                    <tr align="center">
                        <td colspan="4">
                            <input name="Submit" type="submit" class="default_button" value="提交">
                            <input name="Reset" type="reset" class="default_button" value="返回" onclick="javascript:window.history.go(-1);">
                        </td>
                    </tr>
                </form>
            </table>

        </td>
    </tr>
</table>
</body>
</html>
