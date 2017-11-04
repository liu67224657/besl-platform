<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>统计首页</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script src="/static/include/js/default.js" type="text/javascript"></script>
    <script src="/static/include/openflashchart/swfobject.js" type="text/javascript"></script>
    <script type="text/javascript">
        swfobject.embedSWF(
                "/static/include/openflashchart/open-flash-chart.swf", "uvrefs_chart", "350", "200",
                "9.0.0", "",
                {"data-file":"/stats/home/uvrefs?x=" + Math.random()});

        swfobject.embedSWF(
                "/static/include/openflashchart/open-flash-chart.swf", "pvpages_chart", "350", "200",
                "9.0.0", "",
                {"data-file":"/stats/home/pvpages?x=" + Math.random()});

        swfobject.embedSWF(
                "/static/include/openflashchart/open-flash-chart.swf", "nextdaybacks_chart", "350", "200",
                "9.0.0", "",
                {"data-file":"/stats/home/nextdaybacks?x=" + Math.random()});

        swfobject.embedSWF(
                "/static/include/openflashchart/open-flash-chart.swf", "sevendaybacks_chart", "350", "200",
                "9.0.0", "",
                {"data-file":"/stats/home/sevendaybacks?x=" + Math.random()});

        swfobject.embedSWF(
                "/static/include/openflashchart/open-flash-chart.swf", "fourdaybacks_chart", "350", "200",
                "9.0.0", "",
                {"data-file":"/stats/home/fourdaybacks?x=" + Math.random()});

        swfobject.embedSWF(
                "/static/include/openflashchart/open-flash-chart.swf", "regrefs_chart", "300", "200",
                "9.0.0", "",
                {"data-file":"/stats/home/regrefs?x=" + Math.random()});

        swfobject.embedSWF(
                "/static/include/openflashchart/open-flash-chart.swf", "postphase_chart", "300", "200",
                "9.0.0", "",
                {"data-file":"/stats/home/postphase?x=" + Math.random()});

        swfobject.embedSWF(
                "/static/include/openflashchart/open-flash-chart.swf", "commentphase_chart", "300", "200",
                "9.0.0", "",
                {"data-file":"/stats/home/commentphase?x=" + Math.random()});
    </script>
</head>

<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
    <td height="22" class="page_navigation_td">>> 统计报告 >> 统计首页</td>
</tr>
<tr>
<td valign="top">
<br>
<table border="0" cellspacing="0" cellpadding="1">
    <tr>
        <td class="list_table_header_td" colspan="3">
            <input type="button" class="tab_button" value="昨日PV统计" title="昨日PV统计">
        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td" colspan="3"></td>
    </tr>
    <tr>
        <td valign="top">
            <table width="200px" height="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td class="list_table_header_td" colspan="2" align="center">
                       pv综合数据
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td" colspan="2"></td>
                </tr>
                <tr>
                    <td align="right" width="80px">
                        页面浏览
                    </td>
                    <td align="right">
                        <fmt:formatNumber value="${yesterdayStats.yesterdayPageView.statValue}"
                                          pattern="###,###"/>
                    </td>
                </tr>
                <tr>
                    <td align="right">
                        唯一用户
                    </td>
                    <td align="right">
                        <fmt:formatNumber value="${yesterdayStats.yesterdayUniqueUser.statValue}" pattern="###,###"/>
                    </td>
                </tr>
                <tr>
                    <td align="right">
                        独立IP
                    </td>
                    <td align="right">
                        <fmt:formatNumber value="${yesterdayStats.yesterdayIP.statValue}" pattern="###,###"/>
                    </td>
                </tr>
                <tr>
                    <td align="right">
                        停留时长
                    </td>
                    <td align="right">
                        <fmt:formatNumber value="${yesterdayStats.yesterdayAvgTimeOfStay.statValue}" pattern="###,###"/>
                    </td>
                </tr>
            </table>
        </td>
        <td valign="top">
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td class="list_table_header_td" colspan="2" align="center">
                        UV来源
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td" colspan="2"></td>
                </tr>
                <tr>
                    <td colspan="2" align="center">
                        <div id="uvrefs_chart"></div>
                    </td>
                </tr>
            </table>
        </td>
        <td valign="top">
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td class="list_table_header_td" colspan="2" align="center">
                        PV分布
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td" colspan="2"></td>
                </tr>
                <tr>
                    <td colspan="2" align="center">
                        <div id="pvpages_chart"></div>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td" colspan="3"></td>
    </tr>
</table>

