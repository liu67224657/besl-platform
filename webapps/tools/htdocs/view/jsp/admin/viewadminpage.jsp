<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台数据管理、查看管理员详情</title>
    <link href="/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/include/js/default.js"></script>
</head>

<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>

        <td height="22" class="page_navigation_td">>> 管理员管理 >> 查看管理员详情</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">

                <tr>
                    <td class="list_table_header_td">查看管理员详情</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td width="120" align="right" class="edit_table_defaulttitle_td">管理员帐号：</td>
                    <td class="edit_table_value_td">
                        <s:property value='admin.loginName'/>
                    </td>
                    <td width="120" align="right" class="edit_table_defaulttitle_td">管理员姓名：</td>
                    <td nowrap class="edit_table_value_td">
                        <s:property value='admin.trueName'/>
                    </td>
                </tr>
                <tr>
                    <td height="1" colspan="4" class="default_line_td"></td>
                </tr>
                <tr>
                    <td align="right" class="edit_table_defaulttitle_td">是否超级管理员：</td>
                    <td nowrap class="edit_table_value_td">
                        <s:if test="!admin.superAdmin">否</s:if>
                        <s:if test="admin.superAdmin">是</s:if>
                    </td>
                    <td align="right" class="edit_table_defaulttitle_td">是否有效：</td>
                    <td nowrap class="edit_table_value_td">
                        <s:if test="admin.validStatus.code eq 'valid' ">是</s:if>
                        <s:if test="admin.validStatus.code eq 'invalid' ">否</s:if>
                    </td>
                </tr>
                <tr>
                    <td height="1" colspan="4" class="default_line_td"></td>
                </tr>
                <tr>
                    <td align="right" class="edit_table_defaulttitle_td">担任的角色：<s:property value="adminroles"/></td>
                    <td colspan="3" class="edit_table_value_td">
                        <s:iterator id="d" value="admin.roles" status="st">
                            <s:property value="#d.name"/><s:if test="#st.isLast()"></s:if><s:else> | </s:else>
                            <s:if test="(#st.index+1)%5==0"><br/></s:if>
                        </s:iterator>
                    </td>
                </tr>
                <tr>
                    <td height="1" colspan="4" class="default_line_td"></td>
                </tr>
                <tr>
                    <td align="right" class="edit_table_defaulttitle_td">创建人帐号：</td>
                    <td nowrap class="edit_table_value_td">
                        <s:property value="admin.createLoginName"/>
                    </td>
                    <td align="right" class="edit_table_defaulttitle_td">创建时间：</td>
                    <td nowrap class="edit_table_value_td">
                        <s:date name="admin.createDate" format="yyyy-MM-dd HH:mm:ss"/>
                    </td>
                </tr>
                <tr>
                    <td height="1" colspan="4" class="default_line_td"></td>
                </tr>
                <tr>
                    <td align="right" class="edit_table_defaulttitle_td">最后修改人帐号：</td>
                    <td nowrap class="edit_table_value_td">
                        <s:property value="admin.updateLoginName"/>
                    </td>
                    <td align="right" class="edit_table_defaulttitle_td">最后修改时间：</td>
                    <td nowrap class="edit_table_value_td">
                        <s:date name="admin.updateDate" format="yyyy-MM-dd HH:mm:ss"/>
                    </td>
                </tr>
                <tr>
                    <td height="1" colspan="4" class="default_line_td"></td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</body>
</html>