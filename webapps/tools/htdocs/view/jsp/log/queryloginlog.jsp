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
    <title>日志管理、登录日志</title>
    <script language="JavaScript" type="text/JavaScript">

        function back() {
            window.location.href = "/log/preloginloglist";
        }
    </script>
</head>

<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 日志管理 >> 查看登录日志</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">查看登录日志</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <form action="" method="POST">
                    <tr>
                        <td height="1" colspan="4" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">登录人id：</td>
                        <td class="edit_table_value_td">
                            ${entity.userId}
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">登录时间：</td>
                        <td class="edit_table_value_td">
                            ${entity.loginTime}
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">访问IP：</td>
                        <td class="edit_table_value_td">
                            ${entity.accIp}
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">是否登录成功：</td>
                        <td nowrap class="edit_table_value_td">
                            <c:if test="${entity.success.code=='y'}"> 是 </c:if>
                            <c:if test="${entity.success.code=='n'}"> 否 </c:if>
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
                            <input name="Reset" type="reset" class="default_button" value="返回"
                                   onclick="back();">
                        </td>
                    </tr>
                </form>
            </table>

        </td>
    </tr>
</table>
</body>
</html>