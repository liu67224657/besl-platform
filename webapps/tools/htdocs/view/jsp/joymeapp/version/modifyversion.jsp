<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>添加版本信息</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script>
        $(document).ready(function () {
            $('#select_appkey').change(function () {
                getVersion();
            });

            $('#select_platform').change(function () {
                getVersion();
            });

            $('#select_enterprise').change(function () {
                getVersion();
            });

            $('#select_version').change(function () {
                getChannel();
            });

            $('#input_searchapp').bind('click', function () {
                var appName = $('#input_appname').val();
                appName.trim('');
                if (appName.length <= 0) {
                    return;
                }
                $.post("/joymeapp/version/getapp", {appname: appName}, function (req) {
                    var resMsg = eval('(' + req + ')');
                    if (resMsg.rs == 0) {
                        return false;
                    } else {
                        if (resMsg.result == null || resMsg.result.appList == null) {
                            return;
                        }
                        var appList = resMsg.result.appList;
                        var appHtml = '<option value="">请选择</option>';
                        for (var i = 0; i < appList.length; i++) {
                            appHtml += '<option value="' + appList[i].appId + '">' + appList[i].appName + '</option>';
                        }
                        $('#select_appkey').html(appHtml);
                    }
                });
            });
        });

        function getVersion() {
            var appkey = $('#select_appkey').val();
            var platform = $('#select_platform').val();
            var ent = $('#select_enterprise').val();
            if (appkey.length <= 0 || platform.length <= 0) {
                $('#select_version').html('<option value="">请选择应用、平台</option>');
                $('#select_channel').html('<option value="">请选择应用、平台、版本</option>');
                return;
            }
            $.post("/joymeapp/version/getversion", {
                appkey: appkey,
                platform: platform,
                enterprise: ent
            }, function (req) {
                var resMsg = eval('(' + req + ')');
                if (resMsg.rs == 0) {
                    return false;
                } else {
                    if (resMsg.result == null || resMsg.result.versionList == null) {
                        return;
                    }
                    var versionList = resMsg.result.versionList;
                    var versionHtml = '<option value="">请选择</option>';
                    for (var i = 0; i < versionList.length; i++) {
                        versionHtml += '<option value="' + versionList[i] + '">' + versionList[i] + '</option>';
                    }
                    versionHtml += '<span style="color: red">*必选项</span>';
                    $('#select_version').html(versionHtml);
                }
            });
        }

        function getChannel() {
            var appkey = $('#select_appkey').val();
            var platform = $('#select_platform').val();
            var version = $('#select_version').val();
            var ent = $('#select_enterprise').val();
            if (appkey.length <= 0 || platform.length <= 0 || version.length <= 0) {
                return;
            }
            $.post("/joymeapp/version/getversion", {
                appkey: appkey,
                platform: platform,
                version: version,
                enterprise: ent
            }, function (req) {
                var resMsg = eval('(' + req + ')');
                if (resMsg.rs == 0) {
                    return false;
                } else {
                    if (resMsg.result == null || resMsg.result.channelList == null) {
                        return;
                    }
                    var channelList = resMsg.result.channelList;
                    var channelHtml = '<option value="">请选择</option>';
                    for (var i = 0; i < channelList.length; i++) {
                        if(channelList[i] != null && channelList[i] != undefined){
                            channelHtml += '<option value="' + channelList[i].code + '">' + channelList[i].name + '</option>';
                        }
                    }
                    channelHtml += '<span style="color: red">*必选项</span>';
                    $('#select_channel').html(channelHtml);
                }
            });
        }
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 小端APP >> 版本更新列表</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">添加版本</td>
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
            <form action="/joymeapp/version/modify" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td>
                            <input type="hidden" name="vid" value="${appVersion.deploymentId}"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            选择APP:
                        </td>
                        <td height="1">
                            <select name="appkey" id="select_appkey">
                                <option value="">请选择</option>
                                <c:forEach items="${appList}" var="app">
                                    <option value="${app.appId}"
                                            <c:if test="${app.appId == appVersion.appkey}">selected="selected"</c:if>>${app.appName}</option>
                                </c:forEach>
                            </select><span style="color: red">*必选项</span>
                            <br/>
                            <input type="text" id="input_appname" name="" value=""/>
                            <input type="button" class="default_button" value="检索" id="input_searchapp"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            选择平台:
                        </td>
                        <td height="1">
                            <select name="platform" id="select_platform">
                                <c:forEach var="plat" items="${platformList}">
                                    <option value="${plat.code}"
                                            <c:if test="${plat.code == appVersion.appPlatform.code}">selected="selected"</c:if>>
                                        <fmt:message key="joymeapp.platform.${plat.code}" bundle="${def}"/></option>
                                </c:forEach>
                            </select><span style="color: red">*必选项</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            是否是企业版:
                        </td>
                        <td height="1">
                            <select name="enterprise" id="select_enterprise">
                                <option value="1" <c:if test="${appVersion.appEnterpriserType.code == 1}">selected</c:if>>否</option>
                                <option value="2" <c:if test="${appVersion.appEnterpriserType.code == 2}">selected</c:if>>是</option>
                            </select><span style="color: red">*必选项</span>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            选择版本:
                        </td>
                        <td height="1" id="td_version">
                            <select name="version" id="select_version">
                                <option value="">请选择</option>
                                <c:forEach items="${versionList}" var="version">
                                    <option value="${version}"
                                            <c:if test="${version == appVersion.title}">selected="selected"</c:if>>${version}</option>
                                </c:forEach>
                            </select><span style="color: red">*必选项</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            选择渠道:
                        </td>
                        <td height="1" id="td_channel">
                            <select name="channel" id="select_channel">
                                <option value="">全部</option>
                                <c:forEach var="chan" items="${channelList}">
                                    <option value="${chan.code}"
                                            <c:if test="${chan.code == appVersion.channel.code}">selected="selected"</c:if>>
                                            ${chan.name}
                                    </option>
                                </c:forEach>
                            </select><span style="color: red">*必选项</span>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            更新描述:
                        </td>
                        <td height="1">
                            <textarea id="textarea_description" type="text" name="description"
                                      style="height: 100px;width: 400px">${appVersion.description}</textarea>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            资源路径:
                        </td>
                        <td height="1">
                            <input type="text" name="path" size="32" id="input_text_path" value="${appVersion.path}"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            更新方式:
                        </td>
                        <td height="1">
                            <select name="versiontype" id="select_version_type">
                                <c:forEach items="${versionTypes}" var="vType">
                                    <option value="${vType.code}"
                                            <c:if test="${vType.code == appVersion.appVersionUpdateType.code}">selected="selected"</c:if>>
                                        <fmt:message key="joymeapp.version.update.type.${vType.code}" bundle="${def}"/>
                                    </option>
                                </c:forEach>
                            </select>
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