<br>
<table border="0" cellspacing="0" cellpadding="1">
    <tr>
        <td class="list_table_header_td" colspan="3">
            <input type="button" class="tab_button" value="昨日留存统计" title="昨日留存统计">
        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td" colspan="3"></td>
    </tr>
    <tr>
        <td align="top">
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td class="list_table_header_td" colspan="3" align="center">
                        隔日留存曲线
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td" colspan="3"></td>
                </tr>
                <tr>
                    <td colspan="3" align="center">
                        <div id="nextdaybacks_chart"></div>
                    </td>
                </tr>
            </table>
        </td>
        <td align="top">
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td class="list_table_header_td" colspan="2" align="center">
                        4日留存曲线
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td" colspan="2"></td>
                </tr>
                <tr>
                    <td colspan="3" align="center">
                        <div id="fourdaybacks_chart"></div>
                    </td>
                </tr>
            </table>
        </td>
        <td align="top">
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td class="list_table_header_td" colspan="2" align="center">
                        7日留存曲线
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td" colspan="2"></td>
                </tr>
                <tr>
                    <td colspan="3" align="center">
                        <div id="sevendaybacks_chart"></div>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td" colspan="3"></td>
    </tr>
</table>

<br>
<table border="0" cellspacing="0" cellpadding="1">
    <tr>
        <td class="list_table_header_td" colspan="4">
            <input type="button" class="tab_button" value="昨日用户行为统计" title="昨日用户行为统计">
        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td" colspan="4"></td>
    </tr>
    <tr>
        <td valign="top">
            <table width="200px" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td class="list_table_header_td" colspan="2" align="center">
                        用户行为
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td" colspan="2"></td>
                </tr>
                <tr>
                    <td align="right" width="80px">
                        发表文章
                    </td>
                    <td align="right">
                        <fmt:formatNumber value="${yesterdayStats.yesterdayNewArticle.statValue}"
                                          pattern="###,###"/>
                    </td>
                </tr>
                <tr>
                    <td align="right">
                        评论文章
                    </td>
                    <td align="right">
                        <fmt:formatNumber value="${yesterdayStats.yesterdayNewComment.statValue}" pattern="###,###"/>
                    </td>
                </tr>
                <tr>
                    <td align="right">
                        转发文章
                    </td>
                    <td align="right">
                        <fmt:formatNumber value="${yesterdayStats.yesterdayNewForward.statValue}" pattern="###,###"/>
                    </td>
                </tr>
                <tr>
                    <td align="right">
                        喜欢文章
                    </td>
                    <td align="right">
                        <fmt:formatNumber value="${yesterdayStats.yesterdayNewLike.statValue}" pattern="###,###"/>
                    </td>
                </tr>
                <tr>
                    <td align="right">
                        用户注册
                    </td>
                    <td align="right">
                        <fmt:formatNumber value="${yesterdayStats.yesterdayNewRegister.statValue}" pattern="###,###"/>
                    </td>
                </tr>
            </table>
        </td>
        <td valign="top">
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td class="list_table_header_td" colspan="2" align="center">
                        注册来源统计
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td" colspan="2"></td>
                </tr>
                <tr>
                    <td colspan="2" align="center">
                        <div id="regrefs_chart"></div>
                    </td>
                </tr>
            </table>
        </td>
        <td valign="top">
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td class="list_table_header_td" colspan="2" align="center">
                        发文数量统计
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td" colspan="2"></td>
                </tr>
                <tr>
                    <td colspan="2" align="center">
                        <div id="postphase_chart"></div>
                    </td>
                </tr>
            </table>
        </td>
        <td valign="top">
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td class="list_table_header_td" colspan="2" align="center">
                        评论数量统计
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td" colspan="2"></td>
                </tr>
                <tr>
                    <td colspan="2" align="center">
                        <div id="commentphase_chart"></div>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td" colspan="4"></td>
    </tr>
</table>

<br>
<table border="0" cellspacing="0" cellpadding="1">
    <tr>
        <td class="list_table_header_td" colspan="2">
            <input type="button" class="tab_button" value="昨日内容统计" title="昨日内容统计">
        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td" colspan="2"></td>
    </tr>
    <tr>
        <td valign="top">
            <table width="300px" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td class="list_table_header_td" colspan="3">
                        最高热度文章Top10
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td" colspan="3"></td>
                </tr>
                <tr>
                    <td align="center">
                        排名
                    </td>
                    <td align="right">
                        热度数
                    </td>
                    <td align="center">
                        文章
                    </td>
                </tr>
                <c:forEach var="item" items="${yesterdayStats.lastWeekTopHotArticles}">
                    <tr>
                        <td align="center">
                                ${item.statSection.code}
                        </td>
                        <td align="right">
                                ${item.statValue}
                        </td>
                        <td align="center">
                            <a href="${item.extData.extValue01}" target="_blank">链接</a>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </td>
        <td valign="top">
            <table width="300px" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td class="list_table_header_td" colspan="3">
                        最高浏览文章Top10
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td" colspan="3"></td>
                </tr>
                <tr>
                    <td align="center">
                        排名
                    </td>
                    <td align="right">
                        浏览数
                    </td>
                    <td align="center">
                        文章
                    </td>
                </tr>
                <c:forEach var="item" items="${yesterdayStats.lastWeekTopViewedArticles}">
                    <tr>
                        <td align="center">
                                ${item.statSection.code}
                        </td>
                        <td align="right">
                                ${item.statValue}
                        </td>
                        <td align="center">
                            <a href="${item.extData.extValue01}" target="_blank">链接</a>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td" colspan="2"></td>
    </tr>
</table>
</td>
</tr>
</table>
</body>
</html>