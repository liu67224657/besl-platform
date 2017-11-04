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

    <title>编辑人员管理</title>
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
                    <td height="22" class="page_navigation_td">>> 编辑管理 >>> 编辑人员管理 </td>
                </tr>
            </table>
            <br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr class="list_table_opp_tr">
                    <form action="/editor/user/page" method="post">
                     <input type="hidden" value="${editor.adminUno}"/>
                    <td>用户名称：<input type="text" type="text" class="default_input_singleline" size="8" name="username" value="${username}" id="username"/></td>
                    <td>有效状态：<select name="status">
                                     <option value="">请选择</option>
                                        <c:forEach var="status" items="${statusList}">
                                                <option value="${status.code}" <c:if test="${status.code eq currentStatusCode}">selected</c:if>><fmt:message key="def.validstatus.${status.code}.name" bundle="${def}"/></option>
                                        </c:forEach>
                                     </select></td>
                    <td><input type="submit" value="查询" class="default_button"/></td>
                    </form>
                </tr>
            </table>
            <br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">编辑人员管理</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="8" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td width="5%" align="center">选择</td>
                    <td width="15%" class="">序列号</td>
                    <td width="15%" class="">用户名称</td>
                    <td width="30%" align="left">用户描述</td>
                     <td width="5%" align="center">有效状态</td>
                    <td width="10%" class="">创建时间</td>
                    <td width="10%" class="">创建IP</td>
                    <td  class="">操作</td>
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
                        <td align="center"><input type="checkbox" name="itemids"/></td>
                        <td width="">${entry.adminUno}</td>
                        <td width="">${entry.editorName}</td>
                        <td width="">${entry.editorDesc}</td>
                        <td align="center"><fmt:message key="def.validstatus.${entry.validStatus.code}.name" bundle="${def}"/></td>
                        <td width=""><fmt:formatDate value="${entry.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td width="">${entry.createIp}</td>
                         <td >
                             <p:privilege name="/editor/user/del">
                            <a href="/editor/user/del?adminuno=${entry.adminUno}">删除</a>
                             </p:privilege>
                              <p:privilege name="/editor/user/modifypage">
                             <a href="/editor/user/modifypage?adminuno=${entry.adminUno}">编辑</a>
                              </p:privilege>
                              <p:privilege name="/editor/user/stat/content">
                             <a href="/editor/user/stat/content?adminuno=${entry.adminUno}">文章统计</a>
                              </p:privilege>
                              <p:privilege name="/editor/user/stat/game">
                             <a href="/editor/user/stat/game?adminuno=${entry.adminUno}">条目统计</a>
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
                            <pg:pager url="/editor/user/page"
                                      items="${page.totalRows}" isOffset="true" maxPageItems="${page.pageSize}" export="offset, currentPageNumber=pageNumber"
                                      scope="request">
                                <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                <pg:param name="items" value="${page.totalRows}"/>
                                <pg:param name="status" value="${currentStatusCode}"/>
                                <pg:param name="username" value="${username}"/>
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