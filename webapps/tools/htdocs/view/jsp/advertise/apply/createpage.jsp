<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title></title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery-1.11.2.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/dhtmlxcalendar.css">
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/skins/dhtmlxcalendar_dhx_skyblue.css">
    <script src="/static/include/dhtmlxcalendar/dhtmlxcalendar.js"></script>
    <style type="text/css">
        .td_cent {
            text-align: center;
            vertical-align: middle
        }

        ;
    </style>

    <script>
        $().ready(function () {
            doOnLoad();

            $('#form_submit').bind('submit', function () {
                if ($("#input_text_publishName").val() == '') {
                    alert("广告发布名称");
                    $("#input_text_publishName").focus();
                    return false;
                }
                if ($("#input_text_advertiseId").val() == '') {
                    alert("请广告素材ID");
                    $("#input_text_advertiseId").focus();
                    return false;
                }
                if (isNaN($("#input_text_advertiseId").val())) {
                    alert("广告素材ID需为数字");
                    $("#input_text_advertiseId").focus();
                    return false;
                }

                if ($("#startdate").val() == '') {
                    alert("请开始时间");
                    $("#startdate").focus();
                    return false;
                }
                if ($("#enddate").val() == '') {
                    alert("请结束时间");
                    $("#enddate").focus();
                    return false;
                }
                var type = $('input[name=publish_type]:checked').val();
                var param = $("#publish_param").val();
                if(type == '0' || type == '1' || type == '2' || type == '3' || type == '8'){
                    if(param == ''){
                        alert("请填写广告参数");
                        $("#publish_param").focus();
                        return false;
                    }
                }
                if (param != '' && isNaN(param)) {
                    alert("广告参数需为数字");
                    $("#publish_param").focus();
                    return false;
                }
            });
        });
        var myCalendar;
        function doOnLoad() {
            myCalendar = new dhtmlXCalendarObject(["startdate", "enddate"]);
        }
    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> APP广告管理 >> APP应用广告列表</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">新增APPP应用广告</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="10" class="error_msg_td">
                            <fmt:message key="${errorMsg}" bundle="${error}"/>
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/advertise/app/apply/create" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            广告位名称
                        </td>
                        <td height="1">
                            <input id="input_text_publishName" type="text" name="publishName" size="100" value=""/><span
                                style="color:red;">*必填项</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            广告位描述
                        </td>
                        <td height="1">
                            <textarea id="input_publishDesc" type="text" name="publishDesc"
                                      style="height: 100px;width: 634px"></textarea>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            广告素材ID
                        </td>
                        <td height="1">
                            <input id="input_text_advertiseId" type="text" name="advertiseId" size="100" value=""/><span
                                style="color:red;">*必填项</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent">
                            选择APP
                        </td>
                        <td height="1">
                            <select name="appkey">
                                <c:forEach var="app" items="${applist}">
                                    <c:if test="${appkey==app.appId}">
                                        <option value="${app.appId}" selected="selected">${app.appName}</option>
                                    </c:if>
                                    <c:if test="${appkey!=app.appId}">
                                        <option value="${app.appId}">${app.appName}</option>
                                    </c:if>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent">
                            渠道
                        </td>
                        <td height="1">
                            <select name="channel" id="select_ctype">
                                <option value="">全部</option>
                                <c:forEach var="ctype" items="${channelTypes}">
                                    <option value="${ctype.code}"<c:if test="${ctype.code eq 'appstore'}"> style="color: red"</c:if>>
                                    <fmt:message key="joymeapp.channel.type.${ctype.code}" bundle="${def}"/>
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            开始时间
                        </td>
                        <td height="1">
                            <input readonly="readonly" type="text" class="default_input_singleline" size="16"
                                   maxlength="20"
                                   name="startdate" id="startdate"
                                   value="<fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/><span
                                style="color:red;">*必填项</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            结束时间
                        </td>
                        <td height="1">
                            <input readonly="readonly" type="text" class="default_input_singleline" size="16"
                                   maxlength="20" name="enddate"
                                   id="enddate"
                                   value="<fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/><span
                                style="color:red;">*必填项</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            广告位类型
                        </td>
                        <td height="1">
                            <input type="radio" name="publish_type" checked="true" value="0"/>开屏广告&nbsp;
                            <input type="radio" name="publish_type" value="1"/>弹窗广告&nbsp;
                            <input type="radio" name="publish_type" value="2"/>咔哒文章列表&nbsp;
                            <input type="radio" name="publish_type" value="3"/>咔哒活动广场&nbsp;
                            <input type="radio" name="publish_type" value="4"/>小端通版首页&nbsp;
                            <input type="radio" name="publish_type" value="5"/>小游戏暂停&nbsp;
                            <input type="radio" name="publish_type" value="6"/>小游戏通关&nbsp;
                            <input type="radio" name="publish_type" value="7"/>小游戏game over&nbsp;
                            <input type="radio" name="publish_type" value="8"/>新手游画报标签广告&nbsp;
                            <br/>
                            <input type="radio" name="publish_type" value="9"/>迷系列首页弹窗&nbsp;
                            <input type="radio" name="publish_type" value="10"/>迷系列搜索页&nbsp;
                            <input type="radio" name="publish_type" value="11"/>迷系列详情页&nbsp;
                            <input type="radio" name="publish_type" value="12"/>迷系列文本&nbsp;
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            广告参数
                        </td>
                        <td height="1">
                            <input id="publish_param" type="text" name="publish_param" size="100" value="0"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            广告参数说明
                        </td>
                        <td height="1">
                            <span style="color:red;">1、广告位类型为"咔哒文章列表"/"咔哒活动广场"/"新手游画报标签广告"时，需填列表位置。 <br/>2、广告位类型为"开屏广告"/"弹窗广告"时，需填广告展示时间（单位：秒）。<br/>3、其他类型不必填。</span>
                        </td>
                        <td height="1" class=>
                        </td>
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