<%@ include file="/view/jsp/common/taglibs.jsp" %>
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
    <title>后台数据管理、修改个人密码</title>
    <script language="JavaScript" type="text/JavaScript">

        function back() {
            window.location.href = "/home";
        }
    </script>
</head>

<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>

        <td height="22" class="page_navigation_td">>> 管理员管理 >> 修改个人密码</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">修改个人密码</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <form action="/privilege/user/savepwd" method="POST">
                    <input id="adminUno" name="adminUno" type="hidden" value="${entity.uno}"/>
                    <tr>
                        <td colspan="4" class="default_line_td" align="center" style="color:red;">
                        </td>
                    </tr>
                    <!--
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">管理员帐号：</td>
                        <td class="edit_table_value_td">${entity.userid}</td>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">管理员姓名：</td>
                        <td nowrap class="edit_table_value_td">${entity.username}</td>
                    </tr>

                    <tr>
                        <td height="1" colspan="4" class="default_line_td"></td>
                    </tr>    -->
                    <tr>
                        <td align="right" class="edit_table_defaulttitle_td">原始密码：</td>
                        <td colspan="3" class="edit_table_value_td">
                            <input type="password" name="oldpwd" class="default_input_singleline" size="24"
                                   maxlength="64" id="oldpwd" value=""/>
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">新密码：</td>
                        <td nowrap class="edit_table_value_td">
                            <input name="newpwd" type="password" class="default_input_singleline" size="24"
                                   maxlength="64" id="newpwd" value="">
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">确认新密码：</td>
                        <td nowrap class="edit_table_value_td">
                            <input name="affirmnewpwd" type="password" class="default_input_singleline"
                                   id="affirmnewpwd" value=""
                                   size="24" maxlength="32">
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
                            <c:choose>
                                <c:when test="${result eq 'success'}">
                                    <font color="red">修改成功!</font>
                                </c:when>
                                <c:when test="${result eq 'failed'}">
                                    <font color="red">修改失败!</font>
                                </c:when>
                            </c:choose>
                            <input name="Submit" type="submit" class="default_button" value="提交">
                            <input name="Reset" type="reset" class="default_button" value="重置">
                            <input name="Reset" type="reset" class="default_button" value="返回" onclick="back();">
                        </td>
                    </tr>
                </form>
            </table>
        </td>
    </tr>
</table>

<c:if test="${isadmin == true}">
<table width="100%" height="50%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">修改他人密码</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <form action="/privilege/user/savepwd" method="POST">
                    <tr>
                        <td colspan="4" class="default_line_td" align="center" style="color:red;">
                        </td>
                    </tr>
                    <tr>
                        <td align="right" class="edit_table_defaulttitle_td">用户ID：</td>
                        <td colspan="3" class="edit_table_value_td">
                            <input type="text" name="userid" class="default_input_singleline" size="24"
                                   maxlength="64"  value=""/>
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">新密码：</td>
                        <td nowrap class="edit_table_value_td">
                            <input name="newpwd" type="password" class="default_input_singleline" size="24"
                                   maxlength="64" id="newpwd" value="">
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">确认新密码：</td>
                        <td nowrap class="edit_table_value_td">
                            <input name="affirmnewpwd" type="password" class="default_input_singleline"
                                   id="affirmnewpwd" value=""
                                   size="24" maxlength="32">
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
                            <c:choose>
                                <c:when test="${result eq 'success'}">
                                    <font color="red">修改成功!</font>
                                </c:when>
                                <c:when test="${result eq 'failed'}">
                                    <font color="red">修改失败!</font>
                                </c:when>
                            </c:choose>
                            <input name="Submit" type="submit" class="default_button" value="提交">
                            <input name="Reset" type="reset" class="default_button" value="重置">
                            <input name="Reset" type="reset" class="default_button" value="返回" onclick="back();">
                        </td>
                    </tr>
                </form>
            </table>
        </td>
    </tr>
</table>
</c:if>
</body>
</html>