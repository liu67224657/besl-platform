<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <title>后台数据管理-创建广告商</title>
    <script language="JavaScript" type="text/JavaScript">
        $().ready(function() {
            $('#form_addagent').submit(function() {
                var errorFlag = false;

                var agentName = $('#input_agentname').val();
                if (agentName == '') {
                    $('#error_agentname').text('广告商名字不能为空');
                    errorFlag = true;
                } else if (agentName.length > 64) {
                    $('#error_agentname').text('广告商名字小于64个字');
                    errorFlag = true;
                } else{
                    $('#error_agentname').text('');
                }

                var desc = $('#input_agentdesc').val();
                if (desc.length > 200) {
                    $('#error_agentdesc').text('广告商描述小于200个字');
                    errorFlag = true;
                }else{
                    $('#error_agentdesc').text('');
                }

                if(errorFlag){
                    return false;
                }
            });
        });
    </script>
    <style type="text/css">
        td,td div{
            overflow: hidden;
            text-overflow:ellipsis; /* for IE */
            -moz-text-overflow: ellipsis; /* for Firefox,mozilla */
            white-space: nowrap;

        }
    </style>
</head>

<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="100%" valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="22" class="page_navigation_td">>> 运营维护 >> 广告管理 >> <a href="/advertise/agent/list">广告商管理</a>
                    </td>
                </tr>
            </table>
            <br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">广告商信息</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <form action="/advertise/agent/add" method="POST" id="form_addagent">
                    <tr>
                        <td height="1" colspan="2" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td width="10%" align="right" class="edit_table_notnulltitle_td">广告商名称：</td>
                        <td nowrap width="660" class="edit_table_value_td">
                            <input style="float:left" name="agentname" type="text" class="default_input_singleline" size="64"
                                   maxlength="64" value="" id="input_agentname">
                            <div style="float:left" id="error_agentname" class="error_msg_td">
                                <c:if test="${errorMsgMap['agentName']!=null}">
                                    <fmt:message key="${errorMsgMap['agentName']}" bundle="${error}"/>
                                </c:if>
                            </div>
                            * 广告商的中文名称。
                        </td>
                    </tr>
                    <tr>
                        <td align="right" class="edit_table_defaulttitle_td">广告商描述：</td>
                        <td nowrap class="edit_table_value_td" >
                            <textarea style="float: left" name="agentdesc" rows="5" cols="51" style="width: 418px;"
                                      class="default_input_multiline" id="input_agentdesc"></textarea>
                            <div style="float: left" id="error_agentdesc" class="error_msg_td">
                                <c:if test="${errorMsgMap['agentDesc']!=null}">
                                    <fmt:message key="${errorMsgMap['agentDesc']}" bundle="${error}"/>
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
                </form>
            </table>
        </td>
    </tr>
</table>
</body>
</html>