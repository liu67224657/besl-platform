<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>生成二维码</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script>
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 工具 >> 生成二维码</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">生成二维码</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/qrcode/generator" method="post" id="form_submit" target="_blank">
                <table width="90%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" class="">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            宽度:
                        </td>
                        <td height="1" class="" width="10">
                            <input type="text" name="width" size="20" id="input_text_width" value="200"/>
                        </td>
                        <td height="1" align="left">*必填项</td>
                    </tr>
                    <tr>
                        <td>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            高度:
                        </td>
                        <td height="1" class="">
                            <input type="text" name="hegiht" size="20" id="input_text_height" value="200"/>
                        </td>
                        <td height="1" >*必填项</td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            内容:
                        </td>
                        <td height="1" class="">
                            <input type="text" name="content" size="100" id="input_text_content" value=""/>
                        </td>
                        <td height="1" >*必填项 二维码对应的链接和内容</td>
                    </tr>
                    <tr align="center">
                        <td colspan="3">
                            <input name="Submit" type="submit" class="default_button" value="生成"  />
                        </td>
                    </tr>
                </table>
            </form>
        </td>
    </tr>
</table>
</body>
</html>