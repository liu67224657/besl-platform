<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台数据管理、查看组对应权限</title>
    <link href="/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/include/js/default.js"></script>
</head>

<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 管理员管理 >> 查看组对应权限</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">查看组对应权限</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="4" class="default_line_td"></td>
                </tr>
                <s:iterator value="adminRoles" id="r" status="st">
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">
                            <s:property value="#r.name"/>：
                        </td>
                        <td class="edit_table_value_td">
                            <s:iterator value="#r.privilages" id="v" status="st">
                                <s:property value="#v.name"/><br/>
                            </s:iterator>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" colspan="4" class="default_line_td"></td>
                    </tr>
                </s:iterator>
            </table>
        </td>
    </tr>
</table>
</body>
</html>