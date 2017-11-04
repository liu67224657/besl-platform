<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/dhtmlxcalendar.css">
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/skins/dhtmlxcalendar_dhx_skyblue.css">
    <script src="/static/include/dhtmlxcalendar/dhtmlxcalendar.js"></script>
    <title>后台数据管理-修改广告发布</title>
    <script language="JavaScript" type="text/JavaScript">
        $().ready(function () {
            doOnLoad();
            $('#form_modifypublish').submit(function () {
                var errorFlag = false;

                var agentName = $('#input_publishname').val();
                if (agentName == '') {
                    $('#error_publishname').text('广告发布名字不能为空');
                    errorFlag = true;
                } else if (agentName.length > 64) {
                    $('#error_publishname').text('广告发布名字小于64个字');
                    errorFlag = true;
                }

                var desc = $('#input_publishdesc').val();
                if (desc.length > 200) {
                    $('#error_publishdesc').text('广告发布描述小于200个字');
                    errorFlag = true;
                }

                var rurl = $.trim($('#input_rurl').val());
                if (rurl.length == 0 || rurl == 'http://' || rurl == 'http://') {
                    $('#error_rel').text('请填写跳转地址');
                    errorFlag = true;
                }

                if (errorFlag) {
                    return false;
                }
            });
        });
        var myCalendar;
        function doOnLoad() {
            myCalendar = new dhtmlXCalendarObject(["startDate", "endDate","statEndDate"]);
        }
    </script>
    <style type="text/css">
        td, td div {
            overflow: hidden;
            text-overflow: ellipsis; /* for IE */
            -moz-text-overflow: ellipsis; /* for Firefox,mozilla */
            white-space: nowrap;
        }

    </style>
</head>

<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 广告管理 >> 广告位管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top">
            <form action="/advertise/publish/modify" method="POST" id="form_modifypublish">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <input type="hidden" name="source" value="${source}">
                    <input type="hidden" name="publishid" value="${publish.publishId}">
                    <tr>
                        <td height="1" colspan="2" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td width="10%" align="right" class="edit_table_notnulltitle_td">广告发布名称：</td>
                        <td width="680" nowrap class="edit_table_value_td">
                            <input style="float: left" name="publishname" type="text" class="default_input_singleline"
                                   size="64" maxlength="64" value="${publish.publishName}" id="input_publishname"/>

                            <div style="float: left" id="error_publishname" class="error_msg_td">
                                <c:if test="${errorMsgMap['publishName']!=null}">
                                    <fmt:message key="${errorMsgMap['publishName']}" bundle="${error}"/>
                                </c:if>
                            </div>
                            * 广告发布的中文名称。
                        </td>
                    </tr>
                    <tr>
                        <td align="right" class="edit_table_notnulltitle_td">广告商：</td>
                        <td nowrap class="edit_table_value_td">
                            ${agent.agentName}
                            <input type="hidden" value="${agent.agentId}" name="agentid">
                        </td>
                    </tr>
                    <tr>
                        <td align="right" class="edit_table_notnulltitle_td">广告项目：</td>
                        <td nowrap class="edit_table_value_td">
                            ${project.projectName}
                            <input type="hidden" value="${project.projectId}" name="projectid">
                        </td>
                    </tr>
                    <tr>
                        <td align="right" class="edit_table_notnulltitle_td">广告发布链接：</td>
                        <td nowrap class="edit_table_value_td">
                            <%--<input style="float: left" name="redirecturl" type="text" class="default_input_singleline" size="64" value="${publish.redirectUrl}" >--%>
                            ${URL_WWW}/click/${publish.publishId}
                        </td>
                    </tr>
                    <tr>
                        <td align="right" class="edit_table_notnulltitle_td">跳转地址：</td>
                        <td nowrap class="edit_table_value_td">
                            <input style="float: left" name="redirecturl" type="text" class="default_input_singleline"
                                   size="64" value="${publish.redirectUrl}" id="input_rurl">

                            <div style="float: left" class="error_msg_td" id="error_rel">
                                <c:if test="${errorMsgMap['redirectUrl']!=null}">
                                    <fmt:message key="${errorMsgMap['redirectUrl']}" bundle="${error}"/>
                                </c:if>
                            </div>
                            * 提转地址请以http://开头。
                        </td>
                    </tr>
                    <tr>
                        <td align="right" class="edit_table_notnulltitle_td">状态：</td>
                        <td nowrap class="edit_table_value_td">
                            <select name="validstatus">
                                <option value="valid" <c:if test="${publish.validStatus.code eq 'valid'}">selected</c:if>>有效
                                </option>
                                <option value="invalid" <c:if test="${publish.validStatus.code eq 'invalid'}">selected</c:if>>
                                    无效
                                </option>
                                <option value="removed" <c:if test="${publish.validStatus.code eq 'removed'}">selected</c:if>>
                                    删除
                                </option>
                            </select> * 广告发布的状态。
                        </td>
                    </tr>
                    <tr>
                        <td align="right" class="edit_table_notnulltitle_td">统计截止时间：</td>
                        <td nowrap class="edit_table_value_td">
                            <input  name="statenddate" type="text" class="default_input_singleline" size="8"
                                    value="<fmt:formatDate value="${publish.statEndDate}" pattern="yyyy-MM-dd"/>" id="statEndDate">
                        </td>
                    </tr>
                    <tr>
                        <td align="right" class="edit_table_defaulttitle_td">广告发布描述：</td>
                        <td nowrap class="edit_table_value_td">
                            <textarea style="float: left" name="publishdesc" rows="5" cols="51" style="width: 418px;"
                                      class="default_input_multiline"
                                      id="input_publishdesc">${publish.publishDesc}</textarea>

                            <div style="float: left" id="error_publishdesc" class="error_msg_td">
                                <c:if test="${errorMsgMap['publishDesc']!=null}">
                                    <fmt:message key="${errorMsgMap['publishDesc']}" bundle="${error}"/>
                                </c:if>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" colspan="2" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td colspan="2">&nbsp;</td>
                    </tr>
                    <tr align="center">
                        <td colspan="2">
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