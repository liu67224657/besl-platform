<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>添加角色权限</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script>
        $(document).ready(function() {
            $('#form_submit').bind('submit', function() {
                var pidVal = $('#select_privilege').val();
                if (pidVal.length == 0) {
                    alert('请选择权限');
                    return false;
                }
            });
        });
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 条目管理 >> 添加角色权限</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">添加角色权限</td>
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
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/gameresource/group/role/privilege/modify" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td">
                            <input type="hidden" name="relationid" value="${relation.relationId}"/>
                            <input type="hidden" name="roleid" value="${roleId}"/>
                            <input type="hidden" name="groupid" value="${groupId}"/>
                            <input type="hidden" name="privilegetype" value="${privilegeType}"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            小组ID:
                        </td>
                        <td height="1">${dto.group.resourceId}</td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            小组名:
                        </td>
                        <td height="1">${dto.group.resourceName}</td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            角色ID:
                        </td>
                        <td height="1">${dto.role.roleId}</td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            角色名:
                        </td>
                        <td height="1">${dto.role.roleName}</td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            角色级别:
                        </td>
                        <td height="1"><fmt:message key="gameresource.group.role.level.${dto.role.roleLevel.code}" bundle="${def}"/></td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            选择权限:
                        </td>
                        <td height="1">
                            <select name="privilegeid" id="select_privilege">
                                <option value="">请选择</option>
                                <c:forEach items="${list}" var="privilege">
                                    <option value="${privilege.privilegeId}" <c:if test="${privilege.privilegeId==relation.privilegeId}">selected="selected"</c:if>>${privilege.privilegeName}</option>
                                </c:forEach>
                            </select>*必选项
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td colspan="3" height="1" class="default_line_td"></td>
                    </tr>
                    <tr align="center">
                        <td colspan="3">
                            <input name="Submit" type="submit" class="default_button" value="提交">
                            <input name="Reset" type="button" class="default_button" value="返回"
                                   onclick="javascipt:window.history.go(-1);">
                        </td>
                    </tr>
                </table>
            </form>
        </td>
    </tr>
</table>
</body>
</html>