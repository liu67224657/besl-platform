<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title></title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
    <style type="text/css">
        .td_cent {
            text-align: center;
            vertical-align: middle;
        }
    </style>
    <script>
        $().ready(function () {
            $('#form_submit').bind('submit', function () {
                if ($("#screenname").val() == '') {
                    alert("昵称");
                    $("#screenname").focus();
                    return false;
                }
                if ($("#body").val() == '') {
                    alert("内容");
                    $("#body").focus();
                    return false;
                }
                var score = $.trim($('#score').val());
                if(isNaN(score)){
                    alert("评分请填写数字");
                    $("#score").focus();
                    return false;
                }
            });

        });

    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr height="40">
        <td height="22" class="page_navigation_td" colspan="2">>> 运营维护 >> 手游排行榜 >> 排行榜长评维护列表</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">新增评论</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                ${errorMsg}
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/joymeapp/mobilegame/reply/create" method="post" id="form_submit">
                <input type="hidden" name="id" value="${id}"/>
                <input type="hidden" name="type" value="${type}"/>
                <input type="hidden" name="contentid" value="${contentid}"/>
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            用户昵称:
                        </td>
                        <td height="1">
                            <input id="screenname" type="text" name="screenname" size="66" value="${screenname}"/><span
                                style="color:red;">*必填项</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <c:if test="${type eq 'gag'}">
                        <tr style="display: none">
                    </c:if>
                            <td height="1" class="default_line_td td_cent" width="100">
                                评分:
                            </td>
                            <td height="1">
                                <input id="score" type="text" name="score" size="66" value="${score}"/><span
                                    style="color:red;">*必填项(填写数字)</span>
                            </td>
                            <td height="1" class=>
                            </td>
                        </tr>

                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            评论内容:
                        </td>
                        <td height="1">
                            <textarea id="body" type="text" name="body"
                                      style="height: 237px;width: 431px">${body}</textarea><span
                                style="color:red;">*必填项</span>
                        </td>
                        <td height="1" class=>
                        </td>
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