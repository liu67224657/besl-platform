<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <title>游戏条目录入页面</title>
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
                    <td height="22" class="page_navigation_td">>> 编辑管理 >>> 文章管理 >>> 游戏条目录入</td>
                </tr>
            </table>
            <br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="">
                        <form action="/editor/game/page" method="post">
                            <input type="submit" value="返回" class="default_button"/>
                        </form>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">游戏条目录入</td>
                </tr>
            </table>

                <c:if test="${fn:length(errorMsg)>0}">
                      <b style="color:red;"><fmt:message key="${errorMsg}" bundle="${error}"/></b>
                </c:if>

            <form action="/editor/game/create" method="post">
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td width="50" align="left" class="edit_table_defaulttitle_td">条目地址</td>
                        <td class="edit_table_value_td" width="230">
                            <input value="${link}" name="link" size="60"/>
                        </td>
                        <td nowrap class="edit_table_value_td"></td>
                    </tr>
                    <tr>
                        <td height="1" colspan="3" class="default_line_td">
                            <input type="submit" value="录入"/>
                        </td>
                    </tr>
                </td>
                </tr>
            </table>
                </form>
        </td>
    </tr>
</table>
</body>
</html>