<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>日志管理、登录日志列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/dhtmlxcalendar.css">
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/skins/dhtmlxcalendar_dhx_skyblue.css">
    <script src="/static/include/dhtmlxcalendar/dhtmlxcalendar.js"></script>
    <script src="/static/include/js/calendar.js" type="text/javascript"></script>
</head>
<script>
    var myCalendar;
    function doOnLoad() {
        myCalendar = new dhtmlXCalendarObject(["startTime", "endTime"]);
    }
</script>
<script language="javascript">
    function check() {
        var startTime = document.logform[0].startTime.value;
        var endTime = document.forms[0].endTime.value;
        startTime = new Date(startTime.replace(/-/g, "/"));
        endTime = new Date(endTime.replace(/-/g, "/"));
        if (startTime.getMonth() == endTime.getMonth()) {
            return true;
        }else {
            alert("请输入相同月");
            return false;
        }
    }
</script>
<body onload="doOnLoad();">


<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 登录日志管理 >> 查询登录日志</td>
    </tr>
    <tr>
        <td>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <form action="/log/loginloglist" name="loginform" method="POST">
                    <tr>
                        <td colspan="8" class="error_msg_td"></td>
                    </tr>
                    <tr>
                        <td height="1" colspan="8" class="default_line_td"></td>
                    </tr>

                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">登录人ID：</td>
                        <td nowrap class="edit_table_value_td">
                            <input name="userId" type="text" class="default_input_singleline" size="24" maxlength="64"
                                   id="userId" value="${entity.userId}">
                        </td>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">开始时间：</td>
                        <td nowrap class="edit_table_value_td">
                            <c:set var="startTime"><fmt:formatDate value="${entity.startTime}"
                                                                   pattern="yyyy-MM-dd"/></c:set>
                            <input name="startTime" type="text" class="default_input_singleline" size="8"
                                   maxlength="64"
                                   id="startTime" onfocus="setday(this);" value="${startTime}" >
                        </td>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">结束时间：</td>
                        <td nowrap class="edit_table_value_td">
                            <c:set var="endTime"><fmt:formatDate value="${entity.endTime}"  pattern="yyyy-MM-dd"/></c:set>
                            <input name="endTime" type="text" class="default_input_singleline" size="8" maxlength="64"
                                   id="endTime" onfocus="setday(this);" value="${endTime}" >
                        </td>
                        <td class="edit_table_defaulttitle_td"></td>
                        <td class="edit_table_value_td">
                            <input name="Submit" size="20" type="submit" class="default_button"
                                   value="&nbsp;查&nbsp;&nbsp;&nbsp;&nbsp;询&nbsp;"/>
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
                    <td nowrap align="center">登录人ID</td>
                    <td nowrap align="center">是否登录成功</td>
                    <td nowrap align="center"> 时间</td>
                    <td nowrap align="center"> 访问IP</td>
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
                        <td align="center">
                                ${ct.userId}
                        </td>
                        <td align="center">
                            <c:if test="${ct.success.code =='y'}">是</c:if>
                            <c:if test="${ct.success.code =='n'}">否</c:if>
                        </td>
                        <td align="center">
                                ${ct.loginTime}
                        </td>
                        <td align="center">
                                ${ct.accIp}
                        </td>
                        <td align="center" width="200px">
                                <%--<p:privilege name="log/">--%>
                            <a href="${ctx}/log/queryloginlog?lid=${ct.lid}">查看</a>
                                <%--</p:privilege>--%>
                        </td>
                    </tr>
                </c:forEach>
            </table>

            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="list_table_title_tr">
                        <LABEL>
                            <pg:pager url="/log/loginloglist"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">

                                <pg:param name="userId" value="${entity.userId}"/>
                                <pg:param name="startTime" value="${startTime}"/>
                                <pg:param name="endTime" value="${endTime}"/>

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