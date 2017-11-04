<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台数据管理、广告发布lcoationlist</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script language="JavaScript" type="text/JavaScript">
        function add(publishId) {
            window.location.href = "/advertise/location/addpage?publishid=" + publishId;
        }
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="100%" valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr class="toolbar_tr">
                    <td>
                        <%--<p:privilege name="/advertise/location/addpage">--%>
                            <input name="Submit" type="submit" class="default_button" value="建立locationcode" onClick="add('${publishid}');">
                        <%--</p:privilege>--%>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="10" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center" width="40">选择</td>
                    <td nowrap align="center" width="100">Code</td>
                    <td nowrap align="left">名称</td>
                    <td nowrap align="left">描述</td>
                    <td nowrap align="left" width="280">链接</td>
                    <td nowrap align="center" width="80">当前状态</td>
                    <td nowrap align="center" width="160">创建人</td>
                    <td nowrap align="center" width="160">创建日期</td>
                    <td nowrap align="center" width="60">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="10" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${locationList.size() > 0}">
                        <%--<form action="/viewline/batchstatuslines" method="POST" name="batchform">--%>
                            <c:forEach items="${locationList}" var="location" varStatus="st">
                                <tr class="<c:choose>
                            <c:when test="${st.index % 2 == 0}">
                               list_table_opp_tr
                            </c:when>
                            <c:otherwise>
                                list_table_even_tr
                            </c:otherwise>
                            </c:choose>">
                                    <td align="center">
                                        <input type="checkbox" name="lineIds" value="${location.plId}">
                                    </td>
                                    <td align="center">
                                        ${location.locationCode}
                                    </td>
                                    <td align="left">
                                        ${location.locationName}
                                    </td>
                                    <td align="left">
                                        ${location.locationDesc}
                                    </td>
                                    <td align="left">
                                        ${URL_WWW}/click/${publishid}/${location.locationCode}
                                    </td>
                                    <td align="center">
                                        <fmt:message key="def.validstatus.${location.validStatus.code}.name" bundle="${def}"/>
                                    </td>
                                    <td align="center">
                                        ${location.createUserid}
                                    </td>
                                    <td align="center">
                                        <fmt:formatDate value="${location.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </td>
                                    <td align="center">
                                        <a href="/advertise/location/modifypage?locationcode=${location.locationCode}&publishid=${publishid}">编辑</a>
                                    </td>
                                </tr>
                            </c:forEach>
                            <tr>
                                <td height="1" colspan="10" class="default_line_td"></td>
                            </tr>
                            <%--<tr class="toolbar_tr">--%>
                                <%--<td colspan="10">--%>
                                    <%--<input type="checkbox" name="selectall" value="1"--%>
                                           <%--onclick='javascript:checkall(document.forms["batchform"].lineIds, document.forms["batchform"].selectall)'>全选--%>
                                    <%--<input type="checkbox" name="uncheck" value="1" onclick='javascript:convertcheck(document.forms["batchform"].lineIds)'>反选--%>
                                    <%--将选中记录有效状态改成：--%>
                                    <%--<select name="updateValidStatusCode" class="default_select_single">--%>
                                        <%--<option value="">--请选择--</option>--%>
                                        <%--<c:forEach items="${validStatuses}" var="validStatus">--%>
                                            <%--<option value="${validStatus.code}" <c:if test="${updateValidStatusCode == validStatus.code}">selected="true"</c:if>>--%>
                                                <%--<fmt:message key="def.validstatus.${validStatus.code}.name" bundle="${def}"/>--%>
                                            <%--</option>--%>
                                        <%--</c:forEach>--%>
                                    <%--</select>--%>
                                    <%--<input name="submit" type="submit" class="default_button" value="批量修改">--%>
                                <%--</td>--%>
                            <%--</tr>--%>
                        <%--</form>--%>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="10" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="10" height="1" class="default_line_td"></td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</body>
</html>