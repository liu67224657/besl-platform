<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>分配角色</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script>
        function checkSubmit() {
            if ($("#role").val() == '') {
                alert("请选择角色");
                return false;
            }
        }

    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 条目管理 >> 分配角色</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">分配角色</td>
                </tr>
                <tr>
                    <td class="list_table_header_td"><c:if test="${empty roleNull}"><span
                            style="color:red">${roleNull}</span></c:if></td>
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
            <form action="/group/profile/create" method="post" onsubmit="return checkSubmit();">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td">
                            <input type="hidden" name="groupid" value="${groupid}"/>
                            <input type="hidden" name="groupprofileid"
                                   value="${groupProfile.groupProfileId}"/>
                            <input type="hidden" name="uno" value="${uno}"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            小组:
                        </td>
                        <td height="1">
                            <input type="text" name="groupname" id="input_text_groupname" value="${group.resourceName}"
                                   disabled="disabled"/>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            成员昵称:
                        </td>
                        <td height="1">
                            <input type="text" name="screenname" value="${screenName}"
                                   disabled="disabled"/>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            请选择成员角色:
                        </td>
                        <td height="1">
                            <select name="roleid" id="role">
                                <option value="">请选择</option>
                                <c:forEach items="${list}" var="role">
                                    <option value="${role.roleId}" <c:if
                                            test="${groupProfile.roleLevel.code==role.roleLevel.code}">selected="" </c:if>>
                                        ${role.roleName}</option>
                                </c:forEach>
                            </select>
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