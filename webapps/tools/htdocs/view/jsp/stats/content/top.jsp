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

    <script type="text/javascript">
        var myCalendar;
        function doOnLoad() {
            myCalendar = new dhtmlXCalendarObject(["statdate"]);
        }
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
                <form name="schForm" method="post" action="/stats/content/top">
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
            <br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td width="48%" valign="top">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td class="list_table_header_td" colspan="3">
                                    <input type="button" class="tab_button_long" value="文章浏览量排行" title="文章浏览量排行">
                                </td>
                            </tr>
                            <tr>
                                <td height="1" class="default_line_td" colspan="3"></td>
                            </tr>
                            <tr class="list_table_title_tr">
                                <td align="center">排行</td>
                                <td align="right">浏览量</td>
                                <td align="center">链接</td>
                            </tr>
                            <c:forEach items="${views}" var="item" varStatus="st">
                                <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                    <td align="center">${item.statSection.code}</td>
                                    <td align="right">${item.statValue}</td>
                                    <td align="center"><a href="${item.extData.extValue01}" target="_blank">链接</a></td>
                                </tr>
                            </c:forEach>
                            <tr>
                                <td height="1" class="default_line_td" colspan="3"></td>
                            </tr>
                        </table>
                    </td>
                    <td width="4%"></td>
                    <td width="48%" valign="top">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td class="list_table_header_td" colspan="3">
                                    <input type="button" class="tab_button_long" value="文章热度排行" title="文章热度排行">
                                </td>
                            </tr>
                            <tr>
                                <td height="1" class="default_line_td" colspan="3"></td>
                            </tr>
                            <tr class="list_table_title_tr">
                                <td align="center">排行</td>
                                <td align="right">热度</td>
                                <td align="center">链接</td>
                            </tr>
                            <c:forEach items="${hots}" var="item" varStatus="st">
                                <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                    <td align="center">${item.statSection.code}</td>
                                    <td align="right">${item.statValue}</td>
                                    <td align="center"><a href="${item.extData.extValue01}" target="_blank">链接</a></td>
                                </tr>
                            </c:forEach>
                            <tr>
                                <td height="1" class="default_line_td" colspan="3"></td>
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