<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>统计数据-列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script src="/static/include/js/default.js" type="text/javascript"></script>
    <script src="/static/include/openflashchart/swfobject.js" type="text/javascript"></script>
    <script type="text/javascript">
        swfobject.embedSWF(
                "/static/include/openflashchart/open-flash-chart.swf", "line_chart", "1000", "400",
                "9.0.0", "",
                {"data-file":escape("/stats/data/line/day?domaincode=${domainCode}&sectioncode=${sectionCode}&fromdate=2012-12-01&todate=2013-1-1&x=" + Math.random())});
    </script>
</head>

<body>
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
                        ${domainCode} - ${sectionCode}
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td" colspan="3"></td>
                </tr>
                <tr>
                    <td valign="top">
                        <div id="line_chart"></div>
                    </td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td" colspan="3"></td>
                </tr>
            </table>

        </td>
    </tr>
</table>
</body>
</html>