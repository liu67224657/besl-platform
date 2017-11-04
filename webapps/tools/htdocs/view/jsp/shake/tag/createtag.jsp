<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>tag软文列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/jquery-1.11.2.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript" src="/static/include/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $('#form_submit').bind('submit', function () {
                var platform = $('#select_platform').val();
                if (platform.length <= 0) {
                    alert('请选择平台');
                    return false;
                }

                var version = '';
                $('input[name=check_version]:checkbox').each(function () {
                    if ($(this).is(":checked")) {
                        version += ($(this).val() + "|");
                    }
                });

                $('#input_version').val(version);
                if (version.length <= 0) {
                    alert('请填写版本');
                    return false;
                }

                var channel = '';
                $('input[name=check_channel]:checkbox').each(function () {
                    if ($(this).is(":checked")) {
                        channel += ($(this).val() + "|");
                    }
                });

                $('#input_channel').val(channel);
                if (channel.length <= 0) {
                    alert('请选择渠道');
                    return false;
                }
            });
        });

        function getVersion() {
            var appKey = $('#input_hidden_appkey').val();
            var platform = $("#select_platform").find("option:selected").val();
            var ent = $('#select_enterprise').val();
            if (platform.length == 0) {
                alert("请选择平台");
                return false;
            }
            $.post("/joymeapp/shake/tag/getversion", {
                platform: platform,
                appkey: appKey,
                enterprise: ent
            }, function (req) {
                var resMsg = eval('(' + req + ')');
                if (resMsg.rs == 0) {
                    return false;
                } else {
                    if (resMsg.result == null) {
                        return;
                    }
                    var radioHtml = '<input type="checkbox" id="all_version" onclick="checkAllVersion($(this))"/>全选<input type="checkbox" id="inverse_version" onclick="checkInverseVersion()"/>反选<br/>';
                    if (resMsg.result.versionList != null && resMsg.result.versionList.length > 0) {
                        var versionList = resMsg.result.versionList;
                        for (var i = 0; i < versionList.length; i++) {
                            radioHtml += '<input type="checkbox" name="check_version" value="' + versionList[i] + '"/>' + versionList[i] + '&nbsp;'
                            if (i % 6 == 5) {
                                radioHtml += '<br/>';
                            }
                        }
                    }
                    radioHtml += '<br/><span style="color: red">*必选项</span>';
                    $('#td_version').html(radioHtml);

                    var radioHtml = '<input type="checkbox" id="all_channel" onclick="checkAllChannel($(this))"/>全选<input type="checkbox" id="inverse_channel" onclick="checkInverseChannel()"/>反选<br/>';
                    if (resMsg.result.channelList != null && resMsg.result.channelList.length > 0) {
                        var channelList = resMsg.result.channelList;
                        for (var i = 0; i < channelList.length; i++) {
                            radioHtml += '<input type="checkbox" name="check_channel" value="' + channelList[i].code + '"/>' + channelList[i].name + '&nbsp;'
                            if (i % 6 == 5) {
                                radioHtml += '<br/>';
                            }
                        }
                    }
                    radioHtml += '<br/><span style="color: red">*必选项</span>';
                    $('#td_channel').html(radioHtml);
                }
            });
        }

        function checkAllChannel(dom) {
            if (dom.prop('checked')) {
                $('input[name=check_channel]:checkbox').each(function () {
                    $(this).prop('checked', true);
                });
            } else {
                $('input[name=check_channel]:checkbox').each(function () {
                    $(this).prop('checked', false);
                });
            }
        }

        function checkInverseChannel() {
            $('input[name=check_channel]:checkbox').each(function () {
                if ($(this).prop('checked')) {
                    $(this).prop('checked', false);
                } else {
                    $(this).prop('checked', true);
                }
            });
        }

        function checkAllVersion(dom) {
            if (dom.prop('checked')) {
                $('input[name=check_version]:checkbox').each(function () {
                    $(this).prop('checked', true);
                });
            } else {
                $('input[name=check_version]:checkbox').each(function () {
                    $(this).prop('checked', false);
                });
            }
        }

        function checkInverseVersion() {
            $('input[name=check_version]:checkbox').each(function () {
                if ($(this).prop('checked')) {
                    $(this).prop('checked', false);
                } else {
                    $(this).prop('checked', true);
                }
            });
        }
    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷手游画报管理 >> 玩霸摇一摇tag管理</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">批量（添加/编辑）</td>
                </tr>
            </table>
            <form action="/joymeapp/shake/tag/create" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td">
                            <input type="hidden" name="appkey" value="${appKey}" id="input_hidden_appkey"/>
                            <input type="hidden" value="${appChannel}" id="input_hidden_channel"/>
                            <input type="hidden" value="${appVersion}" id="input_hidden_version"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            平台:
                        </td>
                        <td height="1">
                            <select id="select_platform" name="platform" onchange="getVersion()">
                                <option value="">请选择</option>
                                <c:forEach items="${platformSet}" var="plat">
                                    <option value="${plat.code}" <c:if test="${plat.code == appPlatform}">selected="selected"</c:if>>
                                        <fmt:message key="joymeapp.platform.${plat.code}" bundle="${def}"/>
                                    </option>
                                </c:forEach>
                            </select>*必填项
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            是否企业版
                        </td>
                        <td height="1">
                            <select id="select_enterprise" name="enterprise" onchange="getVersion()">
                                <option value="1" <c:if test="${appEnterprise == 1}">selected="selected"</c:if>>否</option>
                                <option value="2" <c:if test="${appEnterprise == 2}">selected="selected"</c:if>>是</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            选择版本:
                            <input type="hidden" id="input_version" name="versions" value=""/>
                        </td>
                        <td height="1" id="td_version">
                            <input type="checkbox" id="all_version" onclick="checkAllVersion($(this))"/>全选
                            <input type="checkbox" id="inverse_version" onclick="checkInverseVersion()"/>反选<br/>
                            <c:forEach items="${versionSet}" var="version" varStatus="st">
                                <input type="checkbox" name="check_version" value="${version}" <c:if test="${version == appVersion}">checked="checked"</c:if>/>${version}&nbsp;
                                <c:if test="${st.index % 6 == 5}"><br/></c:if>
                            </c:forEach>
                            <br/>
                            <span style="color: red">*必选项</span>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            选择渠道:
                            <input type="hidden" id="input_channel" name="channels" value=""/>
                        </td>
                        <td height="1" id="td_channel">
                            <input type="checkbox" id="all_channel" onclick="checkAllChannel($(this))"/>全选
                            <input type="checkbox" id="inverse_channel" onclick="checkInverseChannel()"/>反选<br/>
                            <c:forEach items="${channelSet}" var="channel" varStatus="st">
                                <input type="checkbox" name="check_channel" value="${channel.code}" <c:if test="${channel.code == appChannel}">checked="checked"</c:if>/>${channel.name}&nbsp;
                                <c:if test="${st.index % 6 == 5}"><br/></c:if>
                            </c:forEach>
                            <br/>
                            <span style="color: red">*必选项</span>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            tag软文:
                        </td>
                        <td>
                            <input id="input_tag" type="text" name="tag" size="40">
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            开始时间:
                        </td>
                        <td height="1">
                            <input id="input_begintime" name="begintime"
                                   onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',autoPickDate:true})"
                                   readonly/>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            结束时间:
                        </td>
                        <td height="1">
                            <input id="input_endtime" name="endtime"
                                   onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',autoPickDate:true})"
                                   readonly/>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
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