<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <title>激活编辑</title>
    <style type="text/css">
        td, td div {
            overflow: hidden;
            text-overflow: ellipsis; /* for IE */
            -moz-text-overflow: ellipsis; /* for Firefox,mozilla */
            white-space: nowrap;

        }
    </style>
</head>

<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="100%" valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="22" class="page_navigation_td">>> 编辑管理 >> 激活帐号
                    </td>
                </tr>
            </table>
            <br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">激活编辑帐号</td>
                </tr>
            </table>
            <c:if test="${fn:length(errorMsg)>0}">
                <b style="color:red;"><fmt:message key="${errorMsg}" bundle="${error}"/></b>
            </c:if>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <form action="/editor/user/active" method="POST">
                    <input type="hidden" value="${rurl}" name="rurl"/>
                    <tr>
                        <td height="1" colspan="2" class="default_line_td"></td>
                    </tr>
                    <tr class="list_table_opp_tr">
                        <td width="300px">该用户（<b style="color:red">${currentUser.username}</b>)没有权限请激活该账户</td>
                        <td width=""><input type="submit" value="激活该用户"/></td>
                    </tr>
                </form>
            </table>
        </td>
    </tr>
</table>
</body>
</html>