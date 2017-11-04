<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>虚拟用户批量点赞</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <style type="text/css">
        .td_cent {
            text-align: center;
            vertical-align: middle
        }

        ;
    </style>
    <script>
        $().ready(function() {
            $('#form_submit').bind('submit', function() {
                if ($("#unos").val() == '') {
                    alert("虚拟用户unos不能为空");
                    $("#unos").focus();
                    return false;
                }
                if ($("#contentId").val() == '') {
                    alert("文章ID不能为空");
                    $("#contentId").focus();
                    return false;
                }
            });

        });


    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 条目管理 >> 虚拟用户批量点赞</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">虚拟用户批量点赞</td>
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
            <form action="/joymeapp/socialclient/virtual/agree" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            虚拟人昵称UNO:
                        </td>
                        <td height="1">
                            <textarea id="input_description" type="text" name="unos"
                                      style="height: 100px;width: 634px">${unos}</textarea><span style="color:red;">

                            </br>*必填项:uno用英文[,]分割，如  93ddd274-0212-483d-b8ba-f3ae8c29a9ee,4064c2ff-af53-4aa8-883e-fb1636190ef3</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            文章ID:
                        </td>
                        <td height="1">
                            <input id="contentId" type="text" name="contentId" size="48" value="${contentId}"/><span
                                style="color:red;">*必填项</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            执行任务间隔:
                        </td>
                        <td height="1">
                            <select name="timerType">
                                <option value="0" <c:if test="${timerType==0}">selected="selected"</c:if>>每1分钟执行一次</option>
                                <option value="1" <c:if test="${timerType==1}">selected="selected"</c:if>>每5分钟执行一次</option>
                                <option value="2" <c:if test="${timerType==2}">selected="selected"</c:if>>每10分钟执行一次</option>
                                <option value="3" <c:if test="${timerType==3}">selected="selected"</c:if>>每15分钟执行一次</option>
                                <option value="4" <c:if test="${timerType==4}">selected="selected"</c:if>>每30分钟执行一次</option>
                            </select>
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