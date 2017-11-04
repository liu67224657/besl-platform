<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>GameClient管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script>
        $().ready(function () {
             $('#form_submit').bind('submit', function() {
                var nick = $('#nick').val();
                if (nick == "") {
                    alert('填写正确的用户昵称');
                    return false;
                }
            });
        });

    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">运营维护 >> <a href="/clientline/iphone/news/list"> 着迷手游画报管理</a> >> 迷友管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td"><span
                            style="font-size:16px; font-family: Arial, Helvetica, sans-serif; font-weight:bold;">添加一条玩覇</span>
                    </td>
                </tr>
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="3" class="error_msg_td">
                                ${errorMsg}
                        </td>
                    </tr>
                </c:if>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">

                <tr>
                    <td height="1" class="default_line_td">


                    </td>
                </tr>
            </table>
            <form action="/gameclient/clientline/miyou/create" method="post" id="form_submit"
                  enctype="multipart/form-data">
                <table width="90%" border="0" cellspacing="1" cellpadding="0">

                    <tr>
                        <td height="1" class="">
                            <input type="hidden" name="lineid" value="${lineid}"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            图片:
                        </td>
                        <td height="1">
                            <input type="file" name="pic"/>640*640
                            <%--<img id="menu_pic" src="/static/images/default.jpg" onclick="deletepic(this)"/>--%>
                            <%--<span id="upload_button">上传</span>--%>
                            <%--<span id="loading" style="display:none"><img src="/static/images/loading.gif"/></span>--%>
                            <%--<input id="input_menu_pic" type="hidden" name="picurl" value="">--%>
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            用户名:
                        </td>
                        <td height="1">
                            <input type="text" id="nick" name="nick" value=""/>
                        </td>
                        <td height="1"></td>
                    </tr>
                </table>
                <table width="90%" border="0" cellspacing="1" cellpadding="0">
                    <tr align="center">
                        <td colspan="3">
                            <input name="Submit" type="submit" class="default_button" value="提交">
                            <input name="Reset" type="button" class="default_button" value="返回"
                                   onclick="javascript:window.history.go(-1);">
                        </td>
                    </tr>
                </table>
            </form>
        </td>
    </tr>
</table>
</body>
</html>