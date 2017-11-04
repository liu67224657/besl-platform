<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台数据管理、管理员列表</title>
    <link href="/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/include/js/default.js"></script>
</head>

<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 管理员管理 >> 管理员列表查询</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">管理员列表查询</td>
                </tr>
            </table>

            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>

            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr class="list_table_title_tr">
                    <td nowrap align="center">NO</td>
                    <td nowrap align="center">帐号</td>
                    <td nowrap align="center">姓名</td>
                    <td nowrap align="center">承担角色</td>
                    <td nowrap align="center">超级管理员</td>
                    <td nowrap align="center">创建人帐号</td>
                    <td nowrap align="center">创建人时间</td>
                    <td nowrap align="center">管理员状态</td>
                    <td width="120" align="center" nowrap>操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="9" class="default_line_td"></td>
                </tr>
                    <s:iterator id="admin" value="pageAdminRows.rows" status="st">
                        <tr class="<s:if test="#st.index % 2 == 0">list_table_opp_tr</s:if><s:else>list_table_even_tr</s:else>">
                            <td align="center">${st.index + 1}</td>
                            <td align="center"><s:property value="#admin.loginName"/></td>
                            <td align="center"><s:property value="#admin.trueName"/></td>
                            <td align="left">
                            	<s:iterator value="#admin.roles" id="adminrole">
                            		<s:property value="#adminrole.name"/>,
                            	</s:iterator> 
                            </td>
                            <td align="center">
                            	<s:if test="!#admin.superAdmin" >否</s:if>
                            	<s:if test="#admin.superAdmin" >是</s:if>
                            </td>
                            <td align="center">
                                <s:property value="#admin.createLoginName"/>
                            </td>
                            <td align="center">
                                <s:date name="#admin.createDate" format="yyyy-MM-dd HH:mm:ss"/>
                            </td>
                            <td align="center">
                            	<s:if test='#admin.status.code eq "valid"'>有效</s:if>
                            	<s:if test='#admin.status.code eq "invalid"'>无效</s:if>
                                <!--<s:text name="def.valid.status.%{#admin.status.code}.name"/>-->
                            </td>
                            <td align="center"><a href="/admin/editadminpage.cgi?adminUno=${admin.adminUno }">修改</a> | <a href="/admin/viewadminpage.cgi?adminUno=${admin.adminUno }">详情</a>
                        </tr>
                    </s:iterator>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</body>
</html>