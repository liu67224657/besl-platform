<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>创建APP</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script>
        $(document).ready(function() {
            $('#form_submit').bind('submit', function() {
                var appNameVal = $('#input_text_appname').val();
                if (appNameVal.length == 0) {
                    alert('请填写APP名称');
                    return false;
                }
            });
        });
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷APP >> APP管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">创建APP</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/video/game/modify" method="post" id="form_submit">
                <input type="hidden" name="returnFlag" value="${returnFlag}"/>
                <input type="hidden" name="appid" value="${authapp.appId}"/>
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td">
                            APP名称:
                        </td>
                        <td height="1" class="edit_table_defaulttitle_td">
                            <input id="input_text_appname" type="text" name="appname" size="32"
                                   value="${authapp.appName}"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <%--<tr>--%>
                        <%--<td height="1" class="default_line_td">--%>
                            <%--是否配置“我的”模块:--%>
                        <%--</td>--%>
                        <%--<td height="1" class="edit_table_defaulttitle_td">--%>
                            <%--<select name="displaymy">--%>
                                <%--<option value="0" <c:if test="${authapp.displayMy==0}">selected</c:if> >不显示</option>--%>
                                <%--<option value="1" <c:if test="${authapp.displayMy==1}">selected</c:if>>显示</option>--%>
                            <%--</select>--%>
                        <%--</td>--%>
                        <%--<td height="1" class=>--%>
                        <%--</td>--%>
                    <%--</tr>--%>

                    <tr>
                        <td height="1" class="default_line_td">
                            秘钥:
                        </td>
                        <td height="1" class="edit_table_defaulttitle_td">
                            ios:<input id="ios" type="text" name="ios" size="40" value="${authapp.appSecret.ios}"/>
                            android<input id="android" type="text" name="android" size="40" value="${authapp.appSecret.android}"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <%--<tr>--%>
                        <%--<td height="1" class="default_line_td">--%>
                            <%--“我的”模块是否显示小红点:--%>
                        <%--</td>--%>
                        <%--<td height="1" class="edit_table_defaulttitle_td">--%>
                              <%--<select name="displayred">--%>
                                <%--<option value="0" <c:if test="${authapp.displayRed==0}">selected</c:if>>不显示</option>--%>
                                <%--<option value="1" <c:if test="${authapp.displayRed==1}">selected</c:if>>显示</option>--%>
                            <%--</select>--%>
                        <%--</td>--%>
                        <%--<td height="1" class=>--%>
                        <%--</td>--%>
                    <%--</tr>--%>
                    <tr align="center">
                        <td colspan="3">
                            <input name="Submit" type="submit" class="default_button" value="提交">
                            <input name="Reset" type="button" class="default_button" value="返回"
                                   onclick="javascipt:window.history.go(-1);">
                        </td>
                    </tr>
                </table>
            </form>
        </td>
    </tr>
</table>
</body>
</html>