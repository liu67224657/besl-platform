<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>数据统计-Behaviour-列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/dhtmlxcalendar.css">
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/skins/dhtmlxcalendar_dhx_skyblue.css">

    <script type="text/javascript" src="/static/include/dhtmlxcalendar/dhtmlxcalendar.js"></script>
    <script type="text/javascript" src="/static/include/js/default.js"></script>

    <script type="text/javascript" src="/static/include/openflashchart/swfobject.js"></script>
    <script type="text/javascript">
        var myCalendar;
        function doOnLoad() {
            myCalendar = new dhtmlXCalendarObject(["fromdate", "todate"]);
        }

        swfobject.embedSWF(
                "/static/include/openflashchart/open-flash-chart.swf", "line_chart", "1100", "400",
                "9.0.0", "",
                {"data-file":escape("/stats/data/line/${dateTypeCode}?domaincode=${domainCode}&sectioncode=${sectionCode}&fromdate=${fromDateStr}&todate=${toDateStr}&x=" + Math.random())});
    </script>
</head>

<body onload="doOnLoad();">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 统计报告 >> 统计列表</td>
    </tr>
    <tr>
        <td valign="top">
            <br>

            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td" colspan="3">
                        <input type="button" class="tab_button_long" value="用户发帖情况统计曲线" title="用户发帖情况统计曲线">
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td" colspan="3"></td>
                </tr>
                <tr>
                    <td valign="top">
                        <div id="line_chart" style="z-index: -1"></div>
                    </td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td" colspan="3"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <form name="schForm" method="post" action="/stats/behaviour/postline">
                    <tr>
                        <td height="1" class="default_line_td" colspan="3"></td>
                    </tr>
                    <tr>
                        <td width="80" align="center">统计条件</td>
                        <td>
                            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                                <tr>
                                    <td width="100" align="right" class="edit_table_defaulttitle_td">统计开始时间：</td>
                                    <td class="edit_table_value_td">
                                        <input id="fromdate" name="fromdate" type="text" class="default_input_singleline" size="8" maxlength="10" value="${fromDateStr}">
                                    </td>
                                    <td width="100" align="right" class="edit_table_defaulttitle_td">统计结束时间：</td>
                                    <td class="edit_table_value_td">
                                        <input id="todate" name="todate" type="text" class="default_input_singleline" size="8" maxlength="10" value="${toDateStr}">
                                    </td>
                                    <td width="100" align="right" class="edit_table_defaulttitle_td">统计时段类型：</td>
                                    <td class="edit_table_value_td">
                                        <select name="datetypecode" class="default_select_single">
                                            <c:forEach items="${dateTypes}" var="dateType">
                                                <option value="${dateType.code}" <c:if test="${dateTypeCode eq dateType.code}">selected="true"</c:if>>
                                                    <fmt:message key="def.stats.date.type.${dateType.code}.name" bundle="${def}"/>
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td width="80" align="center">
                            <input name="submit" type="submit" class="default_button" value=" 刷新 ">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" colspan="3"></td>
                    </tr>
                </form>
            </table>
        </td>
    </tr>
</table>
</body>
</html>