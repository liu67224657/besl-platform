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
    <title>后台数据管理-创建广告项目</title>
    <script language="JavaScript" type="text/JavaScript">
        $().ready(function() {
            doOnLoad();

            $('#form_addproject').submit(function() {
                var errorFlag = false;

                var projectName = $('#input_projectname').val();
                if (projectName == '') {
                    $('#error_projectname').text('广告项目名字不能为空');
                    errorFlag = true;
                } else if (projectName.length > 64) {
                    $('#error_projectname').text('广告项目名字小于64个字');
                    errorFlag = true;
                } else{
                    $('#error_projectname').text('');
                }

                var desc = $('#input_projectdesc').val();
                if (desc.length > 200) {
                    $('#error_projectdesc').text('广告项目描述小于200个字');
                    errorFlag = true;
                }else{
                    $('#error_projectdesc').text('');
                }
                if($("#input_project_startdate").val() == ''){
                    $("#input_project_startdate").next().text("起始时间不能为空");
                    errorFlag = true;
                }else if($("#input_project_startdate").val().length!=10){
                    $("#input_project_startdate").next().text("起始时间格式不正确");
                    errorFlag = true;
                }else{
                    $("#input_project_startdate").next().text("");
                }
                if($("#input_project_enddate").val() == ''){
                    $("#input_project_enddate").next().text("结束时间不能为空");
                    errorFlag = true;
                }else if($("#input_project_enddate").val().length!=10){
                    $("#input_project_enddate").next().text("结束时间格式不正确");
                    errorFlag = true;
                }else if($("#input_project_enddate").val()<$("#input_project_startdate").val()){
                    $("#input_project_enddate").next().text("结束时间大于起始时间");
                    errorFlag = true;
                }else{
                    $("#input_project_enddate").next().text("");
                }
                if($("#input_project_statenddate").val() == ''){
                    $("#input_project_statenddate").next().text("统计结束时间不能为空");
                    errorFlag = true;
                }else if($("#input_project_statenddate").val().length!=10){
                    $("#input_project_statenddate").next().text("统计结束时间格式不正确");
                    errorFlag = true;
                }else{
                    $("#input_project_statenddate").next().text("");
                }

                if(errorFlag){
                    return false;
                }
            });
        })
        var myCalendar;
        function doOnLoad() {
            myCalendar = new dhtmlXCalendarObject(["input_project_startdate", "input_project_enddate","input_project_statstartdate", "input_project_statenddate"]);
        }
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
                    <td height="22" class="page_navigation_td">>> 运营维护 >> 广告管理 >> <a href="/advertise/project/list">广告项目管理</a>
                </tr>
            </table>
            <br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">广告项目信息</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <form action="/advertise/project/add" method="POST" id="form_addproject">
                    <tr>
                        <td height="1" colspan="4" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td width="10%" align="right" class="edit_table_notnulltitle_td">广告项目名称：</td>
                        <td nowrap width="660" class="edit_table_value_td">
                            <input style="float:left" name="projectname" type="text" class="default_input_singleline" size="64" maxlength="64" value="" id="input_projectname">
                            <div style="float:left" id="error_projectname" class="error_msg_td">
                                <c:if test="${errorMsgMap['projectName']!=null}">
                                    <fmt:message key="${errorMsgMap['projectName']}" bundle="${error}"/>
                                </c:if>
                            </div>
                            * 广告商的中文名称。
                        </td>
                    </tr>
                    <tr>
                        <td align="right" class="edit_table_notnulltitle_td">起始时间：</td>
                        <td nowrap class="edit_table_value_td">
                            <input style="float:left" name="startdate" type="text" class="default_input_singleline" size="32" value="" id="input_project_startdate">
                            <div style="float:left;color: red" ></div>
                            *广告项目起始时间。
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_notnulltitle_td">结束时间：</td>
                        <td nowrap class="edit_table_value_td">
                            <input style="float:left"  name="enddate" type="text" class="default_input_singleline" size="32" value="" id="input_project_enddate">
                            <div style="float:left;color: red" ></div>*广告项目结束时间。
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_notnulltitle_td">统计结束时间：</td>
                        <td nowrap class="edit_table_value_td">
                            <input style="float:left"  name="statenddate" type="text" class="default_input_singleline" size="32" value="" id="input_project_statenddate">
                            <div style="float:left;color: red" ></div>*广告项目统计结束时间。
                        </td>
                    </tr>
                    <tr>
                        <td align="right" class="edit_table_defaulttitle_td">广告项目描述：</td>
                        <td nowrap class="edit_table_value_td">
                            <textarea style="float:left" name="projectdesc" rows="5" cols="51" style="width: 418px;" class="default_input_multiline" id="input_projectdesc"></textarea>
                            <div style="float:left" id="error_projectdesc" class="error_msg_td">
                                <c:if test="${errorMsgMap['projectDesc']!=null}">
                                    <fmt:message key="${errorMsgMap['projectDesc']}" bundle="${error}"/>
                                </c:if>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" colspan="4" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td colspan="4">&nbsp;</td>
                    </tr>
                    <tr align="center">
                        <td colspan="4">
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