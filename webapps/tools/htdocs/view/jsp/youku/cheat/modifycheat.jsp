<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>优酷修改点赞</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript" src="/static/include/My97DatePicker/WdatePicker.js"></script>
    <script language="JavaScript" type="text/JavaScript">

        $(document).ready(function () {


            $('#form_submit').bind('submit', function () {
                var msg_subject = $('#msg_subject').val();
                if (msg_subject.length == 0) {
                    alert('标题');
                    return false;
                }
                var input_sortmsg = $('#input_sortmsg').val();
                if (input_sortmsg.length == 0) {
                    alert('描述');
                    return false;
                }

                var select_rtype = $('#select_rtype').val();
                if (select_rtype.length == 0) {
                    alert('跳转类型');
                    return false;
                }
                var senddate = $('#senddate').val();
                if (senddate.length == 0) {
                    alert('日期');
                    return false;
                }


            });
        });


    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 优酷游戏中心 >> 优酷修改点赞</td>
    </tr>
<tr>
<td height="100%" valign="top"><br>
<table width="30%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td class="list_table_header_td">优酷修改点赞</td>
        <td class="">
            <form></form>
        </td>
    </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td height="1" class="default_line_td">
        </td>
    </tr>
</table>
<form action="/youku/cheat/modify" method="post" id="form_submit">
<table width="100%" border="0" cellspacing="1" cellpadding="0">
<tr>
    <td height="1" colspan="3" class="default_line_td">
        <input type="hidden" name="dede_archives_id" value="${cheat.dede_archives_id}"/>
        <input type="hidden" name="offset" value="${pageStartIndex}"/>

    </td>
</tr>

<tr>
    <td height="1" class="default_line_td">
        标题:
    </td>
    <td height="1">
        <input  type="text"  size="64" value="${archive.title}" disabled>
    </td>
    <td height="1">
    </td>
</tr>
    <tr>
        <td height="1" class="default_line_td">
            阅读数:
        </td>
        <td height="1">
            <input type="text" name="read_num" size="64" value="${cheat.read_num}" >
        </td>
        <td height="1">
        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td">
            点赞数:
        </td>
        <td height="1">
            <input type="text" name="agree_num" size="64" value="${cheat.agree_num}" >
        </td>
        <td height="1">
        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td">
            真实阅读数:
        </td>
        <td height="1">
            <input type="text" name="real_read_num" size="64" value="${cheat.real_read_num}" disabled>
        </td>
        <td height="1">
        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td">
            真实点赞数:
        </td>
        <td height="1">
            <input type="text" name="real_agree_num" size="64" value="${cheat.real_agree_num}" disabled>
        </td>
        <td height="1">
        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td">
            创建日志:
        </td>
        <td height="1">
            <input type="text" name="cheating_time" size="64" value="${cheat.cheating_time}" disabled>
        </td>
        <td height="1">
        </td>
    </tr>
<tr>
    <td height="1" colspan="3" class="default_line_td"></td>
</tr>
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