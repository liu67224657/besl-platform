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
    <style type="text/css">
        .td_cent {
            text-align: center;
            vertical-align: middle;
        }
    </style>
    <script>
        $(document).ready(function () {
            $('#form_submit').bind('submit', function () {
                if ($("#title").val() == '') {
                    alert("通知标题");
                    $("#title").focus();
                    return false;
                }
                if ($("#text").val() == '') {
                    alert("通知内容");
                    $("#text").focus();
                    return false;
                }
                if ($("#jt").val() == '') {
                    alert("跳转类别");
                    $("#jt").focus();
                    return false;
                }
                if ($("#ji").val() == '') {
                    alert("跳转目标");
                    $("#ji").focus();
                    return false;
                }

            });

        });


    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 玩霸-问答 >> 系统通知</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">新增系统通知</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="10" class="error_msg_td">
                            ${errorMsg}
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/wanba/systemnotice/modify" method="post" id="form_submit">
                <input type="hidden" name="systemNoticeId" size="48" value="${systemNotice.systemNoticeId}"/>
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            通知标题:
                        </td>
                        <td height="1">
                            <input id="title" type="text" name="title" size="48" value="${systemNotice.title}"/><span>*必填</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            通知内容:
                        </td>
                        <td height="1">
                            <input id="text" type="text" name="text" size="48" value="${systemNotice.text}"/><span>*必填</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            跳转类别:
                        </td>
                        <td height="1">
                            <select name="jt" id="jt">
                                <option value="-1">请选择</option>
                                <c:forEach items="${wanbaJt}" var="type">
                                    <option value="${type.code}" <c:if test="${type.code==systemNotice.jt}">selected</c:if> >${type.name}</option>
                                </c:forEach>
                            </select>*必选项
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="80">
                            跳转目标:
                        </td>
                        <td height="1">
                            <input id="ji" type="text" name="ji" size="48" value="${systemNotice.ji}"/>
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