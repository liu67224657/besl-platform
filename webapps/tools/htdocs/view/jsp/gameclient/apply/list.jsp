<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台数据管理,APP应用广告列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>

    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/dhtmlxcalendar.css">
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/skins/dhtmlxcalendar_dhx_skyblue.css">
    <script src="/static/include/dhtmlxcalendar/dhtmlxcalendar.js"></script>
    <style type="text/css">
            .td_cent{text-align:center;vertical-align:middle};
        </style>

    <script language="JavaScript" type="text/JavaScript">
        $().ready(function() {
            doOnLoad();
        });

        var myCalendar;
        function doOnLoad() {
            myCalendar = new dhtmlXCalendarObject(["startDate", "endDate"]);
        }
    </script>
</head>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
    <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷新手游画报管理 >> 广告应用列表</td>
</tr>
<tr>
<td height="100%" valign="top"><br>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <c:if test="${fn:length(errorMsg)>0}">
        <tr>
            <td height="1" colspan="14" class="error_msg_td">${errorMsg}</td>
        </tr>
    </c:if>
    <tr>
        <td height="1" class="default_line_td"></td>
    </tr>
</table>
<table width="80%" border="0" cellspacing="0" cellpadding="0">
    <form action="/advertise/app/apply/list" method="post" id="form_submit_search">
        <tr>
            <td width="80" align="center">搜索条件</td>
            <td>
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td width="110" align="right" class="edit_table_defaulttitle_td td_cent">应用广告名称：</td>
                        <td>
                            <input name="publishName" type="text" size="25"/>
                        </td>
                        <td width="80" align="right" class="edit_table_defaulttitle_td td_cent">状态：</td>
                    <td class="edit_table_value_td">
                        <select name="removestatus" class="default_select_single">
                                    <option value="">--全部--</option>
                                    <option value="n">启用</option>
                                    <option value="y">停用</option>
                                </select></td>
                    <td width="80" align="right" class="edit_table_defaulttitle_td td_cent">所属APP：</td>
                    <td class="edit_table_value_td">
                        <select name="appkey">
                                 <option value="">--全部--</option>
                                <c:forEach var="app" items="${applist}">
                                    <c:if test="${app.appId==appkey}">
                                        <option value="${app.appId}" selected="selected">${app.appName}</option>
                                    </c:if>
                                    <c:if test="${app.appId!=appkey}">
                                        <option value="${app.appId}">${app.appName}</option>
                                    </c:if>
                                </c:forEach>
                            </select>
                      </td>
                    </tr>

                </table>
            </td>
            <td width="80" align="center">
                <input name="Button" type="submit" class="default_button" value=" 搜索 ">
            </td>
        </tr>
    </form>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td height="1" class="default_line_td"></td>
    </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td>
            <form method="post" action="/advertise/app/apply/createpage?appkey=${appkey}">
                <table>
                    <tr>
                        <td>
                            <input type="submit" name="button" class="default_button" value="添加APP应用广告"/>
                        <td>
                    <tr>
                </table>
            </form>
        </td>
    </tr>
</table>
<table width="100%" border="0" cellspacing="1" cellpadding="0"style="vertical-align:middle; text-align:center;">
    <tr>
        <td height="1" colspan="11" class="default_line_td"></td>
    </tr>
    <tr class="list_table_title_tr">
        <td nowrap align="center" width="80">广告位ID</td>
        <td nowrap align="center" width="150">广告位名称</td>
        <td nowrap align="center" width="200">广告位描述</td>
        <td nowrap align="center" width="50">素材ID</td>
        <td nowrap align="center" width="100">所属APP</td>
        <td nowrap align="center" width="100">渠道</td>
        <td nowrap align="center" width="100">开始时间</td>
        <td nowrap align="center" width="100">结束时间</td>
        <td nowrap align="center" width="100">创建时间</td>
        <td nowrap align="center" width="100">创建人</td>
        <td nowrap align="center" width="150">操作</td>
    </tr>
    <tr>
        <td height="1" colspan="11" class="default_line_td"></td>
    </tr>
    <c:choose>
        <c:when test="${list.size() > 0}">
            <c:forEach items="${list}" var="ad" varStatus="st">
                <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                    <td nowrap>${ad.publishId}</td>
                    <td nowrap>${ad.publishName}</td>
                    <td nowrap>${ad.publishDesc}</td>
                    <td nowrap><a href="/advertise/app/appinfo?advertiseid=${ad.advertiseId}">${ad.advertiseId}</a></td>
                    <td nowrap>${ad.appkey}</td>
                    <td nowrap><c:if test="${ad.channel != null}"><fmt:message key="joymeapp.channel.type.${ad.channel.code}" bundle="${def}"/></c:if></td>
                    <td nowrap>${ad.startTime}</td>
                    <td nowrap>${ad.endTime}</td>
                    <td nowrap>${ad.createTime}</td>
                    <td nowrap>${ad.createUser}</td>

                    <td nowrap>
                         <a href="/advertise/app/apply/modifypage?publishId=${ad.publishId}&pager.offset=${page.startRowIdx}&appkey=${appkey}">编辑</a>
                        <c:choose>
                            <c:when test="${ad.removeStatus.code eq 'n'}">
                                    <a href="/advertise/app/apply/modify?publishId=${ad.publishId}&removestatus=y&pager.offset=${page.startRowIdx}&appkey=${appkey}">停用</a>
                            </c:when>
                            <c:when test="${ad.removeStatus.code eq 'y'}">
                                 <a href="/advertise/app/apply/modify?publishId=${ad.publishId}&removestatus=n&pager.offset=${page.startRowIdx}&appkey=${appkey}">启用</a>
                            </c:when>
                        </c:choose>
                        &nbsp;
                    </td>

                </tr>
            </c:forEach>
            <tr>
                <td height="1" colspan="11" class="default_line_td"></td>
            </tr>
        </c:when>
        <c:otherwise>
            <tr>
                <td colspan="11" class="error_msg_td">暂无数据!</td>
            </tr>
        </c:otherwise>
    </c:choose>
    <tr>
        <td colspan="11" height="1" class="default_line_td"></td>
    </tr>
    <c:if test="${page.maxPage > 1}">
        <tr class="list_table_opp_tr">
            <td colspan="11">
                <!--
                <c:set var="startDateStr">
                    <fmt:formatDate value="${queryStartTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                </c:set>
                <c:set var="endDateStr">
                    <fmt:formatDate value="${queryEndTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                </c:set>
                -->
                <LABEL>
                    <pg:pager url="/advertise/app/apply/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="publishName" value="${publishName}"/>
                                <pg:param name="removestatus" value="${removestatus}"/>
                                <pg:param name="appkey" value="${appkey}"/>
                                <pg:param name="currentPageNumber" value="${page.curPage}"/>
                                <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                <pg:param name="items" value="${page.totalRows}"/>
                                <%@ include file="/WEB-INF/jsp/toolspg.jsp" %>
                    </pg:pager>
                </LABEL>
            </td>
        </tr>
    </c:if>
</table>
</td>
</tr>
</table>
</body>
</html>