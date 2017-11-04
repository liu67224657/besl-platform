<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>创建APP</title>
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
                if ($("#startTime").val() == '') {
                    alert("请开始时间");
                    $("#startTime").focus();
                    return false;
                }
                if ($("#endTime").val() == '') {
                    alert("请结束时间");
                    $("#endTime").focus();
                    return false;
                }
                if ($("#endTime").val() < $("#startTime").val()) {
                    alert("结束时间大于起始时间");
                    return false;
                }
                var type = $('input[name=publishType]:checked').val();
                var param = $("#publishParam").val();
                if(type == '0' || type == '1' || type == '2' || type == '3' || type == '8'){
                    if(param == ''){
                        alert("请填写广告参数");
                        $("#publishParam").focus();
                        return false;
                    }
                }
                if (param != '' && isNaN(param)) {
                    alert("广告参数需为数字");
                    $("#publishParam").focus();
                    return false;
                }
            });
        });
        var myCalendar;
        function doOnLoad() {
            myCalendar = new dhtmlXCalendarObject(["startTime", "endTime"]);
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
        <td class="list_table_header_td">修改APPP应用广告</td>
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
<form action="/advertise/app/apply/modify" method="post" id="form_submit">
<input type="hidden" name="publishId" value="${advertisePublish.publishId}">
<input type="hidden" name="pager.offset" value="${pageStartIndex}">
<table width="100%" border="0" cellspacing="1" cellpadding="0">
<tr>
    <td height="1" colspan="3" class="default_line_td"></td>
</tr>
<tr>
    <td height="1" class="default_line_td td_cent" width="100">
        广告位名称
    </td>
    <td height="1">
        <input id="input_text_publishName" type="text" name="publishName" size="100"
               value="${advertisePublish.publishName}"/><span style="color:red;">*必填项</span>
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
                  style="height: 100px;width: 634px">${advertisePublish.publishDesc}</textarea>
    </td>
    <td height="1">
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td td_cent" width="100">
        广告素材ID
    </td>
    <td height="1">
        <input id="input_text_advertiseId" type="text" name="advertiseId" size="100"
               value="${advertisePublish.advertiseId}"/><span style="color:red;">*必填项</span>
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
                <c:choose>
                    <c:when test="${app.appId==advertisePublish.appkey}">
                        <option value="${app.appId}" selected="selected">${app.appName}</option>
                    </c:when>
                    <c:otherwise>
                        <option value="${app.appId}">${app.appName}</option>
                    </c:otherwise>
                </c:choose>
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
                <option value="${ctype.code}"
                <c:if test="${ctype.code eq 'appstore'}"> style="color: red"</c:if><c:if
                    test="${ctype.code == advertisePublish.channel.code}"> selected="selected"</c:if>>
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
               name="startTime" id="startTime"
               value="<fmt:formatDate value="${advertisePublish.startTime}" pattern="yyyy-MM-dd
        HH:mm:ss"/>"/><span style="color:red;">*必填项</span>
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
               maxlength="20" name="endTime"
               id="endTime"
               value="<fmt:formatDate value="${advertisePublish.endTime}" pattern="yyyy-MM-dd
        HH:mm:ss"/>"/><span style="color:red;">*必填项</span>
    </td>
    <td height="1" class=>
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td td_cent" width="100">
        跳转类型
    </td>
    <td height="1">
        <c:if test="${advertisePublish.publishType.code == '0'}">
            <input type="radio" name="publishType" checked="true" value="0"/>开屏广告&nbsp;
            <input type="radio" name="publishType" value="1"/>弹窗广告&nbsp;
            <input type="radio" name="publishType" value="2"/>咔哒文章列表&nbsp;
            <input type="radio" name="publishType" value="3"/>咔哒活动广场&nbsp;
            <input type="radio" name="publishType" value="4"/>小端通版首页&nbsp;
            <input type="radio" name="publishType" value="5"/>小游戏暂停&nbsp;
            <input type="radio" name="publishType" value="6"/>小游戏通关&nbsp;
            <input type="radio" name="publishType" value="7"/>小游戏game over&nbsp;
            <input type="radio" name="publishType" value="8"/>新手游画报标签广告&nbsp;
            <br/>
            <input type="radio" name="publishType" value="9"/>迷系列首页弹窗&nbsp;
            <input type="radio" name="publishType" value="10"/>迷系列搜索页&nbsp;
            <input type="radio" name="publishType" value="11"/>迷系列详情页&nbsp;
            <input type="radio" name="publishType" value="12"/>迷系列文本&nbsp;
        </c:if>
        <c:if test="${advertisePublish.publishType.code == '1'}">
            <input type="radio" name="publishType" value="0"/>开屏广告&nbsp;
            <input type="radio" name="publishType" checked="true" value="1"/>弹窗广告&nbsp;
            <input type="radio" name="publishType" value="2"/>咔哒文章列表&nbsp;
            <input type="radio" name="publishType" value="3"/>咔哒活动广场&nbsp;
            <input type="radio" name="publishType" value="4"/>小端通版首页&nbsp;
            <input type="radio" name="publishType" value="5"/>小游戏暂停&nbsp;
            <input type="radio" name="publishType" value="6"/>小游戏通关&nbsp;
            <input type="radio" name="publishType" value="7"/>小游戏game over&nbsp;
            <input type="radio" name="publishType" value="8"/>新手游画报标签广告&nbsp;
            <br/>
            <input type="radio" name="publishType" value="9"/>迷系列首页弹窗&nbsp;
            <input type="radio" name="publishType" value="10"/>迷系列搜索页&nbsp;
            <input type="radio" name="publishType" value="11"/>迷系列详情页&nbsp;
            <input type="radio" name="publishType" value="12"/>迷系列文本&nbsp;
        </c:if>
        <c:if test="${advertisePublish.publishType.code == '2'}">
            <input type="radio" name="publishType" value="0"/>开屏广告&nbsp;
            <input type="radio" name="publishType" value="1"/>弹窗广告&nbsp;
            <input type="radio" name="publishType" checked="true" value="2"/>咔哒文章列表&nbsp;
            <input type="radio" name="publishType" value="3"/>咔哒活动广场&nbsp;
            <input type="radio" name="publishType" value="4"/>小端通版首页&nbsp;
            <input type="radio" name="publishType" value="5"/>小游戏暂停&nbsp;
            <input type="radio" name="publishType" value="6"/>小游戏通关&nbsp;
            <input type="radio" name="publishType" value="7"/>小游戏game over&nbsp;
            <input type="radio" name="publishType" value="8"/>新手游画报标签广告&nbsp;
            <br/>
            <input type="radio" name="publishType" value="9"/>迷系列首页弹窗&nbsp;
            <input type="radio" name="publishType" value="10"/>迷系列搜索页&nbsp;
            <input type="radio" name="publishType" value="11"/>迷系列详情页&nbsp;
            <input type="radio" name="publishType" value="12"/>迷系列文本&nbsp;
        </c:if>
        <c:if test="${advertisePublish.publishType.code == '3'}">
            <input type="radio" name="publishType" value="0"/>开屏广告&nbsp;
            <input type="radio" name="publishType" value="1"/>弹窗广告&nbsp;
            <input type="radio" name="publishType" value="2"/>咔哒文章列表&nbsp;
            <input type="radio" name="publishType" checked="true" value="3"/>咔哒活动广场&nbsp;
            <input type="radio" name="publishType" value="4"/>小端通版首页&nbsp;
            <input type="radio" name="publishType" value="5"/>小游戏暂停&nbsp;
            <input type="radio" name="publishType" value="6"/>小游戏通关&nbsp;
            <input type="radio" name="publishType" value="7"/>小游戏game over&nbsp;
            <input type="radio" name="publishType" value="8"/>新手游画报标签广告&nbsp;
            <br/>
            <input type="radio" name="publishType" value="9"/>迷系列首页弹窗&nbsp;
            <input type="radio" name="publishType" value="10"/>迷系列搜索页&nbsp;
            <input type="radio" name="publishType" value="11"/>迷系列详情页&nbsp;
            <input type="radio" name="publishType" value="12"/>迷系列文本&nbsp;
        </c:if>
        <c:if test="${advertisePublish.publishType.code == '4'}">
            <input type="radio" name="publishType" value="0"/>开屏广告&nbsp;
            <input type="radio" name="publishType" value="1"/>弹窗广告&nbsp;
            <input type="radio" name="publishType" value="2"/>咔哒文章列表&nbsp;
            <input type="radio" name="publishType" value="3"/>咔哒活动广场&nbsp;
            <input type="radio" name="publishType" checked="true" value="4"/>小端通版首页&nbsp;
            <input type="radio" name="publishType" value="5"/>小游戏暂停&nbsp;
            <input type="radio" name="publishType" value="6"/>小游戏通关&nbsp;
            <input type="radio" name="publishType" value="7"/>小游戏game over&nbsp;
            <input type="radio" name="publishType" value="8"/>新手游画报标签广告&nbsp;
            <br/>
            <input type="radio" name="publishType" value="9"/>迷系列首页弹窗&nbsp;
            <input type="radio" name="publishType" value="10"/>迷系列搜索页&nbsp;
            <input type="radio" name="publishType" value="11"/>迷系列详情页&nbsp;
            <input type="radio" name="publishType" value="12"/>迷系列文本&nbsp;
        </c:if>
        <c:if test="${advertisePublish.publishType.code == '5'}">
            <input type="radio" name="publishType" value="0"/>开屏广告&nbsp;
            <input type="radio" name="publishType" value="1"/>弹窗广告&nbsp;
            <input type="radio" name="publishType" value="2"/>咔哒文章列表&nbsp;
            <input type="radio" name="publishType" value="3"/>咔哒活动广场&nbsp;
            <input type="radio" name="publishType" value="4"/>小端通版首页&nbsp;
            <input type="radio" name="publishType" checked="true" value="5"/>小游戏暂停&nbsp;
            <input type="radio" name="publishType" value="6"/>小游戏通关&nbsp;
            <input type="radio" name="publishType" value="7"/>小游戏game over&nbsp;
            <input type="radio" name="publishType" value="8"/>新手游画报标签广告&nbsp;
            <br/>
            <input type="radio" name="publishType" value="9"/>迷系列首页弹窗&nbsp;
            <input type="radio" name="publishType" value="10"/>迷系列搜索页&nbsp;
            <input type="radio" name="publishType" value="11"/>迷系列详情页&nbsp;
            <input type="radio" name="publishType" value="12"/>迷系列文本&nbsp;
        </c:if>
        <c:if test="${advertisePublish.publishType.code == '6'}">
            <input type="radio" name="publishType" value="0"/>开屏广告&nbsp;
            <input type="radio" name="publishType" value="1"/>弹窗广告&nbsp;
            <input type="radio" name="publishType" value="2"/>咔哒文章列表&nbsp;
            <input type="radio" name="publishType" value="3"/>咔哒活动广场&nbsp;
            <input type="radio" name="publishType" value="4"/>小端通版首页&nbsp;
            <input type="radio" name="publishType" value="5"/>小游戏暂停&nbsp;
            <input type="radio" name="publishType" checked="true" value="6"/>小游戏通关&nbsp;
            <input type="radio" name="publishType" value="7"/>小游戏game over&nbsp;
            <input type="radio" name="publishType" value="8"/>新手游画报标签广告&nbsp;
            <br/>
            <input type="radio" name="publishType" value="9"/>迷系列首页弹窗&nbsp;
            <input type="radio" name="publishType" value="10"/>迷系列搜索页&nbsp;
            <input type="radio" name="publishType" value="11"/>迷系列详情页&nbsp;
            <input type="radio" name="publishType" value="12"/>迷系列文本&nbsp;
        </c:if>
        <c:if test="${advertisePublish.publishType.code == '7'}">
            <input type="radio" name="publishType" value="0"/>开屏广告&nbsp;
            <input type="radio" name="publishType" value="1"/>弹窗广告&nbsp;
            <input type="radio" name="publishType" value="2"/>咔哒文章列表&nbsp;
            <input type="radio" name="publishType" value="3"/>咔哒活动广场&nbsp;
            <input type="radio" name="publishType" value="4"/>小端通版首页&nbsp;
            <input type="radio" name="publishType" value="5"/>小游戏暂停&nbsp;
            <input type="radio" name="publishType" value="6"/>小游戏通关&nbsp;
            <input type="radio" name="publishType" checked="true" value="7"/>小游戏game over&nbsp;
            <input type="radio" name="publishType" value="8"/>新手游画报标签广告&nbsp;
            <br/>
            <input type="radio" name="publishType" value="9"/>迷系列首页弹窗&nbsp;
            <input type="radio" name="publishType" value="10"/>迷系列搜索页&nbsp;
            <input type="radio" name="publishType" value="11"/>迷系列详情页&nbsp;
            <input type="radio" name="publishType" value="12"/>迷系列文本&nbsp;
        </c:if>
        <c:if test="${advertisePublish.publishType.code == '8'}">
            <input type="radio" name="publishType" value="0"/>开屏广告&nbsp;
            <input type="radio" name="publishType" value="1"/>弹窗广告&nbsp;
            <input type="radio" name="publishType" value="2"/>咔哒文章列表&nbsp;
            <input type="radio" name="publishType" value="3"/>咔哒活动广场&nbsp;
            <input type="radio" name="publishType" value="4"/>小端通版首页&nbsp;
            <input type="radio" name="publishType" value="5"/>小游戏暂停&nbsp;
            <input type="radio" name="publishType" value="6"/>小游戏通关&nbsp;
            <input type="radio" name="publishType"  value="7"/>小游戏game over&nbsp;
            <input type="radio" name="publishType" checked="true" value="8"/>新手游画报标签广告&nbsp;
            <br/>
            <input type="radio" name="publishType" value="9"/>迷系列首页弹窗&nbsp;
            <input type="radio" name="publishType" value="10"/>迷系列搜索页&nbsp;
            <input type="radio" name="publishType" value="11"/>迷系列详情页&nbsp;
            <input type="radio" name="publishType" value="12"/>迷系列文本&nbsp;
        </c:if>
        <c:if test="${advertisePublish.publishType.code == '9'}">
            <input type="radio" name="publishType" value="0"/>开屏广告&nbsp;
            <input type="radio" name="publishType" value="1"/>弹窗广告&nbsp;
            <input type="radio" name="publishType" value="2"/>咔哒文章列表&nbsp;
            <input type="radio" name="publishType" value="3"/>咔哒活动广场&nbsp;
            <input type="radio" name="publishType" value="4"/>小端通版首页&nbsp;
            <input type="radio" name="publishType" value="5"/>小游戏暂停&nbsp;
            <input type="radio" name="publishType" value="6"/>小游戏通关&nbsp;
            <input type="radio" name="publishType"  value="7"/>小游戏game over&nbsp;
            <input type="radio" name="publishType" value="8"/>新手游画报标签广告&nbsp;
            <br/>
            <input type="radio" name="publishType" checked="true" value="9"/>迷系列首页弹窗&nbsp;
            <input type="radio" name="publishType" value="10"/>迷系列搜索页&nbsp;
            <input type="radio" name="publishType" value="11"/>迷系列详情页&nbsp;
            <input type="radio" name="publishType" value="12"/>迷系列文本&nbsp;
        </c:if>
        <c:if test="${advertisePublish.publishType.code == '10'}">
            <input type="radio" name="publishType" value="0"/>开屏广告&nbsp;
            <input type="radio" name="publishType" value="1"/>弹窗广告&nbsp;
            <input type="radio" name="publishType" value="2"/>咔哒文章列表&nbsp;
            <input type="radio" name="publishType" value="3"/>咔哒活动广场&nbsp;
            <input type="radio" name="publishType" value="4"/>小端通版首页&nbsp;
            <input type="radio" name="publishType" value="5"/>小游戏暂停&nbsp;
            <input type="radio" name="publishType" value="6"/>小游戏通关&nbsp;
            <input type="radio" name="publishType"  value="7"/>小游戏game over&nbsp;
            <input type="radio" name="publishType" value="8"/>新手游画报标签广告&nbsp;
            <br/>
            <input type="radio" name="publishType" value="9"/>迷系列首页弹窗&nbsp;
            <input type="radio" name="publishType" checked="true" value="10"/>迷系列搜索页&nbsp;
            <input type="radio" name="publishType" value="11"/>迷系列详情页&nbsp;
            <input type="radio" name="publishType" value="12"/>迷系列文本&nbsp;
        </c:if>
        <c:if test="${advertisePublish.publishType.code == '11'}">
            <input type="radio" name="publishType" value="0"/>开屏广告&nbsp;
            <input type="radio" name="publishType" value="1"/>弹窗广告&nbsp;
            <input type="radio" name="publishType" value="2"/>咔哒文章列表&nbsp;
            <input type="radio" name="publishType" value="3"/>咔哒活动广场&nbsp;
            <input type="radio" name="publishType" value="4"/>小端通版首页&nbsp;
            <input type="radio" name="publishType" value="5"/>小游戏暂停&nbsp;
            <input type="radio" name="publishType" value="6"/>小游戏通关&nbsp;
            <input type="radio" name="publishType"  value="7"/>小游戏game over&nbsp;
            <input type="radio" name="publishType" value="8"/>新手游画报标签广告&nbsp;
            <br/>
            <input type="radio" name="publishType" value="9"/>迷系列首页弹窗&nbsp;
            <input type="radio" name="publishType" value="10"/>迷系列搜索页&nbsp;
            <input type="radio" name="publishType" checked="true" value="11"/>迷系列详情页&nbsp;
            <input type="radio" name="publishType" value="12"/>迷系列文本&nbsp;
        </c:if>
        <c:if test="${advertisePublish.publishType.code == '12'}">
            <input type="radio" name="publishType" value="0"/>开屏广告&nbsp;
            <input type="radio" name="publishType" value="1"/>弹窗广告&nbsp;
            <input type="radio" name="publishType" value="2"/>咔哒文章列表&nbsp;
            <input type="radio" name="publishType" value="3"/>咔哒活动广场&nbsp;
            <input type="radio" name="publishType" value="4"/>小端通版首页&nbsp;
            <input type="radio" name="publishType" value="5"/>小游戏暂停&nbsp;
            <input type="radio" name="publishType" value="6"/>小游戏通关&nbsp;
            <input type="radio" name="publishType"  value="7"/>小游戏game over&nbsp;
            <input type="radio" name="publishType" value="8"/>新手游画报标签广告&nbsp;
            <br/>
            <input type="radio" name="publishType" value="9"/>迷系列首页弹窗&nbsp;
            <input type="radio" name="publishType" value="10"/>迷系列搜索页&nbsp;
            <input type="radio" name="publishType" value="11"/>迷系列详情页&nbsp;
            <input type="radio" name="publishType" checked="true" value="12"/>迷系列文本&nbsp;
        </c:if>
    </td>
    <td height="1" class=>
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td td_cent" width="100">
        广告参数
    </td>
    <td height="1">
        <input id="publishParam" type="text" name="publishParam" size="70"
               value="${advertisePublish.publishParam.numberParam==0?advertisePublish.publishParam.longtime:advertisePublish.publishParam.numberParam}"/>
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