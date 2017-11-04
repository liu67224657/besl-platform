<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>添加配置</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
        });
    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷手游画报管理 >> 玩霸摇一摇配置列表</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">添加配置</td>
                </tr>
            </table>
            <form action="/joymeapp/config/modify" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td">
                            <input type="hidden" name="configid" value="${config.configId}"/>
                            <input type="hidden" name="appkey" value="${config.appKey}"/>
                            <input type="hidden" name="platform" value="${config.platform.code}"/>
                            <input type="hidden" name="version" value="${config.version}"/>
                            <input type="hidden" name="channel" value="${config.channel}"/>
                            <input type="hidden" name="enterprise" value="${config.enterpriseType.code}"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            应用:
                        </td>
                        <td height="1">
                            ${app.appName}(${app.appId})
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            平台:
                        </td>
                        <td height="1">
                            <fmt:message key="joymeapp.platform.${config.platform.code}" bundle="${def}"/>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            版本:
                        </td>
                        <td>
                            ${config.version}
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            渠道:
                        </td>
                        <td>
                            <fmt:message key="joymeapp.channel.type.${config.channel}" bundle="${def}"/>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            是否是企业版:
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${config.enterpriseType.code == 1}">否</c:when>
                                <c:when test="${config.enterpriseType.code == 2}">是</c:when>
                                <c:otherwise></c:otherwise>
                            </c:choose>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            摇一摇开关:
                        </td>
                        <td>
                            <select name="shake_open">
                                <option value="false"
                                        <c:if test="${config.info.shake_open == 'false'}">selected="selected"</c:if>>关
                                </option>
                                <option value="true"
                                        <c:if test="${config.info.shake_open == 'true'}">selected="selected"</c:if>>开
                                </option>
                            </select><span style="color: #ff0000">控制"摇一摇"是否出现</span>
                        </td>
                        <td height="1">
                        </td>
                    </tr>

                </table>
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr align="center">
                        <td colspan="3">
                            <input name="Submit" type="submit" class="default_button" value="提交">
                            <input name="Reset" type="button" class="default_button" value="返回"
                                   onclick="javascript:window.history.go(-1);">
                        </td>
                    </tr>
                </table>
            </form>
        </td>
    </tr>
</table>
</body>
</html>