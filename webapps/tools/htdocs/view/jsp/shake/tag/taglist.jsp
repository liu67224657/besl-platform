<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>tag软文列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $('#select_platform').change(function () {
                getVersion();
            });

            $('#select_enterprise').change(function () {
                getVersion();
            });

            $('#select_version').change(function () {
                getChannel();
            });

            var appkey = $('#input_hidden_appkey').val();
            var platform = $('#select_platform').val();
            var ent = $('#select_enterprise').val();
            if (appkey.length <= 0 || platform.length <= 0) {
                $('#select_version').html('<option value="">请选择应用、平台</option>');
                $('#select_channel').html('<option value="">请选择应用、平台、版本</option>');
                return;
            }
            $.post("/joymeapp/shake/tag/getversion", {
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
                    $.post("/joymeapp/shake/tag/getversion", {
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
                                if (channelList[i].code == $('#input_hidden_channel').val()) {
                                    channelHtml += '<option value="' + channelList[i].code + '" selected="selected">' + channelList[i].name + '</option>';
                                } else {
                                    channelHtml += '<option value="' + channelList[i].code + '">' + channelList[i].name + '</option>';
                                }
                            }
                            $('#select_channel').html(channelHtml);
                        }
                    });
                }
            });
        });

        function getVersion() {
            var appkey = $('#input_hidden_appkey').val();
            var platform = $('#select_platform').val();
            var ent = $('#select_enterprise').val();
            if (appkey.length <= 0 || platform.length <= 0) {
                $('#select_version').html('<option value="">请选择应用、平台</option>');
                $('#select_channel').html('<option value="">请选择应用、平台、版本</option>');
                return;
            }
            $.post("/joymeapp/shake/tag/getversion", {
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
            var appkey = $('#input_hidden_appkey').val();
            var platform = $('#select_platform').val();
            var ent = $('#select_enterprise').val();
            var version = $('#select_version').val();
            if (appkey.length <= 0 || platform.length <= 0 || version.length <= 0) {
                return;
            }
            $.post("/joymeapp/shake/tag/getversion", {
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
                        channelHtml += '<option value="' + channelList[i].code + '">' + channelList[i].name + '</option>';
                    }
                    $('#select_channel').html(channelHtml);
                }
            });
        }
    </script>
</head>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷手游画报管理 >> 玩霸摇一摇tag管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">>tag软文列表</td>
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
            <table width="90%" border="0" cellspacing="0" cellpadding="0">
                <form action="/joymeapp/shake/tag/list" method="post" id="form_submit_search">
                    <tr>
                        <td width="80" align="center">搜索条件</td>
                        <td>
                            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                                <tr>
                                    <td>
                                        <input type="hidden" name="appkey" value="${appKey}" id="input_hidden_appkey"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="right" class="edit_table_defaulttitle_td">平台：</td>
                                    <td>
                                        <select id="select_platform" name="platform">
                                            <option value="">请选择</option>
                                            <c:forEach items="${platformSet}" var="plat">
                                                <option value="${plat.code}"
                                                <c:if test="${plat.code == appPlatform}">selected="selected"</c:if>>
                                                <fmt:message key="joymeapp.platform.${plat.code}" bundle="${def}"/>
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td align="right" class="edit_table_defaulttitle_td">是否企业版：</td>
                                    <td>
                                        <select id="select_enterprise" name="enterprise">
                                            <option value="1"
                                            <c:if test="${enterprise == 1}">selected="selected"</c:if>>否</option>
                                            <option value="2"
                                            <c:if test="${enterprise == 2}">selected="selected"</c:if>>是</option>
                                        </select>
                                    </td>
                                </tr>
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
                                        <input type="hidden" id="input_hidden_channel" value="${appChannel}"/>
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
                        <a href="/joymeapp/shake/tag/createpage?appkey=${appKey}&platform=${appPlatform}&enterprise=${appEnterprise}&version=${appVersion}&channel=${appChannel}">
                            <input type="button" class="default_button" value="批量（添加/编辑）"/>
                        </a>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="10" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center">tag软文</td>
                    <td nowrap align="center">平台</td>
                    <td nowrap align="center">版本</td>
                    <td nowrap align="center">渠道</td>
                    <td nowrap align="center">是否企业版</td>
                    <td nowrap align="center">有效期</td>
                </tr>
                <tr>
                    <td height="1" colspan="6" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="config" varStatus="st">
                            <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                <td nowrap><input type="hidden" name="configid" value="${config.configId}"/>
                    <c:if test="${config.info != null && config.info.shake_tag != null}">${config.info.shake_tag.tag}</c:if>
                </td>
                <td nowrap>
                    <fmt:message key="joymeapp.platform.${config.platform.code}" bundle="${def}"/>
                </td>
                <td nowrap>${config.version}</td>
                <td nowrap>
                    <fmt:message key="joymeapp.channel.type.${config.channel}" bundle="${def}"/>
                </td>
                <td nowrap><c:if test="${config.enterpriseType.code == 1}">否</c:if><c:if
                        test="${config.enterpriseType.code == 2}">是</c:if></td>
                <td nowrap><c:if
                        test="${config.info != null && config.info.shake_tag != null && config.info.shake_tag.begintime > 0 && config.info.shake_tag.endtime > 0}">
                    <fmt:formatDate value="${date:long2date(config.info.shake_tag.begintime)}"
                                    pattern="yyyy-MM-dd HH:mm:ss"/><br/>
                    <fmt:formatDate value="${date:long2date(config.info.shake_tag.endtime)}"
                                    pattern="yyyy-MM-dd HH:mm:ss"/></c:if>
                </td>
                </tr>
                </c:forEach>
                <tr>
                    <td height="1" colspan="10" class="default_line_td"></td>
                </tr>
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
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="10">
                            <LABEL>
                                <pg:pager url="/joymeapp/shake/tag/list"
                                          items="${page.totalRows}" isOffset="true"
                                          maxPageItems="${page.pageSize}"
                                          export="offset, currentPageNumber=pageNumber" scope="request">
                                    <pg:param name="appkey" value="${appKey}"/>
                                    <pg:param name="platform" value="${appPlatform}"/>
                                    <pg:param name="version" value="${appVersion}"/>
                                    <pg:param name="channel" value="${appChannel}"/>
                                    <pg:param name="enterprise" value="${appEnterprise}"/>
                                    <pg:param name="currentPageNumber" value="${page.curPage}"/>
                                    <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                    <pg:param name="items" value="${page.totalRows}"/>
                                    <%@ include file="/WEB-INF/jsp/toolspgnoincludejquery.jsp" %>
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