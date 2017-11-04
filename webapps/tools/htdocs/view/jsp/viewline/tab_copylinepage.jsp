<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台数据管理、复制Line到目标Line中</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script language="JavaScript" type="text/JavaScript">
        function copy(id) {
            window.location.href = "";
        }
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <form action="/viewline/copyline" method="POST" >
    <tr>
        <td height="100%" valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr class="toolbar_tr">
                    <td>
                     请输入目标Line的ID
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="2" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center" colspan="2">
                        <input name="targetLineId" id="targetLineId" type="text" class="default_input_singleline" value="${targetLineId}">
                        <c:if test="${errorMsgMap['targetLineId'] != null}">
                            <span class="error_msg_td"><fmt:message key="${errorMsgMap['targetLineId']}" bundle="${error}"/></span><br>
                        </c:if>
                    </td>
                </tr>
                <tr align="center">
                    <td colspan="2">
                        <input type="submit" value="复制到目标Line" class="default_button">
                        <input name="Reset" type="button" class="default_button" value="返回" onclick="javascript:window.history.go(-1);">
                    </td>
                </tr>
                <tr>
                    <td colspan="2" height="1" class="default_line_td"></td>
                </tr>
            </table>
        </td>
    </tr>
    <input type="hidden" name="lineId" value="${lineId}">
    </form>

</table>
</body>
</html>