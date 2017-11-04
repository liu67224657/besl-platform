<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>修改账号域</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript">

    </script>
</head>

<body scroll=no>
<table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0">
  <form action="/audit/profile/updatedomain" method="POST" target="_parent">
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
        <input name="uno" type="hidden" value="${profile.uno}">
        <tr class="list_table_title_tr">
          <td width="120" align="right" class="detail_table_title_td">修改账号域: </td>
          <td class="detail_table_value_td"><select name="domainType" class="default_select_single">
              <c:forEach items="${profileDomainTypes}" var="profileDomainType">
                <option value="${profileDomainType.code}"
                        <c:if test="${profile.profileDomain.code eq profileDomainType.code}">selected="selected"</c:if>>
                    <fmt:message key="def.profile.domain.${profileDomainType.code}.name" bundle="${def}"/>
                </option>
              </c:forEach>
              </select>
          </td>
        </tr>
      </table>
      <table width="100%"  border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="1" class="default_line_td"></td>
        </tr>
          <tr align="center">
              <td>
                  <input name="Submit" type="submit" class="default_button" value="提交">
                  <input name="Button" type="button" class="default_button" value="返回" onClick="history.back()">
              </td>
          </tr>
      </table></td>
  </form>
  </tr>
</table>
</body>
</html>