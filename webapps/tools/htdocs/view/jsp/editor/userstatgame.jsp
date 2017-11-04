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
            myCalendar = new dhtmlXCalendarObject(["statdate"]);
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
                    <td height="22" class="page_navigation_td">>> 编辑统计 >>> 编辑人员管理  >>> 编辑人员文章统计 </td>
                </tr>
            </table>
            <br>
            <table width="30%" border="0" cellspacing="0" cellpadding="0">
                <tr class="list_table_opp_tr">
                    <form action="/editor/user/stat/game" method="post">
                     <input type="hidden" name="adminuno" value="${editor.adminUno}"/>
                    <td width="">统计时间：<input type="text" type="text" class="default_input_singleline" size="15" name="statdate" value="<fmt:formatDate value="${statDate}" pattern="yyyy-MM-dd"/>" id="statdate"/></td>
                    <td>时间类型：<select name="datetype">
                                        <c:forEach var="statDateType" items="${dateTypeList}">
                                                <option value="${statDateType.code}" <c:if test="${statDateType.code eq dateType}">selected</c:if>><fmt:message key="def.stats.date.type.${statDateType.code}.name" bundle="${def}"/></option>
                                        </c:forEach>
                     </select></td>
                    <td><input type="submit" value="查询" class="default_button"/></td>
                    </form>
                </tr>
            </table>
            <br>
            <c:if test="${fn:length(errorMsg)>0}">
                <b style="color:red;"><fmt:message key="${errorMsg}" bundle="${error}"/></b>
            </c:if>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">用户：${editor.editorName}文章详细表</td>
                </tr>
            </table>
            <table width="60%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="8" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td width="" align="left">游戏链接</td>
                    <td width="20%" align="left">发布时间</td>
                     <td width="20%" align="center">PV</td>
                    <td width="20%" align="center">UV</td>

                </tr>
                <c:forEach var="dto" varStatus="st" items="${list}">
                    <tr>
                    <td ><a href="${dto.linkurl}" target="_blank">${dto.linkurl}</a></td>
                    <td ><fmt:formatDate value="${dto.createDate}" pattern="yyyy-MM-dd HH:mm:dd"/></td>
                    <td align="center">${dto.pv}</td>
                    <td align="center">${dto.uv}</td>
                    </tr>
                </c:forEach>
            </table>
            <br/>
        </td>
    </tr>
</table>
</body>
</html>