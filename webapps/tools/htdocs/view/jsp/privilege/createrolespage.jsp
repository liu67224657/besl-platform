<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="s" uri="/struts-tags" %>
    <%
        //remove cache
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
    %>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <title>后台数据管理、添加新管理员</title>

    <script language="JavaScript" type="text/JavaScript">

        function back() {
            window.location.href = "/privilege/roles/roleslist";
        }
    </script>
</head>

<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">系统管理 >> 权限管理 >> 操作角色管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">添加角色</td>
                    <td align="left"><c:choose>
                        <c:when test="${message == 0}">
                            <b><font color="red"> 保存失败,该角色已经存在！</font></b>
                        </c:when>
                    </c:choose>
                    </td>
                </tr>

            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <form action="/privilege/roles/saverolespage" method="POST">
                    <tr>
                        <td height="1" colspan="4" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">角色名：</td>
                        <td nowrap class="edit_table_value_td">
                            <input name="rolename" type="text" class="default_input_singleline" size="24" maxlength="64"
                                   id="rolename">
                            <c:if test="${errorMsgMap.containsKey('rolename')}">
                                <fmt:message key="${errorMsgMap['rolename']}" bundle="${error}"></fmt:message>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">角色描述：</td>
                        <td nowrap class="edit_table_value_td">
                            <input name="description" type="text" class="default_input_singleline" size="24"
                                   maxlength="64"
                                   id="description">
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">角色状态：</td>
                        <td nowrap class="edit_table_value_td">
                            <select name="status" id="status">
                                <option value=""
                                        <c:if test="${entity.status.code =='' || entity.status.code == null}">selected="selected" </c:if>>--请选择--</option>
                                <option value="y"
                                        <c:if test="${entity.status.code =='y'}">selected="selected" </c:if>>可用</option>
                                <option
                                        <c:if test="${entity.status.code =='n'}">selected="selected" </c:if> value="n">停用</option>
                            </select>
                            <c:if test="${errorMsgMap.containsKey('status')}">
                                <fmt:message key="${errorMsgMap['status']}" bundle="${error}"></fmt:message>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">角色类型：</td>
                        <td nowrap class="edit_table_value_td">
                            <select name="type" id="type">
                                <option value="">--请选择--</option>
                                <option value="1">角色1</option>
                                <option value="2">角色2</option>
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
                            <input name="Reset" type="reset" class="default_button" value="返回" onclick="back();">
                        </td>
                    </tr>
                </form>
            </table>

        </td>
    </tr>
</table>
</body>
</html>