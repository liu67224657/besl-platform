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
    <title>后台数据管理、添加新管理员</title>
    <link href="/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/include/js/default.js"></script>
</head>

<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>

        <td height="22" class="page_navigation_td">>> 管理员管理 >> 添加新的管理员</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">添加新管理员</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <form action="/admin/createadmin.cgi" method="POST">
                    <tr>
                        <td colspan="4" class="error_msg_td"><s:text name="%{msg}"></s:text></td>
                    </tr>
                    <tr>
                        <td height="1" colspan="4" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">管理员帐号：</td>
                        <td class="edit_table_value_td">
                            <input name="loginName" type="text" class="default_input_singleline" id="loginName" value="<s:property value='loginName'/>" size="24" maxlength="32">
                            <s:text name="%{formErrorsMap.get('loginName')}"></s:text>
                        </td>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">管理员姓名：</td>
                        <td nowrap class="edit_table_value_td">
                            <input name="trueName" type="text" class="default_input_singleline" size="24" maxlength="64" id="trueName" value="<s:property value='truename'/>">
                            <s:text name="%{formErrorsMap.get('name')}"></s:text>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" colspan="4" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">密码：</td>
                        <td nowrap class="edit_table_value_td">
                            <input name="loginPwd" type="password" class="default_input_singleline" size="24" maxlength="64" id="loginPwd" value="">
                            <s:text name="%{formErrorsMap.get('name')}"></s:text>
                        </td>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">确认密码：</td>
                        <td nowrap class="edit_table_value_td">
                            <input name="reLoginPwd" type="password" class="default_input_singleline" id="reLoginPwd" value="" size="24" maxlength="32">
                            <s:text name="%{formErrorsMap.get('loginName')}"></s:text>
                        </td>
                    </tr>
                    <tr>
                        <td align="right" class="edit_table_defaulttitle_td">是否超级管理员：</td>
                        <td nowrap class="edit_table_value_td">
                            <input name="superAdmin" type="checkbox" id="superAdmin" value="true"
                                   <s:if test="superAdmin==1">checked</s:if>>
                            [选中为超级管理员]
                        </td>
                        <td align="right" class="edit_table_defaulttitle_td">是否有效：</td>
                        <td nowrap class="edit_table_value_td">
                            <input name="validStatus" type="checkbox" id="validStatus" value="valid"
                                   <s:if test="adminStats eq 'valid' ">checked</s:if>>
                            [选中为有效]
                        </td>
                    </tr>
                    <tr>
                        <td height="1" colspan="4" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td align="right" class="edit_table_defaulttitle_td">担任的角色：</td>
                        <td colspan="3" class="edit_table_value_td">
                            <s:iterator id="d" value="adminRoles" status="st">
                                <input name="adminroles" type="checkbox" id="departments${st.index+1 }" value="<s:property value='#d.code'/>"><s:property value='#d.name'/>
                                <s:if test="(#st.index+1)%5==0"><br/></s:if>
                            </s:iterator>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" colspan="4" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td align="right" class="edit_table_defaulttitle_td">备注：</td>
                        <td colspan="3" class="edit_table_value_td">
                            <textarea name="description" cols="85" rows="4" class="default_input_multiline"></textarea>
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