<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台数据管理、版本更新</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>

    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/dhtmlxcalendar.css">
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/skins/dhtmlxcalendar_dhx_skyblue.css">
    <script src="/static/include/dhtmlxcalendar/dhtmlxcalendar.js"></script>
    <script language="JavaScript" type="text/JavaScript">
        $(document).ready(function () {
            doOnLoad();

            $('#select_appkey').change(function () {
                getVersion();
            });

            $('#select_platform').change(function () {
                getVersion();
            });

            $('#select_enterprise').change(function(){
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

            var appkey = $('#select_appkey').val();
            var platform = $('#select_platform').val();
            var ent = $('#select_enterprise').val();
            if (appkey.length <= 0 || platform.length <= 0) {
                $('#select_version').html('<option value="">请选择应用、平台</option>');
                $('#select_channel').html('<option value="">请选择应用、平台、版本</option>');
                return;
            }
            $.post("/joymeapp/version/getversion", {appkey: appkey, platform: platform, enterprise:ent}, function (req) {
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
                        if (versionList[i] == $('#input_hidden_version').val()) {
                            versionHtml += '<option value="' + versionList[i] + '" selected="selected">' + versionList[i] + '</option>';
                        } else {
                            versionHtml += '<option value="' + versionList[i] + '">' + versionList[i] + '</option>';
                        }
                    }
                    $('#select_version').html(versionHtml);
                    var version = $('#select_version').val();
                    if (version.length <= 0) {
                        return;
                    }
                    $.post("/joymeapp/version/getversion", {appkey: appkey, platform: platform, version: version, enterprise:ent}, function (req) {
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
                                    if (channelList[i].code == $('#input_hidden_channel').val()) {
                                        channelHtml += '<option value="' + channelList[i].code + '" selected="selected">' + channelList[i].name + '</option>';
                                    } else {
                                        channelHtml += '<option value="' + channelList[i].code + '">' + channelList[i].name + '</option>';
                                    }
                                }
                            }
                            $('#select_channel').html(channelHtml);
                        }
                    });
                }
            });
        });

        var myCalendar;
        function doOnLoad() {
            myCalendar = new dhtmlXCalendarObject(["startDate", "endDate"]);
        }

        function getVersion() {
            var appkey = $('#select_appkey').val();
            var platform = $('#select_platform').val();
            var ent = $('#select_enterprise').val();
            if (appkey.length <= 0 || platform.length <= 0) {
                $('#select_version').html('<option value="">请选择应用、平台</option>');
                $('#select_channel').html('<option value="">请选择应用、平台、版本</option>');
                return;
            }
            $.post("/joymeapp/version/getversion", {appkey: appkey, platform: platform, enterprise:ent}, function (req) {
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
                        if (versionList[i] == $('#input_hidden_version').val()) {
                            versionHtml += '<option value="' + versionList[i] + '" selected="selected">' + versionList[i] + '</option>';
                        } else {
                            versionHtml += '<option value="' + versionList[i] + '">' + versionList[i] + '</option>';
                        }

                    }
                    $('#select_version').html(versionHtml);
                }
            });
        }

        function getChannel() {
            var appkey = $('#select_appkey').val();
            var platform = $('#select_platform').val();
            var ent = $('#select_enterprise').val();
            var version = $('#select_version').val();
            if (appkey.length <= 0 || platform.length <= 0 || version.length <= 0) {
                return;
            }
            $.post("/joymeapp/version/getversion", {appkey: appkey, platform: platform, version: version, enterprise:ent}, function (req) {
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
                    $('#select_channel').html(channelHtml);
                }
            });
        }
    </script>
</head>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
    <td height="22" class="page_navigation_td">>> 运营维护 >> 小端APP >> 版本更新列表</td>
</tr>
<tr>
<td height="100%" valign="top"><br>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td class="list_table_header_td">>版本更新列表</td>
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
        <td height="1" width="80" align="center">应用名称</td>
        <td>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td>
                        <input type="text" id="input_appname" name="" value=""/>
                        <input type="button" class="default_button" value="检索" id="input_searchapp"/>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td height="1" class="default_line_td"></td>
    </tr>
