<%@ page import="java.net.URLEncoder" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta charset="utf-8">
    <title>后台管理系统登录</title>
    <Script language="javascript">
        if (self != top) {
            top.location = self.location;
        }
    </Script>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="2" cellspacing="0">
    <tr class="header" height="24">
        <td valign="bottom"><b><i>Joyme.com后台管理工具</i></b></td>
    </tr>
    <tr>
        <td align="center" valign="center">
            <table width="100%" border="0" cellspacing="1" cellpadding="1">
                <tr align="center">
                    <td align="center" valign="bottom">
                        <fmt:message key="${'def.login.welcome'}" bundle="${def}"/>
                    </td>
                </tr>
                <form action="/login" method="post" name="loginForm" id="loginForm">
                    <input name="reurl" type="hidden"  size="16" maxlength="16"
                           value="<c:out value="${reurl}"/>">
                    <tr class="toolbar" align="center">
                        <td>
                            管理员账号:
                            <input name="loginName" type="text" id="loginName" size="16" maxlength="16"
                                   value="${loginName}">
                            管理员密码:
                            <input name="loginPwd" type="password" id="loginPwd" size="16" maxlength="16">
                            <input type="submit" name="lk" value=" 登录 ">
                        </td>
                    </tr>
                </form>
                <tr>
                    <td align="center">
                        <div>
                            <c:if test="${!empty msg}">
                                <font color="red"> <c:out value="${msg}"/> </font>
                            </c:if>
                        </div>
                    </td>
                </tr>
            </table>
        </td>
    </tr>

    <tr class="footer">
        <td colspan="2" align="center"><b><i>CopyRight 2017 Joyme.com</i></b></td>
    </tr>
</table>
</body>
</html>
