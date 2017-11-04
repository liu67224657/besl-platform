<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <title>后台数据管理-创建广告发布locationCode</title>
    <script language="JavaScript" type="text/JavaScript">

    </script>
</head>

<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="100%" valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">修改广告发布locationCode</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <form action="/advertise/location/modify" method="POST">
                    <input type="hidden" name="publishid" value="${location.publishId}"/>
                    <input type="hidden" name="locationcode" value="${location.locationCode}"/>
                    <tr>
                        <td height="1" colspan="4" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_notnulltitle_td">LocationCode：</td>
                        <td nowrap class="edit_table_value_td">
                            <c:if test="${errorMsgMap['locationCode']!=null}">
                                <span class="error_msg_td"><fmt:message key="${errorMsgMap['locationCode']}" bundle="${error}"/></span><br>
                            </c:if>
                            ${location.locationCode}
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_notnulltitle_td">Location名称：</td>
                        <td nowrap class="edit_table_value_td">
                            <c:if test="${errorMsgMap['locationName']!=null}">
                                <span class="error_msg_td"><fmt:message key="${errorMsgMap['locationName']}" bundle="${error}"/></span><br>
                            </c:if>
                            <input name="locationname" type="text" class="default_input_singleline" size="64" maxlength="64" value="${location.locationName}" id=""> * Location的中文名称。
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_notnulltitle_td">Location描述：</td>
                        <td nowrap class="edit_table_value_td">
                            <c:if test="${errorMsgMap['locationDesc']!=null}">
                                <span class="error_msg_td"><fmt:message key="${errorMsgMap['locationDesc']}" bundle="${error}"/></span><br>
                            </c:if>
                            <input name="locationdesc" type="text" class="default_input_singleline" size="64" maxlength="64" value="${location.locationDesc}" id=""> * Location的中文描述。
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_notnulltitle_td">状态：</td>
                        <td nowrap class="edit_table_value_td">
                            <select name="validstatus">
                                <option value="valid" <c:if test="${agent.validStatus.code eq 'valid'}">slected=true</c:if>><fmt:message key="def.validstatus.valid.name"  bundle="${def}"/></option>
                                <option value="removed" <c:if test="${agent.validStatus.code eq 'removed'}">slected=true</c:if>><fmt:message key="def.validstatus.removed.name"  bundle="${def}"/></option>
                                <option value="invalid" <c:if test="${agent.validStatus.code eq 'invalid'}">slected=true</c:if>><fmt:message key="def.validstatus.invalid.name"  bundle="${def}"/></option>
                            </select> * 广告发布location的状态。
                        </td>
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