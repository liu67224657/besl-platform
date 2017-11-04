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
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script>
        function isNotNull() {
            var ios = $("#input_text_ios").val();
            var android = $("#input_text_android").val();
            var startUrl = "http://";
            var startUrl2 = "https://";
            var startUrl3 = "itms-services";
            if ($("#codename").val() == '') {
                alert("请输入名称");
                return false;
            }
            if (ios.length == 0 || android.length == 0) {
                if (ios == startUrl || ios == startUrl2) {
                    alert("IOS/安卓 至少填一个链接");
                    return false;
                }
            }
            if (ios.length > 0) {
                if (ios.substring(0, 7) != startUrl && ios.substring(0, 8) != startUrl2 && ios.substring(0, 13) != startUrl3) {
                    alert("IOS链接请以‘http://’或者‘https://’开头或者‘itms-services’开头");
                    return false;
                }
                if (ios == startUrl || ios == startUrl2) {
                    alert("请填写完整IOS链接");
                    return false;
                }
            }
            if (android.length > 0) {
                if (android.substring(0, 7) != startUrl && android.substring(0, 8) != startUrl2) {
                    alert("android链接请以‘http://’或者‘https;//’开头");
                    return false;
                }
                if (android == startUrl || android == startUrl2) {
                    alert("请填写完整Android链接");
                    return false;
                }
            }
        }

    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 广告管理 >> APP跳转链接管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">链接添加</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/advertise/appurl/create" method="post" id="form_submit" onsubmit="return isNotNull();">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            名称:
                        </td>
                        <td height="1" class="edit_table_defaulttitle_td">
                            <input id="codename" type="text" name="codename" size="32" value=""/>

                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            IOS链接:
                        </td>
                        <td height="1" class="edit_table_defaulttitle_td">
                            <input id="input_text_ios" type="text" name="iosUrl" size="32" value=""/>
                            <span style="color:red">*开头必须为 “http://”或 “https://” 或 ”itms-services“</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            ANDROID链接:
                        </td>
                        <td height="1" class="edit_table_defaulttitle_td">
                            <input id="input_text_android" type="text" name="androidUrl" size="32" value=""/>
                            <span style="color:red">*开头必须为 “http://”或 “https://”</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr align="center">
                        <td colspan="3">
                            <input name="Submit" type="submit" class="default_button" value="添加">
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