<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>分享话题</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
<script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script>
    $(document).ready(function() {
        $('#form_submit').bind('submit', function() {
            var sourceUrl = $('#input_text_sharetopic').val();
            if (sourceUrl.length == 0) {
                alert('请填写话题');
                return false;
            }
        });
    });
</script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷分享 >> 分享话题</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">编辑分享话题</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/sync/sharetopic/modify" method="post" id="form_submit">
                <input type="hidden" name="sharetopicid" value="${topic.shareTopicId}" />
                <input type="hidden" name="shareid" value="${topic.shareId}" />
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                        <td height="1" class="default_line_td">
                             分享基本信息
                        </td>
                        <td height="1" class="edit_table_defaulttitle_td">
                            <input type="text" name="sharekey" value="${info.shareKey}" disabled="true"/>
                        </td>
                    </tr>
                <tr>
                    <td height="1" class="default_line_td">
                           话题:
                    </td>
                    <td height="1" class="edit_table_defaulttitle_td">
                        <input id="input_text_sharetopic" type="text"  name="sharetopic" size="32" value="${topic.shareTopic}"/>
                    </td>
                    <td height="1" class=>
                    </td>
                </tr>
                <tr align="center">
                        <td colspan="3">
                            <input name="Submit" type="submit" class="default_button" value="提交">
                            <input name="Reset" type="button" class="default_button" value="返回" onclick="javascipt:window.history.go(-1);">
                        </td>
                 </tr>
            </table>
            </form>
        </td>
    </tr>
</table>
</body>
</html>