<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>数据统计-Content-排行</title>
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
                "/static/include/openflashchart/open-flash-chart.swf", "one_day_chart", "600", "200",
                "9.0.0", "",
                {"data-file": escape("/stats/data/bar/${dateTypeCode}?domaincode=ops|back&sectioncode=one&fromdate=${fromDateStr}&todate=${toDate.time > preDay1.time ? preDayStr1 : toDateStr}&percent=true&x=" + Math.random())});
        swfobject.embedSWF(
                "/static/include/openflashchart/open-flash-chart.swf", "four_day_chart", "600", "200",
                "9.0.0", "",
                {"data-file": escape("/stats/data/bar/${dateTypeCode}?domaincode=ops|back&sectioncode=four&fromdate=${fromDateStr}&todate=${toDate.time > preDay2.time ? preDayStr2 : toDateStr}&percent=true&x=" + Math.random())});
        swfobject.embedSWF(
                "/static/include/openflashchart/open-flash-chart.swf", "seven_day_chart", "600", "200",
                "9.0.0", "",
                {"data-file": escape("/stats/data/bar/${dateTypeCode}?domaincode=ops|back&sectioncode=seven&fromdate=${fromDateStr}&todate=${toDate.time > preDay3.time ? preDayStr3 : toDateStr}&percent=true&x=" + Math.random())});

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
                <form name="schForm" method="post" action="/stats/retention/list">
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
            <br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td width="48%" valign="top">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td class="list_table_header_td" colspan="8">
                                    <input type="button" class="tab_button_long" value="用户回访统计" title="用户回访统计">
                                </td>
                            </tr>
                            <tr>
                                <td height="1" class="default_line_td" colspan="8"></td>
                            </tr>
                            <tr class="list_table_title_tr">
                                <td align="center">初始日期</td>
                                <td align="right">隔天回访率</td>
                                <td align="right">2天回访率</td>
                                <td align="right">3天回访率</td>
                                <td align="right">4天回访率</td>
                                <td align="right">5天回访率</td>
                                <td align="right">6天回访率</td>
                                <td align="right">7天回访率</td>
                            </tr>
                            <c:forEach items="${datas.entrySet()}" var="entry" varStatus="st">
                                <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                    <td align="center">
                                        <b><fmt:formatDate value="${entry.key}" pattern="yyyy-MM-dd"/></b>
                                    </td>
                                    <td align="right">
                                        <c:if test="${(today.time - entry.key.time) / 1000 / 3600 / 24 > 1}">
                                            <fmt:formatNumber value="${entry.value.get('one').statValue / 100.0}" pattern="###,##0.00"/>
                                        </c:if>%
                                    </td>
                                    <td align="right">
                                        <c:if test="${(today.time - entry.key.time) / 1000 / 3600 / 24 > 2}">
                                            <fmt:formatNumber value="${entry.value.get('two').statValue / 100.0}" pattern="###,##0.00"/>
                                        </c:if>%
                                    </td>
                                    <td align="right">
                                        <c:if test="${(today.time - entry.key.time) / 1000 / 3600 / 24 > 3}">
                                            <fmt:formatNumber value="${entry.value.get('three').statValue / 100.0}" pattern="###,##0.00"/>
                                        </c:if>%
                                    </td>
                                    <td align="right">
                                        <c:if test="${(today.time - entry.key.time) / 1000 / 3600 / 24 > 4}">
                                            <fmt:formatNumber value="${entry.value.get('four').statValue / 100.0}" pattern="###,##0.00"/>
                                        </c:if>%
                                    </td>
                                    <td align="right">
                                        <c:if test="${(today.time - entry.key.time) / 1000 / 3600 / 24 > 5}">
                                            <fmt:formatNumber value="${entry.value.get('five').statValue / 100.0}" pattern="###,##0.00"/>
                                        </c:if>%
                                    </td>
                                    <td align="right">
                                        <c:if test="${(today.time - entry.key.time) / 1000 / 3600 / 24 > 6}">
                                            <fmt:formatNumber value="${entry.value.get('six').statValue / 100.0}" pattern="###,##0.00"/>
                                        </c:if>%
                                    </td>
                                    <td align="right">
                                        <c:if test="${(today.time - entry.key.time) / 1000 / 3600 / 24 > 7}">
                                            <fmt:formatNumber value="${entry.value.get('seven').statValue / 100.0}" pattern="###,##0.00"/>
                                        </c:if>%
                                    </td>
                                </tr>
                            </c:forEach>
                            <tr>
                                <td height="1" class="default_line_td" colspan="8"></td>
                            </tr>
                        </table>
                    </td>
                    <td width="4%"></td>
                    <td width="48%" valign="top">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td class="list_table_header_td">
                                    <input type="button" class="tab_button_long" value="隔日回访情况" title="隔日回访情况">
                                </td>
                            </tr>
                            <tr>
                                <td height="1" class="default_line_td"></td>
                            </tr>
                            <tr>
                                <td>
                                    <div id="one_day_chart"></div>
                                </td>
                            </tr>
                            <tr>
                                <td height="1" class="default_line_td"></td>
                            </tr>
                        </table>
                        <br>
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td class="list_table_header_td">
                                    <input type="button" class="tab_button_long" value="四日回访情况" title="四日回访情况">
                                </td>
                            </tr>
                            <tr>
                                <td height="1" class="default_line_td"></td>
                            </tr>
                            <tr>
                                <td>
                                    <div id="four_day_chart"></div>
                                </td>
                            </tr>
                            <tr>
                                <td height="1" class="default_line_td"></td>
                            </tr>
                        </table>
                        <br>
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td class="list_table_header_td">
                                    <input type="button" class="tab_button_long" value="七日回访情况" title="七日回访情况">
                                </td>
                            </tr>
                            <tr>
                                <td height="1" class="default_line_td"></td>
                            </tr>
                            <tr>
                                <td>
                                    <div id="seven_day_chart"></div>
                                </td>
                            </tr>
                            <tr>
                                <td height="1" class="default_line_td"></td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</body>
</html>