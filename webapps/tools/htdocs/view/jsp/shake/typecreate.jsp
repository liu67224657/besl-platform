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
    <script type="text/javascript" src="/static/include/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $('#timelimit').change(function () {
                if ($(this).val() == 'true') {
                    $('#row_begintime').show();
                    $('#row_endtime').show();
                } else {
                    $('#row_begintime').hide();
                    $('#begintime').val('');
                    $('#row_endtime').hide();
                    $('#endtime').val('');
                }
            });

            $('#form_submit').submit(function () {
                if (isNaN($('#min_range').val()) || isNaN($('#max_range').val())) {
                    alert('权重需要输入0-100的整数');
                    return false;
                }

            });
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
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="">
                        应用: ${app.appName}(${app.appId})&nbsp;&nbsp;&nbsp;&nbsp;
                        平台: <fmt:message key="joymeapp.platform.${config.platform.code}" bundle="${def}"/>&nbsp;&nbsp;&nbsp;&nbsp;
                        版本: ${config.version}&nbsp;&nbsp;&nbsp;&nbsp;
                        渠道: <fmt:message key="joymeapp.channel.type.${config.channel}" bundle="${def}"/>&nbsp;&nbsp;&nbsp;&nbsp;
                        是否是企业版:<c:choose> <c:when test="${config.enterpriseType.code == 1}">否</c:when> <c:when
                            test="${config.enterpriseType.code == 2}">是</c:when> <c:otherwise></c:otherwise></c:choose>&nbsp;&nbsp;&nbsp;&nbsp;
                        摇一摇开关:${config.info.shake_open}
                    </td>
                </tr>
            </table>
            <form action="/shake/type/create" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td">
                            <input type="hidden" name="configid" value="${config.configId}"/>
                            <%--<input type="hidden" name="appkey" value="${config.appKey}"/>--%>
                            <%--<input type="hidden" name="platform" value="${config.platform.code}"/>--%>
                            <%--<input type="hidden" name="version" value="${config.version}"/>--%>
                            <%--<input type="hidden" name="channel" value="${config.channel}"/>--%>
                            <%--<input type="hidden" name="enterprise" value="${config.enterpriseType.code}"/>--%>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            摇一摇按钮文字:
                        </td>
                        <td>
                            <input type="text" name="buttontext" value=""/>
                        </td>
                        <td height="1">
                            <span style="color: #ff0000">摇一摇的按钮文字</span>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            摇一摇文案:
                        </td>
                        <td>
                            <input type="text" name="title" value="">
                        </td>
                        <td height="1">
                            <span style="color: #ff0000">摇一摇的文案</span>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            摇一摇类型:
                        </td>
                        <td>
                            <select name="shaketype">
                                <c:forEach var="type" items="${types}">
                                    <option value="${type.code}"><fmt:message key="shake.type.${type.code}"
                                                                              bundle="${def}"/></option>
                                </c:forEach>
                            </select>
                        </td>
                        <td height="1">
                            <span style="color: #ff0000">摇一摇类型</span>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            每个人摇一摇的次数:
                        </td>
                        <td>
                            <input type="text" name="shaketimes" value="0">
                        </td>
                        <td height="1">
                            <span style="color: #ff0000">摇一摇类型</span>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            是否有时间限制:
                        </td>
                        <td>
                            <select name="timelimit" id="timelimit">
                                <option value="false">无</option>
                                <option value="true">有</option>
                            </select>
                        </td>
                        <td height="1">
                            <span style="color: #ff0000">摇一摇时间限制</span>
                        </td>
                    </tr>
                    <tr id="row_begintime" style="display: none">
                        <td height="1" class="default_line_td" width="100">
                            开始时间:
                        </td>
                        <td>
                            <input type="text" class="Wdate"
                                   onClick="WdatePicker({autoPickDate:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"
                                   readonly="readonly" name="begintime" id="begintime"/>
                        </td>
                        <td height="1">
                            <span style="color: #ff0000">摇一摇时间限制</span>
                        </td>
                    </tr>
                    <tr id="row_endtime" style="display: none">
                        <td height="1" class="default_line_td" width="100">
                            结束时间:
                        </td>
                        <td>
                            <input type="text" class="Wdate"
                                   onClick="WdatePicker({autoPickDate:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"
                                   readonly="readonly" name="endtime" id="endtime"/>
                        </td>
                        <td height="1">
                            <span style="color: #ff0000">摇一摇时间限制</span>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            权重:
                        </td>
                        <td>
                            最小：<input type="text" name="min_range" id="min_range" value="0">
                            最大：<input type="text" name="max_range" id="max_range" value="100">
                        </td>
                        <td height="1">
                            <span style="color: #ff0000"><100的正整数，用于摇一摇结果的权重</span>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            tag相关:
                        </td>
                        <td>
                            tag:<input type="text" name="tag" value="0">
                            开始时间：<input type="text" class="Wdate"
                                        onClick="WdatePicker({autoPickDate:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"
                                        readonly="readonly" name="tagbegintime" id="tagbegintime"/>
                            结束时间：<input type="text" class="Wdate"
                                        onClick="WdatePicker({autoPickDate:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"
                                        readonly="readonly" name="tagendtime" id="tagendtime"/>
                        </td>
                        <td height="1">
                            <span style="color: #ff0000"></span>
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