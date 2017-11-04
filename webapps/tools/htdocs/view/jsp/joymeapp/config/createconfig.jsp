<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>添加配置</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/jquery-1.11.2.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $('#form_submit').bind('submit', function () {
                var app = $('#select_appkey').val();
                if (app.length <= 0) {
                    alert('请选择应用');
                    return false;
                }

                var platform = $('#select_platform').val();
                if (platform.length <= 0) {
                    alert('请选择平台');
                    return false;
                }

                var version = $('#input_version').val();
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
    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷APP >> APP配置列表</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">添加配置</td>
                </tr>
            </table>
            <form action="/joymeapp/config/create" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            应用:
                        </td>
                        <td height="1">
                            <select id="select_appkey" name="appkey">
                                <option value="">请选择</option>
                                <c:forEach items="${appList}" var="app" varStatus="st">
                                    <option value="${app.appId}">${app.appName}</option>
                                </c:forEach>
                            </select>*必填项
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            平台:
                        </td>
                        <td height="1">
                            <select id="select_platform" name="platform">
                                <option value="">请选择</option>
                                <c:forEach items="${platformSet}" var="plat">
                                    <option value="${plat.code}">
                                        <fmt:message key="joymeapp.platform.${plat.code}" bundle="${def}"/>
                                    </option>
                                </c:forEach>
                            </select>*必填项
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            版本:
                        </td>
                        <td>
                            <input id="input_version" type="text" name="version"/>*必填项
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            渠道:
                        </td>
                        <td>
                            <input type="hidden" name="channel" id="input_channel"/>
                            <input type="checkbox" id="all_channel" onclick="checkAllChannel($(this))"/>全选
                            <input type="checkbox" id="inverse_channel" onclick="checkInverseChannel()"/>反选<br/>
                            <c:forEach var="channel" items="${channelSet}" varStatus="st">
                                <input type="checkbox" name="check_channel" value="${channel.code}"/>
                                <fmt:message key="joymeapp.channel.type.${channel.code}" bundle="${def}"/>&nbsp;
                                <c:if test="${st.index % 6 ==5}"><br/></c:if>
                            </c:forEach>
                            <br/>*必填项
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            审核期间广告:
                        </td>
                        <td>
                            <input id="input_ad" type="text" name="defad_url" size="40">
                            <br/>
                            <span style="color: #ff0000">审核期间广告不为空，审核通过后，需手动改为空</span>
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
                                <option value="false">关</option>
                                <option value="true">开</option>
                            </select><span style="color: #ff0000">控制"摇一摇"是否出现</span>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            礼包中心开关:
                        </td>
                        <td>
                            <select name="gift_open">
                                <option value="false">关</option>
                                <option value="true">开</option>
                            </select><span style="color: #ff0000">控制"礼包中心"是否打开</span>
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