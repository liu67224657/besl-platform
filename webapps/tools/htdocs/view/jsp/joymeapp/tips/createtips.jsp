<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>添加提示板</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/dhtmlxcalendar.css">
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/skins/dhtmlxcalendar_dhx_skyblue.css">
    <script src="/static/include/dhtmlxcalendar/dhtmlxcalendar.js"></script>
    <script>
        $().ready(function () {
            doOnLoad();

        });

        var myCalendar;
        function doOnLoad() {
            myCalendar = new dhtmlXCalendarObject(["updatetime"]);
        }
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 小端APP >> 提示板列表</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">添加提示板</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="14" class="error_msg_td">${errorMsg}</td>
                    </tr>
                </c:if>
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/joymeapp/tips/create" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td">
                            <input type="hidden" name="queryappid" value="${queryAppId}"/>
                            <input type="hidden" name="queryplatform" value="${queryPlatform}"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            标题:
                        </td>
                        <td height="1">
                            <input type="text" name="title" size="32" id="input_text_title" value="${title}"/>暂时不用填
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            描述:
                        </td>
                        <td height="1">
                            <textarea name="description" id="input_text_description" rows="8"
                                      cols="50">${description}</textarea>*必填项
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            更新时间:
                        </td>
                        <td height="1">
                            <input type="text" class="default_input_singleline" size="16" maxlength="20"
                                   name="updatetime" id="updatetime" value=""/>*必填项
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            链接:
                        </td>
                        <td height="1">
                            <input type="text" name="url" size="32" id="input_text_url" value="${url}"/>暂时不用填
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            APP:
                        </td>
                        <td height="1">
                            <select name="appid" id="select_appid">
                                <option value="">请选择</option>
                                <c:forEach items="${appList}" var="app">
                                    <c:choose>
                                        <c:when test="${app.appId == appId}">
                                            <option value="${app.appId}" selected="selected">${app.appName}</option>
                                        </c:when>
                                        <c:otherwise>
                                            <option value="${app.appId}">${app.appName}</option>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </select>*必选项
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            选择平台:
                        </td>
                        <td height="1">
                            <select name="platform">
                                <c:forEach var="plat" items="${platformList}">
                                    <c:choose>
                                        <c:when test="${plat.code == platform}">
                                            <option value="${plat.code}" selected="selected">
                                                <fmt:message key="joymeapp.platform.${plat.code}" bundle="${def}"/>
                                            </option>
                                        </c:when>
                                        <c:otherwise>
                                            <option value="${plat.code}">
                                                <fmt:message key="joymeapp.platform.${plat.code}" bundle="${def}"/>
                                            </option>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </select>*必选项
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="3" height="1" class="default_line_td"></td>
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