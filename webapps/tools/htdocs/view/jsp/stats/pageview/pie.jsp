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
            myCalendar = new dhtmlXCalendarObject(["statdate"]);
        }

        swfobject.embedSWF(
                "/static/include/openflashchart/open-flash-chart.swf", "uv_ref_chart", "400", "380",
                "9.0.0", "",
                {"data-file": escape("/stats/data/pie/${dateTypeCode}?domaincode=ops|uv.ref&statdate=${statDateStr}x=" + Math.random())});
        swfobject.embedSWF(
                "/static/include/openflashchart/open-flash-chart.swf", "pv_page_chart", "400", "380",
                "9.0.0", "",
                {"data-file": escape("/stats/data/pie/${dateTypeCode}?domaincode=ops|pv.page&statdate=${statDateStr}x=" + Math.random())});
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
                    <td>
                        <input type="button" class="tab_button_long" value="用户来源分布" title="用户来源分布">
                    </td>
                    <td></td>
                    <td>
                        <input type="button" class="tab_button_long" value="页面访问分布" title="页面访问分布">
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td" colspan="3"></td>
                </tr>
                <tr>
                    <td>
                        <div id="uv_ref_chart"></div>
                    </td>
                    <td></td>
                    <td>
                        <div id="pv_page_chart"></div>
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td" colspan="3"></td>
                </tr>
            </table>

            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <form name="schForm" method="post" action="/stats/pageview/pie">
                    <tr>
                        <td height="1" class="default_line_td" colspan="3"></td>
                    </tr>
                    <tr>
                        <td width="80" align="center">统计条件</td>
                        <td>
                            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                                <tr>
                                    <td width="100" align="right" class="edit_table_defaulttitle_td">统计时间：</td>
                                    <td class="edit_table_value_td">
                                        <input id="statdate" name="statdate" type="text" class="default_input_singleline" size="8" maxlength="10" value="${statDateStr}">
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