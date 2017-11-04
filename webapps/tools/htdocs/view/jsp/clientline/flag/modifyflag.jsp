<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head><title>Simple jsp page</title>
    <script charset="utf-8" src="/static/include/kindeditor/kindeditor-min.js"></script>
    <script charset="utf-8" src="/static/include/kindeditor/lang/zh_CN.js"></script>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script>
        $().ready(function () {
            $('#form_submit').bind('submit', function () {
                var desc = $('#input_text_desc').val();
                if (desc.length == 0) {
                    alert('请填写名称');
                    return false;
                }
                if ($('#selected_lineid').length > 0) {
                    var lineid = $('#selected_lineid').val();
                    if (lineid.length == 0) {
                        alert('请选择一条line');
                        return false;
                    }
                }
                if ($('#selected_platform').length > 0) {
                    var lineid = $('#selected_platform').val();
                    if (lineid.length == 0) {
                        alert('请选择平台');
                        return false;
                    }
                }
                var maxid = $('#input_text_maxitemid').val();
                if (maxid.length == 0 || !isNumericString(maxid)) {
                    alert('请填写数字类型的ID最大值');
                    return false;
                }
            });
        });
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 手游画报 >> 着迷大端flag标志管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td"><span
                            style="font-size:16px; font-family: Arial, Helvetica, sans-serif; font-weight:bold;">添加一个flag标志</span>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/clientline/flag/modify" method="post" id="form_submit">
                <table width="90%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1">
                            <input type="hidden" name="flagid" id="input_hidden_flagid" value="${flag.flagId}"/>
                            <input type="hidden" name="flagtype" id="input_hidden_flagtype"
                                   value="${flag.clientLineFlagType.code}"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            名称
                        </td>
                        <td height="1">
                            <input type="text" name="flagdesc" size="30" id="input_text_desc" value="${flag.flagDesc}"/>
                            *必填项
                        </td>
                        <td height="1" align="left"></td>
                    </tr>
                    <c:if test="${flag.clientLineFlagType.code == clientLine}">
                        <tr>
                            <td height="1" class="default_line_td">
                                选择一条Line:
                            </td>
                            <td height="1">
                                <select name="lineid" id="selected_lineid">
                                    <option value="">请选择</option>
                                    <c:forEach items="${lineList}" var="line">
                                        <option value="${line.lineId}"
                                        <c:if test="${line.lineId == flag.lineId}">selected="selected"</c:if>>
                                        ${line.lineName}&nbsp;&nbsp;${line.code}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td height="1"><span style="color: #ff0000;"><c:if
                                    test="${fn:length(existError)>0}"><fmt:message key="client.flag.line.exist"
                                                                                   bundle="${def}"/></c:if></span></td>
                        </tr>
                    </c:if>
                    <c:if test="${flag.clientLineFlagType.code == topMenu}">
                        <tr>
                            <td height="1" class="default_line_td">
                                选择平台:
                            </td>
                            <td height="1">
                                <select name="lineid" id="selected_platform">
                                    <option value="">请选择</option>
                                    <c:forEach items="${platforms}" var="pl">
                                        <option value="${pl.code}"
                                        <c:if test="${pl.code == flag.lineId}">selected="selected"</c:if>>
                                        <fmt:message key="client.platform.${pl.code}" bundle="${def}"/></option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td height="1"><span style="color: #ff0000;"><c:if
                                    test="${fn:length(existError)>0}"><fmt:message key="client.flag.platform.exist"
                                                                                   bundle="${def}"/></c:if></span></td>
                        </tr>
                        <tr>
                            <td height="1" class="default_line_td">
                                选择APP:
                            </td>
                            <td height="1">
                                <select name="appid" id="selected_app">
                                    <option value="">请选择</option>
                                    <c:forEach items="${appList}" var="app">
                                        <option value="${app.appId}"
                                        <c:if test="${app.appId == flag.lineCode}">selected="selected"</c:if>>${app.appName}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td height="1"></td>
                        </tr>
                    </c:if>
                    <tr>
                        <td height="1" class="default_line_td">
                            子元素ID最大值
                        </td>
                        <td height="1">
                            <input type="text" size="10" name="maxitemid" id="input_text_maxitemid"
                                   value="${flag.maxItemId}"/>
                        </td>
                        <td height="1"></td>
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