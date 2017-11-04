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
<c:if test="${fn:length(errorMsg)>0}">
    <b style="color:red;"><fmt:message key="${errorMsg}" bundle="${error}"/></b>
</c:if>

<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">添加管理员</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <form action="/gameresource/privacy/modify" method="POST">
                    <input name="resid" type="hidden" value="${resid}" />
                    <input name="uno" type="hidden" value="${uno}" />
                    <tr>
                        <td height="1" colspan="4" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">用户昵称：</td>
                        <td class="edit_table_value_td">
                            <input name="screenname" type="text" class="default_input_singleline" size="24" maxlength="50" value="${privacy.screenName}" disabled="true" />
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">选择权限：</td>
                        <td nowrap class="edit_table_value_td">
                            <select name="level">
                                <c:forEach items="${privacyLevelList}" var="privacyLevel">
                                    <option value="${privacyLevel.code}" <c:if test="${level eq privacyLevel.code}">selected="selected"</c:if>>
                                        <fmt:message key="def.gameres.privacy.${privacyLevel.code}.name" bundle="${def}"/>
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