</table>
<table width="90%" border="0" cellspacing="0" cellpadding="0">
    <form action="/joymeapp/version/list" method="post" id="form_submit_search">
        <tr>
            <td width="80" align="center">搜索条件</td>
            <td>
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td align="right" class="edit_table_defaulttitle_td">应用：</td>
                        <td>
                            <select id="select_appkey" name="appkey">
                                <option value="">请选择</option>
                                <c:forEach items="${appList}" var="app" varStatus="st">
                                    <option value="${app.appId}"
                                            <c:if test="${app.appId == appKey}">selected="selected"</c:if>>${app.appName}</option>
                                </c:forEach>
                            </select><br/>*若不好找对应的APP，请输入app的名字，检索一下，缩小选择范围
                        </td>
                        <td align="right" class="edit_table_defaulttitle_td">平台：</td>
                        <td>
                            <select id="select_platform" name="platform">
                                <option value="">请选择</option>
                                <c:forEach items="${platformList}" var="plat">
                                    <option value="${plat.code}"
                                            <c:if test="${plat.code == platform}">selected="selected"</c:if>>
                                        <fmt:message key="joymeapp.platform.${plat.code}" bundle="${def}"/>
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td align="right" class="edit_table_defaulttitle_td">是否企业版：</td>
                        <td>
                            <select id="select_enterprise" name="enterprise">
                                <option value="1" <c:if test="${enterprise == 1}">selected="selected"</c:if>>否</option>
                                <option value="2" <c:if test="${enterprise == 2}">selected="selected"</c:if>>是</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td align="right" class="edit_table_defaulttitle_td">版本：</td>
                        <td>
                            <select id="select_version" name="version">
                                <option value="">请选择应用、平台</option>
                            </select>
                            <input type="hidden" id="input_hidden_version" value="${appVersion}"/>
                        </td>
                        <td align="right" class="edit_table_defaulttitle_td">渠道：</td>
                        <td>
                            <select name="channel" id="select_channel">
                                <option value="">请选择应用、平台、版本</option>
                            </select>
                            <input type="hidden" id="input_hidden_channel" value="${channel}"/>
                        </td>
                    </tr>
                </table>
            </td>
            <td align="center">
                <input name="Button" type="submit" class="default_button" value=" 搜索 ">
            </td>
        </tr>
    </form>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td height="1" class="default_line_td"></td>
    </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td>
            <form method="post" action="/joymeapp/version/createpage">
                <table>
                    <tr>
                        <td>
                            <input type="hidden" name="appkey" value="${appKey}"/>
                            <input type="hidden" name="platform" value="${platform}"/>
                            <input type="hidden" name="version" value="${appVersion}"/>
                            <input type="hidden" name="channel" value="${channel}"/>
                            <input type="hidden" name="enterprise" value="${enterprise}"/>
                            <p:privilege name="/joymeapp/version/createpage">
                                <input type="submit" name="button" class="default_button" value="添加版本信息"/>
                            </p:privilege>
                        <td>
                    <tr>
                </table>
            </form>
        </td>
    </tr>
