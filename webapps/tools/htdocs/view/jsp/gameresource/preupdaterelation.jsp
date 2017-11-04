<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/resourcehandler.js"></script>
    <title>增加资源连接</title>
    <script language="JavaScript" type="text/JavaScript">

    </script>
</head>

<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="100%" valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">增加资源连接</td>
                </tr>
            </table>
            <c:if test="${errorMsgMap['system']!=null}">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td class="error_msg_td">
                            <fmt:message key="${errorMsgMap['system']}" bundle="${error}"/>
                        </td>
                    </tr>
                </table>
            </c:if>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <form action="/gameresource/updaterelation" method="POST">
                    <input type="hidden" name="relationid" value="${gameRelation.relationId}">
                    <input type="hidden" name="resid" value="${gameRelation.resourceId}">
                    <tr>
                        <td height="1" colspan="4" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_notnulltitle_td">资源类型：</td>
                        <td nowrap class="edit_table_value_td">
                            <c:if test="${errorMsgMap['lineName']!=null}">
                                <span class="error_msg_td"><fmt:message key="${errorMsgMap['lineName']}" bundle="${error}"/></span><br>
                            </c:if>
                            <%--<select name="relationType" class="default_select_single">--%>
                                <%--<c:forEach items="${relationTypes}" var="relationtype">--%>
                                    <%--<option value="${relationtype.code}"--%>
                                            <%--<c:if test="${relationtype.code eq gameRelation.gameRelationType.code}">selected="selected" </c:if>>--%>
                                        <fmt:message key="gameres.relation.type.${gameRelation.gameRelationType.code}.name" bundle="${def}"/>
                                    <%--</option>--%>
                                <%--</c:forEach>--%>
                            <%--</select>--%>
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_notnulltitle_td">资源名称：</td>
                        <td nowrap class="edit_table_value_td">
                            <span class="error_msg_td">用于自定义链接显示名称</span><br>
                            <input name="relationName" type="text" class="default_input_singleline" value="${gameRelation.relationName}">
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_notnulltitle_td">资源URL或者ID：</td>
                        <td nowrap class="edit_table_value_td">
                            <c:if test="${errorMsgMap['locationCode']!=null}">
                                <span class="error_msg_td"><fmt:message key="${errorMsgMap['locationCode']}" bundle="${error}"/></span><br>
                            </c:if>
                            <input name="relationValue" type="text" class="default_input_singleline" value="${gameRelation.relationValue}">
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_notnulltitle_td">排序值：</td>
                        <td nowrap class="edit_table_value_td">
                            <input type="text" name="sortNum" class="default_input_singleline" value="${gameRelation.sortNum}"> *必须是数字
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">当前状态：</td>
                        <td nowrap class="edit_table_value_td">
                            <select name="validStatus" class="default_select_single">
                                <c:forEach items="${validStatus}" var="validstatus">
                                    <option value="${validstatus.code}"
                                            <c:if test="${validstatus.code eq gameRelation.validStatus.code}">selected="selected" </c:if>
                                            >
                                        <fmt:message key="def.validstatus.${validstatus.code}.name" bundle="${def}"/>
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" colspan="4" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td colspan="4">&nbsp;</td>
                    </tr>
                    <tr align="center">
                        <td colspan="4">
                            <input name="Submit" type="submit" class="default_button" value="提交">
                            <input name="Reset" type="button" class="default_button" value="返回" onclick="javascipt:window.history.go(-1);">
                        </td>
                    </tr>
                </form>
            </table>
        </td>
    </tr>
</table>
</body>
</html>