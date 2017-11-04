<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>账号列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript">

    </script>
</head>

<body scroll=no>
<table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td height="100%" valign="top">
      <table width="100%"  border="0" cellspacing="0" cellpadding="0">
        <tr class="toolbar_tr">
          <%--<td><input name="Submit" type="submit" class="default_button" value="Create A Sub Module" onClick="parent.location='edit.htm'"></td>--%>
            <td>&nbsp;</td>
        </tr>
      </table>
      <table width="100%"  border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="1" class="default_line_td"></td>
        </tr>
      </table>
      <table width="100%"  border="0" cellspacing="1" cellpadding="0">
        <tr class="list_table_title_tr">
          <td align="center" nowrap>ACCOUNTUNO</td>
          <td nowrap>邮箱 </td>
          <td nowrap>注册时间 </td>
          <td align="center" nowrap>注册IP</td>
          <td align="center" nowrap>登录时间 </td>
          <td align="center" nowrap>登录IP </td>
          <td align="center" nowrap>更新时间 </td>
          <td align="center" nowrap>审核者</td>
          <td align="center" nowrap>状态</td>
          <td align="center" nowrap>来自</td>
        </tr>
        <tr>
          <td height="1" colspan="10" class="default_line_td"></td>
        </tr>
        <c:forEach items="${rows}" var="account" varStatus="st">
        <tr class="list_table_opp_tr" title="System Management">
          <td align="center">${account.account.accountUno}</td>
          <td><!--<a href="view.htm" target="_parent">-->${account.account.userid}<!--</a>--> </td>
          <td>${account.account.registerDate}</td>
          <td align="center">${account.account.registerIp}</td>
          <td align="center">${account.account.loginDate}</td>
          <td align="center">${account.account.loginIp}</td>
          <td align="center">${account.account.updateDate}</td>
          <td align="center">${account.account.auditUserId}</td>
          <td align="center">${account.account.accountStatus.code}</td>
          <td align="center">${account.account.accountDomain.code}</td>
        </tr>
        </c:forEach>
        <c:if test="${rows.size() <= 0}">
            <tr>
              <td colspan="10" class="error_msg_td"><li>No Module Rows.</li></td>
            </tr>
        </c:if>

      </table>
      <table width="100%"  border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="1" class="default_line_td"></td>
        </tr>
          <tr align="center"><td><input name="Button" type="button" class="default_button" value="返回" onClick="history.back()"></td></tr>
      </table></td>
  </tr>
</table>
</body>
</html>