</table>
<table width="100%" border="0" cellspacing="1" cellpadding="0">
    <tr>
        <td height="1" colspan="17" class="default_line_td"></td>
    </tr>
    <tr class="list_table_title_tr">
        <td nowrap align="center" width="">版本ID</td>
        <td nowrap align="center" width="">版本</td>
        <td nowrap align="center" width="">更新描述</td>
        <td nowrap align="center" width="">资源包地址</td>
        <td nowrap align="center" width="">更新方式</td>
        <td nowrap align="center" width="">appid</td>
        <td nowrap align="center" width="">平台</td>
        <td nowrap align="center" width="">是否企业版</td>
        <td nowrap align="center" width="">渠道</td>
        <td nowrap align="center" width="">状态</td>
        <td nowrap align="center" width="">操作</td>
        <td nowrap align="center" width="">创建信息</td>
        <td nowrap align="center" width="">修改信息</td>
    </tr>
    <tr>
        <td height="1" colspan="17" class="default_line_td"></td>
    </tr>
    <c:choose>
        <c:when test="${list.size() > 0}">
            <c:forEach items="${list}" var="version" varStatus="st">
                <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                    <td nowrap>${version.deploymentId}</td>
                    <td nowrap>${version.title}</td>
                    <td nowrap><textarea readonly="readonly"
                                         style="height: 100%;width: 100%;">${version.description}</textarea></td>
                    <td nowrap>${version.path}</td>
                    <td nowrap><fmt:message key="joymeapp.version.update.type.${version.appVersionUpdateType.code}"
                                            bundle="${def}"/></td>
                    <td nowrap>
                        <c:forEach items="${appList}" var="app">
                            <c:if test="${app.appId == version.appkey}">${app.appName}</c:if>
                        </c:forEach>
                    </td>
                    <td nowrap><fmt:message key="joymeapp.platform.${version.appPlatform.code}" bundle="${def}"/></td>
                    <td nowrap><c:if test="${version.appEnterpriserType.code == 1}">否</c:if><c:if test="${version.appEnterpriserType.code == 2}">是</c:if></td>
                    <td nowrap><c:if test="${version.channel != null}"><fmt:message
                            key="joymeapp.channel.type.${version.channel.code}" bundle="${def}"/></c:if></td>
                    <td nowrap
                            <c:choose>
                                <c:when test="${version.removeStatus.code eq 'n'}">style="color: #008000;"</c:when>
                                <c:otherwise>style="color: #ff0000;"</c:otherwise></c:choose>>
                        <fmt:message key="joymeapp.version.status.${version.removeStatus.code}" bundle="${def}"/>
                    </td>
                    <td nowrap>
                        <c:choose>
                            <c:when test="${version.removeStatus.code eq 'n'}">
                                <p:privilege name="/joymeapp/version/remove">
                                    <a href="/joymeapp/version/remove?vid=${version.deploymentId}&offset=${page.startRowIdx}&appkey=${version.appkey}&platform=${version.appPlatform.code}&version=${appVersion}&channel=${version.channel }&enterprise=${version.appEnterpriserType.code}">删除</a>
                                </p:privilege>
                            </c:when>
                            <c:otherwise>
                                <p:privilege name="/joymeapp/version/recover">
                                    <a href="/joymeapp/version/recover?vid=${version.deploymentId}&offset=${page.startRowIdx}&appkey=${version.appkey}&platform=${version.appPlatform.code}&version=${appVersion}&channel=${version.channel }&enterprise=${version.appEnterpriserType.code}">恢复</a>
                                </p:privilege>
                            </c:otherwise>
                        </c:choose>
                        &nbsp;
                        <p:privilege name="/joymeapp/version/modifypage">
                            <a href="/joymeapp/version/modifypage?vid=${version.deploymentId}&offset=${page.startRowIdx}&appkey=${version.appkey}&platform=${version.appPlatform.code}&version=${appVersion}&channel=${version.channel }&enterprise=${version.appEnterpriserType.code}">编辑</a>
                        </p:privilege>
                    </td>
                    <td nowrap><fmt:formatDate value="${version.createDate}"
                                               pattern="yyyy-MM-dd"/><br/>${version.createUserId}<br/>${version.createIp}
                    </td>
                    <td nowrap><fmt:formatDate value="${version.modifyDate}"
                                               pattern="yyyy-MM-dd"/><br/>${version.modifyUserId}<br/>${version.modifyIp}
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td height="1" colspan="17" class="default_line_td"></td>
            </tr>
        </c:when>
        <c:otherwise>
            <tr>
                <td colspan="17" class="error_msg_td">暂无数据!</td>
            </tr>
        </c:otherwise>
    </c:choose>
    <tr>
        <td colspan="17" height="1" class="default_line_td"></td>
    </tr>
    <c:if test="${page.maxPage > 1}">
        <tr class="list_table_opp_tr">
            <td colspan="17">
                <LABEL>
                    <pg:pager url="/joymeapp/version/list"
                              items="${page.totalRows}" isOffset="true"
                              maxPageItems="${page.pageSize}"
                              export="offset, currentPageNumber=pageNumber" scope="request">
                        <pg:param name="appkey" value="${appKey}"/>
                        <pg:param name="platform" value="${platform}"/>
                        <pg:param name="version" value="${appVersion}"/>
                        <pg:param name="channel" value="${channel}"/>
                        <pg:param name="currentPageNumber" value="${page.curPage}"/>
                        <pg:param name="maxPageItems" value="${page.pageSize}"/>
                        <pg:param name="items" value="${page.totalRows}"/>
                        <%@ include file="/WEB-INF/jsp/toolspg.jsp" %>
                    </pg:pager>
                </LABEL>
            </td>
        </tr>
    </c:if>
</table>
</td>
</tr>
</table>
</body>
</html>