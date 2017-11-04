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
    <title>后台数据管理、修改个人密码</title>
    <link href="/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/include/js/default.js"></script>

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
                <form action="/admin/changeloginpwd.cgi" method="POST">
                    <input id="adminUno" name="adminUno" type="hidden" value="${adminUno}"/>
                    <tr>
                        <td colspan="4" class="default_line_td" align="center" style="color:red;">
                            <s:text name="%{msg}"></s:text>
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">管理员帐号：</td>
                        <td class="edit_table_value_td">${admin.loginName}</td>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">管理员姓名：</td>
                        <td nowrap class="edit_table_value_td">${admin.trueName}</td>
                    </tr>
                    <tr>
                        <td height="1" colspan="4" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td align="right" class="edit_table_defaulttitle_td">原始密码：</td>
                        <td colspan="3" class="edit_table_value_td">
                            <input type="password" name="origLoginPwd" class="default_input_singleline" size="24"
                                   maxlength="64" id="origLoginPwd" value=""/>
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">新密码：</td>
                        <td nowrap class="edit_table_value_td">
                            <input name="newLoginPwd" type="password" class="default_input_singleline" size="24"
                                   maxlength="64" id="newLoginPwd" value="">
                        </td>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">确认新密码：</td>
                        <td nowrap class="edit_table_value_td">
                            <input name="renewLoginPwd" type="password" class="default_input_singleline" id="renewLoginPwd" value=""
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
                            <input name="Submit" type="submit" class="default_button" value="提交">
                            <input name="Reset" type="reset" class="default_button" value="重置">
                        </td>
                    </tr>
                </form>
            </table>
        </td>
    </tr>
</table>
</body>
</html>