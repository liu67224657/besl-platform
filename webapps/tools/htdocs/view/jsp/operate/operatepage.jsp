<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>商品管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script>
        $(document).ready(function(){
             $('#refresh_button').click(function(){
                window.open("/operate/dorefreshindex");
             });
        });
    </script>.
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 运营操作 </td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">运营操作</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table>
                <p:privilege name="/operate/dorefreshindex">
                <tr>      `
                    <td>刷新首页：</td>
                    <td class="edit_table_value_td"><input type="button" id="refresh_button" value="刷新首页" ></td>
                    <td></td>
                </tr>
                    <tr>      `
                        <td>刷新M站首页：</td>
                        <td class="edit_table_value_td"><a href="http://webcache.joyme.com/ac/freshindex.jsp"><input type="button" value="刷新M站首页" ></a></td>
                        <td></td>
                    </tr>
               </p:privilege>
            </table>
        </td>
    </tr>
</table>
</body>
</html>