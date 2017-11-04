<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台消息推送列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script language="JavaScript" type="text/JavaScript">
        $(document).ready(function () {
            $('#form_submit').bind('submit', function () {
                var appkey = $('#select_appkey').val();
                if (appkey.length == 0) {
                    alert("请选择应用");
                    return false;
                }

                var appname = $('#input_appname').val();
                if (appname.length == 0) {
                    alert("请填写标题");
                    return false;
                }

                var version = $('#input_version').val();
                if (version.length == 0) {
                    alert("请填写版本");
                    return false;
                }

                var platform = $('#select_platform').val();
                if (platform.length == 0) {
                    alert("请选择平台");
                    return false;
                }

                var channel = $('#select_ctype').val();
                if (channel.length == 0) {
                    alert("请选择渠道");
                    return false;
                }
            });
        });

    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 小端APP >> 礼包中心开关列表</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="30%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">编辑礼包中心开关</td>
                    <td class="">
                        <form></form>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/joymeapp/giftopen/modify" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td">
                            <input type="hidden" name="infoid" id="input_infoid" value="${appInfo.appInfoId}"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            选择APP:
                        </td>
                        <td height="1" class="edit_table_defaulttitle_td">
                            <select name="appkey" id="select_appkey">
                                <option value="">请选择</option>
                                <c:forEach items="${appList}" var="app">
                                    <option value="${app.appId}"
                                            <c:if test="${app.appId == appInfo.appKey}">selected="selected"</c:if>>${app.appName}</option>
                                </c:forEach>
                            </select>*必选
                        </td>
                        <td height="1" class="edit_table_defaulttitle_td">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            标题:
                        </td>
                        <td height="1">
                            <input id="input_appname" type="text" name="appname" size="64" value="${appInfo.appName}">*必填
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            版本:
                        </td>
                        <td height="1">
                            <input id="input_version" type="text" name="version" size="64" value="${appInfo.version}">*必填
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            平台:
                        </td>
                        <td height="1">
                            <select name="platform" id="select_platform">
                                <option value="">请选择</option>
                                <c:forEach var="platform" items="${platforms}">
                                    <option value="${platform}" <c:if test="${platform == appInfo.appPlatform.code}">selected="selected"</c:if>>
                                        <fmt:message key="joymeapp.platform.${platform}" bundle="${def}"/></option>
                                </c:forEach>
                            </select>*必选
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            渠道:
                        </td>
                        <td height="1">
                            <select name="channeltype" id="select_ctype">
                                <option value="">全部</option>
                                <c:forEach var="ctype" items="${channelTypes}">
                                    <option value="${ctype.code}" <c:if test="${ctype.code == appInfo.channelType.code}">selected="selected"</c:if>
                                            <c:if test="${ctype.code eq 'appstore'}">style="color: red"</c:if>>
                                        <fmt:message key="joymeapp.channel.type.${ctype.code}" bundle="${def}"/>
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            礼包中心:
                        </td>
                        <td height="1">
                            <select name="hasgift" id="select_hasgift">
                                <option value="false">无</option>
                                <option value="true" <c:if test="${appInfo.hasGift}">selected="selected"</c:if>>有</option>
                            </select>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
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