<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/dhtmlxcalendar.css">
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/skins/dhtmlxcalendar_dhx_skyblue.css">
    <script src="/static/include/dhtmlxcalendar/dhtmlxcalendar.js"></script>
    <script language="JavaScript" type="text/JavaScript">
        $().ready(function() {
            doOnLoad();
        });
        var myCalendar;
        function doOnLoad() {
            myCalendar = new dhtmlXCalendarObject(["startDate", "endDate"]);
        }
    </script>

    <title>文章管理</title>
    <style type="text/css">
        td, td div {
            overflow: hidden;
            text-overflow: ellipsis; /* for IE */
            -moz-text-overflow: ellipsis; /* for Firefox,mozilla */
            white-space: nowrap;
        }
    </style>
</head>

<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="100%" valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="22" class="page_navigation_td">>> 编辑管理 >>> 文章管理 >>> ${editor.editorName}</td>
                </tr>
            </table>
            <br>
            <table width="40%" border="0" cellspacing="0" cellpadding="0">
                <tr class="list_table_opp_tr">
                    <form action="/editor/content/page" method="post">
                     <input type="hidden" value="${editor.adminUno}"/>
                    <td width="">开始时间：<input type="text" type="text" class="default_input_singleline" size="8" name="startDate" value="<fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd"/>" id="startDate"/></td>
                    <td>结束时间：<input type="text" type="text" class="default_input_singleline" size="8" name="endDate" value="<fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd"/>" id="endDate"/></td>
                    <td>有效状态：<select name="status">
                                     <option value="">请选择</option>
                                        <c:forEach var="status" items="${statusList}">
                                                <option value="${status.code}" <c:if test="${status.code eq currentStatusCode}">selected</c:if>><fmt:message key="def.validstatus.${status.code}.name" bundle="${def}"/></option>
                                        </c:forEach>
                     </select></td>
                    <td><input type="submit" value="查询" class="default_button"/></td>
                    </form>
                    <td>
                         &nbsp;&nbsp;&nbsp;&nbsp;<a href="/editor/content/createpage">录入文章</a>
                    </td>
                </tr>
            </table>
            <br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">文章管理</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="8" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td width="50" class="">选择</td>
                    <td width="70" class="">序列号</td>
                    <td width="100" class="">文章地址</td>
                    <td width="100" class="">文章类型</td>
                    <td width="70" align="center">状态</td>
                    <td width="80" class="">创建时间</td>
                    <td width="100" class="">创建IP</td>
                    <td width="100" class="">操作</td>
                </tr>
                <c:forEach var="entry" varStatus="st" items="${list}">
                    <tr class="<c:choose>
                        <c:when test="${st.index % 2 == 0}">
                           list_table_opp_tr
                        </c:when>
                        <c:otherwise>
                            list_table_even_tr
                        </c:otherwise>
                        </c:choose>">
                        <td width=""><input type="checkbox" name="itemids"/></td>
                        <td width="">${entry.itemNo}</td>
                        <td width="">${entry.itemSrcNo}</td>
                        <td width=""><fmt:message key="def.editor.${entry.itemSubType}.name" bundle="${def}"/></td>
                        <td align="center"><fmt:message key="def.validstatus.${entry.validStatus.code}.name"
                                                        bundle="${def}"/></td>
                        <td width=""><fmt:formatDate value="${entry.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td width="">${entry.createIp}</td>
                        <td >
                            <p:privilege name="/editor/content/del">
                            <a href="/editor/content/del?itemno=${entry.itemNo}">删除</a>
                            </p:privilege>
                            <p:privilege name="/editor/content/modifypage">
                            <a href="/editor/content/modifypage?itemno=${entry.itemNo}">编辑</a>
                            </p:privilege>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            <br/>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td align="center"  class="default_line_td">
                        <LABEL>
                            <pg:pager url="/editor/content/page"
                                      items="${page.totalRows}" isOffset="true" maxPageItems="${page.pageSize}" export="offset, currentPageNumber=pageNumber"
                                      scope="request">
                                <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                <pg:param name="items" value="${page.totalRows}"/>
                                <pg:param name="status" value="${currentStatusCode}"/>
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