<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/viewlineitemhandler.js"></script>
    <title>后台数据管理-添加Line元素</title>
</head>

<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="100%" valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="edit_table_header_td">添加元素</td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <c:if test="${errorMsgMap['system']!=null}">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td class="error_msg_td">
                            <fmt:message key="${errorMsgMap['system']}" bundle="${error}"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td"></td>
                    </tr>
                </table>
            </c:if>
            <c:if test="${errorMsgMap['duplicateEntry']!=null}">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td class="error_msg_td">
                            <fmt:message key="${errorMsgMap['duplicateEntry']}" bundle="${error}"/> ，请<a href="/viewline/preeditlineitem?lineItemId=${lineItem.itemId}">点击</a>此进行编辑
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td"></td>
                    </tr>
                </table>
            </c:if>
            <jsp:include page="/view/jsp/viewline/tab_preaddlineitem_${line.itemType.code}.jsp"></jsp:include>
        </td>
    </tr>
</table>
</body>
</html>
