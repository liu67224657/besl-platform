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
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <title>后台数据管理、添加新管理员</title>
       <script language="JavaScript" type="text/JavaScript">

        function back() {
            window.location.href = "/privilege/user/userlist";
        }

        $(document).ready(function(){
            $('#form_submit').bind('submit', function () {
                var userid = $("#userid").val();
                if (userid != '' && userid.indexOf("|")>=0) {
                    alert("用户ID包含特殊字符");
                    $("#userid").focus();
                    return false;
                }
                var username = $("#username").val();
                if (username != '' && username.indexOf("|")>=0) {
                    alert("用户姓名包含特殊字符");
                    $("#username").focus();
                    return false;
                }

               // return false;
            });
        });
    </script>
</head>

<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">系统管理 >> 权限管理 >> 管理员管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">修改用户</td>
                </tr>
                <tr>
                    <td class="list_table_header_td"><div style="color: #008200">命名规则：用户ID（英文名）  用户姓名（部门_用户中文名）</div></td>
                </tr>
                <tr>
                    <td class="list_table_header_td"><div style="color: #008200">重复则在后面加数字。</div></td>
                </tr>
                <tr>
                    <td class="list_table_header_td"><div style="color: #008200">用户ID和用户姓名均不的采用特殊字符 "|"</div></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <form action="/privilege/user/edituserpage" method="POST">
                    <input name="uno" type="hidden" id="uno" value="${entity.uno}">
                    <tr>
                        <td height="1" colspan="4" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">用户ID：</td>
                        <td class="edit_table_value_td">
                            <input name="userid" type="text" class="default_input_singleline" id="userid"
                                   value="${entity.userid}"
                                   size="24" maxlength="32" readonly="true">
                        </td>

                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">用户姓名：</td>
                        <td nowrap class="edit_table_value_td">
                            <input name="username" type="text" class="default_input_singleline" size="24" maxlength="64"
                                   value="${entity.username}"
                                   id="username">
                        </td>
                    </tr>

                    <!--
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">用户密码：</td>
                        <td nowrap class="edit_table_value_td">
                            <input name="password" type="password" class="default_input_singleline" size="24"
                                   maxlength="64" value="${entity.password}"
                                   id=" password">
                        </td>
                    </tr>
                    -->

                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">用户状态：</td>
                        <td nowrap class="edit_table_value_td">
                            <select name="ustatus" id="ustatus">
                                <option value="">--请选择--</option>
                                <c:choose>
                                    <c:when test="${entity.ustatus.code=='y'}">
                                        <option value="y" selected="true">启用</option>
                                        <option value="n">停用</option>
                                    </c:when>
                                    <c:when test="${entity.ustatus.code=='n'}">
                                        <option value="y">启用</option>
                                        <option value="n" selected="true">停用</option>
                                    </c:when>

                                </c:choose>
                            </select>
                        </td>
                    </tr>
                         <!--
                     -->
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">仅限公司内网访问：</td>
                        <td nowrap class="edit_table_value_td">
                            <select name="limitLocation" >
                                <option value="">--请选择--</option>
                                <c:choose>
                                    <c:when test="${entity.limitLocation.code=='y'}">
                                        <option value="y" selected="true">是</option>
                                        <option value="n">否</option>
                                    </c:when>
                                    <c:when test="${entity.limitLocation.code=='n'}">
                                        <option value="y">是</option>
                                        <option value="n" selected="true">否</option>
                                    </c:when>

                                </c:choose>
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