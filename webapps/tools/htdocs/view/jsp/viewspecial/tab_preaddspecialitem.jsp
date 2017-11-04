<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <title>后台数据管理-添加专题元素</title>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="100%" valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">添加专题元素</td>
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
                <form action="/viewspecial/addspecialitem" method="POST" id="addspecialitem" name="addspecialitem">
                    <input name="specialId" type="hidden" value="${special.specialId}" id="specialId"/>
                    <tr>
                        <td height="1" colspan="4" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">模板位置：</td>
                        <td class="edit_table_value_td">
                            <select name="templateLocationCode" class="default_select_single"
                                    onchange="javascript:document.getElementById('addspecialitem').action='/viewspecial/preaddspecialitem';document.getElementById('addspecialitem').submit()">
                                <c:forEach items="${templateLocations}" var="tLocation">
                                    <option value="${tLocation.locationCode}" <c:if test="${templateLocation.locationCode == tLocation.locationCode}">selected="true"</c:if>>
                                            ${tLocation.locationCode} - ${tLocation.locationName}
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                        <jsp:include page="/view/jsp/viewspecial/tab_preaddspecialitem_${templateLocation.itemType.code}.jsp"></jsp:include>
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
                            <input name="Reset" type="reset" class="default_button" value="返回" onclick="history.back();">
                        </td>
                    </tr>
                </form>
            </table>
        </td>
    </tr>
</table>
</body>
</html>