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
    <title>后台数据管理-创建广告发布</title>
    <script language="JavaScript" type="text/JavaScript">
        $().ready(function() {
            doOnLoad();
            $('#form_addapublish').submit(function() {
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

                var agentid=$('#publish_agentid').val();
                if(agentid=='' ||agentid<=0){
                    $('#error_agent').text('请选择广告商');
                    errorFlag=true;
                }
                var projectid=$('#publish_projectid').val();
                if(projectid=='' ||projectid<=0){
                    $('#error_project').text('请选择广告项目');
                    errorFlag=true;
                }

                var rurl=$.trim($('#input_rurl').val());
                if(rurl.length==0 || rurl=='http://'|| rurl=='http://'){
                    $('#error_rel').text('请填写跳转地址');
                    errorFlag=true;
                }


                if(errorFlag){
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
        <td height="22" class="page_navigation_td">>> 运营管理 >> 广告管理 >> 广告位管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top">
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <form action="/advertise/publish/add" method="POST" id="form_addapublish">
                    <input type="hidden" name="source" value="${source}">
                    <tr>
                        <td height="1" colspan="4" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td width="10%" align="right" class="edit_table_notnulltitle_td">广告发布名称：</td>
                        <td nowrap width="660" class="edit_table_value_td">
                            <input style="float:left;" name="publishname" type="text" class="default_input_singleline" size="64" maxlength="64" value="" id="input_publishname">
                            <div style="float:left;" id="error_publishname" class="error_msg_td">
                                <c:if test="${errorMsgMap['publishName']!=null}">
                                    <fmt:message key="${errorMsgMap['publishName']}" bundle="${error}"/>
                                </c:if>
                            </div>
                            * 广告商的中文名称。
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_notnulltitle_td">广告商：</td>
                        <td nowrap class="edit_table_value_td">
                            <c:choose>
                                <c:when test="${agent==null}">
                                    <select style="float:left;" name="agentid" id="publish_agentid" style="width: 160px">
                                        <option value="0">请选择</option>
                                        <c:forEach var="agent" items="${agentList}">
                                            <option value="${agent.agentId}">${agent.agentName}</option>
                                        </c:forEach>
                                    </select>
                                </c:when>
                                <c:otherwise>
                                    <span style="float: left">${agent.agentName}</span>
                                    <input type="hidden" value="${agent.agentId}" name="agentid" id="publish_agentid">
                                </c:otherwise>
                            </c:choose>
                            <div style="float:left;" id="error_agent" class="error_msg_td">
                                <c:if test="${errorMsgMap['agent']!=null}">
                                    <fmt:message key="${errorMsgMap['agent']}" bundle="${error}"/>
                                </c:if>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_notnulltitle_td">选择广告项目：</td>
                        <td nowrap class="edit_table_value_td">
                            <c:choose>
                                <c:when test="${project==null}">
                                    <select style="float:left;" name="projectid" id="publish_projectid" style="width: 160px">
                                        <option value="0">请选择</option>
                                        <c:forEach var="project" items="${projectList}">
                                            <option value="${project.projectId}">${project.projectName}</option>
                                        </c:forEach>
                                    </select>
                                </c:when>
                                <c:otherwise>
                                    <div style="float: left">${project.projectName}</div>
                                    <input type="hidden" value="${project.projectId}" name="projectid" id="publish_projectid">
                                </c:otherwise>
                            </c:choose>
                            <div style="float:left;" id="error_project" class="error_msg_td">
                                <c:if test="${errorMsgMap['project']!=null}">
                                    <fmt:message key="${errorMsgMap['project']}" bundle="${error}"/>
                                </c:if>
                            </div>
                            * 广告项目。
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_notnulltitle_td">跳转地址：</td>
                        <td nowrap class="edit_table_value_td">
                            <input style="float:left;" name="redirecturl" type="text" class="default_input_singleline" size="64" value="http://" id="input_rurl">
                            <div style="float:left;" id="error_rel" class="error_msg_td">
                                <c:if test="${errorMsgMap['redirectUrl']!=null}">
                                    <fmt:message key="${errorMsgMap['redirectUrl']}" bundle="${error}"/>
                                </c:if>
                            </div>
                            * 提转地址请以http://开头。
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_notnulltitle_td">统计截止时间：</td>
                        <td nowrap class="edit_table_value_td">
                            <input  name="statenddate" type="text" class="default_input_singleline" size="8" value="${statEndDate}" id="statEndDate">
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">广告发布描述：</td>
                        <td nowrap class="edit_table_value_td">
                            <textarea style="float:left;" name="publishdesc" rows="5" cols="51" style="width: 418px;" class="default_input_multiline" id="input_publishdesc"></textarea>
                            <div style="float:left;" id="error_publishdesc" class="error_msg_td">
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
                        <td colspan="4">&nbsp;</td>
                    </tr>
                    <tr align="center">
                        <td colspan="4">
                            <input name="Submit" type="submit" class="default_button" value="提交">
                            <input name="Reset" type="button" class="default_button" value="返回" onclick="javascipt:window.history.go(-1);">
                        </td>
                    </tr>
                </form>
            </table>
        </td>
    </tr>
</table>
</body>
</html>