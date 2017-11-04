<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>日志管理、操作日志列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/dhtmlxcalendar.css">
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/skins/dhtmlxcalendar_dhx_skyblue.css">
    <script src="/static/include/dhtmlxcalendar/dhtmlxcalendar.js"></script>
    <script src="/static/include/js/calendar.js" type="text/javascript"></script>
    <script>
        var myCalendar;
        function doOnLoad() {
            myCalendar = new dhtmlXCalendarObject(["startTime", "endTime"]);
        }
    </script>
    <script language="javascript">
        function checkForm() {
            var startTime = document.forms[0].startTime.value;
            var endTime = document.forms[0].endTime.value;

            if (startTime && endTime) {
                startTime = new Date(startTime.replace(/-/g, "/"));
                endTime = new Date(endTime.replace(/-/g, "/"));
                if (startTime.getMonth() == endTime.getMonth()) {
                    document.form1.submit();
                    return true;
                } else {
                    alert("请输入相同月");
                    return false;
                }
            }
            document.form1.submit();
            return true;
        }
    </script>
    <style type="text/css">
        .truncate{
            overflow: hidden;
            text-overflow:ellipsis; /* for IE */
            -moz-text-overflow: ellipsis; /* for Firefox,mozilla */
            white-space: nowrap;
        }
    </style>
</head>

<body onload="doOnLoad();">


<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 日志管理 >> 查询操作日志</td>
    </tr>
    <tr>
        <td>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <form action="/log/loglist" name="form1" method="POST">
                    <tr>
                        <td colspan="8" class="error_msg_td"></td>
                    </tr>
                    <tr>
                        <td height="1" colspan="8" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td align="center" rowspan="2">搜索条件</td>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">操作对象ID：</td>
                        <td nowrap class="edit_table_value_td">
                            <input name="opUserId" type="text" class="default_input_singleline" size="24" maxlength="64"
                                   id="opUserId" value="${entity.opUserId}"/>
                        </td>
                        <td class="edit_table_defaulttitle_td" align="right">操作结果：</td>
                        <td nowrap class="edit_table_value_td">
                            <input name="opafter" type="text" class="default_input_singleline" size="24" maxlength="64"
                                   value="${entity.opAfter}"/>
                        </td>
                        <td align="center" rowspan="2">
                            <input name="button" size="20" type="button" class="default_button"
                                   value="&nbsp;查&nbsp;&nbsp;&nbsp;&nbsp;询&nbsp;"
                                   onclick="checkForm()">
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">开始时间：</td>
                        <td nowrap class="edit_table_value_td">
                            <c:set var="startTime"><fmt:formatDate value="${entity.startTime}"
                                                                   pattern="yyyy-MM-dd"/></c:set>
                            <input  name="startTime" type="text" class="default_input_singleline" size="8"
                                    maxlength="64"
                                    id="startTime" onfocus="setday(this);" value="${startTime}" >
                        </td>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">结束时间：</td>
                        <td nowrap class="edit_table_value_td">
                            <c:set var="endTime"><fmt:formatDate value="${entity.endTime}"
                                                                 pattern="yyyy-MM-dd"/></c:set>
                            <input name="endTime" type="text" class="default_input_singleline" size="8" maxlength="64"
                                   id="endTime" onfocus="setday(this);" value="${endTime}" >
                        </td>
                    </tr>
                    <tr>
                        <td height="1" colspan="8" class="default_line_td"></td>
                    </tr>
                </form>
            </table>

        </td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">操作日志查询</td>
                </tr>
            </table>

            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>

            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr class="list_table_title_tr">
                    <td nowrap align="center">操作人ID</td>
                    <td nowrap align="center">操作模块</td>
                    <td nowrap align="center">操作方法</td>
                    <td nowrap align="center"> 访问IP</td>
                    <td nowrap align="center"> 时间</td>
                    <td nowrap align="center">操作后</td>
                    <td nowrap align="center">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="9" class="default_line_td"></td>
                </tr>
                <c:forEach items="${rows}" var="ct" varStatus="st">
                    <tr class="
                        <c:choose>
                        <c:when test="${st.index % 2 == 0}">
                           list_table_opp_tr
                        </c:when>
                        <c:otherwise>
                            list_table_even_tr
                        </c:otherwise>
                        </c:choose>">
                        <td align="center">${ct.opUserId}</td>
                        <td align="center"><fmt:message key="${ct.operType.module}" bundle="${toolsProps}"></fmt:message></td>
                        <td align="center"><fmt:message key="${ct.operType.module}.${ct.operType.oper}" bundle="${toolsProps}"></fmt:message></td>
                        <td align="center">${ct.opIp}</td>
                        <td align="center">${ct.opTime}</td>
                        <td align="center" class="truncate" style="width: 220px;text-align: left;"><p style="overflow:hidden;white-space:nowrap;text-overflow:ellipsis;width:450px;">${ct.opAfter}</p></td>
                        <td align="center" width="60px">
                                <%--<p:privilege name="log/">--%>
                            <a href="${ctx}/log/querylog?lid=${ct.lid}&opTime=${ct.opTime}">查看</a>
                                <%--</p:privilege>--%>
                        </td>
                    </tr>
                </c:forEach>
            </table>

            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="list_table_title_tr">
                        <LABEL>
                            <pg:pager url="/log/loglist"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">

                                <pg:param name="srcId" value="${entity.srcId}"/>
                                <pg:param name="startTime" value="${startTime}"/>
                                <pg:param name="endTime" value="${endTime}"/>
                                <pg:param name="opafter" value="${entity.opAfter}"/>
                                <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                <pg:param name="items" value="${page.totalRows}"/>
                                <%@ include file="/WEB-INF/jsp/toolspg.jsp" %>
                            </pg:pager>
                        </LABEL>
                    </td>
                </tr>
            </table>

        </td>
    </tr>
</table>
</body>
</html>